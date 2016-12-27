package com.pryh.ui;

import java.util.List;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v4.app.NotificationCompat;
import android.widget.RemoteViews;

import com.pryh.music.Music;
import com.pryh.musicActivity.PlayActivity;
import com.pryh.musicplayer.R;
import com.pryh.utils.CommonUtils;
   /*
    * 自定义notifycation
    */
public class MyNotification {
	
	     private static Notification mNotification;
	
	public static Notification getnotifi(int number,List<Music> list, Context context){  
		if (mNotification!=null) {
			return mNotification;
		}
		Music music = (Music) list.get(number);
		String name = music.getName();
		String albumString = music.getPicName();
	    Bitmap bitmap = CommonUtils.scaleBitmap(context, albumString);
		RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.notify_layout);
		remoteViews.setTextViewText(R.id.musicName, music.getName());
		remoteViews.setImageViewBitmap(R.id.album,bitmap);
		Intent intent = new Intent(Intent.ACTION_MAIN);
	     intent.addCategory(Intent.CATEGORY_LAUNCHER);
//		Intent intent = new Intent();
	     intent.setClass(context, PlayActivity.class);
	     intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
	     PendingIntent contentIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_CANCEL_CURRENT);
	     NotificationCompat.Builder builder = new NotificationCompat.Builder(context);
	     builder.setContentIntent(contentIntent)
	             .setContent(remoteViews)
	             .setTicker(context.getString(R.string.app_name))
	             .setSmallIcon(R.drawable.music);
	        mNotification = builder.build();
	        mNotification.bigContentView = remoteViews;
		return mNotification;
//		Notification notification = new Notification(R.drawable.music, "begin play", System.currentTimeMillis());
//		Intent intent = new Intent(this,PlayActivity.class);
//		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
//		PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);
//		notification.setLatestEventInfo(this,"MusicPlayer",name,pendingIntent);    //android6.0 已经舍弃

	}
	
	

}
