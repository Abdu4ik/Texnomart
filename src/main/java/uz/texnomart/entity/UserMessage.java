package uz.texnomart.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class UserMessage {
    private int id;
    private String senderChatId;
    private String fullName;
    private String phoneNumber;
    private String senderMessage;
}
