package com.sjzg.recommend;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;

import com.sjzg.database.DBConn;
import com.sjzg.paper.PaperModel;
import com.sjzg.question.QuestionModel;

public class getQ {
	private static final String DRIVER="com.mysql.jdbc.Driver";
	private static final String URL="jdbc:mysql://172.16.34.91/db_jizhitest?characterEncoding=UTF-8";
	private static final String USER="root";
	private static final String PASSWORD="a7682318";

	
	private static Connection conn;
	private static Statement st;
	private static ResultSet rs;
	
	public void connetdb()
	{
		open();
	}
	public static Connection getConnection()
	{
		try
		{
			Class.forName(DRIVER);
			conn=DriverManager.getConnection(URL,USER,PASSWORD);			
		}catch(Exception e)
		{
			
		}
		return conn;
	}
	public void open()
	{
		try
		{
			Class.forName(DRIVER);
			conn=DriverManager.getConnection(URL,USER,PASSWORD);			
		}catch(Exception e)
		{
			
		}
	}
	public ResultSet selectInfo(String sql)
	{
		try
		{
			Statement st=conn.createStatement();
			rs=st.executeQuery(sql);
		}catch (Exception e)
		{
			close();
		}
		return rs;
	}
	public int noselectInfo(String sql)
	{
		int result=0;
		try
		{
			st=conn.createStatement();
			
			
			result=st.executeUpdate(sql);
			
		}
		catch (Exception e)
		{
			close();
		}
		return result;
	}

	private void close() {
		try
		{
			if(rs!=null)rs.close();
			if(st!=null)st.close();
			if(conn!=null)conn.close();
			
		}catch (Exception e)
		{
			e.printStackTrace();
		}
		
	}
	public static void main(String args[])
	{
		getQ.getq(20);
	}
	public static int[][] getq(int testid)
	{
		HashMap<String,Integer> kg=new HashMap<String,Integer>();
		kg.put("1NF",0);
        kg.put("2NF",1);
        kg.put("3NF",2);
        kg.put("BCNF",3);
        kg.put("主属性",4);
        kg.put("传递函数依赖",5);
        kg.put("决定因素",6);
        kg.put("函数依赖",7);
        kg.put("码",8);
        kg.put("部分函数依赖",9);
        kg.put("非主属性",10);
//		String sql="SELECT question.id,question.stem,question.answer,question.answerkey,question.tag,question.type,r_testpaper_question.q_order "+
//	               "FROM r_testpaper_question,question where r_testpaper_question.paperid=287 and r_testpaper_question.questionid=question.id ORDER BY r_testpaper_question.q_order";
//		Connection conn=getConnection();
//		HashMap<String,ArrayList<String>>questions=new HashMap<String,ArrayList<String>>();
//		int col=0;
//		int [][] Q = null;
//		try {
//			PreparedStatement ps=conn.prepareStatement(sql,java.sql.ResultSet.CONCUR_READ_ONLY);
//			ResultSet rs=ps.executeQuery();
//			String s;
//			rs.last();
//			int sumcol=rs.getRow();
////			System.out.println(sumcol);
//			rs.beforeFirst();
//			Q=new int[sumcol][11];
//			while(rs.next())
//			{
//		       
//		       	s=rs.getString("question.tag");
//		         s.replaceAll("\\?", " "); 
//				s=s.trim();
//				//System.out.println(s+" "+kg.get(s));	
//	            Q[col][kg.get(s)]=1;
//	            col++;
//			}
//			conn.close();
//		} catch (SQLException e) {
//			e.printStackTrace();
//		}
        PaperModel DBfindPaper_result = DBfindPaper(testid);
		
		if (DBfindPaper_result == null){
			return null;
		}
		String questionListStr = DBfindPaper_result.getQuestions();
		System.out.println(questionListStr);
		String[] questionList = questionListStr.split("@@");//分割出questinid
		int questionCount = questionList.length;
		int[][] Q=new int[questionCount][11];
         for(int i=0;i<questionCount;i++)
         {
        	String sql="SELECT Question.QuestionID,Question.Type,Content,Choices,Image,Tag,Share,Answerkey,Difficulty,UserID,Answer,R_question_kp.kpID,Knowledge_point.Description,R_question_kp.CreateAt FROM  Question,Knowledge_point,R_question_kp WHERE Question.QuestionID=? and Question.QuestionID=R_question_kp.QuestionID and R_question_kp.KpID=Knowledge_point.KpID order by QuestionID";	
     		Connection conn = null;
     	    PreparedStatement ps = null;
     	    ResultSet rs = null;
     		String errorString =  "数据库操作异常";
     		try {
     				conn=DBConn.getConnection();
     				ps=conn.prepareStatement(sql);
     				ps.setInt(1,Integer.parseInt(questionList[i]));
     				rs=ps.executeQuery();
     				String s;
     				if(rs.next())
     				 {
     					 System.out.println();
     			       	s=rs.getString("Description");
     			         s.replaceAll("\\?", " "); 
     					s=s.trim();
     					System.out.println(rs.getInt("QuestionID")+" "+s+" "+kg.get(s));	
     		            Q[i][kg.get(s)]=1;
     		           System.out.println("col "+i+" kg.get(s) "+kg.get(s)+"  "+ Q[i][kg.get(s)]);	
     				}
     				conn.close();
     			} catch (SQLException e) {
     				e.printStackTrace();
     			}finally {
     			    if (rs != null) {
     			        try {
     			            rs.close();
     			        } catch (SQLException e) { /* ignored */}
     			    }
     			    if (ps != null) {
     			        try {
     			            ps.close();
     			        } catch (SQLException e) { /* ignored */}
     			    }
     			    if (conn != null) {
     			        try {
     			            conn.close();
     			        } catch (SQLException e) { /* ignored */}
     			    }
     			}
         }
		 
//		System.out.println(col);
		for(int i=0;i<questionCount;i++)
		{
			for(int j=0;j<11;j++)
			{
				System.out.print(Q[i][j]);
			}
			System.out.println();
		}
		return Q;
	}
	public void getQwritefile( int testid) throws UnsupportedEncodingException, IOException
	{
		HashMap<String,Integer> kg=new HashMap<String,Integer>();
		kg.put("1NF",0);
        kg.put("2NF",1);
        kg.put("3NF",2);
        kg.put("BCNF",3);
        kg.put("主属性",4);
        kg.put("传递函数依赖",5);
        kg.put("决定因素",6);
        kg.put("函数依赖",7);
        kg.put("码",8);
        kg.put("部分函数依赖",9);
        kg.put("非主属性",10);
//		String sql="SELECT question.id,question.stem,question.answer,question.answerkey,question.tag,question.type,r_testpaper_question.q_order "+
//	               "FROM r_testpaper_question,question where r_testpaper_question.paperid=287 and r_testpaper_question.questionid=question.id ORDER BY r_testpaper_question.q_order";
//		Connection conn=getConnection();
//		HashMap<String,ArrayList<String>>questions=new HashMap<String,ArrayList<String>>();
//		int col=0;
//		int [][] Q = null;
//		try {
//			PreparedStatement ps=conn.prepareStatement(sql,java.sql.ResultSet.CONCUR_READ_ONLY);
//			ResultSet rs=ps.executeQuery();
//			String s;
//			rs.last();
//			int sumcol=rs.getRow();
////			System.out.println(sumcol);
//			rs.beforeFirst();
//			Q=new int[sumcol][11];
//			while(rs.next())
//			{
//		       
//		       	s=rs.getString("question.tag");
//		         s.replaceAll("\\?", " "); 
//				s=s.trim();
//				//System.out.println(s+" "+kg.get(s));	
//	            Q[col][kg.get(s)]=1;
//	            col++;
//			}
//			conn.close();
//		} catch (SQLException e) {
//			e.printStackTrace();
//		}
        PaperModel DBfindPaper_result = DBfindPaper(testid);
		
		if (DBfindPaper_result == null){
			return;
		}
		String questionListStr = DBfindPaper_result.getQuestions();
		System.out.println(questionListStr);
		String[] questionList = questionListStr.split("@@");//分割出questinid
		int questionCount = questionList.length;
		int[][] Q=new int[questionCount][11];
         for(int i=0;i<questionCount;i++)
         {
        	String sql="SELECT Question.QuestionID,Question.Type,Content,Choices,Image,Tag,Share,Answerkey,Difficulty,UserID,Answer,R_question_kp.kpID,Knowledge_point.Description,R_question_kp.CreateAt FROM  Question,Knowledge_point,R_question_kp WHERE Question.QuestionID=? and Question.QuestionID=R_question_kp.QuestionID and R_question_kp.KpID=Knowledge_point.KpID order by QuestionID";	
     		Connection conn = null;
     	    PreparedStatement ps = null;
     	    ResultSet rs = null;
     		String errorString =  "数据库操作异常";
     		try {
     				conn=DBConn.getConnection();
     				ps=conn.prepareStatement(sql);
     				ps.setInt(1,Integer.parseInt(questionList[i]));
     				rs=ps.executeQuery();
     				String s;
     				if(rs.next())
     				 {
     					 System.out.println();
     			       	s=rs.getString("Description");
     			         s.replaceAll("\\?", " "); 
     					s=s.trim();
     					System.out.println(rs.getInt("QuestionID")+" "+s+" "+kg.get(s));	
     		            Q[i][kg.get(s)]=1;
     		           System.out.println("col "+i+" kg.get(s) "+kg.get(s)+"  "+ Q[i][kg.get(s)]);	
     				}
     				conn.close();
     			} catch (SQLException e) {
     				e.printStackTrace();
     			}finally {
     			    if (rs != null) {
     			        try {
     			            rs.close();
     			        } catch (SQLException e) { /* ignored */}
     			    }
     			    if (ps != null) {
     			        try {
     			            ps.close();
     			        } catch (SQLException e) { /* ignored */}
     			    }
     			    if (conn != null) {
     			        try {
     			            conn.close();
     			        } catch (SQLException e) { /* ignored */}
     			    }
     			}
         }
		 
//		System.out.println(col);
		for(int i=0;i<questionCount;i++)
		{
			for(int j=0;j<11;j++)
			{
				System.out.print(Q[i][j]);
			}
			System.out.println();
		}
		FileOutputStream o= new FileOutputStream("E:/R_data/Q.txt");
		for(int i=0;i<questionCount;i++)
		{
			String col="";
			for(int j=0;j<11;j++)
			{
				if(j!=10)
				{
					col=col+Q[i][j]+",";
				}
				else
				{
					col=col+Q[i][j]+"\r\n";
				}
			}
			
			o.write(col.getBytes("GBK"));
		}
		return;
	}
	public String[] GetQuestionIDlistBytest(int testid)
	{
        
		PaperModel DBfindPaper_result = DBfindPaper(testid);
		
		if (DBfindPaper_result == null){
			return null;
		}
		String questionListStr = DBfindPaper_result.getQuestions();
		System.out.println(questionListStr);
		String[] questionList = questionListStr.split("@@");//分割出questinid
		return questionList;
	}
	public static PaperModel DBfindPaper(int PaperID) {
		System.out.println("完成执行DBfindPaper");
		String sql="SELECT Paper.PaperID,Paper.UserID,Paper.Title,Paper.Description,Paper.Questions,Paper.Tag FROM  Paper,Test WHERE TestID=? and Test.PaperID=Paper.PaperID";
		Connection conn=DBConn.getConnection();
		PaperModel paperModel_result = null;
					
		try {
			PreparedStatement ps=conn.prepareStatement(sql);
			ps.setInt(1, PaperID);
			ResultSet rs=ps.executeQuery();
			
			if(rs.next())
			{
				paperModel_result = new PaperModel();
				paperModel_result.setTitle(rs.getString("Title"));
				paperModel_result.setQuestions(rs.getString("Questions"));
				paperModel_result.setDescription(rs.getString("Description"));
				paperModel_result.setTag(rs.getString("Tag"));
		
	
			}
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
      
		return  paperModel_result;
	}
}
