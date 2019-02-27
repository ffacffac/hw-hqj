package szu.wifichat.android.groundbeen;

import java.io.Serializable;

/**
 * Created by huangqj on 2017-06-13.
 */

public class StepAndDevice implements Serializable {

    private Step step;
    private Device device;

    public Step getStep() {
        return step;
    }

    public void setStep(Step step) {
        this.step = step;
    }

    public Device getDevice() {
        return device;
    }

    public void setDevice(Device device) {
        this.device = device;
    }

    //    private int stepId = -1;
//    private String stepName;
//    private int stepState;
//
//    private int deviceId;
//    private String deviceName;
//    private int deviceState;
//    private boolean isTitle;
//    private String deviceLabel;
//
//    public int getStepId() {
//        return stepId;
//    }
//
//    public void setStepId(int stepId) {
//        this.stepId = stepId;
//    }
//
//    public String getStepName() {
//        return stepName;
//    }
//
//    public void setStepName(String stepName) {
//        this.stepName = stepName;
//    }
//
//    public int getStepState() {
//        return stepState;
//    }
//
//    public void setStepState(int stepState) {
//        this.stepState = stepState;
//    }
//
//    public int getDeviceId() {
//        return deviceId;
//    }
//
//    public void setDeviceId(int deviceId) {
//        this.deviceId = deviceId;
//    }
//
//    public String getDeviceName() {
//        return deviceName;
//    }
//
//    public void setDeviceName(String deviceName) {
//        this.deviceName = deviceName;
//    }
//
//    public int getDeviceState() {
//        return deviceState;
//    }
//
//    public void setDeviceState(int deviceState) {
//        this.deviceState = deviceState;
//    }
//
//    public boolean isTitle() {
//        return isTitle;
//    }
//
//    public void setTitle(boolean title) {
//        isTitle = title;
//    }
//
//    public String getDeviceLabel() {
//        return deviceLabel;
//    }
//
//    public void setDeviceLabel(String deviceLabel) {
//        this.deviceLabel = deviceLabel;
//    }
//
//    @Override
//    public String toString() {
//        return "StepAndDevice{" +
//                "stepId=" + stepId +
//                ", stepName='" + stepName + '\'' +
//                ", stepState=" + stepState +
//                ", deviceId=" + deviceId +
//                ", deviceName='" + deviceName + '\'' +
//                ", deviceState=" + deviceState +
//                ", isTitle=" + isTitle +
//                '}';
//    }
}
