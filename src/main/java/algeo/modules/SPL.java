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

    public static HasilSPL gauss(Matriks augmented){
        Matriks m = augmented.copy();
        int jumlahVar = m.getCols() - 1;
        List<String> langkah = new ArrayList<>();
        langkah.add("Matriks augmented awal:\n" + m.keString());

        List<Integer> kolomPivot = eliminasiMaju(m, jumlahVar, langkah);
        langkah.add("=== Hasil eliminasi maju (matriks eselon baris) ===\n" + m.keString());

        langkah.add("=== Substitusi mundur ===");
        eliminasiMundur(m, kolomPivot, langkah);

        return ambilHasil(m, kolomPivot, jumlahVar, langkah);
    }

    public static HasilSPL gaussJordan(Matriks augmented){
        Matriks m = augmented.copy();
        int jumlahVar = m.getCols() - 1;
        List<String> langkah = new ArrayList<>();
        langkah.add("Matriks augmented awal:\n" + m.keString());

        List<Integer> kolomPivot = eliminasiMaju(m, jumlahVar, langkah);
        eliminasiMundur(m, kolomPivot, langkah);
        langkah.add("=== Hasil eliminasi Gauss-Jordan (matriks eselon baris tereduksi) ===\n" + m.keString());

        return ambilHasil(m, kolomPivot, jumlahVar, langkah);
    }

    private static HasilSPL ambilHasil(Matriks rref, List<Integer> kolomPivot, int jumlahVar, List<String> langkah){
        int kolKonstanta = rref.getCols() - 1;

        for(int r = kolomPivot.size(); r < rref.getRows(); r++){
            if(Math.abs(rref.getElemen(r, kolKonstanta)) > EPS){
                langkah.add("Baris " + (r + 1) + " bernilai 0 = " + format(rref.getElemen(r, kolKonstanta)) + " (tidak konsisten)");
                return HasilSPL.tidakAda(langkah);
            }
        }

        boolean[] adalahPivot = new boolean[jumlahVar];
        for(int kol : kolomPivot){
            adalahPivot[kol] = true;
        }
        List<Integer> kolomBebas = new ArrayList<>();
        for(int kol = 0; kol < jumlahVar; kol++){
            if(!adalahPivot[kol]) kolomBebas.add(kol);
        }

        if(kolomBebas.isEmpty()){
            double[] nilai = new double[jumlahVar];
            for(int i = 0; i < kolomPivot.size(); i++){
                nilai[kolomPivot.get(i)] = rref.getElemen(i, kolKonstanta);
            }
            return HasilSPL.tunggal(nilai, langkah);
        }

        String[] simbol = buatSimbol(kolomBebas.size());
        String[] ekspresi = new String[jumlahVar];
        for(int k = 0; k < kolomBebas.size(); k++){
            ekspresi[kolomBebas.get(k)] = simbol[k];
        }
        for(int i = 0; i < kolomPivot.size(); i++){
            StringBuilder sb = new StringBuilder();
            tambahSuku(sb, rref.getElemen(i, kolKonstanta), "");
            for(int k = 0; k < kolomBebas.size(); k++){
                tambahSuku(sb, -rref.getElemen(i, kolomBebas.get(k)), simbol[k]);
            }
            if(sb.length() == 0) sb.append("0");
            ekspresi[kolomPivot.get(i)] = sb.toString();
        }
        return HasilSPL.takHingga(ekspresi, langkah);
    }

    private static String[] buatSimbol(int jumlah){
        String[] simbol = new String[jumlah];
        if(jumlah <= 2){
            String[] dasar = {"t", "s"};
            for(int i = 0; i < jumlah; i++) simbol[i] = dasar[i];
        } else {
            for(int i = 0; i < jumlah; i++) simbol[i] = "a" + (i + 1);
        }
        return simbol;
    }

    private static void tambahSuku(StringBuilder sb, double koef, String simbol){
        if(Math.abs(koef) < EPS) return;
        boolean negatif = koef < 0;
        double abs = Math.abs(koef);

        if(sb.length() == 0){
            if(negatif) sb.append("-");
        } else {
            sb.append(negatif ? " - " : " + ");
        }

        if(simbol.isEmpty()){
            sb.append(format(abs));
        } else if(Math.abs(abs - 1.0) < EPS){
            sb.append(simbol);
        } else {
            sb.append(format(abs)).append(simbol);
        }
    }
}