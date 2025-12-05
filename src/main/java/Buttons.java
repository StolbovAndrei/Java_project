import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.util.ArrayList;
import java.util.List;


public class Buttons {
  public static ReplyKeyboardMarkup getKeyboardForUser(long chatId) {
    ReplyKeyboardMarkup keyboardMarkup = new ReplyKeyboardMarkup();
    keyboardMarkup.setResizeKeyboard(true);
    keyboardMarkup.setOneTimeKeyboard(false);

    UserState currentState = UserData.checkUserState(chatId);
    boolean isNewUser = !UserData.checkUser(chatId);

    keyboardMarkup.setKeyboard(createKeyboardRows(currentState, isNewUser));

    return keyboardMarkup;
  }

  private static List<KeyboardRow> createKeyboardRows(UserState currentState, boolean isNewUser) {
    List<KeyboardRow> keyboard = new ArrayList<>();

    if (isNewUser) {
      KeyboardRow row = new KeyboardRow();
      row.add("/start");
      keyboard.add(row);
    }

    else if (currentState == null || currentState == UserState.WAITING_FOR_ACTIONS) {
      KeyboardRow row = new KeyboardRow();
      row.add("/help");
      row.add("/search");
      keyboard.add(row);
    }

    else if (currentState == UserState.WAITING_FOR_ARTISTS) {
      KeyboardRow row = new KeyboardRow();
      row.add("/back");
      keyboard.add(row);
    }

    return keyboard;
  }
}