package zpo_strumienie;

import java.io.File;
import java.io.FileNotFoundException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Scanner;
import java.util.StringTokenizer;
import java.util.stream.Collectors;


public class MainClass {
	
	public static void main(String[] args) throws FileNotFoundException, NumberFormatException, ParseException{
        
		File plik = new File("txt/lotto.txt");
		Scanner odczyt;
		StringTokenizer token;
		int j=0;
		
		List<Losowanie> wynikiLista = new ArrayList<Losowanie>();				
		SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
		
		try {
			odczyt = new Scanner(plik);
			
			while(odczyt.hasNextLine()) {
				
				token = new StringTokenizer (odczyt.nextLine(),"	");
				while(token.hasMoreElements()) {
					wynikiLista.add(new Losowanie(Integer.parseInt(token.nextToken()), formatter.format(formatter.parse(token.nextToken()))));
					
					for(int i=0; i<6; i++) {
						wynikiLista.get(j).dodajLiczbe(Integer.parseInt(token.nextToken()));						
					}
					j++;					
					
				}				
			}
					
//			for(Losowanie los : wynikiLista) {
//				System.out.println(los);
//			}			
			
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
		
		//wyniki losowanie wskazanego po numerze
		
		System.out.println("Wskaz numer losowania: ");		
		Scanner pobierz = new Scanner(System.in);		
		int number = pobierz.nextInt();		
		pobierz.close();
		
		final List<Losowanie> losPoWskazanymNumerze =
				wynikiLista.stream()
				         .filter(numerLosowania -> numerLosowania.getNumer() == number)				        
				         .collect(Collectors.toList());
				 
		System.out.println("Wynik losowania wskazanego po numerze: ");
		losPoWskazanymNumerze.forEach(System.out::println);
		
		//wska¿ najrzadziej wystêpuj¹cych 6 liczb (mo¿liwe, ¿e niektóre wyst¹pi³y 0 razy)
		
		//do poprawy, dziala ale troche na okolo ???
		int[] tab = new int[50];
		for(int p=0; p<50; p++) {
			tab[p]=0;
		}
		
		ArrayList<Integer> temp = new ArrayList<Integer>();
		
		for(Losowanie los : wynikiLista) {
			temp = los.getLiczby();
			
			for(int r=0;r<6; r++) {
				tab[temp.get(r)] = tab[temp.get(r)]+1;
			}
			
		}
		
		Map<Integer,Integer> map = new HashMap<>();
		int ile=1;
		for(int p=1; p<50; p++) {
			map.put(ile, tab[p]);
			ile++;
		}
		
		//System.out.println(map);
		
		int klucz = 0;
		
		while(map.size()>6) {
		
			int maxValueInMap=(Collections.max(map.values()));
	        for (Entry<Integer, Integer> entry : map.entrySet()) {  
	            if (entry.getValue()==maxValueInMap) {
	                klucz = entry.getKey();
	            }
	        }
	        
		    map.remove(klucz);		
		}
		
		List<Integer> listOfMin = map.entrySet()
	            .stream()	            
	            .map(Map.Entry::getKey)
	            .collect(Collectors.toList());
		
		System.out.println("Najrzadziej wystêpuj¹cych 6 liczb: ");
		listOfMin.forEach(System.out::println);
				
		
	}

}
