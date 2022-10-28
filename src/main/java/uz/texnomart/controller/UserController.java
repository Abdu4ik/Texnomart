package uz.texnomart.controller;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Contact;
import org.telegram.telegrambots.meta.api.objects.Message;
import uz.texnomart.container.Container;
import uz.texnomart.entity.TelegramUser;
import uz.texnomart.service.AdminService;

import java.sql.*;


public class UserController {
    private static final String url = "jdbc:postgresql://ec2-54-75-26-218.eu-west-1.compute.amazonaws.com:5432/dae44hkoegn6lq";
    private static final String dbuser = "utpvoxxsoitfbq";
    private static final String dbpassword = "0ae03e88b14ced6a6e431080225030545efe9af022cc14f62fb96346a3a16ea5";

    public static void handleMessage(Message message) {

         AdminService.checkAdmin(message);

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


        }
    }


    private static void handleContact(Message message, Contact contact) {

        String chatId = String.valueOf(message.getChatId());


    }

    public static void handleCallBackQuery(Message message, String data) {

    }
}
