package uz.texnomart.db;

import uz.texnomart.entity.Discount;
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

    public static void addDiscount(Discount discount) {

    }

    public static void addAdvert(Advertisement ad) {
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

    public static Advertisement getAdFromDB(String chatId) {
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
             Statement statement = connection.createStatement()
        ) {
            Class.forName("org.postgresql.Driver");

            String query = "SELECT id, caption, photo FROM advertisement where chatid = '" + chatId + "' order by id DESC";

            ResultSet resultSet = statement.executeQuery(query);
            while (resultSet.next()) {
                String caption = resultSet.getString("caption");
                String photo = resultSet.getString("photo");
                return new Advertisement(caption, photo, chatId);
            }

        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static List<Category> parentCategoryList() {
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

    public static String addCategory(String categoryName) {
        // parent id ni kiriting
        // yangi categoriya qo'shish uchun ishlatiladi.
        String response = "";
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD)) {
            if (categoryName.isEmpty() || categoryName.isBlank()) {
                response = "Kategoriya nomida xatolik mavjud ";
            } else {
                categoryName = categoryName.trim();

                String queryFirst = """ 
                        select * from category where name=?;
                        """;
                PreparedStatement preparedStatementFirst = connection.prepareStatement(queryFirst);
                preparedStatementFirst.setString(1, categoryName);
                ResultSet resultSet = preparedStatementFirst.executeQuery();
                if (!resultSet.next()) {
                    String query = """
                            insert into category(name)
                            values(?);
                            """;
                    PreparedStatement preparedStatement = connection.prepareStatement(query);
                    preparedStatement.setString(1, categoryName);
                    int execute = preparedStatement.executeUpdate();

                    if (execute == 1) {
                        response = "Successfully: added category";
                    }
                } else {
                    System.out.println("bunday category mavjud ");
                }
            }

        } catch (SQLException e) {
            response = "kategoriya exceptionga tushdi";
        }

        return response;
    }

    public static void addCustomer(String chat_id, String fullname, String phone_number) {

        // yangi customer  qoshish uchun ishlatiladi.
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD)) {
            if (fullname.isEmpty() || fullname.isBlank() || chat_id.isBlank() || chat_id.isEmpty()
                    || phone_number.isBlank()) {
                System.out.println("User malumotlarida xatolik mavjud ");
            } else {
                chat_id = chat_id.trim();
                fullname = fullname.trim();
                phone_number = phone_number.trim();

                String queryFirst = """
                        select * from customer where id=?; 
                        """;
                PreparedStatement preparedStatementFirst = connection.prepareStatement(queryFirst);
                preparedStatementFirst.setString(1, chat_id);
                ResultSet resultSet = preparedStatementFirst.executeQuery();
                if (!resultSet.next()) {
                    String query = """
                            insert into customer(chat_id,fullname,phone_number)
                            values(?,?,?);
                            """;
                    PreparedStatement preparedStatement = connection.prepareStatement(query);
                    preparedStatement.setString(1, chat_id);
                    preparedStatement.setString(2, fullname);
                    preparedStatement.setString(3, phone_number);

                    int execute = preparedStatement.executeUpdate();

                    if (execute == 1) {
                        System.out.println("Successfully: added customer ");
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public static boolean getCategoryById(Integer categoryId) {
        // bu kategoriyalar ichida categoryId shu id ga ega bolgan kategoriya bormi yoqmi shuni korsatib beradi bu
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD)) {
            if (categoryId <= 0) {
                System.out.println("bunday categoriya id bolmaydi ");
            } else {
                String query = """
                        select * from category where id=?;
                         """;
                PreparedStatement preparedStatement = connection.prepareStatement(query);
                preparedStatement.setInt(1, categoryId);
                ResultSet resultSet = preparedStatement.executeQuery();
                return resultSet.next();

            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }


    public static void addProduct(String productName, Integer categoryId, double price, String photo_file_id) {
        // yangi categoriya qoshish uchun ishlatiladi.
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD)) {
            if (productName.isEmpty() || productName.isBlank() || !getCategoryById(categoryId)
                    || price <= 0 || photo_file_id.trim().isEmpty()) {
                System.out.println("Kategoriya nomida xatolik mavjud ");
            } else if (getCategoryById(categoryId)) {
                String query = """
                        insert into product(name,category_id,price,photo_file_id)
                        values(?,?,?,?);
                        """;
                PreparedStatement preparedStatement = connection.prepareStatement(query);
                preparedStatement.setString(1, productName);
                preparedStatement.setInt(2, categoryId);
                preparedStatement.setDouble(3, price);
                preparedStatement.setString(4, photo_file_id);

                int execute = preparedStatement.executeUpdate();
                System.out.println("execute = " + execute);

                if (execute == 1) {
                    System.out.println("Successfully: added product ");
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

    }


    public static void addShopBranches(String location_url, String name, String working_hours) {
        // bu yangi filial qoshish uchun ishlatiladi
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD)) {
            if (location_url.trim().isEmpty() || name.trim().isEmpty() || working_hours.trim().isEmpty()) {
                System.out.println("Shop branches qiymatlari xato kiritildi..");
            } else {
                location_url = location_url.trim();
                name = name.trim();
                working_hours = working_hours.trim();


                String queryFirst = """
                        select * from shop_branches where name=? or location_url=?; 
                        """;
                PreparedStatement preparedStatement = connection.prepareStatement(queryFirst);
                preparedStatement.setString(1, name);
                preparedStatement.setString(2, location_url);
                ResultSet resultSet = preparedStatement.executeQuery();
                if (!resultSet.next()) {
                    String queryTwo = """
                            insert into shop_branches(location_url,name,working_hours)
                            values(?,?,?);
                            """;

                    PreparedStatement preparedStatementTwo = connection.prepareStatement(queryTwo);
                    preparedStatementTwo.setString(1, location_url);
                    preparedStatementTwo.setString(2, name);
                    preparedStatementTwo.setString(3, working_hours);

                    int execute = preparedStatementTwo.executeUpdate();
                    System.out.println("execute = " + execute);

                    if (execute == 1) {
                        System.out.println("Successfully: added shop branches ");
                    }
                }
            }

        } catch (SQLException e) {
            System.out.println("Xatolik  mavjud");
        }

    }

    public static String addProductDiscount(Integer product_id, Integer discount_id) {
        String response = "";
        // bu mahsulotga chegirma qoyish uchun ishlatiladi
        // qaysi mahsulot qaysi chegirmaga ega ekanligini aytb beradi
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD)) {

            String queryFirst = """
                    select * from discount_id where id=?; 
                    """;
            PreparedStatement preparedStatement = connection.prepareStatement(queryFirst);
            preparedStatement.setInt(1, discount_id);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                String queryTwo = """
                        insert into product_discount(product_id,discount_id)
                        values(?,?);
                        """;
                PreparedStatement preparedStatementTwo = connection.prepareStatement(queryTwo);
                preparedStatementTwo.setInt(1, product_id);
                preparedStatementTwo.setInt(2, discount_id);
                int execute = preparedStatementTwo.executeUpdate();
                if (execute == 1) {
                    response = ("Successfully: added shop branches ");
                }
            } else {
                response = "bunday chegirma mavjud emas ";
            }

        } catch (SQLException e) {
            response = "Xatolik  mavjud";
        }
        return response;
    }

    public static String addDiscount(Integer discount_percentage, String name, String start_time, String end_time, String photo_file_id) {
        String response = "";

        // yangi discount qoshish uchun ishlatiladi
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD)) {

            if (discount_percentage <= 0 || name.trim().isEmpty() || photo_file_id.trim().isEmpty()) {
                response = "Chegirma malumotlarida xatolik mavjud ";
            } else {
                name = name.trim();
                photo_file_id = photo_file_id.trim();

                String query = """
                        insert into discount(discount_percentage,name,start_time,end_time,photo_file_id)
                        values(?,?,?,?,?);
                        """;
                PreparedStatement preparedStatement = connection.prepareStatement(query);
                preparedStatement.setInt(1, discount_percentage);
                preparedStatement.setString(2, name);
                preparedStatement.setTimestamp(3, (Timestamp.valueOf(start_time + " 00:00:01")));
                preparedStatement.setTimestamp(4, (Timestamp.valueOf(end_time + " 00:00:01")));
                preparedStatement.setString(5, photo_file_id);

                int execute = preparedStatement.executeUpdate();

                if (execute == 1) {
                    response = "Successfully: added discount  ";
                }

            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return response;
    }

    public static boolean getProductById(Integer product_id) {
        // bu product  ichida product id  shu id ga ega bolgan product bormi yoqmi shuni korsatib beradi bu
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD)) {
            if (product_id > 0) {
                String query = """
                        select * from product where id=?;
                         """;
                PreparedStatement preparedStatement = connection.prepareStatement(query);
                preparedStatement.setInt(1, product_id);
                ResultSet resultSet = preparedStatement.executeQuery();
                System.out.println("resultSet.next() = " + resultSet.next());
                return resultSet.next();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static String editProductName(String name, Integer product_id) {
        // bu metodga product id va productni nameni berasan va bu bazadan almashtirib qoyadi
        String response = "";

        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD)) {
            if (name.trim().isEmpty() || getProductById(product_id)) {
                System.out.println("Xatolik bor ");
            }
            String query = """
                    update product set name=? where id=?;
                      """;
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, name);
            preparedStatement.setInt(2, product_id);
            if (preparedStatement.executeUpdate() == 1) {
                response = "Successfully edit product name";
            }

        } catch (SQLException e) {
            response = "Productni edit qilishda exceptionga tushdi bu ";
        }
        return response;
    }

    public static String editProductPrice(Double price, Integer product_id) {
        // bu metodga product id va productni proce berasan va bu bazadan almashtirib qoyadi
        String response = "";

        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD)) {
            if (price < 0 || getProductById(product_id)) {
                System.out.println("Xatolik bor ");
            }
            String query = """
                    update product set price=? where id=?;
                      """;
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setDouble(1, price);
            preparedStatement.setInt(2, product_id);
            if (preparedStatement.executeUpdate() == 1) {
                response = "Successfully edit product name";
            }

        } catch (SQLException e) {
            response = "Productni edit qilishda exceptionga tushdi bu ";
        }
        return response;
    }

    public static String editProductPhotoFile(String photo_file_id, Integer product_id) {
        // bu metodga product id va productni proce berasan va bu bazadan almashtirib qoyadi
        String response = "";

        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD)) {
            if (photo_file_id.trim().isEmpty() || getProductById(product_id)) {
                response = "Xatolik bor ";
            }
            String query = """
                    update product set photo_file_id=? where id=?;
                      """;
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, photo_file_id);
            preparedStatement.setInt(2, product_id);
            if (preparedStatement.executeUpdate() == 1) {
                response = "Successfully edit product name";
            }

        } catch (SQLException e) {
            response = "Productni edit qilishda exceptionga tushdi bu ";
        }
        return response;
    }

    public static List<Discount> getNotDeletedDiscounts(String chatId) {
        List<Discount> discounts = new ArrayList<>();
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
             Statement statement = connection.createStatement()
        ) {
            Class.forName("org.postgresql.Driver");

            String query = "SELECT * FROM discount where is_deleted = false and chat_id = '"+ chatId +"'";
            ResultSet resultSet = statement.executeQuery(query);
            while (resultSet.next()){
                int id = resultSet.getInt(1);
                int discount_percentage = resultSet.getInt("discount_percentage");
                String name = resultSet.getString("name");
                String created_at = resultSet.getString("created_at");
                boolean is_deleted = resultSet.getBoolean("is_deleted");
                String start_time = resultSet.getString("start_time");
                String end_time = resultSet.getString("end_time");
                String photo_file_id = resultSet.getString("photo_file_id");

                discounts.add(new Discount(id, chatId, discount_percentage, name, start_time, end_time, photo_file_id));
            }


        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
        return discounts;
    }


}
