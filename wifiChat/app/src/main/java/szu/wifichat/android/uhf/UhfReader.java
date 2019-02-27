package szu.wifichat.android.uhf;

import com.android.hdhe.uhf.reader.SerialPort;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;


public class UhfReader implements CommendManager {

    private static NewSendCommendManager manager;// ////////

    // private static SendCommendManager manager ;

    private static SerialPort serialPort;// 串口

    private static int port = 13;// 串口号，手持机的是13，平板的是12

    private static int baudRate = 115200;// 波特率

    private static InputStream in;// 串口输入流

    private static OutputStream os;// 串口输出流

    private UhfReader() {
    }

    ;

    private static UhfReader reader;

    // private UhfReader(InputStream input, OutputStream output){
    // manager = new NewSendCommendManager(input, output);
    // }
    /*
     * 深圳宝嘉
     */
    // public static UhfReader getInstance(InputStream input, OutputStream
    // output){
    // if(reader == null){
    // reader = new UhfReader(input, output);
    // }
    // return reader;
    // }

    public static UhfReader getInstance() {
        if (serialPort == null) {
            try {
                serialPort = new SerialPort(port, baudRate, 0);
            } catch (Exception e) {
                return null;
            }
            serialPort.psampoweron();
            in = serialPort.getInputStream();
            os = serialPort.getOutputStream();
        }
        if (manager == null) {
            manager = new NewSendCommendManager(in, os);
            // manager = new SendCommendManager(in, os);
        }
        if (reader == null) {
            reader = new UhfReader();
        }
        return reader;
    }

    public void powerOn() {
        serialPort.psampoweron();
    }

    public void powerOff() {
        serialPort.psampoweroff();
    }

    @Override
    public boolean setBaudrate() {

        return manager.setBaudrate();
    }

    @Override
    public byte[] getFirmware() {

        return manager.getFirmware();
    }

    @Override
    public boolean setOutputPower(int value) {
        return manager.setOutputPower(value);
    }

    @Override
    public List<byte[]> inventoryRealTime() {

        return manager.inventoryRealTime();
    }

    @Override
    public void selectEPC(byte[] epc) {
        manager.selectEPC(epc);

    }

    @Override
    public byte[] readFrom6C(int memBank, int startAddr, int length, byte[] accessPassword) {

        return manager.readFrom6C(memBank, startAddr, length, accessPassword);
    }

    @Override
    public boolean writeTo6C(byte[] password, int memBank, int startAddr, int dataLen, byte[] data) {
        return manager.writeTo6C(password, memBank, startAddr, dataLen, data);
    }

    @Override
    public void setSensitivity(int value) {
        manager.setSensitivity(value);

    }

    @Override
    public boolean lock6C(byte[] password, int memBank, int lockType) {
        return manager.lock6C(password, memBank, lockType);
    }

    @Override
    public void close() {
        if (manager != null) {
            manager.close();
            manager = null;
        }

        if (serialPort != null) {
            serialPort.psampoweroff();
            serialPort.close(port);
            serialPort = null;
        }
        if (reader != null) {
            reader = null;
        }
    }

    @Override
    public byte checkSum(byte[] data) {
        return 0;
    }

    @Override
    public int setFrequency(int startFrequency, int freqSpace, int freqQuality) {

        return manager.setFrequency(startFrequency, freqSpace, freqQuality);
    }

    public void setDistance(int distance) {

    }

    public void close(InputStream input, OutputStream output) {
        if (manager != null) {
            manager = null;
            try {
                input.close();
                output.close();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

        }
    }

    /**
     * 新版API 设置工作地区
     *
     * @param area
     * @return
     */
    public int setWorkArea(int area) {

        return manager.setWorkArea(area);
    }

    /**
     * 新版API 多标签盘存
     */
    public List<byte[]> inventoryMulti() {

        return manager.inventoryMulti();
    }

    /**
     * 新版API 停止多标签盘存
     */
    public void stopInventoryMulti() {
        manager.stopInventoryMulti();
    }

    /**
     * 新版API 读取频段
     */
    public int getFrequency() {
        return manager.getFrequency();
    }

    /**
     * 新版API 取消选择
     */
    public int unSelect() {
        return manager.unSelectEPC();
    }

    /**
     * 新版API
     *
     * @param mixer_g ,混频器增益(mixer_g的范围为0~6)
     * @param if_g    ,中频放大器增益(if_g的范围为0~7)
     * @param trd     ,信号解调阀值（trd越大距离越近，越小距离越远，范围0x01b0(432)~0x0360(864)）
     */
    public void setRecvParam(int mixer_g, int if_g, int trd) {
        manager.setRecvParam(mixer_g, if_g, trd);
    }
}
