package com.pryh.ui;


import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.pryh.musicplayer.R;
import com.slidingmenu.lib.SlidingMenu;

public class MySlidingMenu extends SlidingMenu {
	private Context context;
	private OnClickListener mListener;
	private TextView local;
	private TextView rencentPlay;
	private TextView artists;
	private TextView online;
	private ImageView person_photo;

	public MySlidingMenu(Context context, OnClickListener clickListener) {
		super(context);
		this.context = context;
		this.mListener = clickListener;
		setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
		setBehindWidth((int)((context.getResources().getDisplayMetrics().widthPixels)*0.8));
		setMode(SlidingMenu.LEFT);
		setFadeDegree(0.4f);
		setSelectorEnabled(true);
		setShadowWidth(23);
		setMenu(R.layout.sliding_layout);
		init();
	}
	private void init() {
		local = (TextView) findViewById(R.id.local);
		rencentPlay = (TextView) findViewById(R.id.recent);
		artists = (TextView) findViewById(R.id.artists);
		person_photo = (ImageView) findViewById(R.id.person_photo);
		local.setOnClickListener(mListener);
		rencentPlay.setOnClickListener(mListener);
		local.setOnClickListener(mListener);
		person_photo.setOnClickListener(mListener);
	}
	
	@Override
	public void toggle() {
		this.invalidate();
		super.toggle();
	}

}
