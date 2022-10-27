package uz.texnomart.container;

import uz.texnomart.bot.MyBot;

import java.util.ArrayList;
import java.util.List;

public class Container {

    public static MyBot MY_BOT = null;
    public static String TOKEN = "5026381874:AAH9wI4PkMgCpw_gPFaNh6y_o5IV7YizEoc";
    public static String USERNAME = "@imkon_uz_muoomo_bot";
    public static List<String> adminList = new ArrayList<>(List.of("164940659","698010075"));

    // database stuff

    public static final String USER = "postgres";
    public static final String PASSWORD = "0ae03e88b14ced6a6e431080225030545efe9af022cc14f62fb96346a3a16ea5"; // har bir kishida har xil bo'ladi
    public static final String URL = "jdbc:postgresql://ec2-54-75-26-218.eu-west-1.compute.amazonaws.com:5432/dae44hkoegn6lq";
}
