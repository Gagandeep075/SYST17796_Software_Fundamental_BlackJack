/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.mycompany.black_jack;

/**
 *
 * @author Osman
 * @author Gagandeep Singh
 * @author Anuvanshika sharma
 * @author Yuvam meelu
 */
import java.util.*;

public class Black_Jack {
    private List<Player> players;
    private Deck deck;
    private boolean gameOver;

    public Black_Jack(int numPlayers) {
        players = new ArrayList<>();
        deck = new Deck();
        gameOver = false;

        for (int i = 0; i < numPlayers; i++) {
            players.add(new Player("Player " + (i + 1)));
        }
    }

    public void registerPlayer(Player player) {
        players.add(player);
    }

    public void startGame() {
        deck.shuffle();
        dealInitialCards();

        for (Player player : players) {
            System.out.println(player.get_name() + "'s turn:");
            playPlayer_turn(player);
        }

        playDealer_turn();

        decide_Winners();
        gameOver = true;
    }

    private void dealInitialCards() {
        for (Player player : players) {
            player.receiveCard(deck.drawCard());
        }
        Card dealerCard = deck.drawCard();
        dealerCard.setFaceUp(false);
        players.get(0).receiveCard(dealerCard);
    }

    private void playPlayer_turn(Player player) {
        while (!player.isBust() && player.getScore() < 21) {
            System.out.println("Your hand: " + player.getHand());
            Scanner scanner = new Scanner(System.in);
            System.out.print("Do you want to hit or stand? (h/s): ");
            String choice = scanner.nextLine().toLowerCase();

            if (choice.equals("h")) {
                player.receiveCard(deck.drawCard());
            } else if (choice.equals("s")) {
                break;
            }
        }
    }

    private void playDealer_turn() {
        Player dealer = players.get(0);

        while (!dealer.isBust() && dealer.getScore() < 17) {
            dealer.receiveCard(deck.drawCard());
        }

        dealer.getHand().get(0).setFaceUp(true);
        System.out.println("Dealer's hand: " + dealer.getHand());
    }

    private void decide_Winners() {
        Player dealer = players.get(0);
        int dealerScore = dealer.getScore();
        boolean dealerBust = dealer.isBust();

        for (int i = 1; i < players.size(); i++) {
            Player player = players.get(i);
            int playerScore = player.getScore();
            boolean playerBust = player.isBust();

            if (playerBust) {
                System.out.println(player.get_name() + " is bust. Dealer wins!");
                player.setGameResult(GameResult.LOSS);
            } else if (dealerBust) {
                System.out.println("Dealer is bust. " + player.get_name() + " wins!");
                player.setGameResult(GameResult.WIN);
            } else if (playerScore > dealerScore) {
                System.out.println(player.get_name() + " wins!");
                player.setGameResult(GameResult.WIN);
            } else if (playerScore < dealerScore) {
                System.out.println(player.get_name() + " loses. Dealer wins!");
                player.setGameResult(GameResult.LOSS);
            } else {
                System.out.println(player.get_name() + " and Dealer have a tie!");
                player.setGameResult(GameResult.TIE);
            }
        }
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter the number of players: ");
        int numPlayers = scanner.nextInt();

        Black_Jack game = new Black_Jack(numPlayers);
        game.startGame();
    }
}

class Card {
    private final String rank;
    private final String suit;
    private boolean faceUp;

    public Card(String rank, String suit) {
        this.rank = rank;
        this.suit = suit;
        faceUp = true;
    }

    public String getRank() {
        return rank;
    }

    public String getSuit() {
        return suit;
    }

    public int getValue() {
        if (rank.equals("Ace")) {
            return 11;
        } else if (rank.equals("King") || rank.equals("Queen") || rank.equals("Jack")) {
            return 10;
        } else {
            return Integer.parseInt(rank);
        }
    }

    public boolean isFaceUp() {
        return faceUp;
    }

    public void setFaceUp(boolean faceUp) {
        this.faceUp = faceUp;
    }

    @Override
    public String toString() {
        if (faceUp) {
            return rank + " of " + suit;
        } else {
            return "Face Down";
        }
    }
}

class Deck {
    private final List<Card> cards;

    public Deck() {
        cards = new ArrayList<>();

        String[] ranks = { "Ace", "2", "3", "4", "5", "6", "7", "8", "9", "10", "Jack", "Queen", "King" };
        String[] suits = { "Spades", "Hearts", "Diamonds", "Clubs" };

        for (String rank : ranks) {
            for (String suit : suits) {
                cards.add(new Card(rank, suit));
            }
        }
    }

    public void shuffle() {
        Collections.shuffle(cards);
    }

    public Card drawCard() {
        if (!cards.isEmpty()) {
            return cards.remove(0);
        }
        return null;
    }
}

class Player {
    private final String name;
    private final List<Card> hand;
    private GameResult gameResult;

    public Player(String name) {
        this.name = name;
        hand = new ArrayList<>();
        gameResult = null;
    }

    public String get_name() {
        return name;
    }

    public void receiveCard(Card card) {
        hand.add(card);
    }

    public List<Card> getHand() {
        return hand;
    }

    public int getScore() {
        int score = 0;
        int numAces = 0;

        for (Card card : hand) {
            score += card.getValue();

            if (card.getRank().equals("Ace")) {
                numAces++;
            }
        }

        while (score > 21 && numAces > 0) {
            score -= 10;
            numAces--;
        }

        return score;
    }

    public boolean isBust() {
        return getScore() > 21;
    }

    public void setGameResult(GameResult result) {
        this.gameResult = result;
    }

    public GameResult getGameResult() {
        return gameResult;
    }
}

enum GameResult {
    WIN,
    LOSS,
    TIE
}
