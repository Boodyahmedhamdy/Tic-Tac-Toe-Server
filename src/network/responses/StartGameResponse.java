package network.responses;

/**
 *
 * @author HP
 */
public class StartGameResponse extends Response{
    
    protected String username;

    public StartGameResponse(String username) {
     
        this.username = username;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
    
    
    
}
