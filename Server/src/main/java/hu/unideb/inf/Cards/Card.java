package hu.unideb.inf.Cards;

import java.io.Serializable;

public class Card implements Serializable {
    private String name;
    private String number;


    public Card(){};


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    @Override
    public String toString() {
        return "Card{" +
                "name='" + name + '\'' +
                ", number='" + number + '\'' +
                '}';
    }

}
