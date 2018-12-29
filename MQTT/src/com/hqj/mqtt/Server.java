package com.hqj.mqtt;

import java.awt.Button;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.UnsupportedEncodingException;

import javax.swing.JFrame;
import javax.swing.JTextField;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.MqttPersistenceException;
import org.eclipse.paho.client.mqttv3.MqttSecurityException;
import org.eclipse.paho.client.mqttv3.MqttTopic;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

public class Server extends JFrame implements ActionListener
{
    private static final long serialVersionUID = 3104020356210645663L;
    public static final String HOST = "tcp://127.0.0.1:61613";
    public static final String TOPIC_SEND = "test-topic";// 发布出去的主题
    // public static final String TOPIC_RECEIVE = "topicr";// 接收的主题
    private static final String CLIENT_ID = "server";

    private static Server server;
    private MqttClient client;
    private MqttTopic topicSend;
    // private MqttTopic topicReceive;// 可以定义多个订阅主题，并向不同的主题发送消息
    private String userName = "admin";
    private String password = "password";

    private MqttMessage message;

    // UI
    private Button btnSend = new Button("发送");
    private Button close = new Button("关闭");
    private JTextField tfMsg = new JTextField("服务端消息");
    private JTextField tfRevMsg = new JTextField();

    public Server() throws MqttException
    {
	// MemoryPersistence设置clientid的保存形式，默认为以内存保存
	client = new MqttClient(HOST, CLIENT_ID, new MemoryPersistence());
	connect();
    }

    public void connect()
    {
	MqttConnectOptions options = new MqttConnectOptions();
	options.setCleanSession(false);
	options.setUserName(userName);
	options.setPassword(password.toCharArray());
	// 设置超时时间,单位为秒
	options.setConnectionTimeout(10);
	// 设置会话心跳时间
	options.setKeepAliveInterval(20);
	options.setAutomaticReconnect(true);// 设置自动重连
	client.setCallback(new PushCallback());
	try
	{
	    client.connect(options);
	    topicSend = client.getTopic(TOPIC_SEND);// 获取定义主题，可以定义多个订阅主题

	    // topicReceive = client.getTopic(TOPIC_RECEIVE);
	    // client.subscribe(TOPIC_RECEIVE, 2);// 订阅主题
	}
	catch (MqttSecurityException e)
	{
	    e.printStackTrace();
	}
	catch (MqttException e)
	{
	    e.printStackTrace();
	}
    }

    public void publish(MqttTopic topic, MqttMessage message)
	    throws MqttPersistenceException, MqttException
    {
	MqttDeliveryToken token = topic.publish(message);
	token.waitForCompletion();
	System.out.println("message is published completely! " + token.isComplete());
	// fal: while (true)
	// {
	// MqttDeliveryToken newToken = topic.publish(message);
	// boolean isComplete = token.isComplete();
	// if (!isComplete)
	// {
	// token.waitForCompletion(1000);
	// }
	// else
	// {
	// break fal;
	// }
	// }
    }

    public static void main(String[] args)
    {
	try
	{
	    server = new Server();
	    server.initLayout();
	    // server.message = server.getMsg("服务端来的消息");
	    // server.publish(server.topic, server.message);
	    // System.out.println(server.message.isRetained() + "------ratained状态");
	    // Utils.sysoFG();
	}
	catch (MqttException e)
	{
	    e.printStackTrace();
	}
    }

    private MqttMessage getMsg(String src) throws UnsupportedEncodingException
    {
	MqttMessage msg = new MqttMessage();
	// msg.setQos(0);// be delivered at most once (zero or one times)
	// msg.setQos(1);// be delivered at least once (one or more times)
	msg.setQos(2);// be delivered once，只发送一次
	msg.setRetained(true);// true：订阅端重新连接后，会收到最后一条消息；false：订阅端重新连接后不会收到
	// 中文的话，发布的msg要定义编码格式，不然android端解析是乱码
	msg.setPayload(src.getBytes("UTF-8"));
	return msg;
    }

    private void initLayout()
    {
	this.setTitle("MQTT即时通讯");
	this.setLayout(null);
	this.setBounds(100, 100, 300, 300);

	this.add(btnSend);
	btnSend.setBounds(200, 200, 80, 50);
	btnSend.addActionListener(this);

	this.add(close);
	close.setBounds(230, 10, 50, 50);
	close.addActionListener(this);

	this.add(tfMsg);
	tfMsg.setBounds(20, 10, 200, 80);

	this.add(tfRevMsg);
	tfRevMsg.setBounds(20, 100, 200, 80);

	this.setVisible(true);
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
	    String rev = "接收到消息：" + "主题 : " + topic + "，Qos：" + message.getQos() + "，内容："
		    + new String(message.getPayload(), "UTF-8");
	    tfRevMsg.setText(rev);
	    // subscribe后得到的消息会执行到这里面
	    System.out.println("接收消息主题 : " + topic);
	    System.out.println("接收消息Qos : " + message.getQos());
	    System.out.println("接收消息内容 : " + new String(message.getPayload(), "UTF-8"));
	    Utils.sysoFG();
	}
    }

    @Override
    public void actionPerformed(ActionEvent e)
    {
	if (e.getSource() == btnSend)
	{
	    try
	    {
		String src = tfMsg.getText();
		server.publish(server.topicSend, server.getMsg(src));
	    }
	    catch (Exception e1)
	    {
		e1.printStackTrace();
	    }
	}
	else if (e.getSource() == close)
	{
	    if (client != null) try
	    {
		client.disconnect();
	    }
	    catch (MqttException e1)
	    {
		e1.printStackTrace();
	    }
	    this.setVisible(false);
	    System.exit(0);
	}
    }
}
