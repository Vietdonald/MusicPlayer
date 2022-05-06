package hqv.app.music.sqlite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

import java.util.ArrayList;

import hqv.app.music.models.Song;

public class SongHelper extends SQLiteOpenHelper {
    private static final String TB_Playlist = "Playlist";
    private static final String TB_Songlist = "SongList";
    private static final String TB_SongFavorite = "SongFavorite";
    private static final String CREATE_TABLE_SONG_LIST = "CREATE TABLE IF NOT EXISTS SongList(Id INTEGER PRIMARY KEY AUTOINCREMENT, Id_Song INTEGER, Id_List INTEGER)";
    private static final String CREATE_TABLE_PLAYLIST = "CREATE TABLE IF NOT EXISTS Playlist(Id INTEGER PRIMARY KEY AUTOINCREMENT, Name VARCHAR(200))";
    private static final String CREATE_TABLE_SONGFAVORITE = "CREATE TABLE IF NOT EXISTS SongFavorite(Id INTEGER PRIMARY KEY, Name VARCHAR(200), Singer VARCHAR(200), Duration INTEGER, Data VARCHAR(200))";
    public SongHelper(@Nullable Context context) {
        super(context, "music.db", null, 1);
        init();
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
    private void init(){
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL(CREATE_TABLE_SONGFAVORITE);
        db.execSQL(CREATE_TABLE_PLAYLIST);
        db.execSQL(CREATE_TABLE_SONG_LIST);
        db.close();
    }

    public void addPlaylist(String name){
        String sql = "INSERT INTO " + TB_Playlist + " VALUES(null, '" + name + "')";
        Log.e("SQL", sql);
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL(sql);
        db.close();
    }

//    public ArrayList<MyList> getPlaylist(){
//        ArrayList<MyList> myLists = new ArrayList<>();
//        SQLiteDatabase db = getReadableDatabase();
//        String sql = "SELECT * FROM " + TB_Playlist;
//        Cursor cursor = db.rawQuery(sql, null);
//
//        while (cursor.moveToNext()){
//            int id = cursor.getInt(0);
//            String name = cursor.getString(1);
//            MyList myList = new MyList(id, name);
//
//            myLists.add(myList);
//        }
//        db.close();
//        return myLists;
//    }

    public void addSongToList(Song music, int id_list){

    }

    public void addSongToFavorite(Song music){
        SQLiteDatabase db = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("Id", music.getIdSong());
        contentValues.put("Name", music.getTitle());
        contentValues.put("Singer", music.getArtist());
        contentValues.put("Duration", music.getDuration());
        contentValues.put("Data", music.getData());
        db.insert(TB_SongFavorite, null, contentValues);
        db.close();
    }

    public boolean checkSongFavorite(Song music){
        SQLiteDatabase db = getReadableDatabase();
        String sql = "SELECT * FROM " + TB_SongFavorite + " WHERE Id=" + music.getIdSong();
        Cursor cursor = db.rawQuery(sql, null);
        String name = "";
        while (cursor.moveToNext()){
            name = cursor.getString(1);
        }
        db.close();
        if(name != ""){
            return true;
        }
        return false;
    }

    public void deleteSong(Song music){
        SQLiteDatabase db = getWritableDatabase();
        String sql = "DELETE FROM " + TB_SongFavorite + " WHERE Id=" + music.getIdSong();
        db.execSQL(sql);
        db.close();
    }

    public ArrayList<Song> getFavorite(){
        ArrayList<Song> favorites = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        String sql = "SELECT * FROM " + TB_SongFavorite;
        Cursor cursor = db.rawQuery(sql, null);

        while (cursor.moveToNext()){
            int id = cursor.getInt(0);
            String name = cursor.getString(1);
            String singer = cursor.getString(2);
            int duration = cursor.getInt(3);
            String data = cursor.getString(4);
            Song music = new Song(id, name, singer, duration, data);

            favorites.add(music);
        }
        db.close();
        return favorites;
    }

    public ArrayList<Song> getSongByIdList(int id){

        return null;
    }

    public void removeSongToList(Song music, int id_list){

    }
}
