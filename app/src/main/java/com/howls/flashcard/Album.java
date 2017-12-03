package com.howls.flashcard;

/**
 * Created by howls on 17/11/24.
 */

public class Album {

    private int id, num;
    private String name;

    public Album(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getNum() {return num;}

    public void setNum(int num) {this.num = num;}
}
