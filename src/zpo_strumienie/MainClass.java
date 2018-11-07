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
		
			wczytajZPliku(wynikiLista);		//+
			
			wskazanyPoNumerze(wynikiLista);		//+
			najrzadziejWystepujaceLiczby(wynikiLista);		//+
			
			plikBinarnyZapis(wynikiLista);		//+/-
			
			
			plikBinarnySkompresowanyZapis();		//+/-
			
			
			
			plikCSVZapis(wynikiLista);	//+
			//plikCSVOdczyt(wynikiLista);	//+
									
			plikObiektowyZapis(wynikiLista);		//+
			//plikObiektowyOdczyt(wynikiLista);		//+
			
			
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
	
//	static byte[] toBytes(long val, int bufferSize)	{
//	    byte[] result = new byte[bufferSize];
//	    for(int i = bufferSize - 1; i >= 0; i--) {
//	        result[i] = (byte) (val /*>> 0*/);
//	        val = (val >> 8);
//	    }
//	    return result;
//	}
	
	public static void plikBinarnyZapis(List<Losowanie> wynikiLista) throws ParseException {
		
		try(DataOutputStream out = new DataOutputStream(new FileOutputStream("txt/plik1.bin"))) { ///jakie rozszerzenie ??
			
			short nr = 0;
			Timestamp dat;
			//byte[] bytes = null;// = new byte[6];
			
			//ArrayList<Integer> binarnie = new ArrayList<Integer>();
			
			for(Losowanie los1 : wynikiLista) {
				nr = (short)los1.getNumer();				
				out.writeShort(nr);
				out.writeChars("\t");				
				
				SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
			    Date parsedDate = dateFormat.parse(los1.getData());
			    dat = new Timestamp(parsedDate.getTime());
			    out.writeLong(dat.getTime());
			    out.writeChars("\n");
			   // System.out.println(dat.getTime());
			    			      
			    /*
			    /////liczby - tablica 6 bajtow ?????
			       
			    ArrayList<Integer> licz = new ArrayList<Integer>();			   
			    licz = los1.getLiczby();			    
			    bytes =	toBytes(licz.get(1)*256*256*256*256 + licz.get(2)*256*256*256 + licz.get(3)*256*256 + licz.get(4)*256 + licz.get(5), 6);
			    System.out.println(bytes[5]);
			    
	           ///////
	           // System.out.println(BigInteger.valueOf(15).toByteArray().toString());
			    System.out.println(String.valueOf(15));
			    bytes =	toBytes(4*256*256*256, 6);
			    bytes =	toBytes(8*256*256+3*256+4, 6);
			    //System.out.println(bytes); //smieci
			    System.out.println(Arrays.toString(bytes));
			    
			    System.out.println(bytes[5]);
			     ////////
			    */			
			}	
           
            out.close();
            
           
       } catch(IOException ioe) {
            System.out.println("Error!");
       }
		
	}
	
	public static void plikBinarnySkompresowanyZapis() throws Exception {
		
		FileInputStream fis = new FileInputStream("txt/plik1.bin"); ///jakie rozszerzenie ??
 		FileOutputStream fos = new FileOutputStream("txt/plik2.txt"); ///jakie rozszerzenie ??
 		DeflaterOutputStream dos = new DeflaterOutputStream(fos);

 		doCopy(fis, dos);
 			
	}
	//??
	public static void plikBinarnySkompresowanyOdczyt() throws Exception {
		
		FileInputStream fis2 = new FileInputStream("txt/plik2.txt");
		InflaterInputStream iis = new InflaterInputStream(fis2);
		FileOutputStream fos2 = new FileOutputStream("txt/plik22.bin");

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
				
		String str;
		ArrayList<Integer> licz;
		try (DataOutputStream dataOutputStream = new DataOutputStream(new FileOutputStream("txt/plik3.csv"))) {
			
			for(Losowanie los : wynikiLista) {
				
				str = los.getNumer()+";"+los.getData()+";";			   
			    licz = los.getLiczby();
			    
			    for(int i=0;i<licz.size();i++) {
			    	str = str + licz.get(i)+";";
			    }
			    
			    str = str + "\n";
			    
			    byte[] data = str.getBytes("UTF-8");
			    dataOutputStream.write(data);			
			}
			
			dataOutputStream.close();
		}catch(IOException ioe) {
            System.out.println("Error!");
       }		
	}
	
	public static void plikCSVOdczyt(List<Losowanie> wynikiLista) throws FileNotFoundException, IOException {
		
		String str1;
		ArrayList<Losowanie> wyniki = new ArrayList<Losowanie>();
		String[] line; 
		int j=0;
		int k=2;
		
		try (DataInputStream dataInputStream = new DataInputStream(new FileInputStream("txt/plik3.csv"))){
			
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
		
		for(Losowanie los5 : wyniki) {
			System.out.println(los5);
        }
		
	}
	
	public static void plikObiektowyZapis(List<Losowanie> wynikiLista) throws FileNotFoundException, IOException {
		
		try (ObjectOutputStream objOutputStream = new ObjectOutputStream(new FileOutputStream("txt/plik4.bin"))) {
			
			for(Losowanie los : wynikiLista) {
				objOutputStream.writeObject(los);
            }
			objOutputStream.close();	        
		}
			
	}
	
	public static void plikObiektowyOdczyt(List<Losowanie> wynikiLista) throws FileNotFoundException, IOException {
		
		ArrayList<Losowanie> wyniki = new ArrayList<Losowanie>();
		ArrayList<Integer> licz = new ArrayList<Integer>();
		int j=0;
		
		try (ObjectInputStream objInputStream = new ObjectInputStream(new FileInputStream("txt/plik4.bin"))) {
			
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
