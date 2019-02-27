package szu.wifichat.android.conf;

/**
 * Created by huangqj on 2017-06-08.
 */

public class WorkEnum {

    /**
     * 步骤的状态
     */
    public enum StepState {

        NotExecutable(0, "不可操作"),
        /**
         * 进行中
         */
        WnderWay(1, "进行中"),
        /**
         * 完成
         */
        Finish(2, "完成");

        private int state;
        private String name;

        private StepState(int state, String name) {
            this.state = state;
            this.name = name;
        }

        public int getState() {
            return state;
        }

        public String getName() {
            return name;
        }
    }

    /**
     * 设备杆号的状态
     */
    public enum DeviceState {

        Normal(0, "正常"),
        /**
         * 进行中
         */
        WnderWay(1, "进行中"),
        /**
         * 完成
         */
        Finish(2, "完成"),
        /**
         * 错误、警告
         */
        Error(3, "警告或错误");

        private int state;
        private String name;

        private DeviceState(int state, String name) {
            this.state = state;
            this.name = name;
        }

        public int getState() {
            return state;
        }

        public String getName() {
            return name;
        }
    }

    /**
     * 用户角色
     */
    public enum UserRole {

        Grounder(1, "接地线人员"),
        /**
         * 完成
         */
        Leader(2, "工作领导人"),
        /**
         * 错误、警告
         */
        Other(3, "调度人员");

        private int role;
        private String name;

        private UserRole(int role, String name) {
            this.role = role;
            this.name = name;
        }

        public int getState() {
            return role;
        }

        public String getName() {
            return name;
        }
    }

    /**
     * 代表哪款PDA，深版或联欣
     */
    public enum PdaType
    {
        /**
         * 深版
         */
        SB((String) "SB"),
        /**
         * 联欣
         */
        LX((String) "LX");

        private String value;

        private PdaType(String value)
        {
            this.value = value;
        }

        public String getValue()
        {
            return value;
        }
    }
}
