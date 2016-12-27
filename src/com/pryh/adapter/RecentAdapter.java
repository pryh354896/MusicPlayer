package com.pryh.adapter;

import java.util.List;

import com.pryh.adapter.LocalAdapter.ViewHolder;
import com.pryh.music.Music;
import com.pryh.musicplayer.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class RecentAdapter extends ArrayAdapter<Music> {
              

	private int mResourceId;
	private Context mContext;
	public RecentAdapter(Context context, int resource, List<Music> objects) {
		super(context, resource, objects);
		this.mResourceId = resource;
		this.mContext =context;
		// TODO Auto-generated constructor stub
	}
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		
		Music mMusic = getItem(position);
		ViewHolder mViewHolder = null;
		if (convertView == null) {
			convertView = LayoutInflater.from(mContext).inflate(mResourceId, null);
			mViewHolder = new ViewHolder();
			mViewHolder.mSongName = (TextView) convertView.findViewById(R.id.song_name);
			mViewHolder.mDele = (TextView) convertView.findViewById(R.id.delet);
			convertView.setTag(mViewHolder);
		}else {
			mViewHolder = (ViewHolder) convertView.getTag();
		}
		if (mMusic!=null) {
			mViewHolder.mSongName.setText(mMusic.getName());
		}
		
		return convertView;
	}
	
	class ViewHolder {
		TextView mSingerName;
		TextView mSongName;
		TextView mDele;
		
	}
	
}
