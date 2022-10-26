package uz.texnomart.controller;


import org.telegram.telegrambots.meta.api.objects.Contact;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.PhotoSize;

import java.util.List;

public class AdminController {
    public static void handleMessage(Message message) {
        if (message.hasText()) {
            handleText(message);
        } else if (message.hasContact()) {
            handleContact(message, message.getContact());
        } else if (message.hasPhoto()) {
            handlePhoto(message, message.getPhoto());
        }
    }

    private static void handlePhoto(Message message, List<PhotoSize> photoSizeList) {

    }


    private static void handleContact(Message message, Contact contact) {

    }

    private static void handleText(Message message) {

    }


    public static void handleCallBackQuery(Message message, String data) {

    }
}