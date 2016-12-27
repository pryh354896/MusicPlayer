package com.pryh.musicService;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import com.pryh.music.Music;
import com.pryh.musicActivity.BaseActivity;
import com.pryh.musicActivity.PlayActivity;
import com.pryh.musicplayer.R;
import com.pryh.ui.FragmentLocal;
import com.pryh.ui.FragmentRecent;
import com.pryh.ui.MyNotification;
import com.pryh.utils.CommonUtils;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.net.Uri;
import android.os.Binder;
import android.os.IBinder;
import android.provider.MediaStore.Files;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.view.View;
import android.widget.RemoteViews;
import android.widget.Switch;
/*
 * 
 * 播放音乐service
 */
public class MyService extends Service implements Runnable{
	
	public MyBinder mBinder = new MyBinder();
	private int number;
	private String url;
	public static MediaPlayer mMediaPlayer;
	public static ArrayList<Music> mList = new ArrayList<Music>();
	private Myreceiver myBroad ;
	private Music music;
	private ScheduledThreadPoolExecutor executor;
	
	@Override
	public void onCreate() {
		mMediaPlayer = new MediaPlayer();
		myBroad = new Myreceiver();
		IntentFilter intentFilter = new IntentFilter();
		intentFilter.addAction("stopService");
		registerReceiver(myBroad, intentFilter);
		super.onCreate();
	}
	
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		return super.onStartCommand(intent, flags, startId);
	}
	
	@Override
	public IBinder onBind(Intent intent) {
		return mBinder;
	}
	
	public class MyBinder extends Binder{
		    
		public MyService getService() {
			return MyService.this;
		}
		
	}
	@Override
	public void onDestroy() {
		    executor.shutdown();
	        executor = null;
		    unregisterReceiver(myBroad);
		    stopPlay();
		super.onDestroy();
	}
	
	@Override
	public boolean onUnbind(Intent intent) {
		return super.onUnbind(intent);
	}
	   //play
		public void playMusic(String path) {
			

	        if (executor == null) {
	            executor = new ScheduledThreadPoolExecutor(1);
	            executor.scheduleAtFixedRate(this, 0, 500, TimeUnit.MILLISECONDS);
	        }
			    mMediaPlayer.reset();  //播放器参数恢复至最开始状态
				try {
					mMediaPlayer.setDataSource(path);  
					mMediaPlayer.prepare();   //缓冲准备
					mMediaPlayer.start();
					mMediaPlayer.seekTo(music.getProgress());
//					mMediaPlayer.setOnPreparedListener(new PreParedListener(currentTime));
				} catch (IllegalArgumentException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (SecurityException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IllegalStateException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		}
		

		   //play
		public void play(int number,List<Music> list) {
			music = (Music) list.get(number);
			String url = music.getUrl();
			playMusic(url);
			mList.add(music);
			FragmentRecent.adapter.notifyDataSetChanged();
		}
	

		//pause
		public void pausePlay() {
			if (mMediaPlayer!=null&mMediaPlayer.isPlaying()) {
				mMediaPlayer.pause();
			}
		}
		
		
		//stop
		public void stopPlay() {
			if (mMediaPlayer!=null) {
				mMediaPlayer.stop();
				try {
					mMediaPlayer.prepare();
				} catch (IllegalStateException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}
			
		}
		
		/*分享音乐*/
		public void shareMusic() {
			Intent shareIntent = new Intent(Intent.ACTION_SEND); 
			shareIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK 
					| Intent.FLAG_GRANT_READ_URI_PERMISSION);
			Uri uri = Uri.fromFile(new File(music.getUrl()));
//			shareIntent.putExtra(Intent.EXTRA_TITLE, getResources().getString(R.string.app_name)); 
//			//
//			shareIntent.putExtra(Intent.EXTRA_TEXT, getResources().getString(R.string.app_title)
//					+ Constant.WEB_ADDRESS);
//			//share to WeiXin   分享至微信
//			shareIntent.putExtra("Kdescription", getResources().getString(R.string.app_title)
//					+ Constant.WEB_ADDRESS);  
//			
//			View view = findViewById(R.id.host_layout); 
//	        view.buildDrawingCache();  
//	        Bitmap bitmap = view.getDrawingCache();  
//	  
//			
//			File tempFile  = new File(Constant.FILE_COMMON_PATH + "HaloWheel.jpeg");
//			
//			if (FileUtils.saveBitmap(Constant.FILE_COMMON_PATH, "HaloWheel.jpeg", bitmap)){
				 shareIntent.setType("audo/*"); 
				 shareIntent.putExtra(Intent.EXTRA_SUBJECT, "Send audio file"); 
				 shareIntent.putExtra(Intent.EXTRA_STREAM, uri); 
//			} else {
//				shareIntent.setType("text/plain");
//			}
//			
			startActivity(Intent.createChooser(shareIntent, "Send audio file "));
			
		}
		
		public void notifyShow(int number,List<Music> list, Context context){  	
			startForeground(1, MyNotification.getnotifi(number, list, context));      //显示于前台
		}
		      //接收广播停止服务
		private class Myreceiver extends BroadcastReceiver{
			@Override
			public void onReceive(Context context, Intent intent) {
				stopSelf();
				BaseActivity.closeActivity();
			}
			
		}
			@Override
			public void run() {
				if (mMediaPlayer==null||!mMediaPlayer.isPlaying()||music==null) {
					return;	
				}
				music.setProgress(mMediaPlayer.getCurrentPosition());
			}
		
}
