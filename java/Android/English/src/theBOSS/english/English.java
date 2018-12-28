package theBOSS.english;

import java.util.Random;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class English extends Activity implements OnClickListener {

	EditText et;
	TextView  ang;
	Button b;
	
	Dictionary d = new Dictionary();
	Random rand = new Random();
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.english);
		et = findViewById(R.id.et);
		ang = findViewById(R.id.tvAng);
		b = findViewById(R.id.b);
		ang.setText("ang");
		b.setOnClickListener(this);
		newWord();
	}
	
	private void newWord() {
		String s = d.words[rand.nextInt(d.words.length)];
		ang.setText(s);
	}
	
	public void onClick(View v) {		
		if(v.getId() == b.getId()) {
			if(et.getText().toString().equals(ang.getText().toString())) {
				newWord();
			}			
			et.setText("");
		}
		
	}
	
}
