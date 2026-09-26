package algeo.modules;

import java.util.ArrayList;
import java.util.List;

public class SPL {
    static final double EPS = 1e-9;
    static final double EPS_PIVOT = 1e-13;

    static List<Integer> eliminasiMaju(Matriks m, int batasKolom, List<String> langkah){
        int baris = m.getRows();
        List<Integer> kolomPivot = new ArrayList<>();
        int barisPivot = 0;

        for(int kol = 0; kol < batasKolom && barisPivot < baris; kol++){
            int terbesar = barisPivot;
            for(int r = barisPivot + 1; r < baris; r++){
                if(Math.abs(m.getElemen(r, kol)) > Math.abs(m.getElemen(terbesar, kol))){
                    terbesar = r;
                }
            }
            if(Math.abs(m.getElemen(terbesar, kol)) < EPS_PIVOT){
                continue;
            }

            if(terbesar != barisPivot){
                m.tukarBaris(barisPivot, terbesar);
                langkah.add("Tukar baris R" + (barisPivot + 1) + " <-> R" + (terbesar + 1) + " (partial pivoting)\n" + m.keString());
            }

            double pivot = m.getElemen(barisPivot, kol);
            if(Math.abs(pivot - 1.0) > EPS){
                m.kaliBaris(barisPivot, 1.0 / pivot);
                langkah.add("R" + (barisPivot + 1) + " <- R" + (barisPivot + 1) + " / " + format(pivot) + "\n" + m.keString());
            }

            for(int r = barisPivot + 1; r < baris; r++){
                double faktor = m.getElemen(r, kol);
                if(faktor != 0.0){
                    m.tambahBaris(r, barisPivot, -faktor);
                    langkah.add("R" + (r + 1) + " <- R" + (r + 1) + " - (" + format(faktor) + ") * R" + (barisPivot + 1) + "\n" + m.keString());
                }
            }

            kolomPivot.add(kol);
            barisPivot++;
        }
        return kolomPivot;
    }

    static void eliminasiMundur(Matriks m, List<Integer> kolomPivot, List<String> langkah){
        for(int i = kolomPivot.size() - 1; i >= 0; i--){
            int kol = kolomPivot.get(i);
            for(int r = i - 1; r >= 0; r--){
                double faktor = m.getElemen(r, kol);
                if(faktor != 0.0){
                    m.tambahBaris(r, i, -faktor);
                    langkah.add("R" + (r + 1) + " <- R" + (r + 1) + " - (" + format(faktor) + ") * R" + (i + 1) + "\n" + m.keString());
                }
            }
        }
    }
    //spek
    static String format(double v){
        if(Math.abs(v) < EPS) v = 0.0;
        return String.format("%.3f", v);
    }

}