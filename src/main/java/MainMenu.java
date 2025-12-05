import org.telegram.telegrambots.meta.api.objects.Update;

import java.io.IOException;
import java.util.List;
import java.util.Map;


public class MainMenu {


    public String mainMenu(Update up) {
        String message = up.getMessage().getText();
        long chatId = up.getMessage().getChatId();
        String outMessage = "";

        if(message.equalsIgnoreCase("/start")){
            if(!UserData.checkUser(chatId)){
                UserData.addUser(chatId);
            }
            return Messages.FIRST_MESSAGE;
        }

        if(UserData.checkUserState(chatId) == UserState.WAITING_FOR_ACTIONS) {
            switch (message){
                case "/help": {
                    outMessage = Messages.HELP_MESSAGE;
                    break;
                }
                case "/search": {
                    UserData.changeUserState(chatId, UserState.WAITING_FOR_ARTISTS);
                    outMessage = Messages.SEARCH_MESSAGE;
                    break;
                }
                default: {
                    outMessage = Messages.DEFAULT_MESSAGE;
                    break;
                }
            }
        }
        else if(UserData.checkUserState(chatId) == UserState.WAITING_FOR_ARTISTS) {
            if(message.equalsIgnoreCase("/back")){
                UserData.changeUserState(chatId, UserState.WAITING_FOR_ACTIONS);
                outMessage = Messages.BACK_MESSAGE;
            }
            else {
                outMessage = startSearch(message) + "\nЕсли хотите выйти в меню, пропишите /back";
            }
        }
        return outMessage;
    }


    public String startSearch(String searchText) {
        SearchArtists searchArtists;
        try {
            searchArtists = new SearchArtists();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        ParserJson parserJson = new ParserJson();
        String resultSearchOfTitles;
        try {
            String responseSearch = searchArtists.searchArtists(searchText);
            Map<String, List<String>> songs = parserJson.parserJson(responseSearch);
            resultSearchOfTitles = parserJson.resultOfSearch(songs);

        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }


        return resultSearchOfTitles;


    }

}
