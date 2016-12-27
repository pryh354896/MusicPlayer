package com.pryh.ui;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import com.pryh.adapter.LocalAdapter;
import com.pryh.music.Music;
import com.pryh.music.MusicData;
import com.pryh.musicActivity.MyApplication;
import com.pryh.musicActivity.PlayActivity;
import com.pryh.musicService.MyService;
import com.pryh.musicplayer.R;
import com.pryh.utils.CommonUtils;

import android.R.integer;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.Toast;

public class FragmentLocal extends Fragment{
	
	private View mView;
	private ListView mList;
	private LocalAdapter mLocalAdapter;
	public static  List<Music> mMusicList = new ArrayList<Music>();
	private MusicData mMusicData;
	private Cursor mCursor;
	public static int num;
	private int posit;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		mView = inflater.inflate(R.layout.local_layout, null);
		mList = (ListView) mView.findViewById(R.id.song_list);
		mMusicList.clear();
		if (mMusicList.size()==0) {
		mCursor = getActivity().getApplication().getContentResolver().query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, null,
					null, null, MediaStore.Audio.Media.DEFAULT_SORT_ORDER);    // 查询手机音乐文件
	    mMusicData = new MusicData(mMusicList, mCursor);
	    mMusicData.getMusicData();
		}
		mLocalAdapter = new LocalAdapter(getActivity(), R.layout.localsonglist, mMusicList);
		mList.setAdapter(mLocalAdapter);
		mList.setOnItemClickListener(new OnItemClickListener() {
         
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				      MyApplication.PLAY = 0;
				      num = position;
				      Music music = mMusicList.get(position);
				      String url = music.getUrl();
				      Intent intent = new Intent(getActivity(),PlayActivity.class);  //启动播放服务
				      intent.putExtra("num", position);
				      startActivity(intent);   
				      getActivity().startService(new Intent(getActivity(),MyService.class));
			}
		});
		
		mList.setOnItemLongClickListener(new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View view,
					int position, long id) {
				     posit = position;
				     AlertDialog.Builder builder =new AlertDialog.Builder(getActivity());
				     builder.setTitle("确定删除？");
				     builder.setPositiveButton("YES", new OnClickListener() {
						
						@Override
						public void onClick(DialogInterface dialog, int which) {
							
							deletSong(posit, mMusicList);
							mMusicList.remove(posit);
							mLocalAdapter.notifyDataSetChanged();
							Toast.makeText(getActivity(), "已删除"+mMusicList.get(posit).getTitle(), Toast.LENGTH_SHORT).show();
						}
					});
				     
				     builder.setNegativeButton("NO", new OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							dialog.dismiss();
						}
					});
				     
				builder.show(); //
				return true;    //returen false
			}
		});
		return mView;
	}
	@Override
	public void onDestroy() {
		mMusicList.clear();
		super.onDestroy();
	}
	
    
	 private void deletSong(int number,List<Music> list) {  //删除音乐文件
			Music music = list.get(number);
			File file = new File(music.getName());
			if (file.exists()) {
				file.delete();
			}
		}
	
	

}
