package com.tinhvan.hd.converter.controller;

import com.documents4j.api.IConverter;
import com.tinhvan.hd.converter.dto.DataDto;
import com.tinhvan.hd.converter.utils.XDocUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.*;

@RestController
@RequestMapping(value = "/api/v1/converter")
public class Controller {

    @Autowired
    private IConverter converter;

    /*@Qualifier("ExecutorBean")
    @Autowired
    private Executor executor;*/

    @PostMapping(value = "/word2pdf")
    @Async
    public ResponseEntity<List<byte[]>> word2pdf(@RequestParam("files") MultipartFile[] files) throws InterruptedException {
        long start = new Date().getTime();
        List<byte[]> responses = new ArrayList<>();
        System.out.println(files.length);
        //IConverter converter = RemoteConverter.make("http://192.168.75.104:8811");
        /*.builder()
                .workerPool(4, 4, 5, TimeUnit.SECONDS)
                .requestTimeout(10, TimeUnit.SECONDS)
                .baseUri("http://192.168.75.104:8811").build();*/
        XDocUtils utils = new XDocUtils();
        Executor executor = Executors.newScheduledThreadPool(files.length);//files.length);
        CompletionService<DataDto> completionService = new ExecutorCompletionService<>(executor);
        for (int i = 0; i < files.length; i++) {
            int index = i;
            responses.add(index, null);
            MultipartFile file = files[index];
            completionService.submit(() ->  utils.convertPDF(converter, index, file));
        }

        int received = 0;
        boolean errors = false;
        List<DataDto> lstDto = new ArrayList<>();
        while (received < files.length && !errors) {
            Future<DataDto> baseConvert = completionService.take();
            try {
                lstDto.add(baseConvert.get());
                received++;
            } catch (Exception e) {
                e.printStackTrace();
                errors = true;
            }
        }
        try {
            lstDto.forEach(dto -> {
                if (dto.getBytes() != null) {
                    responses.set(dto.getIndex(), dto.getBytes());
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().build();
        } finally {
            //converter.shutDown();
        }
        long end = new Date().getTime();
        System.out.println("word2pdf: " + (end - start));
        return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION).contentType(MediaType.APPLICATION_JSON).body(responses);
    }
}
