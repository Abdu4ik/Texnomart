package uz.texnomart.util;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import uz.texnomart.entity.Category;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static uz.texnomart.container.Container.*;
import static uz.texnomart.util.InlineKeyboardButtonConstants.*;
import static uz.texnomart.util.KeyboardButtonConstants.BACK;

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
        List<InlineKeyboardButton> inlineKeyboardButtonList = new ArrayList<>();

        if (categories.isEmpty()) {
            return null;
        }

        for (Category category : categories) {

            InlineKeyboardButton button = getButton(category.getName(), "parent/"+ category.getName()+"/"+category.getId()); // callBackQuery siga categoriya nomi va id sini bervoryapman
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


    public static InlineKeyboardMarkup getSubCategoryButtons(List<Category> categories) {
        List<InlineKeyboardButton> inlineKeyboardButtonList = new ArrayList<>();

        if (categories.isEmpty()) {
            return null;
        }

        for (Category category : categories) {

            InlineKeyboardButton button = getButton(category.getName(), "sub/"+ category.getName()+"/"+category.getId()); // callBackQuery siga categoriya nomi va id sini bervoryapman
            inlineKeyboardButtonList.add(button);
        }

        List<InlineKeyboardButton> row1 = new ArrayList<>();
        List<InlineKeyboardButton> row2 = new ArrayList<>();
        List<InlineKeyboardButton> row3 = new ArrayList<>();
        List<InlineKeyboardButton> row4 = new ArrayList<>();

        for (int i = 0; i < inlineKeyboardButtonList.size(); i++) {
            if (i%4==0){
                row1.add(inlineKeyboardButtonList.get(i));
            }else if (i%4==1){
                row2.add(inlineKeyboardButtonList.get(i));
            }else if (i%4==2){
                row3.add(inlineKeyboardButtonList.get(i));
            }else {
                row4.add(inlineKeyboardButtonList.get(i));
            }
        }
        return getMarkup(row1,row2,row3,row4);
    }


    //for contacting with admins
    public static InlineKeyboardMarkup getConnectMarkup(String chatId, int id) {
        InlineKeyboardButton button = new InlineKeyboardButton(InlineKeyboardButtonConstants.REPLY_DEMO);
        button.setCallbackData(InlineKeyboardButtonConstants.REPLY_CALL_BACK+"/"+id+"/"+chatId);

        return new InlineKeyboardMarkup(List.of(List.of(button)));
    }

    public static InlineKeyboardMarkup getDiscountDeleteButton(Integer discountId) {
        InlineKeyboardButton button = getButton("O'chirish", "_delete_" + discountId);
        return getMarkup(button);
    }

    //for getting basket details
    public static InlineKeyboardMarkup getConfirmOrCancelMenu(String basketId) {
        InlineKeyboardButton details = new InlineKeyboardButton(InlineKeyboardButtonConstants.DETAILS);
        details.setCallbackData(InlineKeyboardButtonConstants.DETAILS_CALL_BACK + "/" + basketId);

        InlineKeyboardButton confirm = new InlineKeyboardButton(InlineKeyboardButtonConstants.CONFIRM_DEMO);
        confirm.setCallbackData(InlineKeyboardButtonConstants.CONFIRM_CALL_BACK + "/" + basketId);

        InlineKeyboardButton cancel = new InlineKeyboardButton(InlineKeyboardButtonConstants.CANCEL_DEMO);
        cancel.setCallbackData(InlineKeyboardButtonConstants.CANCEL_CALL_BACK + "/" + basketId);

        return new InlineKeyboardMarkup(List.of(
                List.of(details),
                List.of(confirm, cancel)
        ));
    }

    public static ReplyKeyboard getProductButtons(int id) {
        List<InlineKeyboardButton> buttons1 = new ArrayList<>(Arrays.asList(getButton(DECREASE, DECREASE_CALL_BACK+":"+id), getButton(INCREASE, INCREASE_CALL_BACK+":"+id)));
        List<InlineKeyboardButton> buttons2 = new ArrayList<>(Arrays.asList(getButton(DOWN, DOWN_CALL_BACK+":"+id), getButton(UP, UP_CALL_BACK+":"+id)));
        List<InlineKeyboardButton> buttons3 = new ArrayList<>(Arrays.asList(getButton(BACK1, BACK1_CALL_BACK+"/"+id)));

        return getMarkup(buttons1, buttons2, buttons3);
    }

    public static ReplyKeyboard getProductButtons1(int id) {
        List<InlineKeyboardButton> buttons1 = new ArrayList<>(Arrays.asList(getButton(DECREASE, DECREASE_CALL_BACK+":"+id), getButton(INCREASE, INCREASE_CALL_BACK+":"+id)));
        List<InlineKeyboardButton> buttons2 = new ArrayList<>(Arrays.asList(getButton(UP, UP_CALL_BACK+":"+id)));
        List<InlineKeyboardButton> buttons3 = new ArrayList<>(Arrays.asList(getButton(BACK1, BACK1_CALL_BACK+"/"+id)));

        return getMarkup(buttons1, buttons2, buttons3);
    }
    public static ReplyKeyboard getProductButtons2(int id) {
        List<InlineKeyboardButton> buttons1 = new ArrayList<>(Arrays.asList(getButton(DECREASE, DECREASE_CALL_BACK+":"+id), getButton(INCREASE, INCREASE_CALL_BACK+":"+id)));
        List<InlineKeyboardButton> buttons2 = new ArrayList<>(Arrays.asList(getButton(DOWN, DOWN_CALL_BACK+":"+id)));
        List<InlineKeyboardButton> buttons3 = new ArrayList<>(Arrays.asList(getButton(BACK1, BACK1_CALL_BACK+"/"+id)));

        return getMarkup(buttons1, buttons2, buttons3);
    }

    public static ReplyKeyboard getPrevNextButton(int discountListId) {
        return getMarkup(getButton(PREV, PREV_CALL+"/"+discountListId),getButton(CANCEL,CANCEL_CALL),
                getButton(NEXT, NEXT_CALL+"/"+discountListId));
    }

    public static ReplyKeyboard getProductButtonsWOLeft(Integer id) {
        List<InlineKeyboardButton> buttons1 = new ArrayList<>(Arrays.asList(getButton(DELETE_P, DELETE_P+"/"+id), getButton(ADD_PRODUCT, ADD_PRODUCT+"/"+id)));
        List<InlineKeyboardButton> buttons2 = new ArrayList<>(Arrays.asList(getButton(EDIT_PRICE_P, EDIT_PRICE_P+"/"+id), getButton(EDIT_NAME_P, EDIT_NAME_P+"/"+id)));
        List<InlineKeyboardButton> buttons3 = new ArrayList<>(Arrays.asList(getButton(FORWARD_P, FORWARD_P)));
        return getMarkup(buttons1, buttons2, buttons3);
    }
    public static ReplyKeyboard getProductButtons(Integer id) {
        List<InlineKeyboardButton> buttons1 = new ArrayList<>(Arrays.asList(getButton(DELETE_P, DELETE_P+"/"+id), getButton(ADD_PRODUCT, ADD_PRODUCT+"/"+id)));
        List<InlineKeyboardButton> buttons2 = new ArrayList<>(Arrays.asList(getButton(EDIT_PRICE_P, EDIT_PRICE_P+"/"+id), getButton(EDIT_NAME_P, EDIT_NAME_P+"/"+id)));
        List<InlineKeyboardButton> buttons3 = new ArrayList<>(Arrays.asList(getButton(BACK, BACK), getButton(FORWARD_P, FORWARD_P)));
        return getMarkup(buttons1, buttons2, buttons3);
    }

    public static ReplyKeyboard getProductButtonsWORight(Integer id) {
        List<InlineKeyboardButton> buttons1 = new ArrayList<>(Arrays.asList(getButton(DELETE_P, DELETE_P+"/"+id), getButton(ADD_PRODUCT, ADD_PRODUCT+"/"+id)));
        List<InlineKeyboardButton> buttons2 = new ArrayList<>(Arrays.asList(getButton(EDIT_PRICE_P, EDIT_PRICE_P+"/"+id), getButton(EDIT_NAME_P, EDIT_NAME_P+"/"+id)));
        List<InlineKeyboardButton> buttons3 = new ArrayList<>(Arrays.asList(getButton(BACK, BACK)));
        return getMarkup(buttons1, buttons2, buttons3);
    }



    /////////////

    public static ReplyKeyboard getProductButtonsWOLeftFU(String chatId,Integer id) {
        List<InlineKeyboardButton> buttons1 = new ArrayList<>(Arrays.asList(getButton(ADD_TO_BASKET, ADD_TO_BASKET+"/"+id+"/"+chatId)));
        List<InlineKeyboardButton> buttons3 = new ArrayList<>(Arrays.asList(getButton(FORWARD_P, FORWARD_P)));
        return getMarkup(buttons1, buttons3);
    }
    public static ReplyKeyboard getProductButtonsFU(String chatId, Integer id) {
        List<InlineKeyboardButton> buttons1 = new ArrayList<>(Arrays.asList(getButton(ADD_TO_BASKET, ADD_TO_BASKET+"/"+id+"/"+chatId)));
        List<InlineKeyboardButton> buttons3 = new ArrayList<>(Arrays.asList(getButton(BACK, BACK), getButton(FORWARD_P, FORWARD_P)));
        return getMarkup(buttons1, buttons3);
    }

    public static ReplyKeyboard getProductButtonsWORightFU(String chatId, Integer id) {
        List<InlineKeyboardButton> buttons1 = new ArrayList<>(Arrays.asList(getButton(ADD_TO_BASKET, ADD_TO_BASKET+"/"+id+"/"+chatId)));
        List<InlineKeyboardButton> buttons3 = new ArrayList<>(Arrays.asList(getButton(BACK, BACK)));
        return getMarkup(buttons1, buttons3);
    }


    public static ReplyKeyboard buttonsForUserSearch() {
        List<InlineKeyboardButton> buttons1 = new ArrayList<>(Arrays.asList(getButton(SEARCH_BY_NAME, SEARCH_BY_NAME), getButton(SEARCH_BY_COST, SEARCH_BY_COST)));
        List<InlineKeyboardButton> buttons3 = new ArrayList<>(Arrays.asList(getButton(SEARCH_BY_COLOR, SEARCH_BY_COLOR), getButton(BACK_TO_SEARCH, BACK_TO_SEARCH)));
        return getMarkup(buttons1, buttons3);
    }
}
