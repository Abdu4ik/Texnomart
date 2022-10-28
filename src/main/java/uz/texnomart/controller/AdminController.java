package uz.texnomart.controller;


import org.telegram.telegrambots.meta.api.methods.send.SendDocument;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.Contact;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.PhotoSize;
import uz.texnomart.container.Container.*;
import uz.texnomart.enums.AdminStatus;
import uz.texnomart.service.AdminService;
import uz.texnomart.util.InlineKeyboardButtonUtil;
import uz.texnomart.util.KeyboardButtonUtil;

import java.util.List;

import static uz.texnomart.container.Container.*;
import static uz.texnomart.enums.AdminStatus.*;
import static uz.texnomart.util.KeyboardButtonConstants.*;

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
        String chatId = String.valueOf(message.getChatId());

        if (adminMap.containsKey(chatId) && adminMap.get(chatId) == SEND_ADS) {
            String caption = message.getCaption();
            String fileId = photoSizeList.get(photoSizeList.size() - 1).getFileId();
            SendPhoto sendPhoto = new SendPhoto();
            sendPhoto.setCaption(caption);
            sendPhoto.setChatId(chatId);
            sendPhoto.setPhoto(new InputFile(fileId));
            sendPhoto.setReplyMarkup(InlineKeyboardButtonUtil.getConfirmationButtons());
            MY_BOT.sendMsg(sendPhoto);

        }
    }


    private static void handleContact(Message message, Contact contact) {

    }

    private static void handleText(Message message) {
        String text = message.getText();
        String chatId = String.valueOf(message.getChatId());
        SendDocument sendDocument = new SendDocument();
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);
        sendDocument.setChatId(chatId);
        if (text.equals("/start"))
        {
            sendMessage.setText("Assalom alaykum!\nTexnomart botiga xush kelibsiz!");
            sendMessage.setReplyMarkup(KeyboardButtonUtil.getAdminMenu());
            MY_BOT.sendMsg(sendMessage);
        }else if (text.equals(_SHOW_USERS_)){
            sendDocument.setDocument(new InputFile(AdminService.showUsersAsPDF()));
            MY_BOT.sendMsg(sendDocument);
        }else if (text.equals(_EDIT_ADMIN_)){
            sendDocument.setDocument(new InputFile(AdminService.showUsersAsPDF()));
            sendDocument.setCaption("Admin qilmoqchi bo'lgan foydalanuvchining ID sini kiriting.");
            MY_BOT.sendMsg(sendDocument);

        }else if (text.equals(_SEND_ADS_)){
            adminMap.put(chatId, SEND_ADS);
            sendMessage.setText("🖼 Reklama matnini rasmi bilan jo'nating yoki reklama matnini jonating.");
            MY_BOT.sendMsg(sendMessage);
        }else if (text.equals(_DISCOUNT_)){

        }else if (text.equals(_CATEGORIES_)){

        }else if (text.equals(_PRODUCTS_)){

        } else if (text.equals(_ORDER_LIST_)) {

        }else if (text.equals(_SHOW_MESSAGES_)){

        }else if (adminMap.containsKey(chatId) && adminMap.get(chatId) == SEND_ADS){
            sendMessage.setText(text);
            sendMessage.setReplyMarkup(InlineKeyboardButtonUtil.getConfirmationButtons());
            MY_BOT.sendMsg(sendMessage);
        }

    }


    public static void handleCallBackQuery(Message message, String data) {

    }
}