package com.pryh.musicActivity;

import java.util.ArrayList;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

public class BaseActivity extends FragmentActivity{
	
	public static ArrayList<Activity> activityLists = new ArrayList<Activity>();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		activityLists.add(this);
		super.onCreate(savedInstanceState);
	}
	
	@Override
	protected void onDestroy() {
		activityLists.remove(this);
		super.onDestroy();
	}
	
	public static void closeActivity() {
		   for (Activity act :activityLists) {
			   if (act!=null) {
				   act.finish();
			    }
		   }
		
	}
	
	

}
