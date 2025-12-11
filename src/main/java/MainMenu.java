import org.telegram.telegrambots.meta.api.methods.ParseMode;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.io.IOException;
import java.util.List;
import java.util.Map;


public class MainMenu {
    private final GeniusClient geniusClient;
    private final FormatClass formatClass;
    public MainMenu() {
        try {
            this.geniusClient = new GeniusClient();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        this.formatClass = new FormatClass();
    }
    public MainMenu(GeniusClient geniusClient, FormatClass formatClass) {
        this.geniusClient = geniusClient;
        this.formatClass = formatClass;
    }

    public SendMessage mainMenu(Update update, UserData userData) {
        String message = update.getMessage().getText();
        long chatId = update.getMessage().getChatId();
        SendMessage outMessage = new SendMessage();

        outMessage.setChatId(chatId);
        outMessage.setParseMode(ParseMode.HTML);

        if (userData.checkUser(chatId)) {
            if (userData.checkUserState(chatId) == UserState.WAITING_FOR_ACTIONS) {
                switch (message) {
                    case "/help", "Помощь": {
                        outMessage.setText(Messages.HELP_MESSAGE);
                        break;
                    }
                    case "/search", "Поиск": {
                        userData.changeUserState(chatId, UserState.WAITING_FOR_ARTISTS);
                        outMessage.setText(Messages.SEARCH_MESSAGE);
                        break;
                    }
                    default: {
                        outMessage.setText(Messages.DEFAULT_MESSAGE);
                        break;
                    }
                }
            } else if (userData.checkUserState(chatId) == UserState.WAITING_FOR_ARTISTS) {
                if (message.equalsIgnoreCase("/back") || message.equalsIgnoreCase("Назад")) {
                    userData.changeUserState(chatId, UserState.WAITING_FOR_ACTIONS);
                    outMessage.setText(Messages.BACK_MESSAGE);
                } else {
                    outMessage.setText(startSearch(message) + "\nЕсли хотите выйти в меню, пропишите /back");
                }
            }
        }
        else {
            userData.addUser(chatId);
            outMessage.setText(Messages.FIRST_MESSAGE);
        }
        return outMessage;
    }


    public String startSearch(String searchText) {
        try {
            Map<String, List<String>> songs = geniusClient.searchArtists(searchText);
            return formatClass.formatResult(songs);
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

}