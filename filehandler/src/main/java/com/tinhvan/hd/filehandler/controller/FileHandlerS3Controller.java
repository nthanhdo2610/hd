package com.tinhvan.hd.filehandler.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.itextpdf.text.Document;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfImportedPage;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfWriter;
import com.tinhvan.hd.filehandler.exception.BadRequestException;
import com.tinhvan.hd.filehandler.exception.InternalServerErrorException;
import com.tinhvan.hd.filehandler.payload.*;
import com.tinhvan.hd.filehandler.service.AmazonS3ClientService;
import com.tinhvan.hd.filehandler.utils.BaseUtil;
import com.tinhvan.hd.filehandler.utils.XDocUtils;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.util.StopWatch;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = "/api/v1/filehandler")
public class FileHandlerS3Controller extends BaseController {

    @Autowired
    private AmazonS3ClientService amazonS3ClientService;

    @Value("${com.tinhvan.hd.contract}")
    private String contractURI;
    private int loopIndex = 0;

    /*@PostMapping("/word2pdf")
    public List<String> word2pdf(@RequestBody List<String> files) {
        loopIndex++;
        files = Arrays.asList(
                "OFFLINE_ONLINE_SVMCECEB_Hop_Dong_Tin_Dung.docx",
                "OFFLINE_ONLINE_ED_Hop_Dong_Tin_Dung_Ap_Dung_Chung_Toan_Bo_ED_FU_TP_SL.docx",
                "ONLINE_CASH_CLFS_Hop_Dong_Tin_Dung.docx",
                "OFFLINE_ONLINE_CLFS_Hop_Dong_Tin_Dung.docx",
                "MCSVEC_Hop_dong_the_chap.docx"*//*,
                "PHU_LUC_HOP_DONG_THE_CHAP.docx",
                "Mau_Giay_yeu_cau_bao_hiem.docx",
                "Dieu_khoan_va_Dieu_kien_Chuong_trinh_hoan_tien.docx"*//*);
        List<File> lstDocX = new ArrayList<>();
        for (int i = 0; i < files.size(); i++) {
            String template = files.get(i);
            lstDocX.add(new File("HopDongFinal_merged/" + template));
        }
        StopWatch sw = new StopWatch("word2pdf");
        List<File> lstPdf = new ArrayList<>();
        try {
            //System.out.println("loopIndex:" + loopIndex);
            sw.start("call converter v1.4");
            lstPdf = XDocUtils.converter("http://192.168.75.205/dev/api/v1/contract/word2pdf", lstDocX);
            //lstPdf = XDocUtils.converter("http://192.168.75.129:8806/api/v1/contract/word2pdf_file", lstDocX);
            //lstPdf = XDocUtils.converter("http://192.168.75.104:8810/api/v1/converter/word2pdf", lstDocX);
            *//*if (loopIndex % 5 == 0)
                lstPdf = XDocUtils.converter("http://192.168.75.104:8810/api/v1/converter/word2pdf", lstDocX);
            if (loopIndex % 5 == 1)
                lstPdf = XDocUtils.converter("http://192.168.75.104:8811/api/v1/converter/word2pdf", lstDocX);
            if (loopIndex % 5 == 2)
                lstPdf = XDocUtils.converter("http://192.168.75.104:8812/api/v1/converter/word2pdf", lstDocX);
            if (loopIndex % 5 == 3)
                lstPdf = XDocUtils.converter("http://192.168.75.104:8813/api/v1/converter/word2pdf", lstDocX);
            if (loopIndex % 5 == 4)
                lstPdf = XDocUtils.converter("http://192.168.75.104:8814/api/v1/converter/word2pdf", lstDocX);
            if (loopIndex== 5 )
                loopIndex = 0;*//*
            sw.stop();
        } catch (Exception e) {
            e.printStackTrace();
            throw new InternalServerErrorException();
        } finally {
            if (lstPdf != null) {
                lstPdf.forEach(pdf -> {
                    if (pdf != null)
                        pdf.delete();
                });
            }
            System.out.println(sw.prettyPrint());
        }
        return null;
    }*/


    /**
     * Upload file to aws s3 bucket
     *
     * @param req object FileRequestList contain info file upload request
     * @return object FileResponseList contain result after upload file successfully
     */
    @PostMapping(value = "/upload")
    public ResponseEntity<?> uploadMultipleFiles(@RequestBody RequestDTO<FileRequestList> req) {
        FileRequestList fileRequestList = req.init();

        FileResponseList fileResponse = new FileResponseList();

        fileResponse.setFiles(fileRequestList.getFiles().stream().map(file -> {
            String key = "";
            if (!BaseUtil.isNullOrEmpty(file.getPath()))
                key += file.getPath() + "/";
            if (!BaseUtil.isNullOrEmpty(file.getId()))
                key += file.getId() + "/";
            if (!BaseUtil.isNullOrEmpty(file.getType()))
                key += file.getType() + "/";

            FileResponseList.FileRep rep = this.amazonS3ClientService.uploadFileToS3Bucket(file.getContentType(), file.getData(), key, file.isEnablePublic());
            if (!BaseUtil.isNullOrEmpty(file.getFileOld()) && !file.getPath().equals("customers"))
                this.amazonS3ClientService.deleteFileFromS3Bucket(file.getFileOld());
            return rep;
        }).collect(Collectors.toList()));
        return ok(fileResponse);
    }

    /**
     * Download file from uri of aws s3 bucket
     *
     * @param req object UriRequest contain uri s3
     * @return object UriResponse contain data of file result
     */
    @PostMapping(value = "/download")
    public ResponseEntity<?> downloadFile(@RequestBody RequestDTO<UriRequest> req) {
        UriRequest fileRequest = req.init();
        File file = amazonS3ClientService.downloadFileFromS3Bucket(fileRequest.getUri());
        try {
            byte[] bytes = BaseUtil.loadFile(file);
            String encodedString = Base64.getEncoder().encodeToString(bytes);
            return ok(new UriResponse(encodedString));
        } catch (IOException e) {
            //e.printStackTrace();
            return badRequest(1119, e.getMessage());
        } finally {
            file.delete();
        }
    }

    /**
     * Delete a file on aws s3 bucket
     *
     * @param req object FileResponseList contain uri request
     * @return http status code
     */
    @PostMapping(value = "/delete")
    public ResponseEntity<?> deleteFile(@RequestBody RequestDTO<FileResponseList> req) {
        FileResponseList fileRequest = req.init();
        fileRequest.getFiles().forEach(file -> this.amazonS3ClientService.deleteFileFromS3Bucket(file.getUri()));
        return ok();
    }

    @Value("${aws.s3.contract.file-signed}")
    private String uriBackground;

    /**
     * Create contract file
     *
     * @param req object ContractGenerateFileRequest contain info require to create contract files
     * @return object GenerateContractFileResponse contain files result
     */
    @PostMapping(value = "/contract/generate")
    public ResponseEntity<?> generateDOCX(@RequestBody RequestDTO<ContractGenerateFileRequest> req) {
        StopWatch sw = new StopWatch("/contract/generate");
        ContractGenerateFileRequest fileRequest = req.init();
        //System.out.println(fileRequest.toString());
        SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd");
        if (fileRequest == null)
            throw new BadRequestException();
        if (fileRequest.getFileOlds() != null && fileRequest.getFileOlds().size() > 0) {
            sw.start("delete File old FromS3Bucket");
            try {
                fileRequest.getFileOlds().forEach(uri -> this.amazonS3ClientService.deleteFileFromS3Bucket(uri));
            } catch (Exception e) {
                e.printStackTrace();
            }
            sw.stop();
        }

        ContractForm form = new ContractForm(fileRequest.getContractInfo());
        Map<String, Object> mapper = new HashMap<>();
        mapper.put("c", form);
        if (BaseUtil.isNullOrEmpty(fileRequest.getFileBackground()))
            fileRequest.setFileBackground(uriBackground);

        /*fileRequest.setFileTemplates(Arrays.asList(
                "OFFLINE_ONLINE_SVMCECEB_Hop_Dong_Tin_Dung.docx",
                "OFFLINE_ONLINE_ED_Hop_Dong_Tin_Dung_Ap_Dung_Chung_Toan_Bo_ED_FU_TP_SL.docx",
                //"PHU_LUC_HOP_DONG_THE_CHAP.docx",
                "ONLINE_CASH_CLFS_Hop_Dong_Tin_Dung.docx",
                "OFFLINE_ONLINE_CLFS_Hop_Dong_Tin_Dung.docx",
                //"Mau_Giay_yeu_cau_bao_hiem.docx",
                "MCSVEC_Hop_dong_the_chap.docx"*//*,
                "Dieu_khoan_va_Dieu_kien_Chuong_trinh_hoan_tien.docx"*//*));*/

        /**
         * created file and merged file template docx
         */
        //List<String> lstData = new ArrayList<>();
        List<File> lstDoc = new ArrayList<>();
        for (int i = 0; i < fileRequest.getFileTemplates().size(); i++) {
            String template = fileRequest.getFileTemplates().get(i);
            //System.out.println("template:" + template);
            //load file templates from S3 bucket
            sw.start("download File Template:" + template);
            File fileTemplate = amazonS3ClientService.downloadFileFromS3Bucket(template);
            //File fileTemplate = new File(template);
            sw.stop();
            File docX = null;
            try {
                sw.start("mergeDocX");
                docX = XDocUtils.mergeDocX(UUID.randomUUID().toString() + "." + FilenameUtils.getExtension(fileTemplate.getName()), mapper, fileTemplate);
                lstDoc.add(i, docX);
                /*byte[] bytes = BaseUtil.loadFile(docX);
                lstData.add(i, Base64.getEncoder().encodeToString(bytes));*/
                sw.stop();
            } catch (Exception e) {
                e.printStackTrace();
                throw new InternalServerErrorException();
            } finally {
                /*if (docX != null)
                    docX.delete();*/
                if (fileTemplate != null)
                    fileTemplate.delete();
            }
        }

        /**
         * convert file docx to pdf and fill watermark
         */
        File background = amazonS3ClientService.downloadFileFromS3Bucket(fileRequest.getFileBackground());
        List<File> lstContractFile = new ArrayList<>();
        List<File> lstPdf = new ArrayList<>();
        try {
            sw.start("call converter");
            lstPdf = contract_word2pdf(contractURI, lstDoc);
            sw.stop();
            if (lstPdf == null || lstPdf.size() == 0) {
                throw new InternalServerErrorException();
            }
            sw.start("watermark");
            for (int i = 0; i < lstPdf.size(); i++) {
                lstContractFile.add(i, XDocUtils.watermark("HD_" + UUID.randomUUID() + "_" + df.format(Calendar.getInstance().getTime()) + ".pdf", lstPdf.get(i), background));
            }
            sw.stop();
        } catch (Exception e) {
            e.printStackTrace();
            throw new InternalServerErrorException();
        } finally {
            if (background != null)
                background.delete();
            lstDoc.forEach(doc -> {
                if (doc != null)
                    doc.delete();
            });
            if (lstPdf != null) {
                lstPdf.forEach(pdf -> {
                    if (pdf != null)
                        pdf.delete();
                });
            }
        }

        /**
         * upload file to s3 and join file
         */
        List<String> attachments = new ArrayList<>();
        List<ContractFileResponse> fileResponses = new ArrayList<>();
        String encoded;
        try {
            encoded = joinPdfFile(lstContractFile);
            for (int i = 0; i < lstContractFile.size(); i++) {
                File contractFile = lstContractFile.get(i);
                ContractFileResponse fileResponse = new ContractFileResponse();
                PdfReader reader = new PdfReader(contractFile.getPath());
                int pages = reader.getNumberOfPages();
                reader.close();
                fileResponse.setFileSize(contractFile.length() / 1024);
                fileResponse.setFileCountPage(pages);

                //generate key and upload file to s3
                String keyName = BaseUtil.generateKeyS3(new GenerateFileRequest(fileRequest.getContractId(), fileRequest.getFileType())) + contractFile.getName();
                sw.start("uploadFileContractToS3Bucket:" + keyName);
                String uri = this.amazonS3ClientService.uploadFileContractToS3Bucket(contractFile, keyName, false);
                sw.stop();
                attachments.add(uri);
                fileResponse.setFileLink(uri);
                fileResponses.add(fileResponse);
            }
            System.out.println(sw.prettyPrint());
        } catch (Exception e) {
            e.printStackTrace();
            attachments.forEach(att -> this.amazonS3ClientService.deleteFileFromS3Bucket(att));
            throw new InternalServerErrorException();
        } finally {
            if (lstContractFile != null) {
                lstContractFile.forEach(contractFile -> {
                    if (contractFile != null)
                        contractFile.delete();
                });
            }
        }
        return ok(new GenerateContractFileResponse(fileResponses, encoded));
    }

    /**
     * Create contract file history
     *
     * @param req object ESignFileTransactionRequest contain info require to create contract file history
     * @return object ContractFileResponse contain file result
     */
    @PostMapping(value = "/contract/history")
    public ResponseEntity<?> history(@RequestBody RequestDTO<ESignFileTransactionRequest> req) {
        ESignFileTransactionRequest signFileRequest = req.init();
        if (BaseUtil.isNullOrEmpty(signFileRequest.getFileBackground()))
            signFileRequest.setFileBackground(uriBackground);
        ContractFileResponse fileResponse = new ContractFileResponse();
        //load file templates from S3 bucket
        File fileTemplate = amazonS3ClientService.downloadFileFromS3Bucket(signFileRequest.getFileTemplate());

        /*signFileRequest.setFileTemplate("e_Signing_log.docx");
        File fileTemplate = new File(signFileRequest.getFileTemplate());*/

        File background = amazonS3ClientService.downloadFileFromS3Bucket(signFileRequest.getFileBackground());
        File docX = null;
        File pdf = null;
        File result = null;
        try {
            Map<String, Object> mapper = new HashMap<>();
            mapper.put("c", signFileRequest);
            mapper.put("list", signFileRequest.getFileNames());
            docX = XDocUtils.mergeDocX(UUID.randomUUID().toString() + "." + FilenameUtils.getExtension(fileTemplate.getName()), mapper, fileTemplate);
            /*byte[] bytes = BaseUtil.loadFile(docX);
            String encoded = Base64.getEncoder().encodeToString(bytes);*/
            List<File> listPdf = contract_word2pdf(contractURI, Arrays.asList(docX));
            if (listPdf == null || listPdf.size() == 0)
                listPdf = contract_word2pdf(contractURI, Arrays.asList(docX));
            pdf = listPdf.get(0);

            String fileName = "HD_" + UUID.randomUUID() + "_" + new SimpleDateFormat("yyyyMMdd").format(Calendar.getInstance().getTime()) + ".pdf";
            result = XDocUtils.watermark(fileName, pdf, background);
            PdfReader reader = new PdfReader(result.getPath());
            fileResponse.setFileCountPage(reader.getNumberOfPages());
            reader.close();
            fileResponse.setFileSize(result.length() / 1024);

            //generate key s3
            String keyName = BaseUtil.generateKeyS3(new GenerateFileRequest(signFileRequest.getContractId(), signFileRequest.getFileType())) + fileName;
            //upload file to s3
            String uri = this.amazonS3ClientService.uploadFileContractToS3Bucket(result, keyName, false);
            fileResponse.setFileLink(uri);
            return ok(fileResponse);
        } catch (Exception e) {
            System.out.println("template:" + signFileRequest.getFileTemplate());
            //e.printStackTrace();
            throw new InternalServerErrorException(e.getMessage());
        } finally {
            if (background != null)
                background.delete();
            if (fileTemplate != null)
                fileTemplate.delete();
            if (docX != null)
                docX.delete();
            if (pdf != null)
                pdf.delete();
            if (background != null)
                background.delete();
        }
    }

    /**
     * Create contract file
     *
     * @param req object AdjustmentFileRequest contain info require to create contract file adjustment
     * @return object AdjustmentFileRequest contain file result
     */
    @PostMapping(value = "/contract/adjustment")
    public ResponseEntity<?> adjustment(@RequestBody RequestDTO<AdjustmentFileRequest> req) {
        AdjustmentFileRequest signFileRequest = req.init();
        StopWatch sw = new StopWatch("/contract/adjustment");
        if (signFileRequest.getFileOlds() != null && signFileRequest.getFileOlds().size() > 0) {
            sw.start("delete File old FromS3Bucket");
            try {
                signFileRequest.getFileOlds().forEach(old_file -> this.amazonS3ClientService.deleteFileFromS3Bucket(old_file));
            } catch (Exception e) {
                //e.printStackTrace();
            }
            sw.stop();
        }

        if (BaseUtil.isNullOrEmpty(signFileRequest.getFileBackground()))
            signFileRequest.setFileBackground(uriBackground);
        ContractFileResponse fileResponse = new ContractFileResponse();
        //load file templates from S3 bucket
        sw.start("download File Template From S3Bucket");
        File fileTemplate = amazonS3ClientService.downloadFileFromS3Bucket(signFileRequest.getFileTemplate());
        sw.stop();
        /*signFileRequest.setFileTemplate("THOA_THUAN_DIEU_CHINH_THONG_TIN.docx");
        File fileTemplate = new File(signFileRequest.getFileTemplate());
        signFileRequest.setAdjConfirmDtoList(Arrays.asList(
                new AdjConfirmDto("fullName", "NGUYỄN THỊ LUONG", "NGUYỄN THỊ LƯƠNG"),
                new AdjConfirmDto("phoneNumber", "0374666746", "0374666789"),
                new AdjConfirmDto("profession", "nhân viên", "quản lý")));*/

        sw.start("download File Background From S3Bucket");
        File background = amazonS3ClientService.downloadFileFromS3Bucket(signFileRequest.getFileBackground());
        sw.stop();
        File docX = null;
        File pdf = null;
        File result = null;
        try {

            Map<String, Object> mapper = new HashMap<>();
            mapper.put("c", signFileRequest.getAdjustmentFrom());
            mapper.put("list", signFileRequest.getAdjConfirmDtoList());
            sw.start("mergeDocX");
            docX = XDocUtils.mergeDocX(UUID.randomUUID().toString() + "." + FilenameUtils.getExtension(fileTemplate.getName()), mapper, fileTemplate);
            sw.stop();
            sw.start("word2pdf");
            /*byte[] bytes = BaseUtil.loadFile(docX);
            String encoded = Base64.getEncoder().encodeToString(bytes);*/
            List<File> listPdf = contract_word2pdf(contractURI, Arrays.asList(docX));
            if (listPdf == null || listPdf.size() == 0)
                listPdf = contract_word2pdf(contractURI, Arrays.asList(docX));
            pdf = listPdf.get(0);
            sw.stop();

            sw.start("watermark");
            String fileName = "HD_" + UUID.randomUUID() + "_" + new SimpleDateFormat("yyyyMMdd").format(Calendar.getInstance().getTime()) + ".pdf";
            result = XDocUtils.watermark(fileName, pdf, background);
            sw.stop();
            PdfReader reader = new PdfReader(result.getPath());
            fileResponse.setFileCountPage(reader.getNumberOfPages());
            reader.close();
            fileResponse.setFileSize(result.length() / 1024);
            String encoded = Base64.getEncoder().encodeToString(BaseUtil.loadFile(result));

            //generate key s3
            String keyName = BaseUtil.generateKeyS3(new GenerateFileRequest(signFileRequest.getContractId(), signFileRequest.getFileType())) + fileName;
            //upload file to s3
            sw.start("uploadFileContractToS3Bucket");
            String uri = this.amazonS3ClientService.uploadFileContractToS3Bucket(result, keyName, false);
            sw.stop();
            fileResponse.setFileLink(uri);
            List<ContractFileResponse> fileResponses = new ArrayList<>();
            fileResponses.add(fileResponse);
            return ok(new GenerateContractFileResponse(fileResponses, encoded));
        } catch (Exception e) {
            System.out.println("template:" + signFileRequest.getFileTemplate());
            //e.printStackTrace();
            throw new InternalServerErrorException(e.getMessage());
        } finally {
            if (fileTemplate != null)
                fileTemplate.delete();
            if (docX != null)
                docX.delete();
            if (pdf != null)
                pdf.delete();
            if (background != null)
                background.delete();
            //System.out.println(sw.prettyPrint());
        }
    }

    /**
     * Download files contract base on list uri s3 request
     *
     * @param req DownloadFileContractRequest contain info uri s3 request
     * @return UriResponse contain base 64 of all files contract has joined
     */
    @PostMapping(value = "/contract/download")
    public ResponseEntity<?> getFile(@RequestBody RequestDTO<DownloadFileContractRequest> req) throws Exception {
        DownloadFileContractRequest fileRequest = req.init();
        if (fileRequest == null)
            return badRequest();
        if (fileRequest.getFiles() == null || fileRequest.getFiles().size() == 0) {
            return ok();
        }
        List<String> files = fileRequest.getFiles();
        List<File> fileList = new ArrayList<>();
        try {
            files.forEach(url -> {
                fileList.add(amazonS3ClientService.downloadFileFromS3Bucket(url));
            });
            return ok(new UriResponse(joinPdfFile(fileList)));
        } finally {
            fileList.forEach(contractFile -> contractFile.delete());
        }
    }

    /**
     * Invoke rest api to convert list file word to list file pdf
     *
     * @param uri     api name
     * @param lstDocx list file word need to convert
     * @return list file pdf
     */
    List<File> contract_word2pdf(String uri, List<File> lstDocx) {
        try {
            return XDocUtils.converter(uri + "/word2pdf", lstDocx);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /*List<File> contract_word2pdf_b64(String uri, List<String> lstData) {
        List<File> lstPdf = new ArrayList<>();
        try {
            List<String> results = XDocUtils.converter_b64(uri + "/word2pdf", lstData);
            if (results == null)
                return null;
            for (int i = 0; i < results.size(); i++) {
                File pdf = new File(UUID.randomUUID() + ".pdf");
                byte[] bytesResult = Base64
                        .getDecoder()
                        .decode(results.get(i));
                //creating the file in the server (temporarily)
                FileOutputStream fos = new FileOutputStream(pdf);
                fos.write(bytesResult);
                fos.close();
                lstPdf.add(i, pdf);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return lstPdf;
    }*/

    /**
     * Join list file pdf to only one file pdf
     *
     * @param lstPdf list file pdf need join
     * @return string base 64 of file pdf joined
     * @throws Exception
     */
    String joinPdfFile(List<File> lstPdf) throws Exception {
        List<InputStream> inputStreams = new ArrayList<>();
        for (File contractFile : lstPdf) {
            inputStreams.add(new FileInputStream(contractFile));
        }
        File file = new File(UUID.randomUUID().toString() + ".pdf");
        OutputStream outputStream = new FileOutputStream(file);
        XDocUtils.mergePdfFiles(inputStreams, outputStream);
        String encoded = Base64.getEncoder().encodeToString(BaseUtil.loadFile(file));
        file.delete();
        return encoded;
    }
}

