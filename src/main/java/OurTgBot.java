import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class OurTgBot extends TelegramLongPollingBot  {
    private final Map<Long, UserState> userStates = new HashMap<>();
    ParserJsonKeys parserJsonKeys = new ParserJsonKeys();
    Keys keys = parserJsonKeys.parserJsonKeysToPojo();
    private final String BOT_USERNAME = keys.getBotUsername();
    private final String BOT_TOKEN = keys.getBotToken();




    @Override
    public void onUpdateReceived (Update update){
        if(update.hasMessage() && update.getMessage().hasText()) {
            String message = update.getMessage().getText();
            long chatId = update.getMessage().getChatId();

            if(message.equalsIgnoreCase("/start")){
                userStates.put(chatId, UserState.WAITING_FOR_ACTIONS);
                sendText(chatId, Messages.FIRST_MESSAGE);
                return;
            }
            UserState state = userStates.get(chatId);

            if(state == null){
                sendText(chatId, Messages.FIRST_MESSAGE);
            }

            else if(state == UserState.WAITING_FOR_ACTIONS) {
                switch (message) {
                    case "/help": {
                        sendText(chatId, Messages.HELP_MESSAGE);
                        userStates.put(chatId, UserState.WAITING_FOR_ACTIONS);
                        break;
                    } case "/back": {
                        userStates.put(chatId, UserState.WAITING_FOR_ACTIONS);
                        sendText(chatId, "Вы вышли в меню, ожидаю действий!");
                        break;
                    } case "/search" : {
                        userStates.put(chatId, UserState.WAITING_FOR_ARTISTS);
                        sendText(chatId, "Введите имя исполнителя и название трека для поиска: ");
                        break;
                    }
                    default:
                        sendText(chatId, Messages.DEFAULT_MESSAGE);
                        userStates.put(chatId, UserState.WAITING_FOR_ACTIONS);
                        break;
                }
            }
            else if (state == UserState.WAITING_FOR_ARTISTS){
                SearchArtists searchArtists;
                try {
                    searchArtists = new SearchArtists();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                ParserJson parserJson = new ParserJson();
                    if(message.equalsIgnoreCase("/help")){
                        sendText(chatId, Messages.HELP_MESSAGE);
                        userStates.put(chatId, UserState.WAITING_FOR_ACTIONS);
                        return;
                    }
                    else if(message.equalsIgnoreCase("/back")){
                        userStates.put(chatId, UserState.WAITING_FOR_ACTIONS);
                        sendText(chatId, "Вы вышли в меню, ожидаю действий!");
                        return;
                    }
                    try{
                       String responseSearch = searchArtists.searchArtists(message);
                       Map<String, List<String>> songs = parserJson.parserJson(responseSearch);
                       String resultSearchOfTitles = parserJson.resultOfSearch(songs);
                       sendText(chatId, "Найденные треки: \n");
                       sendText(chatId, resultSearchOfTitles);

                    } catch (IOException | InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                    sendText(chatId, "Если желаете выйти, то напишите /back");
                        if(message.equalsIgnoreCase("/back")){
                            userStates.put(chatId, UserState.WAITING_FOR_ACTIONS);
                            sendText(chatId, "Вы вышли в меню, ожидаю действий!");
                        }

            }

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
