package szu.wifichat.android.httputils;

import java.util.Arrays;

/**
 * Created by huangqj on 2017-06-19.
 */

public class GroundState {

    private String[] GroundNums;
    private int[] ClickPoint;
    private int Confirm;
    private int AddDown;
    private int[] Add;
    private int AddConfirm;
    private int DelDown;
    private int[] Del;
    private int DelConfirm;
    private String WorkContent;
    private String WorkPlace;
    private String Workers;

    public GroundState() {
    }

    public int[] getAdd() {
        return Add;
    }

    public void setAdd(int[] add) {
        Add = add;
    }

    public int getAddDown() {
        return AddDown;
    }

    public void setAddDown(int addDown) {
        AddDown = addDown;
    }

    public int[] getClickPoint() {
        return ClickPoint;
    }

    public void setClickPoint(int[] clickPoint) {
        ClickPoint = clickPoint;
    }

    public int getConfirm() {
        return Confirm;
    }

    public void setConfirm(int confirm) {
        Confirm = confirm;
    }

    public int[] getDel() {
        return Del;
    }

    public void setDel(int[] del) {
        Del = del;
    }

    public int getDelConfirm() {
        return DelConfirm;
    }

    public void setDelConfirm(int delConfirm) {
        DelConfirm = delConfirm;
    }

    public int getDelDown() {
        return DelDown;
    }

    public void setDelDown(int delDown) {
        DelDown = delDown;
    }

    public String[] getGroundNums() {
        return GroundNums;
    }

    public void setGroundNums(String[] groundNums) {
        GroundNums = groundNums;
    }

    public String getWorkContent() {
        return WorkContent;
    }

    public void setWorkContent(String workContent) {
        WorkContent = workContent;
    }

    public String getWorkPlace() {
        return WorkPlace;
    }

    public void setWorkPlace(String workPlace) {
        WorkPlace = workPlace;
    }

    public String getWorkers() {
        return Workers;
    }

    public void setWorkers(String workers) {
        Workers = workers;
    }

    public int getAddConfirm() {
        return AddConfirm;
    }

    public void setAddConfirm(int addConfirm) {
        AddConfirm = addConfirm;
    }

    @Override
    public String toString() {
        return "GroundState{" +
                "GroundNums=" + Arrays.toString(GroundNums) +
                ", ClickPoint=" + Arrays.toString(ClickPoint) +
                ", Confirm=" + Confirm +
                ", AddDown=" + AddDown +
                ", Add=" + Arrays.toString(Add) +
                ", AddConfirm=" + AddConfirm +
                ", DelDown=" + DelDown +
                ", Del=" + Arrays.toString(Del) +
                ", DelConfirm=" + DelConfirm +
                ", WorkContent='" + WorkContent + '\'' +
                ", WorkPlace='" + WorkPlace + '\'' +
                ", Workers='" + Workers + '\'' +
                '}';
    }
}
