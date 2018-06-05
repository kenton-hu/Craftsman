package com.ktn.craftsman;

import java.util.ArrayList;

import com.ktn.craftsman.adapter.PayMethodAdapter;
import com.ktn.craftsman.bean.PayMethod;

import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;

public class ChoosePayMethodActivity extends AbstractActivity implements OnClickListener{

	private String fee; 
	private ListView listview;
	private ArrayList<PayMethod> dataList;
	private PayMethodAdapter adapter;
	
	@Override
	protected int loadLayout() {
		return R.layout.choosepaymethod;
	}

	@Override
	protected void findView() {
		listview = (ListView) findViewById(R.id.listview);

	}

	@Override
	protected void onCreate() {

	}

	@Override
	protected void setListener() {
		(findViewById(R.id.mCancel)).setOnClickListener(this);
		(findViewById(R.id.btn_back)).setOnClickListener(this);
		(findViewById(R.id.next)).setOnClickListener(this);
		listview.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				for(int i = 0;i < dataList.size(); i++){
					PayMethod method = dataList.get(i);
					if(i == position)
						method.setChecked(true);
					else
						method.setChecked(false);
				}
				adapter.notifyDataSetChanged();
			}
		});
	}

	@Override
	protected void loadData() {
		fee = getIntent().getStringExtra("fee");
		((TextView)findViewById(R.id.fee)).setText("需支付"+fee);
		
		dataList = new ArrayList<PayMethod>();
		PayMethod method1 = new PayMethod();
		method1.setTitle("余额支付");
		method1.setContent("账户可用余额"+App.baseinfo.getBalance()+"元");
		method1.setChecked(true);
		dataList.add(method1);
		PayMethod method2 = new PayMethod();
		method2.setTitle("支付宝支付");
		method2.setContent("推荐有支付宝账号的用户");
		method2.setChecked(false);
		dataList.add(method2);
		PayMethod method3 = new PayMethod();
		method3.setTitle("微信支付");
		method3.setContent("微信版本6.23以上");
		method3.setChecked(false);
		dataList.add(method3);
		
		adapter = new PayMethodAdapter(this, dataList);
		listview.setAdapter(adapter);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.mCancel:
		case R.id.btn_back:
			finish();
			break;

		default:
			break;
		}
		
	}

}
