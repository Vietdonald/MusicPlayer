package hqv.app.music;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.Manifest;
import android.content.ContentResolver;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import hqv.app.music.dialogs.PlayMusicDialog;
import hqv.app.music.fragments.NewsFragment;
import hqv.app.music.fragments.OnlineFragment;
import hqv.app.music.fragments.PersonFragment;
import hqv.app.music.models.Action;
import hqv.app.music.models.Song;
import hqv.app.music.sqlite.SongHelper;

public class MainActivity extends AppCompatActivity {
    private static final int REQUEST_PERMISSION_CODE = 1;

    private BottomNavigationView bottomNavigationView;
    private Toolbar toolbar;
    private LinearLayout playMiniCtrl;
    private ImageButton btnPlayPause;
    private ImageButton btnPrevious;
    private ImageButton btnNext;
    private TextView songName;


    private PlayMusicDialog playMusicDialog;
    private ArrayList<Song> mListAllMusic;
    private MusicController musicController;
    private SongHelper songHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();
        requestPermission();
        mListAllMusic = getListSongs();
        musicController = new MusicController(this, mListAllMusic);


        toolbar.setTitle("Cá nhân");
        replaceFragment(new PersonFragment(mListAllMusic, musicController));

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.action_offline:
                        toolbar.setTitle("Cá nhân");
                        replaceFragment(new PersonFragment(mListAllMusic, musicController));
                        break;

                    case R.id.action_online:
                        toolbar.setTitle("Nhạc online");
                        replaceFragment(new OnlineFragment());
                        break;

                    case R.id.action_notify:
                        toolbar.setTitle("Thông báo");
                        replaceFragment(new NewsFragment());
                        break;
                }
                return true;
            }
        });

        showMiniControll();
    }

    @Override
    protected void onResume() {
        super.onResume();
        musicController.recieveBroadcast(songName, btnPlayPause, null);
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        musicController.unRecieveBroadcast();
    }

    private void showMiniControll(){
        songName.setText(mListAllMusic.get(0).getTitle());
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

        playMiniCtrl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                playMusicDialog = null;
                playMusicDialog = new PlayMusicDialog(MainActivity.this, musicController);
                playMusicDialog.show();
            }
        });
    }

    private void replaceFragment(Fragment fragment){
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.main_frame, fragment);
        transaction.commit();
    }

    private void requestPermission(){
        if(Build.VERSION.SDK_INT < Build.VERSION_CODES.M){
            return;
        }
        if(checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED){
            mListAllMusic = getListSongs();
        }
        else{
            String[] permission = {Manifest.permission.READ_EXTERNAL_STORAGE};
            requestPermissions(permission, REQUEST_PERMISSION_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_PERMISSION_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                mListAllMusic = getListSongs();
            } else {
                Toast.makeText(this, "Quyền truy cập bị từ chối", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public ArrayList getListSongs() {
        ArrayList<Song> listMusics = new ArrayList();

        ContentResolver musicResolver = getContentResolver();
        Uri musicUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        Cursor musicCursor = musicResolver.query(musicUri, null, null, null, null);
        if (musicCursor != null && musicCursor.moveToFirst()) {
            int titleColumn = musicCursor.getColumnIndex(MediaStore.Audio.Media.TITLE);
            int idColumn = musicCursor.getColumnIndex(MediaStore.Audio.Media._ID);
            int artistColumn = musicCursor.getColumnIndex(MediaStore.Audio.Media.ARTIST);
            int durationColumn = musicCursor.getColumnIndex(MediaStore.Audio.Media.DURATION);
            int dataColumn = musicCursor.getColumnIndex(MediaStore.Audio.Media.DATA);
            do {
                int id = musicCursor.getInt(idColumn);
                String title = musicCursor.getString(titleColumn);
                String artist = musicCursor.getString(artistColumn);
                int duration = Integer.parseInt(musicCursor.getString(durationColumn));
                String data = musicCursor.getString(dataColumn);
                listMusics.add(new Song(id, title, artist, duration, data));
            }
            while (musicCursor.moveToNext());
        }
        Collections.sort(listMusics, new Comparator<Song>() {
            @Override
            public int compare(Song music, Song t1) {
                return music.getTitle().compareTo(t1.getTitle());
            }
        });
        return listMusics;
    }

    private void initView(){
        bottomNavigationView = findViewById(R.id.nav_bottom);
        toolbar = findViewById(R.id.toolbar);
        playMiniCtrl = findViewById(R.id.play_music_mini_main);
        btnPlayPause = findViewById(R.id.btn_play_pause_mini_main);
        btnPrevious = findViewById(R.id.btn_prev_mini_main);
        btnNext = findViewById(R.id.btn_next_mini_main);
        songName = findViewById(R.id.song_name_mini_main);

        mListAllMusic = new ArrayList<>();
    }

}