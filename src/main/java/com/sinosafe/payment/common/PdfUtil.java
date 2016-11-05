package com.sinosafe.payment.common;

import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Paragraph;
import com.lowagie.text.pdf.PdfWriter;
import org.apache.log4j.Logger;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;

public class PdfUtil {
    private static Logger logger = Logger.getLogger(PdfUtil.class);

    public static boolean createPdf(String pdfPath, String content) {
        try {
            Document document = new Document();
            PdfWriter.getInstance(document, new FileOutputStream(pdfPath));
            document.open();
            document.add(new Paragraph(content));
            document.close();
        } catch (FileNotFoundException e) {
            logger.error(e.getMessage());
            return false;
        } catch (DocumentException e) {
            logger.error(e.getMessage());
            return false;
        }
        return true;
    }

    public static void main(String[] args) {

        PdfUtil.createPdf("D:\\1.pdf", "YOYO CHECK OUT!");

    }
}