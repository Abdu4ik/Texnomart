package uz.texnomart.controller;

import org.telegram.telegrambots.meta.api.methods.AnswerCallbackQuery;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
import org.telegram.telegrambots.meta.api.objects.*;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardRemove;
import uz.texnomart.container.Container;
import uz.texnomart.db.WorkWithDatabase;
import uz.texnomart.entity.Discount;
import uz.texnomart.entity.TelegramUser;
import uz.texnomart.entity.UserProduct;
import uz.texnomart.util.InlineKeyboardButtonConstants;
import uz.texnomart.util.InlineKeyboardButtonUtil;
import uz.texnomart.util.KeyboardButtonConstants;
import uz.texnomart.util.KeyboardButtonUtil;

import java.util.List;

import static uz.texnomart.container.Container.*;
import static uz.texnomart.container.Container.userBaskets;
import static uz.texnomart.db.WorkWithDatabase.changeActive;
import static uz.texnomart.util.InlineKeyboardButtonConstants.*;
import static uz.texnomart.util.KeyboardButtonConstants.*;


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
                sendMessage.setText("Menu: ");
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

                    String str = WorkWithDatabase.getBasket(chatId);

                    String numberOfItems = str.split(" : ")[0];
                    String totalPrice = str.split(" : ")[1];
                    String basketId = str.split(" : ")[2];
                    if (numberOfItems.equals("0")){
                        sendMessage.setText("Savatingizda hech narsa yo'q");
                        MY_BOT.sendMsg(sendMessage);
                    }else {
                        sendMessage.setText("Sizning savatingizda 🛒: " +
                                "\n\nMahsulot turlari soni 🎆:   " + numberOfItems
                                + "\nUmumiy summa 🎇:   " + totalPrice);
                        sendMessage.setReplyMarkup(InlineKeyboardButtonUtil.getConfirmOrCancelMenu(basketId));
                        Container.MY_BOT.sendMsg(sendMessage);
                    }

                }else if (text.equals(BACK)) {
                    sendMessage.setText("Menu");
                    sendMessage.setReplyMarkup(KeyboardButtonUtil.getUserMenu());
                    MY_BOT.sendMsg(sendMessage);
                } else if (text.equals(DISCOUNTED_PRODUCTS)) {
                    sendMessage.setText("Tanlang: ");
                    sendMessage.setReplyMarkup(KeyboardButtonUtil.getDiscountedProductsMenu());
                    MY_BOT.sendMsg(sendMessage);
                } else if (text.equals(SHOW_DISCOUNTED_PRODUCTS)) {
                    WorkWithDatabase.deleteExpiredDiscounts();
                    discountList = WorkWithDatabase.getDiscountsList(chatId);

                    for (int i = 0; i < discountList.size(); i++) {
                        if (discountList.get(i).getChatId().equals(chatId)) {
                            if (discountList.get(i).getPhoto_file_id() != null) {
                                SendPhoto sendPhoto = new SendPhoto();
                                sendPhoto.setChatId(chatId);
                                sendPhoto.setPhoto(new InputFile(discountList.get(i).getPhoto_file_id()));
                                StringBuilder photoCaption = new StringBuilder();

                                if (discountList.get(i).getName() != null) {
                                    photoCaption.append(discountList.get(i).getName()).append("\n\n");
                                }

                                photoCaption.append("Ushbu ").append(discountList.get(i).getDiscount_percentage())
                                        .append("% lik chegirmamiz bilan mahsulotlarimizni faqat ").append(discountList.get(i).getStart_time())
                                        .append(" dan ").append(discountList.get(i).getEnd_time()).append(" gacha vaqtda xarid qilishga ulgurib qoling\uD83E\uDD29");

                                sendPhoto.setCaption(String.valueOf(photoCaption));
                                sendPhoto.setReplyMarkup(InlineKeyboardButtonUtil.getPrevNextButton(i));
                                MY_BOT.sendMsg(sendPhoto);
                                break;
                            } else {
                                String discountText = discountList.get(i).getName() + "\n\nUshbu " +
                                        discountList.get(i).getDiscount_percentage() +
                                        "% lik chegirmamiz bilan mahsulotlarimizni faqat " +
                                        discountList.get(i).getStart_time() +
                                        " dan " +
                                        discountList.get(i).getEnd_time() + " gacha vaqtda xarid qilishga ulgurib qoling\uD83E\uDD29";
                                sendMessage.setText(discountText);
                                sendMessage.setReplyMarkup(InlineKeyboardButtonUtil.getPrevNextButton(i));
                                MY_BOT.sendMsg(sendMessage);
                                break;
                            }
                        }
                    }
                } else if(text.equals(KeyboardButtonConstants.MESSAGE_ADMIN)){

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

    public static void handleCallBackQuery(Message message, String data, String callbackQueryId) {
        String chatId = String.valueOf(message.getChatId());

        SendMessage sendMessage = new SendMessage();
        DeleteMessage deleteMessage = new DeleteMessage(chatId, message.getMessageId());
        sendMessage.setChatId(chatId);

        if (data.startsWith("details_")){

            String str = data.split("/")[0];
            int basketId = Integer.parseInt(data.split("/")[1]);
            List<UserProduct> userProducts = WorkWithDatabase.getBasketDetails(basketId);
            Container.userBaskets.put(chatId, userProducts);

            if (str.equals(InlineKeyboardButtonConstants.DETAILS_CALL_BACK)){

                SendPhoto sendPhoto = showProducts(userProducts, 0);
                sendPhoto.setChatId(chatId);
                Container.countButtons.put(chatId, 1);
                sendPhoto.setReplyMarkup(InlineKeyboardButtonUtil.getProductButtons(userProducts.get(0).getId()));
                Container.MY_BOT.sendMsg(sendPhoto);
                Container.MY_BOT.sendMsg(deleteMessage);
            }else if (str.equals(InlineKeyboardButtonConstants.CONFIRM_CALL_BACK)){
                WorkWithDatabase.updateBasket(basketId);

                sendMessage.setText("Buyurtmangiz tasdiqlandi. To'lovni amalaga oshirish uchun admin bilan bo'glaning!");
                MY_BOT.sendMsg(sendMessage);
                MY_BOT.sendMsg(deleteMessage);
            }else if (str.equals(InlineKeyboardButtonConstants.CANCEL_CALL_BACK)){
                WorkWithDatabase.clearBasket(basketId);

                sendMessage.setText("Savatingiz tozalandi!");
                MY_BOT.sendMsg(sendMessage);
                MY_BOT.sendMsg(deleteMessage);
            }else if (str.equals(InlineKeyboardButtonConstants.BACK1_CALL_BACK)){
                String str1 = WorkWithDatabase.getBasket(chatId);

                String numberOfItems = str1.split(" : ")[0];
                String totalPrice = str1.split(" : ")[1];
                String basketId1 = str1.split(" : ")[2];

                sendMessage.setText("Sizning savatingizda 🛒: " +
                        "\n\nMahsulot turlari soni 🎆:   " + numberOfItems
                        + "\nUmumiy summa 🎇:   " + totalPrice);
                sendMessage.setReplyMarkup(InlineKeyboardButtonUtil.getConfirmOrCancelMenu(basketId1));
                MY_BOT.sendMsg(sendMessage);
                MY_BOT.sendMsg(deleteMessage);
            }


        }else if (data.equals(CANCEL_CALL)) {
            discountList.removeIf(discount -> discount.getChatId().equals(chatId));
            DeleteMessage deleteMessage1 = new DeleteMessage(chatId, message.getMessageId());
            MY_BOT.sendMsg(deleteMessage1);
        } else if (data.startsWith("_oldingi_")) {
            data = data.substring(data.lastIndexOf("/") + 1);
            int index = Integer.parseInt(data);

            boolean isExist=false;

            for (int i = 0; i < discountList.size(); i++) {
                Discount discount = discountList.get(i);

                if (discount.getChatId().equals(chatId) && i < index) {
                    isExist=true;

                    DeleteMessage deleteMessage1 = new DeleteMessage(chatId, message.getMessageId());
                    MY_BOT.sendMsg(deleteMessage1);

                    if (discount.getPhoto_file_id() != null) {
                        SendPhoto sendPhoto = new SendPhoto();
                        sendPhoto.setChatId(chatId);
                        sendPhoto.setPhoto(new InputFile(discount.getPhoto_file_id()));
                        StringBuilder photoCaption = new StringBuilder();

                        if (discount.getName() != null) {
                            photoCaption.append(discount.getName()).append("\n\n");
                        }

                        photoCaption.append("Ushbu ").append(discount.getDiscount_percentage())
                                .append("% lik chegirmamiz bilan mahsulotlarimizni faqat ").append(discount.getStart_time())
                                .append(" dan ").append(discount.getEnd_time()).append(" gacha vaqtda xarid qilishga ulgurib qoling\uD83E\uDD29");

                        sendPhoto.setCaption(String.valueOf(photoCaption));
                        sendPhoto.setReplyMarkup(InlineKeyboardButtonUtil.getPrevNextButton(i));
                        MY_BOT.sendMsg(sendPhoto);
                        break;
                    } else {
                        sendMessage.setChatId(chatId);
                        String discountText = discount.getName() + "\n\nUshbu " +
                                discount.getDiscount_percentage() +
                                "% lik chegirmamiz bilan mahsulotlarimizni faqat " +
                                discount.getStart_time() +
                                " dan " + discount.getEnd_time() + " gacha vaqtda xarid qilishga ulgurib qoling\uD83E\uDD29";
                        sendMessage.setText(discountText);
                        sendMessage.setReplyMarkup(InlineKeyboardButtonUtil.getPrevNextButton(i));
                        MY_BOT.sendMsg(sendMessage);
                        break;
                    }
                }
            }

            if (!isExist) {
                AnswerCallbackQuery answerCallbackQuery = new AnswerCallbackQuery();
                answerCallbackQuery.setCallbackQueryId(callbackQueryId);
                answerCallbackQuery.setText("Siz birinchi betdasiz❕");
                answerCallbackQuery.setShowAlert(false);
                MY_BOT.sendMsg(answerCallbackQuery);
            }
        } else if (data.startsWith("_keyingi_")) {
            data = data.substring(data.lastIndexOf("/") + 1);
            int index = Integer.parseInt(data);

            boolean isExist=false;

            for (int i = 0; i < discountList.size(); i++) {
                Discount discount = discountList.get(i);
                if (discount.getChatId().equals(chatId) && i > index) {
                    isExist=true;

                    DeleteMessage deleteMessage1 = new DeleteMessage(chatId, message.getMessageId());
                    MY_BOT.sendMsg(deleteMessage1);

                    if (discount.getPhoto_file_id() != null) {
                        SendPhoto sendPhoto = new SendPhoto();
                        sendPhoto.setChatId(chatId);
                        sendPhoto.setPhoto(new InputFile(discount.getPhoto_file_id()));
                        StringBuilder photoCaption = new StringBuilder();

                        if (discount.getName() != null) {
                            photoCaption.append(discount.getName()).append("\n\n");
                        }

                        photoCaption.append("Ushbu ").append(discount.getDiscount_percentage())
                                .append("% lik chegirmamiz bilan mahsulotlarimizni faqat ").append(discount.getStart_time())
                                .append(" dan ").append(discount.getEnd_time()).append(" gacha vaqtda xarid qilishga ulgurib qoling\uD83E\uDD29");

                        sendPhoto.setCaption(String.valueOf(photoCaption));
                        sendPhoto.setReplyMarkup(InlineKeyboardButtonUtil.getPrevNextButton(i));
                        MY_BOT.sendMsg(sendPhoto);
                        break;
                    } else {
                        sendMessage.setChatId(chatId);
                        String discountText = discount.getName() + "\n\nUshbu " +
                                discount.getDiscount_percentage() +
                                "% lik chegirmamiz bilan mahsulotlarimizni faqat " +
                                discount.getStart_time() +
                                " dan " + discount.getEnd_time() + " gacha vaqtda xarid qilishga ulgurib qoling\uD83E\uDD29";
                        sendMessage.setText(discountText);
                        sendMessage.setReplyMarkup(InlineKeyboardButtonUtil.getPrevNextButton(i));
                        MY_BOT.sendMsg(sendMessage);
                        break;
                    }
                }
            }

            if (!isExist) {
                AnswerCallbackQuery answerCallbackQuery = new AnswerCallbackQuery();
                answerCallbackQuery.setCallbackQueryId(callbackQueryId);
                answerCallbackQuery.setText("Siz oxirgi betdasiz❕");
                answerCallbackQuery.setShowAlert(false);
                MY_BOT.sendMsg(answerCallbackQuery);
            }
} else if (data.startsWith("change")){
            int bdId = Integer.parseInt(data.split(":")[1]);
            int quantity = WorkWithDatabase.getQuantityOfBasketDetail(bdId);
            if (data.startsWith(InlineKeyboardButtonConstants.INCREASE_CALL_BACK) && quantity<5){
                WorkWithDatabase.increaseQuantityOfBasketDetail(bdId);
            }else if (data.startsWith(InlineKeyboardButtonConstants.DECREASE_CALL_BACK) && quantity >=2){
                WorkWithDatabase.decreaseQuantityOfBasketDetail(bdId);
            }
            int basketId = WorkWithDatabase.getBasketId(chatId);
            List<UserProduct> userProducts = WorkWithDatabase.getBasketDetails(basketId);
            SendPhoto sendPhoto = showProducts(userProducts, 0);
            sendPhoto.setChatId(chatId);
            sendPhoto.setReplyMarkup(InlineKeyboardButtonUtil.getProductButtons(userProducts.get(0).getId()));
            Container.MY_BOT.sendMsg(sendPhoto);
            Container.MY_BOT.sendMsg(deleteMessage);
        }else if (data.startsWith(InlineKeyboardButtonConstants.UP_CALL_BACK)){
            int temp = Container.countButtons.get(chatId);
            SendPhoto sendPhoto = showProducts(userBaskets.get(chatId), countButtons.get(chatId));
            sendPhoto.setChatId(chatId);
            if (temp != userBaskets.get(chatId).size()-1){
                Container.countButtons.put(chatId, temp+1);
            }
            sendPhoto.setReplyMarkup(InlineKeyboardButtonUtil.getProductButtons2(userBaskets.get(chatId).get(0).getId()));
            Container.MY_BOT.sendMsg(sendPhoto);
            Container.MY_BOT.sendMsg(deleteMessage);
        }else if (data.startsWith(InlineKeyboardButtonConstants.DOWN_CALL_BACK)){
            int temp = Container.countButtons.get(chatId);
            SendPhoto sendPhoto = showProducts(userBaskets.get(chatId), countButtons.get(chatId));
            sendPhoto.setChatId(chatId);
            if (temp != 0){
                Container.countButtons.put(chatId, temp-1);
            }
            sendPhoto.setReplyMarkup(InlineKeyboardButtonUtil.getProductButtons1(userBaskets.get(chatId).get(0).getId()));
            Container.MY_BOT.sendMsg(sendPhoto);
            Container.MY_BOT.sendMsg(deleteMessage);
        }
    }

    private static SendPhoto showProducts(List<UserProduct> userProducts, int i) {
        SendPhoto sendPhoto = new SendPhoto();

        sendPhoto.setPhoto(new InputFile(userProducts.get(i).getPhoto_file_url()));
        String photoCaption = "Mahsulot nomi - " +
                userProducts.get(i).getName() +
                "\n\nMahsulot narxi - " +
                userProducts.get(i).getPrice() +
                "\n\nMahsulot rangi - " +
                userProducts.get(i).getColor() +
                "\n\nTanlangan - " +
                userProducts.get(i).getQuantity();

        sendPhoto.setCaption(photoCaption);
        return sendPhoto;
    }
}
