package com.ktn.craftsman.fragment;

import java.util.ArrayList;
import java.util.List;

import com.alibaba.fastjson.JSON;
import com.ktn.craftsman.App;
import com.ktn.craftsman.JobCategoryAcitivity;
import com.ktn.craftsman.NoticeAcitivity;
import com.ktn.craftsman.NoticeDetailActivity;
import com.ktn.craftsman.R;
import com.ktn.craftsman.SelectCityActivity;
import com.ktn.craftsman.adapter.JobListAdapter;
import com.ktn.craftsman.bean.AD;
import com.ktn.craftsman.bean.ADResult;
import com.ktn.craftsman.bean.HttpResult;
import com.ktn.craftsman.bean.JobCategory;
import com.ktn.craftsman.bean.JobCategoryResult;
import com.ktn.craftsman.bean.Notice;
import com.ktn.craftsman.bean.NoticeResult;
import com.ktn.craftsman.util.AdvertControl;
import com.ktn.craftsman.util.SPKeys;
import com.ktn.craftsman.util.SharedPrefTool;
import com.ktn.craftsman.util.VDialog;
import com.ktn.craftsman.util.ValueKeys;
import com.squareup.picasso.Picasso;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.animation.ScaleAnimation;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;
import cn.finalteam.okhttpfinal.BaseHttpRequestCallback;
import cn.finalteam.okhttpfinal.HttpRequest;
import cn.finalteam.okhttpfinal.RequestParams;

public class HomeFragment extends BaseFragment implements OnClickListener {

	private View lytLocation;
	private TextView city;
	private AutoCompleteTextView  edtSearch;
	private View imgSearch;
	private AdvertControl advertControl;
	private ArrayList<String> advertViewText = new ArrayList<String>();
    private ArrayList<View> advertViews = new ArrayList<View>();
	private View notice;
	private View share;
	private TextView noticeDetail;
	private GridView gridView;
	private JobListAdapter jobListAdapter;
	private ArrayList<Notice> notices;
	private String noticeName;
	private int id;
	
	private ScaleAnimation ItemDownAnim;
	private ScaleAnimation ItemUpAnim;
	private boolean isTouchItem = false;
	private int lastItem = 0;
	
	public int getFragmentType() {
        return BaseFragment.FragmentType_homeFragement;
    }
	
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.homefragment, container, false);
        //查找控件
        findView(rootView);
        initItemAnim();
        //设置监听
        setListener();
       
        loadData();
        
        return rootView;
    }
	
	private void initItemAnim() {
	    //缩小动画
	  ItemDownAnim = new ScaleAnimation(1.0F, 0.80F, 1.0F, 0.80F, 1, 0.5F, 1, 0.5F);
	    ItemDownAnim.setDuration(200L);
	    ItemDownAnim.setFillAfter(true);

	    //放大动画
	  ItemUpAnim = new ScaleAnimation(0.80F, 1.0F, 0.80F, 1.0F, 1, 0.5F, 1, 0.5F);
	    ItemUpAnim.setDuration(100L);
	    ItemUpAnim.setFillAfter(true);
	}
	
	private void findView(View view){
		lytLocation = view.findViewById(R.id.location);
		city = (TextView)view.findViewById(R.id.city);
		edtSearch = (AutoCompleteTextView )view.findViewById(R.id.edit_search);
		imgSearch = view.findViewById(R.id.img_search);
		advertControl = (AdvertControl) view.findViewById(R.id.home_advert);
		advertControl.setFootViewHeight(35);
        advertControl.setViewHeight((int) (App.px2dip(App.getScreenWidth()) / 2.1));
        advertControl.setPointSize(5);
        advertControl.setInterTime(4000);
		notice = view.findViewById(R.id.notice);
		noticeDetail = (TextView)view.findViewById(R.id.noticedetail);
		gridView = (GridView)view.findViewById(R.id.home_grid);
		share = view.findViewById(R.id.share);
	}

	private void setListener(){
		lytLocation.setOnClickListener(this);
		imgSearch.setOnClickListener(this);
		advertControl.setOnClickListener(this);
//		notice.setOnClickListener(this);
		share.setOnClickListener(this);
		noticeDetail.setOnClickListener(this);
		gridView.setOnTouchListener(new OnTouchListener() {
            
            @Override
            public boolean onTouch(View v, MotionEvent event) {
            	if(jobListAdapter ==null || jobListAdapter.getCurIndex() < 0){
            		return false;
            	}
                switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    isTouchItem = true ;
                    gridView.getChildAt(jobListAdapter.getCurIndex()).startAnimation(ItemDownAnim);
                    break;
                case MotionEvent.ACTION_UP:
                   recoverItem();
                    break;
                default:
                    break;
                }
            return false;
            }
        });
		gridView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				JobCategory item = (JobCategory)parent.getAdapter().getItem(position);
				App.JOBCATEGORYINDEX = 0;
				App.JOBCATEGORYID = item.getId();
				App.JOBCATEGORYNAME = item.getName();
				Intent intent = new Intent();
				intent.setClass(getActivity(), JobCategoryAcitivity.class);
				startActivity(intent);
//				MainActivity mainActivity = (MainActivity)getActivity();
//				mainActivity.updateFragement(MainActivity.CATEGORY_ITEM_INDEX);
			}
		});
		
		edtSearch.setOnKeyListener(new OnKeyListener() {
            
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if(keyCode==KeyEvent.KEYCODE_ENTER &&  event.getAction() == KeyEvent.ACTION_DOWN){//修改回车键功能
                    ((InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE))
                    .hideSoftInputFromWindow(
                    getActivity()
                    .getCurrentFocus()
                    .getWindowToken(),
                    InputMethodManager.HIDE_NOT_ALWAYS);
                    String keywords = edtSearch.getText().toString();
                    if(TextUtils.isEmpty(keywords)){
                        VDialog.getDialogInstance().toast(getActivity(), "请输入搜索内容");
                        return true;
                    }
                    Intent intent = new Intent();
                    intent.setClass(getActivity(), JobCategoryAcitivity.class);
                    intent.putExtra("search", true);
                    intent.putExtra("keywords", keywords);
                    startActivity(intent);
                }
                return false;
            } 
        });
	}

	private void recoverItem() {
	    isTouchItem = false ;
	    gridView.getChildAt(jobListAdapter.getCurIndex()).startAnimation(ItemUpAnim);
	    if (lastItem >= 0 && lastItem != jobListAdapter.getCurIndex()) {
	        gridView.getChildAt(lastItem).clearAnimation();
	    }
	    lastItem = jobListAdapter.getCurIndex();
	}
	
	@Override
	public void onClick(View v) {

		Intent it = new Intent();
		switch (v.getId()) {
		case R.id.location:
			it.setClass(getActivity(), SelectCityActivity.class);
			startActivityForResult(it, 11);
			break;
		case R.id.share:
			Intent shareIntent = new Intent();
	        shareIntent.setAction(Intent.ACTION_SEND);
	        shareIntent.putExtra(Intent.EXTRA_TEXT, "我在"+ getResources().getString(R.string.app_name) +"发现很多工匠手艺不错，强烈分享给大家！http://fusion.qq.com/cgi-bin/qzapps/unified_jump?appid=42386151&isTimeline=true&actionFlag=0&params=pname%3Dcom.ktn.craftsman%26versioncode%3D14%26channelid%3D%26actionflag%3D0&from=timeline&isappinstalled=1");
	        shareIntent.setType("text/plain");
	 
	        //设置分享列表的标题，并且每次都显示分享列表
	        startActivity(Intent.createChooser(shareIntent, "分享到"));
			break;
		case R.id.img_search:
			String keywords = edtSearch.getText().toString();
			if(TextUtils.isEmpty(keywords)){
				VDialog.getDialogInstance().toast(getActivity(), "请输入搜索内容");
				return;
			}
//			searchMemberList(App.API_jobsearch,keywords);
			Intent intent = new Intent();
			intent.setClass(getActivity(), JobCategoryAcitivity.class);
			intent.putExtra("search", true);
			intent.putExtra("keywords", keywords);
			startActivity(intent);
			break;
		case R.id.notice:
			it.setClass(getActivity(), NoticeAcitivity.class);
			it.putExtra("NoticeList", JSON.toJSONString(notices));
			startActivity(it);
			break;
		case R.id.noticedetail:
			it.setClass(getActivity(), NoticeDetailActivity.class);
			it.putExtra("Name", noticeName);
			it.putExtra("Id", id);
			startActivity(it);
			break;
		default:
			break;
		}
	}

	protected void loadData() {
	}
	
	@Override
	public void onResume() {
		super.onResume();
		String json = SharedPrefTool.getValue(SPKeys.SP_CRAFTMAN_LIST, ValueKeys.CRAFTMAN_LIST, "");
		if(!TextUtils.isEmpty(json)){
			List<JobCategory> list = JSON.parseArray(json, JobCategory.class);
			jobListAdapter =  new JobListAdapter(getActivity(), (ArrayList<JobCategory>) list);
			gridView.setAdapter(jobListAdapter);
			ArrayList<String> items = new ArrayList<String>();
			for (JobCategory job : list) {
				items.add(job.getName());
			}
			edtSearch.setAdapter(new ArrayAdapter<String>(getActivity() ,R.layout.simple_item ,items));
		}
		sendJobCategorylist(App.API_jobcategorylist);
		String chooseCity = SharedPrefTool.getValue(SPKeys.SP_LOCATE_CITY, ValueKeys.CHOOSED_CITY_NAME, "");
		String locateCity = SharedPrefTool.getValue(SPKeys.SP_LOCATE_CITY, ValueKeys.LOCATE_CITY_NAME, "");	
		if(!TextUtils.isEmpty(chooseCity)){
			showCityName(chooseCity);
		}else if(!TextUtils.isEmpty(locateCity)){
			showCityName(locateCity);
		}else {
			city.setText("北京");
		}
		
		getNoticeList(App.API_notifylist,1,20);
		
		getADList(App.API_adlist,"homeSlide");
	}
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if(resultCode == getActivity().RESULT_OK){
			if(requestCode == 11){
				String chooseCity = SharedPrefTool.getValue(SPKeys.SP_LOCATE_CITY, ValueKeys.CHOOSED_CITY_NAME, "");
				if(!TextUtils.isEmpty(chooseCity)){
					showCityName(chooseCity);
				}
			}
		}
	}
	
	private void sendJobCategorylist(String api){
		RequestParams params = new RequestParams(this);
		HttpRequest.post(App.serverURL+api, params, new BaseHttpRequestCallback<HttpResult>(){
			@Override
			public void onStart() {
				super.onStart();
			}
			
			@Override
			public void onFailure(int errorCode, String msg) {
				super.onFailure(errorCode, msg);
				 VDialog.getDialogInstance().toast(getActivity(), "网络异常，请检查你的网络是否连接后再试");
			}
			
			@Override
			public void onFinish() {
				super.onFinish();
			}
			
			@Override
			protected void onSuccess(HttpResult t) {
				super.onSuccess(t);
				
				if(t.getType().equals(App.SUCCESS)){
					JobCategoryResult list = JSON.parseObject(t.getResults().toString(), JobCategoryResult.class);
					if(list != null && list.getList() != null){
						String json = JSON.toJSONString(list.getList());
						SharedPrefTool.setValue(SPKeys.SP_CRAFTMAN_LIST, ValueKeys.CRAFTMAN_LIST, JSON.toJSONString(list.getList()));
						App.jobCategoryList = new ArrayList<JobCategory>(list.getList());
						jobListAdapter =  new JobListAdapter(getActivity(), list.getList());
						gridView.setAdapter(jobListAdapter);
					}
				}else if(!TextUtils.isEmpty(t.getContent())){
				}
			}
		});
	}
	
	public void showCityName(String cityName){
		if(!TextUtils.isEmpty(cityName)){
			if(cityName.endsWith("市"))
				city.setText(cityName.substring(0, cityName.length()-1));
			else {
				city.setText(cityName);
			}
		}
	}
	
//	public void searchMemberList(String api, String keywords){
//		RequestParams params = new RequestParams(this);
//		params.addFormDataPart("keywords", keywords);
//		HttpRequest.post(App.serverURL+api, params, new BaseHttpRequestCallback<HttpResult>(){
//			@Override
//			public void onStart() {
//				super.onStart();
//				VDialog.getDialogInstance().showLoadingDialog(getActivity(),
//						"正在搜索",true,true,0);
//			}
//			
//			@Override
//			public void onFailure(int errorCode, String msg) {
//				super.onFailure(errorCode, msg);
//				VDialog.getDialogInstance().closeLoadingDialog();
//				VDialog.getDialogInstance().toast(getActivity(), "网络异常，请检查你的网络是否连接后再试");
//			}
//			
//			@Override
//			public void onFinish() {
//				super.onFinish();
//				VDialog.getDialogInstance().closeLoadingDialog();
//			}
//			
//			@Override
//			protected void onSuccess(HttpResult t) {
//				super.onSuccess(t);
//				VDialog.getDialogInstance().closeLoadingDialog();
//				
//				if(t.getType().equals(App.SUCCESS)){
//					CraftManResult craftManResult = JSON.parseObject(t.getResults().toString(), CraftManResult.class);
////					MainActivity mainActivity = (MainActivity)getActivity();
////					mainActivity.updateFragement(MainActivity.CRAFTMAN_ITEM_INDEX);
//					//					adapter = new CraftManAdapter(getActivity(), craftManResult.getList(),jobName.substring(0, 1));
////					craftManList.setAdapter(adapter);
//				}else if(!TextUtils.isEmpty(t.getContent())){
//					VDialog.getDialogInstance().toast(getActivity(), t.getContent());
//				}
//			}
//		});
//	}
	
	public void getNoticeList(String api, int pageNumber, int pageSize){
		RequestParams params = new RequestParams(this);
		params.addFormDataPart("pageNumber", pageNumber);
		params.addFormDataPart("pageSize", pageSize);
		HttpRequest.post(App.serverURL+api, params, new BaseHttpRequestCallback<HttpResult>(){
			
			@Override
			protected void onSuccess(HttpResult t) {
				super.onSuccess(t);
				if(t.getType().equals(App.SUCCESS)){
					NoticeResult result = JSON.parseObject(t.getResults().toString(), NoticeResult.class);
					if(result != null && result.getList().size() != 0 ){
						notices = result.getList();
						noticeName = notices.get(0).getName();
						id = notices.get(0).getId();
						noticeDetail.setText(notices.get(0).getName());
					}
				}else if(!TextUtils.isEmpty(t.getContent())){
				}
			}
		});
	}
	
	public void getADList(String api, String adPosition){
		RequestParams params = new RequestParams(this);
		params.addFormDataPart("adPosition", adPosition);
		HttpRequest.post(App.serverURL+api, params, new BaseHttpRequestCallback<HttpResult>(){
			
			@Override
			protected void onSuccess(HttpResult t) {
				super.onSuccess(t);
				if(t.getType().equals(App.SUCCESS)){
					ADResult result = JSON.parseObject(t.getResults().toString(), ADResult.class);
					if(result != null && result.getList().size() != 0 ){
						for (AD item : result.getList()) {
							ImageView tempImageView = new ImageView(
	                                HomeFragment.this.getActivity());
	                        tempImageView.setScaleType(ScaleType.FIT_XY);
	                        tempImageView.setLayoutParams(new LayoutParams(
	                                		App.getScreenWidth(),
	                                        (int) (App
	                                                .getScreenWidth() / 2.1)));
	                        tempImageView.setId(item.getId());
	                        Picasso.with(getActivity()).load(item.getImage())
	                        .placeholder(R.drawable.img_loading)
	                        .error(R.drawable.pic_no_news)
	                        .into(tempImageView);
	                        advertViews.add(tempImageView);
	                        advertViewText.add(item.getName());
						}
					}else{
						ImageView tempImageView = new ImageView(
		                        HomeFragment.this.getActivity());
		                tempImageView.setLayoutParams(new LayoutParams(App
		                        .getScreenWidth(), (int) (App
		                        .getScreenWidth() / 2.1)));
		                tempImageView.setScaleType(ScaleType.FIT_XY);
		                tempImageView.setId(0);
		                tempImageView.setBackgroundResource(R.drawable.img_load_fail);
		                advertViews.add(tempImageView);
		                advertViewText.add("图片加载失败，请稍后重试");
		                advertControl.setViewTexts(advertViewText);
		                advertControl.setHeadViews(advertViews);
					}
					advertControl.setViewTexts(advertViewText);
	                advertControl.setHeadViews(advertViews);
				}else if(!TextUtils.isEmpty(t.getContent())){
				}
			}
		});
	}
}
