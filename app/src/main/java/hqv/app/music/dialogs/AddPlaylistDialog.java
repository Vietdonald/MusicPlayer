package hqv.app.music.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import hqv.app.music.R;
import hqv.app.music.sqlite.SongHelper;

public class AddPlaylistDialog extends Dialog {
    private TextView cancel;
    private TextView save;
    private EditText name;

    public AddPlaylistDialog(@NonNull Context context) {
        super(context);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.add_playlist_layout);

        Window window = getWindow();
        if(window == null){
            return;
        }
        window.setLayout(WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT);
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        name = findViewById(R.id.edt_playlist_name);
        save = findViewById(R.id.action_save);
        cancel = findViewById(R.id.action_cancel);

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SongHelper songHelper = new SongHelper(context);
                String playlistName = name.getText().toString().trim();
                if(playlistName != null){
                    songHelper.addPlaylist(playlistName);
                    Toast.makeText(context, "Đã lưu", Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(context, "Vui lòng nhập tên danh sách", Toast.LENGTH_SHORT).show();
                }
                dismiss();
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
    }
}
