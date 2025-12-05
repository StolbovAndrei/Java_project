import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.IOException;


public class OurTgBot extends TelegramLongPollingBot  {
    ParserKeys parserKeys = new ParserKeys();
    Keys keys = parserKeys.getKeys();

    private final String BOT_USERNAME = keys.getBotUsername();
    private final String BOT_TOKEN = keys.getBotToken();




    @Override
    public void onUpdateReceived (Update update){
        if(update.hasMessage() && update.getMessage().hasText()) {
            long chatId = update.getMessage().getChatId();

            MainMenu mainMenu = new MainMenu();
            String result = mainMenu.mainMenu(update);
            sendText(chatId, result);

        }
    }

    private void sendText(long chatId, String text) {
        SendMessage msg = new SendMessage();
        msg.setChatId(String.valueOf(chatId));
        msg.setText(text);
        try{
            execute(msg);
        } catch (TelegramApiException e){
            e.printStackTrace();
        }
    }
    public OurTgBot() throws IOException {

    }
    @Override
    public String getBotUsername() {
        return BOT_USERNAME;
    }
    @Override
    public String getBotToken(){
        return BOT_TOKEN;
    }

}
