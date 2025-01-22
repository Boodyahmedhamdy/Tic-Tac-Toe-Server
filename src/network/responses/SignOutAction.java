package network.responses;

import network.requests.Request;
import java.io.Serializable;

public class SignOutAction extends Request implements Serializable {
    private String username;

    public SignOutAction(String username) {
        this.username = username;
    }

    public String getUsername() {
        return username;
    }
}