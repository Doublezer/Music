package com.example.yday_15_04.entity;

/**
 * Created by Administrator on 2017/5/5 0005.
 */
public class Music {
    private String musicName;
    private String singerName;
    private String data;

    public String getMusicName() {
        return musicName;
    }

    public void setMusicName(String musicName) {
        this.musicName = musicName;
    }

    public String getSingerName() {
        return singerName;
    }

    public void setSingerName(String singerName) {
        this.singerName = singerName;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public Music(String musicName, String singerName, String data) {

        this.musicName = musicName;
        this.singerName = singerName;
        this.data = data;
    }

    public Music(String musicName, String singerName) {

        this.musicName = musicName;
        this.singerName = singerName;
    }
}
