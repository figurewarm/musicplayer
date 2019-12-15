package com.example.musicplayer;

import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {


    private Handler myHandler = new Handler();
    private static final String TAG = "myTag";
    private MediaPlayer mediaPlayer;
    private TextView timeTextView;
    private SimpleDateFormat time = new SimpleDateFormat("m:ss");//mm分:ss秒
    private SeekBar seekBar;
    private TextView music_name;
    private int flag = 0;
    private ArrayList<String> cMusics = new ArrayList<>();

    final String[] curMusic = {""};

    @Override
    protected void onResume() {
        super.onResume();
        Intent intent = getIntent();
        ArrayList<String> cMusic = intent.getStringArrayListExtra("values");
        if (cMusic != null) {
            for (int i = 0;i < cMusic.size();i++) {
                Log.e("safasdf", cMusic.get(i));
                cMusics.add(i,cMusic.get(i));
            }
            cMusics = cMusic;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final ArrayList<String> musics = getFilesAllName("");

        final MusicAdapter adapter = new MusicAdapter(MainActivity.this, R.layout.musiclist, cMusics);

        ListView listView = findViewById(R.id.list_view);
        listView.setAdapter(adapter);


        mediaPlayer = new MediaPlayer();
        seekBar = findViewById(R.id.seekbar);
        music_name = findViewById(R.id.music_name);
        timeTextView = findViewById(R.id.text1);

        final TextView txtLoopState = findViewById(R.id.txtLoopState);
        final Button buttonPause = findViewById(R.id.buttonPause);
        final Button buttonStop = findViewById(R.id.buttonStop);
        final Button buttonLoop = findViewById(R.id.buttonLoop);
        final Button buttonLast = findViewById(R.id.previous);
        final Button buttonNext = findViewById(R.id.next);
        buttonPause.setEnabled(false);
        buttonStop.setEnabled(false);
        buttonLoop.setEnabled(false);
        buttonLast.setEnabled(false);
        buttonNext.setEnabled(false);
        mediaPlayer.setLooping(true);
        txtLoopState.setText("单曲循环");
        final Button buttonmine = findViewById(R.id.mine);
        final Button buttonPlayingList = findViewById(R.id.playingList);
        buttonmine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (cMusics != null) {
                    for (String i : cMusics) {
                        Log.e("safasdf", i);
                    }
                }
                ListView listView = findViewById(R.id.list_view);
                listView.setAdapter(adapter);
            }
        });

        buttonPlayingList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(MainActivity.this, Main2Activity.class);
                startActivity(intent1);
            }
        });

        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override

            public void onCompletion(MediaPlayer mp) {
                Log.v(TAG, "setOnCompletionListener");
                String music = "";
                if (flag == 0)
                    music = curMusic[0];
                else if (flag == 1)
                    music = ramdom(curMusic[0]);
                else if (flag == 2)
                    music = theNext(curMusic[0]);
                Log.e("music", music);
                try {
                    Log.v(TAG, "start");
                    mediaPlayer.reset();
                    AssetManager assetManager = getAssets();
                    AssetFileDescriptor assetFileDescriptor = assetManager.openFd(music);
                    mediaPlayer.setDataSource(assetFileDescriptor.getFileDescriptor(), assetFileDescriptor.getStartOffset(), assetFileDescriptor.getLength());
                    mediaPlayer.prepare();
                    mediaPlayer.start();
                } catch (IllegalArgumentException e) {
                    e.printStackTrace();
                } catch (IllegalStateException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                curMusic[0] = music;
                seekBar.setMax(mediaPlayer.getDuration());

            }
        });

        myHandler.post(update_music);
        buttonPause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mediaPlayer.isPlaying()) {
                    buttonPause.setText("Play");
                    mediaPlayer.pause();
                } else {
                    buttonPause.setText("Pause");
                    mediaPlayer.start();
                }
            }
        });

        buttonStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mediaPlayer.isPlaying()) mediaPlayer.stop();
            }
        });

        buttonLoop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (flag == 2)
                    flag = 0;
                else flag = flag + 1;
                if (flag == 0) {
                    txtLoopState.setText("单曲循环");
                } else if (flag == 1) {
                    txtLoopState.setText("随机播放");
                } else if (flag == 2) {
                    txtLoopState.setText("顺序播放");
                }
            }
        });


        buttonNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (int i = 0; i < cMusics.size(); i++) {
                    if (curMusic[0].equals(cMusics.get(i))) {
                        if (i == cMusics.size() - 1) {
                            String music = cMusics.get(0);
                            Log.v("curMusic", music);
                            try {
                                mediaPlayer.reset();
                                AssetManager assetManager = getAssets();
                                AssetFileDescriptor assetFileDescriptor = assetManager.openFd(music);
                                mediaPlayer.setDataSource(assetFileDescriptor.getFileDescriptor(), assetFileDescriptor.getStartOffset(), assetFileDescriptor.getLength());
                                mediaPlayer.prepare();
                                mediaPlayer.start();
                            } catch (IllegalArgumentException e) {
                                e.printStackTrace();
                            } catch (IllegalStateException e) {
                                e.printStackTrace();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            curMusic[0] = music;
                            seekBar.setMax(mediaPlayer.getDuration());
                            break;
                        } else {
                            String music = cMusics.get(i + 1);
                            Log.v("curMusic", music);
                            try {
                                mediaPlayer.reset();
                                AssetManager assetManager = getAssets();
                                AssetFileDescriptor assetFileDescriptor = assetManager.openFd(music);
                                mediaPlayer.setDataSource(assetFileDescriptor.getFileDescriptor(), assetFileDescriptor.getStartOffset(), assetFileDescriptor.getLength());
                                mediaPlayer.prepare();
                                mediaPlayer.start();
                            } catch (IllegalArgumentException e) {
                                e.printStackTrace();
                            } catch (IllegalStateException e) {
                                e.printStackTrace();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            curMusic[0] = music;
                            seekBar.setMax(mediaPlayer.getDuration());
                            break;
                        }
                    }
                }
            }
        });


        buttonLast.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (int i = 0; i < cMusics.size(); i++) {
                    if (curMusic[0].equals(cMusics.get(i))) {
                        if (i == 0) {
                            String music = cMusics.get(cMusics.size() - 1);
                            Log.v("curMusic", music);
                            try {
                                mediaPlayer.reset();
                                AssetManager assetManager = getAssets();
                                AssetFileDescriptor assetFileDescriptor = assetManager.openFd(music);
                                mediaPlayer.setDataSource(assetFileDescriptor.getFileDescriptor(), assetFileDescriptor.getStartOffset(), assetFileDescriptor.getLength());
                                mediaPlayer.prepare();
                                mediaPlayer.start();
                            } catch (IllegalArgumentException e) {
                                e.printStackTrace();
                            } catch (IllegalStateException e) {
                                e.printStackTrace();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            curMusic[0] = music;
                            seekBar.setMax(mediaPlayer.getDuration());
                            break;
                        } else {
                            String music = cMusics.get(i - 1);
                            Log.v("curMusic", music);
                            try {
                                mediaPlayer.reset();
                                AssetManager assetManager = getAssets();
                                AssetFileDescriptor assetFileDescriptor = assetManager.openFd(music);
                                mediaPlayer.setDataSource(assetFileDescriptor.getFileDescriptor(), assetFileDescriptor.getStartOffset(), assetFileDescriptor.getLength());
                                mediaPlayer.prepare();
                                mediaPlayer.start();
                            } catch (IllegalArgumentException e) {
                                e.printStackTrace();
                            } catch (IllegalStateException e) {
                                e.printStackTrace();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            curMusic[0] = music;
                            seekBar.setMax(mediaPlayer.getDuration());
                            break;
                        }
                    }
                }
            }
        });

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {


                Log.e("position", position + " ");
//                Log.e("position",cMusics.get(position)+cMusics.get(0)+cMusics.get(1)+cMusics.get(2)+cMusics.get(3)+cMusics.get(4));
//                final String music = musics.get(position);
                if (cMusics.get(position).equals(curMusic[0]) && mediaPlayer.isPlaying()) {
                    mediaPlayer.stop();
                    cMusics.remove(cMusics.get(position));
                    Log.e("position", "succ ");
                    MusicAdapter adapter2 = new MusicAdapter(MainActivity.this, R.layout.musiclist, cMusics);
                    ListView listView = findViewById(R.id.list_view);
                    listView.setAdapter(adapter2);
                } else {
                    cMusics.remove(cMusics.get(position));
                    Log.e("position",  "fas ");
                    MusicAdapter adapter2 = new MusicAdapter(MainActivity.this, R.layout.musiclist, cMusics);
                    ListView listView = findViewById(R.id.list_view);
                    listView.setAdapter(adapter2);
                }
                return true;
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                final String music = cMusics.get(i);
                curMusic[0] = music;
                try {
                    try {
                        Log.v(TAG, "start");
                        mediaPlayer.reset();
                        AssetManager assetManager = getAssets();
                        AssetFileDescriptor assetFileDescriptor = assetManager.openFd(music);
                        mediaPlayer.setDataSource(assetFileDescriptor.getFileDescriptor(), assetFileDescriptor.getStartOffset(), assetFileDescriptor.getLength());
                        mediaPlayer.prepare();
                        mediaPlayer.start();
                        buttonPause.setEnabled(true);
                        buttonStop.setEnabled(true);
                        buttonLoop.setEnabled(true);
                        buttonLast.setEnabled(true);
                        buttonNext.setEnabled(true);
                    } catch (IllegalArgumentException e) {
                        e.printStackTrace();
                    } catch (IllegalStateException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    seekBar.setMax(mediaPlayer.getDuration());
                    //拖动进度条事件
                    seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                        @Override
                        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                            if (fromUser) {
                                mediaPlayer.seekTo(seekBar.getProgress());
                            }
                        }

                        @Override
                        public void onStartTrackingTouch(SeekBar seekBar) {

                        }

                        @Override
                        public void onStopTrackingTouch(SeekBar seekBar) {

                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(MainActivity.this, music, Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    //随机播放
    private String ramdom(String music) {
        flag = 1;
        String mu = "";
        int tem = 0;
        do {
            tem = (int) (Math.random() * 10) % (cMusics.size() - 1);
            mu = cMusics.get(tem);
            Log.e("number", String.valueOf(tem));

        } while (cMusics.get(tem).equals(music));
        Log.e("number", mu);
        return mu;
    }

    //顺序播放
    private String theNext(String music) {
        flag = 2;
        String mu = "";
        for (int i = 0; i < cMusics.size(); i++) {
            if (music.equals(cMusics.get(i))) {
                if (i == cMusics.size() - 1) {
                    mu = cMusics.get(0);
                } else {
                    mu = cMusics.get(i + 1);
                }
            }
        }
        return mu;
    }

    private Runnable update_music = new Runnable() {
        @Override
        public void run() {
            try {
                if (mediaPlayer != null) {
                    if (mediaPlayer.isPlaying()) {
                        seekBar.setProgress(mediaPlayer.getCurrentPosition());
                        timeTextView.setText(time.format(mediaPlayer.getCurrentPosition()) + "s" + "/" + time.format(mediaPlayer.getDuration()) + "s");//将给定时间转化为指定格式
                    }
                    music_name.setText("当前正在播放：" + curMusic[0]);//当前正在播放
                    myHandler.postDelayed(update_music, 1000);//UI的更新周期为1s
                }
            } catch (Exception e) {

            }
        }

    };


    private ArrayList<String> getFilesAllName(String path) {

        ArrayList<String> s = new ArrayList<>();
        try {
            AssetManager assetManager = getAssets();
            String[] s2 = assetManager.list(path);
            Log.e("ssss", String.valueOf(s2.length));
            for (int i = 2; i < s2.length; i++) {
                Log.v("ssss", s2[i]);
                s.add(s2[i]);
            }
            return s;

        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (IllegalStateException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return s;

    }

}
