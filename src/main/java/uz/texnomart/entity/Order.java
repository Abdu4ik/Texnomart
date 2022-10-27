package uz.texnomart.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.telegram.telegrambots.meta.api.objects.User;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class Order {
    private Integer id;
    private User user_id;
    private Double total_price;
    private LocalDateTime created_at;
    private String status;
    private Double delivary_price;
}
