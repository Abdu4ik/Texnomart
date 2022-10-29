package uz.texnomart.container;

import uz.texnomart.bot.MyBot;
import uz.texnomart.entity.Discount;
import uz.texnomart.enums.AdminStatus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Container {

    public static MyBot MY_BOT = null;
    public static String TOKEN = "5737907108:AAH2OGMUwZr4f6ecoXc2sEen43I5oaCEjDM";
    public static String USERNAME = "@Baxtigul_JM3_V1_bot";
    public static List<String> adminList = new ArrayList<>(List.of("164940659","698010075","609762012"));

    // database stuff

    public static final String USER = "utpvoxxsoitfbq";
    public static final String PASSWORD = "0ae03e88b14ced6a6e431080225030545efe9af022cc14f62fb96346a3a16ea5";
    public static final String URL = "jdbc:postgresql://ec2-54-75-26-218.eu-west-1.compute.amazonaws.com:5432/dae44hkoegn6lq";

    // admin status 👇

    public static Map<String, AdminStatus> adminMap = new HashMap<>();

    // resourses folder path

    public static final String BASE_FOLDER = "src/main/resources/files/documents";

    //vaqtincha(tablega yozib bolguncha) discountlarni saqlash uchun list
    public static List<Discount> discountList = new ArrayList<>();
}
