package com.ki.document;
import java.util.Date;
import java.util.HashSet;
//import com.ki.util.DBUtils;
public class Document {
	private long id;
	private String title;//Document标题
	private String path;//Document路径
	private HashSet<String> tags;//Document标签
	private HashSet<String> updateTags;

	/**
	private String updateTitle;
	private String updatePath;
	*/
	private Date updateDate;//最后更新日期
	public Document(long id,String path,String title,String[] tags){
		this.id=id;
		this.title=title;
		this.path = path;
		this.tags = new HashSet<String>();
		//this.updateTags = new HashSet<String>();
		if(tags!=null)
			for(String tag:tags){
				this.tags.add(tag);
			}
		//DBUtils.create(this);
		updateDate = new Date();
	}
	public Document(String title,String path,String[]tags){
		this(new Date().getTime(),title,path,tags);
	}
	public Document(String title,String path){
		this(title,path,null);
	}
	public Document(String path,String[] tags){
		this(new Date().getTime(),"",path,tags);
	}
	public Document(String path){
		this(path,new String[0]);
	}

	public long getId(){
		return id;
	}
	public String getTitle(){
		return title;
	}
	public void setTitle(String title){
		this.title = title;
		updateDate = new Date();
	}
	/**
	public void setTitle(String title){
		if(updateTitle!=null)
			this.title=updateTitle;
		this.updateTitle=title;
	}
	*/
	public String getPath(){
		return path;
	}
	public void setPath(String path){
		this.path = path;
		updateDate = new Date();
	}
	/**
	public void setPath(String path){
		if(updatePath!=null)
			this.path = this.updatePath;
		this.updatePath = path;
		this.path = path;
	}
	*/
	public HashSet<String> getTags(){
		return this.tags;
	}
	
	public void addTags(String tag){
		if(!this.tags.contains(tag))
			this.updateTags.add(tag);
		updateDate = new Date();
	}
	
	public void clearTags(){
		tags.clear();
		updateDate = new Date();
	}
	public Date getUpdateDate(){
		return updateDate;
	}
}
