public class User {
    public User(long chatId) {
        this.chatId = chatId;
        this.state = UserState.WAITING_FOR_ACTIONS;
    }
    public User(long chatId, UserState userState) {
        this.chatId = chatId;
        this.state = userState;
    }

    public long getChatId() {
        return chatId;
    }

    private final long chatId;

    public UserState getState() {
        return state;
    }

    public void setState(UserState state) {
        this.state = state;
    }

    private UserState state;
}
