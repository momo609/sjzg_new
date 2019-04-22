//package com.sjzg.recommend;
//
//import java.io.IOException;
//import java.io.PrintWriter;
//import java.sql.SQLException;
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map.Entry;
//
//import javax.servlet.ServletException;
//import javax.servlet.http.HttpServlet;
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//
//import org.rosuda.REngine.Rserve.RserveException;
//
//import com.google.gson.JsonArray;
//import com.google.gson.JsonObject;
//import com.sjzg.answer.analyzeknowledge;
//import com.sjzg.question.QuestionModel;
//
//public class RecommendByALL extends HttpServlet {
//
//	/**
//	 * Constructor of the object.
//	 */
//	public RecommendByALL() {
//		super();
//	}
//
//	
//	/**
//	 * The doPost method of the servlet. <br>
//	 *
//	 * This method is called when a form has its tag value method equals to post.
//	 * 
//	 * @param request the request send by the client to the server
//	 * @param response the response send by the server to the client
//	 * @throws ServletException if an error occurred
//	 * @throws IOException if an error occurred
//	 */
//	public void doPost(HttpServletRequest request, HttpServletResponse response)
//			throws ServletException, IOException {
//
//		response.setContentType("application/json;charset=utf-8");
//		request.setCharacterEncoding("UTF-8");//设置对客户端请求进行重新编码的编码。
//		response.setCharacterEncoding("UTF-8");//指定对服务器响应进行重新编码的编码。
//		response.addHeader("Access-Control-Allow-Origin", "*");//跨域
//		PrintWriter out = response.getWriter();//务必放在最后
//		
//		String Testid=request.getParameter("TestID");
//		String rqlist_kg = null;
//		try {
//			rqlist_kg = new analyzeknowledge().analyzetoquestion(Integer.parseInt(Testid));
//		} catch (NumberFormatException e2) {
//			// TODO Auto-generated catch block
//			e2.printStackTrace();
//		} catch (SQLException e2) {
//			// TODO Auto-generated catch block
//			e2.printStackTrace();
//		}
//		HashMap<String, ArrayList<QuestionModel>> rqlist_pmfcd = null;
//		try {
//			rqlist_pmfcd = new pmfcd().pmfcd_recommendtoquestion(Integer.parseInt(Testid));
//		} catch (NumberFormatException e1) {
//			// TODO Auto-generated catch block
//			e1.printStackTrace();
//		} catch (RserveException e1) {
//			// TODO Auto-generated catch block
//			e1.printStackTrace();
//		}
//		if (rqlist_kg.equals("ok")){
//			//找不到
//			out.print("{\"errcode\":101,\"errmsg\":\"题库为空\"}");
//			out.flush();
//			out.close();
//			return;
//		}
//		else 
//		{
//				JsonObject jsonObject = new JsonObject();
//				
//			    List<Entry<String, ArrayList<QuestionModel>>> list = new ArrayList<Entry<String,ArrayList<QuestionModel>>>(rqlist_kg.entrySet());
//			 	for (Entry<String, ArrayList<QuestionModel>> e: list) 
//			 	{
//			 		 JsonArray questionJsonArray = new JsonArray();
//			 		  for (int i=e.getValue().size()-1;i>=0;i--)
//			 		  {
//							JsonObject tempObject = new JsonObject();
//							tempObject.addProperty("Answer", e.getValue().get(i).getAnswer());
//							tempObject.addProperty("Answerkey", e.getValue().get(i).getAnswerkey());
//							tempObject.addProperty("Choices", e.getValue().get(i).getChoices());
//							tempObject.addProperty("Content", e.getValue().get(i).getContent());
//							tempObject.addProperty("Image", e.getValue().get(i).getImage());
//							tempObject.addProperty("Share", e.getValue().get(i).getShare());
//							tempObject.addProperty("Tag", e.getValue().get(i).getTag());
//							tempObject.addProperty("UserID", e.getValue().get(i).getUserID());
//							tempObject.addProperty("Difficulty", e.getValue().get(i).getDifficulty());
//							tempObject.addProperty("QuestionID", e.getValue().get(i).getQuestionID());
//							tempObject.addProperty("Type", e.getValue().get(i).getType());
//							tempObject.addProperty("CreateAt", e.getValue().get(i).getCreateAt());
//							tempObject.addProperty("knowledgeid", e.getValue().get(i).getKnowledgeid());
//							tempObject.addProperty("knowledgepoint", e.getValue().get(i).getKnowledgepoint());
//							questionJsonArray.add(tempObject);
//					 }
//			 		 jsonObject.add(e.getKey(), questionJsonArray);
//				} 
//			 	  List<Entry<String, ArrayList<QuestionModel>>> list2 = new ArrayList<Entry<String,ArrayList<QuestionModel>>>(rqlist_pmfcd.entrySet());
//				 	for (Entry<String, ArrayList<QuestionModel>> e: list2) 
//				 	{
//				 		 JsonArray questionJsonArray = new JsonArray();
//				 		  for (int i=e.getValue().size()-1;i>=0;i--)
//				 		  {
//								JsonObject tempObject = new JsonObject();
//								tempObject.addProperty("Answer", e.getValue().get(i).getAnswer());
//								tempObject.addProperty("Answerkey", e.getValue().get(i).getAnswerkey());
//								tempObject.addProperty("Choices", e.getValue().get(i).getChoices());
//								tempObject.addProperty("Content", e.getValue().get(i).getContent());
//								tempObject.addProperty("Image", e.getValue().get(i).getImage());
//								tempObject.addProperty("Share", e.getValue().get(i).getShare());
//								tempObject.addProperty("Tag", e.getValue().get(i).getTag());
//								tempObject.addProperty("UserID", e.getValue().get(i).getUserID());
//								tempObject.addProperty("Difficulty", e.getValue().get(i).getDifficulty());
//								tempObject.addProperty("QuestionID", e.getValue().get(i).getQuestionID());
//								tempObject.addProperty("Type", e.getValue().get(i).getType());
//								tempObject.addProperty("CreateAt", e.getValue().get(i).getCreateAt());
//								tempObject.addProperty("knowledgeid", e.getValue().get(i).getKnowledgeid());
//								tempObject.addProperty("knowledgepoint", e.getValue().get(i).getKnowledgepoint());
//								questionJsonArray.add(tempObject);
//						 }
//				 		 jsonObject.add(e.getKey(), questionJsonArray);
//					} 
//			 	jsonObject.addProperty("errcode","0");
//				out.print(jsonObject.toString());
//				out.flush();
//				out.close();
//				return;
//		}
//	}
//
//}
