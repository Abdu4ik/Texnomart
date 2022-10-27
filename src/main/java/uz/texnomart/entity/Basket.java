package uz.texnomart.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class Basket {
    private Integer id;
    private String user_chatId;
    private Double total_price;
    private Boolean is_approved;

}
