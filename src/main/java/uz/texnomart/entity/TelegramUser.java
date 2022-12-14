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
    private String fullName;
    private String phoneNumber;
    private UserRoles userRoles ;
    private boolean isActive;

    public TelegramUser(String chatId, String fullName, String phoneNumber, UserRoles userRoles) {
        this.chatId = chatId;
        this.fullName = fullName;
        this.phoneNumber = phoneNumber;
        this.userRoles = userRoles;
    }

}
