package uz.texnomart.entity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@AllArgsConstructor
@NoArgsConstructor
@Data

public class Discount {
    private Integer id;
    private String chatId;
    private Integer discount_percentage;
    private String  name;
    private String  start_time;
    private String  end_time;
    private String photo_file_id;

    public Discount(String chatId, Integer discount_percentage, String name, String start_time, String end_time, String photo_file_id) {
        this.chatId = chatId;
        this.discount_percentage = discount_percentage;
        this.name = name;
        this.start_time = start_time;
        this.end_time = end_time;
        this.photo_file_id = photo_file_id;
    }
}