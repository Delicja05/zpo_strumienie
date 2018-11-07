package zpo_strumienie;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Scanner;
import java.util.StringTokenizer;
import java.util.TreeMap;
import java.util.stream.Collectors;
import java.util.zip.DeflaterOutputStream;
import java.util.zip.InflaterInputStream;



public class MainClass {
	
	static ArrayList<Losowanie> wynikiLista;
	
	public static void main(String[] args) throws Exception{
		
		try {
			wynikiLista = new ArrayList<Losowanie>();		
		
			wczytajZPliku(wynikiLista);
			
			wskazanyPoNumerze(wynikiLista);
			najrzadziejWystepujaceLiczby(wynikiLista);
			
			//////// zapisy do pliku ////////
			
			plikBinarnyZapis(wynikiLista);  // tworzy plikBinarny.data
			//plikBinarnyOdczyt("txt/plikBinarny.data");
						
			plikBinarnyDeflater("txt/plikBinarny.data"); //kompresuje plikBinarny.data
			plikBinarnyInflater("txt/plikBinarnySkompresowany.data"); //dekompresuje plik i tworzy plikBinarny2.data
			//plikBinarnyOdczyt("txt/plikBinarny2.data");
			
			plikCSVZapis(wynikiLista);  // tworzy plikCSV.csv
			//plikCSVOdczyt("txt/plikCSV.csv");
									
			plikObiektowyZapis(wynikiLista);  // tworzy plikObiektowy.bin
			//plikObiektowyOdczyt("txt/plikObiektowy.bin");
			
			
		} catch (IOException e) {
			e.printStackTrace();
		}
				
	}
	
	
	public static void wczytajZPliku(List<Losowanie> wynikiLista) {
		
		File plik;
		Scanner odczyt;
		StringTokenizer token;
		SimpleDateFormat formatter;
		int j=0;
		
		try {
			plik = new File("txt/lotto.txt");		
			formatter = new SimpleDateFormat("dd-MM-yyyy");
			odczyt = new Scanner(plik);
			
			while(odczyt.hasNextLine()) {
				
				token = new StringTokenizer (odczyt.nextLine(),"\t");
				while(token.hasMoreElements()) {
					wynikiLista.add(new Losowanie(Integer.parseInt(token.nextToken()), formatter.format(formatter.parse(token.nextToken()))));
					
					for(int i=0; i<6; i++) {
						wynikiLista.get(j).dodajLiczbe(Integer.parseInt(token.nextToken()));						
					}
					j++;					
				}				
			}
								
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (NumberFormatException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		}		
	}	
	
	public static void wskazanyPoNumerze(List<Losowanie> wynikiLista) {
		
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
		
	}
	
	public static void najrzadziejWystepujaceLiczby(List<Losowanie> wynikiLista) {
		
		TreeMap<Integer, Integer> map = new TreeMap<>();
        for(int i = 1; i < 50; i++)
        	map.put(i, 0);
                
        for (Losowanie los: wynikiLista) {
        	ArrayList<Integer> temp = los.getLiczby();
            for(int i = 0; i < temp.size(); i++){
                map.put(temp.get(i), map.get(temp.get(i)) + 1);
            }
        }
        
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
		
		List<Integer> listOfMin = map.entrySet().stream()	            
	            .map(Map.Entry::getKey)
	            .collect(Collectors.toList());
		
		System.out.println("Najrzadziej wystêpuj¹cych 6 liczb: ");
		listOfMin.forEach(System.out::println);
		
		
	}
	
	
	public static void plikBinarnyZapis(List<Losowanie> wynikiLista) throws ParseException, NullPointerException {
		
		SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
		ArrayList<Integer> licz = new ArrayList<Integer>();
		byte[] result = new byte[6];
		Timestamp dat;	
		Date parsedDate;		
		
		try(DataOutputStream out = new DataOutputStream(new FileOutputStream("txt/plikBinarny.data"))) {
								
			for(Losowanie losowanie : wynikiLista) {								
				out.writeShort((short)losowanie.getNumer());							
				
				parsedDate = dateFormat.parse(losowanie.getData());
			    dat = new Timestamp(parsedDate.getTime());
			    out.writeLong(dat.getTime());
			    			   			    			   
			    licz = losowanie.getLiczby();
			    
			    try{			    	
			    	for(int j = 0; j < licz.size(); j++) {
			    		result[j] = licz.get(j).byteValue();
		    		}
			    }catch (Exception e) {
			    	System.out.println(e.getMessage());
		    		break;
				}
			    			    
			    out.write(result);		    	
			}
			
            out.close();                       
       } catch(IOException ioe) {
            System.out.println("Error!");
       }
		
	}
	
	public static void plikBinarnyOdczyt(String nazwa) throws ParseException, NullPointerException, FileNotFoundException, IOException {
		
		SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
		ArrayList<Losowanie> wyniki = new ArrayList<Losowanie>();
		Date date;
		long time;
		Byte b;
		int nr;
		int j=0;
		
		try(DataInputStream in = new DataInputStream(new FileInputStream(nazwa))){
			
			while(true) {
				
				try {
					nr = (int)in.readUnsignedShort();
	        	}catch (Exception e) {
	        		break;
				}
								
				time = in.readLong();
				date = new Date(time);
				
				wyniki.add(new Losowanie(nr, format.format(date)));
				
				for(int i=0; i<6; i++) {
					
					b = in.readByte();					
					wyniki.get(j).dodajLiczbe(b.intValue());
				}
				
		        j++;
			} 
			
		}
		
		for(Losowanie los : wyniki) {
			System.out.println(los);
        }
				
	}	
	
	public static void plikBinarnyDeflater(String nazwa) throws Exception {
		
		FileInputStream fis = new FileInputStream(nazwa); 
 		FileOutputStream fos = new FileOutputStream("txt/plikBinarnySkompresowany.data");
 		DeflaterOutputStream dos = new DeflaterOutputStream(fos);

 		doCopy(fis, dos);
 			
	}
	
	public static void plikBinarnyInflater(String nazwa) throws Exception {
		
		FileInputStream fis2 = new FileInputStream(nazwa);
		InflaterInputStream iis = new InflaterInputStream(fis2);
		FileOutputStream fos2 = new FileOutputStream("txt/plikBinarny2.data");

		doCopy(iis, fos2);
 			
	}
	
	public static void doCopy(InputStream is, OutputStream os) throws Exception {
		int oneByte;
		while ((oneByte = is.read()) != -1) {
			os.write(oneByte);
		}
		os.close();
		is.close();
	}
	
	public static void plikCSVZapis(List<Losowanie> wynikiLista) throws IOException, ParseException {
				
		byte[] data;
		String str;
		ArrayList<Integer> licz;
		
		try (DataOutputStream dataOutputStream = new DataOutputStream(new FileOutputStream("txt/plikCSV.csv"))) {
			
			for(Losowanie los : wynikiLista) {
				
				str = los.getNumer()+";"+los.getData()+";";			   
			    licz = los.getLiczby();
			    
			    for(int i=0;i<licz.size();i++) {
			    	str = str + licz.get(i)+";";
			    }
			    
			    str = str + "\n";
			    
			    data = str.getBytes("UTF-8");
			    dataOutputStream.write(data);			
			}
			
			dataOutputStream.close();
		}catch(IOException ioe) {
            System.out.println("Error!");
       }		
	}
	
	public static void plikCSVOdczyt(String nazwa) throws FileNotFoundException, IOException {
		
		String str1;
		ArrayList<Losowanie> wyniki = new ArrayList<Losowanie>();
		String[] line; 
		int j=0;
		int k=2;
		
		try (DataInputStream dataInputStream = new DataInputStream(new FileInputStream(nazwa))){
			
			BufferedReader bReader = new BufferedReader(new InputStreamReader(dataInputStream, "UTF-8"));
			
			while((str1 = bReader.readLine()) != null) {
				line = str1.split(";");
				 
				wyniki.add(new Losowanie(Integer.parseInt(line[0]),line[1]));
				for(int i=0; i<6; i++) {
					wyniki.get(j).dodajLiczbe(Integer.parseInt(line[k]));
					k++;
				}
				
		        j++;
		        k=2;				 
			}
		}
		
		for(Losowanie los : wyniki) {
			System.out.println(los);
        }
		
	}
	
	public static void plikObiektowyZapis(List<Losowanie> wynikiLista) throws FileNotFoundException, IOException {
		
		try (ObjectOutputStream objOutputStream = new ObjectOutputStream(new FileOutputStream("txt/plikObiektowy.bin"))) {
			
			for(Losowanie los : wynikiLista) {
				objOutputStream.writeObject(los);
            }
			objOutputStream.close();	        
		}			
	}
	
	public static void plikObiektowyOdczyt(String nazwa) throws FileNotFoundException, IOException {
		
		ArrayList<Losowanie> wyniki = new ArrayList<Losowanie>();
		ArrayList<Integer> licz = new ArrayList<Integer>();
		int j=0;
		
		try (ObjectInputStream objInputStream = new ObjectInputStream(new FileInputStream(nazwa))) {
			
	       	Losowanie losowanie ;	
	        while(true) {
	        	
	        	try {
	        		losowanie = (Losowanie) objInputStream.readObject();
	        	}catch (Exception e) {
	        		break;
				}
	        	
	        	wyniki.add(new Losowanie(losowanie.getNumer(),losowanie.getData()));
	        	licz = losowanie.getLiczby();		        	
	        	for(int i=0; i<licz.size(); i++) {
					wyniki.get(j).dodajLiczbe(licz.get(i));						
				}
				j++;				
	        }
	     }
		
		 for(Losowanie los : wyniki) {
				System.out.println(los);
	     }	
	}
	
}
