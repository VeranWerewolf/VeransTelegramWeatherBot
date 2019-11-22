import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.methods.send.SendSticker;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.ArrayList;
import java.util.List;

public class Bot extends TelegramLongPollingBot {

    public void onUpdateReceived(Update update) {
        Thread thread = new Thread(() -> threadUpdateReceived(update));
        thread.run();
    }

    public void threadUpdateReceived(Update update) {
        BotMessageController botMessageController = new BotMessageController(this);
        System.out.println(update.getMessage());
        Message message = update.getMessage();
        if (message != null && message.hasText()) {
            botMessageController.MessageParser(message);
        }
        if (message != null && message.hasLocation())
        {
            botMessageController.locationMessage(message);
        }
        sendTextMessage(message, botMessageController.getTextMessage());
    }

    public void sendTextMessage(Message message, String text) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.enableMarkdown(true);
        sendMessage.setChatId(message.getChatId().toString());
        /** Включить прикрепление оригинального сообщения к ответу*/
        //sendMessage.setReplyToMessageId((message.getMessageId()));
        sendMessage.setText(text);
        try {
            execute(sendMessage);
            //setButtons(sendMessage);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    public void sendTextMessage(Message message, String text, String photo) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.enableMarkdown(true);
        sendMessage.setChatId(message.getChatId().toString());
        /** Включить прикрепление оригинального сообщения к ответу*/
        //sendMessage.setReplyToMessageId((message.getMessageId()));
        sendMessage.setText(text);
        try {
            execute(sendMessage);
            //setButtons(sendMessage);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
        SendPhoto sendPhoto = new SendPhoto();
        sendPhoto.setChatId(message.getChatId().toString());
        sendPhoto.setPhoto(photo);
        try {
            execute(sendPhoto);
            //setButtons(sendMessage);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    public void sendWrapper(String chatID, String text, String photo) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.enableMarkdown(true);
        sendMessage.setChatId(chatID);
        /** Включить прикрепление оригинального сообщения к ответу*/
        //sendMessage.setReplyToMessageId((message.getMessageId()));
        sendMessage.setText(text);
        try {
            execute(sendMessage);
            //setButtons(sendMessage);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
        SendPhoto sendPhoto = new SendPhoto();
        sendPhoto.setChatId(chatID);
        sendPhoto.setPhoto(photo);
        try {
            execute(sendPhoto);
            //setButtons(sendMessage);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    public void sendSticker(Message message, String stickerString) {
        SendSticker sticker = new SendSticker();
        sticker.setChatId(message.getChatId().toString());
        sticker.setSticker(stickerString);
        try {
            execute(sticker);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    public void setButtons(SendMessage sendMessage) {
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        sendMessage.setReplyMarkup(replyKeyboardMarkup);
        replyKeyboardMarkup.setSelective(true);
        replyKeyboardMarkup.setResizeKeyboard(true);
        replyKeyboardMarkup.setOneTimeKeyboard(false);

        List<KeyboardRow> keyboardRowList = new ArrayList<>();
        KeyboardRow keyboardFirstRow = new KeyboardRow();

        keyboardFirstRow.add(new KeyboardButton("/help"));
        keyboardFirstRow.add(new KeyboardButton("/settings"));

        keyboardRowList.add(keyboardFirstRow);
        replyKeyboardMarkup.setKeyboard(keyboardRowList);
    }

    public String getBotUsername() {
        return "VeransJavaWeather_Bot";
    }

    @Override
    public String getBotToken() {
        return "BotTokenHere";
    }


}

