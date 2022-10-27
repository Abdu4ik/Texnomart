package uz.texnomart.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class Basket_detail {
    private Integer id;
    private Basket basket_id;
    private Product product_id;
    private Integer quantity;
}
