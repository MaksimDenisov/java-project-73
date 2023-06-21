package hexlet.code.app.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class UserTO {
    private String email;
    private String firstName;
    private String lastName;
    private String password;
}
