package hqv.app.music.models;

import java.io.Serializable;

public class Song implements Serializable {
    private int idSong;
    private String title;
    private String artist;
    private int duration;
    private String data;

    public Song(int idSong, String title, String artist, int duration, String data) {
        this.idSong = idSong;
        this.title = title;
        this.artist = artist;
        this.duration = duration;
        this.data = data;
    }

    public int getIdSong() {
        return idSong;
    }

    public void setIdSong(int idSong) {
        this.idSong = idSong;
    }

    public String getTitle() {
        return title;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

}
