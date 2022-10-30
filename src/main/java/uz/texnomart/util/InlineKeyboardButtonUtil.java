package uz.texnomart.util;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import uz.texnomart.entity.Category;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static uz.texnomart.util.InlineKeyboardButtonConstants.*;

public class InlineKeyboardButtonUtil {

    public static InlineKeyboardMarkup getBasketButton(String id) {
        InlineKeyboardButton button = getButton(ADD_BASKET, ADD_BASKET_CALL+"/"+id);
        return getMarkup(button);
    }

    static InlineKeyboardButton getButton(String name, String callBackData){
        InlineKeyboardButton button = new InlineKeyboardButton(name);
        button.setCallbackData(callBackData);

        return button;
    }

    static InlineKeyboardMarkup getMarkup(InlineKeyboardButton ... buttons){
        return new InlineKeyboardMarkup(Arrays.asList(Arrays.asList(buttons)));
    }


    public static InlineKeyboardMarkup getOrderButton(String phoneId) {
        List<InlineKeyboardButton> buttons1 = new ArrayList<>(Arrays.asList(getButton(ORDER, ORDER_CALL+"/"+phoneId), getButton(CHANGE_NUMBER, CHANGE+"/"+phoneId)));
        List<InlineKeyboardButton> buttons2 = new ArrayList<>(Arrays.asList(getButton(REMOVE, REMOVE_CALL+"/"+phoneId)));

        return getMarkup(buttons1, buttons2);
    }

    static InlineKeyboardMarkup getMarkup(List<InlineKeyboardButton> ... inlineKeyboardButtonList){
        return new InlineKeyboardMarkup(Arrays.asList(inlineKeyboardButtonList));
    }

    public static InlineKeyboardMarkup getDeleteButton() {
        InlineKeyboardButton button = getButton(DELETE, DELETE_CALL);
        return getMarkup(button);
    }

    public static ReplyKeyboard getConfirmationButtons() {
        return getMarkup(getButton(YES, YES_CALL),
                getButton(NO, NO_CALL));
    }

    public static InlineKeyboardMarkup getEditMenu() {
        InlineKeyboardButton button = getButton(EDIT_NAME, EDIT_CALLBACK);
        return new InlineKeyboardMarkup(Arrays.asList(Arrays.asList(button)));
    }

    public static ReplyKeyboard getDeleteButton(Integer id) {
        return getMarkup(getButton(YES, YES_CALL+"/"+id),
                getButton(NO, NO_CALL));
    }

    public static InlineKeyboardMarkup getCategoryButtonsForUser(List<Category> categories) {
//        InlineKeyboardMarkup markup = new InlineKeyboardMarkup();
//        List<List<InlineKeyboardButton>> list = new ArrayList<>();
//        InlineKeyboardButton button;
//        Category category;
//        List<InlineKeyboardButton> buttons = new ArrayList<>();
//        if (!categories.isEmpty()) {
//            for (Category value : categories) {
//                category = value;
//                button = new InlineKeyboardButton();
//                button.setText(category.getName());
//                button.setCallbackData(category.getName());
//                buttons.add(button);
//                if (buttons.size() > 1 && buttons.size() % 2 != 0) {
//                    list.add(buttons);
//                    buttons = null;
//                }
//            }
//        }
//        markup.setKeyboard(list);
        List<InlineKeyboardButton> inlineKeyboardButtonList = new ArrayList<>();

        if (categories.isEmpty()) {
            return null;
        }

        for (Category category : categories) {

            InlineKeyboardButton button = getButton(category.getName(), category.getName()+"/"+category.getId()); // callBackQuery siga categoriya nomi va id sini bervoryapman
            inlineKeyboardButtonList.add(button);
        }

        List<InlineKeyboardButton> row1 = new ArrayList<>();
        List<InlineKeyboardButton> row2 = new ArrayList<>();
        List<InlineKeyboardButton> row3 = new ArrayList<>();
        List<InlineKeyboardButton> row4 = new ArrayList<>();
        List<InlineKeyboardButton> row5 = new ArrayList<>();
        for (int i = 0; i < inlineKeyboardButtonList.size(); i++) {
            if (i%5==0){
                row1.add(inlineKeyboardButtonList.get(i));
            }else if (i%5==1){
                row2.add(inlineKeyboardButtonList.get(i));
            }else if (i%5==2){
                row3.add(inlineKeyboardButtonList.get(i));
            }else if (i%5==3){
                row4.add(inlineKeyboardButtonList.get(i));
            }else {
                row5.add(inlineKeyboardButtonList.get(i));
            }
        }

        return getMarkup(row1,row2,row3,row4,row5);
    }


    //for contacting with admins
    public static InlineKeyboardMarkup getConnectMarkup(String chatId, int id) {
        InlineKeyboardButton button = new InlineKeyboardButton(InlineKeyboardButtonConstants.REPLY_DEMO);
        button.setCallbackData(InlineKeyboardButtonConstants.REPLY_CALL_BACK+"/"+id+"/"+chatId);

        return new InlineKeyboardMarkup(List.of(List.of(button)));
    }
}
