package com.hw.baselibrary.db;

import com.hw.baselibrary.constant.Constant;

import org.xutils.DbManager;
import org.xutils.x;

import java.io.File;

/**
 * @author huangqj
 *         Created by huangqj on 2019-04-10.
 */

public class DbUtils {

    private static DbManager.DaoConfig daoConfig;

    public synchronized static DbManager.DaoConfig getDaoConfig() {
        if (daoConfig != null) {
            return daoConfig;
        }
        daoConfig = new DbManager.DaoConfig().setDbName(Constant.DB_NAME).setDbDir(new File(Constant.DB_DIR))
                                             .setDbVersion(1).setDbOpenListener(db -> {
                    // 开启WAL, 对写入加速提升巨大
                    db.getDatabase().enableWriteAheadLogging();
                }).setDbUpgradeListener((db, oldVersion, newVersion) -> {
                });
        return daoConfig;
    }

    public static DbManager getDbManager(DbManager.DaoConfig daoConfig) {
        return x.getDb(daoConfig);
    }
}
