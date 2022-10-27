package uz.texnomart.container;

import uz.texnomart.bot.MyBot;

import java.util.ArrayList;
import java.util.List;

public class Container {

    public static MyBot MY_BOT = null;
    public static String TOKEN = "";
    public static String USERNAME = "";
    public static List<String> adminList = new ArrayList<>(List.of("164940659"));

    // database stuff

    public static final String USER = "postgres";
    public static final String PASSWORD = "1"; // har bir kishida har xil bo'ladi
    public static final String URL = "jdbc:postgresql://localhost:5432/Texnomart";
}
