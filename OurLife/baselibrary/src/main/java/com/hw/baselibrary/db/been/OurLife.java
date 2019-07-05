package com.hw.baselibrary.db.been;

import com.hw.baselibrary.db.load.OurLifeDb;

import org.xutils.db.annotation.Column;
import org.xutils.db.annotation.Table;

import java.util.List;

/**
 * @author huangqj
 *         Created by huangqj on 2019-04-10.
 */

@Table(name = OurLifeDb.TABLE_NAME, onCreated = OurLifeDb.ON_CREATE)
public class OurLife {

    // @Column(name = "id", isId = true)
    // private int id;
    //
    // @Column(name = "desc")
    // private String desc;
    //
    // @Column(name = "lifeType")
    // private int lifeType;
    //
    // @Column(name = "creater")
    // private String creater;
    //
    // @Column(name = "date")
    // private String date;

    @Column(name = OurLifeDb.ID, isId = true)
    private int id;

    @Column(name = OurLifeDb.DESC)
    private String desc;

    @Column(name = OurLifeDb.LIFE_TYPE)
    private int lifeType;

    @Column(name = OurLifeDb.CREATER)
    private String creater;

    @Column(name = OurLifeDb.DATE)
    private String date;

    private List<OurLifeDesc> descList;

    public List<OurLifeDesc> getDescList() {
        return descList;
    }

    public void setDescList(List<OurLifeDesc> descList) {
        this.descList = descList;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public int getLifeType() {
        return lifeType;
    }

    public void setLifeType(int lifeType) {
        this.lifeType = lifeType;
    }

    public String getCreater() {
        return creater;
    }

    public void setCreater(String creater) {
        this.creater = creater;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    @Override
    public String toString() {
        return "OurLife{" + "id=" + id + ", desc='" + desc + '\'' + ", lifeType=" + lifeType + ", creater='" + creater
                + '\'' + ", date='" + date + '\'' + '}';
    }
}
