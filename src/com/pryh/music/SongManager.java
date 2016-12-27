package com.pryh.music;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import com.pryh.utils.SharedUtils;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.provider.SyncStateContract.Constants;
import android.text.TextUtils;
import android.util.SparseArray;


public class SongManager {
	 public static final int STATE_ALL = 1;
	    public static final int STATE_RANDOM = 2;
	    public static final int STATE_SINGLE = 3;

	    public static final int STATE_PINGING = 100;
	    public static final int STATE_PAUSE = 101;

	    private static SongManager sSongManager;

	    private Context mContext;
	    private SparseArray<Music> mSongInfos;
	    private ArrayList<Music> mSongInfoList;
	    private ArrayList<Integer> mPlayList;
	    private int mCurSongId;

	    private SongManager(Context ctx) {
	        mContext = ctx;
	        mSongInfos = new SparseArray<>();
	        mSongInfoList = new ArrayList<>();
	        mPlayList = new ArrayList<>();
	    }

	    public static SongManager getInstance(Context ctx) {
	        if (sSongManager == null) {
	            sSongManager = new SongManager(ctx);
	        } else {
	            sSongManager.mContext = ctx;
	        }
	        return sSongManager;
	    }

	    public void clearSong() {
	        mPlayList.clear();
	        mSongInfos.clear();
	        mSongInfoList.clear();
	    }

	    public void addSong(Music info) {
	        mSongInfos.put(info.getId(), info);
	    }

	    public void sort() {
	        mSongInfoList.clear();
	        for (int i = 0; i < mSongInfos.size(); i++) {
	        	Music info = mSongInfos.valueAt(i);
	            mSongInfoList.add(info);
	        }

	        Collections.sort(mSongInfoList, new Comparator<Music>() {
	            @Override
	            public int compare(Music lhs, Music rhs) {
	                return lhs.getTitle().compareTo(rhs.getTitle());
	            }
	        });

	    }

	    public void removeSong(int index) {
	        for (int i = 0; i < mPlayList.size(); i++) {
	            if (mPlayList.get(i) == mSongInfos.keyAt(index)) {
	                mPlayList.remove(i);
	                break;
	            }
	        }

	        mSongInfos.removeAt(index);
	        sort();
	    }

	    public Music getCurrentSong() {
	        return mSongInfos.get(mCurSongId);
	    }

	    public void initPlayList() {
	        int mode = SharedUtils.getInt(mContext, "key_play_mode", STATE_ALL);
	        switch (mode) {
	            case STATE_ALL:
	                normalPlayList();
	                break;
	            case STATE_RANDOM:
	                shuffePlayList();
	                break;
	            case STATE_SINGLE:
//	                singlePlayList();
	                break;
	        }
	    }

//	    public void singlePlayList() {
//	        mPlayList.clear();
//	        Music song = getCurrentSong();
//	        if (song == null) {
//	            LastSong lastSong = SongDb.getLastSong(mContext);
//	            if (lastSong != null) {
//	                mPlayList.add(lastSong.getId());
//	            }
//	        } else {
//	            mPlayList.add(song.getId());
//	        }
//
//	    }

	    private void shuffePlayList() {
	        if (mPlayList.size() == 0) {
	            normalPlayList();
	        }
	        Collections.shuffle(mPlayList);
	    }

	    private void normalPlayList() {
	        mPlayList.clear();
	        for (Music info : mSongInfoList) {
	            int id = info.getId();
	            mPlayList.add(id);
	        }
	    }

	    public int getNextSongId() {
	        if (mPlayList.size() == 0) return -1;

	        if (STATE_RANDOM == SharedUtils.getInt(mContext, "key_play_mode", STATE_ALL)) {
	            shuffePlayList();
	            return mPlayList.get(0);
	        }

	        for (int i = 0; i < mPlayList.size(); i++) {
	            int id = mPlayList.get(i);
	            if (mCurSongId == id) {
	                return i + 1 == mPlayList.size() ? mPlayList.get(0) : mPlayList.get(i + 1);
	            }
	        }


	        return mPlayList.get(0);
	    }

	    public int getPreviousSongId() {
	        if (mPlayList.size() == 0) return -1;


	        for (int i = 0; i < mPlayList.size(); i++) {
	            int id = mPlayList.get(i);
	            if (mCurSongId == id) {
	                return i - 1 < 0 ? mPlayList.get(mPlayList.size() - 1) : mPlayList.get(i - 1);
	            }
	        }
	        return mPlayList.get(0);
	    }

	    public void setCurrentSong(int id) {
	        mCurSongId = id;
	    }

//	    public void saveSongToDb() {
//	        SongDb.saveSongInfos(mContext, mSongInfoList);
//	    }
//
//	    public void fetchSongFromDb() {
//	        mSongInfos = SongDb.getTotalSongInfo(mContext);
//	        sort();
//	        initPlayList();
//	    }


	    public Music getSongByIndex(int index) {
	        return mSongInfoList.get(index);
	    }

	    public Music getSongById(int songId) {
	        return mSongInfos.get(songId);
	    }

	    public int getSongSize() {
	        return mSongInfoList.size();
	    }

//	    public Bitmap getAlbumPicById(int songId) {
//	    	Music song = mSongInfos.get(songId);
//	        if (song == null) return null;
//	        String albumPath = song.getAlbum_pic_path();
//	        if (TextUtils.isEmpty(albumPath)) return null;
//
//	        return CommonUtils.scaleBitmap(mContext, albumPath);
//	    }


//	    public void deleteSong(int id) {
//	        SongDb.deleteSongById(mContext, id);
//	        SongInfo info = mSongInfos.get(id);
//	        if (info != null) {
//	            SongInfo currentSong = getCurrentSong();
//	            if (currentSong != null
//	                    && currentSong.getId() == info.getId()) {
//	                mContext.sendBroadcast(new Intent("stop"));
//	            }
//	            File file = new File(info.getPath());
//	            if (file.exists()) file.delete();
//	        }
//
//	        mSongInfos.delete(id);
//	        sort();
//	    }

}
