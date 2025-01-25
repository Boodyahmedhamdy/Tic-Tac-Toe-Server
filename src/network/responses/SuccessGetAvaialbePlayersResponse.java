package network.responses;

import java.util.List;

/**
 *
 * @author HP
 */
public class SuccessGetAvaialbePlayersResponse extends GetAvailablePlayersResponse{
    private List<String> usernames;

    public SuccessGetAvaialbePlayersResponse(List<String> usernames) {
        this.usernames = usernames;
    }

    public List<String> getUsernames() {
        return usernames;
    }

    public void setUsernames(List<String> usernames) {
        this.usernames = usernames;
    }
}
