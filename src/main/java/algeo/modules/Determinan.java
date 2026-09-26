package algeo.modules;

public class Determinan {
    public static double reduksiBaris(Matriks M){
        if(M.getRows() != M.getCols()){
            throw new IllegalArgumentException("Matriks tidak memiliki determinan");
        }

        int x = M.getRows();
        Matriks obe = M.copy();
        double[][] m = obe.getData();

        System.out.println("Menghitung Determinan Matriks Dengan Metode Reduksi Baris/OBE");
        System.out.println("Matriks Awal:");
        M.printMatriks();

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

            if(max == 0){
                System.out.println("Kolom " + i + "semuanya bernilai 0, determinan = 0");
                return 0.0;
            }

            if(pivot != i){
                obe.tukarBaris(i, pivot);
                jumlahTukarBaris++;
                System.out.println("Baris " + (i+1) + " ditukar dengan baris " + (pivot + 1));
                obe.printMatriks();
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
            System.out.println("Matriks setelah di lakukan obe: ");
            obe.printMatriks();
        }

        double kaliDiagonal = 1.0;
        for(int i = 0; i < x; i++){
            kaliDiagonal *= m[i][i];
        }

        double det;
        if(jumlahTukarBaris != 0){
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
        System.out.println("Menghitung Determinan Matriks Dengan Metode Reduksi Baris/OBE");
        return kofaktorRekursif(M, 0);
    }

    private static int tanda(int i, int j){
        if((i+j) % 2 == 0){
            return 1;
        } else {
            return -1;
        }
    }

    private static String indentasi(int depth){
        StringBuilder a = new StringBuilder();
        for(int i = 0; i < depth; i++){
            a.append("  ");
        }
        return a.toString();
    }

    public static double kofaktorRekursif(Matriks M, int depth){
        int x = M.getRows();
        double[][] m = M.getData();
        String indent = indentasi(depth);

        if(x == 1){
            return m[0][0];
        }

        if(x == 2){
            double hasilm2x2 = m[0][0]*m[1][1] - m[0][1]*m[1][0];
            System.out.println("Matriks 2x2, Determinan = " + String.format(".3%", hasilm2x2));
            return hasilm2x2;
        }

        int barisTerbaik = 0;
        int kolomTerbaik = 0;
        int nolDiBaris = -1;
        int nolDiKolom = -1;

        for(int i = 0; i < x; i++){
            int count = 0;
            for(int j = 0; j < x; j++){
                if(Math.abs(m[i][j]) == 0){
                    count++;
                }
            }
            if(count > nolDiBaris){
                nolDiBaris = count;
                barisTerbaik = i;
            }
        }

        for(int j = 0; j < x; j++){
            int count = 0;
            for(int i = 0; i < x; i++){
                if(Math.abs(m[i][j]) == 0){
                    count++;
                }
            }
            if(count > nolDiKolom){
                nolDiKolom = count;
                kolomTerbaik = j;
            }
        }

        double hasil = 0.0;

        if(nolDiBaris >= nolDiKolom){
            System.out.println("Ekspansi kofaktor pada baris: " + (barisTerbaik + 1));
            for(int j = 0; j < x; j++){
                double elemen = m[barisTerbaik][j];
                if(Math.abs(elemen) == 0) continue;
                Matriks sub = M.getSubMatriksKofaktor(barisTerbaik, j);
                double minorDet = kofaktorRekursif(sub, depth + 1);
                double nilaiKofaktor = tanda(barisTerbaik, j)*minorDet;
                hasil += elemen*nilaiKofaktor;
                System.out.println(indent + "  a[" + (barisTerbaik + 1) + "][" + (j + 1) + "]=" + elemen + " x kofaktor=" + String.format("%.3f", nilaiKofaktor));
            }
        } else {
            System.out.println("Ekspansi kofaktor pada kolom: " + (kolomTerbaik + 1));
            for(int i = 0; i < x; i++){
                double elemen = m[i][kolomTerbaik];
                if(Math.abs(elemen) == 0) continue;
                Matriks sub = M.getSubMatriksKofaktor(i, kolomTerbaik);
                double minorDet = kofaktorRekursif(sub, depth + 1);
                double nilaiKofaktor = tanda(i, kolomTerbaik)*minorDet;
                hasil += elemen*nilaiKofaktor;
                System.out.println(indent + "  a[" + (i + 1) + "][" + (kolomTerbaik + 1) + "]=" + elemen + " x kofaktor =" + String.format("%.3f", nilaiKofaktor));
            }
        }
    return hasil;
    }

    public static String formatOutput(String metode, Matriks input, double determinan) {
        StringBuilder sb = new StringBuilder();
        sb.append("Metode perhitungan determinan: ").append(metode).append("\n");
        sb.append("Input matriks yang digunakan:\n");
        double[][] m = input.getData();
        for (int i = 0; i < input.getRows(); i++) {
            for (int j = 0; j < input.getCols(); j++) {
                sb.append(String.format("%.3f ", m[i][j]));
            }
            sb.append("\n");
        }
        sb.append("Hasil determinan: ").append(String.format("%.3f", determinan)).append("\n");
        return sb.toString();
    }
}
