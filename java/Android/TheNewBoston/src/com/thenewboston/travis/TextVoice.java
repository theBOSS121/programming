package com.thenewboston.travis;

import java.util.Locale;
import java.util.Random;

import android.app.Activity;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.speech.tts.TextToSpeech.OnInitListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class TextVoice extends Activity implements OnClickListener{

	static final String[] texts = {
		"Whaat's up Gangstas!", "You smell!", "I have aaaaaa!"
	};
	
	TextToSpeech tts;	
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.textvoice);
		Button b = (Button) findViewById(R.id.bTextVoice);
		b.setOnClickListener(this);
		tts = new TextToSpeech(TextVoice.this, new OnInitListener() {
			public void onInit(int status) {
				if(status != TextToSpeech.ERROR) {
					tts.setLanguage(Locale.ENGLISH);
				}
			}
		});
	}
	
	protected void onPause() {
		super.onPause();
		if(tts != null) {
			tts.stop();
			tts.shutdown();
		}
	}

	public void onClick(View v) {
		Random r = new Random();
		String random = texts[r.nextInt(3)];
		tts.speak(random, TextToSpeech.QUEUE_FLUSH, null);
	}
	
}
