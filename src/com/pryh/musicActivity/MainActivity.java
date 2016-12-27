package com.pryh.musicActivity;

import java.util.ArrayList;
import java.util.List;

import com.pryh.musicplayer.R;
import com.pryh.adapter.FragmentsAdapter;
import com.pryh.adapter.LocalAdapter;
import com.pryh.musicService.MyService;
import com.pryh.ui.FragmentLocal;
import com.pryh.ui.FragmentRecent;
import com.pryh.ui.FragmentSinger;
import com.pryh.ui.MySlidingMenu;
import com.pryh.utils.CommonUtils;
import com.slidingmenu.lib.SlidingMenu;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

public class MainActivity extends BaseActivity {
	
	private Button btn_back;
	private MySlidingMenu mSlingMenu;
	private OnClickListener mListener;
	private List<Fragment> mList;
	private ViewPager mViewPager;
	private FragmentsAdapter mAdapter;
	private Intent intent;
	private Bitmap mBitMap;
	private int mQuality;
	private ImageView pho_image;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);  //去掉标题
		setContentView(R.layout.activity_main);
		init();
	}
	
	private void init(){
		 btn_back = (Button) findViewById(R.id.back);
		 mViewPager = (ViewPager) findViewById(R.id.viewpager);
		 pho_image = (ImageView) findViewById(R.id.person_photo);
		 mList = new ArrayList<Fragment>();
		 mList.add(new FragmentLocal());
         mList.add(new FragmentRecent());
//		 mList.add(new FragmentSinger());
		 mAdapter = new FragmentsAdapter(getSupportFragmentManager(), mList);
		 mViewPager.setAdapter(mAdapter);
		 mViewPager.setCurrentItem(0);
//		int height =  btn_back.getLayoutParams().height;    //获取button高度
//		 mViewPager.setOnPageChangeListener(new OnPageChangeListener() {
//			
//			@Override
//			public void onPageSelected(int arg0) {
//				mViewPager.setCurrentItem(arg0);
//				
//			}
//			
//			@Override
//			public void onPageScrolled(int arg0, float arg1, int arg2) {
//				// TODO Auto-generated method stub
//				
//			}
//			
//			@Override
//			public void onPageScrollStateChanged(int arg0) {
//				// TODO Auto-generated method stub
//				
//			}
//		});
		 mListener = new OnClickListener() {
			@Override
			public void onClick(View v) {
				switch (v.getId()) {
				case R.id.back:
					mSlingMenu.toggle();
					break;
				case R.id.local:
					mViewPager.setCurrentItem(0);
					mSlingMenu.toggle();
					break;
				case R.id.recent:
                    mViewPager.setCurrentItem(1);
					mSlingMenu.toggle();
					break;
				case R.id.artists:
					break;
				case R.id.person_photo:
					getphoto();
					break;
				default:
					break;
				}
				
			}
		};
		mSlingMenu = new MySlidingMenu(this, mListener);
		mSlingMenu.attachToActivity(this, SlidingMenu.SLIDING_CONTENT);      //必须要调用这个方法mSlingMenue
		btn_back.setOnClickListener(mListener);
		
	}
	
	
	private void getphoto() {
		Intent pickImageIntent = new Intent(Intent.ACTION_PICK);
		pickImageIntent.setType("image/*");
		pickImageIntent.putExtra("crop", "true");
		pickImageIntent.putExtra("aspectX", 1);
		pickImageIntent.putExtra("aspectY", 1);
		pickImageIntent.putExtra("outputX", 100);
		pickImageIntent.putExtra("outputY", 100);
		pickImageIntent.putExtra("return-data", true);
		startActivityForResult(pickImageIntent, CommonUtils.REQUEST_CODE_IMAGE);
	}
	
	@Override
	protected void onActivityResult(int arg0, int arg1, Intent arg2) {
		super.onActivityResult(arg0, arg1, arg2);
		if (arg2==null) {
			return;
		}
		Bundle extrasBundle = arg2.getExtras();
		if (extrasBundle!=null) {
			mQuality = 0;
			mBitMap = extrasBundle.getParcelable("data");
			if (mBitMap.getHeight() > 53 || mBitMap.getWidth() > 53) {
				if (mBitMap.getHeight() > mBitMap.getWidth()) {
					mQuality =(int) (53/(float)mBitMap.getHeight()*100+0.5);
				}else {
					mQuality =(int) (53/(float)mBitMap.getWidth()*100+0.5);
				}
				
			}else {
				mQuality = 100;
			}
			pho_image.setImageBitmap(mBitMap);
		}
	}
	      //backpress响应
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK&&event.getAction()==KeyEvent.ACTION_DOWN) {
			AlertDialog.Builder dialog = new AlertDialog.Builder(this);
			dialog.setTitle("");
			dialog.setMessage("确定退出程序？");
			dialog.setCancelable(true);
			dialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					 sendBroadcast(new Intent("stopService"));
					 finish();
//					 System.exit(0);
				}
			});
			dialog.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
				     dialog.dismiss();     
				}
			});
			dialog.show();
		}
		return super.onKeyDown(keyCode, event);
	}
	
	 
	
}
