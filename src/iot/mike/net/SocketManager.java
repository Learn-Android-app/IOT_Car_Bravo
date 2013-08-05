package iot.mike.net;

import iot.mike.setting.SettingData;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

import android.os.Handler;

public class SocketManager {
	private SocketManager(){}
	private Handler mainHandler = null;
	private Socket MainSocket = null;
	private DataInputStream reader = null;
	private DataOutputStream writer = null;
	
	
	private static class SocketManagerHolder{
		public static SocketManager socketManager = new SocketManager();
	}
	
	public static SocketManager getInstance(){
		return SocketManagerHolder.socketManager;
	}
	
	/**
	 * 设置主界面控制器
	 * @param handler
	 */
	public void setHandler(Handler handler){
		mainHandler = null;
		mainHandler = handler;
	}
	
	/**
	 * 建立连接
	 * @return 是否连接成功
	 */
	public boolean startLink(){
		if (SettingData.CarIP.equals("") 
				|| SettingData.CarMainPort == 0) {
			return false;
		}
		
		try {
			MainSocket = new Socket(SettingData.CarIP, 
					SettingData.CarMainPort);
			reader = new DataInputStream(MainSocket.getInputStream());
			writer = new DataOutputStream(MainSocket.getOutputStream());
			return true;
		} catch (UnknownHostException e) {
			e.printStackTrace();
			return false;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}finally{
			try {MainSocket.close();} 
			catch (IOException e) 
			{e.printStackTrace();}
			MainSocket = null;
		}
	}
	
	/**
	 * 结束连接
	 */
	public void endLink(){
		if (MainSocket != null) {
			try {
				MainSocket.close();
			} catch (IOException e) {
				e.printStackTrace();
			}finally{
				MainSocket = null;
			}
		}
	}
	
	/**
	 * 将命令传输出去
	 * @param jsonorder 命令
	 * @return 是否成功
	 */
	public boolean sendOrder(String jsonorder){
		try {
			synchronized (writer) {
				writer.writeBytes(jsonorder + "\n");
				writer.flush();
			}
			return true;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
	}
	
	/**
	 * 返回读取数据的流
	 * @return DataInputStream
	 */
	public DataInputStream getReader() {
		return this.reader;
	}
}
