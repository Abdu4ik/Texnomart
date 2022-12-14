package uz.texnomart.controller;


import org.telegram.telegrambots.meta.api.methods.send.SendDocument;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.Contact;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.PhotoSize;
import uz.texnomart.bot.MyBot;
import uz.texnomart.container.Container;
import uz.texnomart.db.WorkWithDatabase;
import uz.texnomart.entity.*;
import uz.texnomart.enums.AdminStatus;
import uz.texnomart.service.AdminService;
import uz.texnomart.service.WorkWithFiles;
import uz.texnomart.util.InlineKeyboardButtonConstants;
import uz.texnomart.util.InlineKeyboardButtonUtil;

import static uz.texnomart.container.Container.*;
import static uz.texnomart.util.InlineKeyboardButtonConstants.*;

import uz.texnomart.util.KeyboardButtonUtil;

import java.io.File;
import java.time.LocalDate;
import java.util.List;

import static uz.texnomart.util.KeyboardButtonConstants.*;

public class AdminController {
    public static void handleMessage(Message message) {
        if (AdminService.checkAdminStatus(String.valueOf(message.getChatId()), AdminStatus.SEND_ADS)){
            AdminService.sendAdsToAllCustomers(message);
            MY_BOT.sendMsg(new SendMessage(String.valueOf(message.getChatId()), "Reklama barcha foydalanuvchilarga yuborildi! ✅"));
            adminMap.put(String.valueOf(message.getChatId()), null);
        }else if (message.hasText()) {
            handleText(message);
        } else if (message.hasContact()) {
            handleContact(message, message.getContact());
        } else if (message.hasPhoto()) {
            handlePhoto(message, message.getPhoto());
        }
    }

    private static void handlePhoto(Message message, List<PhotoSize> photoSizeList) {
        if (AdminService.checkAdminStatus(String.valueOf(message.getChatId()), AdminStatus.SEND_ADS)) {
            SendPhoto sendPhoto = new SendPhoto();
            sendPhoto.setChatId(message.getChatId());
            String photo = photoSizeList.get(photoSizeList.size() - 1).getFileId();
            String caption = message.getCaption();

            WorkWithDatabase.addAdvert(new Advertisement(caption, photo, String.valueOf(message.getChatId())));
            sendPhoto.setPhoto(new InputFile(photo));
            sendPhoto.setCaption(caption + "\n\n Shu e'lonni barcha foydalanuvchilarga yurborasizmi?");
            sendPhoto.setReplyMarkup(InlineKeyboardButtonUtil.getConfirmationButtons());
            MY_BOT.sendMsg(sendPhoto);
        }

        if (AdminService.checkAdminStatus(String.valueOf(message.getChatId()), AdminStatus.ADD_PRODUCT)){
            SendPhoto sendPhoto = new SendPhoto();
            sendPhoto.setChatId(message.getChatId());
            String photo = photoSizeList.get(photoSizeList.size() - 1).getFileId();




        }

        if (AdminService.checkAdminStatus(String.valueOf(message.getChatId()), AdminStatus.ADD_DISCOUNT)) {
            String chatId = String.valueOf(message.getChatId());
            String fileId = photoSizeList.get(photoSizeList.size() - 1).getFileId();
            String discountName = message.getCaption();
            SendMessage sendMessage = new SendMessage();
            sendMessage.setChatId(chatId);

            boolean isDiscountExist = false;
            for (Discount discount : Container.discountList) {
                if (discount.getChatId().equals(chatId)) {
                    discount.setChatId(chatId);
                    discount.setName(discountName);
                    discount.setPhoto_file_id(fileId);
                    isDiscountExist = true;
                    break;
                }
            }
            if (!isDiscountExist)
                Container.discountList.add(new Discount(chatId, null, discountName, null, null, fileId));

            sendMessage.setText("Chegirma foizini kiriting: ");
            AdminService.changeAdminStatus(chatId, AdminStatus.SEND_DISCOUNT_PERCENTAGE);
            MY_BOT.sendMsg(sendMessage);
        }

    }


    private static void handleContact(Message message, Contact contact) {

    }

    private static void handleText(Message message) {
        String text = message.getText();
        String chatId = String.valueOf(message.getChatId());
        DeleteMessage deleteMessage = new DeleteMessage(chatId, message.getMessageId());
        SendDocument sendDocument = new SendDocument();
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);
        sendDocument.setChatId(chatId);
        if (text.equals("/start")) {
            AdminService.putAminsIntoMap(chatId);
            sendMessage.setText("Assalom alaykum hurmatli adminimiz " + message.getFrom().getFirstName() + "!\nTexnomart botiga xush kelibsiz!");
            sendMessage.setReplyMarkup(KeyboardButtonUtil.getAdminMenu());
            MY_BOT.sendMsg(sendMessage);
        } else if ((AdminService.checkAdminStatus(chatId, AdminStatus.ADMIN_CRUD) ||
                AdminService.checkAdminStatus(chatId, AdminStatus.REMOVE_ADMIN) ||
                AdminService.checkAdminStatus(chatId, AdminStatus.ADD_ADMIN)
        ) && text.equals(BACK)) {
            adminMap.put(chatId, null);
            sendMessage.setText("Asosiy menyu:");
            sendMessage.setReplyMarkup(KeyboardButtonUtil.getAdminMenu());
            MY_BOT.sendMsg(sendMessage);
        } else if (text.equals(BACK)) {
            AdminService.changeAdminStatus(chatId, null);
            sendMessage.setText("Menu: ");
            sendMessage.setReplyMarkup(KeyboardButtonUtil.getAdminMenu());
            MY_BOT.sendMsg(sendMessage);
        } else if (AdminService.checkAdminStatus(chatId, AdminStatus.REMOVE_ADMIN)) {
            sendMessage.setText(WorkWithDatabase.takeAdminPrivilege(text));
            sendMessage.setReplyMarkup(KeyboardButtonUtil.getAdminMenu());
            MY_BOT.sendMsg(sendMessage);
        } else if (AdminService.checkAdminStatus(chatId, AdminStatus.ADD_ADMIN)) {
            sendMessage.setText(WorkWithDatabase.grantAdminPrivilege(text));
            sendMessage.setReplyMarkup(KeyboardButtonUtil.getAdminMenu());
            MY_BOT.sendMsg(sendMessage);
        } else if (text.equals(_ADD_ADMIN_)) {
            AdminService.changeAdminStatus(chatId, AdminStatus.ADD_ADMIN);
            sendMessage.setText("Yuqoridagi fayldan👆 foydalanib adminlik rolini\nbermoqchi bo'lgan foydalanuvchining chat ID sini kiriting.");
            MY_BOT.sendMsg(sendMessage);
        } else if (text.equals(_REMOVE_ADMIN_)) {
            AdminService.changeAdminStatus(chatId, AdminStatus.REMOVE_ADMIN);
            sendMessage.setText("Yuqoridagi fayldan👆 foydalanib adminlik rolidan\nmahrum qilmoqchi bo'lgan foydalanuvchining chat ID sini kiriting.");
            MY_BOT.sendMsg(sendMessage);
        } else if (text.equals(_SHOW_USERS_)) {
            AdminService.changeAdminStatus(chatId, AdminStatus.SHOW_USERS);
            AdminService.showUsersAsPDF();
            File file = new File(BASE_FOLDER, "Foydalanuvchilar rõyxati.pdf");
            sendDocument.setDocument(new InputFile(file));
            MY_BOT.sendMsg(sendDocument);
            file.delete();
        } else if (text.equals(_ADMIN_CRUD_)) {
            AdminService.changeAdminStatus(chatId, AdminStatus.ADMIN_CRUD); // admin statusni Edit qilishga o'tkazib qo'yadi
            AdminService.showUsersAsPDF();
            File file = new File(BASE_FOLDER, "Foydalanuvchilar rõyxati.pdf");
            sendDocument.setDocument(new InputFile(file));
            sendDocument.setReplyMarkup(KeyboardButtonUtil.getEditAdminMenu());
            sendDocument.setCaption("Admin qo'shish yoki adminlik huquqini olib qo'yish uchun sizga yuqoridagi ma'lumot kerak bo'ladi.");
            MY_BOT.sendMsg(sendDocument);
            file.delete();
        } else if (text.equals(_SEND_ADS_)) {
            adminMap.put(chatId, null);
            AdminService.changeAdminStatus(chatId, AdminStatus.SEND_ADS);
            sendMessage.setText("🖼️ Reklamangizni jo'nating." +
                    "\n\n" +
                    "⚠️Diqqat! Reklama siz tomondan jo'natilgani zahoti barcha foydalanuvchilarga yuboriladi!" +
                    " Shuni hisobga olib appropriate va correct reklama yuboring!");
            MY_BOT.sendMsg(sendMessage);
        } else if (text.equals(_DISCOUNT_)) {
            AdminService.changeAdminStatus(chatId, AdminStatus.DISCOUNT);
            sendMessage.setText("Tanlang: ");
            sendMessage.setReplyMarkup(KeyboardButtonUtil.getDiscountMenu());
            MY_BOT.sendMsg(sendMessage);
        } else if (text.equals(_ADD_NEW_DISCOUNT_) && AdminService.checkAdminStatus(chatId, AdminStatus.DISCOUNT)) {
            AdminService.changeAdminStatus(chatId, AdminStatus.ADD_DISCOUNT);
            sendMessage.setText("Chegirma rasmini yoki nomini yoki rasmi bilan nomini jo'nating");
            MY_BOT.sendMsg(sendMessage);
        } else if (text.equals(_DELETE_DISCOUNT_) && AdminService.checkAdminStatus(chatId, AdminStatus.DISCOUNT)) {
            AdminService.changeAdminStatus(chatId, AdminStatus.DELETE_DISCOUNT);
            List<Discount> notDeletedDiscounts = WorkWithDatabase.getNotDeletedDiscounts(chatId);
            if (notDeletedDiscounts.size() != 0) {
                for (Discount discount : notDeletedDiscounts) {
                    if (discount.getPhoto_file_id() != null) {
                        SendPhoto sendPhoto = new SendPhoto();
                        sendPhoto.setChatId(discount.getChatId());
                        sendPhoto.setPhoto(new InputFile(discount.getPhoto_file_id()));
                        StringBuilder photoCaption = new StringBuilder();

                        if (discount.getName() != null) {
                            photoCaption.append(discount.getName()).append("\n\n");
                        }

                        photoCaption.append("Chegirma foizi: ").append(discount.getDiscount_percentage())
                                .append("\n\nBoshlanish vaqti: ").append(discount.getStart_time())
                                .append("\n\nYakunlanish vaqti: ").append(discount.getEnd_time());

                        sendPhoto.setCaption(String.valueOf(photoCaption));
                        sendPhoto.setReplyMarkup(InlineKeyboardButtonUtil.getDiscountDeleteButton(discount.getId()));
                        MY_BOT.sendMsg(sendPhoto);
                    } else {
                        String discountText = discount.getName() + "\n\nChegirma foizi: " +
                                discount.getDiscount_percentage() +
                                "\n\nBoshlanish vaqti: " +
                                discount.getStart_time() +
                                "\n\nYakunlanish vaqti: " +
                                discount.getEnd_time();
                        sendMessage.setText(discountText);
                        sendMessage.setReplyMarkup(InlineKeyboardButtonUtil.getDiscountDeleteButton(discount.getId()));
                        MY_BOT.sendMsg(sendMessage);
                    }
                }
            } else {
                sendMessage.setText("Chegirmalar mavjud emas");
                MY_BOT.sendMsg(sendMessage);
            }
        } else if (text.equals(_ADD_CATEGORIES_)) {
            AdminService.changeAdminStatus(chatId, AdminStatus.ADD_CATEGORIES);
            sendMessage.setText("Tanlang:");
            sendMessage.setReplyMarkup(KeyboardButtonUtil.getCRUDCategoryMenu());
            MY_BOT.sendMsg(sendMessage);
        }else if (text.equals(_ADD_PARENT_C_) && AdminService.checkAdminStatus(chatId, AdminStatus.ADD_CATEGORIES)){
            AdminService.changeAdminStatus(chatId, AdminStatus.ADD_PARENT_C);
            sendMessage.setText("Yangi ota kategoriyaning nomini kiriting: ");
            MY_BOT.sendMsg(sendMessage);
        }else if (AdminService.checkAdminStatus(chatId, AdminStatus.ADD_PARENT_C)){
            // Todo write a method that will take .text. which will be the new parent category and create a new parent category

            sendMessage.setText(WorkWithDatabase.createNewParentCategory(text));
            MY_BOT.sendMsg(sendMessage);
        }else if (text.equals(_ADD_SUB_C_) && AdminService.checkAdminStatus(chatId, AdminStatus.ADD_CATEGORIES)){
            AdminService.changeAdminStatus(chatId, AdminStatus.ADD_CHILD_C);
            sendMessage.setText("Qaysi ota kategoriyaga sub kategoriya qo'shmoqchisiz?");
            sendMessage.setReplyMarkup(InlineKeyboardButtonUtil.getCategoryButtonsForUser(WorkWithDatabase.parentCategoryList()));
            MY_BOT.sendMsg(sendMessage);
        }else if (AdminService.checkAdminStatus(chatId, AdminStatus.ADD_CHILD_C)){
            sendMessage.setText(WorkWithDatabase.createNewSubCategory(text, parent_c_id));
            MY_BOT.sendMsg(sendMessage);
        } else if (text.equals(_REMOVE_PARENT_C_) && AdminService.checkAdminStatus(chatId, AdminStatus.ADD_CATEGORIES)){
            AdminService.changeAdminStatus(chatId, AdminStatus.REMOVE_PARENT_C);
            sendMessage.setText("O'chirishingiz mumkin bo'lgan kategoriyalar:");
            sendMessage.setReplyMarkup(InlineKeyboardButtonUtil.getCategoryButtonsForUser(WorkWithDatabase.parentCategoryList()));
            MY_BOT.sendMsg(sendMessage);
            adminMap.put(chatId, null);
        } else if (text.equals(_REMOVE_SUB_C_) && AdminService.checkAdminStatus(chatId, AdminStatus.ADD_CATEGORIES)){
            AdminService.changeAdminStatus(chatId, AdminStatus.REMOVE_CHILD_C);
            sendMessage.setText("O'chirmoqhi bo'lgan sub kategoriyaning parent kategoriyasini tanlang:");
            sendMessage.setReplyMarkup(InlineKeyboardButtonUtil.getCategoryButtonsForUser(WorkWithDatabase.parentCategoryList()));
            MY_BOT.sendMsg(sendMessage);
            adminMap.put(chatId, null);
        } else if (false){

        } else if (false){

        }else if (text.equals(_PRODUCTS_)) {
            sendMessage.setText("Kategoriyalar: ");
            sendMessage.setReplyMarkup(InlineKeyboardButtonUtil.getCategoryButtonsForUser(WorkWithDatabase.parentCategoryList()));
            MY_BOT.sendMsg(sendMessage);
        } else if (text.equals(_ORDER_LIST_)) {
                AdminService.changeAdminStatus(chatId, AdminStatus.ORDER_LIST);
            if (WorkWithDatabase.getOrderList().size() == 0){
                sendMessage.setText("Hali buyurtma yo'q!");
                MY_BOT.sendMsg(sendMessage);
            }else {
                WorkWithFiles.orderListInPDF(WorkWithDatabase.getOrderList());
                File file = new File(BASE_FOLDER, "Buyurtmalar rõyxati.pdf");
                sendDocument.setDocument(new InputFile(file));
                MY_BOT.sendMsg(sendDocument);
                file.delete();
            }
        } else if (text.equals(_SHOW_MESSAGES_)) {
            List<UserMessage> userMessages = WorkWithDatabase.getMessagesFromCustomers();
            if (userMessages.isEmpty()) {
                sendMessage.setText("Yangi xabarlar yo'q");
                sendMessage.setReplyMarkup(KeyboardButtonUtil.getAdminMenu());
                Container.MY_BOT.sendMsg(sendMessage);
            } else {
                for (UserMessage userMessage : userMessages) {
                    String str = "ChatId : " + userMessage.getSenderChatId() + "\nFull name: " + userMessage.getFullName() +
                            "\nPhone number: " + userMessage.getPhoneNumber() +
                            "\nText : " + userMessage.getSenderMessage();

                    SendMessage sendMessage1 = new SendMessage(chatId, str);
                    sendMessage1.setReplyMarkup(InlineKeyboardButtonUtil.getConnectMarkup(userMessage.getSenderChatId(), userMessage.getId()));
                    Container.MY_BOT.sendMsg(sendMessage1);
                }
            }
        } else if (AdminService.checkAdminStatus(chatId, AdminStatus.ADD_DISCOUNT)) {
            boolean isDiscountExist = false;
            for (Discount discount : Container.discountList) {
                if (discount.getChatId().equals(chatId)) {
                    discount.setChatId(chatId);
                    discount.setName(text);
                    isDiscountExist = true;
                    break;
                }
            }
            if (!isDiscountExist)
                Container.discountList.add(new Discount(chatId, null, text, null, null, null));

            sendMessage.setText("Chegirma foizini kiriting: ");
            MY_BOT.sendMsg(sendMessage);
            AdminService.changeAdminStatus(chatId, AdminStatus.SEND_DISCOUNT_PERCENTAGE);
        } else if (AdminService.checkAdminStatus(chatId, AdminStatus.SEND_DISCOUNT_PERCENTAGE)) {
            text = text.replace("%", "");
            if (text.length() == 2 && text.matches("\\d+")) {
                for (Discount discount : Container.discountList) {
                    if (discount.getChatId().equals(chatId)) {
                        discount.setDiscount_percentage(Integer.valueOf(text));
                        break;
                    }
                }
                sendMessage.setText("Chegirmani boshlanish va yakunlanish sanalarini yil-oy-kun formatida," +
                        " quyidagi ko'rinishda kiriting\n" +
                        "Misol uchun: 2012-06-09/2012-10-18");
                AdminService.changeAdminStatus(chatId, AdminStatus.SEND_DISCOUNT_START_END_TIME);
            } else sendMessage.setText("❌ Foiz notogri kiritildi!\n Qayta kiriting!");
            MY_BOT.sendMsg(sendMessage);
        } else if (AdminService.checkAdminStatus(chatId, AdminStatus.SEND_DISCOUNT_START_END_TIME)) {
            if (!text.matches("[0-9]{4}[-]{1}[0-9]{2}[-]{1}[0-9]{2}[/]{1}[0-9]{4}[-]{1}[0-9]{2}[-]{1}[0-9]{2}")) {
                sendMessage.setText("❌ Sana formati xato kiritildi, Qayta kiriting!");
                MY_BOT.sendMsg(sendMessage);
                return;
            }

            String[] date = text.split("/");
            String[] startDate = date[0].split("-");
            String[] endDate = date[1].split("-");
            LocalDate localStartDate = null;
            LocalDate localEndDate = null;
            try {
                localStartDate = LocalDate.of(Integer.parseInt(startDate[0]), Integer.parseInt(startDate[1]), Integer.parseInt(startDate[2]));
                localEndDate = LocalDate.of(Integer.parseInt(endDate[0]), Integer.parseInt(endDate[1]), Integer.parseInt(endDate[2]));
            } catch (Exception e) {
            }
            if (localStartDate == null || localEndDate == null) {
                sendMessage.setText("❌ Sana formati xato kiritildi, Qayta kiriting!");
                MY_BOT.sendMsg(sendMessage);
                return;
            }
            if (!localStartDate.isBefore(localEndDate) || !LocalDate.now().isBefore(localStartDate)) {
                sendMessage.setText("❌ Sana formati xato kiritildi, Qayta kiriting!");
                MY_BOT.sendMsg(sendMessage);
                return;
            }

            for (Discount discount : Container.discountList) {
                if (discount.getChatId().equals(chatId)) {
                    discount.setStart_time(date[0]);
                    discount.setEnd_time(date[1]);

                    if (discount.getPhoto_file_id() != null) {
                        SendPhoto sendPhoto = new SendPhoto();
                        sendPhoto.setChatId(discount.getChatId());
                        sendPhoto.setPhoto(new InputFile(discount.getPhoto_file_id()));
                        StringBuilder photoCaption = new StringBuilder();

                        if (discount.getName() != null) {
                            photoCaption.append(discount.getName()).append("\n\n");
                        }

                        photoCaption.append("Chegirma foizi: ").append(discount.getDiscount_percentage())
                                .append("\n\nBoshlanish vaqti: ").append(discount.getStart_time())
                                .append("\n\nYakunlanish vaqti: ").append(discount.getEnd_time());

                        sendPhoto.setCaption(String.valueOf(photoCaption));
                        sendPhoto.setReplyMarkup(InlineKeyboardButtonUtil.getConfirmationButtons());
                        AdminService.changeAdminStatus(chatId, AdminStatus.DISCOUNT_CONFIRM);
                        MY_BOT.sendMsg(sendPhoto);
                    } else {
                        String discountText = discount.getName() + "\n\nChegirma foizi: " +
                                discount.getDiscount_percentage() +
                                "\n\nBoshlanish vaqti: " +
                                discount.getStart_time() +
                                "\n\nYakunlanish vaqti: " +
                                discount.getEnd_time();
                        sendMessage.setText(discountText);
                        sendMessage.setReplyMarkup(InlineKeyboardButtonUtil.getConfirmationButtons());
                        AdminService.changeAdminStatus(chatId, AdminStatus.DISCOUNT_CONFIRM);
                        MY_BOT.sendMsg(sendMessage);
                    }
                    break;
                }
            }
        } else if (Container.adminAnswerMap.containsKey(chatId)) {
            MessageData messageData = Container.adminAnswerMap.get(chatId);

            String customerChatId = messageData.getCustomerChatId();
            Integer messageId = messageData.getMessage().getMessageId();
            String messageText = messageData.getMessage().getText();
            String customerMessage = messageData.getMessage().getText().split(" : ")[2];

            sendMessage.setChatId(customerChatId);
            sendMessage.setText("Admin ning javobi: " + text);
            Container.MY_BOT.sendMsg(sendMessage);

            WorkWithDatabase.updateMessage(chatId, customerMessage, customerChatId, text);

            EditMessageText editMessageText = new EditMessageText();
            editMessageText.setChatId(chatId);
            editMessageText.setText(messageText + "\n\n xabariga javob: \n\n " + text);
            editMessageText.setMessageId(messageId);
            Container.MY_BOT.sendMsg(editMessageText);

            Container.adminAnswerMap.remove(chatId);
        } else if (AdminService.checkAdminStatus(String.valueOf(message.getChatId()), AdminStatus.SEND_ADS)) {
            sendMessage.setText(text + "\n\n Shu e'lonni barcha foydalanuvchilarga yurborasizmi?");
            sendMessage.setReplyMarkup(InlineKeyboardButtonUtil.getConfirmationButtons());
            MY_BOT.sendMsg(sendMessage);
        }else {
            sendMessage.setText("Kod yozganlar kutmagan qanaqadir case yuz berdi!\n Iltimos bu haqda @a_Bukharanian_user ga murojaat qiling.");
            MY_BOT.sendMsg(sendMessage);
        }

    }


    public static void handleCallBackQuery(Message message, String data) {
        String chatId = String.valueOf(message.getChatId());
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);

        DeleteMessage deleteMessage = new DeleteMessage(chatId, message.getMessageId());
        MY_BOT.sendMsg(deleteMessage);

        if (AdminService.checkAdminStatus(chatId, AdminStatus.DISCOUNT_CONFIRM) && data.equals(YES_CALL)) {
            for (Discount discount : Container.discountList) {
                if (discount.getChatId().equals(chatId)) {
                    WorkWithDatabase.addDiscount(new Discount(chatId, discount.getDiscount_percentage(), discount.getName(), discount.getStart_time(), discount.getEnd_time(), discount.getPhoto_file_id()));
                    sendMessage.setText("Chegirma muvaffaqiyatli saqlandi");
                    MY_BOT.sendMsg(sendMessage);
                    Container.discountList.remove(discount);
                    break;
                }
            }
            AdminService.putAminsIntoMap(chatId);
        } else if (AdminService.checkAdminStatus(chatId, AdminStatus.DISCOUNT_CONFIRM) && data.equals(NO_CALL)) {
            for (Discount discount : Container.discountList) {
                if (discount.getChatId().equals(chatId)) {
                    sendMessage.setText("Chegirma saqlanmadi");
                    MY_BOT.sendMsg(sendMessage);
                    Container.discountList.remove(discount);
                    break;
                }
            }
            AdminService.putAminsIntoMap(chatId);
        }else if (data.startsWith(DELETE_P)){
            WorkWithDatabase.deleteProduct(Integer.valueOf(data.split("/")[1]));
            sendMessage.setText("Mahsulot o'chirildi!");
            MY_BOT.sendMsg(sendMessage);
        }else if (data.equals(YES_CALL) && AdminService.checkAdminStatus(chatId, AdminStatus.SEND_ADS)) {
            Advertisement adToBeSent = WorkWithDatabase.getAdFromDB(chatId);
            AdminService.sendAdsToAllCustomers(adToBeSent.getPhoto(), adToBeSent.getCaption());
            sendMessage.setText("Reklama barcha foydalanuvchilarga muvaffaqiyatli yuborildi! 🎉");
            MY_BOT.sendMsg(sendMessage);
            adminMap.put(chatId, null);
        } else if (data.equals(NO_CALL) && AdminService.checkAdminStatus(chatId, AdminStatus.SEND_ADS)) {
            MY_BOT.sendMsg(deleteMessage);
            AdminService.putAminsIntoMap(chatId);
        } else if (AdminService.checkAdminStatus(chatId, AdminStatus.DELETE_DISCOUNT) && data.startsWith("_delete_")) {
            sendMessage.setText("Chegirma o'chirildi");
            data = data.replace("_delete_", "");
            WorkWithDatabase.deleteDiscount(Integer.parseInt(data));
            MY_BOT.sendMsg(sendMessage);
        }else if (data.startsWith("parent") && AdminService.checkAdminStatus(chatId, AdminStatus.ADD_CHILD_C)){
            String[] split = data.split("/");
            parent_c_id = Integer.parseInt(split[2]);
            sendMessage.setText("Yangi sub kategoriyaning nomini kiriting: ");
            MY_BOT.sendMsg(sendMessage);
        }else if (data.startsWith("parent") && AdminService.checkAdminStatus(chatId, AdminStatus.REMOVE_PARENT_C)){
            String[] split = data.split("/");
            parent_c_name = split[1];
            sendMessage.setText(WorkWithDatabase.deleteParentCategory(parent_c_name));
            MY_BOT.sendMsg(sendMessage);
        }else if (data.startsWith("parent") && AdminService.checkAdminStatus(chatId, AdminStatus.REMOVE_CHILD_C)){
            String[] split = data.split("/");
            sendMessage.setText("O'chirmoqchi bo'lgan sub kategoriyangizni tanlang:");
            sendMessage.setReplyMarkup(InlineKeyboardButtonUtil.getSubCategoryButtons(WorkWithDatabase.getSubCategoryList(split[2])));
            MY_BOT.sendMsg(sendMessage);
        }else if (data.startsWith("parent")) {
            String[] split = data.split("/");
            if (WorkWithDatabase.getSubCategoryList(split[2])==null){
                sendMessage.setText("Hozircha bu kategoriyaning sub kategoriyasi mavjud emas!");
                MY_BOT.sendMsg(sendMessage);
            }else{
                sendMessage.setText(split[1] + "ning sub kategoriya(lar)i:");
                sendMessage.setReplyMarkup(InlineKeyboardButtonUtil.getSubCategoryButtons(WorkWithDatabase.getSubCategoryList(split[2])));
                MY_BOT.sendMsg(sendMessage);
            }
        }else if (data.startsWith("sub") && AdminService.checkAdminStatus(chatId, AdminStatus.REMOVE_CHILD_C)){
            String[] split = data.split("/");
            sendMessage.setText(WorkWithDatabase.deleteSubCategory(split[1]));
            MY_BOT.sendMsg(sendMessage);
        } else if (data.startsWith("sub")) {
            String[] split = data.split("/");
            System.out.println(WorkWithDatabase.getProductOneByOne(Integer.parseInt(split[2])).size()+" ta product");
            if (WorkWithDatabase.getProductOneByOne(Integer.parseInt(split[2])).size()==0){
                sendMessage.setText("Hozircha bu kategoriyada mahsulot mavjud emas!");
                MY_BOT.sendMsg(sendMessage);
            }else {
                productMap.put(chatId, WorkWithDatabase.getProductOneByOne(Integer.parseInt(split[2])));
                Product product = productMap.get(chatId).get(0);
                SendPhoto sendPhoto = new SendPhoto();
                sendPhoto.setChatId(chatId);
                buttonPressCount.put(chatId, 1); // forward yoki back buttonini nechchi marta bosganini yozib boradi
                sendPhoto.setPhoto(new InputFile(product.getPhoto_file_id()));
                sendPhoto.setCaption("Mahsulot nomi: " + product.getName() + "\n\n" +
                        "Narxi: " + product.getPrice() + "\n\n" +
                        "Rangi: " + product.getColor()
                );
                sendPhoto.setReplyMarkup(InlineKeyboardButtonUtil.getProductButtons(product.getId()));
                MY_BOT.sendMsg(sendPhoto);
            }
        }else if (data.startsWith(FORWARD_P)){
            int temp = buttonPressCount.get(chatId);
            SendPhoto sendPhoto = new SendPhoto();
            sendPhoto.setChatId(chatId);
            Product product = productMap.get(chatId).get(temp);
            sendPhoto.setPhoto(new InputFile(product.getPhoto_file_id()));
            sendPhoto.setCaption("Mahsulot nomi: "+product.getName()+"\n\n"+
                    "Narxi: "+product.getPrice()+ "\n\n"+
                    "Rangi: "+ product.getColor()
            );
            if (temp == productMap.get(chatId).size()-1){
                sendPhoto.setReplyMarkup(InlineKeyboardButtonUtil.getProductButtonsWORight(product.getId()));
            }else {
                sendPhoto.setReplyMarkup(InlineKeyboardButtonUtil.getProductButtons(product.getId()));
                buttonPressCount.put(chatId, ++temp);
            }
            MY_BOT.sendMsg(sendPhoto);
        } else if (data.startsWith(BACK)){
            SendPhoto sendPhoto = new SendPhoto();
            sendPhoto.setChatId(chatId);
            int temp = buttonPressCount.get(chatId);
            Product product = productMap.get(chatId).get(temp);
            sendPhoto.setPhoto(new InputFile(product.getPhoto_file_id()));
            sendPhoto.setCaption("Mahsulot nomi: "+product.getName()+"\n\n"+
                    "Narxi: "+product.getPrice()+ "\n\n"+
                    "Rangi: "+ product.getColor()
            );
            if (temp == 0){
                sendPhoto.setReplyMarkup(InlineKeyboardButtonUtil.getProductButtonsWOLeft(product.getId()));
            }else {
                sendPhoto.setReplyMarkup(InlineKeyboardButtonUtil.getProductButtons(product.getId()));
                buttonPressCount.put(chatId, --temp);
            }
            MY_BOT.sendMsg(sendPhoto);
        }

        String customerMessage = message.getText().split(" : ")[2];
        if (data.startsWith(InlineKeyboardButtonConstants.REPLY_CALL_BACK)) { //for contacting to admins
            String messageId = data.split("/")[1];
            if (WorkWithDatabase.checkMessage(messageId)) {
                sendMessage.setText("Xabarga javob berilgan!");
                Container.MY_BOT.sendMsg(sendMessage);

                deleteMessage = new DeleteMessage(chatId, message.getMessageId());
                Container.MY_BOT.sendMsg(deleteMessage);
            } else {
                String customerChatId = data.split("/")[2];

                Container.adminAnswerMap.put(chatId, new MessageData(message, customerChatId));
                WorkWithDatabase.updateMessageId(chatId, customerMessage, customerChatId);

                sendMessage.setText("Javobingizni kiriting: ");
                Container.MY_BOT.sendMsg(sendMessage);
            }

        }
    }
}
