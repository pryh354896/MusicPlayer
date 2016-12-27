package com.pryh.adapter;

import java.util.List;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class FragmentsAdapter extends FragmentPagerAdapter{
    
	private List<Fragment> mList;
	public FragmentsAdapter(FragmentManager fm) {
		super(fm);
		// TODO Auto-generated constructor stub
	}
	public FragmentsAdapter(FragmentManager fm,List<Fragment> lists) {
		super(fm);
		this.mList = lists;
		
	}

	@Override
	public Fragment getItem(int arg0) {
		
		return mList.get(arg0);
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return mList.isEmpty()?0:mList.size();
	}

}
