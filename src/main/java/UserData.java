import java.util.HashMap;
import java.util.Map;

public class UserData {
    public static Map<Long, User> users = new HashMap<>();


    public static boolean checkUser(long userId) {
        return users.containsKey(userId);
    }
    public static void addUser(long chatId) {
        users.put(chatId, new User(chatId));
    }
    public static UserState checkUserState(long chatId) {
        if(!users.containsKey(chatId)) {
            return null;
        }
        return users.get(chatId).getState();
    }
    public static void changeUserState(long chatId, UserState userState) {
        users.put(chatId, new User(chatId, userState));
    }




    public Map<Long, User> getUsers() {
        return users;
    }

    public void setUsers(Map<Long, User> users) {
        UserData.users = users;
    }
}
