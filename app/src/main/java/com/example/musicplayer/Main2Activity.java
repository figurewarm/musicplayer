package com.example.musicplayer;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;

public class Main2Activity extends AppCompatActivity {

    private ArrayList<String> message =new ArrayList<>();
    private int co = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        final ArrayList<String> musics = getFilesAllName("");

        Button turnTo = findViewById(R.id.turnTo);
        turnTo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Main2Activity.this, MainActivity.class);
                intent.putExtra("values", message);
                startActivityForResult(intent, 1001);
            }
        });
        final MusicAdapter adapter2 = new MusicAdapter(Main2Activity.this, R.layout.musiclist, musics);
        ListView listView = findViewById(R.id.list_view2);
        listView.setAdapter(adapter2);

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                String m = musics.get(position);
                Log.e("meemem",m+"1111");
                message.add(co,m);
                co = co +1;
                Toast.makeText(Main2Activity.this, "添加成功", Toast.LENGTH_SHORT).show();
                return true;
            }
        });
    }


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
