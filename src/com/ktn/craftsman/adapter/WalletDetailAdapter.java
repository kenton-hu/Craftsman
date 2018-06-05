package com.ktn.craftsman.adapter;

import java.util.ArrayList;

import com.ktn.craftsman.App;
import com.ktn.craftsman.R;
import com.ktn.craftsman.bean.WalletDetail;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class WalletDetailAdapter extends BaseAdapter {

	private ArrayList<WalletDetail> dataList;
	private Context mContext;
	private String lastTitle = "";
	
	public WalletDetailAdapter(Context context, ArrayList<WalletDetail> dataList) {
        mContext = context;
        this.dataList = dataList;
    }
	
	@Override
	public int getCount() {
		return dataList.size();
	}

	@Override
	public Object getItem(int position) {
		return dataList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder = null;
        if(convertView==null){
            convertView = (View)LayoutInflater.from(mContext).inflate(R.layout.walletitem, null);
            viewHolder = new ViewHolder();
            viewHolder.title = (TextView)convertView.findViewById(R.id.title);
            viewHolder.name = (TextView)convertView.findViewById(R.id.name);
            viewHolder.time = (TextView)convertView.findViewById(R.id.time);
            viewHolder.momo = (TextView)convertView.findViewById(R.id.momo);
            viewHolder.count = (TextView)convertView.findViewById(R.id.count);
            convertView.setTag(viewHolder);
        }else{
        	viewHolder = (ViewHolder)convertView.getTag();
        }
        
        WalletDetail detail = dataList.get(position);
        String createTime = App.Date2StringMMDD(detail.getCreateDate());
        String curTitle = null;
        if(createTime.startsWith("1")){
        	curTitle = createTime.substring(0, 2);
        }else
        	curTitle = createTime.substring(1, 2);
        if(curTitle.equals(lastTitle)){
        	viewHolder.title.setVisibility(View.GONE);
        }else{
        	viewHolder.title.setText(curTitle+"月");
        	viewHolder.title.setVisibility(View.VISIBLE);
        }
        viewHolder.name.setText(detail.getName());
        viewHolder.time.setText(createTime);
        viewHolder.momo.setText(detail.getMemo());
        String countstr = detail.getCredit();
        if(countstr.equals("0.00")){
        	viewHolder.count.setText(detail.getDebit());
        }else{
        	viewHolder.count.setText("+"+detail.getCredit());
        }
        
        return convertView;
	}

	public class ViewHolder{
        TextView title;
        TextView name;
        TextView time;
        TextView momo;
        TextView count;
    }
}
