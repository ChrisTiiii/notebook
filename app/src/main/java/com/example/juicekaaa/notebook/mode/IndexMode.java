package com.example.juicekaaa.notebook.mode;

/**
 * Created by Juicekaaa on 2017/10/25.
 */

public class IndexMode {
    private String content;
    private String time;
    private int _id;
    private String title;
    private String address;
    private String type;


    public IndexMode(String content, String time, String title, String address) {
        this.content = content;
        this.time = time;
        this.title = title;
        this.address = address;
    }

    public IndexMode(String time, String title) {
        this.time = time;
        this.title = title;
    }

    public IndexMode() {
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }


    public int get_id() {
        return _id;
    }

    public void set_id(int _id) {
        this._id = _id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
