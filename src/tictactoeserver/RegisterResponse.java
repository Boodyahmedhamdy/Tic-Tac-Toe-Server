

/**
 *
 * @author HP
 */
import java.io.Serializable;

public class RegisterResponse extends Response {
    public RegisterResponse(boolean success, String message) {
        super(success, message);
    }
}
