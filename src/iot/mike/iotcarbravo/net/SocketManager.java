package iot.mike.iotcarbravo.net;

import iot.mike.iotcarbravo.setting.SettingData;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;

import android.os.Handler;
import android.util.Log;

public class SocketManager {
	private SocketManager(){
		try {
			videoSocket = new ServerSocket(11530);
			Log.d("Socket Video", "Established");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private Handler MainActivityHandler = null;
	private Socket MainSocket = null;
	private BufferedReader reader = null;
	private BufferedWriter writer = null;
	private Thread readFromCarThread = null;
	
	private ServerSocket videoSocket = null;
	private volatile Socket vviewSocket = null;
	
	private static class SocketManagerHolder{
		public static SocketManager socketManager = new SocketManager();
	}
	
	public static SocketManager getInstance(){
		return SocketManagerHolder.socketManager;
	}
	
	/**
	 * 给视频码流
	 * @param framedata 视频数据
	 * @return
	 */
	public boolean sendVideo(byte[] framedata) {
		if (vviewSocket != null) {
			try {
				vviewSocket.getOutputStream().write(framedata);
				Log.d("VideoSocket", "Send OK");
				return true;
			} catch (IOException e) {
				e.printStackTrace();
				return false;
			}
		}
		Log.d("VideoSocket", "Send Failed");
		return false;
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
					while (true) {
						vviewSocket = videoSocket.accept();
						Log.d("VideoSocket", "New Comer");
					}
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
			if (writer != null) {
				synchronized (writer) {
					writer.write(jsonorder + "\n");
					writer.flush();
					return true;
				}
			}else {
				return false;
			}
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
