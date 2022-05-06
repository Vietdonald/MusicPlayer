package hqv.app.music.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import hqv.app.music.MusicController;
import hqv.app.music.R;
import hqv.app.music.models.Action;
import hqv.app.music.sqlite.SongHelper;

public class PlayMusicDialog extends Dialog {
    private MusicController musicController;
    private SongHelper songHelper;

    private ImageButton btnClose;
    private ImageButton btnFavorite;
    private ImageButton btnPlaylist;
    private ImageButton btnRandom;
    private ImageButton btnPrevious;
    private ImageButton btnPlayPause;
    private ImageButton btnNext;
    private ImageButton btnRepeat;
    private TextView playNameSong;
    private ImageView cd;
    private SeekBar seekBar;

    public PlayMusicDialog(@NonNull Context context, MusicController musicController) {
        super(context);
        this.musicController = musicController;
        songHelper = new SongHelper(context);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.play_music_layout);

        Window window = getWindow();
        if(window == null){
            return;
        }
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        initView();
        playNameSong.setText(musicController.getSongPlaying().getTitle());
        if(musicController.isPlaying()){
            btnPlayPause.setImageResource(R.drawable.icon_pause);
        }else{
            btnPlayPause.setImageResource(R.drawable.icon_play);
        }
        if(songHelper.checkSongFavorite(musicController.getSongPlaying())){
            btnFavorite.setImageResource(R.drawable.ic_favorite);
        }else {
            btnFavorite.setImageResource(R.drawable.ic_nonfavorite);
        }

        btnClose.setOnClickListener(onClickItem());
        btnFavorite.setOnClickListener(onClickItem());
        btnPlaylist.setOnClickListener(onClickItem());
        btnRandom.setOnClickListener(onClickItem());
        btnPrevious.setOnClickListener(onClickItem());
        btnPlayPause.setOnClickListener(onClickItem());
        btnNext.setOnClickListener(onClickItem());
        btnRepeat.setOnClickListener(onClickItem());

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LinearLayout playMusicDialog = findViewById(R.id.play_music_dialog);
        Animation animation = AnimationUtils.loadAnimation(getContext(), R.anim.slide_up);
        animation.setDuration(600);
        playMusicDialog.startAnimation(animation);

        musicController.recieveBroadcast(playNameSong, btnPlayPause, btnFavorite);
        musicController.recieveDuration(seekBar);

    }

    private View.OnClickListener onClickItem(){
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(view.getId() == btnClose.getId()){
                    dismiss();
                }
                else if(view.getId() == btnFavorite.getId()){
                    if(songHelper.checkSongFavorite(musicController.getSongPlaying())){
                        songHelper.deleteSong(musicController.getSongPlaying());
                        btnFavorite.setImageResource(R.drawable.ic_nonfavorite);
                        Toast.makeText(getContext(), "Đã xóa khỏi yêu thích", Toast.LENGTH_SHORT).show();
                    }else{
                        songHelper.addSongToFavorite(musicController.getSongPlaying());
                        btnFavorite.setImageResource(R.drawable.ic_favorite);
                        Toast.makeText(getContext(), "Đã thêm vào yêu thích", Toast.LENGTH_SHORT).show();
                    }
                }
                else if(view.getId() == btnPlaylist.getId()){
                    PlayingListDialog listSongDialog = new PlayingListDialog(getContext(), musicController.getPlayingListSong(), "Đang phát", musicController);
                    listSongDialog.show();
                }
                else if(view.getId() == btnRandom.getId()){
                    Toast.makeText(getContext(), "Random", Toast.LENGTH_SHORT).show();
                }
                else if(view.getId() == btnPrevious.getId()){
                    musicController.controlSong(Action.PREVIOUS);
                }
                else if(view.getId() == btnPlayPause.getId()){
                    if(musicController.isPlaying()){
                        musicController.controlSong(Action.PAUSE);
                    }else{
                        musicController.controlSong(Action.PLAY);
                    }
                }
                else if(view.getId() == btnNext.getId()){
                    musicController.controlSong(Action.NEXT);
                }
                else if(view.getId() == btnRepeat.getId()){
                    Toast.makeText(getContext(), "Repeat", Toast.LENGTH_SHORT).show();
                }
            }
        };
    }

    @Override
    public void dismiss() {
        super.dismiss();
        setOnDismissListener(new OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {
                LinearLayout playMusicDialog = findViewById(R.id.play_music_dialog);
                Animation animation = AnimationUtils.loadAnimation(getContext(), R.anim.slide_down);
                animation.setDuration(600);
                playMusicDialog.startAnimation(animation);
            }
        });
    }

    private void initView() {
        btnClose = findViewById(R.id.play_close);
        btnFavorite = findViewById(R.id.play_btn_favorite);
        btnPlaylist = findViewById(R.id.play_btn_playlist);
        btnRandom = findViewById(R.id.play_random);
        btnPrevious = findViewById(R.id.play_prev);
        btnPlayPause = findViewById(R.id.play_pause);
        btnNext = findViewById(R.id.play_next);
        btnRepeat = findViewById(R.id.play_repeat);
        playNameSong = findViewById(R.id.play_name_song);
        cd = findViewById(R.id.cd);
        seekBar = findViewById(R.id.play_seekbar);
    }
}
