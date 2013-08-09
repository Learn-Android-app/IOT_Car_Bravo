package iot.mike.iotcarbravo.setting;


public class SettingData {
	private SettingData(){}
	
	public static String CarIP = "192.168.0.232";
	public static int CarMainPort = 7890;
	
	public static final int KeyBoard = 1;
	public static final int OnKeyBoard = 0;
	
	public static volatile int CtrlMode = KeyBoard;
	
	public static final int OKCamera = 1;
	public static final int USBCamera = 2;
	public static final int NONE = 0;
	
	public static volatile int VideoMode = NONE;
}
