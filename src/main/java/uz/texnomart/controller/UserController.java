package uz.texnomart.controller;
import org.telegram.telegrambots.meta.api.methods.send.SendDocument;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
import org.telegram.telegrambots.meta.api.objects.Contact;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardRemove;
import java.io.File;
import java.io.IOException;
import java.util.Map;


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
