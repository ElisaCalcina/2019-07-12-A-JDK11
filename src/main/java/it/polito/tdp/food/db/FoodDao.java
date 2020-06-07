package it.polito.tdp.food.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import it.polito.tdp.food.model.CalorieCongiunte;
import it.polito.tdp.food.model.Condiment;
import it.polito.tdp.food.model.Food;
import it.polito.tdp.food.model.Portion;

public class FoodDao {
	public List<Food> listAllFoods(Map<Integer, Food> idMap){
		String sql = "SELECT * FROM food" ;
		try {
			Connection conn = DBConnect.getConnection() ;

			PreparedStatement st = conn.prepareStatement(sql) ;
			
			List<Food> list = new ArrayList<>() ;
			
			ResultSet res = st.executeQuery() ;
			
			while(res.next()) {
				try {
					if(!idMap.containsKey(res.getInt("food_code"))) {
						Food food =new Food(res.getInt("food_code"), res.getString("display_name"));
					list.add(food);
					
					idMap.put(food.getFood_code(), food);
					}
				} catch (Throwable t) {
					t.printStackTrace();
				}
			}
			
			conn.close();
			return list;
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}

	}
	
	public List<Condiment> listAllCondiments(){
		String sql = "SELECT * FROM condiment" ;
		try {
			Connection conn = DBConnect.getConnection() ;

			PreparedStatement st = conn.prepareStatement(sql) ;
			
			List<Condiment> list = new ArrayList<>() ;
			
			ResultSet res = st.executeQuery() ;
			
			while(res.next()) {
				try {
					list.add(new Condiment(res.getInt("condiment_code"),
							res.getString("display_name"),
							res.getDouble("condiment_calories"), 
							res.getDouble("condiment_saturated_fats")
							));
				} catch (Throwable t) {
					t.printStackTrace();
				}
			}
			
			conn.close();
			return list ;

		} catch (SQLException e) {
			e.printStackTrace();
			return null ;
		}
	}
	
	public List<Portion> listAllPortions(){
		String sql = "SELECT * FROM portion" ;
		try {
			Connection conn = DBConnect.getConnection() ;

			PreparedStatement st = conn.prepareStatement(sql) ;
			
			List<Portion> list = new ArrayList<>() ;
			
			ResultSet res = st.executeQuery() ;
			
			while(res.next()) {
				try {
					list.add(new Portion(res.getInt("portion_id"),
							res.getDouble("portion_amount"),
							res.getString("portion_display_name"), 
							res.getDouble("calories"),
							res.getDouble("saturated_fats"),
							res.getInt("food_code")
							));
				} catch (Throwable t) {
					t.printStackTrace();
				}
			}
			
			conn.close();
			return list ;

		} catch (SQLException e) {
			e.printStackTrace();
			return null ;
		}

	}
	
	
	public List<Food> getDescrizione(int id) {
		String sql= "SELECT  display_name, f.food_code , COUNT(DISTINCT(p.portion_id)) AS c " + 
				"FROM food AS f, `portion` AS p " + 
				"WHERE p.food_code=f.food_code " + 
				"GROUP BY f.food_code " + 
				"HAVING c=? " +
				"ORDER BY display_name ASC ";
		List<Food> descr= new ArrayList<>();
		
		try {
			Connection conn = DBConnect.getConnection() ;
			PreparedStatement st = conn.prepareStatement(sql) ;
			st.setInt(1, id);
		
			ResultSet res = st.executeQuery() ;
			
			while(res.next()) {
				descr.add(new Food(res.getInt("food_code"), res.getString("display_name")));
			}
			conn.close();
			return descr;
		}catch (SQLException e) {
				e.printStackTrace();
				return null ;
			}
	}
	
	//non uso mai id qui, posso toglierlo e non cambia niente
	public List<CalorieCongiunte> getCalorie(Map<Integer, Food> idMap){
		String sql="SELECT fc.food_code AS f1, fc2.food_code AS f2, AVG(c.condiment_calories) as peso " + 
				"FROM food_condiment AS fc, condiment AS c, food_condiment as fc2 " + 
				//"WHERE fc.id!=fc2.id " + 
				"		WHERE c.condiment_code=fc2.condiment_code " + 
				"		AND fc.condiment_code=fc2.condiment_code " + 
				" AND fc.food_code>fc2.food_code " +
				"GROUP BY fc.food_code, fc2.food_code " +
				"HAVING peso!=0 ";
		
		List<CalorieCongiunte> result= new ArrayList<>();
		
		try {
			Connection conn = DBConnect.getConnection() ;
			PreparedStatement st = conn.prepareStatement(sql) ;
			
			ResultSet res = st.executeQuery() ;
			
			while(res.next()) {
				
				Food food1= idMap.get(res.getInt("f1"));
				Food food2= idMap.get(res.getInt("f2"));
				
				if(food1!=null && food2!=null) {
				CalorieCongiunte c= new CalorieCongiunte(food1, food2, res.getDouble("peso"));
				result.add(c);
				}
				
			}
			
			conn.close();
			return result;
			
		}catch (SQLException e) {
			e.printStackTrace();
			System.out.println("Errore in get calorie");
			return null ;
		}
	}
}
