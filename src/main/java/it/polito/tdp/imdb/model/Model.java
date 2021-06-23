package it.polito.tdp.imdb.model;

import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;

import it.polito.tdp.imdb.db.ImdbDAO;

public class Model {
	
	private ImdbDAO dao;
	private Map<Integer,Director> mapDirector;
	public Graph<Director,DefaultWeightedEdge> grafo;
	List<Director> vertici;
	
	public Model() {
		dao= new ImdbDAO();
		mapDirector= new HashMap<>();
		dao.listAllDirectors(mapDirector);
		this.vertici= new LinkedList<>();
	}
	public void creaGrafo(int anno) {
		grafo= new SimpleWeightedGraph<>(DefaultWeightedEdge.class);
		
		//aggiunta vertici
		vertici=dao.getVertici(mapDirector, anno);
		Graphs.addAllVertices(grafo,vertici );
		
		//aggiunta archi
		for(Adiacenza a: dao.getAdiacenze(anno, mapDirector)) {
			Graphs.addEdge(grafo, a.getD1(), a.getD2(), a.getPeso());
		}
	}
	public int numVertici() {
		return this.grafo.vertexSet().size();
	}
	public int numArchi() {
		return this.grafo.edgeSet().size();
	}
	public List<Director> getVertici(){
		Collections.sort(vertici);
		return vertici;
	}
	public boolean grafoCreato() {
		if(grafo==null)
			return false;
		return true;
	}

	public List<Vicino> getAdiacenti(Director director){
		if(this.grafoCreato()) {
			List<Vicino> result= new LinkedList<>();
			for(Director d: Graphs.neighborListOf(grafo, director)) {
				result.add(new Vicino(d,grafo.getEdgeWeight(grafo.getEdge(d, director))));
			}
			Collections.sort(result);
			return result;
		}
		return null;
	}
}
