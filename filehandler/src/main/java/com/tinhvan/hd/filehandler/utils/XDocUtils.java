package com.tinhvan.hd.filehandler.utils;

import com.documents4j.api.DocumentType;
import com.documents4j.api.IConverter;
import com.documents4j.job.LocalConverter;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.itextpdf.text.Document;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfImportedPage;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfWriter;
import fr.opensagres.xdocreport.converter.ConverterTypeTo;
import fr.opensagres.xdocreport.converter.ConverterTypeVia;
import fr.opensagres.xdocreport.core.XDocReportException;
import fr.opensagres.xdocreport.document.IXDocReport;
import fr.opensagres.xdocreport.document.registry.XDocReportRegistry;
import fr.opensagres.xdocreport.itext.extension.IPdfWriterConfiguration;
import fr.opensagres.xdocreport.template.IContext;
import fr.opensagres.xdocreport.template.TemplateEngineKind;
import fr.opensagres.xdocreport.template.formatter.FieldsMetadata;
import org.apache.poi.xwpf.converter.core.Options;
import org.apache.poi.xwpf.converter.pdf.PdfConverter;
import org.apache.poi.xwpf.converter.pdf.PdfOptions;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.*;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import javax.xml.crypto.Data;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.URL;
import java.nio.file.Files;
import java.util.*;
import java.util.List;
import java.util.concurrent.Future;

public class XDocUtils {

    /**
     * Using IXDocReport and freemarker template to fill data into file template ms word
     *
     * @param fileName name of file result
     * @param mapper contain values need merge
     * @param fileTemplate
     * @return file word after fill data successfully
     * @throws Exception
     */
    public static File mergeDocX(String fileName, Map<String, Object> mapper, File fileTemplate) throws Exception {
        File file = new File(fileName);
        InputStream in = loadDocumentAsStream(fileTemplate);
        IXDocReport xDocReport = loadDocumentAsIDocxReport(in, TemplateEngineKind.Freemarker);
        FieldsMetadata metadata = new FieldsMetadata();
        metadata.addFieldAsList("list");
        xDocReport.setFieldsMetadata(metadata);
        IContext context = replaceVariabalesInTemplateOtherThanImages(xDocReport, mapper);

        byte[] bytes = generateMergedOutput(xDocReport, context);
        Files.write(file.toPath(), bytes);
        return file;
    }

    /**
     * Convert file word to pdf
     *
     * @param fileName name of file pdf
     * @param fileDocX file word need convert
     * @return file pdf
     * @throws Exception
     */
    public static File convertPDF(String fileName, File fileDocX) throws Exception {
        File file = new File(fileName);
        ByteArrayOutputStream bo = new ByteArrayOutputStream();
        InputStream in = new FileInputStream(fileDocX);

        IConverter converter = LocalConverter.builder().build();
        Future<Boolean> conversion = converter
                .convert(in).as(DocumentType.MS_WORD)
                .to(bo).as(DocumentType.PDF)
                .prioritizeWith(1000) // optional
                .schedule();
        conversion.get();
        OutputStream out = new FileOutputStream(file);
        bo.writeTo(out);
        out.close();
        in.close();
        bo.close();
        converter.shutDown();
        return file;
    }

    /**
     * Set background image for one file pdf
     *
     * @param fileName file pdf after update background
     * @param filePDF file pdf need to set background
     * @param fileImage background image
     * @return file pdf after update background
     * @throws Exception
     */
    public static File watermark(String fileName, File filePDF, File fileImage) throws Exception {
        File file = new File(fileName);
        PdfReader reader = new PdfReader(filePDF.getPath());
        Image watermark = Image.getInstance(fileImage.toURL());
        watermark.scaleToFit(PageSize.A4.getWidth(), PageSize.A4.getHeight());
        //watermark.setAlignment(Image.MIDDLE);
        watermark.setAbsolutePosition(0, 0);

        // Create output PDF
        Document document = new Document(PageSize.A4);
        PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(file));
        document.open();
        PdfContentByte canvas = writer.getDirectContent();

        // Copy page of existing PDF into output PDF
        for (int i = 1; i <= reader.getNumberOfPages(); i++) {
            document.newPage();
            PdfImportedPage page = writer.getImportedPage(reader, i);
            canvas.addTemplate(page, 0, 0);
            document.add(watermark);
        }
        document.close();
        writer.close();
        reader.close();
        return file;
    }

    static InputStream loadDocumentAsStream(File fileTemplate) throws IOException {
        URL url = fileTemplate.toURI().toURL();
        InputStream documentTemplateAsStream = null;
        documentTemplateAsStream = url.openStream();
        return documentTemplateAsStream;
    }

    static IXDocReport loadDocumentAsIDocxReport(InputStream documentTemplateAsStream, TemplateEngineKind freemarkerOrVelocityTemplateKind) throws IOException, XDocReportException {
        IXDocReport xdocReport = XDocReportRegistry.getRegistry().loadReport(documentTemplateAsStream, freemarkerOrVelocityTemplateKind);
        return xdocReport;
    }

    static IContext replaceVariabalesInTemplateOtherThanImages(IXDocReport report, Map<String, Object> variablesToBeReplaced) throws XDocReportException {
        IContext context = report.createContext();
        for (Map.Entry<String, Object> variable : variablesToBeReplaced.entrySet()) {
            context.put(variable.getKey(), variable.getValue());
        }
        return context;
    }

    static byte[] generateMergedOutput(IXDocReport report, IContext context) throws XDocReportException, IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        report.process(context, outputStream);
        return outputStream.toByteArray();
    }

    public static void mergePdfFiles(java.util.List<InputStream> inputPdfList, OutputStream outputStream) throws Exception {

        //Create document and pdfReader objects.
        Document document = new Document();
        java.util.List<PdfReader> readers = new ArrayList<>();
        int totalPages = 0;

        //Create pdf Iterator object using inputPdfList.
        Iterator<InputStream> pdfIterator =
                inputPdfList.iterator();

        // Create reader list for the input pdf files.
        while (pdfIterator.hasNext()) {
            InputStream pdf = pdfIterator.next();
            PdfReader pdfReader = new PdfReader(pdf);
            readers.add(pdfReader);
            totalPages = totalPages + pdfReader.getNumberOfPages();
        }

        // Create writer for the outputStream
        PdfWriter writer = PdfWriter.getInstance(document, outputStream);

        //Open document.
        document.open();

        //Contain the pdf data.
        PdfContentByte pageContentByte = writer.getDirectContent();

        PdfImportedPage pdfImportedPage;
        int currentPdfReaderPage = 1;
        Iterator<PdfReader> iteratorPDFReader = readers.iterator();

        // Iterate and process the reader list.
        while (iteratorPDFReader.hasNext()) {
            PdfReader pdfReader = iteratorPDFReader.next();
            //Create page and add content.
            while (currentPdfReaderPage <= pdfReader.getNumberOfPages()) {
                document.newPage();
                pdfImportedPage = writer.getImportedPage(
                        pdfReader, currentPdfReaderPage);
                pageContentByte.addTemplate(pdfImportedPage, 0, 0);
                currentPdfReaderPage++;
            }
            currentPdfReaderPage = 1;
        }

        //Close document and outputStream.
        outputStream.flush();
        document.close();
        outputStream.close();
    }

    public static List<String> converter_b64(String uri, List<String> lstBase64) {
        ObjectMapper mapper = new ObjectMapper();
        HttpHeaders headers = new HttpHeaders();
        headers.set("x-environment", "FILE-HANDLER");
        headers.setContentType(MediaType.APPLICATION_JSON);

        try {
            RestTemplate restTemplate = new RestTemplate();
            //BaseUtil.setTimeout(restTemplate);
            // Data attached to the request.
            HttpEntity<List<String>> requestEntity = new HttpEntity<>(lstBase64, headers);

            // Send request with POST method.
            ResponseEntity<String> result = restTemplate.postForEntity(uri, requestEntity, String.class);

            // Code = 200.
            if (result != null && result.getStatusCode() == HttpStatus.OK && result.getBody() != null) {
                try {
                    return mapper.readValue(result.getBody(), new TypeReference<List<String>>() {
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static List<File> converter(String uri, List<File> lstDocx) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            LinkedMultiValueMap<String, Object> map = new LinkedMultiValueMap<>();
            for (File docX : lstDocx) {
                FileSystemResource value = new FileSystemResource(docX);
                map.add("files", value);
            }
            HttpHeaders headers = new HttpHeaders();
            headers.set("x-environment", "FILE-HANDLER");
            headers.setContentType(MediaType.MULTIPART_FORM_DATA);
            HttpEntity<LinkedMultiValueMap<String, Object>> requestEntity = new HttpEntity<>(map, headers);
            RestTemplate restTemplate = new RestTemplate();
            ResponseEntity<String> responseEntity = restTemplate.exchange(uri, HttpMethod.POST, requestEntity, String.class);

            // Code = 200.
            if (responseEntity != null && responseEntity.getStatusCode() == HttpStatus.OK && responseEntity.getBody() != null) {
                try {
                    List<byte[]> responses = mapper.readValue(responseEntity.getBody(), new TypeReference<List<byte[]>>() {
                    });
                    System.out.println(responses.size());
                    if (responses == null || responses.size() == 0)
                        return null;
                    List<File> lstPdf = new ArrayList<>();
                    for (byte[] bytes : responses) {
                        if(bytes==null)
                            return null;
                        File pdf = new File(UUID.randomUUID().toString() + ".pdf");
                        Files.write(pdf.toPath(), bytes);
                        lstPdf.add(pdf);
                    }
                    return lstPdf;
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
