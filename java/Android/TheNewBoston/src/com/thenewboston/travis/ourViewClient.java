package com.thenewboston.travis;

import android.webkit.WebView;
import android.webkit.WebViewClient;

public class ourViewClient extends WebViewClient {

	public boolean shouldOverrideUrlLoading(WebView v, String url) {
		v.loadUrl(url);
		return true;
	}
	
}
