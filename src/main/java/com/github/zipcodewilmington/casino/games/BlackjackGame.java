package com.github.zipcodewilmington.casino.games;
import com.github.zipcodewilmington.Casino;
import com.github.zipcodewilmington.Deck;
import com.github.zipcodewilmington.casino.CasinoAccount;
import com.github.zipcodewilmington.casino.CasinoAccountManager;
import com.github.zipcodewilmington.casino.GameInterface;
import com.github.zipcodewilmington.casino.PlayerInterface;
import com.github.zipcodewilmington.casino.players.BlackjackPlayer;
import com.github.zipcodewilmington.casino.players.RoulettePlayer;
import com.github.zipcodewilmington.utils.AnsiColor;
import com.github.zipcodewilmington.utils.IOConsole;

import java.util.Scanner;

public class BlackjackGame implements GameInterface {
    int total;
    Scanner scanner = new Scanner(System.in);
    Deck BJDeck = new Deck(1, true);
    BlackjackPlayer firstPlayer;
    BlackjackPlayer secondPlayer;
    BlackjackPlayer thirdPlayer;
    BlackjackPlayer fourthPlayer;
    BlackjackPlayer dealer = new BlackjackPlayer("Dealer");
    private final IOConsole firstConsole = new IOConsole(AnsiColor.GREEN);
    private final IOConsole secondConsole = new IOConsole(AnsiColor.RED);
    private final IOConsole thirdConsole = new IOConsole(AnsiColor.BLUE);
    private final IOConsole fourthConsole = new IOConsole(AnsiColor.YELLOW);
    private final IOConsole dealerConsole = new IOConsole(AnsiColor.WHITE);
    String player2;
    String player3;
    String player4;
    boolean draw = false;
    boolean firstPlayerDone = false;
    boolean secondPlayerDone = false;
    boolean thirdPlayerDone = false;
    boolean fourthPlayerDone = false;
    boolean dealerDone = false;
    boolean toggle = false;
    CasinoAccount player;
    String response;
    public BlackjackGame() {
        player = CasinoAccountManager.casinoAccountList.get(0);
        add(firstPlayer);
        add(secondPlayer);
        add(thirdPlayer);
        add(fourthPlayer);
        run();
        Casino casino = new Casino();
        casino.run();
    }
    public BlackjackGame(String name) {

    }
    @Override
    public void add(PlayerInterface player) {
        firstPlayer = new BlackjackPlayer(CasinoAccountManager.casinoAccountList.get(0).getAccountName());
        secondPlayer = new BlackjackPlayer(null);
        thirdPlayer = new BlackjackPlayer(null);
        fourthPlayer = new BlackjackPlayer(null);
    }

    @Override
    public void remove(PlayerInterface player) {

    }

    @Override
    public void run() {
        total = dealerConsole.getIntegerInput("Welcome to BlackJack. Please enter bet amount: ");
        while (true) {
            this.player2 = secondConsole.getStringInput("Please enter player 2. Leave empty for 1 player game");
            secondPlayerName();
            this.player3 = thirdConsole.getStringInput("Please enter player 3. Leave empty for 2 player game");
            thirdPlayerName();
            this.player4 = fourthConsole.getStringInput("Please enter player 4. Leave empty for 3 player game");
            fourthPlayerName();
            break;
        }
        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < 1; j++) {
                firstPlayerDraw();
                dealer.draw(BJDeck.dealCard());
                secondPlayerDraw();
                thirdPlayerDraw();
                fourthPlayerDraw();
            }
        }
        handReveal();
        dealer.cardReveal(false);
        System.out.println("\nCards are dealt\n");

        while (!firstPlayerDone) {
            response = firstConsole.getStringInput(firstPlayer.getname() + ": Hit Me [H] or Stay [S]:");
            firstPersonHitOrStay(response);
        }
        while (!secondPlayerDone) {
            if (secondPlayer.getname() == null) {
                break;
            } else if (!secondPlayerDone) {
                response = secondConsole.getStringInput(secondPlayer.getname() + ": Hit Me [H] or Stay [S]:");
                secondPersonHitOrStay(response);
            }
        }
        while (!thirdPlayerDone) {
            if (thirdPlayer.getname() == null) {
                break;
            } else if (!thirdPlayerDone) {
                response = thirdConsole.getStringInput(thirdPlayer.getname() + ": Hit Me [H] or Stay [S]:");
                thirdPersonHitOrStay(response);
            }
        }
        while (!fourthPlayerDone) {
            if (fourthPlayer.getname() == null) {
                break;
            } else if (!fourthPlayerDone) {
                response = fourthConsole.getStringInput(fourthPlayer.getname() + ": Hit Me [H] or Stay [S]:");
                fourthPersonHitOrStay(response);
            }
        }
        while (!dealerDone) {
            dealerHitOrStay();
        }

        handReveal();
        dealer.cardReveal(true);
        System.out.printf("Dealer's total hand is: %s\n\n",dealer.getHandSum());
        System.out.print("\n");

        int playerOneSum = firstPlayer.getHandSum();
        int playerTwoSum = secondPlayer.getHandSum();
        int playerThreeSum = thirdPlayer.getHandSum();
        int playerFourSum = fourthPlayer.getHandSum();
        int dealerSum = dealer.getHandSum();

        if (playerOneSum > dealerSum &&
                playerOneSum > playerTwoSum &&
                playerOneSum > playerThreeSum &&
                playerOneSum > playerFourSum &&
                playerOneSum <= 21 || dealerSum > 21) {
            System.out.println(firstPlayer.getname() + " wins!");
            player.addAccountBalance(total);
            player.addSecurityLevel();
            player.kickedOutBySecurity();
        } else if (playerTwoSum > dealerSum &&
                playerTwoSum > playerOneSum &&
                playerTwoSum > playerThreeSum &&
                playerTwoSum > playerFourSum &&
                playerTwoSum <= 21 || dealerSum > 21) {
            System.out.println(secondPlayer.getname() + " + wins!");
            player.subtractAccountBalance(total);
        } else if (playerThreeSum > dealerSum &&
                playerThreeSum > playerOneSum &&
                playerThreeSum > playerTwoSum &&
                playerThreeSum > playerFourSum &&
                playerThreeSum <= 21 || dealerSum > 21) {
            System.out.println(thirdPlayer.getname() + " wins!");
            player.subtractAccountBalance(total);
        } else if (playerFourSum > dealerSum &&
                playerFourSum > playerOneSum &&
                playerFourSum > playerTwoSum &&
                playerFourSum > playerThreeSum &&
                playerFourSum <= 21 || dealerSum > 21) {
            System.out.println(fourthPlayer.getname() + " wins!");
            player.subtractAccountBalance(total);
        } else {
            System.out.println("Dealer wins!");
            player.subtractAccountBalance(total);
        }
        this.toggle = true;
    }
    public boolean firstPlayerDraw() {
        return firstPlayer.draw(BJDeck.dealCard());
    }
    public BlackjackPlayer secondPlayerName() {
        while(true) {
            if (player2 == "") {
                player2 = null;
                break;
            } else {
                return secondPlayer = new BlackjackPlayer(player2);
            }
        }
        return secondPlayer;
    }
    public BlackjackPlayer thirdPlayerName() {
        while(true) {
            if (player3 == "") {
                player3 = null;
                break;
            } else {
                return thirdPlayer = new BlackjackPlayer(player3);
            }
        }
        return thirdPlayer;
    }
    public BlackjackPlayer fourthPlayerName() {
        while(true) {
            if (player4 == "") {
                player4 = null;
                break;
            } else {
                return fourthPlayer = new BlackjackPlayer(player4);
            }
        }
        return fourthPlayer;
    }
    public boolean secondPlayerDraw() {
        while (true) {
            if (secondPlayer.getname() == null) {
                break;
            } else {
                return secondPlayer.draw((BJDeck.dealCard()));
            }
        }
        return draw;
    }
    public boolean thirdPlayerDraw() {
        while (true) {
            if (thirdPlayer.getname() == null) {
                break;
            } else {
                return thirdPlayer.draw((BJDeck.dealCard()));
            }
        }
        return draw;
    }
    public boolean fourthPlayerDraw() {
        while (true) {
            if (fourthPlayer.getname() == null) {
                break;
            } else {
                return fourthPlayer.draw((BJDeck.dealCard()));
            }
        }
        return draw;
    }
    public boolean firstPersonHitOrStay(String response) {
        if (response.compareToIgnoreCase("H") == 0) {
            firstPlayerDone = !firstPlayerDraw();
            firstPlayer.cardReveal(true);
            System.out.printf("Your total hand is: %s\n\n",firstPlayer.getHandSum());
        } else if (response.compareToIgnoreCase("S") == 0) {
            firstPlayerDone = true;
        }
        return firstPlayerDone;
    }
    public boolean secondPersonHitOrStay(String response) {
        if (response.compareToIgnoreCase("H") == 0) {
            secondPlayerDone = !secondPlayerDraw();
            secondPlayer.cardReveal(true);
            System.out.printf("Your total hand is: %s\n\n",secondPlayer.getHandSum());
        } else if (response.compareToIgnoreCase("S") == 0) {
            secondPlayerDone = true;
        }
        return secondPlayerDone;
    }
    public boolean thirdPersonHitOrStay(String response) {
        if (response.compareToIgnoreCase("H") == 0) {
            thirdPlayerDone = !thirdPlayerDraw();
            thirdPlayer.cardReveal(true);
            System.out.printf("Your total hand is: %s\n\n",thirdPlayer.getHandSum());
        } else if (response.compareToIgnoreCase("S") == 0) {
            thirdPlayerDone = true;
        }
        return thirdPlayerDone;
    }
    public boolean fourthPersonHitOrStay(String response) {
        if (response.compareToIgnoreCase("H") == 0) {
            fourthPlayerDone = !fourthPlayerDraw();
            fourthPlayer.cardReveal(true);
            System.out.printf("Your total hand is: %s\n\n",fourthPlayer.getHandSum());
        } else if (response.compareToIgnoreCase("S") == 0) {
            fourthPlayerDone = true;
        }
        return fourthPlayerDone;
    }
    public boolean dealerHitOrStay() {
        if (dealer.getHandSum() < 17) {
            System.out.println("Dealer hits\n");
            dealerDone = !dealer.draw(BJDeck.dealCard());
            dealer.cardReveal(false);
        } else {
            System.out.println("Dealer stays\n");
            dealerDone = true;
        }
        return dealerDone;
    }
    public void handReveal() {
        firstPlayer.cardReveal(true);
        System.out.print("\n");
        System.out.printf("Your total hand is: %s\n\n",firstPlayer.getHandSum());
        secondPlayer.cardReveal(true);
        System.out.print("\n");
        System.out.printf("Your total hand is: %s\n\n",secondPlayer.getHandSum());
        thirdPlayer.cardReveal(true);
        System.out.print("\n");
        System.out.printf("Your total hand is: %s\n\n",thirdPlayer.getHandSum());
        fourthPlayer.cardReveal(true);
        System.out.print("\n");
        System.out.printf("Your total hand is: %s\n\n",fourthPlayer.getHandSum());
    }
}


