package uz.texnomart.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import uz.texnomart.enums.UserRoles;

@NoArgsConstructor
@AllArgsConstructor
@Data

public class TelegramUser {

    private String chatId;
    private String firstName;
    private String lastName;
    private String phoneNumber;
    private UserRoles userRoles;


}
