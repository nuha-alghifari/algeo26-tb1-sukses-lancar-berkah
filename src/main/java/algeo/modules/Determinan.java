package algeo.modules;

public class Determinan{
    private static final double EPS = 1e-9;

    public static double reduksiBaris(Matriks M){
        if(M.getRows() != M.getCols()){
            throw new IllegalArgumentException("Matriks tidak memiliki determinan");
        }

        int x = M.getRows();
        Matriks obe = M.copy();
        double[][] m = obe.getData();

        boolean cetakMatriks = (x <= 10);

        System.out.println("Menghitung Determinan Matriks Dengan Metode Reduksi Baris/OBE");
        if (cetakMatriks) {
            System.out.println("Matriks Awal:");
            M.printMatriks();
        }

        int jumlahTukarBaris = 0;

        for(int i = 0; i < x; i++){
            int pivot = i;
            double max = Math.abs(m[i][i]);
            for(int k = i+1; k < x; k++){
                if(Math.abs(m[k][i]) > max){
                    max = Math.abs(m[k][i]);
                    pivot = k;
                }
            }

            if(max < EPS){
                if(cetakMatriks){
                    System.out.println("Kolom " + i + "semuanya bernilai 0, determinan = 0");
                }
                return 0.0;
            }

            if (pivot != i){
                obe.tukarBaris(i, pivot);
                jumlahTukarBaris++;
    
                    if (cetakMatriks) {
                        System.out.println("\nBaris " + (i + 1) + " ditukar dengan baris " + (pivot + 1));
                        obe.printMatriks();
                    }
                System.out.println("Jumlah pertukaran baris = " + jumlahTukarBaris);
                }

            for(int k = i+1; k < x; k++){
                if(Math.abs(m[k][i]) > 0){
                    double faktor = m[k][i] / m[i][i];
                    for(int j = i; j < x; j++){
                        m[k][j] -= faktor*m[i][j];
                    }
                    System.out.printf("B%d = B%d - (%.3f) x B%d%n", k + 1, k + 1, faktor, i + 1);
                }
            }
            if(cetakMatriks){
                System.out.println("\nMatriks setelah di lakukan obe: ");
                obe.printMatriks();
            }
        }

        double kaliDiagonal = 1.0;
        for(int i = 0; i < x; i++){
            kaliDiagonal *= m[i][i];
        }

        double det;
        if(jumlahTukarBaris % 2 != 0){
            det = -kaliDiagonal;
        } else {
            det = kaliDiagonal;
        }
        System.out.printf("Determinan = %.3f%n", det);
        return det;
    }

    public static double kofaktor(Matriks M){
        if(M.getRows() != M.getCols()){
            throw new IllegalArgumentException("Matriks tidak memiliki determinan");
        }
        System.out.println("Menghitung Determinan Matriks Dengan Ekspansi Kofaktor");

        if(M.getRows() > 10){
            System.out.println("Matriks terlalu besar dan tidak memungkinkan dihitung menggunakan Ekspansi Kofaktor\n");
            System.out.println("Silahkan pilih metode reduksi baris/OBE");
        }

        return kofaktorRekursif(M, 0);
    }

    private static int tanda(int i, int j){
        if((i+j) % 2 == 0){
            return 1;
        } else {
            return -1;
        }
    }

    private static String formatAngka(double val) {
        String s = String.format("%.3f", val);
        if (val < 0) {
            return "(" + s + ")";
        }
        return s;
    }

    public static double kofaktorRekursif(Matriks M, int depth) {
        int x = M.getRows();
        double[][] m = M.getData();

        System.out.println("\nPerhitungan Matriks (" + x + "x" + x + ")");
        M.printMatriks();

        if (x == 1) {
            System.out.println("Matriks 1x1, Determinan = " + formatAngka(m[0][0]));
            return m[0][0];
        }

        if (x == 2) {
            double hasilm2x2 = m[0][0] * m[1][1] - m[0][1] * m[1][0];
            System.out.println("Matriks 2x2, Determinan = (" + formatAngka(m[0][0]) + " * " + formatAngka(m[1][1]) + ") - (" + formatAngka(m[0][1]) + " * " + formatAngka(m[1][0]) + ") = " + formatAngka(hasilm2x2));
            return hasilm2x2;
        }

        int barisTerbaik = 0;
        int kolomTerbaik = 0;
        int nolDiBaris = -1;
        int nolDiKolom = -1;

        for (int i = 0; i < x; i++) {
            int count = 0;
            for (int j = 0; j < x; j++) {
                if (Math.abs(m[i][j]) < EPS) {
                    count++;
                }
            }
            if (count > nolDiBaris) {
                nolDiBaris = count;
                barisTerbaik = i;
            }
        }

        for (int j = 0; j < x; j++) {
            int count = 0;
            for (int i = 0; i < x; i++) {
                if (Math.abs(m[i][j]) < EPS) {
                    count++;
                }
            }
            if (count > nolDiKolom) {
                nolDiKolom = count;
                kolomTerbaik = j;
            }
        }

        double hasil = 0.0;
        String elemenKaliKofaktor = "";

        if (nolDiBaris >= nolDiKolom) {
            System.out.println(">> Ekspansi kofaktor pada baris: " + (barisTerbaik + 1));

            for (int j = 0; j < x; j++) {
                double elemen = m[barisTerbaik][j];
                System.out.println("\nElemen m[" + (barisTerbaik + 1) + "][" + (j + 1) + "] = " + formatAngka(elemen));

                if (Math.abs(elemen) < EPS) {
                    System.out.println("Elemen bernilai 0 (Dilewati)\n");
                    continue;
                }

                Matriks sub = M.getSubMatriksKofaktor(barisTerbaik, j);
                System.out.println("Submatriks kofaktor setelah menghapus Baris " + (barisTerbaik + 1) + " & Kolom " + (j + 1) + ":");
                sub.printMatriks();

                double minorDet = kofaktorRekursif(sub, depth + 1);
                int t = tanda(barisTerbaik, j);
                double nilaiKofaktor = t * minorDet;
                double hasilKali = elemen * nilaiKofaktor;
                hasil += hasilKali;

                System.out.println("Kofaktor C[" + (barisTerbaik + 1) + "][" + (j + 1) + "] = (" + (t > 0 ? "+1" : "-1") + ") * " + formatAngka(minorDet) + " = " + formatAngka(nilaiKofaktor));
                System.out.println("Hasil Perkalian m[" + (barisTerbaik + 1) + "][" + (j + 1) + "] * C[" + (barisTerbaik + 1) + "][" + (j + 1) + "] = " + formatAngka(elemen) + " * " + formatAngka(nilaiKofaktor) + " = " + formatAngka(hasilKali));

                if (!elemenKaliKofaktor.equals("")) {
                    if (hasilKali >= 0) {
                        elemenKaliKofaktor += " + " + String.format("%.3f", hasilKali);
                    } else {
                        elemenKaliKofaktor += " - " + String.format("%.3f", Math.abs(hasilKali));
                    }
                } else {
                    elemenKaliKofaktor += formatAngka(hasilKali);
                }
            }
        } else {
            System.out.println(">> Ekspansi kofaktor pada kolom: " + (kolomTerbaik + 1));

            for (int i = 0; i < x; i++) {
                double elemen = m[i][kolomTerbaik];
                System.out.println("Elemen m[" + (i + 1) + "][" + (kolomTerbaik + 1) + "] = " + formatAngka(elemen) + "\n");

                if (Math.abs(elemen) < EPS) {
                    System.out.println("Elemen bernilai 0 (Dilewati).");
                    continue;
                }

                Matriks sub = M.getSubMatriksKofaktor(i, kolomTerbaik);
                System.out.println("Submatriks kofaktor setelah menghapus Baris " + (i + 1) + " & Kolom " + (kolomTerbaik + 1) + ":");
                sub.printMatriks();

                double minorDet = kofaktorRekursif(sub, depth + 1);
                int t = tanda(i, kolomTerbaik);
                double nilaiKofaktor = t * minorDet;
                double hasilKali = elemen * nilaiKofaktor;
                hasil += hasilKali;

                System.out.println("Kofaktor C[" + (i + 1) + "][" + (kolomTerbaik + 1) + "] = (" + (t > 0 ? "+1" : "-1") + ") * " + formatAngka(minorDet) + " = " + formatAngka(nilaiKofaktor));
                System.out.println("Hasil Perkalian m[" + (i + 1) + "][" + (kolomTerbaik + 1) + "] * C[" + (i + 1) + "][" + (kolomTerbaik + 1) + "] = " + formatAngka(elemen) + " * " + formatAngka(nilaiKofaktor) + " = " + formatAngka(hasilKali));

                if (!elemenKaliKofaktor.equals("")) {
                    if (hasilKali >= 0) {
                        elemenKaliKofaktor += " + " + String.format("%.3f", hasilKali);
                    } else {
                        elemenKaliKofaktor += " - " + String.format("%.3f", Math.abs(hasilKali));
                    }
                } else {
                    elemenKaliKofaktor += formatAngka(hasilKali);
                }
            }
        }

        System.out.println("Hasil: " + elemenKaliKofaktor + " = " + formatAngka(hasil));
        System.out.println("Total Determinan Sub-bagian ini = " + formatAngka(hasil) + "\n");

        return hasil;
    }
}
