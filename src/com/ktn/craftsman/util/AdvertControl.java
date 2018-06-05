package com.ktn.craftsman.util;

import java.util.ArrayList;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.GestureDetector.OnGestureListener;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.ViewFlipper;

/**
 * 广告视图控件
 * @time 2013-11-20
 * @author: 叶三星
 * 用该控件的时候，最终必须要用   setHeadViews  ，即要初始化headView 才能用
 */
public class AdvertControl extends FrameLayout implements OnGestureListener{
	
	/**  最上面的顶置图标*/
	protected Drawable topMark;
	/**  正常点图标 */
	protected Drawable dotNormal;
	/**  显示点图标*/
	protected Drawable dotChecked;
	/** 手势判断  */
	private GestureDetector gestureDetector;
	/** 滑动控件 */
	private ViewFlipper viewFlipper;
	/** 点的线性布局 */
	private LinearLayout pointsLayout;
	/**  下面的文本框控件 */
	private TextView  textView;
	/**  下面的点视图 */
	private View[] dotViews;
	private static int ID_RIGHT=1;
	/** 自定义接口 */
	private OnUserClickListener userClick;	
	/** 滚动的视图组  */
	private  ArrayList <View> headView=new ArrayList<View>(); 
	/** 对每个View的文字描述  */
	private  ArrayList <String> viewText=new ArrayList<String>();
	/** 图片是否顶置  */
	private  ArrayList<Boolean>  viewTops=new ArrayList<Boolean>();
	/** 视图组的页数  */
	private int pageCount; 
	/** 下面文字的颜色  */
	private int textColor; 
	/** 文字字体的大小   */
	private int textSize; 
	/**  下面的点的大小 */
	private int pointSize;
	/** 文本控件左填充 */
	private int textLeftPadding;
	/**  设置脚部视图背景 */
	private int footViewBackgroudColor;
	/** 整个视图的高度，单位dp */
	private int viewHeight;
	/** 下面文字和点点的视图高度  */
	private int footViewHeight; 
	/**  设置间隔时间 */
    private int interTime;
    /** 上一个显示的位置 */
	private int lastIndex;
	/**  当前显示位置 */
	private int currentIndex;



	public AdvertControl(Context context) {
		super(context);
	}
	
	public AdvertControl(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	public AdvertControl(Context context, AttributeSet attrs) {
		super(context, attrs);
		initMoRenParams();
	}
	
	public AdvertControl(Context context,ArrayList <View> headView) {
		super(context);
		initMoRenParams();
	}
	
	public View getHeadView(int position){
		if(headView!=null&&position<headView.size()){
			return headView.get(position);
		}
		else return null;
	}
    public  ArrayList <View>  getHeadViews(){
    	return headView;
    }
    
    public boolean setHeadView(int position,View view){
    	if(headView==null){
    		return false;
    	}
    	try{
    		//如果原来这个地方有这个图，先删掉这个图,再在该位置来插入该视图
    		if(headView.get(position)!=null){
    			headView.remove(position);
    			headView.add(position, view);
    		}
    		else {
    	        	headView.add(position, view);
    		}
    		pageCount=headView.size();
    		initGongNeng();
    		return true;
    	}
    	catch(Exception e){
    		e.printStackTrace();
    	}
    	return false;
    }
    public boolean setHeadViews(ArrayList<View> views){
    	try{
    		headView.clear();
    		headView=(ArrayList<View>) views.clone();
    		pageCount=headView.size();
    		initGongNeng();
    
    		return true;
    	}
    	catch(Exception e){
    		e.printStackTrace();
    	}
    	return false;
    }
    
    /** 获取某一个图片是否顶置 */
    public boolean getViewTop(int position){
    	if(viewTops!=null&&position<viewTops.size()){
    		return viewTops.get(position).booleanValue();
    	}
    	return false;
    }
    
    public boolean setViewTop(int position ,boolean  isTop){
    	if(viewTops==null){
    		return false;
    	}
    	try{
    		if(viewTops.get(position)!=null){
    			viewTops.remove(position);
    			viewTops.add(position,isTop);
    		}
    		viewTops.add(position, isTop);
    	}
    	catch(Exception e){
    		e.printStackTrace();
    	}
    	return true;
    }
    
    public ArrayList<Boolean> getViewTops(){
    	return viewTops;
    }
    
    public boolean setViewTops(ArrayList<Boolean> viewTops){
    	try{
    		this.viewTops.clear();
    		this.viewTops=(ArrayList<Boolean>) viewTops.clone();
    		return true;
    	}
    	catch(Exception e){
    		e.printStackTrace();
    	}
    	return false;
    }
    
    public String getViewText(int position){
    	if(viewText!=null&&position<viewText.size()){
			return viewText.get(position);
		}
		else return "";
    }
    
    
    public ArrayList<String> getViewText(){
    	return viewText;
    }
    
    public boolean setViewText(int position,String text){
    	if(viewText==null){
    		return false;
    	}
    	try{
    		if(viewText.get(position)!=null){
    			viewText.remove(position);
    			viewText.add(position,text);
    		}
    		viewText.add(position, text);
    	}
    	catch(Exception e){
    		e.printStackTrace();
    	}
    	return true;
    }
    public boolean setViewTexts(ArrayList<String> texts){
    	try{
    		viewText.clear();
    		viewText=(ArrayList<String>) texts.clone();
    		return true;
    	}
    	catch(Exception e){
    		e.printStackTrace();
    	}
    	return false;
    }
    
    public  int getPageCount(){
    	return pageCount;
    }
    
    public void setPageCount(int pageCount){
    	this.pageCount=pageCount;	
    }
    
    public int getTextColor(){
    	return textColor;
    }
    public void setTextColor(int textColor){
    	this.textColor=textColor;
    }
    
    public int getFootViewColor(){
    	return footViewBackgroudColor;
    }
    public void setFootViewColor(int footViewBackgroudColor){
    	this.footViewBackgroudColor=footViewBackgroudColor;
    }
    
    public int getTextSize(){
    	return textSize;
    }
    
    public void setTextSize(int textSize){
    	this.textSize=textSize;
    }
    
    public int getViewHeight(){
    	return viewHeight;
    }
    
    public void setViewHeight(int viewHeight){
    	this.viewHeight=viewHeight;
    }
    
    public int getFootViewHeight(){
    	return footViewHeight;
    }
    
    public void setFootViewHeight(int footViewheight){
    	this.footViewHeight=footViewheight;
    }
    
    public int getPointSize(){
    	return pointSize;
    }
    
    public void setPointSize(int pointSize){
    	this.pointSize=pointSize;
    }
    
    public void setInterTime(int interTime){
    	this.interTime=interTime;
    }
    
    public int getInterTime(){
    	return interTime;
    }
    
    public void setTopSource(Drawable topSource){
    	topMark=topSource;
    }
    
    public Drawable getTopSource(Drawable topSource){
    	return topMark;
    }
    /**
     * 初始话默认参数
     */
    private void initMoRenParams(){
    	pageCount=0;
    	footViewHeight=50;
    	viewHeight=150;
    	textColor=Color.WHITE;
    	pointSize=7;
    	footViewBackgroudColor=Color.argb(100, 0, 0, 0);
    	textSize=14;
    	interTime=3000;
    	textLeftPadding=10;
    }
   /**
    * 初始化功能 ，保证 图片处于最底层，然后是点层，最后是顶置层
    */
    private void initGongNeng(){
    	initResource();
    	initPages();
    	initPoints();
    	if(pageCount>1){
    		viewFlipper.startFlipping();
    	}
    }
     /**
      * 初始化资源
      */
	  private void initResource(){
		if(pageCount>0){
		   dotViews=getDots(getContext());
		   textView=(TextView)getTextView(getContext());
		   textView.setSingleLine();
		   gestureDetector=new GestureDetector(getContext(), this);
		   currentIndex=0;
		   dotViews[currentIndex].setBackgroundDrawable(getDotChecked(getContext()));
	       textView.setText(getViewText(currentIndex));
		   textView.setTextColor(textColor);
		   textView.setTextSize(textSize);
		   textView.setPadding(textLeftPadding, 0, 0, 0);
		   textView.setVisibility(View.GONE);
		}
	}
    /**
     * 初始化点
     */
	private void initPoints(){
		if(pageCount>0){
		RelativeLayout layout=new RelativeLayout(getContext());
//		layout.setBackgroundColor(footViewBackgroudColor);
		layout.setGravity(Gravity.CENTER);
		LayoutParams params=new LayoutParams(
				LayoutParams.FILL_PARENT,getFixPx(getContext(),footViewHeight));
		params.gravity=Gravity.TOP;
		params.setMargins(0, getFixPx(getContext(),viewHeight-footViewHeight), 0, 0);
		this.addView(layout,params);
		pointsLayout=new LinearLayout(getContext());
		pointsLayout.setId(ID_RIGHT);
		pointsLayout.setOrientation(LinearLayout.HORIZONTAL);
		RelativeLayout.LayoutParams ptParams=new RelativeLayout.LayoutParams(
				LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);
		ptParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
		ptParams.addRule(RelativeLayout.CENTER_VERTICAL,RelativeLayout.TRUE);
		layout.addView(pointsLayout,ptParams);
		for (int i = 0; i <pageCount; i++) {
			pointsLayout.addView(dotViews[i]);
		
		}
		
		RelativeLayout.LayoutParams tvParams=new RelativeLayout.LayoutParams(
				LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);
		tvParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
		tvParams.addRule(RelativeLayout.LEFT_OF, ID_RIGHT);
		layout.addView(textView,tvParams);
		}
	}


    /**
     * 初始化页
     */
	private void initPages(){
		if(pageCount>0){
		viewFlipper=new ViewFlipper(getContext()){
			protected void onDetachedFromWindow() {
				try {super.onDetachedFromWindow(); 
				}catch (Exception e){stopFlipping(); }
			}
			@Override
			public void showNext() {
				super.showNext();
				changeDot();
			}
			@Override
			public void showPrevious() {
				super.showPrevious();
				changeDot();
			}
			private void changeDot(){
				lastIndex=currentIndex;
				if(getCurrentView()!=null){
					currentIndex=((Integer)(getCurrentView().getTag())).intValue();
				}
				else{
					currentIndex=0;
				}
				
				dotViews[currentIndex].setBackgroundDrawable(getDotChecked(getContext()));
				dotViews[lastIndex].setBackgroundDrawable(getDotNormal(getContext()));
				textView.setText(getViewText(currentIndex));
			}
		};
		viewFlipper.setClickable(true);
		viewFlipper.setLongClickable(true);
		viewFlipper.setFlipInterval(interTime);
		LayoutParams vfParams=new LayoutParams(
				LayoutParams.FILL_PARENT,getFixPx(getContext(),viewHeight));
		vfParams.setMargins(0, 0, 0, 0);
		this.addView(viewFlipper,vfParams);

		for (int i = 0; i <pageCount; i++) {
			View c=getHeadView(i);
			if(c!=null){
			  c.setTag(i);
			   viewFlipper.addView(c,new LayoutParams(LayoutParams.FILL_PARENT,LayoutParams.FILL_PARENT));
			}
		}
		setLeftAnimation();
		}
	}

	@Override
	public boolean onDown(MotionEvent e) {return false;}
	@Override
	public void onShowPress(MotionEvent e) {}
	@Override
	public boolean onSingleTapUp(MotionEvent e) {
		
		int index = viewFlipper.getDisplayedChild();
	    View temp=viewFlipper.getChildAt(index);
	    if(userClick!=null){
	       userClick.OnUserClick(temp);		
		   return true;
	    }
	    return false;
	}
	@Override
	public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX,float distanceY) {
		return false;
	}
	@Override
	public void onLongPress(MotionEvent e) {}
	@Override
	public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,float velocityY) {
		float x=e1.getX() - e2.getX();
		/*float y=e1.getY() - e2.getY();
		if(Math.abs(y)>Math.abs(x) || y> adaper.getHeight(getContext())/4)
		
			return true;*/
		if(pageCount>1){
			if (x > 5) { 
				viewFlipper.showNext();
			}else if(x < -5) {  
				setRightAnimation();
				viewFlipper.showPrevious();
				setLeftAnimation();
			}
		}
		return true;
	}

	/**
	 * 设置向左滑动动画
	 */
	private void setLeftAnimation(){
		int width=getScreenWidth(getContext());
		Animation inAnimation=new TranslateAnimation(width, 0, 0, 0);
		inAnimation.setDuration(200);
		viewFlipper.setInAnimation(inAnimation);
		Animation outAnimation=new TranslateAnimation(0,-width, 0, 0);
		outAnimation.setDuration(200);
		viewFlipper.setOutAnimation(outAnimation);
	}

	/**
	 * 设置向右滑动动画
	 */
	private void setRightAnimation(){
		int width=getScreenWidth(getContext());
		Animation inAnimation=new TranslateAnimation(-width, 0, 0, 0);
		inAnimation.setDuration(200);
		viewFlipper.setInAnimation(inAnimation);
		Animation outAnimation=new TranslateAnimation(0,width, 0, 0);
		outAnimation.setDuration(200);
		viewFlipper.setOutAnimation(outAnimation);
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		gestureDetector.onTouchEvent(event);
		return true;
	}

	@Override
	public boolean dispatchTouchEvent(MotionEvent ev) {
		this.onTouchEvent(ev);
		return true;
	}
	
	/**
	 * 获得屏幕宽度
	 * @param context
	 * @return
	 */
	public int getScreenWidth(Context context){
		return context.getResources().getDisplayMetrics().widthPixels;
	}
	/**
	 * 获得屏幕高度
	 * @param context
	 * @return
	 */
	public int getScreenHeight(Context context){
		return context.getResources().getDisplayMetrics().heightPixels;
	}
	/**
	 * DP转PIX
	 * @param context
	 * @param dp
	 * @return
	 */
	public int getFixPx(Context context,int dp){
		float scale=context.getResources().getDisplayMetrics().density;
		return (int)(scale*dp+0.5);
	}
	public void setOnUserClickListener(OnUserClickListener userClick){
		this.userClick=userClick;
	}
	public  interface OnUserClickListener{
		  void OnUserClick(View view);
	}
	
	private View[] getDots(Context context){
		View[] views=new View[pageCount];
		for (int i = 0; i < pageCount; i++) {
			views[i]=getDot(context, i);
		}
		return views;
	}
	
	
	private View getDot(Context context,int position){
		ImageView iv=new ImageView(context);
		iv.setBackgroundDrawable(getDotNormal(context));
		LinearLayout.LayoutParams pointParams=new LinearLayout.LayoutParams(
				getFixPx(context,pointSize), getFixPx(context,pointSize));
		pointParams.setMargins(
				getFixPx(context, 3), 0, 
				getFixPx(context, 3), 0);
		iv.setLayoutParams(pointParams);
		return iv;
	}
	
	private Drawable getDotNormal(Context context) {
		if(dotNormal==null)
			dotNormal=createPonit(context,Color.rgb(163, 163, 163));
		return dotNormal;
	}


	 private Drawable getDotChecked(Context context) {
		if(dotChecked==null)
			dotChecked=createPonit(context,Color.rgb(238, 238, 238));
		return dotChecked;
	}

	
	public View getTextView(Context context){
		TextView tv= new TextView(context);
		LinearLayout.LayoutParams textParams=new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);
		textParams.setMargins(getFixPx(context, 6), 0, getFixPx(context, 6), 0);
		tv.setLayoutParams(textParams);
		return tv;
	}
	
	
	
	public Drawable createPonit(Context context,int color){
		Bitmap bitmap=Bitmap.createBitmap(pointSize, pointSize,Config.ARGB_8888);
		Drawable drawable=new BitmapDrawable(bitmap);
		
		Canvas canvas=new Canvas(bitmap);
		Paint paint= new Paint();
		paint.setColor(color);
		paint.setAntiAlias(true);
		
		canvas.drawCircle(pointSize*1.0f/2, pointSize*1.0f/2, pointSize*1.0f/2, paint);
		drawable.draw(canvas);
		//canvas.restore();
		
		return drawable;
	}

	
}