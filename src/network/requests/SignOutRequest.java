package network.requests;

/**
 *
 * @author HP
 */
public class SignOutRequest extends Request {
    private String username;


    public SignOutRequest(String username) {
        this.username = username;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
    
    
    
}
