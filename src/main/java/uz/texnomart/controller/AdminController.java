package uz.texnomart.controller;


import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Contact;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.PhotoSize;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import uz.texnomart.util.KeyboardButtonUtil;

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
        String text = message.getText();
        String chatId = String.valueOf(message.getChatId());
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);
        if (text.equals("/start"))
        {
            sendMessage.setText("Assalom alaykum!\nTexnomart botiga xush kelibsiz!\nKontaktingizni jonating.");
            sendMessage.setReplyMarkup(KeyboardButtonUtil.getContactMenu());
        }

    }


    public static void handleCallBackQuery(Message message, String data) {

    }
}