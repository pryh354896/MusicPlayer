package com.pryh.music;

/*
 * ������
 */
public class Music {
	
		  
		private String title;//���ֱ���
		private String singer;//��������
		private String album;// ר��
		private String url;//path
		private long size;// ������С
		private long time;//ʱ��
		private String name; //�����ļ�����
		private String picName;   //����ͼƬ
		private int progress;     //�������Ž���
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
