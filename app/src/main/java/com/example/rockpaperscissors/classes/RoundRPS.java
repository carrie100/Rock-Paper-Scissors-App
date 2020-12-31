package com.example.rockpaperscissors.classes;

import androidx.annotation.NonNull;

import com.example.rockpaperscissors.enums.ItemRPS;
import com.example.rockpaperscissors.enums.WinType;

public class RoundRPS {

    private ItemRPS computerItem, playerItem;

    public RoundRPS(ItemRPS computerItem, ItemRPS playerItem) {
        this.computerItem = computerItem;
        this.playerItem = playerItem;
    }

    public RoundRPS(RoundRPS roundRPS) {
        this(roundRPS.computerItem, roundRPS.playerItem);
    }

    public RoundRPS() {
        this(null, null);
    }

    public ItemRPS getComputerItem() {
        return computerItem;
    }

    public void setComputerItem(ItemRPS computerItem) {
        this.computerItem = computerItem;
    }

    public ItemRPS getPlayerItem() {
        return playerItem;
    }

    public void setPlayerItem(ItemRPS playerItem) {
        this.playerItem = playerItem;
    }

    public WinType getRoundWinType() {
        return RoundRPS.getRoundWinType(computerItem, playerItem);
    }

    @Override
    @NonNull
    public String toString() {
        return "RPS_Round{" +
                "computerItem=" + computerItem +
                ", playerItem=" + playerItem +
                '}';
    }

    public static WinType getRoundWinType(ItemRPS computerItem, ItemRPS playerItem) {
        if (computerItem == null || playerItem == null)
            return null;
        else if (playerItem.equals(computerItem)) {
            return WinType.TIE;
        } else
            return (computerItem == ItemRPS.ROCK && playerItem == ItemRPS.SCISSORS) ||
                    (computerItem == ItemRPS.PAPER && playerItem == ItemRPS.ROCK) ||
                    (computerItem == ItemRPS.SCISSORS && playerItem == ItemRPS.PAPER)
                    ? WinType.COMPUTER
                    : WinType.PLAYER;
    }
}
