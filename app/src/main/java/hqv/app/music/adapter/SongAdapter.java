package hqv.app.music.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import hqv.app.music.R;
import hqv.app.music.interfaces.IClickSong;
import hqv.app.music.models.Song;

public class SongAdapter extends RecyclerView.Adapter<SongAdapter.ViewHolder> {
    private ArrayList<Song> mListSongs;
    private IClickSong iClickSong;

    public SongAdapter(ArrayList<Song> mListMusic, IClickSong iClickSong){
        this.mListSongs = mListMusic;
        this.iClickSong = iClickSong;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.song_layout, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Song song = mListSongs.get(position);
        holder.mSong_name.setText(song.getTitle());
        holder.mSong_artist.setText(song.getArtist());
        holder.songLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                iClickSong.onClickSong(song);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mListSongs.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        private TextView mSong_name;
        private TextView mSong_artist;
        private RelativeLayout songLayout;

        public ViewHolder(@NonNull View itemView){
            super(itemView);
            mSong_name = itemView.findViewById(R.id.name_song);
            mSong_artist = itemView.findViewById(R.id.artist_song);
            songLayout = itemView.findViewById(R.id.songlayout);
        }
    }
}

