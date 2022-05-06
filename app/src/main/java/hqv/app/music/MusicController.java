package hqv.app.music;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;


import java.util.ArrayList;

import hqv.app.music.models.Action;
import hqv.app.music.models.Song;
import hqv.app.music.services.MusicService;
import hqv.app.music.sqlite.SongHelper;

public class MusicController {
    private static final String FLAG = "FLAG";
    private static final String SONG = "SONG";

    private ArrayList<Song> playingListMusic;
    private Context context;
    private Intent intent;
    private Song musicPlaying;
    private Song music;
    private boolean isPlaying;
    private int curentPos;

    private BroadcastReceiver reciever;
    private SongHelper songHelper;

    public MusicController(Context context, ArrayList<Song> playingListMusic) {
        this.context = context;
        this.playingListMusic = playingListMusic;
        this.music = playingListMusic.get(0);
        this.musicPlaying = playingListMusic.get(0);
        intent = new Intent(context, MusicService.class);
        songHelper = new SongHelper(context);
    }

    public void controlSong(String action){
        switch (action){
            case Action.PLAY:
                playSong(action);
                break;
            case Action.NEXT:
                nextSong(action);
                break;
            case Action.PREVIOUS:
                previousSong(action);
                break;
            case Action.PAUSE:
                pauseSong(action);
                break;
            case Action.CHANGE:
                changeSong(action);
                break;
        }
    }

    private void changeSong(String action) {
        intent.putExtra(FLAG, action);
        intent.putExtra(SONG, music);
        context.startService(intent);
        curentPos = 0;
    }

    public void playSong(String action){
        intent.putExtra(FLAG, action);
        intent.putExtra(SONG, music);
        context.startService(intent);
    }

    public void pauseSong(String action){
        intent.putExtra(FLAG, action);
        context.startService(intent);
    }

    public void nextSong(String action){
        int site = getSiteBySong(musicPlaying);
        if(site == -1){
            return;
        }
        if(site == (playingListMusic.size() - 1)){
            site = 0;
        }else{
            site += 1;
        }
        music = playingListMusic.get(site);
        intent.putExtra(FLAG, action);
        intent.putExtra(SONG, music);
        context.startService(intent);
        curentPos = 0;
    }

    public void previousSong(String action){
        int site = getSiteBySong(musicPlaying);
        if(site == -1){
            return;
        }
        if(site == 0){
            site = playingListMusic.size() - 1;
        }else{
            site -= 1;
        }
        music = playingListMusic.get(site);
        intent.putExtra(FLAG, action);
        intent.putExtra(SONG, music);
        context.startService(intent);
        curentPos = 0;
    }

    public void seekSong(int toDuration){
        intent.putExtra(FLAG, Action.SEEK);
        intent.putExtra(Action.DURATION, toDuration);
        context.startService(intent);
    }

    public void recieveBroadcast(TextView songName, ImageButton btnPlayPause, ImageButton btnFavorite){
        reciever = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                String action = (String) intent.getStringExtra(Action.ACTION);
                switch (action){
                    case Action.PLAYING:
                        musicPlaying = (Song) intent.getSerializableExtra(Action.SONG_PLAYING);
                        if(musicPlaying != null){
                            songName.setText(musicPlaying.getTitle());
                            btnPlayPause.setImageResource(R.drawable.icon_pause);
                            isPlaying = true;
                        }
                        break;
                    case Action.PAUSE:
                        btnPlayPause.setImageResource(R.drawable.icon_play);
                        isPlaying = false;
                        break;
                    case Action.COMPLETE:
                        Song s = (Song) intent.getSerializableExtra(Action.SONG_PLAYING);
                        if(s != null){
                            int site = getSiteBySong(s);
                            if(site == -1){
                                return;
                            }
                            if(site == (playingListMusic.size() - 1)){
                                site = 0;
                            }else{
                                site += 1;
                            }
                            controlSong(Action.NEXT);
                        }
                        break;
                }

                if(btnFavorite != null){
                    if(songHelper.checkSongFavorite(musicPlaying)){
                        btnFavorite.setImageResource(R.drawable.ic_favorite);
                    }else {
                        btnFavorite.setImageResource(R.drawable.ic_nonfavorite);
                    }
                }
            }
        };
        IntentFilter intentFilter = new IntentFilter(Action.CONTROL);
        context.registerReceiver(reciever, intentFilter);

    }
    public void recieveDuration(SeekBar seekBar){
        BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                int position = (int) intent.getIntExtra(Action.DURATION, -1);
                curentPos = position;
                seekBar.setMax(musicPlaying.getDuration());
                seekBar.setProgress(curentPos);
            }
        };
        IntentFilter intentFilter = new IntentFilter(Action.SEEK);
        context.registerReceiver(broadcastReceiver, intentFilter);
    }

    public void unRecieveBroadcast(){
        if(reciever != null){
            context.unregisterReceiver(reciever);
        }
    }
    public Song getSongPlaying(){
        return this.musicPlaying;
    }
    public void setSongPlaying(Song music){
        this.musicPlaying = music;
    }

    public Song getSong() {
        return music;
    }

    public void setSong(Song music) {
        this.music = music;
    }

    public ArrayList<Song> getPlayingListSong() {
        return playingListMusic;
    }

    public void setPlayingListSong(ArrayList<Song> playingListMusic) {
        this.playingListMusic = playingListMusic;
    }

    public boolean isPlaying(){
        return this.isPlaying;
    }

    public int getCurentPos() {
        return curentPos;
    }

    public void setCurentPos(int curentPos) {
        this.curentPos = curentPos;
    }

    public int getSiteBySong(Song music){
        for(int i = 0; i < playingListMusic.size(); i++){
            if(music.getIdSong() == playingListMusic.get(i).getIdSong()){
                return i;
            }
        }
        return -1;
    }

}

