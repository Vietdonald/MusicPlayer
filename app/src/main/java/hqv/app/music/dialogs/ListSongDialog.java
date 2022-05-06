package hqv.app.music.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import hqv.app.music.MusicController;
import hqv.app.music.R;
import hqv.app.music.adapter.SongAdapter;
import hqv.app.music.interfaces.IClickSong;
import hqv.app.music.models.Action;
import hqv.app.music.models.Song;

public class ListSongDialog extends Dialog {

    private TextView dialogTitle;
    private LinearLayout playMusicMini;
    private ImageButton btnBack;
    private TextView songNameMini;
    private ImageButton btnPlayPause;
    private ImageButton btnNext;
    private ImageButton btnPrevious;

    private MusicController msctrl;

    public ListSongDialog(@NonNull Context context, ArrayList<Song> listMusic, String mTitle, MusicController musicController) {
        super(context);
        this.msctrl = musicController;
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.list_layout);

        Window window = getWindow();
        if(window == null){
            return;
        }
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        initView();
        dialogTitle.setText(mTitle);
        songNameMini.setText(musicController.getSongPlaying().getTitle());
        if(musicController.isPlaying()){
            btnPlayPause.setImageResource(R.drawable.icon_pause);
        }else{
            btnPlayPause.setImageResource(R.drawable.icon_play);
        }
        musicController.recieveBroadcast(songNameMini, btnPlayPause, null);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
        RecyclerView rcvListSong = findViewById(R.id.dialog_listsong);
        rcvListSong.setLayoutManager(linearLayoutManager);
        SongAdapter songAdapter = new SongAdapter(listMusic, new IClickSong() {
            @Override
            public void onClickSong(Song song) {
                musicController.setPlayingListSong(listMusic);
                musicController.setSong(song);
                musicController.controlSong(Action.CHANGE);
            }
        });
        rcvListSong.setAdapter(songAdapter);
        RecyclerView.ItemDecoration itemDecoration = new DividerItemDecoration(context, DividerItemDecoration.VERTICAL);
        rcvListSong.addItemDecoration(itemDecoration);

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });

        playMusicMini.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PlayMusicDialog playMusicDialog = new PlayMusicDialog(context, musicController);
                playMusicDialog.show();
            }
        });

        btnPlayPause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(musicController.isPlaying()){
                    musicController.controlSong(Action.PAUSE);
                }else {
                    musicController.controlSong(Action.PLAY);
                }
            }
        });

        btnPrevious.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                musicController.controlSong(Action.PREVIOUS);
            }
        });

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                musicController.controlSong(Action.NEXT);
            }
        });
    }

    private void initView() {
        dialogTitle = findViewById(R.id.dialog_titile);
        btnBack = findViewById(R.id.dialog_back);
        playMusicMini = findViewById(R.id.play_music_mini);
        songNameMini = findViewById(R.id.song_name_mini);
        btnPlayPause = findViewById(R.id.btn_play_pause_mini);
        btnNext = findViewById(R.id.btn_next_mini);
        btnPrevious = findViewById(R.id.btn_prev_mini);
    }

    @Override
    public void dismiss() {
        super.dismiss();
        if(msctrl != null){
            msctrl.unRecieveBroadcast();
        }
    }
}
