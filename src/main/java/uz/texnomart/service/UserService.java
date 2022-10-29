package uz.texnomart.service;

import uz.texnomart.entity.Category;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import static uz.texnomart.container.Container.*;

public class UserService {

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
