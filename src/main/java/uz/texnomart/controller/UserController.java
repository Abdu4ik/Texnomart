package uz.texnomart.controller;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Contact;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;
import uz.texnomart.container.Container;
import uz.texnomart.db.WorkWithDatabase;
import uz.texnomart.entity.TelegramUser;
import uz.texnomart.enums.UserRoles;
import uz.texnomart.service.AdminService;
import uz.texnomart.util.KeyboardButtonUtil;


public class UserController {
    private static final String url = "jdbc:postgresql://ec2-54-75-26-218.eu-west-1.compute.amazonaws.com:5432/dae44hkoegn6lq";
    private static final String dbuser = "utpvoxxsoitfbq";
    private static final String dbpassword = "0ae03e88b14ced6a6e431080225030545efe9af022cc14f62fb96346a3a16ea5";

    public static void handleMessage(Update update) {

        Message message = update.getMessage();
        User user = update.getMessage().getFrom();

        if (message.hasContact()) {
            handleContact(user,message, message.getContact());
        }
        if (message.hasText()) {
            handleText(message, message.getText());
        }

    }

    private static void handleText(Message message, String text) {

        String chatId = String.valueOf(message.getChatId());

        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);

        if ("/start".equals(text)) {

            if (WorkWithDatabase.doesExist(chatId)){
                sendMessage.setText("Hurmatli mijoz xush kelibsiz ... 🎉🎉");
                sendMessage.setReplyMarkup(KeyboardButtonUtil.getUserMenu());
                Container.MY_BOT.sendMsg(sendMessage);
            } else if (!WorkWithDatabase.doesExist(chatId)) {

                sendMessage.setText("Assalamu Aleykum \n O'z kontaktingizni jo'natish tugmasi orqali jo'nating");
                sendMessage.setReplyMarkup(KeyboardButtonUtil.getContactMenu());
                Container.MY_BOT.sendMsg(sendMessage);
            }
        } else {
            sendMessage.setText("Nimadir xato ketti, qayta urunib ko'ring\nyoki /help yoki /start ni bosing");
            Container.MY_BOT.sendMsg(sendMessage);
        }
    }


    private static void handleContact( User user ,Message message, Contact contact) {

        String chatId = String.valueOf(message.getChatId());

        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);

             sendMessage.setText("""
                     Siz Bizning botimizda yangi ekansiz
                      Biz Sizni Ro'yhatga olib qoydik!!!\s
                     Hush kelibsiz ....""");
             WorkWithDatabase.addUsers(new TelegramUser(chatId,user.getFirstName()+user.getLastName(),contact.getPhoneNumber(), UserRoles.USER));
             sendMessage.setReplyMarkup(KeyboardButtonUtil.getUserMenu());
             Container.MY_BOT.sendMsg(sendMessage);

    }

    public static void handleCallBackQuery(Message message, String data) {

    }
}
