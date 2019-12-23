package com.tinhvan.hd.filehandler.itext;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.UUID;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import org.springframework.core.io.ClassPathResource;
import org.springframework.util.ResourceUtils;

public class PdfProvider {
//    public static void main(String[] args) {
//        try {
//            // Create output PDF
//            Document document = new Document(PageSize.A4);
//            PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream("result.pdf"));
//            document.open();
//            PdfContentByte canvas = writer.getDirectContent();
//
//            File file = ResourceUtils.getFile("classpath:templates/sample.pdf");
//
//            // Load existing PDF
//            PdfReader reader = new PdfReader(file.getPath());
//            Image signed = Image.getInstance(ResourceUtils.getURL("classpath:templates/signed.png"));
//            signed.setAlignment(Image.MIDDLE);
//            // Copy page of existing PDF into output PDF
//            for (int i = 1; i <= reader.getNumberOfPages(); i++) {
//                document.newPage();
//                PdfImportedPage page = writer.getImportedPage(reader, i);
//                canvas.addTemplate(page, 0, 0);
//                document.add(signed);
//
//                // Add your new data / text here
//                Phrase phrase = new Phrase("Some text" + i, new Font());
//                ColumnText.showTextAligned(canvas, Element.ALIGN_CENTER, phrase, 260, PageSize.A4.getHeight() - 148, 0);
//
//            }
//            document.close();
//
//        } catch (IOException e) {
//            e.printStackTrace();
//        } catch (DocumentException e) {
//            e.printStackTrace();
//        }
//    }
}