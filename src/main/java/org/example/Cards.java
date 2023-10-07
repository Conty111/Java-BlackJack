package org.example;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Cards {
    private static final String[] suits = {
            " Пик\n", " Бубен\n", " Черв\n", " Треф\n"
    };
    private static final String[] ranks = {
            "2", "3", "4", "5", "6", "7", "8", "9", "10",
            "Валет", "Королева", "Король", "Туз"
    };
    private static int countDecks = 1;
    public static List<String> deck = new ArrayList<>();
    public void setDecksCount(int n){
        if (n < 1 || n > 8){
            countDecks = n;
        }
    }
    public void GenerateDeck(){
        for (int i = 0; i < countDecks; i++){
            for (String s: suits) {
                for (String rank: ranks){
                    deck.add(rank+s);
                }
            }
        }
        Collections.shuffle(deck);
    }
}
