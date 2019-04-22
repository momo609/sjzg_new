package com.sjzg.test;

import java.io.IOException;
import java.sql.SQLException;

import org.rosuda.REngine.Rserve.RserveException;

import com.sjzg.answer.analyzeknowledge;
import com.sjzg.recommend.getQ;
import com.sjzg.recommend.pmfcd;

public class TestRecommend {
public static void main(String args[]) throws IOException, SQLException, RserveException
{
//	System.out.println(new analyzeknowledge().analyze(28));
//	  new getQ().getQwritefile(28); 
	new pmfcd().pmfcd_recommend(28);
}
}
