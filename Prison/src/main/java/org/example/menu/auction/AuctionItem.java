package org.example.menu.auction;

import org.bukkit.inventory.ItemStack;

import java.util.UUID;

public class AuctionItem {
    private final ItemStack item;
    private final UUID seller;
    private final double startingPrice;
    private double currentPrice;
    private String highestBidder;

    public AuctionItem(ItemStack item, UUID seller, double startingPrice) {
        this.item = item;
        this.seller = seller;
        this.startingPrice = startingPrice;
        this.currentPrice = startingPrice;
        this.highestBidder = null;
    }

    public ItemStack getItem() {
        return item;
    }

    public UUID getSeller() {
        return seller;
    }

    public double getStartingPrice() {
        return startingPrice;
    }

    public double getCurrentPrice() {
        return currentPrice;
    }

    public void setCurrentPrice(double currentPrice) {
        this.currentPrice = currentPrice;
    }

    public String getHighestBidder() {
        return highestBidder;
    }

    public void setHighestBidder(String highestBidder) {
        this.highestBidder = highestBidder;
    }
}
