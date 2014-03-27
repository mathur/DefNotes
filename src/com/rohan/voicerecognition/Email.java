package com.rohan.voicerecognition;

import android.annotation.SuppressLint;
import android.os.Environment;
import android.util.Log;

import com.github.sendgrid.SendGrid;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Peter on 3/25/14.
 */
public class Email {

    public static String emailContentsHTML = "",
            emailContentsText = "";
    public static final String intro = "Key Terms/Definitions:",
            startHtml = "<style type='text/css'>body{font-size: 13;}h1{font-size: 14;font-family:serif;}table,th,td,tr {color:#000;width:700px;}</style><table><tr><td><img border='0' src='http://www.rmathur.com/images/BANNER.png' width=700></td></tr><tr><td><h1>Hey there! Look we what managed to find out about that boring lecture you weren't forced to sit though!</h1></td></tr><tr><td><p wdith=700>",
            endHtml = "</p></td></tr></table>";

    public Email() {
    }

    @SuppressLint("SimpleDateFormat")
    public void sendEmail(String userEmail, String lectureName) {
        Date date = new Date();
        DateFormat dateFormat = new SimpleDateFormat("MMddyyyyHHmmss");

        Document document = new Document();
        String file = Environment.getExternalStorageDirectory().getPath() + "/"
                + dateFormat.format(date) + "defnotes.pdf";
        Log.d("pdf location", file);
        try {
            PdfWriter.getInstance(document, new FileOutputStream(file));
        } catch (Exception e) {
            Log.e("pdf get instance", e.getMessage());
        }
        document.open();

        try {
            Paragraph p1 = new Paragraph(lectureName);
            document.add(p1);
            //Paragraph p2 = new Paragraph(Email.emailContentsText);
            //document.add(p2);
            PdfPTable table = createTable1();
            document.add(table);
        } catch (DocumentException e) {
            Log.e("pdf add p1/p2", e.getMessage());
        }

        document.close();

        SendGrid sendgrid = new SendGrid("rohan32", "hackru");
        sendgrid.addTo(userEmail);
        sendgrid.setFrom("study@defnotes.com");
        sendgrid.setSubject("Your " + lectureName + " study guide!");
        sendgrid.setHtml(Email.startHtml + Email.emailContentsHTML + Email.endHtml);
        try {
            sendgrid.addFile(new File(file));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        sendgrid.send();
        File pdfFile = new File(file);
        pdfFile.delete();
    }

    /**
     * Creates a table; widths are set with setWidths().
     *
     * @return a PdfPTable
     * @throws DocumentException
     */
    public static PdfPTable createTable1() throws DocumentException {
        PdfPTable table = new PdfPTable(3);
        table.setWidthPercentage(288 / 5.23f);
        table.setWidths(new int[]{2, 1, 1});
        PdfPCell cell;
        cell = new PdfPCell(new Phrase("Vocabulary:"));
        cell.setColspan(2);
        table.addCell(cell);
        cell = new PdfPCell(new Phrase("Cell with rowspan 2"));
        cell.setRowspan(Dictionary.definitions.size());
        table.addCell(cell);

        //If we managed these lists right, the terms and definitions should match
        for (int i = 0; i < Alchemy.keywordsList.size(); i++) {
            String term = Alchemy.keywordsList.get(i);
            String def = Dictionary.definitions.get(i);
            table.addCell(term + ": " + def);
        }
        return table;
    }
}
