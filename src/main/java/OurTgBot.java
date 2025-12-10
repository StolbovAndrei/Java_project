import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.AnswerCallbackQuery;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
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
    private final Buttons buttons = new Buttons();
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
        DeleteMessage deleteMessage = new DeleteMessage();
        if (update.hasMessage() && update.getMessage().hasText()) {
            handleMessage(update);

        } else if (update.hasCallbackQuery()) {
            handleCallbackQuery(update);
            long chatId = update.getCallbackQuery().getMessage().getChatId();
            int messageId = update.getCallbackQuery().getMessage().getMessageId();
            String text = update.getCallbackQuery().getData();
            if(!text.startsWith("/back")) {
                deleteMessage.setMessageId(messageId);
                deleteMessage.setChatId(chatId);
                try {
                    execute(deleteMessage);
                } catch (TelegramApiException e) {
                    throw new RuntimeException(e);
                }
            }
        }

    }

    private void handleMessage(Update update) {
        long chatId = update.getMessage().getChatId();
        SendMessage result = mainMenu.mainMenu(update, userData);
        result.setReplyMarkup(buttons.getKeyboardForUser(chatId, userData));
        try {
            execute(result);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    private void handleCallbackQuery(Update update) {
        CallbackQuery callbackQuery = update.getCallbackQuery();
        String callbackData = callbackQuery.getData();
        long chatId = callbackQuery.getMessage().getChatId();

        Update artificialUpdate = new Update();
        Message message = new Message();
        message.setChat(new Chat(chatId, "private"));
        message.setText(callbackData);
        artificialUpdate.setMessage(message);

        SendMessage result = mainMenu.mainMenu(artificialUpdate, userData);
        result.setReplyMarkup(buttons.getKeyboardForUser(chatId, userData));

        try {
            AnswerCallbackQuery answer = new AnswerCallbackQuery();
            answer.setCallbackQueryId(callbackQuery.getId());
            execute(answer);
            execute(result);


        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }
}