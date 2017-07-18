package com.ki.bin;
/**
 * Hello world!
 *
 */
import com.ki.util.FileUtil;
import com.ki.user.userAction;
public class App 
{
    public static void main( String[] args )
    {
    	if(args.length>=2){
    	switch(args[1]){
    	case "-f":searchByTitle(args);break;
    	case "-ft":searchByTag(args);break;
    	case "-o":open(args);break;
    	case "-s":show(args);break;
    	case "-n":create(args);break;
    	//case "-at"添加tag
    	//case "-rn"重命名
    	//case "-mv"移动文档
    	//case "-d"删除文档
    	//case "-dt"删除tag
    	//case "-ut"更新tag
    	//copy合并
    	//case "-p"打包
    	// case "-removeALL"删除所有
    	//批量删除～是否更改分隔符——目前分隔符为|
      	}
    	}else{
    		System.out.println("wrong input");
    	}
    }
    
    public static void create(String[]args){
    	String path = args[0];
    	String title;
    	String tags;
    	switch(args.length){
    		case 2:{
    			title=FileUtil.read("File name?");
    			tags=FileUtil.read("Tags?");
    			userAction.createADocument(path,FileUtil.formatTitle(title),tags);
    		}break;
    		case 3:{
    			title = args[2];
    			userAction.createADocument(path, FileUtil.formatTitle(title));
    		}break;
    		case 4:{
    			title = args[2];
    			tags = args[3];
    			userAction.createADocument(path, FileUtil.formatTitle(title), tags);
    		}break;
    		default:{
    			System.out.println("create:wrong input");
    		}
    	}
    }
    public static void show(String[]args){
    	if(args.length==2){
    		userAction.showMessage(args[0]);
    	}else if(args.length==3){
    		userAction.showMessage(args[0],FileUtil.formatTitle(args[2]));
    	}else{
    		System.out.println("show:wrong input");
    	}
    }
    public static void open(String[]args){
    	switch(args.length){
    	case 2:userAction.openFile(args[0]);break;
    	case 3:userAction.openFile(args[0],FileUtil.formatTitle(args[2]));break;
    	default:System.out.println("open:wrong input");
    	}
    }
    public static void searchByTitle(String[]args){
    	switch(args.length){
    	case 3:{
    		if(!args[0].equals("-a"))
    			userAction.searchByTitle(args[0],args[2]);
    		else
    			userAction.searchByTitle(args[2]);
    		}break;
    		
    	case 4:{
    		if(!args[0].equals("-a"))
    			userAction.searchByTitleTag(args[0],args[2],args[3]);
    		else
    			userAction.searchByTitleTag(args[2],args[3]);
    		}break;
    	default:System.out.println("search:wrong input");
    	}
    }
    public static void searchByTag(String[]args){
    	switch(args.length){
    	case 3:{
    		if(!args[0].equals("-a"))
    			userAction.searchByTag(args[0],args[2]);
    		else
    			userAction.searchByTag(args[2]);
    		}break;
    	case 4:{
    		if(!args[0].equals("-a"))
    			userAction.searchByTitleTag(args[0],args[2],args[3]);
    		else
    			userAction.searchByTitleTag(args[2],args[3]);
    	}break;
    	default:System.out.println("search:wrong input");
    	}
    }
}