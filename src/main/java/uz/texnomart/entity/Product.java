package uz.texnomart.entity;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class Product{
    private Integer id;
    private String name;
    private Category category_id;
    private Double price;
    private Double rate;
    private Boolean is_deleted;
    private String photo_file_id;
    private String  color;
}
