package uz.texnomart.db;
import org.telegram.telegrambots.meta.api.objects.Contact;
import uz.texnomart.container.Container;
import uz.texnomart.entity.*;
import uz.texnomart.enums.UserRoles;

import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import static uz.texnomart.container.Container.*;

public class WorkWithDatabase {

    public static boolean doesExist(String chatId) {
        String query = "select * from customer where chat_id=?";
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement preparedStatement = connection.prepareStatement(query)
        ) {

            preparedStatement.setString(1, chatId);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (!resultSet.next()) {
                return false;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return true;
    }

    public static void addDiscount(Discount discount) {
        String photo = (discount.getPhoto_file_id() == null) ? "" : discount.getPhoto_file_id();
        //INSERT INTO discount ( discount_percentage , name, start_time, end_time,  photo_file_id, chat_id) VALUES (

        String query = "INSERT INTO discount (discount_percentage , name, start_time, end_time,  photo_file_id, chat_id) VALUES (" + discount.getDiscount_percentage() +
                " , '" + discount.getName() +
                "', '" + discount.getStart_time() +
                "', '" + discount.getEnd_time() +
                "', '" + photo +
                "', '" + discount.getChatId() +
                "')";

        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
             Statement statement = connection.createStatement()
        ) {
            Class.forName("org.postgresql.Driver");


            statement.executeUpdate(query);


        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }


    }

    public static void addAdvert(Advertisement ad) {

        String query = "INSERT INTO advertisement (caption, photo, chatid) VALUES (?, ?, ?)";

        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement preparedStatement = connection.prepareStatement(query)
        ) {
            Class.forName("org.postgresql.Driver");
            preparedStatement.setString(1, ad.getCaption());
            preparedStatement.setString(2, ad.getPhoto());
            preparedStatement.setString(3, ad.getChatId());
            preparedStatement.execute();


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
            if (resultSet.next()) {
                String caption = resultSet.getString("caption");
                String photo = resultSet.getString("photo");
                return new Advertisement(caption, photo, chatId);
            }

        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static List<Category> getSubCategoryList(String parentCategoryID) {
        List<Category> categoryList = new ArrayList<>();
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
             Statement statement = connection.createStatement()
        ) {
            String query = "select * from category where parent_id = " + parentCategoryID + " and is_deleted is false";
            ResultSet resultSet = statement.executeQuery(query);

            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String name = resultSet.getString("name");
                int parent_id = resultSet.getInt("parent_id");
                categoryList.add(new Category(id, name, parent_id));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return categoryList;
    }

    public static List<Category> parentCategoryList() {
        List<Category> categoryList = new ArrayList<>();
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
             Statement statement = connection.createStatement()
        ) {
            String query = """
                              select * from category where parent_id is null and is_deleted is false ;
                    """;
            ResultSet resultSet = statement.executeQuery(query);

            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String name = resultSet.getString("name");
                categoryList.add(new Category(id, name));
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

    public static void addCustomer1(String chat_id, String fullname, String phone_number) {

        // yangi customer  qoshish uchun ishlatiladi.
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD)) {
            if (fullname.isEmpty() || fullname.isBlank() || chat_id.isBlank() || chat_id.isEmpty()
                    || phone_number.isBlank()) {
                System.out.println("User malumotlarida xatolik mavjud ");
            } else {
                chat_id = chat_id.trim();
                fullname = fullname.trim();
                phone_number = phone_number.trim();

                String queryFirst = "select * from customer where id = ?;";
                PreparedStatement preparedStatementFirst = connection.prepareStatement(queryFirst);
                preparedStatementFirst.setString(1, chat_id);
                ResultSet resultSet = preparedStatementFirst.executeQuery();
                if (!resultSet.next()) {
                    String query = "insert into customer(chat_id,fullname,phone_number) values(?,?,?)";
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
                String query = "select * from category where id = ?";
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
                String query = "insert into product(name,category_id,price,photo_file_id) values (?,?,?,?)";
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


                String queryFirst = "select * from shop_branches where name = ? or location_url = ?";
                PreparedStatement preparedStatement = connection.prepareStatement(queryFirst);
                preparedStatement.setString(1, name);
                preparedStatement.setString(2, location_url);
                ResultSet resultSet = preparedStatement.executeQuery();
                if (!resultSet.next()) {
                    String queryTwo = "insert into shop_branches(location_url,name,working_hours) values(?,?,?)";

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

            String queryFirst = "select * from discount_id where id=?";
            PreparedStatement preparedStatement = connection.prepareStatement(queryFirst);
            preparedStatement.setInt(1, discount_id);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                String queryTwo = "insert into product_discount(product_id,discount_id) values(?,?)";
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

                String query = "insert into discount(discount_percentage,name,start_time,end_time,photo_file_id) values(?,?,?,?,?)";
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
                String query = "select * from product where id = ?";
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
            String query = "update product set name=? where id = ?";
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
            String query = "update product set price=? where id=?";
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
            String query = "update product set photo_file_id=? where id=?";
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

    public static String takeAdminPrivilege(String chatId) {
        String response = "";
        boolean success = false;

        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
             Statement statement = connection.createStatement()
        ) {
            Class.forName("org.postgresql.Driver");

            String query = "SELECT user_role FROM customer where user_role = 'ADMIN'::user_roles and chat_id = '" + chatId + "'";
            ResultSet resultSet = statement.executeQuery(query);
            if (resultSet.next()) {
                query = "UPDATE customer SET user_role = 'USER'::user_roles WHERE chat_id = '" + chatId + "'";
                statement.execute(query);
                response = chatId + " chat ID li userdan Adminlik huquqi olindi.";
                success = true;
            } else {
                response = "Bu chat ID li admin mavjud emas!";
            }

        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
        if (success) {
            adminList.remove(chatId);
        }
        return response;
    }

    public static String grantAdminPrivilege(String chatId) {
        String response = "";
        boolean success = false;

        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
             Statement statement = connection.createStatement()
        ) {
            Class.forName("org.postgresql.Driver");

            String query = "SELECT user_role FROM customer where user_role = 'USER'::user_roles and chat_id = '" + chatId + "'";
            ResultSet resultSet = statement.executeQuery(query);
            if (resultSet.next()) {
                query = "UPDATE customer SET user_role = 'ADMIN'::user_roles WHERE chat_id = '" + chatId + "'";
                statement.execute(query);
                response = chatId + " chat ID li userga Adminlik huquqi berildi.";
                success = true;
            } else {
                response = "Bu chat ID li user mavjud emas!";
            }

        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
        if (success) {
            adminList.add(chatId);
        }
        return response;
    }

    //for contacting admin
    public static void addCustomer(String chatId, Contact contact) {
        TelegramUser customer = new TelegramUser(chatId, contact.getFirstName(), contact.getPhoneNumber(), UserRoles.USER);
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD)) {

            Class.forName("org.postgresql.Driver");


            String query = "insert into customer(chat_id, fullname, phone_number) values(?, ?, ?);";

            PreparedStatement preparedStatement = connection.prepareStatement(query);

            preparedStatement.setString(1, customer.getChatId());
            preparedStatement.setString(2, customer.getFullName());
            preparedStatement.setString(3, customer.getPhoneNumber());
//            preparedStatement.setString(4, customer.getUserRoles().name());

            preparedStatement.executeUpdate();
            preparedStatement.close();

        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static TelegramUser getCustomerByChatId(String chatId) {
        TelegramUser customer = new TelegramUser();
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD)) {

            Class.forName("org.postgresql.Driver");

            String query = "select * from customer where chat_id = ?;";

            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, chatId);
            ResultSet rs = preparedStatement.executeQuery();

            if (!rs.next()) {
                return null;
            } else {
                customer.setChatId(rs.getString("chat_id"));
                customer.setFullName(rs.getString("fullname"));
                customer.setPhoneNumber(rs.getString("phone_number"));
                if (rs.getString("user_role").equals("ADMIN")) {
                    customer.setUserRoles(UserRoles.ADMIN);
                }
                if (rs.getBoolean("for_fullname")) {
                    customer.setActive(true);
                }
            }
            preparedStatement.close();
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return customer;
    }

    public static void changeActive(String chatId, boolean active) {
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD)) {

            Class.forName("org.postgresql.Driver");

            String query = "update customer set for_fullname = ? where chat_id=?";

            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setBoolean(1, active);
            preparedStatement.setString(2, chatId);

            preparedStatement.executeUpdate();

        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static void setFullName(String chatId, String text) {
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD)) {

            Class.forName("org.postgresql.Driver");

            String query = "update customer set fullname = ? where chat_id=?";

            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, text);
            preparedStatement.setString(2, chatId);

            preparedStatement.executeUpdate();
            preparedStatement.close();

        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static void addMessageData(String chatId) {
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD)) {

            Class.forName("org.postgresql.Driver");

            String query = "insert into message_data(sender_chat_id) values (?)";

            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, chatId);

            preparedStatement.executeUpdate();
            preparedStatement.close();

        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static void addMessage(String text, String chatId) {
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD)) {

            Class.forName("org.postgresql.Driver");

            String query = "update message_data set sender_message = ? where sender_chat_id=? and admin_chat_id is null";

            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, text);
            preparedStatement.setString(2, chatId);

            preparedStatement.executeUpdate();
            preparedStatement.close();

        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static void updateMessageId(String chatId, String message, String customerChatId) {
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD)) {

            Class.forName("org.postgresql.Driver");

            String query = "update message_data set admin_chat_id = ? where sender_chat_id=? and sender_message = ?";

            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, chatId);
            preparedStatement.setString(2, customerChatId);
            preparedStatement.setString(3, message);

            preparedStatement.executeUpdate();
            preparedStatement.close();

        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static void updateMessage(String chatId, String customerMessage, String customerChatId, String text) {
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD)) {

            Class.forName("org.postgresql.Driver");

            String query = "update message_data set admin_message = ? where sender_chat_id=? and sender_message = ? and admin_chat_id = ?";

            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, text);
            preparedStatement.setString(2, customerChatId);
            preparedStatement.setString(3, customerMessage);
            preparedStatement.setString(4, chatId);

            preparedStatement.executeUpdate();
            preparedStatement.close();

        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static List<UserMessage> getMessagesFromCustomers() {
        List<UserMessage> messages = new ArrayList<>();

        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
             Statement statement = connection.createStatement()) {


            Class.forName("org.postgresql.Driver");

            String query = "select m.id, sender_chat_id, fullname, phone_number, sender_message from message_data m join customer c on m.sender_chat_id = c.chat_id where m.admin_chat_id is null and m.admin_message is null;";

            ResultSet rs = statement.executeQuery(query);

            while (rs.next()) {
                int messageId = rs.getInt(1);
                String senderChatId = rs.getString("sender_chat_id");
                String fullName = rs.getString("fullname");
                String phoneNumber = rs.getString("phone_number");
                String senderMessage = rs.getString("sender_message");

                messages.add(new UserMessage(messageId, senderChatId, fullName, phoneNumber, senderMessage));
            }

        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return messages;
    }

    public static boolean checkMessage(String messageId) {
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD)) {

            Class.forName("org.postgresql.Driver");

            String query = "select admin_message from message_data where id = ?;";

            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, Integer.parseInt(messageId));

            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                if (resultSet.getString("admin_message") == null) {
                    return false;
                }
            }

            preparedStatement.close();

        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return true;
    }

    public static List<OrderList> getOrderList() { //Todo getOrderList method and write it to a PDF File
        List<OrderList> orderList = new ArrayList<>();

        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
             Statement statement = connection.createStatement()
        ) {
            Class.forName("org.postgresql.Driver");

            String query = "SELECT b.customer_id, p.name, p.price , bd.quantity, b.total_price  FROM basket b JOIN basket_detail bd on b.id = bd.basket_id JOIN product p on bd.product_id = p.id WHERE b.is_confirmed IS TRUE";

            ResultSet resultSet = statement.executeQuery(query);
            while (resultSet.next()) {
                String customer_id = resultSet.getString("customer_id");
                String name = resultSet.getString("name");
                BigDecimal price = resultSet.getBigDecimal("price");
                int quantity = resultSet.getInt("quantity");
                BigDecimal total_price = resultSet.getBigDecimal("total_price");
                orderList.add(new OrderList(customer_id, name, price, quantity, total_price));
            }

        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }


        return orderList;
    }

    public static List<Discount> getNotDeletedDiscounts(String chatId) {
        List<Discount> discounts = new ArrayList();

        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
                Statement statement = connection.createStatement()){

            try {

                try {
                    Class.forName("org.postgresql.Driver");
                    String query = "SELECT * FROM discount where is_deleted = false and chat_id = '" + chatId + "'";
                    ResultSet resultSet = statement.executeQuery(query);

                    while (resultSet.next()) {
                        int id = resultSet.getInt(1);
                        int discount_percentage = resultSet.getInt("discount_percentage");
                        String name = resultSet.getString("name");
                        String start_time = resultSet.getString("start_time");
                        String end_time = resultSet.getString("end_time");
                        String photo_file_id = resultSet.getString("photo_file_id");
                        String chat_id = resultSet.getString("chat_id");
                        discounts.add(new Discount(id, chat_id, discount_percentage, name, start_time, end_time, photo_file_id));
                    }
                } catch (Throwable var17) {
                    if (statement != null) {
                        try {
                            statement.close();
                        } catch (Throwable var16) {
                            var17.addSuppressed(var16);
                        }
                    }

                    throw var17;
                }

                if (statement != null) {
                    statement.close();
                }
            } catch (Throwable var18) {
                if (connection != null) {
                    try {
                        connection.close();
                    } catch (Throwable var15) {
                        var18.addSuppressed(var15);
                    }
                }

                throw var18;
            }

            if (connection != null) {
                connection.close();
            }
        } catch (SQLException | ClassNotFoundException var19) {
            var19.printStackTrace();
        }

        return discounts;
    }

    public static void deleteDiscount(int discountId) { // Todo
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
             Statement statement = connection.createStatement()){
            String query = "DELETE FROM discount WHERE id = "+ discountId;

            statement.execute(query);

        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public static String createNewSubCategory(String name, Integer parentId) {
        String response = "";
        boolean isDuplicate = false;

        try ( Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
              Statement statement = connection.createStatement())
        {
        String query1 = "SELECT name FROM category WHERE parent_id IS NOT NULL";

            ResultSet resultSet = statement.executeQuery(query1);
            while (resultSet.next()){
                if (resultSet.getString("name").equalsIgnoreCase(name.trim())){
                    isDuplicate = true;
                }
            }

            if (!isDuplicate){
                    response = "Sub kategoriya muvaffaqiyatli qo'shildi!";
                    String query = "INSERT INTO category(name, parent_id) VALUES (?, ?)";
                try (Connection connection1 = DriverManager.getConnection(URL, USER, PASSWORD);
                    PreparedStatement preparedStatement = connection1.prepareStatement(query);
                ) {
                    Class.forName("org.postgresql.Driver");

                    preparedStatement.setString(1, name);
                    preparedStatement.setInt(2, parentId);

                    preparedStatement.executeUpdate();

                } catch (SQLException | ClassNotFoundException e) {
                    e.printStackTrace();
                }

            }else{
                response = "Bu nomdagi sub kategoriya mavjud";
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }



        return response;
    }


    public static String createNewParentCategory(String name) {
        String response = "";
        boolean isDuplicate = false;

        try ( Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
              Statement statement = connection.createStatement())
        {
            String query1 = "SELECT name FROM category WHERE parent_id IS NULL";

            ResultSet resultSet = statement.executeQuery(query1);
            while (resultSet.next()){
                if (resultSet.getString("name").equalsIgnoreCase(name.trim())){
                    isDuplicate = true;
                }
            }

            if (!isDuplicate){
                response = "Ota kategoriya muvaffaqiyatli qo'shildi!";
                String query = "INSERT INTO category(name) VALUES (?)";
                try (Connection connection1 = DriverManager.getConnection(URL, USER, PASSWORD);
                     PreparedStatement preparedStatement = connection1.prepareStatement(query);
                ) {
                    Class.forName("org.postgresql.Driver");

                    preparedStatement.setString(1, name);

                    preparedStatement.executeUpdate();

                } catch (SQLException | ClassNotFoundException e) {
                    e.printStackTrace();
                }

            }else{
                response = "Bu nomdagi ota kategoriya mavjud!";
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }



        return response;
    }


    public static String deleteParentCategory(String name) {
        String response = "";
        boolean doesExist = false;

        try ( Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
              Statement statement = connection.createStatement())
        {
            String query1 = "SELECT name FROM category WHERE parent_id IS NULL";

            ResultSet resultSet = statement.executeQuery(query1);
            while (resultSet.next()){
                if (resultSet.getString("name").equalsIgnoreCase(name.trim())){
                    doesExist = true;
                }
            }

            if (doesExist){
                response = "Ota kategoriya muvaffaqiyatli o'chirildi!";
                String query = "UPDATE category SET is_deleted = true WHERE name = (?)";
                try (Connection connection1 = DriverManager.getConnection(URL, USER, PASSWORD);
                     PreparedStatement preparedStatement = connection1.prepareStatement(query);
                ) {
                    Class.forName("org.postgresql.Driver");

                    preparedStatement.setString(1, name);

                    preparedStatement.executeUpdate();

                } catch (SQLException | ClassNotFoundException e) {
                    e.printStackTrace();
                }

            }else{
                response = "Bu nomdagi ota kategoriya mavjud emas!";
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }



        return response;
    }

    public static String deleteSubCategory(String name) {
        String response = "";
        boolean doesExist = false;

        try ( Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
              Statement statement = connection.createStatement())
        {
            String query1 = "SELECT name FROM category WHERE parent_id IS NOT NULL";

            ResultSet resultSet = statement.executeQuery(query1);
            while (resultSet.next()){
                if (resultSet.getString("name").equalsIgnoreCase(name.trim())){
                    doesExist = true;
                }
            }

            if (doesExist){
                response = "Ota kategoriya muvaffaqiyatli o'chirildi!";
                String query = "UPDATE category SET is_deleted = true WHERE name = (?)";
                try (Connection connection1 = DriverManager.getConnection(URL, USER, PASSWORD);
                     PreparedStatement preparedStatement = connection1.prepareStatement(query);
                ) {
                    Class.forName("org.postgresql.Driver");

                    preparedStatement.setString(1, name);

                    preparedStatement.executeUpdate();

                } catch (SQLException | ClassNotFoundException e) {
                    e.printStackTrace();
                }

            }else{
                response = "Bu nomdagi ota kategoriya mavjud emas!";
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }



        return response;
    }

    public static List<Product> getProductOneByOne(int categoryId){
        List<Product> products = new ArrayList<>();
            String query = "SELECT * FROM product WHERE is_deleted = FALSE and category_id = (?) ORDER BY id ";
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement preparedStatement = connection.prepareStatement(query)
        ) {
            preparedStatement.setInt(1 ,categoryId);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()){
                int id = resultSet.getInt("id");
                String name = resultSet.getString("name");
                int category_id = resultSet.getInt("category_id");
                BigDecimal price = resultSet.getBigDecimal("price");
                String photo_file_id = resultSet.getString("photo_file_id");
                String color = resultSet.getString("color");
                products.add(new Product(id, name, category_id, price, false, photo_file_id, color));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return products;

    }

    public static String getBasket(String chatId) {
        String str = "";
        try(Connection connection = DriverManager.getConnection(URL, USER, PASSWORD)) {

            Class.forName("org.postgresql.Driver");

            String query = "select count(d.product_id), sum(pr.price * d.quantity), b.id from basket_detail d join basket b on b.id = d.basket_id join product pr ON pr.id = d.product_id where b.customer_id = ? and b.is_confirmed = FALSE group by b.id;";

            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, chatId);

            ResultSet resultSet = preparedStatement.executeQuery();
            int num = 0, index = 0;
            double sum = 0.0;
            while (resultSet.next()){
                num = resultSet.getInt(1);
                sum = resultSet.getDouble(2);
                index = resultSet.getInt(3);
            }
            str = str + num + ":" + sum + ":" + index;

            preparedStatement.close();

        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return str;
    }

    public static List<UserProduct> getBasketDetails(int basketId) {
        List<UserProduct> userProducts = new ArrayList<>();
        try(Connection connection = DriverManager.getConnection(URL, USER, PASSWORD)) {

            Class.forName("org.postgresql.Driver");

            String query = "select bd.id, pr.name, pr.price, pr.photo_file_id, pr.color, bd.quantity from basket_detail bd join product pr on pr.id = bd.product_id where bd.basket_id = ?;";

            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, basketId);

            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()){
                int productId = resultSet.getInt("id");
                String name = resultSet.getString("name");
                double price = resultSet.getDouble("price");
                String photoUrl = resultSet.getString("photo_file_id");
                String color = resultSet.getString("color");
                int quantity = resultSet.getInt("quantity");

                userProducts.add(new UserProduct(productId, name, price, photoUrl, color, quantity));
            }

            preparedStatement.close();

        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return userProducts;
    }

    public static void updateBasket(int basketId) {
        try(Connection connection = DriverManager.getConnection(URL, USER, PASSWORD)) {

            Class.forName("org.postgresql.Driver");

            String query = "update basket set is_confirmed = true where id =?;";

            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, basketId);

            preparedStatement.executeUpdate();
            preparedStatement.close();

        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static void clearBasket(int basketId) {
        try(Connection connection = DriverManager.getConnection(URL, USER, PASSWORD)) {

            Class.forName("org.postgresql.Driver");

            String query = "delete from basket_detail where basket_id = ?;";
            String query1 = "delete from basket where id = ?;";

            PreparedStatement preparedStatement = connection.prepareStatement(query);
            PreparedStatement preparedStatement1 = connection.prepareStatement(query1);
            preparedStatement.setInt(1, basketId);
            preparedStatement1.setInt(1, basketId);

            preparedStatement.executeUpdate();
            preparedStatement1.executeUpdate();
            preparedStatement.close();

        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static void increaseQuantityOfBasketDetail(int bdId) {
        try(Connection connection = DriverManager.getConnection(URL, USER, PASSWORD)) {

            Class.forName("org.postgresql.Driver");

            String query = "update basket_detail set quantity = quantity + 1 where id =?;";

            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, bdId);

            preparedStatement.executeUpdate();
            preparedStatement.close();

        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static void decreaseQuantityOfBasketDetail(int bdId) {
        try(Connection connection = DriverManager.getConnection(URL, USER, PASSWORD)) {

            Class.forName("org.postgresql.Driver");

            String query = "update basket_detail set quantity = quantity - 1 where id =?;";

            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, bdId);

            preparedStatement.executeUpdate();
            preparedStatement.close();

        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static int getQuantityOfBasketDetail(int bdId) {
        int number = 0;
        try(Connection connection = DriverManager.getConnection(URL, USER, PASSWORD)) {

            Class.forName("org.postgresql.Driver");

            String query = "select quantity from basket_detail where id =?;";

            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, bdId);

            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()){
                number = resultSet.getInt(1);
            }
            preparedStatement.close();

        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return number;
    }

    public static int getBasketId(String chatId) {
        int number = 0;
        try(Connection connection = DriverManager.getConnection(URL, USER, PASSWORD)) {

            Class.forName("org.postgresql.Driver");

            String query = "select id from basket join customer on customer.chat_id = basket.customer_id where chat_id =? and basket.is_confirmed = false;";

            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, chatId);

            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()){
                number = resultSet.getInt(1);
            }
            preparedStatement.close();

        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return number;
    }

    public static void deleteExpiredDiscounts() {
            String sql = "{ call delete_expired_discounts() }";
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
            CallableStatement callableStatement = connection.prepareCall(sql)){

            callableStatement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static List<Discount> getDiscountsList(String chatId) {
        try {
            Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
            Statement statement = connection.createStatement();
            String query = """
                    select * from discount where is_deleted=false;
                    """;

            ResultSet resultSet = statement.executeQuery(query);

            List<Discount> discountsList = new ArrayList<>();
            while (resultSet.next()){
                int discountPercentage = resultSet.getInt("discount_percentage");
                String name = resultSet.getString("name");
                Date start_time = resultSet.getDate("start_time");
                Date endTime = resultSet.getDate("end_time");
                String photoFileId = resultSet.getString("photo_file_id");
                if (photoFileId==null||photoFileId.equals("")){
                    discountsList.add(new Discount(chatId,discountPercentage,name,start_time.toString(),endTime.toString(),null));
                }else
                    discountsList.add(new Discount(chatId,discountPercentage,name,start_time.toString(),endTime.toString(),photoFileId));
            }
            connection.close();
            statement.close();

            return discountsList;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void addBasketProductUser(Integer product_id,String chat_id) {
        int number = 0;
        int basketId = getBasketId(chat_id);
        String query = "insert into basket_detail (basket_id,product_id) values(?,?);";
        try(Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
            PreparedStatement preparedStatement = connection.prepareStatement(query);
        ) {
            Class.forName("org.postgresql.Driver");

            preparedStatement.setInt(1, basketId);
            preparedStatement.setInt(2, product_id);
            preparedStatement.execute();

        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }

    }

    public  static List<Product> searchProductName(String searching){
        List<Product> productList = new ArrayList<>();
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD)) {
            Class.forName("org.postgresql.Driver");

            String query = "select * from product where name ILIKE '"+searching+"';";


            Statement statement = connection.createStatement();

            ResultSet rs= statement.executeQuery(query);

            while (rs.next()) {
                int id = rs.getInt(1);
                String name = rs.getString(2);
                int category_id = rs.getInt(3);
                BigDecimal price=rs.getBigDecimal(4);
                boolean is_deleted = rs.getBoolean(5);
                String photo_file_id = rs.getString(6);
                String color = rs.getString(7);
                productList.add(new Product(id,name,category_id,price,is_deleted,photo_file_id,color));
            }
            return  productList;

        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return  null;

    }

    public  static List<Product> searchProductByPrice(String searchPrice){
        String[] pricersearch=searchPrice.split("-");
        int beginPrice= Integer.parseInt(pricersearch[0]);
        int endPrice= Integer.parseInt(pricersearch[1]);

        List<Product> productList = new ArrayList<>();
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD)) {
            Class.forName("org.postgresql.Driver");

            String query = "select * from product where price between "+beginPrice+" and "+endPrice+";";


            Statement statement = connection.createStatement();

            ResultSet rs= statement.executeQuery(query);
            while (rs.next()) {
                int id = rs.getInt(1);
                String name = rs.getString(2);
                int category_id = rs.getInt(3);
                BigDecimal price=rs.getBigDecimal(4);
                boolean is_deleted = rs.getBoolean(5);
                String photo_file_id = rs.getString(6);
                String color = rs.getString(7);
                productList.add(new Product(id,name,category_id,price,is_deleted,photo_file_id,color));
            }
            return  productList;

        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return  null;

    }

    public  static List<Product> searchProductColor(String searchColor){
        List<Product> productList = new ArrayList<>();
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD)) {
            Class.forName("org.postgresql.Driver");

            String query = "select * from product where color ILIKE '"+searchColor+"';";

            Statement statement = connection.createStatement();

            ResultSet rs= statement.executeQuery(query);
            while (rs.next()) {
                int id = rs.getInt(1);
                String name = rs.getString(2);
                int category_id = rs.getInt(3);
                BigDecimal price=rs.getBigDecimal(4);
                boolean is_deleted = rs.getBoolean(5);
                String photo_file_id = rs.getString(6);
                String color = rs.getString(7);
                productList.add(new Product(id,name,category_id,price,is_deleted,photo_file_id,color));
            }
            return  productList;

        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return  null;

    }

    public static void addBasketProduct(String chat_id) {
        int number = 0;
        int basketId = getBasketId(chat_id);
        String query = "insert into basket (customer_id) values(?);";
        try(Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
            PreparedStatement preparedStatement = connection.prepareStatement(query);
        ) {
            Class.forName("org.postgresql.Driver");

            preparedStatement.setString(1, chat_id);
            preparedStatement.execute();

        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }

    }
    public static String deleteProduct(Integer product_id) {
        //
        String response = "";

        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD)) {

            String query = "update product set is_deleted=true  where id=?";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, product_id);
            if (preparedStatement.executeUpdate() == 1) {
                response = "Successfully delete product ";
            }

        } catch (SQLException e) {
            response = "Productni delete qilishda exceptionga tushdi bu ";
        }
        return response;
    }
}
