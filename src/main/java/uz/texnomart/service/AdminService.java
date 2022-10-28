package uz.texnomart.service;

import uz.texnomart.container.Container;
import uz.texnomart.enums.AdminStatus;

import java.io.File;
import java.util.Map;

public class AdminService {

    public static File showUsersAsPDF() {
        return null;
    }

    public static void changeAdminStatus(String chatId, AdminStatus adminStatus){ // adminMapga

        for (Map.Entry<String, AdminStatus> adminStatusEntry : Container.adminMap.entrySet()) {
            if (adminStatusEntry.getKey().equals(chatId)){
                adminStatusEntry.setValue(adminStatus);
            }
        }

    }

    public static boolean checkAdminStatus(String chatId, AdminStatus adminStatus){

        for (Map.Entry<String, AdminStatus> adminStatusEntry : Container.adminMap.entrySet()) {
            if (adminStatusEntry.getKey().equals(chatId) && adminStatusEntry.getValue() == adminStatus){
                return true;
            }
        }

        return false;
    }

    public static void putAminsIntoMap (String chatId){ //  har safar admin qo'shilganda uni chat id sini admin mapga put qilish uchun
        if (!Container.adminMap.containsKey(chatId)){
            Container.adminMap.put(chatId, null);
        }
    }
}
