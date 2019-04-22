package com.sjzg.recommend;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import com.sjzg.database.DBConn;
import com.sjzg.question.QuestionModel;

public class RquestiongetbyId {
public ArrayList<QuestionModel> getRquestionByID(String userid)
{
	System.out.println("完成执行DBfindQuestion");
	String sql="SELECT * from r_question WHERE Userid=?";
	ArrayList<QuestionModel> questionList = new ArrayList<QuestionModel>();
	Connection conn = null;
	PreparedStatement ps = null;
	ResultSet rs = null;
	String errorString = "数据库操作异常";
	try {
		 conn=DBConn.getConnection();
		 ps=conn.prepareStatement(sql);
		ps.setString(1, userid);
		 rs=ps.executeQuery();
		
		while(rs.next())
		{
			QuestionModel questionModel_temp = new QuestionModel();
			questionModel_temp.setUserID(userid);
			questionModel_temp.setContent(rs.getString("Content"));
			questionModel_temp.setAnswerkey(rs.getString("Answerkey"));
			questionModel_temp.setAnswer(rs.getString("Answer"));
			questionModel_temp.setChoices(rs.getString("Choices"));
			questionModel_temp.setQuestionID(rs.getInt("QuestionID"));
			questionModel_temp.setType(rs.getInt("Type"));
			questionModel_temp.setTag(rs.getString("Tag"));
			questionModel_temp.setDifficulty(rs.getInt("Difficulty"));
			questionModel_temp.setShare(rs.getInt("Share"));
			questionModel_temp.setKnowledgepoint(rs.getString("knowledgepoint"));
			questionModel_temp.setKnowledgeid(rs.getInt("knowledgeid"));
			questionList.add(questionModel_temp);
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

	return  questionList;
}
}
