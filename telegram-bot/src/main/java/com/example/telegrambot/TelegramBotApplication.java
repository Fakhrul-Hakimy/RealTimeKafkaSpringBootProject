package com.example.telegrambot;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

import com.example.telegrambot.service.YouTubeTelegramBot;

@SpringBootApplication
public class TelegramBotApplication {

	public static void main(String[] args) {
		SpringApplication.run(TelegramBotApplication.class, args);
	}

	@Bean
	public TelegramBotsApi telegramBotsApi() throws TelegramApiException {
		return new TelegramBotsApi(DefaultBotSession.class);
	}

	@Bean
	public YouTubeTelegramBot youTubeTelegramBot(
			@Value("${telegram.bot.token}") String botToken,
			@Value("${telegram.bot.username}") String botUsername,
			TelegramBotsApi telegramBotsApi) {
		YouTubeTelegramBot bot = new YouTubeTelegramBot(botToken, botUsername);
		try {
			telegramBotsApi.registerBot(bot);
		} catch (TelegramApiException e) {
			e.printStackTrace();
		}
		return bot;
	}

}
