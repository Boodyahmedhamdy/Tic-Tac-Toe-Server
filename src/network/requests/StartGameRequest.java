package network.requests;

import java.io.Serializable;

public class StartGameRequest extends Request implements Serializable {

    private String senderUsername;
    private String recieverUsername;

    public StartGameRequest(String senderUsername, String recieverUsername) {
        this.senderUsername = senderUsername;
        this.recieverUsername = recieverUsername;
    }
    
    public String getSenderUsername() {
        return senderUsername;
    }

    public void setSenderUsername(String senderUsername) {
        this.senderUsername = senderUsername;
    }

    public String getRecieverUsername() {
        return recieverUsername;
    }

    public void setRecieverUsername(String recieverUsername) {
        this.recieverUsername = recieverUsername;
    }

}