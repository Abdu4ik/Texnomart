package uz.texnomart.entity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@NoArgsConstructor
@AllArgsConstructor
@Data

public class OrderList {
    private String customer_id;
    private String productName;
    private BigDecimal price;
    private Integer quantity;
    private BigDecimal total_price;

}
