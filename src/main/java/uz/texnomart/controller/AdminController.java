package uz.texnomart.controller;


import org.telegram.telegrambots.meta.api.methods.send.SendDocument;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Contact;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.PhotoSize;
import uz.texnomart.container.Container;
import uz.texnomart.container.Container.*;
import uz.texnomart.service.AdminService;
import uz.texnomart.util.KeyboardButtonUtil;

import java.io.File;
import java.util.List;

import static uz.texnomart.container.Container.MY_BOT;
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
        String text = message.getText();

        System.out.println("text = " + text);

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
            sendMessage.setText("Assalom alaykum " + message.getFrom().getFirstName() + "!\nTexnomart botiga xush kelibsiz!");
            sendMessage.setReplyMarkup(KeyboardButtonUtil.getAdminMenu());
            MY_BOT.sendMsg(sendMessage);
        }else if (text.equals(_SHOW_USERS_)){
            AdminService.showUsersAsPDF();
            File file = new File(Container.BASE_FOLDER, "customer.pdf");
            sendDocument.setDocument(new InputFile(file));
            MY_BOT.sendMsg(sendDocument);
            file.delete();
        }else if (text.equals(_EDIT_ADMIN_)){
            AdminService.showUsersAsPDF();
            File file = new File(Container.BASE_FOLDER, "customer.pdf");
            sendDocument.setDocument(new InputFile(file));
            MY_BOT.sendMsg(sendDocument);
            sendDocument.setCaption("Admin qilmoqchi bo'lgan foydalanuvchining ID sini kiriting.");
            MY_BOT.sendMsg(sendDocument);
            file.delete();
        }else if (text.equals(_SEND_ADS_)){
            sendMessage.setText("🖼️ Reklamangizni jo'nating.");

        }else if (text.equals(_DISCOUNT_)){

        }else if (text.equals(_CATEGORIES_)){

        }else if (text.equals(_PRODUCTS_)){

        } else if (text.equals(_ORDER_LIST_)) {

        }else if (text.equals(_SHOW_MESSAGES_)){

        }

    }


    public static void handleCallBackQuery(Message message, String data) {

    }
}