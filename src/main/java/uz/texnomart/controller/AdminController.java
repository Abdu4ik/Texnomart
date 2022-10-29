package uz.texnomart.controller;


import org.telegram.telegrambots.meta.api.methods.send.SendDocument;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
import org.telegram.telegrambots.meta.api.objects.Contact;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.PhotoSize;
import uz.texnomart.container.Container;
import uz.texnomart.container.Container.*;
import uz.texnomart.db.WorkWithDatabase;
import uz.texnomart.entity.Advertisement;
import uz.texnomart.enums.AdminStatus;
import uz.texnomart.service.AdminService;
import uz.texnomart.util.InlineKeyboardButtonConstants;
import uz.texnomart.util.InlineKeyboardButtonUtil;
import uz.texnomart.util.KeyboardButtonConstants;
import uz.texnomart.util.KeyboardButtonUtil;

import java.io.File;
import java.util.List;

import static uz.texnomart.container.Container.MY_BOT;
import static uz.texnomart.container.Container.adminMap;
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
        SendPhoto sendPhoto = new SendPhoto();
        sendPhoto.setChatId(message.getChatId());
        String photo = photoSizeList.get(photoSizeList.size() - 1).getFileId();
        String caption = message.getCaption();
        if (AdminService.checkAdminStatus(String.valueOf(message.getChatId()), AdminStatus.SEND_ADS)){
            WorkWithDatabase.addAdvert(new Advertisement(caption, photo, String.valueOf(message.getChatId())));
            sendPhoto.setPhoto(new InputFile(photo));
            sendPhoto.setCaption(caption+"\n\n Shu e'lonni barcha foydalanuvchilarga yurborasizmi?");
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
            AdminService.changeAdminStatus(chatId, AdminStatus.EDIT_ADMIN); // admin statusni Edit qilishga o'tkazib qo'yadi
            AdminService.showUsersAsPDF();
            File file = new File(Container.BASE_FOLDER, "customer.pdf");
            sendDocument.setDocument(new InputFile(file));
            sendDocument.setCaption("Admin qilmoqchi bo'lgan foydalanuvchining ID sini kiriting.");
            MY_BOT.sendMsg(sendDocument);
            file.delete();  // fileni jo'natgandan keyin darhol bazadan o'chirib yuboradi
        }else if (text.equals(_SEND_ADS_)){
            adminMap.put(chatId, null);
            AdminService.changeAdminStatus(chatId, AdminStatus.SEND_ADS);
            sendMessage.setText("🖼️ Reklamangizni jo'nating.");
            MY_BOT.sendMsg(sendMessage);
        }else if (text.equals(_DISCOUNT_)){

        }else if (text.equals(_CATEGORIES_)){

        }else if (text.equals(_PRODUCTS_)){

        } else if (text.equals(_ORDER_LIST_)) {

        }else if (text.equals(_SHOW_MESSAGES_)){

        }else if (AdminService.checkAdminStatus(String.valueOf(message.getChatId()), AdminStatus.SEND_ADS)){
            sendMessage.setText(text+"\n\n Shu e'lonni barcha foydalanuvchilarga yurborasizmi?");
            sendMessage.setReplyMarkup(InlineKeyboardButtonUtil.getConfirmationButtons());
        }

    }


    public static void handleCallBackQuery(Message message, String data) {
            if (data.equals(InlineKeyboardButtonConstants.YES_CALL) && AdminService.checkAdminStatus(String.valueOf(message.getChatId()), AdminStatus.SEND_ADS)){
                Advertisement adToBeSent = WorkWithDatabase.getAdFromDB(String.valueOf(message.getChatId()));
                AdminService.sendAdsToAllCustomers(adToBeSent.getPhoto(), adToBeSent.getCaption());
                DeleteMessage deleteMessage = new DeleteMessage(String.valueOf(message.getChatId()), message.getMessageId());
                MY_BOT.sendMsg(deleteMessage);
            }
    }
}