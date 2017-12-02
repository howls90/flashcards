package com.howls.flashcard;

/**
 * Created by howls on 17/11/23. sadadad
 */

public class Flashcard {
    private int id;
    private String word, read,translate,sound;

    public Flashcard(String word, String read, String translate, String sound) {
        this.word = word;
        this.read = read;
        this.translate = translate;
        this.sound = sound;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public String getRead() {
        return read;
    }

    public void setRead(String read) {
        this.read = read;
    }

    public String getTranslate() {
        return translate;
    }

    public void setTranslate(String translate) { this.translate = translate; }

    public String getSound() { return sound; }

    public void setSound(String sound) { this.sound = sound; }

}
