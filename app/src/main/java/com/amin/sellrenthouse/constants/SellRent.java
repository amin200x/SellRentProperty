package com.amin.sellrenthouse.constants;

public enum SellRent {
    SELL ("فروش", 1),
    RENT ("اجاره", 2);

    private final String name;
    private final int index;
    SellRent(String name, int index){
        this.name = name;
        this.index = index;
    }

    public String getName() {
        return name;
    }

    public int getIndex() {
        return index;
    }

    @Override
    public String toString() {
        return name;
    }
}
