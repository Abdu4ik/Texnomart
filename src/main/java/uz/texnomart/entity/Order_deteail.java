package uz.texnomart.entity;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class Order_deteail {
    private  Integer id;
    private  Order order_id;
    private Product product_id;
    private Integer quantitiy;
}
