package network.requests;

import java.io.Serializable;

public class StartGameRequest extends Request implements Serializable {
    private String username;

    public StartGameRequest(String username) {
        this.username = username;
    }

    public String getUsername() {
        return username;
    }
}