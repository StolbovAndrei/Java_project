public class Keys {
    public String getBotToken() {

        return botToken;
    }

    public void setBotToken(String botToken) {
        this.botToken = botToken;
    }

    public String getGeniusApiToken() {
        return geniusApiToken;
    }

    public void setGeniusApiToken(String geniusApiToken) {
        this.geniusApiToken = geniusApiToken;
    }

    public String getBotUsername() {
        return botUsername;
    }

    public void setBotUsername(String botUsername) {
        this.botUsername = botUsername;
    }

    private String botToken;
    private String botUsername;
    private String geniusApiToken;
}
