import org.telegram.telegrambots.meta.api.methods.ParseMode;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainMenu {
    private final GeniusClient geniusClient;
    private final Buttons buttons = new Buttons();
    private final Map<Long, List<String>> userTrackList = new HashMap<>();
    private final Map<Long, List<String>> trackList = new HashMap<>();
    private int index = 0;

    public MainMenu() {
        try {
            this.geniusClient = new GeniusClient();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public MainMenu(GeniusClient geniusClient) {
        this.geniusClient = geniusClient;
    }

    public SendMessage mainMenu(Update update, UserData userData) throws Exception {
        String message = update.getMessage().getText();
        long chatId = update.getMessage().getChatId();
        SendMessage outMessage = new SendMessage();

        outMessage.setChatId(String.valueOf(chatId));
        outMessage.setParseMode(ParseMode.HTML);

        if (!userData.checkUser(chatId)) {
            userData.addUser(chatId);
            outMessage.setText(Messages.FIRST_MESSAGE);
            outMessage.setReplyMarkup(buttons.getKeyboardForUser(chatId, userData));
            return outMessage;
        }
        UserState currentState = userData.checkUserState(chatId);

        switch (currentState) {
            case WAITING_FOR_ACTIONS:
                return handleMainMenu(message, userData, chatId);
            case WAITING_FOR_ARTISTS:
                return handleSearchMenu(message, userData, chatId);
            case WAITING_FOR_TRACK_SELECTION:
                return handleTrackSelection(message, userData, chatId);
            case WAITING_FOR_TEXT:
                return handleSendText(userData, chatId);
            case WAITING_FOR_LINK:
                return handleSendTrack(userData, chatId);
            default:
                outMessage.setText(Messages.DEFAULT_MESSAGE);
                outMessage.setReplyMarkup(buttons.getKeyboardForUser(chatId, userData));
                return outMessage;
        }
    }

    public SendMessage handleMainMenu(String command, UserData userData, long chatId) {
        SendMessage outMessage = new SendMessage();
        outMessage.setChatId(String.valueOf(chatId));
        outMessage.setParseMode(ParseMode.HTML);

        switch (command.toLowerCase()) {
            case "/help", "помощь":
                outMessage.setText(Messages.HELP_MESSAGE);
                break;
            case "/search", "поиск":
                userData.changeUserState(chatId, UserState.WAITING_FOR_ARTISTS);
                outMessage.setText(Messages.SEARCH_MESSAGE);
                break;
            default:
                outMessage.setText(Messages.DEFAULT_MESSAGE);
                break;
        }

        outMessage.setReplyMarkup(buttons.getKeyboardForUser(chatId, userData));
        return outMessage;
    }

    public SendMessage handleSearchMenu(String search, UserData userData, long chatId) {
        SendMessage outMessage = new SendMessage();
        outMessage.setChatId(String.valueOf(chatId));
        outMessage.setParseMode(ParseMode.HTML);

        if (search.equalsIgnoreCase("/back") || search.equalsIgnoreCase("назад")) {
            userData.changeUserState(chatId, UserState.WAITING_FOR_ACTIONS);
            outMessage.setText(Messages.BACK_MESSAGE);
            outMessage.setReplyMarkup(buttons.getKeyboardForUser(chatId, userData));
            return outMessage;
        }

        try {
            Map<String, List<String>> songs = geniusClient.searchArtists(search);
            List<String> urls = geniusClient.getUrls(search);
            userTrackList.put(chatId, urls);
            List<String> tracks = new ArrayList<>();
            for (Map.Entry<String, List<String>> entry : songs.entrySet()) {
                tracks.add(entry.getKey() + "-" + songs.get(entry.getKey()).get(0));
            }
            trackList.put(chatId, tracks);




            if (songs == null || songs.isEmpty()) {
                outMessage.setText("По вашему запросу ничего не найдено.\nПопробуйте изменить запрос.");
                outMessage.setReplyMarkup(buttons.getKeyboardForUser(chatId, userData));
                return outMessage;
            }

            outMessage.setText("Найденные треки:\n");
            List<List<InlineKeyboardButton>> buttonsRows = buttons.formatResult(songs);
            InlineKeyboardMarkup keyboardMarkup = new InlineKeyboardMarkup();
            keyboardMarkup.setKeyboard(buttonsRows);
            outMessage.setReplyMarkup(keyboardMarkup);


        } catch (Exception e) {
            outMessage.setText("Произошла ошибка при поиске.\nПопробуйте еще раз.");
            outMessage.setReplyMarkup(buttons.getKeyboardForUser(chatId, userData));
            e.printStackTrace();
        }

        return outMessage;
    }

    public SendMessage handleTrackSelection(String message, UserData userData, long chatId) throws IOException, InterruptedException {
        SendMessage outMessage = new SendMessage();
        outMessage.setChatId(String.valueOf(chatId));
        outMessage.setParseMode(ParseMode.HTML);

        if (message.equalsIgnoreCase("/back") || message.equalsIgnoreCase("назад")) {
            userData.changeUserState(chatId, UserState.WAITING_FOR_ACTIONS);
            outMessage.setText(Messages.BACK_MESSAGE);
            outMessage.setReplyMarkup(buttons.getKeyboardForUser(chatId, userData));
            return outMessage;
        }

        if(message.startsWith("track_")){
            outMessage.setText("Что вы хотите выбрать?");
            index = Character.getNumericValue(message.charAt(6));
            userData.changeUserState(chatId, UserState.WAITING_FOR_TRACK_ACTIONS);
            outMessage.setReplyMarkup(buttons.getKeyboardForUser(chatId, userData));
            return outMessage;
        }
        return outMessage;
    }
    public SendMessage handleSendText(UserData userData, long chatId) throws IOException {
        SendMessage outMessage = new SendMessage();
        outMessage.setChatId(String.valueOf(chatId));
        outMessage.setParseMode(ParseMode.HTML);
        GeniusParser geniusParser = new GeniusParser();
        userData.changeUserState(chatId, UserState.WAITING_FOR_ARTISTS);

        String lyrics = geniusParser.getCleanLyrics(userTrackList.get(chatId).get(index));
        outMessage.setText(lyrics);
        outMessage.setReplyMarkup(buttons.getKeyboardForUser(chatId, userData));
        return outMessage;
    }

    public SendMessage handleSendTrack(UserData userData, long chatId) throws Exception {
        SendMessage outMessage = new SendMessage();
        outMessage.setChatId(String.valueOf(chatId));
        outMessage.setParseMode(ParseMode.HTML);
        DeezerMusicLink deezerMusicLink = new DeezerMusicLink();
        userData.changeUserState(chatId, UserState.WAITING_FOR_ARTISTS);
        System.out.println(trackList.get(chatId).get(index));

        String link = deezerMusicLink.getDeezerMusicLink(trackList.get(chatId).get(index));
        outMessage.setText(link);
        outMessage.setReplyMarkup(buttons.getKeyboardForUser(chatId, userData));
        return outMessage;
    }

}