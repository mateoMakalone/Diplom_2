package site.stellarburgers.User;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserEmailCredentials {
    private String email;
    public static UserEmailCredentials from(User user){
        return new UserEmailCredentials(user.getEmail());
    }
}
