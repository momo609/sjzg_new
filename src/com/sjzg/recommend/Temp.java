package com.sjzg.recommend;

import org.rosuda.REngine.REXP;
import org.rosuda.REngine.REXPMismatchException;
import org.rosuda.REngine.Rserve.RConnection;
import org.rosuda.REngine.Rserve.RserveException;


public class Temp {
	public void connectR() throws RserveException
	{
		RConnection connection = Rservel.getRConnection();
		System.out.println("ִ�нű�");
		connection.eval("source('E:/R-3.5.3/myStartR/myAdd.R')");
		connection.eval("myAdd()");
		System.out.println("done!");
		connection.close();
	}
	public static void main(String[] args) throws RserveException, REXPMismatchException {
		RConnection connection = Rservel.getRConnection();
		System.out.println("ִ�нű�");
		connection.eval("source('C:/Program Files/R/R-3.5.1/myStartR/myAdd.R')");
		connection.eval("myAdd()");
		System.out.println("done!");
		connection.close();
	}
}
