package uz.texnomart.db;

import uz.texnomart.entity.TelegramUser;

import java.sql.*;

import static uz.texnomart.container.Container.*;

public class WorkWithDatabase {

    public static void addUsers(TelegramUser telegramUser) {
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD)) {
            Class.forName("org.postgresql.Driver");
            Statement statement = connection.createStatement();

            String query = "INSERT INTO customer (chat_id, fullname, phone_number) " +
                    "VALUES ('" + telegramUser.getChatId() + "', '" + telegramUser.getFullName() + "', '" + telegramUser.getPhoneNumber() + "')";
            statement.execute(query);


        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
    }

    public static boolean doesExist(String chatId) {
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD)) {
            String query = "select * from customer where chat_id=?";

            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, chatId);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                if (resultSet.getString("chat_id") != null) {
                    return true;
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}
