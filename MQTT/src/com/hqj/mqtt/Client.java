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
	    // hostΪ��������clientid������MQTT�Ŀͻ���ID��һ����Ψһ��ʶ����ʾ��MemoryPersistence����clientid�ı�����ʽ��Ĭ��Ϊ���ڴ汣��
	    mqttClient = new MqttClient(HOST, clientId, new MemoryPersistence());
	    // MQTT����������
	    options = new MqttConnectOptions();
	    // �����Ƿ����session,�����������Ϊfalse��ʾ�������ᱣ���ͻ��˵����Ӽ�¼����������Ϊtrue��ʾÿ�����ӵ������������µ��������
	    options.setCleanSession(true);
	    // �������ӵ��û���
	    options.setUserName(userName);
	    // �������ӵ�����
	    options.setPassword(password.toCharArray());
	    // ���ó�ʱʱ�� ��λΪ��
	    options.setConnectionTimeout(10);
	    // ���ûỰ����ʱ�� ��λΪ�� ��������ÿ��1.5*20���ʱ����ͻ��˷��͸���Ϣ�жϿͻ����Ƿ����ߣ������������û�������Ļ���
	    options.setKeepAliveInterval(20);
	    // ���ûص�
	    mqttClient.setCallback(new PushCallback());
	    MqttTopic topic = mqttClient.getTopic(TOPIC);
	    // topic = mqttClient.getTopic(TOPIC);
	    // setWill�����������Ŀ����Ҫ֪���ͻ����Ƿ����,���Ե��ø÷������������ն˿ڵ�֪ͨ��Ϣ
	    // options.setWill(topic, "close--�ҵ�����".getBytes(), 2, true);
	    // mqttClient.connect();
	    mqttClient.connect(options);
	    // ������Ϣ
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
	// client.clientMsg.setPayload("�ͻ���������Ϣ".getBytes());
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
	    // subscribe��õ�����Ϣ��ִ�е�������
	    System.out.println("������Ϣ���� : " + topic);
	    System.out.println("������ϢQos : " + message.getQos());
	    System.out.println("������Ϣ���� : " + new String(message.getPayload()));
	    Utils.sysoFG();
	}
    }

}
