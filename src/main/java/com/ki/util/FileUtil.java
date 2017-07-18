package com.ki.util;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Scanner;

public class FileUtil {
	public static final String home = "/home/ki/DailyNote";
	public static Scanner in;
	static{
		in = new Scanner(System.in);
	}
	public static void typora(String path,String title){
		String filePath=merge(path,title);
		try{
			Runtime.getRuntime().exec("typora "+filePath);
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	public static String choose(String printMessage){
		String choice = "";
		while(!(choice.equals("y")||choice.equals("n"))){
			System.out.println(printMessage);
			choice = in.next();
			System.out.println();
		}
		return choice;
	}
	public static String read(String printMessage){
		String read;
		System.out.println(printMessage);
		read = in.nextLine();
		return read;
	}
	public static String formatTitle(String title){
		if(title.length()>3&&title.substring(title.length()-3).equals(".md"))
			return title;
		else
			return title+".md";
	}
	public static String merge(String path,String title) {
		return home+"/"+path+"/"+title;
	}
	public static String merge(String path){
		return home+"/"+path;
	}
	public static String[] split(String filePath){
		String[] array = filePath.split("/");
		int n = array.length;
		if(filePath.substring(filePath.length()-3).equals(".md"))
			return new String[]{array[n-1].substring(0, array[n-1].length()-3),array[n-2]};
		else
			return new String[]{array[n-1],array[n-2]};
	}
	
	public static void printResult(LinkedList<String[]>result){
		StringBuffer text = new StringBuffer("序号\t");
		Iterator<String[]> I = result.iterator();
		for(String s:I.next())
		{
			text.append(s);
			text.append('\t');
		}
		text.append("\n");
		int i = 0;
		while(I.hasNext()){
			text.append(i);
			text.append('\t');
			i++;
			for(String s:I.next())
			{
				text.append(s);
				text.append('\t');
			}
			text.append('\n');
		}
		System.out.println(text);
	}
	public static boolean exists(String path,String title){
		return new File(merge(path,title)).exists();
	}
	public static boolean delete(String path,String title){
		return new File(merge(path,title)).delete();
	}
	public static void rename(String oldPath,String oldTitle,String newPath,String newTitle){
		new File(merge(oldPath,oldTitle)).renameTo(new File(merge(newPath,newTitle)));
	}
	public static void create(String path,String title){
		try {
			new File(merge(path,title)).createNewFile();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	public static boolean isDirectory(String path){
		return new File(merge(path)).isDirectory();
	}
	public static boolean mkdir(String path){
		return new File(merge(path)).mkdir();
	}

}
