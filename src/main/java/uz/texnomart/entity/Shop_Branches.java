package uz.texnomart.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class Shop_Branches {
    private Integer id;
    private String  location_url;
    private String  name;
    private String  working_hours;
}
