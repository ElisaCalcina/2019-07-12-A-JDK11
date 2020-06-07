package it.polito.tdp.food.model;

public class CalorieCongiunte implements Comparable<CalorieCongiunte>{

	Food f1;
	Food f2;
	double peso;
	
	public CalorieCongiunte(Food f1, Food f2, double peso) {
		super();
		this.f1 = f1;
		this.f2 = f2;
		this.peso = peso;
	}

	public Food getF1() {
		return f1;
	}

	public void setF1(Food f1) {
		this.f1 = f1;
	}

	public Food getF2() {
		return f2;
	}

	public void setF2(Food f2) {
		this.f2 = f2;
	}

	public double getPeso() {
		return peso;
	}

	public void setPeso(double peso) {
		this.peso = peso;
	}

	@Override
	public String toString() {
		return "CalorieCongiunte [f1=" + f1 + ", f2=" + f2 + ", peso=" + peso + "]";
	}

	//meno = decrescente
	@Override
	public int compareTo(CalorieCongiunte o) {
		return -(int) (this.getPeso()-o.getPeso());
	}
	
	
}
