package uz.texnomart.service;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import uz.texnomart.entity.Category;
import uz.texnomart.entity.Product;
import uz.texnomart.util.InlineKeyboardButtonUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import static uz.texnomart.container.Container.*;

public class UserService {

    public static void searchByAll(List<Product>productList, String chatId) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);
        if (productList.size()==0){
            sendMessage.setText("Bunday mahsulotlar mavjud emas !!!");
            sendMessage.setReplyMarkup(InlineKeyboardButtonUtil.buttonsForUserSearch());
            MY_BOT.sendMsg(sendMessage);
        }
        sendMessage.setText(" 🪄 Mahsulotlar : ");
        MY_BOT.sendMsg(sendMessage);

        SendPhoto sendPhoto = new SendPhoto();
        sendPhoto.setChatId(chatId);



        for (int i = 0; i < productList.size(); i++) {
            sendPhoto.setPhoto(new InputFile(productList.get(i).getPhoto_file_id()));
            System.out.println(productList.get(i).getPhoto_file_id());
            sendPhoto.setCaption(infoMaker(productList.get(i)));
            MY_BOT.sendMsg(sendPhoto);
        }
    }

    private static String infoMaker(Product product) {
        return "Info :\n"+
                "\n📝 Mahsulot nomi :"+ product.getName()+
                "\n⭕ Mahsulot rangi : "+ product.getColor()+
                "\n💵 Mahsulot narxi : "+product.getPrice();
    }

}
