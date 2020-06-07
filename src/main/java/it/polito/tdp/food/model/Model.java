package it.polito.tdp.food.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;

import it.polito.tdp.food.db.FoodDao;

public class Model {
	private FoodDao dao;
	private Graph<Food, DefaultWeightedEdge> grafo;
	private Map<Integer, Food> idMap;
	
	public Model() {
		dao= new FoodDao();
		idMap= new HashMap<>();
		dao.listAllFoods(idMap);
	}
	
	public List<Food> getDescrizione(int id) {
		return this.dao.getDescrizione(id);
	}
	
	public void creaGrafo(int id) {
		this.grafo= new SimpleWeightedGraph<>(DefaultWeightedEdge.class);
		
		//aggiungo i vertici 
		Graphs.addAllVertices(grafo, dao.getDescrizione(id));
		
		//aggiungo archi --> qui non serve la condizione c.getF1() != c.getF2(), lo metto solo come controllo
		for(CalorieCongiunte c: this.dao.getCalorie(idMap)) {
			if(c.getF1()!=c.getF2() && this.grafo.containsVertex(c.getF1()) && this.grafo.containsVertex(c.getF2())) {
				Graphs.addEdgeWithVertices(grafo, c.getF1(), c.getF2(), c.getPeso());
			}
		}
		
		System.out.println(String.format("Grafo creato con %d vertici e %d archi", this.grafo.vertexSet().size(), this.grafo.edgeSet().size()));
	}
	
	public int nVertici() {
		return this.grafo.vertexSet().size();
	}
	
	public int nArchi() {
		return this.grafo.edgeSet().size();
	}

	//calorie congiunte e non food perchè voglio anche il peso
	public List<CalorieCongiunte> elencoConn(Food f){
		List<CalorieCongiunte> result= new ArrayList<>();
		
		List<Food> vicini = Graphs.neighborListOf(grafo, f);
		//per ogni vicino aggiungo un valore in result
		for(Food v: vicini) {
			//arco che collega f a v
			Double peso= this.grafo.getEdgeWeight(this.grafo.getEdge(f, v));
			result.add(new CalorieCongiunte(f,v, peso));
		}
		Collections.sort(result);
		return result;
	}
}
