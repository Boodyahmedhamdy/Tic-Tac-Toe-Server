

/**
 *
 * @author HP
 */
import java.io.Serializable;
public class LoginResponse extends Response {
    private String userName;

    public LoginResponse(boolean success, String message, String userName) {
        super(success, message);
        this.userName = userName;
    }

    public String getUserId() {
        return userName;
    }
}