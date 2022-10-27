package uz.texnomart.service;

import org.telegram.telegrambots.meta.api.objects.Message;
import uz.texnomart.container.Container;
import uz.texnomart.controller.AdminController;


public class AdminService {

    public static void checkAdmin(Message message) {
        String chatID = String.valueOf(message.getChatId());
        for (int i = 0; i < Container.admins.size(); i++) {
            if (Container.admins.get(i).equals(chatID)){
                AdminController.handleMessage(message);
                return;
            }
        }
    }
}
