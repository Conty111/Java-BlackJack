package org.example;

import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.HashMap;

public class Bot extends TelegramLongPollingBot {
    public HashMap<String, Game> Games = new HashMap<>(5);

    public Config config;
    @Override
    public String getBotUsername() {
        return System.getenv("TG_BOT_NAME");
    }
    @Override
    public String getBotToken() {
        return System.getenv("TG_TOKEN");
    }
    @Override
    public void onUpdateReceived(Update update) {
        if(update.hasMessage()){
            Message readMsg = update.getMessage();
            String chatId = readMsg.getChatId().toString();
            String text = readMsg.getText();

            SendMessage sendMsg = getSendMessage(text, chatId, readMsg.getChat().getFirstName());

            try {
                execute(sendMsg);
            } catch (TelegramApiException e) {
                throw new RuntimeException(e);
            }
        }
    }
    private SendMessage getSendMessage(String text, String chatId, String userName) {
        String respText = "";
        Game BlackJack = Games.get(chatId);
        if (text.equals("/start")){
            Games.put(chatId, new Game());
            Games.get(chatId).Init(config.DefaultCountDecks);
            Games.get(chatId).userName = userName;
            respText = config.helpText;
            respText += Games.get(chatId).NewGame();
        } else if (text.equals("/restart")) {
            if (!BlackJack.isStarted){
                respText = BlackJack.NewGame();
            } else {
                respText = "Доиграй сначала эту игру.";
            }
        } else if (text.equals("/help")) {
            respText = config.helpText;
        } else if (text.equals("/rules")) {
            respText = config.rulesText;
        } else if (BlackJack.isStarted) {
            if ((text.equals("11")||text.equals("1"))&&BlackJack.gettingTus) {
                BlackJack.gettingTus = false;
                BlackJack.userScore += Integer.parseInt(text);
                respText = BlackJack.GetUserCards();
                if (BlackJack.isOver()) {
                    respText += BlackJack.GetWinner();
                }
            } else if (isContinue(text)) {
                String card = BlackJack.GetCards(1).get(0);
                BlackJack.userCards.add(card);
                respText = BlackJack.GetUserCards();
                int res = BlackJack.AllowCardToScore(card);
                if (res==0&&BlackJack.userScore+11<22) {
                    respText += "\nТуз. 11 или 1 очков?";
                    BlackJack.gettingTus = true;
                } else if (res==0) {
                    BlackJack.userScore += 1;
                } else {
                    BlackJack.userScore += res;
                    if (BlackJack.isOver()){
                        respText += BlackJack.GetDillerCards();
                        respText += BlackJack.GetWinner();
                    }
                }
            } else if (isDillerGo(text)) {
                while (BlackJack.dillerScore<17) {
                    BlackJack.dillerCards.add(BlackJack.GetCards(1).get(0));
                    int score = BlackJack.AllowCardToScore(BlackJack.dillerCards.get(BlackJack.dillerCards.size() - 1));
                    if (score == 0) {
                        if (BlackJack.dillerScore + 11 <= 21) {
                            BlackJack.dillerScore += 11;
                        } else {
                            BlackJack.dillerScore += 1;
                        }
                    } else {
                        BlackJack.dillerScore += score;
                    }
                }
                respText = BlackJack.GetUserCards();
                respText += BlackJack.GetDillerCards();
                respText += "\n" + BlackJack.GetWinner();

            } else {
                respText = "Не понимаю о чем ты. Играй нормально";
            }
        } else {
            respText = "Ты давай по делу пиши, человечина";
        }
        SendMessage sendMsg = new SendMessage();
        sendMsg.setText(respText);
        sendMsg.setChatId(chatId);
        return sendMsg;
    }
    public boolean isContinue(String txt){
        txt = txt.toLowerCase();
        return txt.contains("еще")||txt.contains("ещё")||txt.contains("дай");
    }
    public boolean isDillerGo(String txt){
        txt = txt.toLowerCase();
        return txt.contains("стоп")||txt.contains("stop")||txt.contains("хватит");
    }
}