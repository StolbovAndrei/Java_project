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
    keyboardMarkup.setKeyboard(createKeyboardRows(currentState));

    return keyboardMarkup;
  }

  private static List<KeyboardRow> createKeyboardRows(UserState currentState) {
    List<KeyboardRow> keyboard = new ArrayList<>();

    if (currentState == null || currentState == UserState.WAITING_FOR_ACTIONS) {

      KeyboardRow row1 = new KeyboardRow();
      row1.add("/start");
      row1.add("/help");

      KeyboardRow row2 = new KeyboardRow();
      row2.add("/search");
      row2.add("/back");

      keyboard.add(row1);
      keyboard.add(row2);
    }
    else if (currentState == UserState.WAITING_FOR_ARTISTS) {

      KeyboardRow row = new KeyboardRow();
      row.add("/back");
      keyboard.add(row);
    }

    return keyboard;
  }
}