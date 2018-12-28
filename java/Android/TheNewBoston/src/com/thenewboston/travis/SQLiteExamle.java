package com.thenewboston.travis;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class SQLiteExamle extends Activity implements OnClickListener{
	
	Button sqlUpdate, sqlView, sqlModify, sqlGetInfo, sqlDelete;
	EditText sqlName, sqlHotness, sqlRow;
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.sqliteexample);
		sqlUpdate = (Button) findViewById(R.id.bSQLUpdate);
		sqlView = (Button) findViewById(R.id.bSQLOpenView);
		sqlGetInfo = (Button) findViewById(R.id.bgetInfo);
		sqlDelete = (Button) findViewById(R.id.bSQLdelete);
		sqlModify = (Button) findViewById(R.id.bSQLmodify);
		sqlUpdate.setOnClickListener(this);
		sqlView.setOnClickListener(this);
		sqlGetInfo.setOnClickListener(this);
		sqlDelete.setOnClickListener(this);
		sqlModify.setOnClickListener(this);
		sqlName = (EditText) findViewById(R.id.etSQLName);
		sqlHotness = (EditText) findViewById(R.id.etSQLHotnass);
		sqlRow = (EditText) findViewById(R.id.etSQLrowInfo);
	}

	public void onClick(View v) {
		if(v.getId() == R.id.bSQLUpdate) {
			boolean didItWork = true;
			try {
				String name = sqlName.getText().toString();
				String hotness = sqlHotness.getText().toString();
				
				HotOrNot entry = new HotOrNot(SQLiteExamle.this);
				entry.open();
				entry.createEntry(name, hotness);
				entry.close();	
			}catch(Exception e) {
				didItWork = false;
				String error = e.toString();
				Dialog d = new Dialog(this);
				d.setTitle("Dang it!");
				TextView tv = new TextView(this);
				tv.setText(error);
				d.setContentView(tv);
				d.show();
			}finally {
				if(didItWork) {
					Dialog d = new Dialog(this);
					d.setTitle("heck yea!");
					TextView tv = new TextView(this);
					tv.setText("Success");
					d.setContentView(tv);
					d.show();
				}
			}
		}else if(v.getId() == R.id.bSQLOpenView) {
			Intent i = new Intent("com.thenewboston.travis.SQLVIEW");
			startActivity(i);
		}else if(v.getId() == R.id.bgetInfo) {
			String s = sqlRow.getText().toString();
			long l = Long.parseLong(s);
			HotOrNot hon = new HotOrNot(this);
			hon.open();
			String returnedName = hon.getName(l);
			String returnedHotness = hon.getHotness(l);
			hon.close();
			sqlName.setText(returnedName);
			sqlHotness.setText(returnedHotness);
		}else if(v.getId() == R.id.bSQLmodify) {
			String mName = sqlName.getText().toString();
			String mHotness = sqlHotness.getText().toString();
			String sRow = sqlRow.getText().toString();
			long lRow = Long.parseLong(sRow);
			HotOrNot ex = new HotOrNot(this);
			ex.open();
			ex.updateEntry(lRow, mName, mHotness);
			ex.close();
		}else if(v.getId() == R.id.bSQLdelete) {
			String sRow1 = sqlRow.getText().toString();
			long lRow1 = Long.parseLong(sRow1);
			HotOrNot ex1 = new HotOrNot(this);
			ex1.open();
			ex1.deleteEntry(lRow1);
			ex1.close();
			
		}
	}
}















