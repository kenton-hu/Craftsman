package com.ktn.craftsman.adapter;

import java.util.ArrayList;

import com.ktn.craftsman.R;
import com.ktn.craftsman.adapter.CraftManAdapter.ViewHolder;
import com.ktn.craftsman.bean.BankCard;
import com.ktn.craftsman.bean.CraftMan;
import com.squareup.picasso.Picasso;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class BankListAdapter extends BaseAdapter {
	private ArrayList<BankCard> dataList;
	private Context mContext;
	public BankListAdapter(Context context, ArrayList<BankCard> dataList) {
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
            convertView = (View)LayoutInflater.from(mContext).inflate(R.layout.bankcarditem, null);
            viewHolder = new ViewHolder();
            viewHolder.img = (ImageView)convertView.findViewById(R.id.leftimg);
            viewHolder.bankName = (TextView)convertView.findViewById(R.id.bankname);
            viewHolder.cardNo = (TextView)convertView.findViewById(R.id.cardno);
            convertView.setTag(viewHolder);
        }else{
        	viewHolder = (ViewHolder)convertView.getTag();
        }
        BankCard card = dataList.get(position);
        viewHolder.bankName.setText(card.getBankName());
        String code = card.getCardNo();
        viewHolder.cardNo.setText(code.substring(code.length()-4));
        Picasso.with(mContext).load(card.getBankImage())
        .fit().centerCrop()
        .placeholder(R.drawable.defalut)
        .error(R.drawable.defalut)
        .into(viewHolder.img);
		return convertView;
	}

	public class ViewHolder{
        ImageView img;
        TextView bankName;
        TextView cardNo;
    }
}
