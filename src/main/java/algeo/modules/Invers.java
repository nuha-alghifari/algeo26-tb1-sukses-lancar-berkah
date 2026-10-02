package algeo.modules;

import java.util.List;

public class Invers {

    public static Matriks gaussJordan(Matriks a, List<String> langkah){
        cekPersegi(a);
        int n = a.getRows();

        Matriks aug = new Matriks(n, 2 * n);
        for(int i = 0; i < n; i++){
            for(int j = 0; j < n; j++){
                aug.setElemen(i, j, a.getElemen(i, j));
            }
            aug.setElemen(i, n + i, 1.0);
        }
        langkah.add("Matriks augmentasi [A|I]:\n" + SPL.tampil(aug));

        List<Integer> kolomPivot = SPL.eliminasiMaju(aug, n, langkah);
        if(kolomPivot.size() < n){
            throw new IllegalArgumentException("Matriks tidak memiliki balikan (matriks singular)");
        }
        SPL.eliminasiMundur(aug, kolomPivot, langkah);
        langkah.add("Hasil [I|A^-1]:\n" + SPL.tampil(aug));

        Matriks balikan = new Matriks(n, n);
        for(int i = 0; i < n; i++){
            for(int j = 0; j < n; j++){
                balikan.setElemen(i, j, aug.getElemen(i, n + j));
            }
        }
        return balikan;
    }

    public static Matriks adjoin(Matriks a, List<String> langkah){
        cekPersegi(a);
        int n = a.getRows();
        if(n > SPL.BATAS_METODE_MAHAL){
            throw new IllegalArgumentException("Metode adjoin butuh " + n + "x" + n + " determinan minor sehingga tidak praktis untuk n > " + SPL.BATAS_METODE_MAHAL + ". Gunakan metode Augmentasi [A|I] dengan Gauss-Jordan.");
        }

        double det = SPL.determinanInternal(a);
        langkah.add("det(A) = " + SPL.format(det));
        if(det == 0.0){
            throw new IllegalArgumentException("Matriks tidak memiliki balikan (determinan = 0)");
        }

        if(n == 1){
            Matriks satu = new Matriks(1, 1);
            satu.setElemen(0, 0, 1.0 / a.getElemen(0, 0));
            return satu;
        }

        Matriks kofaktor = new Matriks(n, n);
        for(int i = 0; i < n; i++){
            for(int j = 0; j < n; j++){
                double minor = SPL.determinanInternal(a.getSubMatriksKofaktor(i, j));
                double tanda = ((i + j) % 2 == 0) ? 1.0 : -1.0;
                kofaktor.setElemen(i, j, tanda * minor);
            }
        }
        langkah.add("Matriks kofaktor:\n" + SPL.tampil(kofaktor));

        Matriks adj = kofaktor.transpose();
        langkah.add("Matriks adjoin (transpose kofaktor):\n" + SPL.tampil(adj));

        Matriks balikan = adj.kaliSkalar(1.0 / det);
        langkah.add("A^-1 = (1/det(A)) * adjoin(A):\n" + SPL.tampil(balikan));
        return balikan;
    }

    private static void cekPersegi(Matriks a){
        if(a.getRows() != a.getCols()){
            throw new IllegalArgumentException("Matriks tidak memiliki balikan karena bukan matriks persegi");
        }
    }
}
