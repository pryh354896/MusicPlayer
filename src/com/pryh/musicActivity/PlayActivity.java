package com.pryh.musicActivity;


import java.io.File;
import java.util.ArrayList;
import java.util.List;

import com.pryh.music.Music;
import com.pryh.musicService.MyService;
import com.pryh.musicService.MyService.MyBinder;
import com.pryh.musicplayer.R;
import com.pryh.ui.FragmentLocal;
import com.pryh.utils.CommonUtils;

import android.R.color;
import android.R.integer;
import android.app.Activity;
import android.app.Notification;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;

public class PlayActivity extends BaseActivity implements OnClickListener{
	
	private ImageButton mButn_pre;
	private ImageButton mButn_play;
	private ImageButton mButn_next;
	private TextView mSongNameView;
	private TextView mTimeLong;
	private ImageView mAlbum;
	private Button mButton_back;
	private Bitmap albmBitmap;
	private boolean isPlay = false;
    private static int mNumber;
    private SeekBar mProgressBar;
    public static MyService mService = null;
	private static List<Music>  list;
	private TextView shareView;
    private Handler handler  = new Handler(){
    	@Override
		public void handleMessage(Message msg) {
    	
    		int position = msg.what;
			int total = mService.mMediaPlayer.getDuration();    //总长度
			int progress = position * 100 / total;
			mProgressBar.setProgress(progress);
			mTimeLong.setText(CommonUtils.toTime(position));
			super.handleMessage(msg);
		}
    };
    
  
 ServiceConnection connction = new ServiceConnection() {
	
	@Override
	public void onServiceDisconnected(ComponentName name) {
	}
	
	@Override
	public void onServiceConnected(ComponentName name, IBinder service) {
		mService = ((MyBinder) service).getService();
        mService.play(mNumber,list);
        mService.notifyShow(mNumber,list,getApplicationContext());
	}
};
          
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.play_layout);
		mButn_pre = (ImageButton) findViewById(R.id.pre);
		mButn_play = (ImageButton) findViewById(R.id.play);
		mButn_next = (ImageButton) findViewById(R.id.next);
		mSongNameView = (TextView) findViewById(R.id.sonName);
		mProgressBar = (SeekBar) findViewById(R.id.proBar);
		mTimeLong = (TextView) findViewById(R.id.timeLong);
		mButton_back = (Button) findViewById(R.id.back);
		mAlbum = (ImageView) findViewById(R.id.iv_album);
		shareView = (TextView) findViewById(R.id.home_data_share);
		shareView.setOnClickListener(this);
		mButn_pre.setOnClickListener(this);
		mButn_play.setOnClickListener(this);
		mButn_next.setOnClickListener(this);
		mButton_back.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		
		mService.mMediaPlayer.setOnCompletionListener(new OnCompletionListener() {
			
			@Override
			public void onCompletion(MediaPlayer mp) {
				mNumber++;
				if (mNumber>FragmentLocal.mMusicList.size()) {
					mNumber = 0;
				}
               mService.play(mNumber,list);
               init();
       		   mService.notifyShow(mNumber,list,getApplicationContext());
			}
			
		});
		
		new Thread(new Runnable() {      //更新进度条
			
			@Override
			public void run() {
				boolean isTrue = true;
				while (isTrue == true) {
					try {
						Thread.sleep(200);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					if (mService.mMediaPlayer == null) {
						isTrue = false;
					} else {
						int position = mService.mMediaPlayer.getCurrentPosition();
						Message msg = handler.obtainMessage();
						msg.what = position;
						handler.sendEmptyMessage(position);
					}

				}
			}
		}).start();
	
	}
	
	@Override
	  protected void onStart() {
	    	Intent intent = new Intent(this,MyService.class);
	    	bindService(intent, connction, BIND_AUTO_CREATE);
		  super.onStart();
	  };
	
	@Override
	protected void onResume() {
		super.onResume();
		mNumber= getIntent().getIntExtra("num", 0);
		if (MyApplication.PLAY==0) {
			list =FragmentLocal.mMusicList; 
		}else {
			list = MyService.mList;
			System.out.println(list.size()+"-----------");
		}
		 init();
	}
	
	
	
	@Override
	protected void onDestroy() {
		unbindService(connction);
		CommonUtils.receycleBitmap(mAlbum, albmBitmap);  //回收图片
		super.onDestroy();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.home_data_share:
			Intent shareIntent = new Intent(Intent.ACTION_SEND); 
			shareIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK 
					| Intent.FLAG_GRANT_READ_URI_PERMISSION);
			Uri uri = Uri.fromFile(new File(getSongUrl(mNumber, list)));
			shareIntent.setType("audo/*"); 
			 shareIntent.putExtra(Intent.EXTRA_SUBJECT, "share music"); 
			 shareIntent.putExtra(Intent.EXTRA_STREAM, uri); 
		     startActivity(Intent.createChooser(shareIntent, "Send music "));
			break;
		case R.id.pre:
			--mNumber;
		       if (mNumber<0) {
		    	   mNumber = FragmentLocal.mMusicList.size()-1;
			   }
		       init();
			   if (mService!=null) {
			    	  mService.play(mNumber,list);
				}
			   mService.notifyShow(mNumber,list,getApplicationContext());
			   isPlay = false;
			   mButn_play.setImageDrawable(getResources().getDrawable(R.drawable.play)); 
			break;
		case R.id.play:
			if (isPlay) {
				  mButn_play.setImageDrawable(getResources().getDrawable(R.drawable.play)); 
				  mService.play(mNumber,list);
				  isPlay = false;
				 
			}else {
				  mButn_play.setImageDrawable(getResources().getDrawable(R.drawable.stop));
				  mService.pausePlay();
			      isPlay = true;
			}
			  mSongNameView.setText(getSongName(mNumber,list));
	          mAlbum.setImageBitmap(getSongAlbum(mNumber, list));
			  mService.notifyShow(mNumber,list,getApplicationContext());
            break;
        case R.id.next:
        	mNumber++;
        	 if (mNumber> FragmentLocal.mMusicList.size()-1) {
					mNumber =0;
				}
        	 init();
     		 mService.notifyShow(mNumber,list,getApplicationContext());
        	 mService.play(mNumber,list);
        	 isPlay = false;
			 mButn_play.setImageDrawable(getResources().getDrawable(R.drawable.play)); 
            break;
		
		default:
			break;
		}
		
		
	}

	
	
	private String getSongName(int num,List<Music> list) {
		String nameString = list.get(num).getName();
		return nameString;
		
	}
	
	private String getSongUrl(int num,List<Music> list) {
		String nameString = list.get(num).getUrl();
		return nameString;
	}
	
	        
	private Bitmap getSongAlbum(int num,List<Music> list) {   //get album
		String albumString = list.get(num).getPicName();
		CommonUtils.receycleBitmap(mAlbum, albmBitmap);
	    Bitmap bitmap = CommonUtils.scaleBitmap(this, albumString);
	    if (bitmap!=null) {
			albmBitmap = bitmap;
		}
	    return albmBitmap;
	}
	
	private void init(){
		list.get(mNumber).setProgress(0);
		mSongNameView.setText(getSongName(mNumber,list));
        mAlbum.setImageBitmap(getSongAlbum(mNumber, list));
	}
	
	
	
}
