package com.ki.user;
import java.util.LinkedList;
import com.ki.util.FileUtil;
import com.ki.document.Document;
import com.ki.util.DBUtils;

/**
 * id一旦创建不可更改？？？？？
 * title和name可以更改
 * @author ki
 *
 */
public class userAction {
	//-n 创建一个新的文件
	// path -n [-Title] [tag1,tag2,tag3,...]
	public static void createADocument(String path,String title){
		createADocument(path,title,null);
	}
	//rename
	public static void createFile(String path,String title,String tags){
		FileUtil.create(path, title);
		if(tags!=null)
			DBUtils.create(new Document(path,title,tags.split("\\|")));
		else
			DBUtils.create(new Document(path,title,null));
		FileUtil.typora(path,title);
	}
	public static void deleteFile(String path,String title){
		DBUtils.deleteDocument(path,title);
		FileUtil.delete(path,title);
	}
	public static void createADocument(String path,String title,String tags){
		if(!FileUtil.isDirectory(path)){
			String choice = FileUtil.choose("当前目录不存在，是否创建新目录？(y:yes,n:no)");
			if(choice.equals("y")){
				try{
					FileUtil.mkdir(path);
					createFile(path,title,tags);
				}catch(Exception e){
					e.printStackTrace();
				}
				System.out.println("create sucess!");
				
			}else{
				System.out.println("create Fail:笔记本不存在。");
			}
		}else{
			try{
				if(FileUtil.exists(path,title)){
					String choice = FileUtil.choose("文件已经存在，是否覆盖原文件？(y:yes,n:no)");
					if(choice.equals("y")){
						deleteFile(path,title);
						createFile(path,title,tags);

					}else{
						title=title.substring(0, title.length()-3)+"-copy.md";
						if(FileUtil.exists(path,title)){
							System.out.println("副本文件已经存在，打开旧的副本文件");
							openFile(path,title);
						}else{
							//清除旧的记录
							deleteFile(path,title);
							
							createFile(path,title,tags);
							//克隆原件tag
							DBUtils.cloneTag(path,title,path,title.substring(0,title.length()-8)+".md");
						}
					}
				}else{
					//清除旧的记录
					deleteFile(path,title);
					
					createFile(path,title,tags);
				}
			}catch(Exception e){
				e.printStackTrace();
			}
		}
	}
	//path -f title (tag) 或者 path -ft (title) tag (多个以|分割)

	public static void searchByTitle(String path,String title){
		LinkedList<String[]>result=DBUtils.searchTitle(path,title);
		FileUtil.printResult(result);
	}	
	public static void searchByTag(String path,String tag){
		LinkedList<String[]>result=DBUtils.searchTag(path,tag);
		FileUtil.printResult(result);
	}
	public static void searchByTitle(String title){
		LinkedList<String[]>result = DBUtils.searchTitleAll(title);
		FileUtil.printResult(result);
	}
	public static void searchByTag(String tag){
		LinkedList<String[]>result = DBUtils.searchTagAll(tag);
		FileUtil.printResult(result);
	}
	public static void searchByTitleTag(String path,String title,String tag){
		LinkedList<String[]>result = DBUtils.searchTitleTag(path, title, tag);
		FileUtil.printResult(result);
	}
	public static void searchByTitleTag(String title,String tag){
		LinkedList<String[]>result = DBUtils.searchTitleTagAll(title, tag);
		FileUtil.printResult(result);
	}
	

	
	// path/title -o或者 path -o title
	public static void openFile(String path,String title){
		if(FileUtil.exists(path,title)){
			//更新时间
			DBUtils.updateDate(path,title);
			FileUtil.typora(path,title);
		}
		else{
			//删除旧记录
			DBUtils.deleteDocument(path, title);
			String choice = FileUtil.choose("文件不存在，是否创建新文件？(y:yes,n:no)");
			if(choice.equals("y")){
				createADocument(path,title);
			}else{
				System.out.println("open Fail:文件不存在。");
			}
		}
		
	}
	public static void openFile(String filePath) {
		String[] val = FileUtil.split(filePath);
		openFile(val[0],val[1]);
	}
	//path/title -s或者 path -s title
	//多个同事查询
	public static void showMessage(String path,String title){
		LinkedList<String> tagList=DBUtils.showTags(path, title);
		if(title.substring(title.length()-3).equals(".md")){
			tagList = DBUtils.showTags(path,title.substring(0, title.length()-3));
		}else{
			tagList = DBUtils.showTags(path,title);
		}
		StringBuffer text = new StringBuffer("Tags:");
		for(String tag:tagList){
			text.append(tag);
			text.append(",");
		}
		System.out.println(text.toString().substring(0,text.length()-1));
	}
	public static void showMessage(String filePath) {
		String[] val = FileUtil.split(filePath);
		showMessage(val[0],val[1]);
	}
}
