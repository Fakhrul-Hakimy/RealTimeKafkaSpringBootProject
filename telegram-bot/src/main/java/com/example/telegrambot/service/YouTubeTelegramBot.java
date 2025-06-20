package com.example.telegrambot.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.client.RestTemplate;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.ParseMode;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

public class YouTubeTelegramBot extends TelegramLongPollingBot {
    private static final Logger logger = LoggerFactory.getLogger(YouTubeTelegramBot.class);
    private final String botUsername;
    private static final String PRODUCER_API_BASE = "http://spring-producer:8080/api/chatid/";
    private final RestTemplate restTemplate = new RestTemplate();

    public YouTubeTelegramBot(String botToken, String botUsername) {
        super(botToken);
        this.botUsername = botUsername;
    }

    @Override
    public String getBotUsername() {
        return botUsername;
    }

    @Override
    public void onUpdateReceived(Update update) {
        Message message = update.getMessage();
        String text = message.getText().trim().toLowerCase();
        Long chatId = message.getChatId();
        if (text.equals("/start")) {
            try {
                restTemplate.postForEntity(PRODUCER_API_BASE + "save", chatId.toString(), String.class);
            } catch (Exception e) {
                logger.error("Failed to save or exist chatId: {}", chatId, e);
            }
            sendMessage(
                    "Welcome to YouTube Analytics Bot!\nYou will receive notifications about videos with odd-length comments\nThis is your chatID: "
                            + chatId,
                    chatId);
        } else if (text.equals("/subscribe")) {
            try {
                restTemplate.postForEntity(PRODUCER_API_BASE + "subscribe", chatId.toString(), String.class);
                sendMessage("You are now subscribed to YouTube updates!", chatId);
            } catch (Exception e) {
                logger.error("Failed to subscribe chatId: {}", chatId, e);
                sendMessage("Subscription failed. Please try /start first.", chatId);
            }
        } else if (text.equals("/stop")) {
            try {
                restTemplate.postForEntity(PRODUCER_API_BASE + "stop", chatId.toString(), String.class);
                sendMessage("You have stopped YouTube updates.\nSend /subscribe to start again.", chatId);
            } catch (Exception e) {
                logger.error("Failed to stop chatId: {}", chatId, e);
                sendMessage("Failed to stop notifications. Please try again.", chatId);
            }
        }
    }

    public void sendMessage(String message, Long chatId) {
        SendMessage sendMessage = SendMessage.builder()
                .parseMode(ParseMode.HTML)
                .chatId(chatId.toString())
                .text(message).build();
        try {
            execute(sendMessage);
            logger.info("Message sent successfully to chatId {}: {}", chatId, message);
        } catch (TelegramApiException e) {
            logger.error("Error sending message to Telegram: ", e);
        }
    }

    public void sendMessage(String message) {
        try {
            String[] chatIds = restTemplate.getForObject(PRODUCER_API_BASE + "subscribed", String[].class);
            if (chatIds != null) {
                for (String chatId : chatIds) {
                    sendMessage(message, Long.valueOf(chatId));
                }
            }
        } catch (Exception e) {
            logger.error("Error sending message to Telegram: ", e);
        }
    }

    public void sendVideoUpdate(String channelName, String videoUrl, Long commentCount) {
        String message = String.format("""
                🎥 New Video Update!
                Channel: %s
                Comments: %d
                URL: %s
                """, channelName, commentCount, videoUrl);

        sendMessage(message);
    }
}