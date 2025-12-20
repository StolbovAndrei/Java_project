import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Buttons {

    public InlineKeyboardMarkup getKeyboardForUser(long chatId, UserData userData) {
        InlineKeyboardMarkup keyboardMarkup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> keyboard = new ArrayList<>();

        UserState currentState = userData.checkUserState(chatId);

        if (currentState == null || currentState == UserState.WAITING_FOR_ACTIONS) {
            List<InlineKeyboardButton> row = new ArrayList<>();

            InlineKeyboardButton buttonHelp = new InlineKeyboardButton();
            buttonHelp.setText("Помощь");
            buttonHelp.setCallbackData("/help");

            InlineKeyboardButton buttonSearch = new InlineKeyboardButton();
            buttonSearch.setText("Поиск");
            buttonSearch.setCallbackData("/search");

            row.add(buttonHelp);
            row.add(buttonSearch);
            keyboard.add(row);

        } else if (currentState == UserState.WAITING_FOR_ARTISTS) {
            List<InlineKeyboardButton> row = new ArrayList<>();

            InlineKeyboardButton buttonBack = new InlineKeyboardButton();
            buttonBack.setText("Назад");
            buttonBack.setCallbackData("/back");

            row.add(buttonBack);
            keyboard.add(row);
        } else if(currentState == UserState.WAITING_FOR_TRACK_ACTIONS){
            List<InlineKeyboardButton> rowHigh = new ArrayList<>();

            InlineKeyboardButton buttonLyrics = new InlineKeyboardButton();
            buttonLyrics.setText("Текст");
            buttonLyrics.setCallbackData("/lyrics");

            InlineKeyboardButton buttonLink = new InlineKeyboardButton();
            buttonLink.setText("Ссылка на трек на DeezerMusic");
            buttonLink.setCallbackData("/link");
            List<InlineKeyboardButton> rowLow = new ArrayList<>();
            InlineKeyboardButton buttonBack = new InlineKeyboardButton();
            buttonBack.setText("Назад");
            buttonBack.setCallbackData("/back");

            rowHigh.add(buttonLyrics);
            rowHigh.add(buttonLink);
            rowLow.add(buttonBack);
            keyboard.add(rowHigh);
            keyboard.add(rowLow);
        } else if(currentState == UserState.WAITING_FOR_TEXT){
            List<InlineKeyboardButton> row = new ArrayList<>();

            InlineKeyboardButton buttonBack = new InlineKeyboardButton();
            buttonBack.setText("Назад");
            buttonBack.setCallbackData("/back");

            row.add(buttonBack);
            keyboard.add(row);
        }

        keyboardMarkup.setKeyboard(keyboard);
        return keyboardMarkup;
    }

    public List<List<InlineKeyboardButton>> formatResult(Map<String, List<String>> songs) {
        List<List<InlineKeyboardButton>> keyboardRows = new ArrayList<>();

        if (songs == null || songs.isEmpty()) {
            return keyboardRows;
        }
        int trackIndex = 0;
        for (Map.Entry<String, List<String>> entry : songs.entrySet()) {
            for (String songTitle : entry.getValue()) {
                List<InlineKeyboardButton> row = new ArrayList<>();
                InlineKeyboardButton button = new InlineKeyboardButton();

                String buttonText = songTitle + " - " + entry.getKey();
                button.setText(buttonText);
                String CallbackData = "track_" + trackIndex;
                button.setCallbackData(CallbackData);

                row.add(button);
                keyboardRows.add(row);
                trackIndex++;
            }
        }
        List<InlineKeyboardButton> backRow = new ArrayList<>();
        InlineKeyboardButton backButton = new InlineKeyboardButton();
        backButton.setText("Назад");
        backButton.setCallbackData("/back");
        backRow.add(backButton);
        keyboardRows.add(backRow);

        return keyboardRows;
    }
}