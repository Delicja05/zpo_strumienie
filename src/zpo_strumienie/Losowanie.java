package zpo_strumienie;

import java.util.ArrayList;
public class Losowanie {
	
	int numer;
	String data;
	ArrayList<Integer> liczby;
	
	public Losowanie(int n,String d){
		this.numer = n;
		this.data = d;
		this.liczby = new ArrayList<Integer>();
	
	}
	

	public void dodajLiczbe(Integer liczba) {		
		this.liczby.add(liczba);		
	}
	
	@Override
	public String toString() {
		
		return "Numer: "+numer+" Data: " +data+ " Liczby: "+liczby+" \n";
		
	}
	
	public int getNumer() {
		return numer;
	}
	
	public String getData() {
		return data;
	}
	
	public ArrayList<Integer> getLiczby() {
		return liczby;
	}
	
	

}
