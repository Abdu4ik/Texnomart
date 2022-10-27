package uz.texnomart.util;

import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.util.List;

import static uz.texnomart.util.KeyboardButtonConstants.*;

public class KeyboardButtonUtil {
    public static ReplyKeyboard getContactMenu() {
        KeyboardButton button = new KeyboardButton(KeyboardButtonConstants.PHONE_NUMBER);
        button.setRequestContact(true);
        return getMarkup(List.of(getRow(button)));
    }

    public static ReplyKeyboard getAdminMenu() {
        return getMarkup(getRowList(
                getRow(getButton(_PRODUCT_CRUD_), getButton(_ADD_ADMIN_)),
                getRow(getButton(_SHOW_MESSAGES_), getButton(_DISCOUNT_))
        ));
    }

    public static ReplyKeyboard getUserMenu() {
        return getMarkup(getRowList(
                getRow(getButton(CATEGORIES), getButton(MESSAGE_ADMIN)),
                getRow(getButton(SHOW_BASKET), getButton(DISCOUNTED_PRODUCTS))
                ));
    }

    private static KeyboardButton getButton(String demo) {
        return new KeyboardButton(demo);
    }

    private static KeyboardRow getRow(KeyboardButton... buttons) {
        return new KeyboardRow(List.of(buttons));
    }

    private static List<KeyboardRow> getRowList(KeyboardRow... rows) {
        return List.of(rows);
    }

    private static ReplyKeyboardMarkup getMarkup(List<KeyboardRow> rowList) {
        ReplyKeyboardMarkup markup = new ReplyKeyboardMarkup(rowList);
        markup.setResizeKeyboard(true);
        markup.setSelective(true);
        return markup;
    }

    public static ReplyKeyboard getBackButton() {
        return getMarkup(getRowList(getRow(getButton(BACK))));
    }

    public static ReplyKeyboard getClearBasketButton() {
        return getMarkup(getRowList(getRow(getButton(CLEAR_BASKET))
                ,getRow(getButton(BACK))));
    }

    public static ReplyKeyboard getProductCRUD() {
        return getMarkup(getRowList(getRow(getButton(_ADD_PRODUCT_),
                getButton(_VIEW_PRODUCT_)),getRow(getButton(_EDIT_PRODUCT_),
                getButton(_DELETE_PRODUCT_)),getRow(getButton(KeyboardButtonConstants._BACK_TO_MENU_))));
    }


}
