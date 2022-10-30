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
import uz.texnomart.db.WorkWithDatabase;
import uz.texnomart.entity.Discount;
import uz.texnomart.enums.AdminStatus;
import uz.texnomart.service.AdminService;
import uz.texnomart.util.InlineKeyboardButtonConstants;
import uz.texnomart.util.InlineKeyboardButtonUtil;
import uz.texnomart.util.KeyboardButtonConstants;
import uz.texnomart.util.InlineKeyboardButtonUtil;

import static uz.texnomart.util.InlineKeyboardButtonConstants.*;

import uz.texnomart.util.KeyboardButtonUtil;

import java.io.File;
import java.time.LocalDate;
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
        if (AdminService.checkAdminStatus(String.valueOf(message.getChatId()), AdminStatus.SEND_ADS)) {
            WorkWithDatabase.addAdvert(new Advertisement(caption, photo, String.valueOf(message.getChatId())));
            sendPhoto.setPhoto(new InputFile(photo));
            sendPhoto.setCaption(caption + "\n\n Shu e'lonni barcha foydalanuvchilarga yurborasizmi?");
            sendPhoto.setReplyMarkup(InlineKeyboardButtonUtil.getConfirmationButtons());
            MY_BOT.sendMsg(sendPhoto);
        }
        String chatId = String.valueOf(message.getChatId());
        String fileId = photoSizeList.get(photoSizeList.size() - 1).getFileId();
        String discountName = message.getCaption();
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);

        if (AdminService.checkAdminStatus(chatId, AdminStatus.ADD_DISCOUNT)) {
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
            sendMessage.setText("Assalom alaykum " + message.getFrom().getFirstName() + "!\nTexnomart botiga xush kelibsiz!");
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
        } else if (AdminService.checkAdminStatus(chatId, AdminStatus.REMOVE_ADMIN)) {
            sendMessage.setText(WorkWithDatabase.takeAdminPrivilege(text));
            MY_BOT.sendMsg(sendMessage);
        } else if (AdminService.checkAdminStatus(chatId, AdminStatus.ADD_ADMIN)) {
            sendMessage.setText(WorkWithDatabase.grantAdminPrivilege(text));
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
            File file = new File(Container.BASE_FOLDER, "Foydalanuvchilar rõyxati.pdf");
            sendDocument.setDocument(new InputFile(file));
            MY_BOT.sendMsg(sendDocument);
            file.delete();
        } else if (text.equals(_ADMIN_CRUD_)) {
            AdminService.changeAdminStatus(chatId, AdminStatus.ADMIN_CRUD); // admin statusni Edit qilishga o'tkazib qo'yadi
            AdminService.showUsersAsPDF();
            File file = new File(Container.BASE_FOLDER, "Foydalanuvchilar rõyxati.pdf");
            sendDocument.setDocument(new InputFile(file));
            sendDocument.setReplyMarkup(KeyboardButtonUtil.getEditAdminMenu());
            sendDocument.setCaption("Admin qo'shish yoki adminlik huquqini olib qo'yish uchun sizga yuqoridagi ma'lumot kerak bo'ladi.");
            MY_BOT.sendMsg(sendDocument);
            file.delete();
        } else if (text.equals(_SEND_ADS_)) {
            adminMap.put(chatId, null);
            AdminService.changeAdminStatus(chatId, AdminStatus.SEND_ADS);
            sendMessage.setText("🖼️ Reklamangizni jo'nating.");
            MY_BOT.sendMsg(sendMessage);

        } else if (text.equals(_DISCOUNT_)) {
            AdminService.changeAdminStatus(chatId, AdminStatus.ADD_DISCOUNT);
            sendMessage.setText("Chegirma rasmini yoki nomini yoki rasmi bilan nomini jo'nating");
            MY_BOT.sendMsg(sendMessage);
        } else if (text.equals(_CATEGORIES_)) {
            sendMessage.setText("Mavjud kategoriyalar");
            sendMessage.setReplyMarkup(InlineKeyboardButtonUtil.getCategoryButtonsForUser(WorkWithDatabase.parentCategoryList()));
            MY_BOT.sendMsg(sendMessage);
        } else if (text.equals(_PRODUCTS_)) {

        } else if (text.equals(_ORDER_LIST_)) {

        } else if (text.equals(_SHOW_MESSAGES_)) {

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
                        String photoCaption = "";

                        if (discount.getName() != null) {
                            photoCaption = photoCaption.concat(discount.getName());
                        }

                        photoCaption = "Chegirma foizi: " +
                                discount.getDiscount_percentage() +
                                "\n\nBoshlanish vaqti: " +
                                discount.getStart_time() +
                                "\n\nYakunlanish vaqti: " +
                                discount.getEnd_time();

                        sendPhoto.setCaption(photoCaption);
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
        } else if (AdminService.checkAdminStatus(String.valueOf(message.getChatId()), AdminStatus.SEND_ADS)) {
            sendMessage.setText(text + "\n\n Shu e'lonni barcha foydalanuvchilarga yurborasizmi?");
            sendMessage.setReplyMarkup(InlineKeyboardButtonUtil.getConfirmationButtons());
        }

    }


    public static void handleCallBackQuery(Message message, String data) {
        String chatId = String.valueOf(message.getChatId());
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);

        DeleteMessage deleteMessage = new DeleteMessage(chatId, message.getMessageId());

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
        } else if (AdminService.checkAdminStatus(chatId, AdminStatus.DISCOUNT_CONFIRM) && data.equals(NO_CALL)) {
            for (Discount discount : Container.discountList) {
                if (discount.getChatId().equals(chatId)) {
                    sendMessage.setText("Chegirma saqlanmadi");
                    MY_BOT.sendMsg(sendMessage);
                    Container.discountList.remove(discount);
                    break;
                }
            }
        }
        if (data.equals(InlineKeyboardButtonConstants.YES_CALL) && AdminService.checkAdminStatus(chatId, AdminStatus.SEND_ADS)) {
            Advertisement adToBeSent = WorkWithDatabase.getAdFromDB(chatId);
            AdminService.sendAdsToAllCustomers(adToBeSent.getPhoto(), adToBeSent.getCaption());
            sendMessage.setText("Reklama barcha foydalanuvchilarga muvaffaqiyatli yuborildi! 🎉");
            MY_BOT.sendMsg(sendMessage);
            MY_BOT.sendMsg(deleteMessage);
            AdminService.putAminsIntoMap(chatId);
        }else if (data.equals(InlineKeyboardButtonConstants.YES_CALL) && AdminService.checkAdminStatus(chatId, AdminStatus.SEND_ADS)) {
            MY_BOT.sendMsg(deleteMessage);
            AdminService.putAminsIntoMap(chatId);
        }
    }
}