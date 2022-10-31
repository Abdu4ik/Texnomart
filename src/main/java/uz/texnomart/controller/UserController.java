package uz.texnomart.controller;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Contact;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardRemove;
import uz.texnomart.container.Container;
import uz.texnomart.db.WorkWithDatabase;
import uz.texnomart.entity.TelegramUser;
import uz.texnomart.enums.UserRoles;
import uz.texnomart.service.AdminService;
import uz.texnomart.util.InlineKeyboardButtonUtil;
import uz.texnomart.util.KeyboardButtonConstants;
import uz.texnomart.util.KeyboardButtonUtil;

import static uz.texnomart.db.WorkWithDatabase.changeActive;


public class UserController {

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
        boolean active = false;

        TelegramUser customer = WorkWithDatabase.getCustomerByChatId(chatId);

        if (text.equals("/start")) {
            if (customer == null) {

                sendMessage.setText("Assalomu alaykum!👋\n Botdan to'liq foydalanish uchun telefon raqamingizni jo'nating: ");
                sendMessage.setReplyMarkup(KeyboardButtonUtil.getContactMenu());
                Container.MY_BOT.sendMsg(sendMessage);

            } else{
                sendMessage.setText("Choose: ");
                sendMessage.setReplyMarkup(KeyboardButtonUtil.getUserMenu());
                Container.MY_BOT.sendMsg(sendMessage);
            }
        }else{
            if(customer == null){
                sendMessage.setText("Botdan to'liq foydalanish uchun telefon raqamingizni jo'nating: ");
                sendMessage.setReplyMarkup(KeyboardButtonUtil.getContactMenu());
                Container.MY_BOT.sendMsg(sendMessage);
            }else {
                if (customer.isActive()){
                    if (!text.trim().isBlank()){
                        WorkWithDatabase.setFullName(chatId, text);
                    }
                    changeActive(chatId, active);
                    sendMessage.setText("Menu");
                    sendMessage.setReplyMarkup(KeyboardButtonUtil.getUserMenu());
                    Container.MY_BOT.sendMsg(sendMessage);
                }else if(text.equals(KeyboardButtonConstants.SHOW_BASKET)){

                    sendMessage.setText("Sizning savatingiz bo'sh");
                    Container.MY_BOT.sendMsg(sendMessage);

                    //ToDo

                }else if(text.equals(KeyboardButtonConstants.MESSAGE_ADMIN)){

                    Container.customerMap.put(chatId, true);
                    WorkWithDatabase.addMessageData(chatId);

                    sendMessage.setText("Xabaringizni kiriting: ");
                    Container.MY_BOT.sendMsg(sendMessage);
                }else if(Container.customerMap.containsKey(chatId)) {

                    WorkWithDatabase.addMessage(text, chatId);
                    Container.customerMap.remove(chatId);

                    sendMessage.setText("✅ Xabaringiz adminga jo'natildi.");
                    Container.MY_BOT.sendMsg(sendMessage);

                } else {
                    sendMessage.setText("Noto'g'ri xabar kiritildi❌");
                    Container.MY_BOT.sendMsg(sendMessage);
                }
            }
        }
    }


    private static void handleContact(User user, Message message, Contact contact) {
        String chatId = String.valueOf(message.getChatId());
        TelegramUser customer = WorkWithDatabase.getCustomerByChatId(chatId);
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);

        if(!contact.getPhoneNumber().matches("(\\+)?998\\d{9}")){
            sendMessage.setText("Telefon raqamda xatolik❗ ");
            sendMessage.setReplyMarkup(KeyboardButtonUtil.getContactMenu());
            Container.MY_BOT.sendMsg(sendMessage);
            return;
        }

        if (customer == null) {
            boolean active = true;
            WorkWithDatabase.addCustomer(chatId, contact);
            sendMessage.setText("Sizga murojaat qilishimiz uchun to'liq ismingizni jo'nating:");
            sendMessage.setReplyMarkup(new ReplyKeyboardRemove(true));
            Container.MY_BOT.sendMsg(sendMessage);
            changeActive(chatId, active);

        }
    }

    public static void handleCallBackQuery(Message message, String data) {

    }
}
