package com.hw.baselibrary.constant;

/**
 * Created by huangqj on 2019-04-11.
 */

public class Eenum {


    public enum LifeFileType {
        /**
         * 附件类型
         */
        FILE_IMG("图片", 0), RES("项目资源", 1), FILE_VIDEO("视频", 0), FILE_RECORD("录音", 1);;
        public String desc;
        public int value;

        LifeFileType(String desc, int value) {
            this.desc = desc;
            this.value = value;
        }
    }
}
