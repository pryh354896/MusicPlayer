package com.pryh.music;

/*
 * 音乐类
 */
public class Music {
	
		  
		private String title;//音乐标题
		private String singer;//歌手名字
		private String album;// 专辑
		private String url;//path
		private long size;// 歌曲大小
		private long time;//时间
		private String name; //歌曲文件名称
		private String picName;   //歌曲图片
		private int progress;     //歌曲播放进度
		private int id;
		
		public int getId() {
			return id;
		}
		public void setId(int id) {
			this.id = id;
		}
		public String getName() {
			return name;
		}
		public void setName(String name) {
			this.name = name;
		}
		public String getTitle() {
			return title;
		}
		public void setTitle(String title) {
			this.title = title;
		}
		public String getSinger() {
			return singer;
		}
		public void setSinger(String singer) {
			this.singer = singer;
		}
		public String getAlbum() {
			return album;
		}
		public void setAlbum(String album) {
			this.album = album;
		}
		public String getUrl() {
			return url;
		}
		public void setUrl(String url) {
			this.url = url;
		}
		public long getSize() {
			return size;
		}
		public void setSize(long size) {
			this.size = size;
		}
		public long getTime() {
			return time;
		}
		public void setTime(long time) {
			this.time = time;
		}
		
		public String getPicName() {
			return picName;
		}
		public void setPicName(String pic) {
			this.picName = pic;
		}
		public int getProgress() {
			return progress;
		}
		public void setProgress(int progress) {
			this.progress = progress;
		}
	
		

}
