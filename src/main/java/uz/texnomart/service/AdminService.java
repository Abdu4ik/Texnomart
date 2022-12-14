package uz.texnomart.service;
import org.telegram.telegrambots.meta.api.methods.ForwardMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.Message;
import uz.texnomart.container.Container;
import uz.texnomart.entity.TelegramUser;
import uz.texnomart.enums.AdminStatus;
import uz.texnomart.enums.UserRoles;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import static uz.texnomart.container.Container.*;

public class AdminService {

    public static void showUsersAsPDF() {

        List<TelegramUser> telegramUserList = new ArrayList<>();
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
            Statement statement = connection.createStatement()
        ) {

            String query = """
                              select * from customer;
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
        WorkWithFiles.writerPdf(telegramUserList);

    }


    public static void changeAdminStatus(String chatId, AdminStatus adminStatus){ // adminMapga

        for (Map.Entry<String, AdminStatus> adminStatusEntry : Container.adminMap.entrySet()) {
            if (adminStatusEntry.getKey().equals(chatId)){
                adminStatusEntry.setValue(adminStatus);
            }
        }

    }
    public static void sendAdsToAllCustomers(Message message){
//        select * from customer where user_role = 'USER'::user_roles order by id;

        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
             Statement statement = connection.createStatement()
        ) {

            String query = """
                              select chat_id from customer where user_role = 'USER'::user_roles;
                    """;
            ResultSet resultSet = statement.executeQuery(query);

            while (resultSet.next()) {
                String chat_id = resultSet.getString("chat_id");
                ForwardMessage forwardMessage = new ForwardMessage(chat_id, String.valueOf(message.getChatId()), message.getMessageId());
                MY_BOT.sendMsg(forwardMessage);
            }
        } catch (SQLException e) {
            e.printStackTrace();
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

    public static void sendAdsToAllCustomers(String photo, String caption){
        SendPhoto sendPhoto = new SendPhoto();
        sendPhoto.setPhoto(new InputFile(photo));
        sendPhoto.setCaption(caption);

        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
             Statement statement = connection.createStatement()
        ) {

            String query = """
                              select chat_id from customer where user_role = 'USER'::user_roles;
                    """;
            ResultSet resultSet = statement.executeQuery(query);

            while (resultSet.next()) {
                String chat_id = resultSet.getString(1);
                sendPhoto.setChatId(chat_id);
                MY_BOT.sendMsg(sendPhoto);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static String editProductName(String  name, Integer product_id) {
        // bu metodga product id va productni nameni berasan va bu bazadan almashtirib qoyadi
        String response = "";

        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD)) {

                String query = """
                      update product set name=? where id=?;
                        """;

            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, name);
            preparedStatement.setInt(2, product_id);
            if(preparedStatement.executeUpdate()==1){
                response="Successfully edit product name";
            }

        } catch (SQLException e) {
            response = "Productni edit qilishda exceptionga tushdi bu ";
        }
        return response;
    }

}


