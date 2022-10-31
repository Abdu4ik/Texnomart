package uz.texnomart.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class UserProduct {
    private int id;
    private String name;
    private double price;
    private String photo_file_url;
    private String color;
    private int quantity;
}
