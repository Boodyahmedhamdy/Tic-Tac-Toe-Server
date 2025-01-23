package network.actions;

import network.requests.Request;

/**
 *
 * @author HP
 */
public class SignOutAction extends Request {
    
    private String username;

    public SignOutAction() {  }
    public SignOutAction(String username) {
        this.username = username;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
    
    
    
    
    
    
    
}
