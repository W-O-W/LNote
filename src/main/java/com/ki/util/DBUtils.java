package com.ki.util;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;

import com.ki.document.Document;
import java.util.LinkedList;

/**
 * 数据库工具类
 * @author ki
 * 插入Document对象，更新Document对象等
 */
public class DBUtils {
	private static Connection connection = null;
	private static Statement stm = null;
	private static ResultSet resultSet = null;
	private static String dburl="jdbc:mysql://localhost:3306/KI?useUnicode=true&characterEncoding=UTF-8";
	private static String user="root";
	private static String password="zxc490076213";
	private static PreparedStatement pstm=null;
	
	private static final String documentINSERT="INSERT INTO path(id,path,title,updateDate) VALUES(?,?,?,NOW()) ON DUPLICATE KEY UPDATE updateDate = NOW()";
	private static final String tagINSERT = "INSERT INTO tags(id,tag) VALUES((SELECT path.id FROM path WHERE path.path=? and path.title=?),?)";
	private static final String documentUPDATE="UPDATE path SET path=?,title=?,updateDate=now() WHERE id=?";
	private static final String tagUPDATE = "UPDATE tags SET tag=?";
	private static final String dateUPDATE = "UPDATE path SET updateDate=NOW() WHERE path=? and title=?";
	private static final String documentDELETE = "DELETE FROM path WHERE path=? and title=?";
	private static final String tagDELETE = "DELETE FROM tags WHERE id=(SELECT path.id FROM path WHERE path.path=? and path.title=?)";
	private static final String titleSEARCH = "SELECT path,title,updateDate FROM path WHERE path=? and locate(?,title); ";
	private static final String tagSEARCH = "SELECT path,title,updateDate FROM path WHERE path=? and id IN (SELECT id FROM tags WHERE tag=?)";
	private static final String titletagSEARCH = "SELECT path,title,updateDate FROM path WHERE path=? and locate(?,title) and path.id IN (SELECT id FROM tags WHERE tag=?)";
	private static final String titleSEARCHALL = "SELECT path,title,updateDate FROM path WHERE locate(?,title)";
	private static final String tagSEARCHALL = "SELECT path,title,updateDate FROM path WHERE id IN (SELECT id FROM tags WHERE tag=?)";
	private static final String titletagSEARCHALL="SELECT path,title,updateDate FROM path WHERE locate(?,title) and path.id IN (SELECT id FROM tags WHERE tag=?)";
	private static final String tagCLONE = "INSERT INTO tags(id,tag) SELECT (SELECT id FROM path WHERE path=? and title=?),tag FROM tags WHERE id=(SELECT id FROM path WHERE path=? and title=?)";
	
	private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); 
	
	
	//初始化
	{
		try{
			Class.forName("com.mysql.jdbc.Driver");
		}catch(Exception e){
			e.printStackTrace();
		}
		
	}
	
	//获取连接
	public static Connection getConnection(){
		try{
			connection = DriverManager.getConnection(dburl,user,password);
		}catch(SQLException e){
			e.printStackTrace();
		}
		return connection;
	}
	//创建一个新的Document
	public static void create(Document d){
		try{
			connection = getConnection();
			pstm = connection.prepareStatement(documentINSERT);
			pstm.setLong(1,d.getId());
			pstm.setString(2,d.getPath());
			pstm.setString(3, d.getTitle());
			pstm.execute();
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			close(null,pstm,null);
		}
		try{
			pstm = connection.prepareStatement(tagINSERT);
			connection.setAutoCommit(false);
			if(!d.getTags().isEmpty()){
				for(String tag:d.getTags()){
					pstm.setString(1,d.getPath());
					pstm.setString(2, d.getTitle());
					pstm.setString(3, tag);
					pstm.addBatch();
				}
				pstm.executeBatch();
				connection.commit();
			}
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			close(connection,pstm,null);
		}
	}
	/**
	 * 按照sql内容update
	 * @param sql
	 * @param parameter
	 */
	public static void updateDocument(Document d){
		connection = getConnection();
		try{
			pstm=connection.prepareStatement(documentUPDATE);
			pstm.setString(1, d.getTitle());
			pstm.setString(2, d.getPath());
			pstm.executeBatch();
		}catch(Exception e){
			try{
				connection.rollback();
			}catch(SQLException e1){
				e1.printStackTrace();
			}
			e.printStackTrace();
		}finally{
			close(connection,stm,resultSet);
		}
	}
	public static void updateTag(String[]new_tags,long id){
		connection = getConnection();
		try{
			connection.setAutoCommit(false);
			pstm=connection.prepareStatement(tagUPDATE);
			for(String tag:new_tags){
				pstm.setLong(1,id);
				pstm.setString(2,tag);
				pstm.addBatch();
			}
			pstm.executeBatch();
		}catch(Exception e){
			try{
				connection.rollback();
			}catch(SQLException e1){
				e1.printStackTrace();
			}
			e.printStackTrace();
		}finally{
			close(connection,stm,resultSet);
		}
	}

	private static LinkedList<String[]> search(String[] var,String sql){
		connection = getConnection();
		LinkedList<String[]>resultList = new LinkedList<>();
		try{
			pstm = connection.prepareStatement(sql);
			for(int i = 0;i<var.length;i++)
				pstm.setString(i+1, var[i]);
			resultSet = pstm.executeQuery();
			resultList.add(new String[]{"title","path","updateDate"});
			while(resultSet.next()){
				resultList.add(new String[]{resultSet.getString("title"),resultSet.getString("path"),sdf.format(resultSet.getTimestamp("updateDate"))});
			}
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			close(connection,pstm,resultSet);
		}
		return resultList;
	}
	public static LinkedList<String[]> searchTitle(String path,String title){
		return search(new String[]{path,title},titleSEARCH);
	}
	public static LinkedList<String[]> searchTag(String path,String tag){
		return search(new String[]{path,tag},tagSEARCH);
	}
	public static LinkedList<String[]>searchTitleTag(String path,String title,String tag){
		return search(new String[]{path,title,tag},titletagSEARCH);
	}
	public static LinkedList<String[]>searchTitleAll(String title){
		return search(new String[]{title},titleSEARCHALL);
	}
	public static LinkedList<String[]>searchTagAll(String tag){
		return search(new String[]{tag},tagSEARCHALL);
	}
	public static LinkedList<String[]>searchTitleTagAll(String title,String tag){
		return search(new String[]{title,tag},titletagSEARCHALL);
	}
	private static void close(Connection connection,Statement stm,ResultSet rts){
		try{
			if(rts!=null){
				rts.close();
				rts=null;
			}
			
			if(stm != null){
				stm.close();
				stm=null;
			}
			
			if(connection != null){
				connection.close();
				connection=null;
			}
			
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	

	public static LinkedList<String> showTags(String string, String substring) {
		// TODO Auto-generated method stub
		return null;
	}
	public static LinkedList<String> showTagsALL(String path) {
		// TODO Auto-generated method stub
		return null;
	}
	
	//先删tag后删path
	public static void deleteDocument(String path, String title) {
		connection = getConnection();
		//delete tag
		try{
			pstm = connection.prepareStatement(tagDELETE);
			pstm.setString(1, path);
			pstm.setString(2, title);
			pstm.execute();
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			close(null,pstm,null);
		}
		
		try{
			pstm = connection.prepareStatement(documentDELETE);
			pstm.setString(1, path);
			pstm.setString(2, title);
			pstm.execute();
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			close(connection,pstm,null);
		}
		
	}
	public static void updateDate(String path, String title) {
		connection = getConnection();
		try{
			pstm = connection.prepareStatement(dateUPDATE);
			pstm.setString(1, path);
			pstm.setString(2, title);
			pstm.executeUpdate();
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			close(connection,pstm,null);
		}
		
	}
	public static void cloneTag(String path,String title,String fromPath,String fromTitle){
		connection = getConnection();
		try{
			pstm = connection.prepareStatement(tagCLONE);
			pstm.setString(1, path);
			pstm.setString(2, title);
			pstm.setString(3, fromPath);
			pstm.setString(4, fromTitle);
			
			pstm.executeUpdate();
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			close(connection,pstm,null);
		}
	}
}
