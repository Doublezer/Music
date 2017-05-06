package com.example.yday_15_04.connect;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.yday_15_04.R;
import com.example.yday_15_04.entity.Music;
import com.example.yday_15_04.service.MusicService;

import java.util.List;

/**
 * Created by Administrator on 2017/5/5 0005.
 */
public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder>{
    private LayoutInflater layoutInflater;
    private List<Music> list;
    private Context context;
    private TextView textView;
    public RecyclerAdapter(Context context, List<Music> list,TextView textView){
        this.context=context;
        layoutInflater=LayoutInflater.from(context);
        this.list=list;
        this.textView=textView;
    }
    class ViewHolder extends RecyclerView.ViewHolder{
        TextView musicName;
        TextView singerName;
        public ViewHolder(View itemView) {
            super(itemView);
            musicName= (TextView) itemView.findViewById(R.id.item_musicname);
            singerName= (TextView) itemView.findViewById(R.id.item_singername);
        }
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view=layoutInflater.inflate(R.layout.list_item,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        holder.musicName.setText(list.get(position).getMusicName());
        holder.singerName.setText(list.get(position).getSingerName());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent play=new Intent(context,MusicService.class);
                play.putExtra("actionKey",list.get(position).getData());
                context.startService(play);
                textView.setText(list.get(position).getMusicName()+"-"+list.get(position).getSingerName());
            }
        });

    }

    @Override
    public int getItemCount() {
        return list==null?0:list.size();
    }



    public void addMusic(List<Music> params){
        this.list.addAll(params);
    }
}
