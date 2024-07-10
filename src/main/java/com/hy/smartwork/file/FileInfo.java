package com.hy.smartwork.file;

import java.io.Serializable;

public class FileInfo implements Serializable {

    private String id;
    private String url;
    private String key;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    @Override
    public String toString() {
        return "FileInfo{" +
                "id='" + id + '\'' +
                ", url='" + url + '\'' +
                ", key='" + key + '\'' +
                '}';
    }
}
