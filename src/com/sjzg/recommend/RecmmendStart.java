package com.sjzg.recommend;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.rosuda.REngine.Rserve.RserveException;

import com.sjzg.answer.analyzeknowledge;
import com.sjzg.question.QuestionModel;

public class RecmmendStart extends HttpServlet {

	/**
	 * Constructor of the object.
	 */
	public RecmmendStart() {
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
		
		String Testid=request.getParameter("TestID");
		String kgm = null;
		try {
			kgm = new analyzeknowledge().analyze(Integer.parseInt(Testid));
		} catch (NumberFormatException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		if(kgm.equals("ok"))
		{
			try {
				String pmfcd=new pmfcd().pmfcd_recommend(Integer.parseInt(Testid));
				if(pmfcd.equals("ok"))
				{
					out.print("{\"errcode\":0,\"errmsg\":\"推荐算法结束\"}");
					out.flush();
					out.close();
					return;
				}
				else
				{
					out.print("{\"errcode\":102,\"errmsg\":\"pmfcd算法出错\"}");
					out.flush();
					out.close();
					return;
				}
			} catch (NumberFormatException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (RserveException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		else
		{
			out.print("{\"errcode\":101,\"errmsg\":\"kg算法出错\"}");
			out.flush();
			out.close();
			return;
		}
	}

}
