package szu.wifichat.android.uhf;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import android.util.Log;

// import com.android.hdhe.uhf.util.Tools;

public class NewSendCommendManager implements CommendManager
{

	private InputStream in;
	private OutputStream out;
	private final byte HEAD = (byte) 0xBB;
	private final byte END = (byte) 0x7E;
	public static final byte RESPONSE_OK = 0x00; // 响应帧0K
	/**
	 * 访问失败，密码错误
	 */
	public static final byte ERROR_CODE_ACCESS_FAIL = 0x16;
	/**
	 * 天线区域无卡片或EPC代码错误
	 */
	public static final byte ERROR_CODE_NO_CARD = 0x09;
	/**
	 * 读数据时，起始偏移量或数据长度超过数据存储区
	 */
	public static final byte ERROR_CODE_READ_SA_OR_LEN_ERROR = (byte) 0xA3;
	/**
	 * 写数据时，起始偏移量或数据长度超过数据存储区
	 */
	public static final byte ERROR_CODE_WRITE_SA_OR_LEN_ERROR = (byte) 0xB3;
	/**
	 * 灵敏度高
	 */
	public static final int SENSITIVE_HIHG = 3;
	/**
	 * 灵敏度中
	 */
	public static final int SENSITIVE_MIDDLE = 2;
	/**
	 * 灵敏度低
	 */
	public static final int SENSITIVE_LOW = 1;
	/**
	 * 灵敏度极低
	 */
	public static final int SENSITIVE_VERY_LOW = 0;

	public static final int LOCK_TYPE_OPEN = 0; // 开放
	public static final int LOCK_TYPE_PERMA_OPEN = 1; // 永久开放
	public static final int LOCK_TYPE_LOCK = 2; // 锁定
	public static final int LOCK_TYPE_PERMA_LOCK = 3; // 永久锁定
	public static final int LOCK_MEM_KILL = 1; // 销毁密码
	public static final int LOCK_MEM_ACCESS = 2; // 访问密码
	public static final int LOCK_MEM_EPC = 3; // EPC
	public static final int LOCK_MEM_TID = 4; // TID
	public static final int LOCK_MEM_USER = 5; // USER
	private byte[] selectEPC = null;

	public NewSendCommendManager(InputStream serialPortInput, OutputStream serialportOutput)
	{
		in = serialPortInput;
		out = serialportOutput;
	}

	/**
	 * 发送指令
	 *
	 * @param cmd
	 */
	private void sendCMD(byte[] cmd)
	{
		try
		{
			out.write(cmd);
			out.flush();
		}
		catch (IOException e)
		{

			e.printStackTrace();
		}
	}

	/**
	 * 设置波特率
	 */
	@Override
	public boolean setBaudrate()
	{
		byte[] cmd = {};
		return false;
	}

	/**
	 * 获取版本信息
	 */
	@Override
	public byte[] getFirmware()
	{
		byte[] cmd = { (byte) 0xBB, (byte) 0x00, (byte) 0x03, (byte) 0x00, (byte) 0x01,
				(byte) 0x00, (byte) 0x04, (byte) 0x7E };
		byte[] version = null;
		sendCMD(cmd);
		byte[] response = this.read();
		if (response != null)
		{
			Log.e("", Tools.Bytes2HexString(response, response.length));
			byte[] resolve = handlerResponse(response);
			if (resolve != null && resolve.length > 1)
			{
				version = new byte[resolve.length - 1];
				System.arraycopy(resolve, 1, version, 0, resolve.length - 1);
			}
		}
		return version;
	}

	/**
	 * 设置读写器的灵敏度，
	 *
	 * @param value
	 */
	public void setSensitivity(int value)
	{
		byte[] cmd = { (byte) 0xBB, (byte) 0x00, (byte) 0xF0, (byte) 0x00, (byte) 0x04,
				(byte) 0x02, (byte) 0x06, (byte) 0x00, (byte) 0xA0, (byte) 0x9C, (byte) 0x7E };
		cmd[5] = (byte) value;
		cmd[cmd.length - 2] = checkSum(cmd);
		sendCMD(cmd);

		byte[] response = this.read();
		if (response != null)
		{
			Log.e("setSensitivity ", Tools.Bytes2HexString(response, response.length));
		}
	}

	/**
	 * 读取响应帧
	 *
	 * @return
	 */
	private byte[] read()
	{
		byte[] responseData = null;
		byte[] response = null;
		int available = 0;
		int index = 0;
		int headIndex = 0;
		// 500ms内轮询无数据则，无数据返回
		try
		{
			while (index < 10)
			{
				Thread.sleep(50);
				available = in.available();
				// 缓冲区有数据
				if (available > 7) break;
				index++;
			}
			if (available > 0)
			{
				responseData = new byte[available];
				in.read(responseData);
				for (int i = 0; i < available; i++)
				{
					if (responseData[i] == HEAD)
					{
						headIndex = i;
						break;
					}
				}
				response = new byte[available - headIndex];
				System.arraycopy(responseData, headIndex, response, 0, response.length);
			}
		}
		catch (Exception e1)
		{
			e1.printStackTrace();
		}
		return response;
	}

	/**
	 * 设置输出功率
	 */
	@Override
	public boolean setOutputPower(int value)
	{
		// byte[] cmd = {(byte)0xBB ,(byte)0x00 ,(byte)0xB6 ,(byte)0x00
		// ,(byte)0x02 ,(byte)0x0A ,(byte)0x28
		// ,(byte)0xEA ,(byte)0x7E };
		// cmd[5] = (byte)((0xff00 & value)>>8);
		// cmd[6] = (byte)(0xff & value);
		// cmd[cmd.length - 2] = checkSum(cmd);
		// Log.e("", Tools.Bytes2HexString(cmd, cmd.length));
		// sendCMD(cmd);
		// byte[] recv = read();
		// if(recv != null){
		// // Log.e("", Tools.Bytes2HexString(recv, recv.length));
		// return true;
		// }
		int mixer = 1;
		int if_g = 3;
		int trd = 432;
		switch (value)
		{
			case 16:
				mixer = 1;
				if_g = 1;
				trd = 432;
				break;
			case 17:
				mixer = 1;
				if_g = 3;
				trd = 432;
				break;
			case 18:
				mixer = 2;
				if_g = 4;
				trd = 432;
				break;
			case 19:
				mixer = 2;
				if_g = 5;
				trd = 432 + 64;
			case 20:
				mixer = 2;
				if_g = 6;
				trd = 432 + 64;
			case 21:
				mixer = 2;
				if_g = 6;
				trd = 432 + 128;
				break;
			case 22:
				mixer = 3;
				if_g = 6;
				trd = 432 + 192;
				break;
			case 23:
				mixer = 4;
				if_g = 6;
				trd = 432 + 192;
				break;
			default:
				mixer = 6;
				if_g = 7;
				// trd = 432 + 192;（本来是这个的）
				trd = 432 + 64;
				break;
		}

		return setRecvParam(mixer, if_g, trd);
	}

	/**
	 * 设置接收解调器参数 参数说明： int mixer_g,混频器增益(增益为9dbm,mixer_g的范围为0~6) int if_g,
	 * 中频放大器增益(增益为36dbm，if_g的范围为0~7) int trd 信号解调阀值（trd越大距离越近，越小距离越远，范围0x01b0(432)~0x0360(864)）
	 */
	public boolean setRecvParam(int mixer_g, int if_g, int trd)
	{
		byte[] cmd = { (byte) 0xBB, (byte) 0x00, (byte) 0xF0, (byte) 0x00, (byte) 0x04,
				(byte) 0x03, (byte) 0x06, (byte) 0x01, (byte) 0xB0, (byte) 0xAE, (byte) 0x7E };
		byte[] recv = null;
		byte[] content = null;
		cmd[5] = (byte) mixer_g;
		cmd[6] = (byte) if_g;
		cmd[7] = (byte) (trd / 256);
		cmd[8] = (byte) (trd % 256);
		cmd[9] = checkSum(cmd);
		sendCMD(cmd);
		recv = read();
		if (recv != null)
		{
			// Log.e("setRecvParam", Tools.Bytes2HexString(recv, recv.length));
			content = handlerResponse(recv);
			if (content != null)
			{
				return true;
			}
		}
		return false;
	}

	/**
	 * 多标签盘存
	 *
	 * @return
	 */
	public List<byte[]> inventoryMulti()
	{
		unSelectEPC();
		List<byte[]> list = new ArrayList<byte[]>();
		byte[] cmd = { (byte) 0xBB, (byte) 0x00, (byte) 0x27, (byte) 0x00, (byte) 0x03,
				(byte) 0x22, (byte) 0x27, (byte) 0x10, (byte) 0x83, (byte) 0x7E };
		sendCMD(cmd);
		byte[] response = this.read();
		if (response != null)
		{
			int responseLength = response.length;
			// Log.e("", Tools.Bytes2HexString(response, response.length));
			int start = 0;
			// 单张卡返回的数据
			// byte[] sigleCard = new byte[24];
			// 多张卡片，将以多条帧 的格式返回
			if (responseLength > 15)
			{
				// Log.e("多张卡", Tools.Bytes2HexString(response,
				// response.length));
				// 要取到数据长度
				while (responseLength > 5)
				{
					// Log.e("多张卡", Tools.Bytes2HexString(response,
					// response.length));
					// 获取完整的一条指令
					int paraLen = response[start + 4] & 0xff;
					int singleCardLen = paraLen + 7;
					// 指令不完整跳出
					if (singleCardLen > responseLength) break;
					byte[] sigleCard = new byte[singleCardLen];
					System.arraycopy(response, start, sigleCard, 0, singleCardLen);

					byte[] resolve = handlerResponse(sigleCard);
					// Log.e("多张卡", Tools.Bytes2HexString(resolve,
					// resolve.length));
					// 处理后的数据第一位是指令代码，第2位RSSI，第3-4位是PC，第5位到最后是EPC
					if (resolve != null && paraLen > 5)
					{
						byte[] epcBytes = new byte[paraLen - 5];
						System.arraycopy(resolve, 4, epcBytes, 0, paraLen - 5);
						// Log.e("处理EPC", Tools.Bytes2HexString(epcBytes,
						// epcBytes.length));
						list.add(epcBytes);
					}
					start += singleCardLen;
					responseLength -= singleCardLen;
				}
			}
			else
			{
				handlerResponse(response);
			}
		}
		return list;
	}

	/**
	 * 停止多标签盘存
	 */
	public void stopInventoryMulti()
	{
		byte[] cmd = { (byte) 0xBB, (byte) 0x00, (byte) 0x28, (byte) 0x00, (byte) 0x00,
				(byte) 0x28, (byte) 0x7E };
		sendCMD(cmd);
		byte[] recv = read();

	}

	/**
	 * 实时盘存
	 */
	@Override
	public List<byte[]> inventoryRealTime()
	{
		// 取消选定
		unSelectEPC();
		byte[] cmd = { (byte) 0xBB, (byte) 0x00, (byte) 0x22, (byte) 0x00, (byte) 0x00,
				(byte) 0x22, (byte) 0x7E };
		sendCMD(cmd);
		List<byte[]> list = new ArrayList<byte[]>();
		byte[] response = this.read();
		if (response != null)
		{
			int responseLength = response.length;
			// Log.e("", Tools.Bytes2HexString(response, response.length));
			int start = 0;
			// 单张卡返回的数据
			// byte[] sigleCard = new byte[24];
			// 多张卡片，将以多条帧 的格式返回
			if (responseLength > 15)
			{
				// Log.e("多张卡", Tools.Bytes2HexString(response,
				// response.length));
				// 要取到数据长度
				while (responseLength > 5)
				{
					// Log.e("多张卡", Tools.Bytes2HexString(response,
					// response.length));
					// 获取完整的一条指令
					int paraLen = response[start + 4] & 0xff;
					int singleCardLen = paraLen + 7;
					// 指令不完整跳出
					if (singleCardLen > responseLength) break;
					byte[] sigleCard = new byte[singleCardLen];
					System.arraycopy(response, start, sigleCard, 0, singleCardLen);

					byte[] resolve = handlerResponse(sigleCard);
					// Log.e("多张卡", Tools.Bytes2HexString(resolve,
					// resolve.length));
					// 处理后的数据第一位是指令代码，第2位RSSI，第3-4位是PC，第5位到最后是EPC
					if (resolve != null && paraLen > 5)
					{
						byte[] epcBytes = new byte[paraLen - 5];
						System.arraycopy(resolve, 4, epcBytes, 0, paraLen - 5);
						// Log.e("处理EPC", Tools.Bytes2HexString(epcBytes,
						// epcBytes.length));
						list.add(epcBytes);
					}
					start += singleCardLen;
					responseLength -= singleCardLen;
				}
			}
			else
			{
				handlerResponse(response);
			}
		}
		return list;
	}

	/**
	 * 选择标签
	 */
	// BB 00 0C 00 13 01 00 00 00 20 60 00 30 75 1F EB 70 5C 59 04 E3 D5 0D 70
	// AD 7E
	@Override
	public void selectEPC(byte[] epc)
	{
		byte[] cmd = { (byte) 0xBB, (byte) 0x00, (byte) 0x12, (byte) 0x00, (byte) 0x01,
				(byte) 0x00, (byte) 0x13, (byte) 0x7E };
		this.selectEPC = epc;
		sendCMD(cmd);
		byte[] response = this.read();
		if (response != null)
		{
			// Log.e("select epc", Tools.Bytes2HexString(response,
			// response.length));
		}

	}

	/**
	 * 取消选择
	 *
	 * @return
	 */
	public int unSelectEPC()
	{
		byte[] cmd = { (byte) 0xBB, (byte) 0x00, (byte) 0x12, (byte) 0x00, (byte) 0x01,
				(byte) 0x01, (byte) 0x14, (byte) 0x7E };
		sendCMD(cmd);
		byte[] response = this.read();
		if (response != null)
		{

		}
		return 0;
	}

	/**
	 * 设置select的参数，在对卡进行操作之前调用
	 */
	private void setSelectPara()
	{
		byte[] cmd = { (byte) 0xBB, (byte) 0x00, (byte) 0x0C, (byte) 0x00, (byte) 0x13,
				(byte) 0x01, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x20, (byte) 0x60,
				(byte) 0x00, (byte) 0x01, (byte) 0x61, (byte) 0x05, (byte) 0xB8, (byte) 0x03,
				(byte) 0x48, (byte) 0x0C, (byte) 0xD0, (byte) 0x00, (byte) 0x03, (byte) 0xD1,
				(byte) 0x9E, (byte) 0x58, (byte) 0x7E };
		if (this.selectEPC != null)
		{
			Log.e("", "select epc");
			System.arraycopy(selectEPC, 0, cmd, 12, selectEPC.length);
			cmd[cmd.length - 2] = checkSum(cmd);
			// Log.e("setSelectPara", Tools.Bytes2HexString(cmd, cmd.length));
			sendCMD(cmd);
			byte[] response = this.read();
			if (response != null)
			{}
			// Log.e("setSelectPara response", Tools.Bytes2HexString(response,
			// response.length));
		}
	}

	/**
	 * 读取6C标签函数
	 */
	@Override
	public byte[] readFrom6C(int memBank, int startAddr, int length, byte[] accessPassword)
	{
		// 进行读写操作前线选择操作的卡
		this.setSelectPara();
		byte[] cmd = { (byte) 0xBB, (byte) 0x00, (byte) 0x39, (byte) 0x00, (byte) 0x09,
				(byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x03, (byte) 0x00,
				(byte) 0x00, (byte) 0x00, (byte) 0x08, (byte) 0x4D, (byte) 0x7E };
		byte[] data = null;
		if (accessPassword == null || accessPassword.length != 4)
		{
			return null;
		}

		System.arraycopy(accessPassword, 0, cmd, 5, 4);
		cmd[9] = (byte) memBank;
		if (startAddr <= 255)
		{
			cmd[10] = 0x00;
			cmd[11] = (byte) startAddr;
		}
		else
		{
			int addrH = startAddr / 256;
			int addrL = startAddr % 256;
			cmd[10] = (byte) addrH;
			cmd[11] = (byte) addrL;
		}
		if (length <= 255)
		{
			cmd[12] = 0x00;
			cmd[13] = (byte) length;
		}
		else
		{
			int lengH = length / 256;
			int lengL = length % 256;
			cmd[12] = (byte) lengH;
			cmd[13] = (byte) lengL;
		}
		cmd[14] = checkSum(cmd);
		sendCMD(cmd);
		byte[] response = this.read();
		if (response != null)
		{
			Log.e("readFrom6c response", Tools.Bytes2HexString(response, response.length));
			byte[] resolve = handlerResponse(response);

			if (resolve != null)
			{
				Log.e("readFrom6c resolve", Tools.Bytes2HexString(resolve, resolve.length));
				// 正常响应帧BB 01 39 00 1F 0E 30 00 01 61 05 B8 03 48 0C D0 00 03 D1
				// 9E 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 4F 7E
				if (resolve[0] == (byte) 0x39)
				{
					int lengData = resolve.length - resolve[1] - 2;
					data = new byte[lengData];
					System.arraycopy(resolve, resolve[1] + 2, data, 0, lengData);
					// Log.e("readFrom6c", Tools.Bytes2HexString(data,
					// data.length));
				}
				else
				{
					// 错误帧，返回错误代码
					data = new byte[1];
					data[0] = resolve[1];
					// Log.e("readFrom6c", Tools.Bytes2HexString(data,
					// data.length));
				}
			}
		}
		return data;
	}

	/**
	 * 写6C标签
	 */
	@Override
	public boolean writeTo6C(byte[] password, int memBank, int startAddr, int dataLen, byte[] data)
	{

		// 进行读写操作前线选择操作的卡
		this.setSelectPara();
		if (password == null || password.length != 4)
		{
			return false;
		}
		boolean writeFlag = false;
		int cmdLen = 16 + data.length;
		int parameterLen = 9 + data.length;
		byte[] cmd = new byte[cmdLen];
		cmd[0] = (byte) 0xBB;
		cmd[1] = 0x00;
		cmd[2] = 0x49;
		if (parameterLen < 256)
		{
			cmd[3] = 0x00;
			cmd[4] = (byte) parameterLen;
		}
		else
		{
			int paraH = parameterLen / 256;
			int paraL = parameterLen % 256;
			cmd[3] = (byte) paraH;
			cmd[4] = (byte) paraL;
		}
		System.arraycopy(password, 0, cmd, 5, 4);
		cmd[9] = (byte) memBank;
		if (startAddr < 256)
		{
			cmd[10] = 0x00;
			cmd[11] = (byte) startAddr;
		}
		else
		{
			int startH = startAddr / 256;
			int startL = startAddr % 256;
			cmd[10] = (byte) startH;
			cmd[11] = (byte) startL;
		}
		if (dataLen < 256)
		{
			cmd[12] = 0x00;
			cmd[13] = (byte) dataLen;
		}
		else
		{
			int dataLenH = dataLen / 256;
			int dataLenL = dataLen % 256;
			cmd[12] = (byte) dataLenH;
			cmd[13] = (byte) dataLenL;
		}
		System.arraycopy(data, 0, cmd, 14, data.length);
		cmd[cmdLen - 2] = checkSum(cmd);
		cmd[cmdLen - 1] = (byte) 0x7E;
		// Log.e("write data", Tools.Bytes2HexString(cmd, cmdLen));
		sendCMD(cmd);
		try
		{
			Thread.sleep(50);
		}
		catch (InterruptedException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		byte[] response = this.read();
		if (response != null)
		{
			// Log.e("write data response", Tools.Bytes2HexString(response,
			// response.length));
			byte[] resolve = this.handlerResponse(response);
			if (resolve != null)
			{
				// Log.e("write data resolve", Tools.Bytes2HexString(resolve,
				// resolve.length));
				if (resolve[0] == 0x49 && resolve[resolve.length - 1] == RESPONSE_OK)
				{
					writeFlag = true;
				}
			}
		}

		return writeFlag;
	}

	/**
	 * 锁定6C标签
	 */
	@Override
	public boolean lock6C(byte[] password, int memBank, int lockType)
	{
		byte[] cmd = { HEAD, 0x00, (byte) 0x82, 0x00, 0x07, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00,
				0x00, 0x00, END };
		int lockPay = 0;
		byte[] recv = null;
		byte[] lockPara = new byte[3];
		// 开放
		if (lockType == LOCK_TYPE_OPEN)
		{
			// System.out.println("开放");
			lockPay = (1 << (20 - 2 * memBank + 1));
		}
		// 永久开
		if (lockType == LOCK_TYPE_PERMA_OPEN)
		{
			// System.out.println("永久开");
			lockPay = (1 << (20 - 2 * memBank + 1)) + (1 << (20 - 2 * memBank))
					+ (1 << (2 * (5 - memBank)));
		}
		// 锁定
		if (lockType == LOCK_TYPE_LOCK)
		{
			// System.out.println("锁定");
			lockPay = (1 << (20 - 2 * memBank + 1)) + (2 << (2 * (5 - memBank)));
		}
		// 永久锁定
		if (lockType == LOCK_TYPE_PERMA_LOCK)
		{
			// System.out.println("永久锁定");
			lockPay = (1 << (20 - 2 * memBank + 1)) + (1 << (20 - 2 * memBank))
					+ (3 << (2 * (5 - memBank)));
		}
		lockPara = Tools.intToByte(lockPay);
		// 密码
		System.arraycopy(password, 0, cmd, 5, password.length);
		// 锁定参数
		System.arraycopy(lockPara, 0, cmd, 9, lockPara.length);
		cmd[cmd.length - 2] = checkSum(cmd);
		sendCMD(cmd);
		recv = this.read();
		if (recv != null)
		{
			Log.e("Lock recv", Tools.Bytes2HexString(recv, recv.length));
		}
		return false;
	}

	@Override
	public void close()
	{
		try
		{
			in.close();
			out.close();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}

	}

	// 计算校验和
	@Override
	public byte checkSum(byte[] data)
	{
		byte crc = 0x00;
		// 从指令类型累加到参数最后一位
		for (int i = 1; i < data.length - 2; i++)
		{
			crc += data[i];
		}
		return crc;
	}

	/**
	 * 处理响应帧
	 *
	 * @param response
	 * @return
	 */
	private byte[] handlerResponse(byte[] response)
	{
		byte[] data = null;
		byte crc = 0x00;
		int responseLength = response.length;
		if (response[0] != HEAD)
		{
			Log.e("handlerResponse", "head error");
			return data;
		}
		if (response[responseLength - 1] != END)
		{
			Log.e("handlerResponse", "end error");
			return data;
		}
		if (responseLength < 7) return data;
		// 转成无符号int
		int lengthHigh = response[3] & 0xff;
		int lengthLow = response[4] & 0xff;
		int dataLength = lengthHigh * 256 + lengthLow;
		// 计算CRC
		crc = checkSum(response);
		if (crc != response[responseLength - 2])
		{
			Log.e("handlerResponse", "crc error");
			return data;
		}
		if (dataLength != 0 && responseLength == dataLength + 7)
		{
			Log.e("handlerResponse", "response right");
			data = new byte[dataLength + 1];
			data[0] = response[2];
			System.arraycopy(response, 5, data, 1, dataLength);
		}
		return data;
	}

	// 设置频率
	@Override
	public int setFrequency(int startFrequency, int freqSpace, int freqQuality)
	{
		int frequency = 1;// 为921.125M频率

		if (startFrequency > 840125 && startFrequency < 844875)
		{// 中国1
			frequency = (startFrequency - 840125) / 250;
		}
		else if (startFrequency > 920125 && startFrequency < 924875)
		{// 中国2
			frequency = (startFrequency - 920125) / 250;
		}
		else if (startFrequency > 865100 && startFrequency < 867900)
		{// 欧洲
			frequency = (startFrequency - 865100) / 200;
		}
		else if (startFrequency > 902250 && startFrequency < 927750)
		{// 美国
			frequency = (startFrequency - 902250) / 500;
		}
		byte[] cmd = { (byte) 0xBB, (byte) 0x00, (byte) 0xAB, (byte) 0x00, (byte) 0x01,
				(byte) 0x04, (byte) 0xB0, (byte) 0x7E };
		cmd[5] = (byte) frequency;
		cmd[6] = checkSum(cmd);
		sendCMD(cmd);
		byte[] recv = read();
		if (recv != null)
		{
			Log.e("frequency", Tools.Bytes2HexString(recv, recv.length));
		}
		return 0;
	}

	/**
	 * 设置工作区域
	 *
	 * @param area
	 * @return
	 */
	public int setWorkArea(int area)
	{
		// BB 00 07 00 01 01 09 7E
		byte[] cmd = { (byte) 0xBB, (byte) 0x00, (byte) 0x07, (byte) 0x00, (byte) 0x01,
				(byte) 0x01, (byte) 0x09, (byte) 0x7E };
		cmd[5] = (byte) area;
		cmd[6] = checkSum(cmd);
		sendCMD(cmd);
		byte[] recv = read();
		if (recv != null)
		{
			Log.e("setWorkArea", Tools.Bytes2HexString(recv, recv.length));
			handlerResponse(recv);

		}
		return 0;
	}

	/**
	 * 获取频率参数
	 *
	 * @return
	 */
	public int getFrequency()
	{
		byte[] cmd = { (byte) 0xBB, (byte) 0x00, (byte) 0xAA, (byte) 0x00, (byte) 0x00,
				(byte) 0xAA, (byte) 0x7E };
		sendCMD(cmd);
		byte[] recv = read();
		if (recv != null)
		{
			Log.e("getFrequency", Tools.Bytes2HexString(recv, recv.length));
			handlerResponse(recv);
		}
		return 0;
	}

	// 调节距离的函数

}
