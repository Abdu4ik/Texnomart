package uz.texnomart.service;

import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.properties.TextAlignment;
import uz.texnomart.container.Container;
import uz.texnomart.entity.TelegramUser;
import uz.texnomart.enums.UserRoles;

import java.io.File;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import static uz.texnomart.container.Container.*;

public class WorkWithFiles {

    public static void writerPdf(List<TelegramUser> telegramUserList) {
        final String BASE_FOLDER = "src/main/resources/files/documents";
        File file = new File(BASE_FOLDER, "Foydalanuvchilar rõyxati.pdf");
        file.getParentFile().mkdirs();

        try (PdfWriter pdfWriter = new PdfWriter(file);
             PdfDocument pdfDocument = new PdfDocument(pdfWriter);
             Document document = new Document(pdfDocument)
        ) {
            pdfDocument.addNewPage();

            Paragraph paragraph = new Paragraph("Foydalanuvchilar ro'yxati");
            paragraph.setTextAlignment(TextAlignment.CENTER);

            document.add(paragraph);

            float[] columnWidths = {12f, 50f, 150f, 100f, 80f};
            Table table = new Table(columnWidths);

            String [] columns = {"T/R ", "Chat Id ", "Full Name", "Phone Number", "User Role"};

            for (String column : columns) {
                table.addCell(column);
            }
            int number = 0;
            for (TelegramUser user : telegramUserList) {

                table.addCell(String.valueOf(++number));
                table.addCell(user.getChatId());
                table.addCell(user.getFullName());
                table.addCell(user.getPhoneNumber());
                table.addCell(String.valueOf(user.getUserRoles()));
            }

            document.add(table);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
