package com.sjzg.recommend;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javassist.bytecode.Descriptor.Iterator;

import com.sjzg.answer.analyzeknowledge;
import com.sjzg.paper.PaperModel;
import com.sjzg.question.QuestionGetByConcept;
import com.sjzg.question.QuestionGetByPaper;
import com.sjzg.question.QuestionModel;


public class recommend {
	public HashMap<String,ArrayList<QuestionModel>>  Recommend(HashMap<String,ArrayList<String>> allresult,int testid)
	{
		HashMap<String,Integer>conceptid=new HashMap<String,Integer>();
//		conceptid.put("kg", 1);
//		conceptid.put("kg2", 2);
		conceptid.put("1NF", 1);
    	conceptid.put("2NF", 2);
    	conceptid.put("3NF",3);
    	conceptid.put("BCNF", 4);
    	conceptid.put("主属性", 5);
    	conceptid.put("传递函数依赖", 6);
    	conceptid.put("决定因素", 7);
     	conceptid.put("函数依赖", 8);
    	conceptid.put("码",9);
    	conceptid.put("部分函数依赖",10);
    	conceptid.put("非主属性",11);
//    	conceptid.put("规范化理论", 12);
		analyzeknowledge dao=new analyzeknowledge();
		int paperid=dao.GetPaperidByTest(testid);
		QuestionGetByPaper questionDaoImpl=new QuestionGetByPaper();
		
		PaperModel DBfindPaper_result = questionDaoImpl.DBfindPaper(paperid);
		
		if (DBfindPaper_result == null){
			System.out.println("题库为空");
		}
		String questionListStr = DBfindPaper_result.getQuestions();
		System.out.println(questionListStr);

		
		String[] questionList = questionListStr.split("@@");//丢弃空字符串

		if(questionList.length<2){
			System.out.println("试题不足");
		}
//		ArrayList<QuestionModel> qlist = questionDaoImpl.DBfindQuestions(questionList);
		HashMap<String,ArrayList<QuestionModel>> rqlist=new HashMap<String,ArrayList<QuestionModel>> ();
		  List<Entry<String, ArrayList<String>>> list = new ArrayList<Entry<String,ArrayList<String>>>(allresult.entrySet());   
		 for (Entry<String, ArrayList<String>> e: list) {  
			   ArrayList<QuestionModel> prlist=new ArrayList<QuestionModel>();
			   System.out.println("e.getKey() "+e.getKey());
			 for(int i=0;i<e.getValue().size();i++)
			   {
				   prlist=new QuestionGetByConcept().GetQuestionByConcept(prlist,conceptid.get(e.getValue().get(i)));
				   
//				   temp_kp=temp_kp+conceptid.get(pkp.get(i))+",";
			   }
			 rqlist.put(e.getKey(), prlist);
       }  
		return rqlist;
	}

}
 