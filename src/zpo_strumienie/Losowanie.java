package zpo_strumienie;

import java.io.Serializable;
import java.util.ArrayList;
public class Losowanie implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
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
	
	 @Override
	    public boolean equals(Object obj) {
	        if (this == obj)
	            return true;
	        if (obj == null)
	            return false;
	        if (getClass() != obj.getClass())
	            return false;

	        Losowanie los2 = (Losowanie) obj;
	        boolean x = true;
	        
	        for (int i = 0; i < liczby.size() && i < los2.liczby.size(); i++) {
	            if (liczby.get(i) != los2.liczby.get(i)) {
	                x = false;
	                break;
	            }
	        }

	        if(numer != los2.numer)
	        {
	            return false;
	        }
	        else if(!(data.equals(los2.data)))
	        {
	            return false;
	        }
	        else if(x == false)
	        {
	            return false;
	        }
	        else
	        {
	            return true;
	        }

	    }
	
	

}
