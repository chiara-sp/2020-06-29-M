package it.polito.tdp.imdb.model;

public class Vicino implements Comparable<Vicino> {

	private Director director;
	private Double condivisi;
	public Vicino(Director director, double condivisi) {
		super();
		this.director = director;
		this.condivisi = condivisi;
	}
	public Director getDirector() {
		return director;
	}
	public void setDirector(Director director) {
		this.director = director;
	}
	public double getCondivisi() {
		return condivisi;
	}
	public void setCondivisi(double condivisi) {
		this.condivisi = condivisi;
	}
	@Override
	public int compareTo(Vicino o) {
		// TODO Auto-generated method stub
		return -(this.condivisi.compareTo(o.condivisi));
	}
	
	
}
