package network.actions;

/**
 *
 * @author HP
 */
public class SignOutAction extends Action {
    
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
