package uz.texnomart.controller;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Contact;
import org.telegram.telegrambots.meta.api.objects.Message;
import uz.texnomart.entity.TelegramUser;


public class    UserController {
    public static void handleMessage(Message message) {

        if (message.hasContact()) {
            handleContact(message, message.getContact());
        }
        if (message.hasText()) {
            handleText(message, message.getText());
        }

    }

    private static void handleText(Message message, String text) {
        String chatId = String.valueOf(message.getChatId());
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);

//        if ("/start".equals(text)) {
//                TelegramUser customer=
//            if (customer == null) {
//                sendMessage.setText("Assalomu alaykum!");
//                sendMessage.setReplyMarkup(KeyboardButtonUtil.getContactMenu());
//                ComponentContainer.MY_BOT.sendMsg(sendMessage);
//            } else if (customer.isActive()) {
//                UserService.getBasicMenu(chatId);
//
//            }
//        }

    }



    private static void handleContact(Message message, Contact contact) {

    }

    public static void handleCallBackQuery(Message message, String data) {

    }
}
