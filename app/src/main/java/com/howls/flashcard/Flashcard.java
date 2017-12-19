package com.howls.flashcard;

import android.media.MediaPlayer;
import android.util.Log;
import android.widget.Toast;

import java.io.IOException;

/**
 * Created by howls on 17/11/23. sadadad
 */

public class Flashcard {
    private int id, position, childID;
    private String word, read,translate,sound, albumId, examples, notes;
    private MediaPlayer m;

    public Flashcard(String word, String read, String translate, String sound, String albumId) {
        this.word = word;
        this.read = read;
        this.translate = translate;
        this.sound = sound;
        this.albumId = albumId;
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

    public String getAlbumId() {return albumId;}

    public void setAlbumId(String albumId) {this.albumId = albumId;}

    public String getExamples() {return examples;}

    public void setExamples(String examples) {this.examples = examples;}

    public String getNotes() {return notes;}

    public void setNotes(String notes) {this.notes = notes;}

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public int getChildID() {
        return childID;
    }

    public void setChildID(int childID) {
        this.childID = childID;
    }

    public int sound() {
        int duration = 0;
        if (this.sound != null) {
            try {
                m = new MediaPlayer();
                m.setDataSource(this.sound);
                m.prepare();
                 duration = m.getDuration();
                m.start();
            } catch (IOException e) {
            }
        }
        return duration;
    }
}
