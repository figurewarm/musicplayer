package com.example.musicplayer;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class MusicAdapter extends ArrayAdapter<String> {
    private int resourceId;
    private ArrayList<String> musicList;

    public MusicAdapter(Context context, int resource, ArrayList<String> objects) {
        super(context, resource, objects);
        musicList = objects;
        resourceId = resource;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        String musicName = getItem(position);
        View view = LayoutInflater.from(getContext()).inflate(resourceId, parent, false);
        TextView musicList = view.findViewById(R.id.musicList);
        if (musicName != null)
            musicList.setText(musicName);
        return view;
    }

}
