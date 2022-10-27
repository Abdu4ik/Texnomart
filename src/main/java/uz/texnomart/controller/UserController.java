package uz.texnomart.controller;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Contact;
import org.telegram.telegrambots.meta.api.objects.Message;
import uz.texnomart.entity.TelegramUser;

import java.sql.*;


public class UserController {
    private static final String url = "jdbc:postgresql://ec2-54-75-26-218.eu-west-1.compute.amazonaws.com:5432/dae44hkoegn6lq";
    private static final String dbuser = "utpvoxxsoitfbq";
    private static final String dbpassword = "0ae03e88b14ced6a6e431080225030545efe9af022cc14f62fb96346a3a16ea5";

    public static void handleMessage(Message message) {

        if (message.hasContact()) {
            handleContact(message, message.getContact());
        }
        if (message.hasText()) {
            handleText(message, message.getText());
        }

    }

    private static void handleText(Message message, String text) {
        TelegramUser customer = new TelegramUser();
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(customer.getChatId());

        if ("/start".equals(text)) {
            Connection connection = null;
            try {
                connection = DriverManager.getConnection(url, dbuser, dbpassword);
                String query = "select * from customer where chat_id=?";

                PreparedStatement preparedStatement = connection.prepareStatement(query);
                preparedStatement.setString(1,customer.getChatId());
                ResultSet resultSet = preparedStatement.executeQuery();

                if(resultSet.getString("chat_id")==null){
                    // new telegram user menu qilish kk fotih bn murod uchun
                }
                else{
                    // admin qismi uchun menu qilish kk buni
                    // abdullo aka shoxsanam qiladi
                }


            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }


    private static void handleContact(Message message, Contact contact) {

    }

    public static void handleCallBackQuery(Message message, String data) {

    }
}
