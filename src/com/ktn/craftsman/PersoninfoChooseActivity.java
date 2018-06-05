package com.ktn.craftsman;

import android.content.Intent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;

public class PersoninfoChooseActivity extends AbstractActivity implements OnClickListener{

	private LinearLayout male_choose;
	private LinearLayout female_choose;
	private LinearLayout cancel_choose;
	
	@Override
	protected int loadLayout() {
		// TODO Auto-generated method stub
		return R.layout.infoperson_image;
	}

	@Override
	protected void findView() {
		male_choose = (LinearLayout)findViewById(R.id.male_choose);
		female_choose = (LinearLayout)findViewById(R.id.female_choose);
        cancel_choose = (LinearLayout)findViewById(R.id.cancel_choose);
	}

	@Override
	protected void onCreate() {

	}

	@Override
	protected void setListener() {
		male_choose.setOnClickListener(this);
		female_choose.setOnClickListener(this);
		cancel_choose.setOnClickListener(this);
	}

	@Override
	protected void loadData() {

	}

	@Override  
    public boolean onTouchEvent(MotionEvent event){  
        finish();  
        return true;  
    }

	@Override
	public void onClick(View v) {
		Intent intent = new Intent(PersoninfoChooseActivity.this,PersonInfoAcitivity.class);
		switch (v.getId()) {
		case R.id.male_choose:
			intent.putExtra("choice", "male");
			setResult(RESULT_OK, intent);
			finish();
			break;
		case R.id.female_choose:
			intent.putExtra("choice", "female");
			setResult(RESULT_OK, intent);
			finish();
			break;
		case R.id.cancel_choose:
			setResult(RESULT_CANCELED, intent);
			finish();
			break;
		default:
			break;
		}
	}
}
