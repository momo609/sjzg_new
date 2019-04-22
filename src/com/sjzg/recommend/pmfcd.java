package com.sjzg.recommend;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Map.Entry;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.rosuda.REngine.Rserve.RserveException;

import com.sjzg.answer.ProcessedAnswerModel;
import com.sjzg.answer.RankByScore;
import com.sjzg.database.DBConn;
import com.sjzg.paper.PaperModel;
import com.sjzg.question.QuestionModel;


public class pmfcd {
	static double P[][];
	static double Q[][];
	static double QT[][];
	static int questionnum;
    static int studentnumber;
  public static ArrayList<String> getstudentnumber(int testid)
  {
	  String sql="select * from Answer where TestID=?";
	  Connection conn=DBConn.getConnection();
	  int studentnumber=-1;
	  ArrayList<String>studentlist=new ArrayList<String>();
	  try {
			PreparedStatement ps=conn.prepareStatement(sql,java.sql.ResultSet.CONCUR_READ_ONLY);
			ps.setInt(1, testid);
			ResultSet rs=ps.executeQuery();
//			rs.last();
//			studentnumber=rs.getRow();
			while(rs.next())
			{
				studentlist.add(rs.getString("UserID"));
			}
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	  return studentlist;
  }
  public HashMap<String,ArrayList<QuestionModel>> pmfcd_recommendtoquestion(int testid) throws UnsupportedEncodingException, IOException, RserveException
  {
//	    new getQ().getQwritefile(testid);  //存储第一次测试题目Q矩阵
//	    new Temp().connectR();
	    int q[][]=new getQ().getq(testid);  //Q矩阵 ,读取推荐题库的Q
	    String [] questionlist=new getQ().GetQuestionIDlistBytest(testid);
	    questionnum=questionlist.length;
	    RankByScore rank=new RankByScore();
	    ArrayList<ArrayList<ProcessedAnswerModel>>studentgroup=rank.groupStudent(testid);
	    List<String>studentlist=new ArrayList<String>();
        for(int i=0;i<studentgroup.get(1).size();i++)
        {
        	studentlist.add(studentgroup.get(1).get(i).getUserID());
        }
//	    ArrayList<String>studentlist=getstudentnumber(testid);
	    studentnumber=studentlist.size()/questionnum;
//	    int questionnum=56;
//	    int studentnumber=12;
	    P=new double[studentnumber][2];
		Q=new double[questionnum][2];
		QT=new double[2][questionnum];
	    double a[][]=new double[studentnumber][questionnum];
	    double sa[][]=new double[studentnumber][questionnum];   
//	    BufferedReader br2 = new BufferedReader(new InputStreamReader(new FileInputStream("E:/R_data/result1.txt")));
	    BufferedReader br2 = new BufferedReader(new InputStreamReader(new FileInputStream("D:/R_data/result1.txt")));
//	    BufferedReader br3 = new BufferedReader(new InputStreamReader(new FileInputStream("E:/R_data/sg.txt")));
	    BufferedReader br3 = new BufferedReader(new InputStreamReader(new FileInputStream("D:/R_data/sg.txt")));
	    double mp[][]=new double[studentnumber][11];  //DINA算法算出的掌握程度
	    double s[]=new double[questionnum];
	    double g[]=new double[questionnum];
	    double Buv[][]=new double[studentnumber][questionnum];
	    double Bu[]=new double[studentnumber];
	    double Bv[]=new double[questionnum];
	    double la[][]=new double[studentnumber][questionnum];
	    double cp[][]=new double[studentnumber][questionnum];
		String data = null;
		int i=0;
		try {
			while((data = br2.readLine())!=null)
			{	
				String[] dataIn=data.split(",");
				System.out.println("ss "+ data);
				for(int j=0;j<dataIn.length/11;j++)
				{
					
					for(int h=0;h<11;h++)
					{
						mp[j][h]=Double.parseDouble(dataIn[j*11+h]);
						System.out.print(mp[j][h]+" ");
					}
					System.out.println();
				}
				i++;
			}		
		} catch (IOException e) {
			e.printStackTrace();
		}
		i=0;
		System.out.println();
		try {
			while((data = br3.readLine())!=null)
			{	
				String[] dataIn=data.split(",");
				System.out.println("ss "+ dataIn[0]+" "+i);
			    g[i]=Double.parseDouble(dataIn[0]);
			    s[i]=Double.parseDouble(dataIn[1]);
				i++;
			}		
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println("studentnumber "+studentnumber+"questionnum "+questionnum);
		for(int z=0;z<studentnumber;z++)
		{
			for(int j=0;j<questionnum;j++)
			{
				for(int h=0;h<11;h++)
				{
					if(q[j][h]==1)
						sa[z][j]=Math.sqrt(mp[z][h]);
				}
					
			}
		}
		for(int z=0;z<studentnumber;z++)
		{
			for(int j=0;j<questionnum;j++)
			{
				if(sa[z][j]!=0)
				{
					a[z][j]=((1-s[j])*sa[z][j])/((1-s[j])*sa[z][j]+g[j]*(1-sa[z][j]));
				}
				else
				{
					a[z][j]=(s[j]*sa[z][j])/(s[j]*sa[z][j]+(1-g[j])*(1-sa[z][j]));
				}
				System.out.print(a[z][j]+" ");
			}
			System.out.println();
		}
		double sumu=0;
		for(int z=0;z<studentnumber;z++)
		{
			sumu=0;
			for(int j=0;j<questionnum;j++)
			{
				sumu=sumu+a[z][j];
			}
			Bu[z]=sumu/questionnum;
		}
		double sumv=0;
		for(int j=0;j<questionnum;j++)
		{
			sumv=0;
			for(int z=0;z<studentnumber;z++)
			{
				sumv=sumv+a[z][j];
			}
			Bv[j]=sumv/studentnumber;
		}
		matrix_factorization(a,5000,0.0002,0.02);
//		System.out.println("P矩阵");
//		for(int h=0;h<P.length;h++)
//		{
//			for(int k=0;k<P[0].length;k++)
//			{
//				System.out.print(P[h][k]);
//			}
//			System.out.println();
//		}
//		System.out.println("QT矩阵");
//		for(int h=0;h<QT.length;h++)
//		{
//			for(int k=0;k<QT[0].length;k++)
//			{
//				System.out.print(QT[h][k]);
//			}
//			System.out.println();
//		}
		double aa[][]=new double[studentnumber][questionnum];
		aa=matrixmutiple(P,QT);
		System.out.println("aa");
		for(int z=0;z<studentnumber;z++)
		{
			for(int j=0;j<questionnum;j++)
			{
				System.out.print(aa[z][j]+" ");
			}
			System.out.println("\n");
		}
		double sumrow=0;
		double sumcol=0;
		double[] averagepro=new double[questionnum];
		double[] averagepeo=new double[studentnumber];
		for(int z=0;z<studentnumber;z++)
		{
			sumrow=0;
			for(int j=0;j<questionnum;j++)
			{
				sumrow=sumrow+aa[z][j];
			}
//			sum=sum+sumrow/56;
			averagepeo[z]=sumrow/questionnum;
		}
		for(int j=0;j<questionnum;j++)
		{
			for(int z=0;z<studentnumber;z++)
			{
				sumcol=sumcol+aa[z][j];
			}
			averagepro[j]=sumcol/studentnumber;
		}
//		double averagetotal=sum/12;
//		System.out.println("avaer "+averagetotal);
		for(int z=0;z<studentnumber;z++)
		{
			for(int j=0;j<questionnum;j++)
			{
				la[z][j]=averagepeo[z]+0.4*(Bu[z]+Bv[j])+0.6*aa[z][j];
//				la[z][j]=0.3*(Bu[z]+Bv[j])+0.7*aa[z][j];
//   			System.out.println("Bu[z]+Bv[j] "+(Bu[z]+Bv[j]));
			}
		}
		System.out.println("la");
		for(int z=0;z<studentnumber;z++)
		{
			for(int j=0;j<questionnum;j++)
			{
				System.out.print(la[z][j]+" ");
			}
			System.out.println("\n");
		}
//		FileOutputStream o2= new FileOutputStream("E:/知识图谱推荐/pmfcd_cp.txt");
//		String writes="";
		double s_average_pro[]=new double[studentnumber];
		for(int z=0;z<studentnumber;z++)
		{
			double sum=0;
			for(int j=0;j<questionnum;j++)
			{
				sum=sum+la[z][j];
			}
			s_average_pro[z]=sum/questionnum;
		}
		HashMap<String,HashMap<String,Double>>rqlist=new HashMap<String,HashMap<String,Double>>();
		for(int z=0;z<studentnumber;z++)
		{
			HashMap<String,Double> pre_stu=new HashMap<String,Double>();
			for(int j=0;j<questionnum;j++)
			{
				if(la[z][j]<s_average_pro[z])
				{
					pre_stu.put(questionlist[j], la[z][j]);
				}
			}
			List<Entry<String, Double>> list = new ArrayList<Entry<String,Double>>(pre_stu.entrySet());
  	    
 	        Collections.sort(list,new Comparator<Map.Entry<String, Double>>() {  
 	              //升序排序  
 	             public int compare(Entry<String, Double> o1, Entry<String,  Double> o2) {  
 	                      return o1.getValue().compareTo(o2.getValue());  
 	                 }  
 	         }); 
 	     rqlist.put(studentlist.get(z), pre_stu);
		}
		HashMap<String,ArrayList<QuestionModel>> recommendquestionlist=new pmfcd().pmfcd_rqlist(rqlist);
		 List<Entry<String, ArrayList<QuestionModel>>> list = new ArrayList<Entry<String,ArrayList<QuestionModel>>>(recommendquestionlist.entrySet());
		 System.out.println("rqlist  "+recommendquestionlist);
		   for (Entry<String, ArrayList<QuestionModel>> e: list) {  
	    	    System.out.println(e.getValue().get(0).getContent());
	     }  
		for(int z=0;z<studentnumber;z++)
		{
			for(int j=0;j<questionnum;j++)
			{
				System.out.print(la[z][j]+" ");
			}
			System.out.println("\n");
		}
		 String insertresult=new insertrecommendquestion().insertquestion(recommendquestionlist);
		return recommendquestionlist;
   
  }
  public String pmfcd_recommend(int testid) throws UnsupportedEncodingException, IOException, RserveException
  {
//	    new getQ().getQwritefile(testid);  //存储第一次测试题目Q矩阵
//	    new Temp().connectR();
	    int q[][]=new getQ().getq(testid);  //Q矩阵 ,读取推荐题库的Q
	    String [] questionlist=new getQ().GetQuestionIDlistBytest(testid);
	    questionnum=questionlist.length;
	    ArrayList<String>studentlist=getstudentnumber(testid);
	    studentnumber=studentlist.size()/questionnum;
//	    int questionnum=56;
//	    int studentnumber=12;
	    P=new double[studentnumber][2];
		Q=new double[questionnum][2];
		QT=new double[2][questionnum];
	    double a[][]=new double[studentnumber][questionnum];
	    double sa[][]=new double[studentnumber][questionnum];   
	    BufferedReader br2 = new BufferedReader(new InputStreamReader(new FileInputStream("E:/R_data/result1.txt")));
//	    BufferedReader br2 = new BufferedReader(new InputStreamReader(new FileInputStream("D:/R_data/result1.txt")));
	    BufferedReader br3 = new BufferedReader(new InputStreamReader(new FileInputStream("E:/R_data/sg.txt")));
//	    BufferedReader br3 = new BufferedReader(new InputStreamReader(new FileInputStream("D:/R_data/sg.txt")));
	    double mp[][]=new double[studentnumber][11];  //DINA算法算出的掌握程度
	    double s[]=new double[questionnum];
	    double g[]=new double[questionnum];
	    double Buv[][]=new double[studentnumber][questionnum];
	    double Bu[]=new double[studentnumber];
	    double Bv[]=new double[questionnum];
	    double la[][]=new double[studentnumber][questionnum];
	    double cp[][]=new double[studentnumber][questionnum];
		String data = null;
		int i=0;
		try {
			while((data = br2.readLine())!=null)
			{	
				String[] dataIn=data.split(",");
				System.out.println("ss "+ data);
				for(int j=0;j<dataIn.length/11;j++)
				{
					
					for(int h=0;h<11;h++)
					{
						mp[j][h]=Double.parseDouble(dataIn[j*11+h]);
						System.out.print(mp[j][h]+" ");
					}
					System.out.println();
				}
				i++;
			}		
		} catch (IOException e) {
			e.printStackTrace();
		}
		i=0;
		System.out.println();
		try {
			while((data = br3.readLine())!=null)
			{	
				String[] dataIn=data.split(",");
				System.out.println("ss "+ dataIn[0]+" "+i);
			    g[i]=Double.parseDouble(dataIn[0]);
			    s[i]=Double.parseDouble(dataIn[1]);
				i++;
			}		
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println("studentnumber "+studentnumber+"questionnum "+questionnum);
		for(int z=0;z<studentnumber;z++)
		{
			for(int j=0;j<questionnum;j++)
			{
				for(int h=0;h<11;h++)
				{
					if(q[j][h]==1)
						sa[z][j]=Math.sqrt(mp[z][h]);
				}
					
			}
		}
		for(int z=0;z<studentnumber;z++)
		{
			for(int j=0;j<questionnum;j++)
			{
				if(sa[z][j]!=0)
				{
					a[z][j]=((1-s[j])*sa[z][j])/((1-s[j])*sa[z][j]+g[j]*(1-sa[z][j]));
				}
				else
				{
					a[z][j]=(s[j]*sa[z][j])/(s[j]*sa[z][j]+(1-g[j])*(1-sa[z][j]));
				}
				System.out.print(a[z][j]+" ");
			}
			System.out.println();
		}
		double sumu=0;
		for(int z=0;z<studentnumber;z++)
		{
			sumu=0;
			for(int j=0;j<questionnum;j++)
			{
				sumu=sumu+a[z][j];
			}
			Bu[z]=sumu/questionnum;
		}
		double sumv=0;
		for(int j=0;j<questionnum;j++)
		{
			sumv=0;
			for(int z=0;z<studentnumber;z++)
			{
				sumv=sumv+a[z][j];
			}
			Bv[j]=sumv/studentnumber;
		}
		matrix_factorization(a,5000,0.0002,0.02);
//		System.out.println("P矩阵");
//		for(int h=0;h<P.length;h++)
//		{
//			for(int k=0;k<P[0].length;k++)
//			{
//				System.out.print(P[h][k]);
//			}
//			System.out.println();
//		}
//		System.out.println("QT矩阵");
//		for(int h=0;h<QT.length;h++)
//		{
//			for(int k=0;k<QT[0].length;k++)
//			{
//				System.out.print(QT[h][k]);
//			}
//			System.out.println();
//		}
		double aa[][]=new double[studentnumber][questionnum];
		aa=matrixmutiple(P,QT);
		System.out.println("aa");
		for(int z=0;z<studentnumber;z++)
		{
			for(int j=0;j<questionnum;j++)
			{
				System.out.print(aa[z][j]+" ");
			}
			System.out.println("\n");
		}
		double sumrow=0;
		double sumcol=0;
		double[] averagepro=new double[questionnum];
		double[] averagepeo=new double[studentnumber];
		for(int z=0;z<studentnumber;z++)
		{
			sumrow=0;
			for(int j=0;j<questionnum;j++)
			{
				sumrow=sumrow+aa[z][j];
			}
//			sum=sum+sumrow/56;
			averagepeo[z]=sumrow/questionnum;
		}
		for(int j=0;j<questionnum;j++)
		{
			for(int z=0;z<studentnumber;z++)
			{
				sumcol=sumcol+aa[z][j];
			}
			averagepro[j]=sumcol/studentnumber;
		}
//		double averagetotal=sum/12;
//		System.out.println("avaer "+averagetotal);
		for(int z=0;z<studentnumber;z++)
		{
			for(int j=0;j<questionnum;j++)
			{
				la[z][j]=averagepeo[z]+0.4*(Bu[z]+Bv[j])+0.6*aa[z][j];
//				la[z][j]=0.3*(Bu[z]+Bv[j])+0.7*aa[z][j];
//   			System.out.println("Bu[z]+Bv[j] "+(Bu[z]+Bv[j]));
			}
		}
		System.out.println("la");
		for(int z=0;z<studentnumber;z++)
		{
			for(int j=0;j<questionnum;j++)
			{
				System.out.print(la[z][j]+" ");
			}
			System.out.println("\n");
		}
//		FileOutputStream o2= new FileOutputStream("E:/知识图谱推荐/pmfcd_cp.txt");
//		String writes="";
		double s_average_pro[]=new double[studentnumber];
		for(int z=0;z<studentnumber;z++)
		{
			double sum=0;
			for(int j=0;j<questionnum;j++)
			{
				sum=sum+la[z][j];
			}
			s_average_pro[z]=sum/questionnum;
		}
		HashMap<String,HashMap<String,Double>>rqlist=new HashMap<String,HashMap<String,Double>>();
		for(int z=0;z<studentnumber;z++)
		{
			HashMap<String,Double> pre_stu=new HashMap<String,Double>();
			for(int j=0;j<questionnum;j++)
			{
				if(la[z][j]<s_average_pro[z])
				{
					pre_stu.put(questionlist[j], la[z][j]);
				}
			}
			List<Entry<String, Double>> list = new ArrayList<Entry<String,Double>>(pre_stu.entrySet());
  	    
 	        Collections.sort(list,new Comparator<Map.Entry<String, Double>>() {  
 	              //升序排序  
 	             public int compare(Entry<String, Double> o1, Entry<String,  Double> o2) {  
 	                      return o1.getValue().compareTo(o2.getValue());  
 	                 }  
 	         }); 
 	     rqlist.put(studentlist.get(z), pre_stu);
		}
		HashMap<String,ArrayList<QuestionModel>> recommendquestionlist=new pmfcd().pmfcd_rqlist(rqlist);
		 List<Entry<String, ArrayList<QuestionModel>>> list = new ArrayList<Entry<String,ArrayList<QuestionModel>>>(recommendquestionlist.entrySet());
		 System.out.println("rqlist  "+recommendquestionlist);
		   for (Entry<String, ArrayList<QuestionModel>> e: list) {  
	    	    System.out.println(e.getValue().get(0).getContent());
	     }  
		for(int z=0;z<studentnumber;z++)
		{
			for(int j=0;j<questionnum;j++)
			{
				System.out.print(la[z][j]+" ");
			}
			System.out.println("\n");
		}
		 String insertresult=new insertrecommendquestion().insertquestion(recommendquestionlist);
		return insertresult;
   
  }
  public  HashMap<String,ArrayList<QuestionModel>> pmfcd_rqlist(HashMap<String,HashMap<String,Double>> qlist)
  {
	  HashMap<String,ArrayList<QuestionModel>> rqlist=new HashMap<String,ArrayList<QuestionModel>>();
	  List<Entry<String, HashMap<String,Double>>> list = new ArrayList<Entry<String,HashMap<String,Double>>>(qlist.entrySet());
 	   for (Entry<String,  HashMap<String,Double>> e: list) {  
 		  List<Entry<String,Double>> list2 = new ArrayList<Entry<String,Double>>(e.getValue().entrySet());
 		 ArrayList<QuestionModel> resultList = new ArrayList<QuestionModel>();
 		  for(Entry<String, Double> e2: list2)
 		  {
 			   String sql="SELECT Question.QuestionID,Question.Type,Content,Choices,Image,Tag,Share,Answerkey,Difficulty,UserID,Answer,R_question_kp.kpID,Description,Knowledge_point.CreateAt FROM  Question,Knowledge_point,R_question_kp WHERE Question.QuestionID=? and R_question_kp.QuestionID=Question.QuestionID and R_question_kp.KpID=Knowledge_point.KpID";
 				Connection conn = null;
 				PreparedStatement ps = null;
 				ResultSet rs = null;
 				String errorString = "数据库操作异常";
 				try {
 					 conn=DBConn.getConnection();
 					 ps=conn.prepareStatement(sql);
 					 ps.setString(1,e2.getKey());
 					 rs=ps.executeQuery();
 					if(rs.next())
 					{
 						QuestionModel questionModel_temp = new QuestionModel();
 						questionModel_temp.setQuestionID(rs.getInt("QuestionID"));
 						questionModel_temp.setType(rs.getInt("Type"));
 						questionModel_temp.setContent(rs.getString("Content"));
 						questionModel_temp.setAnswerkey(rs.getString("Answerkey"));
 						questionModel_temp.setChoices(rs.getString("Choices"));
 						questionModel_temp.setAnswer(rs.getString("Answer"));
 						
 						questionModel_temp.setImage(rs.getString("Image"));
 						questionModel_temp.setUserID(rs.getString("UserID"));
 						questionModel_temp.setTag(rs.getString("Tag"));
 						questionModel_temp.setDifficulty(rs.getInt("Difficulty"));
 						questionModel_temp.setShare(rs.getInt("Share"));
 						questionModel_temp.setCreateAt(rs.getString("CreateAt"));
 						questionModel_temp.setKnowledgeid(rs.getInt("KpID"));
 						questionModel_temp.setKnowledgepoint(rs.getString("Description"));
 						resultList.add(questionModel_temp);
 					}
 					conn.close();
 				} catch (SQLException h) {
 					h.printStackTrace();
 				}finally {
 				    if (rs != null) {
 				        try {
 				            rs.close();
 				        } catch (SQLException h) { /* ignored */}
 				    }
 				    if (ps != null) {
 				        try {
 				            ps.close();
 				        } catch (SQLException h) { /* ignored */}
 				    }
 				    if (conn != null) {
 				        try {
 				            conn.close();
 				        } catch (SQLException h) { /* ignored */}
 				    }
 				}
 		  }
 		 rqlist.put(e.getKey(), resultList);
 		  
	   }  
 	   return rqlist;
  }
  public static void main(String args[]) throws UnsupportedEncodingException, IOException, RserveException
  {
	 
//	    new Temp().connectR();
//	    new getQ().getQwritefile(26);  //存储第一次测试题目Q矩阵
	    int q[][]=new getQ().getq(28);  //Q矩阵 ,读取推荐题库的Q
	    String [] questionlist=new getQ().GetQuestionIDlistBytest(28);
	    questionnum=questionlist.length;
	    ArrayList<String>studentlist=getstudentnumber(28);
	    studentnumber=studentlist.size()/questionnum;
//	    int questionnum=56;
//	    int studentnumber=12;
	    P=new double[studentnumber][2];
		Q=new double[questionnum][2];
		QT=new double[2][questionnum];
	    double a[][]=new double[studentnumber][questionnum];
	    double sa[][]=new double[studentnumber][questionnum];   
	    BufferedReader br2 = new BufferedReader(new InputStreamReader(new FileInputStream("E:/R_data/result1.txt")));
//	    BufferedReader br2 = new BufferedReader(new InputStreamReader(new FileInputStream("D:/R_data/result1.txt")));
	    BufferedReader br3 = new BufferedReader(new InputStreamReader(new FileInputStream("E:/R_data/sg.txt")));
//	    BufferedReader br3 = new BufferedReader(new InputStreamReader(new FileInputStream("D:/R_data/sg.txt")));
	    double mp[][]=new double[studentnumber][11];  //DINA算法算出的掌握程度
	    double s[]=new double[questionnum];
	    double g[]=new double[questionnum];
	    double Buv[][]=new double[studentnumber][questionnum];
	    double Bu[]=new double[studentnumber];
	    double Bv[]=new double[questionnum];
	    double la[][]=new double[studentnumber][questionnum];
	    double cp[][]=new double[studentnumber][questionnum];
		String data = null;
		int i=0;
		try {
			while((data = br2.readLine())!=null)
			{	
				String[] dataIn=data.split(",");
				System.out.println("ss "+ data);
				for(int j=0;j<dataIn.length/11;j++)
				{
					
					for(int h=0;h<11;h++)
					{
						mp[j][h]=Double.parseDouble(dataIn[j*11+h]);
						System.out.print(mp[j][h]+" ");
					}
					System.out.println();
				}
				i++;
			}		
		} catch (IOException e) {
			e.printStackTrace();
		}
		i=0;
		System.out.println();
		try {
			while((data = br3.readLine())!=null)
			{	
				String[] dataIn=data.split(",");
				System.out.println("ss "+ dataIn[0]+" "+i);
			    g[i]=Double.parseDouble(dataIn[0]);
			    s[i]=Double.parseDouble(dataIn[1]);
				i++;
			}		
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println("studentnumber "+studentnumber+"questionnum "+questionnum);
		for(int z=0;z<studentnumber;z++)
		{
			for(int j=0;j<questionnum;j++)
			{
				for(int h=0;h<11;h++)
				{
					if(q[j][h]==1)
						sa[z][j]=Math.sqrt(mp[z][h]);
				}
					
			}
		}
		for(int z=0;z<studentnumber;z++)
		{
			for(int j=0;j<questionnum;j++)
			{
				if(sa[z][j]!=0)
				{
					a[z][j]=((1-s[j])*sa[z][j])/((1-s[j])*sa[z][j]+g[j]*(1-sa[z][j]));
				}
				else
				{
					a[z][j]=(s[j]*sa[z][j])/(s[j]*sa[z][j]+(1-g[j])*(1-sa[z][j]));
				}
				System.out.print(a[z][j]+" ");
			}
			System.out.println();
		}
		double sumu=0;
		for(int z=0;z<studentnumber;z++)
		{
			sumu=0;
			for(int j=0;j<questionnum;j++)
			{
				sumu=sumu+a[z][j];
			}
			Bu[z]=sumu/questionnum;
		}
		double sumv=0;
		for(int j=0;j<questionnum;j++)
		{
			sumv=0;
			for(int z=0;z<studentnumber;z++)
			{
				sumv=sumv+a[z][j];
			}
			Bv[j]=sumv/studentnumber;
		}
		matrix_factorization(a,5000,0.0002,0.02);
//		System.out.println("P矩阵");
//		for(int h=0;h<P.length;h++)
//		{
//			for(int k=0;k<P[0].length;k++)
//			{
//				System.out.print(P[h][k]);
//			}
//			System.out.println();
//		}
//		System.out.println("QT矩阵");
//		for(int h=0;h<QT.length;h++)
//		{
//			for(int k=0;k<QT[0].length;k++)
//			{
//				System.out.print(QT[h][k]);
//			}
//			System.out.println();
//		}
		double aa[][]=new double[studentnumber][questionnum];
		aa=matrixmutiple(P,QT);
		System.out.println("aa");
		for(int z=0;z<studentnumber;z++)
		{
			for(int j=0;j<questionnum;j++)
			{
				System.out.print(aa[z][j]+" ");
			}
			System.out.println("\n");
		}
		double sumrow=0;
		double sumcol=0;
		double[] averagepro=new double[questionnum];
		double[] averagepeo=new double[studentnumber];
		for(int z=0;z<studentnumber;z++)
		{
			sumrow=0;
			for(int j=0;j<questionnum;j++)
			{
				sumrow=sumrow+aa[z][j];
			}
//			sum=sum+sumrow/56;
			averagepeo[z]=sumrow/questionnum;
		}
		for(int j=0;j<questionnum;j++)
		{
			for(int z=0;z<studentnumber;z++)
			{
				sumcol=sumcol+aa[z][j];
			}
			averagepro[j]=sumcol/studentnumber;
		}
//		double averagetotal=sum/12;
//		System.out.println("avaer "+averagetotal);
		for(int z=0;z<studentnumber;z++)
		{
			for(int j=0;j<questionnum;j++)
			{
				la[z][j]=averagepeo[z]+0.4*(Bu[z]+Bv[j])+0.6*aa[z][j];
//				la[z][j]=0.3*(Bu[z]+Bv[j])+0.7*aa[z][j];
//     			System.out.println("Bu[z]+Bv[j] "+(Bu[z]+Bv[j]));
			}
		}
		System.out.println("la");
		for(int z=0;z<studentnumber;z++)
		{
			for(int j=0;j<questionnum;j++)
			{
				System.out.print(la[z][j]+" ");
			}
			System.out.println("\n");
		}
//		FileOutputStream o2= new FileOutputStream("E:/知识图谱推荐/pmfcd_cp.txt");
//		String writes="";
		double s_average_pro[]=new double[studentnumber];
		for(int z=0;z<studentnumber;z++)
		{
			double sum=0;
			for(int j=0;j<questionnum;j++)
			{
				sum=sum+la[z][j];
			}
			s_average_pro[z]=sum/questionnum;
		}
		HashMap<String,HashMap<String,Double>>rqlist=new HashMap<String,HashMap<String,Double>>();
		for(int z=0;z<studentnumber;z++)
		{
			HashMap<String,Double> pre_stu=new HashMap<String,Double>();
			for(int j=0;j<questionnum;j++)
			{
				if(la[z][j]<s_average_pro[z])
				{
					pre_stu.put(questionlist[j], la[z][j]);
				}
			}
			List<Entry<String, Double>> list = new ArrayList<Entry<String,Double>>(pre_stu.entrySet());
    	    
   	        Collections.sort(list,new Comparator<Map.Entry<String, Double>>() {  
   	              //升序排序  
   	             public int compare(Entry<String, Double> o1, Entry<String,  Double> o2) {  
   	                      return o1.getValue().compareTo(o2.getValue());  
   	                 }  
   	         }); 
   	     rqlist.put(studentlist.get(z), pre_stu);
		}
		HashMap<String,ArrayList<QuestionModel>> recommendquestionlist=new pmfcd().pmfcd_rqlist(rqlist);
		 List<Entry<String, ArrayList<QuestionModel>>> list = new ArrayList<Entry<String,ArrayList<QuestionModel>>>(recommendquestionlist.entrySet());
		 System.out.println("rqlist  "+recommendquestionlist);
		   for (Entry<String, ArrayList<QuestionModel>> e: list) {  
	    	    System.out.println(e.getValue().get(0).getContent());
	     }  
		 new insertrecommendquestion().insertquestion(recommendquestionlist);
		for(int z=0;z<studentnumber;z++)
		{
			for(int j=0;j<questionnum;j++)
			{
				System.out.print(la[z][j]+" ");
			}
			System.out.println("\n");
		}
		for(int z=0;z<studentnumber;z++)
		{
			for(int j=0;j<questionnum;j++)
			{
				cp[z][j]=Math.pow(g[j], (1-la[z][j]))*Math.pow((1-s[j]), la[z][j]);
				//System.out.println(Math.pow(g[j], (1-la[z][j]))+" "+Math.pow((1-s[j]), la[z][j]));
				
//				if(j!=55)
//				{
//					writes=writes+String.format("%.0f", cp[z][j])+",";
//				}
//				else
//				{
//					writes=writes+String.format("%.0f", cp[z][j]);
//				}
			}
//			writes=writes+"\r\n";
//			o2.write(writes.getBytes("GBK"));
		}
//		for(int z=0;z<12;z++)
//		{
//			for(int j=0;j<56;j++)
//			{
//				System.out.print(cp[z][j]+" ");
//			}
//			System.out.println("\n");
//		}
	
  }

  public static void matrix_factorization(double a[][], int steps, double alpha, double beta)
  {
	  int k=2;
	  Random r = new Random();
	  for(int l=0;l<studentnumber;l++)
	  {
		  for(int z=0;z<2;z++)
		  { 
			  P[l][z]= r.nextGaussian();
		  }
	  }
	  Random r2 = new Random();
	  for(int l=0;l<questionnum;l++)
	  {
		  for(int z=0;z<2;z++)
		  {
			  Q[l][z]=r.nextGaussian();
		  }
	  }
	  QT=Transpose(Q,questionnum,2);
	  double eij;
	  double e;
	  for (int step=0;step<steps;step++)
	  {
		      for (int z=0;z<a.length ;z++)
		      {
		    	  for (int j=0;j<a[0].length;j++) 
		    	  {
		    		  if (a[z][j]>0)
		    		  {
		    			   eij=a[z][j]-mutiple(P[z],QT,j);  // .dot(P,Q) 表示矩阵内积
		                    for (int h=0;h<k;h++)
		                    {
		                    	P[z][h]=P[z][h]+alpha*(2*eij*QT[h][j]-beta*P[z][h]);
		                    	QT[h][j]=QT[h][j]+alpha*(2*eij*P[z][h]-beta*QT[h][j]);
		                    }
		    		  }
		    	  }
		      }
		                        
		      //eR=numpy.dot(P,Q)
		      e=0;
		      double fm=Frobenius(P,P.length,P[0].length);
		      double fn=Frobenius(QT,QT.length,QT[0].length);
//		      System.out.println("fm "+fm+"fn "+fn);
		      for (int z=0;z<a.length ;z++)
		      {
		    	  for (int j=0;j<a[0].length;j++)
		    	  {
		    		  if (a[z][j]>0)
		    		  {
		    			  e=e+Math.pow((a[z][j]-mutiple(P[z],QT,j)),2);
		                  for (int h=0;h<k;h++)
		                  {
		                	  e=e+(beta/2)*(Math.pow(P[z][h],2)+Math.pow(QT[h][j],2)+0.01*fm+0.01*fn);
		                  }
		    		  }           
		        	}
		        }
		    if(e<0.001)
		       break;
	  }
			            
  }
  public static double Frobenius(double [][] Matrix,int Line,int List)
  {
	  double sum=0;
	  for (int i = 0; i < Line; i++) 
	  {
		  for (int j = 0; j < List; j++) 
		  {
			  sum=sum+Math.pow(Matrix[i][j],2);
          }
	  }
	  return Math.sqrt(sum) ;
  }
  public static double [][] Transpose(double [][] Matrix, int Line, int List) 
  {
	  double [][] MatrixC = new double [List] [Line] ;
	  for (int i = 0; i < Line; i++) 
	  {
		  for (int j = 0; j < List; j++) 
		  {
			  MatrixC[j][i] = Matrix[i][j] ;
          }
	  }
	  return MatrixC ;
   }
  public static double mutiple(double a[], double b[][],int j) 
  {
      //当a的列数与矩阵b的行数不相等时，不能进行点乘，返回null
      if (a.length != b.length)
          return 0.0;
      //c矩阵的行数y，与列数x
      double sum=0;
      for (int i = 0; i < a.length; i++)
      {
    	  sum=sum+a[i]*b[i][j];
      }
                  
      return sum;
  }
  public static double[][] matrixmutiple(double a[][], double b[][]) {
      //当a的列数与矩阵b的行数不相等时，不能进行点乘，返回null
      if (a[0].length != b.length)
          return null;
      //c矩阵的行数y，与列数x
      int y = a.length;
      int x = b[0].length;
      double c[][] = new double[y][x];
      for (int i = 0; i < y; i++)
          for (int j = 0; j < x; j++)
              //c矩阵的第i行第j列所对应的数值，等于a矩阵的第i行分别乘以b矩阵的第j列之和
              for (int k = 0; k < b.length; k++)
                  c[i][j] += a[i][k] * b[k][j];
      return c;
  }


}
