package hqv.app.music.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.util.ArrayList;

import hqv.app.music.MainActivity;
import hqv.app.music.MusicController;
import hqv.app.music.R;
import hqv.app.music.adapter.SongAdapter;
import hqv.app.music.dialogs.AddPlaylistDialog;
import hqv.app.music.dialogs.ListSongDialog;
import hqv.app.music.interfaces.IClickPlaylist;
import hqv.app.music.models.Song;
import hqv.app.music.models.MyPlaylist;
import hqv.app.music.sqlite.SongHelper;

public class PersonFragment extends Fragment {
    private ArrayList<Song> listMusic;
    private MusicController musicController;
    private String title;
    private MainActivity mainActivity;
    private SongHelper songHelper;

    private LinearLayout btnAllSong;
    private LinearLayout btnFavorite;
    private LinearLayout btnSinger;
    private LinearLayout btnAlbum;
    private LinearLayout btnAddPlaylist;
    private ListSongDialog listSongDialog;
    private RecyclerView rcvPlayList;

    public PersonFragment() {

    }

    public PersonFragment(ArrayList<Song> listMusic, MusicController musicController) {
        this.listMusic = listMusic;
        this.musicController = musicController;

    }

    public static PersonFragment newInstance(String param1, String param2) {
        PersonFragment fragment = new PersonFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_person, container, false);

        mainActivity = (MainActivity) getActivity();
        songHelper = new SongHelper(getContext());

        btnAllSong = view.findViewById(R.id.person_all_song);
        btnFavorite = view.findViewById(R.id.person_favorite_song);
        btnSinger = view.findViewById(R.id.person_singer);
        btnAlbum = view.findViewById(R.id.person_album);
        btnAddPlaylist = view.findViewById(R.id.person_add_playlist);
        rcvPlayList = view.findViewById(R.id.fragment_playlist);

        btnAllSong.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                title = "Tất cả bài hát";
                listSongDialog = new ListSongDialog(mainActivity, listMusic, title, musicController);
                listSongDialog.show();
            }
        });

        btnFavorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ArrayList<Song> favorites = songHelper.getFavorite();
                title = "Yêu thích";
                listSongDialog = new ListSongDialog(mainActivity, favorites, title, musicController);
                listSongDialog.show();
            }
        });

        btnSinger.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        btnAlbum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        btnAddPlaylist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AddPlaylistDialog addPlaylistDialog = new AddPlaylistDialog(getContext());
                addPlaylistDialog.show();
            }
        });


        return view;
    }

}