package uz.texnomart.db;
import uz.texnomart.entity.TelegramUser;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

import static uz.texnomart.container.Container.*;

public class WorkWithDatabase {

    public static void addUsers (TelegramUser telegramUser){
        try {
            Class.forName("org.postgresql.Driver");
            Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
            Statement statement = connection.createStatement();

            String query = "INSERT INTO users (chat_id, firstname, lastname, phone_number) VALUES ("+ telegramUser.getChatId() +", "+ telegramUser.getFirstName() +", "+ telegramUser.getLastName() +", "+ telegramUser.getPhoneNumber() +")";
            statement.execute(query);



        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
    }
}
