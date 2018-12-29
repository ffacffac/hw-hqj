package com.hw.mqtt;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

public class MainActivity extends AppCompatActivity implements Handler.Callback {

    private static final String TAG = MainActivity.class.getName();
    public static final int MSG_MESSAGE_ARRIVED = 1;//接收到消息
    public static final int MSG_CONNECT_SUCCESS = 2;//连接成功
    public static final int MSG_CONNECT_ERR = 3;//连接失败，重连
    public static final int MSG_DIS_CONNECT = 4;//与服务断开连接
    private static final long DELAY_MILLIS = 10 * 1000;//重连间隔时间
    public static final String HOST = "tcp://192.168.0.137:61613";
    /**
     * 客户端唯一标识，可取android设备的IME+MAC地址
     */
    private static final String CLIENT_ID = "client124";
    /**
     * 订阅的主题保持和服务端一样
     */
    private static final String TOPIC = "test-topic";
    // public static final String TOPIC_SEND = "topicr";// 返回消息的主题
    private static final String USER_NAME = "admin";
    private static final String PASSWORD = "password";
    private TextView tvConnect, tvRevMsg;
    private Handler handler;
    private int reconnect_count;//重连次数
    private MqttClient client;
    private MqttConnectOptions options;
    // private MqttTopic topicSend;
    private ScheduledExecutorService scheduler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tvConnect = findViewById(R.id.tv_connect_result);
        tvRevMsg = findViewById(R.id.tv_msg);
        scheduler = Executors.newSingleThreadScheduledExecutor();
        handler = new Handler(this);
        initMqtt();
        // startReconnect();
        postConnect();
    }

    @Override
    public boolean handleMessage(Message msg) {
        handlerMsg(msg);
        return false;
    }

    private void handlerMsg(Message msg) {
        switch (msg.what) {
            case MSG_MESSAGE_ARRIVED:
                reconnect_count = 0;
                Toast.makeText(MainActivity.this, (String) msg.obj, Toast.LENGTH_SHORT).show();
                String message = (String) msg.obj;
                String data = getDate();
                tvRevMsg.append(data + ":" + "消息：" + message);
                tvRevMsg.append("\n");
                sendBackMsg("我接收到消息：" + message);//返回消息接收到
                handler.removeCallbacks(connectRun);
                break;
            case MSG_CONNECT_SUCCESS:
                reconnect_count = 0;
                System.out.println("连接成功");
                Toast.makeText(MainActivity.this, "连接成功", Toast.LENGTH_SHORT).show();
                tvConnect.append(getDate() + "：" + "连接成功");
                tvConnect.append("\n");
                try {
                    // topicSend = client.getTopic(TOPIC_SEND);
                    client.subscribe(TOPIC, 1);//订阅
                } catch (Exception e) {
                    e.printStackTrace();
                    Log.e(TAG, "handlerMsg: ", e);
                }
                handler.removeCallbacks(connectRun);
                break;
            case MSG_CONNECT_ERR:
                reconnect_count++;
                Toast.makeText(MainActivity.this, "连接失败，系统正在重连", Toast.LENGTH_SHORT).show();
                Log.e(TAG, "handlerMsg: " + "连接失败，系统正在重连，第（" + reconnect_count + "）次");
                String err = getDate() + "：" + "连接失败，系统正在重连，第（" + reconnect_count + "）次";
                tvConnect.setText(err);
                handler.postDelayed(connectRun, DELAY_MILLIS);//重连间隔时间
                break;
            case MSG_DIS_CONNECT:
                tvConnect.append(getDate() + "：" + "与服务器断开连接，尝试重连" + "\n");
                // startReconnect();//与服务器断开，重新尝试连接
                postConnect();//与服务器断开，重新尝试连接
                break;
        }
    }

    // private void startReconnect() {
    //     scheduler.scheduleAtFixedRate(new Runnable() {
    //         @Override
    //         public void run() {
    //             Log.e(TAG, "run: " + "startReconnect()--------------->" + Thread.currentThread().getName());
    //             if (client.isConnected()) return;
    //             syncConnect();
    //         }
    //     }, 0, DELAY_MILLIS, TimeUnit.MILLISECONDS);
    // }

    private void initMqtt() {
        try {
            //host为主机名，test为clientid即连接MQTT的客户端ID，一般以客户端唯一标识符表示，
            // MemoryPersistence设置clientid的保存形式，默认为以内存保存
            client = new MqttClient(HOST, CLIENT_ID, new MemoryPersistence());
            //MQTT的连接设置
            options = new MqttConnectOptions();
            //设置是否清空session,这里如果设置为false表示服务器会保留客户端的连接记录，
            // 这里设置为true表示每次连接到服务器都以新的身份连接
            //设置session是否保留上一条记录
            options.setCleanSession(false);
            //设置连接的用户名
            options.setUserName(USER_NAME);
            //设置连接的密码
            options.setPassword(PASSWORD.toCharArray());
            // 设置超时时间 单位为秒
            options.setConnectionTimeout(10);
            // 设置会话心跳时间 单位为秒 服务器会每隔1.5*20秒的时间向客户端发送个消息判断客户端是否在线，
            // 但这个方法并没有重连的机制
            options.setKeepAliveInterval(20);
            //设置自动重连
            options.setAutomaticReconnect(true);
            //设置回调
            client.setCallback(new MyMqttCallback());
            // MqttTopic topic = client.getTopic(TOPIC);
            // setWill方法，如果项目中需要知道客户端是否掉线,可以调用该方法。设置最终端口的通知消息
            // options.setWill(topic, "close--我掉线啦".getBytes(), 2, true);
        } catch (Exception e) {
            e.printStackTrace();
            Log.e(TAG, "initMqtt: ", e);
        }
    }

    private void sendBackMsg(String src) {
        // try {
        //     MqttDeliveryToken token = topicSend.publish(getSendMsg(src));
        //     token.waitForCompletion();
        //     boolean isComplete = token.isComplete();
        //     if (isComplete) {
        //         tvRevMsg.append("返回消息成功。。。");
        //     } else {
        //         tvRevMsg.append("返回消息失败。。。");
        //     }
        // } catch (MqttException e) {
        //     e.printStackTrace();
        //     Log.e(TAG, "sendBackMsg: ", e);
        // }
    }

    private MqttMessage getSendMsg(String src) {
        MqttMessage mqttMessage = new MqttMessage();
        mqttMessage.setQos(2);
        mqttMessage.setRetained(true);//true：订阅者重新连接时，会收到最后一次的那条消息
        try {
            mqttMessage.setPayload(src.getBytes("UTF-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            Log.e(TAG, "getSendMsg: ", e);
        }
        return mqttMessage;
    }

    private class MyMqttCallback implements MqttCallback {

        @Override
        public void connectionLost(Throwable cause) {
            //连接丢失后，一般在这里面进行重连
            System.out.println("connectionLost----------");
            handler.sendMessage(handler.obtainMessage(MSG_DIS_CONNECT));
        }

        @Override
        public void deliveryComplete(IMqttDeliveryToken token) {
            //publish后会执行到这里
            System.out.println("deliveryComplete---------" + token.isComplete());
        }

        @Override
        public void messageArrived(String topicName, MqttMessage message) throws Exception {
            //subscribe后得到的消息会执行到这里面
            System.out.println("messageArrived----------");
            String revSrc =
                    "主题：" + topicName + "，Qos：" + message.getQos() + "，内容：" + new String(message.getPayload(), "UTF-8");
            Log.e(TAG, "messageArrived: " + revSrc);
            handler.sendMessage(handler.obtainMessage(MSG_MESSAGE_ARRIVED, revSrc));
        }
    }

    private void syncConnect() {
        try {
            client.connect(options);
            handler.sendMessage(handler.obtainMessage(MSG_CONNECT_SUCCESS));
        } catch (Exception e) {
            e.printStackTrace();
            handler.sendMessage(handler.obtainMessage(MSG_CONNECT_ERR));
        }
    }

    private void postConnect() {
        scheduler.execute(connectRun);
    }

    private Runnable connectRun = new Runnable() {
        @Override
        public void run() {
            syncConnect();
        }
    };

    private String getDate() {
        Date date = new Date();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA);
        return format.format(date);
    }

    private void disconnect() {
        try {
            if (handler != null) {
                handler.removeCallbacks(connectRun);
                handler.removeCallbacksAndMessages(null);
            }
            if (client != null) client.disconnect();
            scheduler.shutdown();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        disconnect();
    }
}
