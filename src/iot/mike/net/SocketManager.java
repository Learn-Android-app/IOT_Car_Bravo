package iot.mike.net;

import iot.mike.setting.SettingData;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.concurrent.ThreadPoolExecutor;

import android.os.Handler;
import android.util.Log;

public class SocketManager {
	private SocketManager(){}
	private Handler MainActivityHandler = null;
	private Socket MainSocket = null;
	private BufferedReader reader = null;
	private BufferedWriter writer = null;
	private Thread readFromCarThread = null;
	
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
	public void setKeyBoardActivityHandler(Handler handler){
		MainActivityHandler = null;
		MainActivityHandler = handler;
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
		Thread startLinkThread = new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					MainSocket = new Socket(SettingData.CarIP, 
							SettingData.CarMainPort);
					Log.d("建立连接", "成功!");
					reader = new BufferedReader(
							new InputStreamReader(
									MainSocket.getInputStream()));
					writer = new BufferedWriter(
							new OutputStreamWriter(
									MainSocket.getOutputStream()));
					readFromCarThread = new Thread(
							new ReadFromCarRunnable(reader, 
									MainActivityHandler));
					readFromCarThread.start();
					NetUtil.sendList(writer);
				} catch (UnknownHostException e) {
					e.printStackTrace();
					try {MainSocket.close();} 
					catch (Exception e2) 
					{e.printStackTrace();}
					MainSocket = null;
				} catch (IOException e) {
					e.printStackTrace();
					try {MainSocket.close();} 
					catch (Exception e2) 
					{e.printStackTrace();}
					MainSocket = null;
				}
			}
		});
		startLinkThread.start();
		return true;
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
				writer.write(jsonorder + "\n");
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
	public BufferedReader getReader() {
		return this.reader;
	}
}
