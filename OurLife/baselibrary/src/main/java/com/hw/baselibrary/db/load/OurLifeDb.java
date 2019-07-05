package com.hw.baselibrary.db.load;

import com.hw.baselibrary.db.been.OurLife;

import org.xutils.DbManager;
import org.xutils.ex.DbException;

/**
 * @author huangqj
 *         Created by huangqj on 2019-04-11.
 */

public class OurLifeDb {

    public static final String ID = "id";
    public static final String DESC = "desc";
    public static final String LIFE_TYPE = "desc";
    public static final String CREATER = "desc";
    public static final String DATE = "desc";
    public static final String TABLE_NAME = "OurLife";
    public static final String ON_CREATE = "CREATE UNIQUE INDEX index_name ON OurFile(desc,lifeType,creater,date)";

    private static OurLifeDb ourLifeDb;

    private OurLifeDb() {
    }

    public static OurLifeDb get() {
        if (ourLifeDb == null) {
            synchronized (OurLifeDb.class) {
                ourLifeDb = new OurLifeDb();
            }
        }
        return ourLifeDb;
    }

    public OurLife getOurLiftByDate(DbManager db, String date) throws DbException {
        return db.selector(OurLife.class).where(DATE, "=", date).findFirst();
    }

    public OurLife save(DbManager db, OurLife ourLife) throws DbException {
        db.saveBindingId(ourLife);
        return ourLife;
    }
}
