package com.tinhvan.hd.converter.utils;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.*;

import com.documents4j.api.DocumentType;
import com.documents4j.api.IConverter;
import com.documents4j.job.LocalConverter;
import com.tinhvan.hd.converter.dto.DataDto;

public class Test {
    public static void main(String[] args) throws InterruptedException {
       long start =  new Date().getTime();
        Executor executor = Executors.newFixedThreadPool(6);
        CompletionService<String> completionService = new ExecutorCompletionService<>(executor);

        for (int i = 0;i < 6;i++) {

            File inputWord = new File("src/main/resources/PHU_LUC_HOP_DONG_THE_CHAP.docx");
            File outputFile = new File("src/main/resources/" + i + "_Test_out.pdf");
            completionService.submit(() -> {
                return converter(inputWord, outputFile);
            });
        }
        int received = 0;
        boolean errors = false;
        List<String> lstDto = new ArrayList<>();
        while (received < 6 && !errors) {
            Future<String> baseConvert = completionService.take();
            try {
                lstDto.add(baseConvert.get());
                received++;
            } catch (Exception e) {
                e.printStackTrace();
                errors = true;
            }
        }
        long end =  new Date().getTime();
        System.out.println("Total: " + (end - start)/1000);
    }

    public static String converter(File inputWord,File outputFile) {
        try  {
            InputStream docxInputStream = new FileInputStream(inputWord);
            OutputStream outputStream = new FileOutputStream(outputFile);
            IConverter converter = LocalConverter.builder()
                    .workerPool(4, 4, 5, TimeUnit.SECONDS)
                    .processTimeout(5, TimeUnit.SECONDS)
                    .build();
            converter.convert(docxInputStream).as(DocumentType.DOCX).to(outputStream).as(DocumentType.PDF).execute();
            outputStream.close();

            System.out.println("success");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }
}
