package com.hw.baselibrary.db.been;

import com.hw.baselibrary.db.load.OurLifeDescDb;

import org.xutils.db.annotation.Column;
import org.xutils.db.annotation.Table;

/**
 * @author huangqj
 *         Created by huangqj on 2019-04-10.
 */

@Table(name = OurLifeDescDb.TABLE_NAME, onCreated = OurLifeDescDb.ON_CREATE)
public class OurLifeDesc {

    @Column(name = OurLifeDescDb.ID, isId = true)
    private int id;

    @Column(name = OurLifeDescDb.DESC)
    private String desc;

    @Column(name = OurLifeDescDb.LIFE_ID)
    private int lifeId;

    private OurFile file;

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

    public int getLifeId() {
        return lifeId;
    }

    public void setLifeId(int lifeId) {
        this.lifeId = lifeId;
    }

    public OurFile getFile() {
        return file;
    }

    public void setFile(OurFile file) {
        this.file = file;
    }

    @Override
    public String toString() {
        return "OurLifeDesc{" + "id=" + id + ", desc='" + desc + '\'' + ", lifeId=" + lifeId + '}';
    }
}
