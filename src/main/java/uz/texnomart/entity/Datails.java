package uz.texnomart.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class Datails {
    private Integer id;
    private String name;
    private Datails parent_id;
    private Product product_id;
}
