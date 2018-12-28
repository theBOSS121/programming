package com.example.hellothreadsclient;

import java.net.InetAddress;
import java.net.UnknownHostException;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivityClient extends Activity {

	public static final int PACKET_CAME = 0;
	
	EditText msg, serverIP;
	TextView message;
	Button send;
	
	ClientThread myThread;
	
	private final Handler mHandler = new Handler() {
		public void handleMessage(Message msg) {
			switch(msg.what) {
				case PACKET_CAME:
					String incomingMessage = (String) msg.obj;
					message.setText(incomingMessage);
					break;
			}
		}
	};
	
	
	protected void onDestroy() {
		super.onDestroy();
	}	
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.mainclient);
		msg = (EditText) findViewById(R.id.et);
		serverIP = (EditText) findViewById(R.id.serverIP);
		message = (TextView) findViewById(R.id.tvMessage);
		send = (Button) findViewById(R.id.send);
		
		myThread = new ClientThread(getApplicationContext(), mHandler);
		myThread.start();
		
		send.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				try {
					ClientThread.serverAddress = InetAddress.getByName(serverIP.getText().toString());
					String s = msg.getText().toString();
					ClientThread.send(s.getBytes());
				}catch(UnknownHostException e) {
					e.printStackTrace();
				}
			}
		});
	}
}
