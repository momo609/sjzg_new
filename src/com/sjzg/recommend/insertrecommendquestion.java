package com.sjzg.recommend;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import com.sjzg.database.DBConn;
import com.sjzg.question.QuestionModel;

public class insertrecommendquestion {
public String insertquestion( HashMap<String,ArrayList<QuestionModel>> rqlist)
{

	   System.out.println("执行DBcreateQuestion");
		String sql="INSERT INTO r_question(Userid,questionid,type,content,choices,answer,answerkey,tag,difficulty,share,knowledgepoint,knowledgeid) VALUES(?,?,?,?,?,?,?,?,?,?,?,?)";
		int influence=0;//影响的行数
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		String errorString = "数据库操作异常";
	   List<Entry<String, ArrayList<QuestionModel>>> list = new ArrayList<Entry<String,ArrayList<QuestionModel>>>(rqlist.entrySet());
	   for (Entry<String, ArrayList<QuestionModel>> e: list) {  
		   try {
				 conn=DBConn.getConnection();
				 ps=conn.prepareStatement(sql);
               
                 for(int i=0;i<e.getValue().size();i++)
                 {
                	 ps.setString(1, e.getKey());
                	 ps.setInt(2, e.getValue().get(i).getQuestionID());
                	 ps.setInt(3, e.getValue().get(i).getType());
    				 ps.setString(4, e.getValue().get(i).getContent());
    				 ps.setString(5, e.getValue().get(i).getChoices());
    				 ps.setString(6, e.getValue().get(i).getAnswer());
    				 ps.setString(7, e.getValue().get(i).getAnswerkey());
    				 ps.setString(8, e.getValue().get(i).getTag());
    				 ps.setFloat(9, e.getValue().get(i).getDifficulty());
    				 ps.setInt(10, e.getValue().get(i).getShare());
    				 ps.setString(11, e.getValue().get(i).getKnowledgepoint());
    				 ps.setInt(12, e.getValue().get(i).getKnowledgeid());
    				 influence+=ps.executeUpdate();
                 }
				
				
				


				conn.close();
			} catch (SQLException h) {
				h.printStackTrace();
				errorString+=h.getLocalizedMessage();
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

		
			if(influence<1)
			{
				return errorString;
			}
			else 
			{
				return "ok";
			}
		
}
}
