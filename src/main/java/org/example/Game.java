package org.example;

import java.util.ArrayList;
import java.util.List;

public class Game extends Cards {
    public boolean isStarted = false;
    public boolean gettingTus = false;
    public int dillerScore = 0;
    public List<String> dillerCards = new ArrayList<>();
    public int userScore = 0;
    public List<String> userCards = new ArrayList<>();
    public String userName = "user";
    public void Init(int n){
        GenerateDeck();
        setDecksCount(n);
    }
    public void Init(){
        GenerateDeck();
    }
    public String NewGame() {
        isStarted = true;
        userCards.clear();
        dillerCards.clear();
        userScore = 0;
        dillerScore = 0;
        List<String> cards = GetCards(4);
        userCards.add(cards.get(0));
        userCards.add(cards.get(1));
        dillerCards.add(cards.get(2));
        dillerCards.add(cards.get(3));
        String respText = GetUserCards();
        respText += "\nМоя карта: " + cards.get(2)+ "Помни, у меня есть еще одна закрытая карта)";
        int countTus = 0;
        for (int i = 0; i < 2; i++){
            int res = AllowCardToScore(cards.get(i));
            if (res == 0){
                gettingTus = true;
                countTus += 1;
                if (countTus == 2){
                    userScore += 1;
                } else {
                    respText += "\nТуз. 11 или 1 очков?";
                }
            } else {
                userScore += res;
            }
        }
        int score = AllowCardToScore(cards.get(2));
        if (score==0){
            dillerScore += 11;
        } else {
            dillerScore += score;
        }
        score = AllowCardToScore(cards.get(3));
        if (score==0){
            dillerScore += 1;
        } else {
            dillerScore += score;
        }
        return respText;
    }
    public List<String> GetCards(int n){
        List<String> res = deck.subList(0, n);
        deck = deck.subList(n, deck.size());
        return res;
    }
    public int AllowCardToScore(String card){
        String rank = card.split(" ")[0];
        if (rank.length() == 1){
            return Integer.parseInt(rank);
        } else if (!rank.equals("Туз")) {
            return 10;
        } else {
            return 0;
        }
    }

    public String GetUserCards() {
        String respText = "Твои карты:\n";
        for (String userCard : userCards) {
            respText += userCard;
        }
        return respText;
    }

    public String GetDillerCards() {
        String respText = "\nКарты диллера:\n";
        for (String userCard : dillerCards) {
            respText += userCard;
        }
        return respText;
    }

    public boolean isOver() {
        return userScore > 20 || dillerScore > 20;
    }

    public String CheckWinner(){
        System.out.println(userScore);
        System.out.println(dillerScore);
        System.out.println();
        if (userScore==21&&dillerScore>21){
            return "user";
        } else if (dillerScore==21&&userScore>21) {
            return "diller";
        } else if ((dillerScore>21&&userScore>21)||(dillerScore==userScore)) {
            return "draw";
        } else {
            if (dillerScore>userScore&&dillerScore<21){
                return "diller";
            }
            return "user";
        }
    }
    public String GetWinner(){
        String winner = CheckWinner();
        String respText = "\nИгра окончена. Результат: ";
        if (winner.equals("user")){
            respText += userName + " выиграл!";
        } else if (winner.equals("diller")) {
            respText += "диллер выиграл!";
        } else {
            respText += "ничья.";
        }
        this.isStarted = false;
        return respText;
    }
}
