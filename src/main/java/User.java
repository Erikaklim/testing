import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Test;

import java.util.UUID;

public class User {
    private String firstName;
    private String lastName;
    private String email;
    private String password;

    public User(){
        firstName = RandomStringUtils.randomAlphabetic(5);
        lastName = RandomStringUtils.randomAlphabetic(5);
        email = "user" + UUID.randomUUID()
                .toString().replaceAll("-", "") + "@example.com";
        password = RandomStringUtils.randomAlphanumeric(10);
    }
    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

}
