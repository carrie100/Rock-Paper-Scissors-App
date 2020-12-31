package com.example.rockpaperscissors.classes;

import com.example.rockpaperscissors.enums.ItemRPS;
import com.example.rockpaperscissors.enums.WinType;
import com.google.gson.Gson;

public class GameRPS {
    private RoundRPS[] roundsArray;
    private int currentRound;

    public static int DEFAULT_ROUNDS_IN_GAME = 3;

    public GameRPS() {
        this(DEFAULT_ROUNDS_IN_GAME);
    }

    public GameRPS(int numberOfRoundsInGame) {
        InitializeAndFillArrayElements(numberOfRoundsInGame);
        startNewGame();
    }

    private void InitializeAndFillArrayElements(int numberOfRoundsInGame) {
        roundsArray = new RoundRPS[numberOfRoundsInGame];
        for (int i = 0; i < roundsArray.length; i++) {
            roundsArray[i] = new RoundRPS();
        }
    }

    public void startNewGame() {
        currentRound = 0;
        for (RoundRPS round : roundsArray) {
            round.setPlayerItem(null);
            round.setComputerItem(null);
        }
    }

    public boolean isGameOver() {
        RoundRPS lastRound = roundsArray[roundsArray.length - 1];
        return lastRound != null &&
                lastRound.getComputerItem() != null & lastRound.getPlayerItem() != null
                && (!lastRound.getComputerItem().equals(lastRound.getPlayerItem()));
    }

    public RoundRPS getRoundNumber(int roundNumber) {
        return new RoundRPS(roundsArray[roundNumber - 1]);
    }

    public RoundRPS getCurrentRound() {
        return (getRoundNumber(currentRound));
    }

    public int getNumberOfRoundsInGame() {
        return roundsArray.length;
    }

    public int getCurrentRoundNumber() {
        return currentRound;
    }

    public void advanceToAndSetNextRound(RoundRPS round) {
        advanceToAndSetNextRound(round.getComputerItem(), round.getPlayerItem());
    }

    public void advanceToAndSetNextRound(ItemRPS computerItem, ItemRPS playerItem) {
        // advance round only if at initial start or if current round not a tie
        if (currentRound == 0)
            currentRound++;
        else {
            RoundRPS currentRound = roundsArray[this.currentRound - 1];
            boolean isCurrentRoundTie =
                    currentRound.getComputerItem().equals(currentRound.getPlayerItem());
            this.currentRound = isCurrentRoundTie ? this.currentRound : this.currentRound + 1;
        }

        roundsArray[currentRound - 1].setComputerItem(computerItem);
        roundsArray[currentRound - 1].setPlayerItem(playerItem);

    }

    public ItemRPS getCurrentRoundComputerItem() {
        return currentRound == 0 ? null : roundsArray[currentRound - 1].getComputerItem();
    }

    public ItemRPS getCurrentRoundPlayerItem() {
        return currentRound == 0 ? null : roundsArray[currentRound - 1].getPlayerItem();
    }

    public String toJSONString() {
        return GameRPS.toJSONString(this);
    }

    public static String toJSONString(GameRPS gameRPS) {
        Gson gson = new Gson();
        return gson.toJson(gameRPS);
    }

    public static GameRPS fromJSONString(String JSON) {
        Gson gson = new Gson();
        return gson.fromJson(JSON, GameRPS.class);
    }

    public WinType getWinner() {
        if (isGameOver()) {

            int computerWins = 0;
            int playerWins = 0;
            for (RoundRPS round : roundsArray) {
                switch (round.getRoundWinType()) {
                    case COMPUTER:
                        computerWins++;
                        break;
                    case PLAYER:
                        playerWins++;
                }
            }
            if (computerWins > playerWins)
                return WinType.COMPUTER;
            else if (playerWins > computerWins)
                return WinType.PLAYER;
            else
                return WinType.TIE;
        } else {
            return null;
        }
    }
}
