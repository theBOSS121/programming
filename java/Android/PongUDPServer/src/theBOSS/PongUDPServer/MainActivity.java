package theBOSS.PongUDPServer;

import java.net.InetAddress;
import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends Activity {
	
	public static final int PACKET_CAME = 0;
	public static final int IS_RUNNING = 1;
	public static final int IP_ADDRESS = 2;
	public static final int NEW_CLIENT = 3;
	
	TextView isRunning, myIPAddressField, lastMessage;
	Button clear;
	ServerThread myThread;
	
	private final Handler mHandler = new Handler() {
		public void handleMessage(Message msg) {
			switch(msg.what) {
				case NEW_CLIENT:
					String size = (String) msg.obj;
					lastMessage.setText(size);
					break;
				case PACKET_CAME:
					String incomingMessage = (String) msg.obj;
					lastMessage.setText(incomingMessage);
					break;
				case IS_RUNNING:
					String socketStatus = (String) msg.obj;
					isRunning.setText(socketStatus);
					break;
				case IP_ADDRESS:
					InetAddress myIPAddress = (InetAddress) msg.obj;
					myIPAddressField.setText("Server IP Address: " + myIPAddress.toString());
					break;	
			}
		}
	};
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		isRunning = (TextView) findViewById(R.id.tvisRunning);
		myIPAddressField = (TextView) findViewById(R.id.tvIP);
		lastMessage = (TextView) findViewById(R.id.tvMessage);
		clear = (Button) findViewById(R.id.button1);
		myThread = new ServerThread(getApplicationContext(), mHandler);
		myThread.start();
		
		clear.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				lastMessage.setText("");
			}
		});
	}
	
	protected void onDestroy() {
		super.onDestroy();
		myThread.closeSocket();
	}
}
