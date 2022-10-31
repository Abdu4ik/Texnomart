package uz.texnomart.util;

import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.util.List;

import static uz.texnomart.util.KeyboardButtonConstants.*;

public class KeyboardButtonUtil {
    public static ReplyKeyboard getContactMenu() {
        KeyboardButton button = getButton(PHONE_NUMBER);
        button.setRequestContact(true);

        return getMarkup(getRowList(getRow(button)));
    }

    public static ReplyKeyboard getAdminMenu() {
        return getMarkup(getRowList(
                getRow(getButton(_PRODUCTS_), getButton(_DISCOUNT_)),
                getRow(getButton(_SHOW_MESSAGES_), getButton(_SEND_ADS_)),
                getRow(getButton(_ADD_CATEGORIES_), getButton(_ADMIN_CRUD_)),
                getRow(getButton(_SHOW_USERS_), getButton(_ORDER_LIST_))
        ));
    }
    public static ReplyKeyboard getBackMenu() {
        return getMarkup(getRowList(
                getRow(getButton(BACK))
        ));
    }

    public static ReplyKeyboard getUserMenu() {
        return getMarkup(getRowList(
                getRow(getButton(CATEGORIES), getButton(MESSAGE_ADMIN)),
                getRow(getButton(SHOW_BASKET), getButton(DISCOUNTED_PRODUCTS)),
                getRow(getButton(FILTER))
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
        markup.setOneTimeKeyboard(true);
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

    public static ReplyKeyboard getEditAdminMenu() {
        return getMarkup(getRowList(
                getRow(getButton(_ADD_ADMIN_), getButton(_REMOVE_ADMIN_)),
                getRow(getButton(BACK))
        ));
    }

    public static ReplyKeyboard getDiscountMenu() {
        return getMarkup(getRowList(getRow(getButton(_ADD_NEW_DISCOUNT_), getButton(_DELETE_DISCOUNT_)), getRow(getButton(_BACK_TO_MENU_))));
    }

    public static ReplyKeyboard getCRUDCategoryMenu() {
        return getMarkup(getRowList(
                getRow(getButton(_ADD_PARENT_C_), getButton(_ADD_SUB_C_)),
                getRow(getButton(_REMOVE_PARENT_C_), getButton(_REMOVE_SUB_C_)),
                getRow(getButton(BACK))
        ));
    }

}
