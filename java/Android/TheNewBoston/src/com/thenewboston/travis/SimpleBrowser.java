package com.thenewboston.travis;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;

public class SimpleBrowser extends Activity implements OnClickListener{

	EditText url;
	WebView ourBrow;
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.simlebrowser);
		
		ourBrow = (WebView) findViewById(R.id.wvBrowser);
		ourBrow.getSettings().setJavaScriptEnabled(true);
		ourBrow.getSettings().setLoadWithOverviewMode(true);
		ourBrow.getSettings().setUseWideViewPort(true);
		ourBrow.setWebViewClient(new ourViewClient());
		try {
			ourBrow.loadUrl("http://www.klime.si");
		}catch (Exception e) {
			e.printStackTrace();
		}
		Button go = (Button) findViewById(R.id.bGo);
		Button back = (Button) findViewById(R.id.bBack);
		Button refresh =  (Button) findViewById(R.id.bRefresh);
		Button forward = (Button) findViewById(R.id.bForward);
		Button clearHistory = (Button) findViewById(R.id.bHistory);
		url = (EditText) findViewById(R.id.etURL);
		go.setOnClickListener(this);
		back.setOnClickListener(this);
		refresh.setOnClickListener(this);
		forward.setOnClickListener(this);
		clearHistory.setOnClickListener(this);
		
	}

	public void onClick(View v) {
		if(v.getId() == R.id.bGo) {
			String theWebside = url.getText().toString();
			ourBrow.loadUrl(theWebside);
			//hide keyboard after clicking on go
			InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
			imm.hideSoftInputFromWindow(url.getWindowToken(), 0);
		}else if(v.getId() == R.id.bBack) {
			if(ourBrow.canGoBack()) ourBrow.goBack();
		}else if(v.getId() == R.id.bForward) {
			if(ourBrow.canGoForward()) ourBrow.goForward();
		}else if(v.getId() == R.id.bRefresh) {
			ourBrow.reload();
		}else if(v.getId() == R.id.bHistory) {
			ourBrow.clearHistory();
		}
	}
	
}
