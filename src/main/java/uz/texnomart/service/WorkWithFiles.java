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

    public static File writerPdf(List<TelegramUser> telegramUserList) {

        File file = new File(Container.BASE_FOLDER, "customer.pdf");
        file.getParentFile().mkdirs();

        try (PdfWriter pdfWriter = new PdfWriter(file);
             PdfDocument pdfDocument = new PdfDocument(pdfWriter);
             Document document = new Document(pdfDocument)
        ) {
            pdfDocument.addNewPage();

            Paragraph paragraph = new Paragraph("Customers");
            paragraph.setTextAlignment(TextAlignment.CENTER);

            document.add(paragraph);

            float[] columnWidths = {50f, 150f, 100f, 30f};
            Table table = new Table(columnWidths);

            String[] columns = {"Chat Id ", "Full name", "Phone Number ", "User Role"};

            for (int i = 0; i < columns.length; i++) {
                table.addCell(columns[i]);
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
        return file;// Todo najim to'gola man hozir pdf faylni create qildim sila buni send file qilib resoursedan ochirib yuboringla xaymi

    }

}
