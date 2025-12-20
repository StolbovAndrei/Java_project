import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.AnswerCallbackQuery;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import java.io.IOException;

public class OurTgBot extends TelegramLongPollingBot {
    private final UserData userData;
    private final ParserKeys parserKeys = new ParserKeys();
    private final Keys keys = parserKeys.getKeys();
    private final MainMenu mainMenu = new MainMenu();
    private final String botUsername = keys.getBotUsername();
    private final String botToken = keys.getBotToken();

    public OurTgBot(UserData userData) throws IOException {
        this.userData = userData;
    }

    @Override
    public String getBotUsername() {
        return botUsername;
    }

    @Override
    public String getBotToken() {
        return botToken;
    }

    @Override
    public void onUpdateReceived(Update update) {
        try {
            if (update.hasMessage() && update.getMessage().hasText()) {
                handleMessage(update);
            } else if (update.hasCallbackQuery()) {
                handleCallbackQuery(update);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void handleMessage(Update update) {

        try {
            SendMessage result = mainMenu.mainMenu(update, userData);
            execute(result);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void handleCallbackQuery(Update update) {
        CallbackQuery callbackQuery = update.getCallbackQuery();
        String callbackData = callbackQuery.getData();
        long chatId = callbackQuery.getMessage().getChatId();

        SendMessage response = new SendMessage();
        response.setChatId(String.valueOf(chatId));
        response.setParseMode("HTML");

        try {
            AnswerCallbackQuery answer = new AnswerCallbackQuery();
            answer.setCallbackQueryId(callbackQuery.getId());
            execute(answer);
            if (callbackData.equals("/help")) {
                Update artificialUpdate = createArtificialUpdate(chatId, "/help");
                response = mainMenu.mainMenu(artificialUpdate, userData);

            } else if (callbackData.equals("/search")) {
                userData.changeUserState(chatId, UserState.WAITING_FOR_ARTISTS);
                response.setText(Messages.SEARCH_MESSAGE);
                Buttons buttons = new Buttons();
                response.setReplyMarkup(buttons.getKeyboardForUser(chatId, userData));

            } else if (callbackData.equals("/back")) {
                userData.changeUserState(chatId, UserState.WAITING_FOR_ACTIONS);
                response.setText(Messages.BACK_MESSAGE);
                Buttons buttons = new Buttons();
                response.setReplyMarkup(buttons.getKeyboardForUser(chatId, userData));


            } else if (callbackData.startsWith("track_")){
                response = mainMenu.handleTrackSelection(callbackData, userData, chatId);

            } else if (callbackData.equals("/lyrics")){
                userData.changeUserState(chatId, UserState.WAITING_FOR_TEXT);
                response = mainMenu.handleSendText(userData, chatId);
            } else if (callbackData.equals("/link")){
                userData.changeUserState(chatId, UserState.WAITING_FOR_LINK);
                response = mainMenu.handleSendTrack(userData, chatId);
            }
            else {
                Update artificialUpdate = createArtificialUpdate(chatId, callbackData);
                response = mainMenu.mainMenu(artificialUpdate, userData);
            }

            execute(response);

        } catch (Exception e) {
            e.printStackTrace();
            try {
                response.setText("Произошла ошибка. Попробуйте еще раз.");
                execute(response);
            } catch (TelegramApiException ex) {
                ex.printStackTrace();
            }
        }
    }

    private Update createArtificialUpdate(long chatId, String text) {
        Update artificialUpdate = new Update();
        Message message = new Message();
        message.setChat(new Chat(chatId, "private"));
        message.setText(text);
        artificialUpdate.setMessage(message);
        return artificialUpdate;
    }
}