import java.util.HashMap;
import java.util.Map;

public class UserData {
    private final Map<Long, User> users = new HashMap<>();

    public boolean checkUser(long userId) {
        return users.containsKey(userId);
    }
    public void addUser(long chatId) {
        users.put(chatId, new User(chatId));
    }
    public UserState checkUserState(long chatId) {
        if(!users.containsKey(chatId)) {
            return null;
        }
        return users.get(chatId).getState();
    }
    public void changeUserState(long chatId, UserState userState) {
        users.put(chatId, new User(chatId, userState));
    }
    public Map<Long, User> getUsers() {
        return users;
    }

}
