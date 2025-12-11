import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import java.util.ArrayList;
import java.util.List;


public class Buttons {

    public InlineKeyboardMarkup getKeyboardForUser(long chatId, UserData userData) {
        InlineKeyboardMarkup keyboardMarkup = new InlineKeyboardMarkup();

        UserState currentState = userData.checkUserState(chatId);

        keyboardMarkup.setKeyboard(createKeyboardRows(currentState));

        return keyboardMarkup;
    }

    private List<List<InlineKeyboardButton>> createKeyboardRows(UserState currentState) {
        List<List<InlineKeyboardButton>> keyboard = new ArrayList<>();
        List<InlineKeyboardButton> buttons = new ArrayList<>();
        InlineKeyboardButton buttonHelp = new InlineKeyboardButton();
        InlineKeyboardButton buttonSearch = new InlineKeyboardButton();
        InlineKeyboardButton buttonBack = new InlineKeyboardButton();

        if (currentState == null || currentState == UserState.WAITING_FOR_ACTIONS) {

            buttonHelp.setText("Помощь");
            buttonHelp.setCallbackData("/help");

            buttonSearch.setText("Поиск");
            buttonSearch.setCallbackData("/search");

            buttons.add(buttonHelp);
            buttons.add(buttonSearch);
        }
        else if (currentState == UserState.WAITING_FOR_ARTISTS) {
            buttonBack.setText("Назад");
            buttonBack.setCallbackData("/back");


            buttons.add(buttonBack);
        }
        keyboard.add(buttons);
        return keyboard;
    }
}