package uz.texnomart.service;

import com.itextpdf.io.image.ImageData;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.properties.TextAlignment;
import uz.texnomart.entity.TelegramUser;
import uz.texnomart.enums.UserRoles;
import com.itextpdf.layout.Document;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import static uz.texnomart.container.Container.*;

public class AdminService {

    public static File showUsersAsPDF() {

        List<TelegramUser> telegramUserList = new ArrayList<>();
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD)) {

            Statement statement = connection.createStatement();
            String query = """
                              select * from customer order by id;
                    """;
            ResultSet resultSet = statement.executeQuery(query);

            while (resultSet.next()) {
                String chat_id = resultSet.getString(1);
                String fullname = resultSet.getString(2);
                String phone_number = resultSet.getString(3);
                UserRoles user_role = UserRoles.valueOf(resultSet.getString(4));
                telegramUserList.add(new TelegramUser(chat_id, fullname, phone_number, user_role));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return writerPdf(telegramUserList);

    }

    public static File writerPdf(List<TelegramUser> telegramUserList) {
        final String BASE_FOLDER = "src/main/resources/files/documents";

        File file = new File(BASE_FOLDER, "customer.pdf");
        file.getParentFile().mkdirs();

        try (PdfWriter pdfWriter = new PdfWriter(file);
             PdfDocument pdfDocument = new PdfDocument(pdfWriter);
             Document document = new Document(pdfDocument);
        ) {
            pdfDocument.addNewPage();

            Paragraph paragraph = new Paragraph("People");
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
            pdfDocument.close();
            pdfWriter.close();
            document.close();
            Desktop.getDesktop().open(file);

        } catch (IOException e) {
            e.printStackTrace();
        }
        return file;// Todo najim to'gola man hozir pdf faylni create qildim sila buni send file qilib resoursedan ochirib yuboringla xaymi

    }
}


