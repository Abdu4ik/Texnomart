package uz.texnomart.db;
import uz.texnomart.entity.Advertisement;
import uz.texnomart.entity.Category;
import uz.texnomart.entity.TelegramUser;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import static uz.texnomart.container.Container.*;

public class WorkWithDatabase {

    public static void addUsers(TelegramUser telegramUser) {
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
            Statement statement = connection.createStatement()
        ) {
            Class.forName("org.postgresql.Driver");

            String query = "INSERT INTO users (chat_id, fullname, phone_number) " +
                    "VALUES ('" + telegramUser.getChatId() + "', '" + telegramUser.getFullName() + "', '" + telegramUser.getPhoneNumber() + "')";
            statement.execute(query);


        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
    }

    public static boolean doesExist(String chatId) {
            String query = "select * from customer where chat_id=?";
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement preparedStatement = connection.prepareStatement(query)
        ) {


            preparedStatement.setString(1, chatId);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.getString("chat_id") == null) {
                return false;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return true;
    }

    public static void addAdvert(Advertisement ad){
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
            Statement statement = connection.createStatement()
        ) {
            Class.forName("org.postgresql.Driver");

            String query = "INSERT INTO advertisement (caption, photo, chatId) " +
                    "VALUES ('" + ad.getCaption() + "', '" + ad.getPhoto() + "', '" + ad.getChatId() + "')";
            statement.execute(query);


        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }


    }

    public static Advertisement getAdFromDB(String chatId){
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
             Statement statement = connection.createStatement()
        ) {
            Class.forName("org.postgresql.Driver");

            String query = "SELECT id, caption, photo FROM advertisement where chatid = "+ chatId +" order by id DESC";

            ResultSet resultSet = statement.executeQuery(query);
            while (resultSet.next()){
                String caption = resultSet.getString("caption");
                String photo = resultSet.getString("photo");
                return new Advertisement(caption, photo, chatId);
            }

        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static List<Category> parentCategoryList(){
        List<Category> categoryList = new ArrayList<>();
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
             Statement statement = connection.createStatement()
        ) {

            String query = """
                              select * from category where parent_id = null;
                    """;
            ResultSet resultSet = statement.executeQuery(query);

            while (resultSet.next()) {
                int id = resultSet.getInt(1);
                String name = resultSet.getString("name");
                int parent_id = resultSet.getInt("parent_id");
                boolean is_deleted = resultSet.getBoolean("is_deleted");
                categoryList.add(new Category(id, name, parent_id, is_deleted));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return categoryList;
    }
}
