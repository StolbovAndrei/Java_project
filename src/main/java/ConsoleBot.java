import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

public class ConsoleBot {
    public static void main(String[] args) throws Exception {
        try {
            UserData userData = new UserData();
            TelegramBotsApi botsApi = new TelegramBotsApi(DefaultBotSession.class);
            botsApi.registerBot(new OurTgBot(userData));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }}
