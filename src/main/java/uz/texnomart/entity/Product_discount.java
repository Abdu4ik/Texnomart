package uz.texnomart.entity;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class Product_discount {
    private Integer id;
    private Product product_id;
    private Discount discount_id;
}
