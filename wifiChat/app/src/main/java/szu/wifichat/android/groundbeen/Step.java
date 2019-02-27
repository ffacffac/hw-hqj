package szu.wifichat.android.groundbeen;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by huangqj on 2017-06-13.
 */

public class Step implements Serializable{

    private int id;
    private String name;
    private int state;
    private ArrayList<Device> devices;

    public Step(int id, String name, int state) {
        this.name = name;
        this.state = state;
        this.id = id;
    }

    public Step() {
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

    public ArrayList<Device> getDevices() {
        return devices;
    }

    public void setDevices(ArrayList<Device> devices) {
        this.devices = devices;
    }

    @Override
    public String toString() {
        return "Step{" +
                "name='" + name + '\'' +
                ", state=" + state +
                '}';
    }
}
