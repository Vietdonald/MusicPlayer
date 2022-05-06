package hqv.app.music.services;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.ImageButton;
import android.widget.TextView;

import hqv.app.music.R;
import hqv.app.music.models.Action;
import hqv.app.music.models.Song;

public class MyReciever extends BroadcastReceiver {
    private Song musicPlaying;
    private TextView songName;
    private ImageButton btnPlayPause;

    public MyReciever(TextView songName, ImageButton btnPlayPause) {
        this.songName = songName;
        this.btnPlayPause = btnPlayPause;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = (String) intent.getStringExtra(Action.ACTION);
        switch (action) {
            case Action.PLAYING:
                musicPlaying = (Song) intent.getSerializableExtra(Action.SONG_PLAYING);
                if(musicPlaying != null){
                    songName.setText(musicPlaying.getTitle());
                    btnPlayPause.setImageResource(R.drawable.icon_pause);
                }
                break;
            case Action.PAUSE:
                btnPlayPause.setImageResource(R.drawable.icon_play);
                break;
            case Action.COMPLETE:
                break;
        }
    }
}
