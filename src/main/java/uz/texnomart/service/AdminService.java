package uz.texnomart.service;
import uz.texnomart.container.Container;
import uz.texnomart.entity.TelegramUser;
import uz.texnomart.enums.AdminStatus;
import uz.texnomart.enums.UserRoles;
import java.io.File;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static uz.texnomart.container.Container.*;

public class AdminService {

    public static File showUsersAsPDF() {

        List<TelegramUser> telegramUserList = new ArrayList<>();
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD)) {

            Statement statement = connection.createStatement();
            String query = """
                              select * from customer where user_role = 'USER'::user_roles order by id;
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
        return WorkWithFiles.writerPdf(telegramUserList);

    }


    public static void changeAdminStatus(String chatId, AdminStatus adminStatus){ // adminMapga

        for (Map.Entry<String, AdminStatus> adminStatusEntry : Container.adminMap.entrySet()) {
            if (adminStatusEntry.getKey().equals(chatId)){
                adminStatusEntry.setValue(adminStatus);
            }
        }

    }

    public static boolean checkAdminStatus(String chatId, AdminStatus adminStatus){

        for (Map.Entry<String, AdminStatus> adminStatusEntry : Container.adminMap.entrySet()) {
            if (adminStatusEntry.getKey().equals(chatId) && adminStatusEntry.getValue() == adminStatus){
                return true;
            }
        }

        return false;
    }

    public static void putAminsIntoMap (String chatId){ //  har safar admin qo'shilganda uni chat id sini admin mapga put qilish uchun
        if (!Container.adminMap.containsKey(chatId)){
            Container.adminMap.put(chatId, null);
        }
    }
}
