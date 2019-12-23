//package com.tinhvan.hd.controller;
//
//import com.fasterxml.jackson.core.type.TypeReference;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.google.common.collect.Iterators;
//import com.tinhvan.hd.base.*;
//import com.tinhvan.hd.dto.ReportRequest;
//import com.tinhvan.hd.dto.UriRequest;
//import com.tinhvan.hd.dto.UriResponse;
//import com.tinhvan.hd.entity.Contract;
//import com.tinhvan.hd.service.ContractService;
//import com.tinhvan.hd.utils.ContractUtils;
//import com.tinhvan.hd.utils.XlsxUtils;
//import org.apache.commons.io.FileUtils;
//import org.apache.commons.io.FilenameUtils;
//import org.apache.poi.ss.usermodel.Cell;
//import org.apache.poi.ss.usermodel.Row;
//import org.apache.poi.ss.usermodel.Sheet;
//import org.apache.poi.ss.usermodel.Workbook;
//import org.apache.poi.xssf.usermodel.XSSFWorkbook;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.core.ParameterizedTypeReference;
//import org.springframework.data.domain.PageRequest;
//import org.springframework.data.domain.Pageable;
//import org.springframework.data.domain.Sort;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//
//import java.io.*;
//import java.text.SimpleDateFormat;
//import java.util.*;
//
//@CrossOrigin(origins = "*", allowedHeaders = "*")
//@RestController
//@RequestMapping("/api/v1/contract/report")
//public class ReportController extends HDController {
//
//    @Autowired
//    private ContractService contractService;
//
//
//    @PostMapping("/report")
//    public ResponseEntity<?> test(@RequestBody RequestDTO<ReportRequest> req) {
//
//        ReportRequest reportRequest = req.init();
//
//        try {
//            //get file template from s3
//            File file = invokeFileHandlerS3_download(new UriRequest(reportRequest.getFileTemplate()));
//            if (file == null)
//                return notFound(1426, "file template not found");
//
//            //find list report
//            Pageable pageable = PageRequest.of(0, 10, Sort.by("id").descending());
//            List<Contract> contracts = contractService.getListContractInfo(pageable);
//            /*if(contracts==null||contracts.size()==0){
//
//            }*/
//            //create file template on local
//            FileInputStream fileIS = new FileInputStream(file);
//            Workbook workbookIn = new XSSFWorkbook(fileIS);
//            Sheet sheetIn = workbookIn.getSheetAt(0);
//            Iterator<Row> rowIteratorIn = sheetIn.iterator();
//            int sizeTemp = Iterators.size(rowIteratorIn) - 2;
//            int sizeIndex = contracts.size() - 1;
//            for (int i = 2; i < sizeIndex; i++) {
//                XlsxUtils.copyRow(workbookIn, sheetIn, i, i + sizeTemp);
//            }
//            //fill data
//            sheetIn = workbookIn.getSheetAt(0);
//            rowIteratorIn = sheetIn.iterator();
//            while (rowIteratorIn.hasNext()) {
//                Row rowIn = rowIteratorIn.next();
//                int rowNum = rowIn.getRowNum();
//                Iterator<Cell> cellIteratorIn = rowIn.iterator();
//                //fill data title
//                if (rowNum == 0) {
//                    while (cellIteratorIn.hasNext()) {
//                        Cell cellIn = cellIteratorIn.next();
//                        sheetIn.autoSizeColumn(cellIn.getColumnIndex());
//                        String header = cellIn.getStringCellValue() + " " + new SimpleDateFormat("MM").format(Calendar.getInstance().getTime());
//                        cellIn.setCellValue(header);
//                    }
//                }
//                /**
//                 * if rowNum = 1 that is row header
//                 * nothing to do
//                 */
//                //fill data content
//                if (rowNum >= 2) {
//                    int stt = rowNum - 2;
//                    Contract contract = contracts.get(stt);
//                    while (cellIteratorIn.hasNext()) {
//                        Cell cellIn = cellIteratorIn.next();
//                        sheetIn.autoSizeColumn(cellIn.getColumnIndex());
//                        //STT
//                        if (cellIn.getColumnIndex() == 0)
//                            cellIn.setCellValue(stt + 1);
//                        if (cellIn.getColumnIndex() == 1)
//                            cellIn.setCellValue(contract.getLendingCoreContractId());
//                        if (cellIn.getColumnIndex() == 2)
//                            cellIn.setCellValue(ContractUtils.convertStatus(contract.getStatus()));
//                        if (cellIn.getColumnIndex() == 3)
//                            cellIn.setCellValue(contract.getLoanAmount().intValue());
//                    }
//                }
//            }
//
//            FileOutputStream fileOS = new FileOutputStream(file.getName());
//            workbookIn.write(fileOS);
//            workbookIn.close();
//            fileIS.close();
//
//
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        return ok();
//    }
//
//    @Value("${service.filehandler.endpoint}")
//    private String urlFileHandlerRequest;
//    private ObjectMapper mapper = new ObjectMapper();
//    private Invoker invoker = new Invoker();
//
//    File invokeFileHandlerS3_download(UriRequest uriRequest) {
//        try {
//            ResponseDTO<Object> dto = invoker.call(urlFileHandlerRequest + "/download", uriRequest,
//                    new ParameterizedTypeReference<ResponseDTO<Object>>() {
//                    });
//            if (dto != null && dto.getCode() == HttpStatus.OK.value()) {
//                try {
//                    UriResponse fileResponse = mapper.readValue(mapper.writeValueAsString(dto.getPayload()),
//                            new TypeReference<UriResponse>() {
//                            });
//                    if (!HDUtil.isNullOrEmpty(fileResponse.getData())) {
//                        String fileNameIn = "HD_REPORT_" + UUID.randomUUID() + "_" + new SimpleDateFormat("yyyyMMdd").format(Calendar.getInstance().getTime()) + "."+ FilenameUtils.getExtension(uriRequest.getUri());
//                        FileUtils.writeByteArrayToFile(new File(fileNameIn), Base64.getDecoder().decode(fileResponse.getData()));
//                        return new File(fileNameIn);
//
//                    }
//                } catch (IOException e) {
//                    Log.error(e.getMessage());
//                    e.printStackTrace();
//                }
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return null;
//    }
//}
