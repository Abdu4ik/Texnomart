package uz.texnomart.entity;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class Product{
    private Integer id;
    private String name;
    private Integer category_id;
    private BigDecimal price;
    private boolean is_deleted;
    private String photo_file_id;
    private String  color;
}
