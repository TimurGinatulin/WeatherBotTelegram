import org.telegram.telegrambots.ApiContextInitializer;
import org.telegram.telegrambots.TelegramBotsApi;
import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.Message;
import org.telegram.telegrambots.api.objects.Update;
import org.telegram.telegrambots.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.exceptions.TelegramApiException;
import org.telegram.telegrambots.exceptions.TelegramApiRequestException;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Bot extends TelegramLongPollingBot {
    public static void main(String[] args) {
        ApiContextInitializer.init();
        TelegramBotsApi telegramBotsApi = new TelegramBotsApi();
        try {
            telegramBotsApi.registerBot(new Bot());
        } catch (TelegramApiRequestException exp) {
            exp.printStackTrace();
        }
    }

    public void setButton(SendMessage sendMessage) {
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        sendMessage.setReplyMarkup(replyKeyboardMarkup);
        replyKeyboardMarkup.setSelective(true);
        replyKeyboardMarkup.setResizeKeyboard(true);
        replyKeyboardMarkup.setOneTimeKeyboard(false);

        List<KeyboardRow> keyboardRowList = new ArrayList<>();
        KeyboardRow keyboardButtons = new KeyboardRow();
        keyboardButtons.add(new KeyboardButton("/start"));
        keyboardButtons.add(new KeyboardButton("/help"));
        keyboardButtons.add(new KeyboardButton("/setting"));
        keyboardRowList.add(keyboardButtons);
        replyKeyboardMarkup.setKeyboard(keyboardRowList);

    }

    @Override
    public void onUpdateReceived(Update update) {
        Model model = new Model();
        Message message = update.getMessage();
        if (message != null && message.hasText()) {
            switch (message.getText()) {
                case "/help":{
                    sendMsg(message, "Nobody can't help you.");
                    break;
                }
                case "/start": {
                    sendMsg(message, " Enter City name. Massage save in personal server");
                    break;
                }
                case "/setting": {
                    sendMsg(message, " Bot Answer setting");
                    break;
                }
                default: {

                    try (FileWriter fileWriter = new FileWriter("msg.txt",true)){
                        fileWriter.write(message.getChat().getUserName()+" "+message.getChat().getFirstName()+
                                " "+ message.getChat().getLastName()+" "+message.getChat().getId()+ "\n");
                        fileWriter.write(message.getDate()+"\n");
                        fileWriter.write(message.getText()+"\n");
                        fileWriter.write(message.getLocation()+"\n");

                        fileWriter.write("\n");
                    }catch (IOException exp){
                        exp.printStackTrace();
                    }

                    try {
                        sendMsg(message, Weather.getWeather(message.getText(), model));
                    } catch (IOException exception) {
                        sendMsg(message, "Town not find.");
                    }
                }
            }
        }

    }

    private void sendMsg(Message message, String s) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.enableMarkdown(true);
        sendMessage.setChatId(message.getChatId());
        sendMessage.setReplyToMessageId(message.getMessageId());
        sendMessage.setText(s);
        try {
            setButton(sendMessage);
            sendMessage(sendMessage);
        } catch (TelegramApiException exception) {
            exception.printStackTrace();
        }
    }

    @Override
    public String getBotUsername() {
        return "Timur_Ginatulin_bot";
    }

    @Override
    public String getBotToken() {
        return "1578371611:AAFlIY8ixiDnr1XShLOoNylLKt4JHhPRYbU";
    }
}
