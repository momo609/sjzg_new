package com.sjzg.recommend;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.rosuda.REngine.Rserve.RserveException;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.sjzg.answer.analyzeknowledge;
import com.sjzg.question.QuestionModel;

public class RecmmendQuestionGetByUserid extends HttpServlet {

	/**
	 * Constructor of the object.
	 */
	public RecmmendQuestionGetByUserid() {
		super();
	}

	/**
	 * The doPost method of the servlet. <br>
	 *
	 * This method is called when a form has its tag value method equals to post.
	 * 
	 * @param request the request send by the client to the server
	 * @param response the response send by the server to the client
	 * @throws ServletException if an error occurred
	 * @throws IOException if an error occurred
	 */
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		response.setContentType("application/json;charset=utf-8");
		request.setCharacterEncoding("UTF-8");//设置对客户端请求进行重新编码的编码。
		response.setCharacterEncoding("UTF-8");//指定对服务器响应进行重新编码的编码。
		response.addHeader("Access-Control-Allow-Origin", "*");//跨域
		PrintWriter out = response.getWriter();//务必放在最后
		
		String userid=request.getParameter("UserID");
		ArrayList<QuestionModel> rqlist=new RquestiongetbyId().getRquestionByID(userid);
		
		if (rqlist == null){
			//找不到
			out.print("{\"errcode\":101,\"errmsg\":\"推荐题目为空\"}");
			out.flush();
			out.close();
			return;
		}
		else 
		{
			JsonObject jsonObject = new JsonObject();
			JsonArray questionJsonArray = new JsonArray();
			int listCount = rqlist.size();
			for (int i=listCount-1;i>=0;i--){
				JsonObject tempObject = new JsonObject();
				tempObject.addProperty("Answer", rqlist.get(i).getAnswer());
				tempObject.addProperty("Answerkey", rqlist.get(i).getAnswerkey());
				tempObject.addProperty("Choices", rqlist.get(i).getChoices());
				tempObject.addProperty("Content", rqlist.get(i).getContent());
				tempObject.addProperty("Share", rqlist.get(i).getShare());
				tempObject.addProperty("Tag", rqlist.get(i).getTag());
				tempObject.addProperty("UserID", rqlist.get(i).getUserID());
				tempObject.addProperty("Difficulty", rqlist.get(i).getDifficulty());
				tempObject.addProperty("QuestionID", rqlist.get(i).getQuestionID());
				tempObject.addProperty("Type", rqlist.get(i).getType());
				tempObject.addProperty("knowledgepoint", rqlist.get(i).getKnowledgepoint());
				tempObject.addProperty("knowledgeid", rqlist.get(i).getKnowledgeid());
				questionJsonArray.add(tempObject);
			}
			
			
			jsonObject.addProperty("errcode","0");
			jsonObject.add("recommend", questionJsonArray);
			out.print(jsonObject.toString());
			out.flush();
			out.close();
			return;
		}
	}

}
