package com.hw.baselibrary.db.been;

import org.xutils.db.annotation.Column;
import org.xutils.db.annotation.Table;

/**
 * @author huangqj
 *         Created by huangqj on 2019-04-10.
 */
@Table(name = "OurFile", onCreated = "CREATE UNIQUE INDEX index_name ON OurFile(fileName,path,lifeDescId,fileType,date)")
public class OurFile {

    @Column(name = "id", isId = true)
    private int id;

    @Column(name = "fileName")
    private String fileName;

    @Column(name = "path")
    private String path;

    // @Column(name = "lifeId")
    // private int lifeId;

    @Column(name = "lifeDescId")
    private int lifeDescId;

    @Column(name = "fileType")
    private int fileType;

    @Column(name = "date")
    private String date;

    private int resId;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public int getFileType() {
        return fileType;
    }

    public void setFileType(int fileType) {
        this.fileType = fileType;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getLifeDescId() {
        return lifeDescId;
    }

    public void setLifeDescId(int lifeDescId) {
        this.lifeDescId = lifeDescId;
    }

    public int getResId() {
        return resId;
    }

    public void setResId(int resId) {
        this.resId = resId;
    }

    @Override
    public String toString() {
        return "OurFile{" + "id=" + id + ", fileName='" + fileName + '\'' + ", path='" + path + '\'' + ", lifeDescId="
                + lifeDescId + ", fileType=" + fileType + ", date='" + date + '\'' + '}';
    }
}
