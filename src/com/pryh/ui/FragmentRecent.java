package com.pryh.ui;

import com.pryh.adapter.LocalAdapter;
import com.pryh.adapter.RecentAdapter;
import com.pryh.music.Music;
import com.pryh.musicActivity.MyApplication;
import com.pryh.musicActivity.PlayActivity;
import com.pryh.musicService.MyService;
import com.pryh.musicplayer.R;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;

public class FragmentRecent extends Fragment{
	private View mView;
	private ListView mList;
	public static RecentAdapter adapter;
                 @Override
              public View onCreateView(LayoutInflater inflater, ViewGroup container,
                        		Bundle savedInstanceState) {
                        	mView = inflater.inflate(R.layout.recent_layout, null);
                        	mList = (ListView) mView.findViewById(R.id.recent_song_list);
                        	adapter = new RecentAdapter(getActivity(), R.layout.localsonglist, MyService.mList);
                            mList.setAdapter(adapter);
                        	mList.setOnItemClickListener(new OnItemClickListener() {
         
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
                      MyApplication.PLAY = 1;
				      Music music =  MyService.mList.get(position);
				      String url = music.getUrl();
				      Intent intent = new Intent(getActivity(),PlayActivity.class);  //启动播放服务
				      intent.putExtra("num", position);
				      startActivity(intent);  
				      getActivity().startService(new Intent(getActivity(),MyService.class));
				    
			}
		});
           return mView;
  }
                 
                 
}
