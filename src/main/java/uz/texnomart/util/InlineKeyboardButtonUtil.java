package uz.texnomart.util;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
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

    public static InlineKeyboardMarkup getDeleteButton(String id) {
        InlineKeyboardButton button = getButton(DELETE, DELETE_CALL+"/"+id);
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
}
