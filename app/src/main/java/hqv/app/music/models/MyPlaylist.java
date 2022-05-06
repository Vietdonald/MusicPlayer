package hqv.app.music.models;

public class MyPlaylist {
    private int id;
    private String name;

    public MyPlaylist(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return this.id + ": " + this.name;
    }
}
