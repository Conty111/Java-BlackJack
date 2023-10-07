package org.example;

import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

public class Main {
    public static void main(String[] args) {
        Bot b = new Bot();
        b.config = new Config();
        try {
            TelegramBotsApi t = new TelegramBotsApi(DefaultBotSession.class);
            t.registerBot(b);
            System.out.println("TG Bot has already started");
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }
}