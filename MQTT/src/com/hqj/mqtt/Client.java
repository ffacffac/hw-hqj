package com.hqj.mqtt;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.MqttPersistenceException;
import org.eclipse.paho.client.mqttv3.MqttTopic;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

public class Client
{
    public static final String HOST = "tcp://127.0.0.1:61613";
    // public static final String HOST = "tcp://192.168.0.137:61680";
    public static final String TOPIC = "test-topic";
    private static final String clientId = "client124";
    private MqttClient mqttClient;
    private MqttConnectOptions options;
    private String userName = "admin";
    private String password = "password";
    // private String userName = "hqj";
    // private String password = "123456";
    private MqttMessage clientMsg;
    private MqttTopic topic;

    private void start()
    {
	try
	{
	    // host为主机名，clientid即连接MQTT的客户端ID，一般以唯一标识符表示，MemoryPersistence设置clientid的保存形式，默认为以内存保存
	    mqttClient = new MqttClient(HOST, clientId, new MemoryPersistence());
	    // MQTT的连接设置
	    options = new MqttConnectOptions();
	    // 设置是否清空session,这里如果设置为false表示服务器会保留客户端的连接记录，这里设置为true表示每次连接到服务器都以新的身份连接
	    options.setCleanSession(true);
	    // 设置连接的用户名
	    options.setUserName(userName);
	    // 设置连接的密码
	    options.setPassword(password.toCharArray());
	    // 设置超时时间 单位为秒
	    options.setConnectionTimeout(10);
	    // 设置会话心跳时间 单位为秒 服务器会每隔1.5*20秒的时间向客户端发送个消息判断客户端是否在线，但这个方法并没有重连的机制
	    options.setKeepAliveInterval(20);
	    // 设置回调
	    mqttClient.setCallback(new PushCallback());
	    MqttTopic topic = mqttClient.getTopic(TOPIC);
	    // topic = mqttClient.getTopic(TOPIC);
	    // setWill方法，如果项目中需要知道客户端是否掉线,可以调用该方法。设置最终端口的通知消息
	    // options.setWill(topic, "close--我掉线啦".getBytes(), 2, true);
	    // mqttClient.connect();
	    mqttClient.connect(options);
	    // 订阅消息
	    int[] Qos = { 1 };
	    String[] topic1 = { TOPIC };
	    mqttClient.subscribe(topic1, Qos);
	}
	catch (Exception e)
	{
	    e.printStackTrace();
	}
    }

    public static void main(String[] args) throws MqttPersistenceException, MqttException
    {
	Client client = new Client();
	client.start();
	// client.clientMsg = new MqttMessage();
	// client.clientMsg.setQos(2);
	// client.clientMsg.setRetained(true);
	// client.clientMsg.setPayload("客户端来的消息".getBytes());
	// client.publish(client.topic, client.clientMsg);
    }

    public void publish(MqttTopic topic, MqttMessage message)
	    throws MqttPersistenceException, MqttException
    {
	MqttDeliveryToken token = topic.publish(message);
	token.waitForCompletion();
	System.out.println("message is published completely! " + token.isComplete());
	Utils.sysoFG();
    }

    /**
     * 发布消息的回调类
     * 
     * 必须实现MqttCallback的接口并实现对应的相关接口方法CallBack 类将实现 MqttCallBack。
     * 每个客户机标识都需要一个回调实例。在此示例中，构造函数传递客户机标识以另存为实例数据。 在回调中，将它用来标识已经启动了该回调的哪个实例。 必须在回调类中实现三个方法：
     * 
     * public void messageArrived(MqttTopic topic, MqttMessage message)接收已经预订的发布。
     * 
     * public void connectionLost(Throwable cause)在断开连接时调用。
     * 
     * public void deliveryComplete(MqttDeliveryToken token)) 接收到已经发布的 QoS 1 或 QoS 2 消息的传递令牌时调用。 由
     * MqttClient.connect 激活此回调。
     * 
     */
    public class PushCallback implements MqttCallback
    {

	public void connectionLost(Throwable cause)
	{
	    // 连接丢失后，一般在这里面进行重连
	    System.out.println("连接断开，可以做重连");
	    Utils.sysoFG();
	}

	public void deliveryComplete(IMqttDeliveryToken token)
	{
	    System.out.println("deliveryComplete---------" + token.isComplete());
	    Utils.sysoFG();
	}

	public void messageArrived(String topic, MqttMessage message) throws Exception
	{
	    // subscribe后得到的消息会执行到这里面
	    System.out.println("接收消息主题 : " + topic);
	    System.out.println("接收消息Qos : " + message.getQos());
	    System.out.println("接收消息内容 : " + new String(message.getPayload()));
	    Utils.sysoFG();
	}
    }

}
