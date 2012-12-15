package it.moondroid.customview.example;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class MainActivity extends Activity implements OnClickListener {

	private CustomView myView; 
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		myView = (CustomView)findViewById(R.id.mycustomview);
//		myView.setText("Marco");
		
		Button btnSize = (Button)findViewById(R.id.buttonSize);
		btnSize.setOnClickListener(this);
		
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_main, menu);
		
		return true;
	}

	@Override
	public void onClick(View v) {
		
		myView.setTextSize(36.0f);
		
	}

}
