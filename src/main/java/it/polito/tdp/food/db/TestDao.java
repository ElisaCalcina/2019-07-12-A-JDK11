package it.polito.tdp.food.db;

import java.util.HashMap;
import java.util.Map;

import it.polito.tdp.food.model.Food;

public class TestDao {

	Map<Integer, Food> idMap=new HashMap<>();
	
	
	public static void main(String[] args) {
		TestDao testDao= new TestDao();
		testDao.run();
	}
	
	public void run() {
		FoodDao dao = new FoodDao();
		
		System.out.println(dao.listAllFoods(idMap));
		System.out.println(dao.getDescrizione(3));
		
		//System.out.println(dao.getCalorie(idMap));
	/*	System.out.println("Printing all the condiments...");
		System.out.println(dao.listAllCondiments());
		
		System.out.println("Printing all the foods...");
		
		
		System.out.println("Printing all the portions...");
		System.out.println(dao.listAllPortions());
	}*/
	}
}
