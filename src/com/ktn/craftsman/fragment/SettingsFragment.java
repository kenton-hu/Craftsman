package com.ktn.craftsman.fragment;

import com.ktn.craftsman.AddressActivity;
import com.ktn.craftsman.App;
import com.ktn.craftsman.BindPhoneAcitivity;
import com.ktn.craftsman.CraftmanAuthenticationAcitivity;
import com.ktn.craftsman.LoginActivity;
import com.ktn.craftsman.MainActivity;
import com.ktn.craftsman.MyBankCardActivity;
import com.ktn.craftsman.MyCollectActivity;
import com.ktn.craftsman.MyWalletActivity;
import com.ktn.craftsman.NameAuthenticationAcitivity;
import com.ktn.craftsman.PersonInfoAcitivity;
import com.ktn.craftsman.R;
import com.ktn.craftsman.RenewActivity;
import com.ktn.craftsman.util.SPKeys;
import com.ktn.craftsman.util.SharedPrefTool;
import com.ktn.craftsman.util.VDialog;
import com.ktn.craftsman.util.ValueKeys;
import com.squareup.picasso.Picasso;

import android.app.Activity;
import android.content.Intent;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

public class SettingsFragment extends BaseFragment implements OnClickListener{

	private View lytTopInfo;
	private View lytTopchoice;
	private View lytRenew;
	private View lytCard;
	private View lytPersoninfo;
	private View lytNameauthentication;
	private View lytCraftmanauthentication;
	private View lytPhonebind;
	private View lytWallet;
	private View lytMybankcard;
	private View lytAddress;
	private View lytCollect;
	private View lytConnect;
	private TextView tvServiceNumber;
	private View lytQuit;
	
	private ImageView head;
	private TextView name;
	private TextView type;
	private TextView title;
	private TextView balance;
	
	public int getFragmentType() {
        return BaseFragment.FragmentType_settingFragment;
    }
	
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.craftsmancentre, container, false);
        //查找控件
        findView(rootView);
        //设置监听
        setListener();
        
        initView();
        
        return rootView;
    }
	
	private void initView(){
		if(App.baseinfo == null || TextUtils.isEmpty(App.baseinfo.getIdentity()))
			return;
		if(App.baseinfo.getIdentity().equals("consumer")){
			title.setText("用户中心");
			type.setVisibility(View.GONE);
			lytTopchoice.setVisibility(View.GONE);
			lytNameauthentication.setVisibility(View.GONE);
			lytCraftmanauthentication.setVisibility(View.GONE);
			lytCollect.setVisibility(View.VISIBLE);
		}else{
			title.setText("工匠中心");
			type.setVisibility(View.VISIBLE);
			lytTopchoice.setVisibility(View.VISIBLE);
			lytNameauthentication.setVisibility(View.VISIBLE);
			lytCraftmanauthentication.setVisibility(View.VISIBLE);
			lytCollect.setVisibility(View.GONE);
		}
	}
	
	@Override
	public void onResume() {
		super.onResume();
		App.customServiceNum = SharedPrefTool.getValue(SPKeys.SP_USER_INFO, ValueKeys.CUSTOM_SERVICE, getString(R.string.serviceNumber));
		if (!TextUtils.isEmpty(App.customServiceNum)) {
			tvServiceNumber.setText(App.customServiceNum);
		}
		balance.setText("¥"+App.baseinfo.getBalance());
		name.setText(App.baseinfo.getName());
		Picasso.with(getActivity()).load(App.baseinfo.getAvatar())
        .fit().centerCrop()
        .placeholder(R.drawable.defalut)
        .error(R.drawable.defalut)
        .into(head);
		if(App.baseinfo.getJob() != null)
			type.setText(App.baseinfo.getJob().getName().substring(0, 1));
	}
	
	private void findView(View view){
		lytTopInfo = view.findViewById(R.id.topinfo);
		lytTopchoice = view.findViewById(R.id.topchoice);
		lytRenew = view.findViewById(R.id.renew);
		lytCard = view.findViewById(R.id.card);
		lytPersoninfo = view.findViewById(R.id.lyt_personinfo);
		lytNameauthentication = view.findViewById(R.id.lyt_nameauthentication);
		lytCraftmanauthentication = view.findViewById(R.id.lyt_craftman_authentication);
		lytPhonebind = view.findViewById(R.id.lyt_phone_bind);
		lytWallet = view.findViewById(R.id.lyt_wallet);
		lytMybankcard = view.findViewById(R.id.lyt_mybankcard);
		lytAddress = view.findViewById(R.id.lyt_address);
		lytCollect = view.findViewById(R.id.lyt_collect);
		lytConnect = view.findViewById(R.id.lyt_connect);
		tvServiceNumber = (TextView)view.findViewById(R.id.serviceNumber);
		lytQuit = view.findViewById(R.id.lyt_quit);
		title = (TextView)view.findViewById(R.id.tv_title);
		type = (TextView)view.findViewById(R.id.type);
		balance = (TextView)view.findViewById(R.id.balance);
		name = (TextView)view.findViewById(R.id.name);
		head = (ImageView)view.findViewById(R.id.head);
	}

	private void setListener(){
		lytTopInfo.setOnClickListener(this);
		lytRenew.setOnClickListener(this);
		lytCard.setOnClickListener(this);
		lytPersoninfo.setOnClickListener(this);
		lytNameauthentication.setOnClickListener(this);
		lytCraftmanauthentication.setOnClickListener(this);
		lytPhonebind.setOnClickListener(this);
		lytWallet.setOnClickListener(this);
		lytMybankcard.setOnClickListener(this);
		lytAddress.setOnClickListener(this);
		lytCollect.setOnClickListener(this);
		lytConnect.setOnClickListener(this);
		lytQuit.setOnClickListener(this);
	}
	
	@Override
	public void onClick(View v) {
		Intent intent = new Intent();
		switch (v.getId()) {
		case R.id.renew:
			intent.setClass(getActivity(), RenewActivity.class);
			startActivity(intent);
			break;
		case R.id.card:
		case R.id.topinfo:
		case R.id.lyt_personinfo:
			intent.setClass(getActivity(), PersonInfoAcitivity.class);
			startActivity(intent);
			break;
		case R.id.lyt_nameauthentication:
			intent.setClass(getActivity(), NameAuthenticationAcitivity.class);
			startActivity(intent);
			break;
		case R.id.lyt_craftman_authentication:
			intent.setClass(getActivity(), CraftmanAuthenticationAcitivity.class);
			startActivity(intent);
			break;
		case R.id.lyt_phone_bind:
			intent.setClass(getActivity(), BindPhoneAcitivity.class);
			startActivityForResult(intent, 99);
			break;
		case R.id.lyt_wallet:
			intent.setClass(getActivity(), MyWalletActivity.class);
			startActivity(intent);
			break;
		case R.id.lyt_mybankcard:
			intent.setClass(getActivity(), MyBankCardActivity.class);
			startActivity(intent);
			break;
		case R.id.lyt_address:
			intent.setClass(getActivity(), AddressActivity.class);
//			intent.putExtra("address", value);
			startActivity(intent);
			break;
		case R.id.lyt_collect:
			intent.setClass(getActivity(), MyCollectActivity.class);
			startActivity(intent);
			break;
		case R.id.lyt_connect:
			//用intent启动拨打电话  
            Intent it = new Intent(Intent.ACTION_CALL,Uri.parse("tel:"+App.customServiceNum));  
            startActivity(it);  
			break;
		case R.id.lyt_quit:
			VDialog.getDialogInstance().showQuitConfirmDialog(getActivity(), new Handler(){
				@Override
				public void handleMessage(Message msg) {
					switch (msg.what) {
					case 1:
						SharedPrefTool.setValue(SPKeys.SP_USER_INFO, ValueKeys.USER_INFO_AUTO, false);
						SharedPrefTool.setValue(SPKeys.SP_USER_INFO, ValueKeys.USER_INFO_PHONE, "");
						SharedPrefTool.setValue(SPKeys.SP_USER_INFO, ValueKeys.USER_INFO_PASSWORD, "");
						App.isLogin = false;
						getActivity().finish();
						break;
					default:
						break;
					}
				}
			});
			break;
		default:
			break;
		}
	}
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if(requestCode == 99){
			MainActivity mainActivity = (MainActivity)getActivity();
			mainActivity.updateFragement(MainActivity.HOME_ITEM_INDEX);
		}
	}
	
}
