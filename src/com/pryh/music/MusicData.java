package com.pryh.music;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import com.pryh.utils.CommonUtils;

import android.R.integer;
import android.content.ContentResolver;
import android.database.Cursor;
import android.provider.MediaStore;

/*
 * 查找手机内存音乐相关数据
 */
public class MusicData {
          
	private List<Music> lists;
	private Cursor cursor;
	private ContentResolver cr;
	private ArrayList<Music> songInfosArrayList = new ArrayList<Music>();
	
	//有参构造函数
	public MusicData(List<Music> lists, Cursor cursor) {
		this.lists = lists;
		this.cursor = cursor;
//		this.cr = cr;
	}
	
	
	
	 /* 歌曲排序
     */
public  void sortList(List<Music> songsInfo) {
	    songInfosArrayList.clear();
	for (int i = 0; i < songsInfo.size(); i++) {
		songInfosArrayList.add(songsInfo.get(i));
	}
	Collections.sort(songInfosArrayList, new Comparator<Music>() {
		@Override
		public int compare(Music lhs, Music rhs) {
			return lhs.getTitle().compareTo(rhs.getTitle());
		}
	});
}
	
	public void getMusicData() {      //查找手机内存音乐文件
		lists.clear();
//		cursor = cr.query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, null,
//				null, null, MediaStore.Audio.Media.DEFAULT_SORT_ORDER);
		if (null == cursor) {
			return;
		}
		int id = 0;
		if (cursor.moveToFirst()) {
			do {
				Music m = new Music();
				String title = cursor.getString(cursor
						.getColumnIndex(MediaStore.Audio.Media.TITLE));
				String singer = cursor.getString(cursor
						.getColumnIndex(MediaStore.Audio.Media.ARTIST));
				if ("<unknown>".equals(singer)) {
					singer = "未";
				}
				String album = cursor.getString(cursor
						.getColumnIndex(MediaStore.Audio.Media.ALBUM));
				long size = cursor.getLong(cursor
						.getColumnIndex(MediaStore.Audio.Media.SIZE));
				long time = cursor.getLong(cursor
						.getColumnIndex(MediaStore.Audio.Media.DURATION));
				String url = cursor.getString(cursor
						.getColumnIndex(MediaStore.Audio.Media.DATA));
				String name = cursor.getString(cursor
						.getColumnIndex(MediaStore.Audio.Media.DISPLAY_NAME));
				String picName = CommonUtils.getAlbumPicPath(url, name);
				m.setId(id++);
				m.setTitle(title);
				m.setSinger(singer);
				m.setAlbum(album);
				m.setSize(size);
				m.setTime(time);
				m.setUrl(url);
				m.setName(name);
				m.setPicName(picName);
				lists.add(m);
			} while (cursor.moveToNext());
			cursor.close();
			sortList(lists);
		}
	}
}
