package hqv.app.music.services;

import static hqv.app.music.ApplicationChannel.CHANNEL_ID;

import android.app.Notification;
import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.IBinder;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import hqv.app.music.R;
import hqv.app.music.models.Action;
import hqv.app.music.models.Song;

public class MusicService extends Service {

    private MediaPlayer mediaPlayer;
    private Song music;
    private String flag;
    private int seekTo;
    private Thread sendDuration;
    public MusicService() {
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        flag = (String) intent.getStringExtra("FLAG");
        music = (Song) intent.getSerializableExtra("SONG");
        seekTo = (int) intent.getIntExtra(Action.DURATION, -1);

        sendNotice();

        if(sendDuration != null){
            sendDuration = null;
        }
        sendDuration = new Thread(){
            @Override
            public void run() {
                super.run();
                while (true){
                    sendDurations(mediaPlayer.getCurrentPosition());
                    try {
                        sleep(500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        };

        playControl(sendDuration);

        if(mediaPlayer.isPlaying()){
            mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mediaPlayer) {
                    sendBroadcastTo(Action.COMPLETE);
                }
            });
        }

        return START_NOT_STICKY;
    }

    private void playControl(Thread sendDuration) {
        switch (flag) {
            case Action.PLAY:
                if (music != null) {
                    playSong(music);
                    sendBroadcastTo(Action.PLAYING);
                } else {
                    Toast.makeText(this, "Không tìm thấy bài: " + music.getTitle(), Toast.LENGTH_SHORT).show();
                }
                break;
            case Action.CHANGE:
                if (music != null) {
                    pauseMusic();
                    changeSong(music);
                    sendBroadcastTo(Action.PLAYING);
                } else {
                    Toast.makeText(this, "Không tìm thấy bài: " + music.getTitle(), Toast.LENGTH_SHORT).show();
                }
                break;
            case Action.PREVIOUS:
                if (music != null) {
                    pauseMusic();
                    changeSong(music);
                    sendBroadcastTo(Action.PLAYING);
                } else {
                    Toast.makeText(this, "Không tìm thấy bài: " + music.getTitle(), Toast.LENGTH_SHORT).show();
                }
                break;
            case Action.NEXT:
                if (music != null) {
                    pauseMusic();
                    changeSong(music);
                    sendBroadcastTo(Action.PLAYING);
                } else {
                    Toast.makeText(this, "Không tìm thấy bài: " + music.getTitle(), Toast.LENGTH_SHORT).show();
                }
                break;
            case Action.PAUSE:
                pauseMusic();
                sendBroadcastTo(Action.PAUSE);
                break;
            case Action.SEEK:
                seekSong(seekTo);
                break;
        }

        sendDuration.start();
    }

    private void seekSong(int toDuration) {
        if(toDuration != -1){
            mediaPlayer.seekTo(toDuration);
        }
    }

    private void playSong(Song music){
        if(music == null){
            return;
        }
        if(mediaPlayer == null){
            mediaPlayer = MediaPlayer.create(getApplicationContext(), Uri.parse(music.getData()));
        }
        mediaPlayer.start();
    }

    private void changeSong(Song music){
        mediaPlayer = MediaPlayer.create(getApplicationContext(), Uri.parse(music.getData()));
        mediaPlayer.start();
    }

    private void pauseMusic() {
        if(mediaPlayer != null){
            mediaPlayer.pause();
        }
    }

    private void sendBroadcastTo(String action){
        Intent intent = new Intent();
        intent.setAction(Action.CONTROL);
        intent.putExtra(Action.ACTION, action);
        intent.putExtra(Action.SONG_PLAYING, music);
        intent.putExtra(Action.SEEK, mediaPlayer.getCurrentPosition());
        sendBroadcast(intent);
    }

    private void sendDurations(int duration){
        Intent intent = new Intent();
        intent.setAction(Action.SEEK);
        intent.putExtra(Action.DURATION, duration);
        sendBroadcast(intent);
    }

    private void sendNotice(){
        Notification notification = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_note)
                .setContentTitle("Track title")
                .setContentText("Artist - Album")
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .build();

        NotificationManagerCompat managerCompat = NotificationManagerCompat.from(this);
        managerCompat.notify(0, notification);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(mediaPlayer != null){
            mediaPlayer.release();
            mediaPlayer = null;
        }
        stopSelf();
    }
}