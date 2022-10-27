package uz.texnomart.entity;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.checkerframework.checker.units.qual.K;

import java.time.LocalDateTime;
import java.time.LocalTime;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class Discount {
    private Integer id;
    private Integer discount_percentage;
    private String  name;
    private LocalDateTime  start_time;
    private LocalDateTime  end_time;
    private String photo_file_id;

}
