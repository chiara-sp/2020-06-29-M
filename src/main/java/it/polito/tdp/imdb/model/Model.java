package it.polito.tdp.imdb.model;

import java.util.ArrayList;
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
	private List<Director> soluzione;
	int maxC;
	int pesoMigliore;
	
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
public List<Director> trovaSequenza(Director oggetto, int maxC) {
		
		//Mi creo una lista di xxxx diponibili, utile per la ricorsione ed inizializzo i valori
        this.soluzione = new ArrayList<>();
		List<Director> parziale = new ArrayList<>();
		parziale.add(oggetto);
	
		this.maxC=maxC;
		this.pesoMigliore=0;
		//Livello non sempre utile

		cerca(parziale, 0, maxC); //secondo praamentro = somma archi parziale
		return this.soluzione;
		
	}
	
	/**
	 * Procedura ricorsiva per il calcolo di sequenze di xxxx
	 * @param parziale parte iniziale della sequenza di xxxx
	 * @param livello indica il livello della ricorsione, sempre uguale a parziale.size().
	 * @param disponibili numero di oggetti non ancora utilizzati
	 */
	private void cerca(List<Director> parziale, int pesoTotale, int maxC) {

		//Sequenza di istruzioni sempre eseguite (solo in casi rari)
		//Condizione di terminazione
		
		if(parziale.size()>soluzione.size()&& this.pesoMigliore<pesoTotale && pesoTotale<=maxC) {
			soluzione = new ArrayList<>(parziale);
			this.pesoMigliore= pesoTotale;
		}
		for(Director vicino: Graphs.neighborListOf(grafo, parziale.get(parziale.size()-1))) {
			
			int pesoDaAggiungere= (int)grafo.getEdgeWeight(grafo.getEdge(vicino,parziale.get(parziale.size()-1)));
			
			if(!parziale.contains(vicino)&& pesoTotale+pesoDaAggiungere<maxC) {
				parziale.add(vicino);
				pesoTotale+=pesoDaAggiungere;
				
				cerca(parziale,pesoTotale, maxC);
				
				pesoTotale-=pesoDaAggiungere;
				parziale.remove(parziale.size()-1);
			}
		}
	
		
	
	}
	public int getAttoriCondivisi() {
		return this.pesoMigliore;
	}

	
}
