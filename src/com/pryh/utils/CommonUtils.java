package com.pryh.utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import com.pryh.music.Music;

import android.R.string;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaMetadataRetriever;
import android.os.Environment;
import android.util.TypedValue;
import android.widget.ImageView;

public class CommonUtils {
	
	
	public static final int  REQUEST_CODE_IMAGE = 300;
	          //��ȡ����ͼƬ
	public static String getAlbumPicPath(String path,String name) {
		String pathString = null;
        Bitmap bitmap;
        //�ܹ���ȡ��ý���ļ�Ԫ���ݵ���
        MediaMetadataRetriever retriever = new MediaMetadataRetriever();
        try {
            retriever.setDataSource(path); //��������Դ
            byte[] embedPic = retriever.getEmbeddedPicture(); //�õ��ֽ�������
            bitmap = BitmapFactory.decodeByteArray(embedPic, 0, embedPic.length); //ת��ΪͼƬ
            pathString = imageLocal(bitmap, name);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                retriever.release();
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }
        return pathString;
	}
	
	private static String imageLocal(Bitmap bitmap, String name) {
		
		  String path = null;
	        try {
	            if (!Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
	                return null;
	            }
	            String filePath = Environment.getExternalStorageDirectory().getPath() + "/cache/";
	            File file = new File(filePath, name);
	            if (file.exists()) {
	                path = file.getAbsolutePath();
	                return path;
	            } else {
	                file.getParentFile().mkdirs();
	            }
	            file.createNewFile();
	            OutputStream outStream = new FileOutputStream(file);
	            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outStream);
	            outStream.flush();
	            outStream.close();
	            if (!bitmap.isRecycled()) {
	                bitmap.recycle();
	            }
	            path = file.getAbsolutePath();
	        } catch (Exception e) {
	        }
	        return path;
	}
	     
	
	/*
	 * recyle bitmap
	 */
	public static void receycleBitmap(ImageView imageView,Bitmap bitmap) {
		
		if (bitmap!=null&&!bitmap.isRecycled()) {
			imageView.setImageBitmap(null);
			bitmap.recycle();
		}
	}
	
	     /*
	      * ����bitmap
	      */
	 public static Bitmap scaleBitmap(Context ctx, String bitmapPath) {
	        BitmapFactory.Options options = new BitmapFactory.Options();
	        options.inJustDecodeBounds = true;
	        BitmapFactory.decodeFile(bitmapPath, options);

	        int resultWidth = (int) CommonUtils.dpToPx(ctx, 100);
	        int max = options.outWidth > options.outHeight ? options.outWidth : options.outHeight;
	        float ratio = max * 1.0f / resultWidth;
	        if (ratio < 1.0f) ratio = 1.0f;

	        options.inSampleSize = Math.round(ratio);
	        options.inJustDecodeBounds = false;
	        return BitmapFactory.decodeFile(bitmapPath, options);
	    }
	
	 public static float dpToPx(Context ctx, float dp) {
	        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, ctx.getResources().getDisplayMetrics());
	    }
	
	 
	 public static String toTime(int time) {
			
			time /= 1000;
			int minute = time / 60;
			int seconds = time % 60;
			int hour = minute / 60;
			minute %=60;
			return String.format("%02d:%02d", minute,seconds);
			
		}
	

}
