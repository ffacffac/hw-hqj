package szu.wifichat.android.groundbeen;

import java.io.Serializable;

/**
 * Created by huangqj on 2017-06-08.
 */

public class Device implements Serializable {

    private int id;
    private int stepId;
    private String name;
    private int state;
    private String label;
    private String imgPath = "";
    private boolean isRefuseByLeader = false;
    private String refuse = "";
    private boolean isTitle;
    private boolean isCanSendAffirm;//是否可以发送确认杆号

    private String KM;//公里标
    private String groundPosition;//接挂位置

    public Device() {
    }

    public Device(int id, String name, int state, String label) {
        this.id = id;
        this.name = name;
        this.state = state;
        this.label = label;
    }

    public boolean isCanSendAffirm() {
        return isCanSendAffirm;
    }

    public void setCanSendAffirm(boolean canSendAffirm) {
        isCanSendAffirm = canSendAffirm;
    }

    public String getKM() {
        return KM;
    }

    public void setKM(String KM) {
        this.KM = KM;
    }

    public String getGroundPosition() {
        return groundPosition;
    }

    public void setGroundPosition(String groundPosition) {
        this.groundPosition = groundPosition;
    }

    public boolean isTitle() {
        return isTitle;
    }

    public void setTitle(boolean title) {
        isTitle = title;
    }

    public String getRefuse() {
        return refuse;
    }

    public void setRefuse(String refuse) {
        this.refuse = refuse;
    }

    public boolean isRefuseByLeader() {
        return isRefuseByLeader;
    }

    public void setRefuseByLeader(boolean refuseByLeader) {
        isRefuseByLeader = refuseByLeader;
    }

    public String getImgPath() {
        return imgPath;
    }

    public void setImgPath(String imgPath) {
        this.imgPath = imgPath;
    }

    public int getStepId() {
        return stepId;
    }

    public void setStepId(int stepId) {
        this.stepId = stepId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    @Override
    public String toString() {
        return "Device{" +
                "id=" + id +
                ", stepId=" + stepId +
                ", name='" + name + '\'' +
                ", state=" + state +
                ", label='" + label + '\'' +
                ", imgPath='" + imgPath + '\'' +
                ", isRefuseByLeader=" + isRefuseByLeader +
                ", refuse='" + refuse + '\'' +
                ", isTitle=" + isTitle +
                ", KM='" + KM + '\'' +
                ", groundPosition='" + groundPosition + '\'' +
                '}';
    }
}
