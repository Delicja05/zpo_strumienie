package zpo_strumienie;

import org.junit.Assert;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;

public class Testy {

    @Test
    public void poprawnoscOdczytuCSV() throws IOException, ParseException {
        ArrayList<Losowanie> listaLosowan = new ArrayList<>();

        MainClass.wczytajZPliku(listaLosowan);

        MainClass.plikCSVZapis(listaLosowan, "txt/testCSV.csv");

        ArrayList<Losowanie> listaLosowan2 = MainClass.plikCSVOdczyt("txt/testCSV.csv");

        Assert.assertTrue(listaLosowan.size()  == listaLosowan2.size());

        for (int i = 0; i < listaLosowan.size() ; i++) {

            Assert.assertTrue(listaLosowan.get(i).equals(listaLosowan2.get(i)));

        }

    }


    @Test
    public void poprawnoscOdczytuObiektowego() throws IOException, ParseException {
        ArrayList<Losowanie> listaLosowan = new ArrayList<>();

        MainClass.wczytajZPliku(listaLosowan);

        MainClass.plikObiektowyZapis(listaLosowan, "txt/testObiektowy.txt");

        ArrayList<Losowanie> listaLosowan2 = MainClass.plikObiektowyOdczyt("txt/testObiektowy.txt");

        Assert.assertTrue(listaLosowan.size()  == listaLosowan2.size());

        for (int i = 0; i < listaLosowan.size() ; i++) {

            Assert.assertTrue(listaLosowan.get(i).equals(listaLosowan2.get(i)));

        }

    }


    @Test
    public void poprawnoscOdczytuBinarnego() throws IOException, ParseException {
        ArrayList<Losowanie> listaLosowan = new ArrayList<>();

        MainClass.wczytajZPliku(listaLosowan);

        MainClass.plikBinarnyZapis(listaLosowan, "txt/testBinarny.txt");

        ArrayList<Losowanie> listaLosowan2 = MainClass.plikBinarnyOdczyt("txt/testBinarny.txt");

        Assert.assertTrue(listaLosowan.size()  == listaLosowan2.size());

        for (int i = 0; i < listaLosowan.size() ; i++) {

            Assert.assertTrue(listaLosowan.get(i).equals(listaLosowan2.get(i)));

        }

    }

    @Test
    public void poprawnoscOdczytuSkompresowanego() throws Exception {
        ArrayList<Losowanie> listaLosowan = new ArrayList<>();

        MainClass.wczytajZPliku(listaLosowan);

        MainClass.plikBinarnyDeflater("txt/testBinarny.txt", "txt/testBinarnySkompresowany.dfl");
        
        MainClass.plikBinarnyInflater("txt/testBinarnySkompresowany.dfl", "txt/testBinarny2.txt");

        ArrayList<Losowanie> listaLosowan2 =  MainClass.plikBinarnyOdczyt("txt/testBinarny2.txt");


        Assert.assertTrue(listaLosowan.size()  == listaLosowan2.size());

        for (int i = 0; i < listaLosowan.size() ; i++) {

            Assert.assertTrue(listaLosowan.get(i).equals(listaLosowan2.get(i)));

        }

    }
}
