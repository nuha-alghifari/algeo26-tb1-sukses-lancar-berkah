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
        return 0;
    }

}
