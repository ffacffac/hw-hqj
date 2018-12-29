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
    public static final String TOPIC_SEND = "test-topic";// ������ȥ������
    // public static final String TOPIC_RECEIVE = "topicr";// ���յ�����
    private static final String CLIENT_ID = "server";

    private static Server server;
    private MqttClient client;
    private MqttTopic topicSend;
    // private MqttTopic topicReceive;// ���Զ������������⣬����ͬ�����ⷢ����Ϣ
    private String userName = "admin";
    private String password = "password";

    private MqttMessage message;

    // UI
    private Button btnSend = new Button("����");
    private Button close = new Button("�ر�");
    private JTextField tfMsg = new JTextField("�������Ϣ");
    private JTextField tfRevMsg = new JTextField();

    public Server() throws MqttException
    {
	// MemoryPersistence����clientid�ı�����ʽ��Ĭ��Ϊ���ڴ汣��
	client = new MqttClient(HOST, CLIENT_ID, new MemoryPersistence());
	connect();
    }

    public void connect()
    {
	MqttConnectOptions options = new MqttConnectOptions();
	options.setCleanSession(false);
	options.setUserName(userName);
	options.setPassword(password.toCharArray());
	// ���ó�ʱʱ��,��λΪ��
	options.setConnectionTimeout(10);
	// ���ûỰ����ʱ��
	options.setKeepAliveInterval(20);
	options.setAutomaticReconnect(true);// �����Զ�����
	client.setCallback(new PushCallback());
	try
	{
	    client.connect(options);
	    topicSend = client.getTopic(TOPIC_SEND);// ��ȡ�������⣬���Զ�������������

	    // topicReceive = client.getTopic(TOPIC_RECEIVE);
	    // client.subscribe(TOPIC_RECEIVE, 2);// ��������
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
	    // server.message = server.getMsg("�����������Ϣ");
	    // server.publish(server.topic, server.message);
	    // System.out.println(server.message.isRetained() + "------ratained״̬");
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
	msg.setQos(2);// be delivered once��ֻ����һ��
	msg.setRetained(true);// true�����Ķ��������Ӻ󣬻��յ����һ����Ϣ��false�����Ķ��������Ӻ󲻻��յ�
	// ���ĵĻ���������msgҪ��������ʽ����Ȼandroid�˽���������
	msg.setPayload(src.getBytes("UTF-8"));
	return msg;
    }

    private void initLayout()
    {
	this.setTitle("MQTT��ʱͨѶ");
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
     * ������Ϣ�Ļص���
     * 
     * ����ʵ��MqttCallback�Ľӿڲ�ʵ�ֶ�Ӧ����ؽӿڷ���CallBack �ཫʵ�� MqttCallBack��
     * ÿ���ͻ�����ʶ����Ҫһ���ص�ʵ�����ڴ�ʾ���У����캯�����ݿͻ�����ʶ�����Ϊʵ�����ݡ� �ڻص��У�����������ʶ�Ѿ������˸ûص����ĸ�ʵ���� �����ڻص�����ʵ������������
     * 
     * public void messageArrived(MqttTopic topic, MqttMessage message)�����Ѿ�Ԥ���ķ�����
     * 
     * public void connectionLost(Throwable cause)�ڶϿ�����ʱ���á�
     * 
     * public void deliveryComplete(MqttDeliveryToken token)) ���յ��Ѿ������� QoS 1 �� QoS 2 ��Ϣ�Ĵ�������ʱ���á� ��
     * MqttClient.connect ����˻ص���
     * 
     */
    public class PushCallback implements MqttCallback
    {

	public void connectionLost(Throwable cause)
	{
	    // ���Ӷ�ʧ��һ�����������������
	    System.out.println("���ӶϿ�������������");
	    Utils.sysoFG();
	}

	public void deliveryComplete(IMqttDeliveryToken token)
	{
	    System.out.println("deliveryComplete---------" + token.isComplete());
	    Utils.sysoFG();
	}

	public void messageArrived(String topic, MqttMessage message) throws Exception
	{
	    String rev = "���յ���Ϣ��" + "���� : " + topic + "��Qos��" + message.getQos() + "�����ݣ�"
		    + new String(message.getPayload(), "UTF-8");
	    tfRevMsg.setText(rev);
	    // subscribe��õ�����Ϣ��ִ�е�������
	    System.out.println("������Ϣ���� : " + topic);
	    System.out.println("������ϢQos : " + message.getQos());
	    System.out.println("������Ϣ���� : " + new String(message.getPayload(), "UTF-8"));
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
