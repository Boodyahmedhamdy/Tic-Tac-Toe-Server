

/**
 *
 * @author HP
 */
public class RegisterRequest extends Request {
    private String username;
    private String password;
    private String email;

    public RegisterRequest(String username, String password, String email) {
        super("REGISTER");
        this.username = username;
        this.password = password;
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getEmail() {
        return email;
    }
}

    
    
