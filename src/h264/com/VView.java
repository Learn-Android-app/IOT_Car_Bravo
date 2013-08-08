package h264.com;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

public class VView extends View  implements Runnable{
	private Bitmap  mBitQQ  = null;   
	private Paint   mPaint = null;   
	private Bitmap  mSCBitmap = null;   
    private Thread playThread = null;
    private ServerSocket serverSocket = null;
    private Socket clientSocket = null;
    
    
    private int width = 640;  
    private int height = 480;
    private byte [] mPixel = new byte[width*height*2];
    private ByteBuffer buffer = ByteBuffer.wrap( mPixel );
    private Bitmap VideoBit = 
    		Bitmap.createBitmap(width, height, Config.RGB_565);           
   
    private int mTrans=0x0F0F0F0F;
    private String PathFileName;
    private DataInputStream inputStream;
	
    public native int InitDecoder(int width, int height);
    public native int UninitDecoder(); 
    public native int DecoderNal(byte[] in, int insize, byte[] out);
    static {
        System.loadLibrary("H264Android");
    }
    
    public VView(Context context)  {
        super(context);
        setFocusable(true);
       	int i = mPixel.length;
        for(i=0; i<mPixel.length; i++){
        	mPixel[i]=(byte)0x00;
        }
    }
    public VView(Context  _context, AttributeSet _attriAttributeSet) {
    	super(_context, _attriAttributeSet);
    	setFocusable(true);
       	int i = mPixel.length;
        for(i=0; i<mPixel.length; i++){
        	mPixel[i]=(byte)0x00;
        }
    }
    
    public void init() throws IOException{
    	serverSocket = new ServerSocket(11530);
    	Log.d("Video", "视频服务器搭建完成");
    }
    
    public void playVideo(){
    	stopPlaty();
    	mPixel = new byte[width*height*2];
    	buffer = ByteBuffer.wrap( mPixel );
    	VideoBit = Bitmap.createBitmap(width, height, Config.RGB_565);           
    	playThread = new Thread(this);
    	playThread.start();
    }
        
    public void stopPlaty() {
    	if (playThread != null) {
    		try{
    			playThread.interrupt();
    			serverSocket.close();
	    		playThread = null;
    		}catch (Exception e) {
    			e.printStackTrace();
    		}
		}
	}
    
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);   
        VideoBit.copyPixelsFromBuffer(buffer);//makeBuffer(data565, N));
        Matrix matrix = new Matrix();
        matrix.postScale((float)1.5,	(float)1.5);
        canvas.drawBitmap(VideoBit, matrix, null); 
    }
    
    public int MergeBuffer(byte[] NalBuf, 
    		int NalBufUsed, byte[] SockBuf, 
    		int SockBufUsed, int SockRemain) {
    	int  i=0;
    	byte Temp;
    	for(i=0; i<SockRemain; i++){
    		Temp  =SockBuf[i+SockBufUsed];
    		NalBuf[i+NalBufUsed]=Temp;

    		mTrans <<= 8;
    		mTrans  |= Temp;
    		if(mTrans == 1) {
    			i++;
    			break;
    		}	
    	}
    	return i;
    }
    
    public void run()  {   
    	int iTemp=0;
    	int nalLen;
    	
    	boolean bFirst=true;
    	boolean bFindPPS=true;
    	
    	int bytesRead=0;    	
    	int NalBufUsed=0;
    	int SockBufUsed=0;
        
    	byte [] NalBuf = new byte[900000]; // 40k
    	byte [] SockBuf = new byte[2048000];
    	
    	InitDecoder(width, height); 
		try {
			clientSocket = serverSocket.accept();
			inputStream = 
					new DataInputStream(clientSocket.getInputStream());
		} catch (IOException e1) {
			e1.printStackTrace();
			return;
		}
		while (!Thread.currentThread().isInterrupted())  {   
		    try  {   
				bytesRead = inputStream.read(SockBuf, 0, 2048000);
				Log.e("Read", "Read");
		    }catch (IOException e) {e.printStackTrace();break;}
		    SockBufUsed =0;
		    
			while(bytesRead-SockBufUsed>0){
				nalLen = MergeBuffer(NalBuf, NalBufUsed, SockBuf, SockBufUsed, bytesRead-SockBufUsed);
				NalBufUsed += nalLen;
				SockBufUsed += nalLen;
				
				while(mTrans == 1){
					mTrans = 0xFFFFFFFF;

					if(bFirst==true) {// the first start flag
						bFirst = false;
					}else { // a complete NAL data, include 0x00000001 trail.
						if(bFindPPS==true){ // true
							if( (NalBuf[4]&0x1F) == 7 ){
								bFindPPS = false;
							}else{
				   				NalBuf[0]=0;
			    				NalBuf[1]=0;
			    				NalBuf[2]=0;
			    				NalBuf[3]=1;
			    				
			    				NalBufUsed=4;
								break;
							}
						}
						//	decode nal
						iTemp=DecoderNal(NalBuf, NalBufUsed-4, mPixel);   
					
			            if(iTemp>0)
			            	postInvalidate();  
					}

					NalBuf[0]=0;
					NalBuf[1]=0;
					NalBuf[2]=0;
					NalBuf[3]=1;
					NalBufUsed=4;
				}		
			} 
		}
        UninitDecoder();
    }  
}