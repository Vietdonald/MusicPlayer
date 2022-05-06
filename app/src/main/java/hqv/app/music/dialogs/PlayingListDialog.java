package hqv.app.music.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
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

public class PlayingListDialog extends Dialog {
    private TextView dialogTitle;
    private ImageButton btnClose;

    public PlayingListDialog(@NonNull Context context, ArrayList<Song> listMusic, String mTitle, MusicController musicController) {
        super(context);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.playing_list_layout);

        Window window = getWindow();
        if(window == null){
            return;
        }
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        initView();
        dialogTitle.setText(mTitle);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
        RecyclerView rcvListSong = findViewById(R.id.playinglist_listsong);
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

        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        RelativeLayout playinglist = findViewById(R.id.playinglist);
        Animation animation = AnimationUtils.loadAnimation(getContext(), R.anim.slide_up);
        animation.setDuration(600);
        playinglist.startAnimation(animation);
    }

    private void initView() {
        dialogTitle = findViewById(R.id.dialog_titile_playinglist);
        btnClose = findViewById(R.id.dialog_close);
    }
}
