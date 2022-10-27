package uz.texnomart.controller;
import org.telegram.telegrambots.meta.api.objects.Contact;
import org.telegram.telegrambots.meta.api.objects.Message;



public class UserController {
    public static void handleMessage(Message message) {

        if (message.hasContact()) {
            handleContact(message, message.getContact());
        }
        if (message.hasText()) {
            handleText(message, message.getText());
        }

    }

    private static void handleText(Message message, String text) {

    }



    private static void handleContact(Message message, Contact contact) {

    }

    public static void handleCallBackQuery(Message message, String data) {

    }
}
