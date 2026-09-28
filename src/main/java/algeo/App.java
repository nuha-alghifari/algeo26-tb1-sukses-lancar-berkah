package algeo;

import algeo.modules.BerkasMatriks;
import algeo.modules.CubicRegressionSpline;
import algeo.modules.Determinan;
import algeo.modules.HasilSPL;
import algeo.modules.Invers;
import algeo.modules.Matriks;
import algeo.modules.NaturalCubicSpline;
import algeo.modules.PolynomialInterpolation;
import algeo.modules.SPL;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class App {
    private static final int MAKS_MANUAL = 11;
    private static final int MAKS_BERKAS = 1001;
    private static final int MAKS_TITIK = 10;

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        boolean jalan = true;

        while(jalan){
            System.out.println("\nMENU UTAMA");
            System.out.println("1. Sistem Persamaan Linier (SPL)");
            System.out.println("2. Determinan Matriks");
            System.out.println("3. Matriks Balikan (Invers)");
            System.out.println("4. Interpolasi Polinomial");
            System.out.println("5. Natural Cubic Spline Interpolation");
            System.out.println("6. Regresi Spline Kubik");
            System.out.println("7. Keluar");
            int pilihan = bacaInt(sc, "Pilih menu: ", 1, 7);

            try{
                switch(pilihan){
                    case 1: menuSPL(sc); break;
                    case 2: menuDeterminan(sc); break;
                    case 3: menuInvers(sc); break;
                    case 4: menuInterpolasi(sc); break;
                    case 5: menuNaturalSpline(sc); break;
                    case 6: menuRegresiSpline(sc); break;
                    case 7: jalan = false; System.out.println("Program selesai."); break;
                    default: System.out.println("Menu ini belum tersedia.");
                }
            } catch (IllegalArgumentException e){
                System.out.println("[ERROR] " + e.getMessage());
            } catch (Exception e){
                System.out.println("[ERROR] Terjadi kesalahan tak terduga: " + e.getMessage());
            }
        }
        sc.close();
    }

    private static void menuSPL(Scanner sc){
        System.out.println("\nSUB-MENU SPL");
        System.out.println("1. Metode Eliminasi Gauss");
        System.out.println("2. Metode Eliminasi Gauss-Jordan");
        System.out.println("3. Metode Matriks Balikan");
        System.out.println("4. Kaidah Cramer");
        int metode = bacaInt(sc, "Pilih metode: ", 1, 4);

        Matriks augmented = ambilMatriks(sc, true);

        String namaMetode;
        HasilSPL hasil;
        switch(metode){
            case 1: namaMetode = "Eliminasi Gauss"; hasil = SPL.gauss(augmented); break;
            case 2: namaMetode = "Eliminasi Gauss-Jordan"; hasil = SPL.gaussJordan(augmented); break;
            case 3: namaMetode = "Metode Matriks Balikan"; hasil = SPL.matriksBalikan(augmented); break;
            default: namaMetode = "Kaidah Cramer"; hasil = SPL.cramer(augmented);
        }

        cetakLangkah(hasil.getLangkah());
        String teksHasil = hasil.keTeks();
        System.out.println(teksHasil);

        String isi = "Metode penyelesaian SPL: " + namaMetode + "\n\n"
                + "Input matriks augmented:\n" + augmented.keString() + "\n"
                + "Hasil SPL:\n" + teksHasil;
        tawarSimpan(sc, isi);
    }

    private static void menuInvers(Scanner sc){
        System.out.println("\nSUB-MENU MATRIKS BALIKAN");
        System.out.println("1. Metode Augmentasi [A|I] dengan Gauss-Jordan");
        System.out.println("2. Metode Adjoin");
        int metode = bacaInt(sc, "Pilih metode: ", 1, 2);

        Matriks a = ambilMatriks(sc, false);

        List<String> langkah = new ArrayList<>();
        String namaMetode = (metode == 1) ? "Augmentasi [A|I] dengan Gauss-Jordan" : "Adjoin";
        Matriks balikan = (metode == 1) ? Invers.gaussJordan(a, langkah) : Invers.adjoin(a, langkah);

        cetakLangkah(langkah);
        System.out.println("Matriks balikan:\n" + balikan.keString());

        String isi = "Metode matriks balikan: " + namaMetode + "\n\n"
                + "Input matriks:\n" + a.keString() + "\n"
                + "Hasil matriks balikan:\n" + balikan.keString();
        tawarSimpan(sc, isi);
    }

    private static void menuDeterminan(Scanner sc){
        System.out.println("\nSUB-MENU DETERMINAN");
        System.out.println("1. Metode Reduksi Baris / OBE");
        System.out.println("2. Metode Ekspansi Kofaktor");
        int metode = bacaInt(sc, "Pilih metode: ", 1, 2);

        Matriks m = ambilMatriks(sc, false);
        String namaMetode = (metode == 1) ? "Reduksi Baris / OBE" : "Ekspansi Kofaktor";

        double[] hasilDet = new double[1];
        String langkahTeks = tangkapOutput(() -> {
            hasilDet[0] = (metode == 1) ? Determinan.reduksiBaris(m) : Determinan.kofaktor(m);
        });

        String hasilTeks = "Nilai determinan = " + SPL.format(hasilDet[0]);
        System.out.println(hasilTeks);

        String isi = "Metode perhitungan determinan: " + namaMetode + "\n\n"
                + langkahTeks + "\n"
                + "Input matriks:\n" + m.keString() + "\n"
                + hasilTeks + "\n";
        tawarSimpan(sc, isi);
    }

    private static void menuInterpolasi(Scanner sc){
        double[][] titik = ambilTitik(sc);

        PolynomialInterpolation interp = new PolynomialInterpolation();
        String langkahTeks = tangkapOutput(() -> {
            interp.hitungInterpolasi(titik);
            interp.printPersamaan();
        });
        System.out.print(langkahTeks);

        StringBuilder isi = new StringBuilder();
        isi.append("Metode: Interpolasi Polinomial\n\n");
        isi.append("Titik sampel:\n");
        for(double[] p : titik) isi.append(SPL.format(p[0])).append("\t").append(SPL.format(p[1])).append("\n");
        isi.append("\n").append(langkahTeks);

        evaluasiBerulang(sc, "Evaluasi P(xt)? (y/n): ", "P", interp::evaluasi, isi);
        tawarSimpan(sc, isi.toString());
    }

    private static void menuNaturalSpline(Scanner sc){
        double[][] titik = ambilTitik(sc);
        if(titik.length < 3){
            throw new IllegalArgumentException("Natural cubic spline butuh minimal 3 titik data");
        }

        NaturalCubicSpline spline = new NaturalCubicSpline();
        String langkahTeks = tangkapOutput(() -> {
            spline.hitungSpline(titik, titik.length);
            spline.printPersamaan();
        });
        System.out.print(langkahTeks);

        StringBuilder isi = new StringBuilder();
        isi.append("Metode: Natural Cubic Spline Interpolation\n\n");
        isi.append("Titik sampel:\n");
        for(double[] p : titik) isi.append(SPL.format(p[0])).append("\t").append(SPL.format(p[1])).append("\n");
        isi.append("\n").append(langkahTeks);

        evaluasiBerulang(sc, "Evaluasi S(xt)? (y/n): ", "S", spline::evaluasiSpline, isi);
        tawarSimpan(sc, isi.toString());
    }

    private static void menuRegresiSpline(Scanner sc){
        double[][] titik = ambilTitik(sc);

        int k = bacaInt(sc, "Jumlah knot K: ", 0, titik.length);
        double[] knot = new double[k];
        for(int i = 0; i < k; i++){
            knot[i] = bacaDouble(sc, "Posisi knot ke-" + (i + 1) + ": ");
        }

        CubicRegressionSpline regresi = new CubicRegressionSpline();
        String langkahTeks = tangkapOutput(() -> {
            regresi.hitungRegresi(titik, knot);
            regresi.printPersamaan();
        });
        System.out.print(langkahTeks);

        StringBuilder isi = new StringBuilder();
        isi.append("Metode: Regresi Spline Kubik\n\n");
        isi.append("Titik sampel:\n");
        for(double[] p : titik) isi.append(SPL.format(p[0])).append("\t").append(SPL.format(p[1])).append("\n");
        isi.append("\n").append(langkahTeks);

        evaluasiBerulang(sc, "Prediksi y_hat(xt)? (y/n): ", "y_hat", regresi::prediksi, isi);
        tawarSimpan(sc, isi.toString());
    }

    private interface FungsiEvaluasi { double hitung(double x); }

    private static void evaluasiBerulang(Scanner sc, String prompt, String label, FungsiEvaluasi f, StringBuilder isi){
        while(true){
            System.out.print(prompt);
            if(!sc.nextLine().trim().equalsIgnoreCase("y")) break;
            double xt = bacaDouble(sc, "xt = ");
            double yt = f.hitung(xt);
            String baris = label + "(" + SPL.format(xt) + ") = " + SPL.format(yt);
            System.out.println(baris);
            isi.append(baris).append("\n");
        }
    }

    private static double[][] ambilTitik(Scanner sc){
        System.out.println("Sumber input:");
        System.out.println("1. Keyboard");
        System.out.println("2. Berkas .txt");
        int sumber = bacaInt(sc, "Pilih sumber: ", 1, 2);

        if(sumber == 1){
            int n = bacaInt(sc, "Jumlah titik data (maks " + MAKS_TITIK + "): ", 1, MAKS_TITIK);
            double[][] titik = new double[n][2];
            for(int i = 0; i < n; i++){
                titik[i][0] = bacaDouble(sc, "x" + (i + 1) + " = ");
                titik[i][1] = bacaDouble(sc, "y" + (i + 1) + " = ");
            }
            return titik;
        }

        System.out.print("Lokasi berkas: ");
        Matriks m = BerkasMatriks.baca(sc.nextLine().trim());
        if(m.getCols() != 2){
            throw new IllegalArgumentException("Format berkas titik salah: tiap baris harus berisi tepat 2 nilai (x y)");
        }
        if(m.getRows() > MAKS_TITIK){
            throw new IllegalArgumentException("Jumlah titik data melebihi batas maksimal " + MAKS_TITIK);
        }
        double[][] titik = new double[m.getRows()][2];
        for(int i = 0; i < m.getRows(); i++){
            titik[i][0] = m.getElemen(i, 0);
            titik[i][1] = m.getElemen(i, 1);
        }
        return titik;
    }

    // Modul teman (Determinan, spline, regresi) mencetak langkah langsung ke System.out.
    // Supaya bisa ikut disimpan ke berkas .txt, keluarannya ditangkap dulu di sini.
    private static String tangkapOutput(Runnable aksi){
        PrintStream asli = System.out;
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        System.setOut(new PrintStream(buffer, true, StandardCharsets.UTF_8));
        try{
            aksi.run();
        } finally {
            System.setOut(asli);
        }
        return buffer.toString(StandardCharsets.UTF_8);
    }

    private static double bacaDouble(Scanner sc, String prompt){
        while(true){
            System.out.print(prompt);
            String masukan = sc.nextLine().trim().replace(',', '.');
            try{
                return Double.parseDouble(masukan);
            } catch (NumberFormatException e){
                System.out.println("Masukan harus berupa angka.");
            }
        }
    }

    private static Matriks ambilMatriks(Scanner sc, boolean augmented){
        System.out.println("Sumber input:");
        System.out.println("1. Keyboard");
        System.out.println("2. Berkas .txt");
        int sumber = bacaInt(sc, "Pilih sumber: ", 1, 2);

        if(sumber == 1){
            int baris = bacaInt(sc, "Jumlah baris (maks " + MAKS_MANUAL + "): ", 1, MAKS_MANUAL);
            int maksKolom = augmented ? MAKS_MANUAL + 1 : MAKS_MANUAL;
            int kolom = bacaInt(sc, "Jumlah kolom" + (augmented ? " termasuk kolom konstanta" : "") + " (maks " + maksKolom + "): ", augmented ? 2 : 1, maksKolom);

            Matriks m = new Matriks(baris, kolom);
            m.inputMatriks(sc);
            sc.nextLine();
            return m;
        }

        System.out.print("Lokasi berkas: ");
        Matriks m = BerkasMatriks.baca(sc.nextLine().trim());
        int kolomEfektif = augmented ? m.getCols() - 1 : m.getCols();
        if(m.getRows() > MAKS_BERKAS || kolomEfektif > MAKS_BERKAS){
            throw new IllegalArgumentException("Ukuran matriks melebihi batas " + MAKS_BERKAS + "x" + MAKS_BERKAS);
        }
        if(augmented && m.getCols() < 2){
            throw new IllegalArgumentException("Matriks augmented minimal memiliki 2 kolom");
        }
        return m;
    }

    private static void cetakLangkah(List<String> langkah){
        for(String s : langkah){
            System.out.println(s);
            System.out.println("----------------------------------------");
        }
    }

    private static void tawarSimpan(Scanner sc, String isi){
        System.out.print("Simpan hasil ke berkas .txt? (y/n): ");
        if(sc.nextLine().trim().equalsIgnoreCase("y")){
            System.out.print("Nama berkas keluaran: ");
            String lokasi = sc.nextLine().trim();
            BerkasMatriks.simpan(lokasi, isi);
            System.out.println("Hasil disimpan ke " + lokasi);
        }
    }

    private static int bacaInt(Scanner sc, String prompt, int min, int maks){
        while(true){
            System.out.print(prompt);
            String masukan = sc.nextLine().trim();
            try{
                int nilai = Integer.parseInt(masukan);
                if(nilai >= min && nilai <= maks){
                    return nilai;
                }
                System.out.println("Masukan harus antara " + min + " dan " + maks + ".");
            } catch (NumberFormatException e){
                System.out.println("Masukan harus berupa bilangan bulat.");
            }
        }
    }
}
