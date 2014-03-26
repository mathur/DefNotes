package com.rohan.voicerecognition;

import android.annotation.SuppressLint;
import android.os.Environment;

import com.github.sendgrid.SendGrid;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Paragraph;
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
    public void sendEmail(String userEmail,String lectureName) {

        Date date = new Date();
        DateFormat dateFormat = new SimpleDateFormat("MMddyyyyHHmmss");

        Document document = new Document();
        String file = Environment.getExternalStorageDirectory().getPath() + "/"
                + dateFormat.format(date) + "defnotes.pdf";
        try {
            PdfWriter.getInstance(document, new FileOutputStream(file));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (DocumentException e) {
            e.printStackTrace();
        }
        document.open();
        Paragraph p1 = new Paragraph(lectureName);
        Paragraph p2 = new Paragraph(Email.emailContentsText);
        try {
            document.add(p1);
            document.add(p2);
        } catch (DocumentException e) {
            e.printStackTrace();
        }
        document.close();

        SendGrid sendgrid = new SendGrid("rohan32", "hackru");
        sendgrid.addTo(userEmail);
        sendgrid.setFrom("info@defnotes.com");
        sendgrid.setSubject("Your " + lectureName + " study guide");
        sendgrid.setHtml(Email.startHtml + Email.emailContentsHTML + Email.endHtml);
        try {
            sendgrid.addFile(new File(file));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        sendgrid.send();
//        Toast.makeText(context, "Email sent successfully.", Toast.LENGTH_SHORT)
//                .show();
        File pdfFile = new File(file);
        pdfFile.delete();
    }


}
