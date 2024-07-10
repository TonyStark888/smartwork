package com.hy.smartwork.file;

import java.io.Serializable;

public class FileResponse implements Serializable {
    private FileInfo data;

    public FileInfo getData() {
        return data;
    }

    public void setData(FileInfo data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "FileResponse{" +
                "data=" + data +
                '}';
    }
}
