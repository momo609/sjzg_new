package com.sjzg.tool;

import java.util.HashMap;

public interface  KnowledgeGraph {
	static final  int[][] edge ={{1,1,0,0,0,0,0,0,0,0,0},
        {0,1,1,0,0,0,0,0,0,0,0},
        {0,0,1,1,0,0,0,0,0,0,0},
        {0,0,0,1,0,0,1,0,0,0,0},
        {0,0,0,0,1,0,0,0,0,0,0},
        {0,0,1,0,0,1,0,0,0,0,0},
        {0,0,0,0,0,0,1,0,1,0,0},
        {0,0,0,0,0,1,0,1,0,1,0},
        {0,0,0,0,1,0,0,0,1,0,1},
        {0,1,0,0,0,0,0,0,0,1,0},
        {0,0,0,0,0,0,0,0,0,0,1}};
	static final  String concepts[]={"1NF","2NF","3NF","BCNF","������","���ݺ�������","��������","��������","��","���ֺ�������","��������"};
//	static final  String concepts[]={"kg","kg2"};
//	static final  String concepts[]={"BCNF","��������"};
	
}