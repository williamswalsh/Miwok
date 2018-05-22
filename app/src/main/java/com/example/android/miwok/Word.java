package com.example.android.miwok;

public class Word {
    private String miwok;
    private String english;     // English_translation for english speakers
    private int imageRef;
    private int musicRef;

    public Word(String english, String miwok, int imageRef) {
        this.english = english;
        this.miwok = miwok;
        this.imageRef = imageRef;
    }

    public Word(String english, String miwok, int imageRef, int musicRef) {
        this.english = english;
        this.miwok = miwok;
        this.imageRef = imageRef;
        this.musicRef = musicRef;
    }

    public String getMiwok() {
        return miwok;
    }

    public String getEnglish() {
        return english;
    }

    public void setMiwok(String miwok) {
        this.miwok = miwok;
    }

    public void setEnglish(String english) {
        this.english = english;
    }

    public int getImageRef() {
        return imageRef;
    }

    public void setImageRef(int imageRef) {
        this.imageRef = imageRef;
    }

    public int getMusicRef() {
        return musicRef;
    }

    public void setMusicRef(int musicRef) {
        this.musicRef = musicRef;
    }
}
