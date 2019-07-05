package com.hw.baselibrary.db.load;

/**
 * @author huangqj
 *         Created by huangqj on 2019-04-10.
 */

public class OurLifeDescDb {

    public static final String ID = "id";
    public static final String DESC = "desc";
    public static final String LIFE_ID = "lifeId";
    public static final String TABLE_NAME = "OurLifeDesc";
    public static final String ON_CREATE = "CREATE UNIQUE INDEX index_name ON OurLifeDesc(desc,lifeId)";

}
