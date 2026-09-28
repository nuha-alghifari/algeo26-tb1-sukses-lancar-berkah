package algeo.modules;

public class PolynomialInterpolation {

    public double[] x;
    public double[] koefisien;

    public void hitungInterpolasi(double[][] titikData) {
        int n = titikData.length;
        x = new double[n];

        Matriks augmented = new Matriks(n, n + 1);
        for (int i = 0; i < n; i++) {
            x[i] = titikData[i][0];
            double xp = 1.0;
            for (int j = 0; j < n; j++) {
                augmented.setElemen(i, j, xp);
                xp *= x[i];
            }
            augmented.setElemen(i, n, titikData[i][1]);
        }

        // --- Integrasi dengan library buatan temanmu (Matriks & SPL) ---
        HasilSPL hasil = SPL.gauss(augmented);

        if (hasil.getTipe() == HasilSPL.Tipe.TUNGGAL) {
            this.koefisien = hasil.getNilai();
        } else {
            throw new RuntimeException("Interpolasi gagal: sistem persamaan tidak memiliki solusi tunggal (periksa duplikasi nilai x)");
        }
    }

    public double evaluasi(double xBaru) {
        double hasil = 0.0;
        double xp = 1.0;
        for (double c : koefisien) {
            hasil += c * xp;
            xp *= xBaru;
        }
        return hasil;
    }

    public double getDomainMin() {
        double m = x[0];
        for (double v : x) m = Math.min(m, v);
        return m;
    }

    public double getDomainMax() {
        double m = x[0];
        for (double v : x) m = Math.max(m, v);
        return m;
    }

    public void printPersamaan() {
        System.out.printf("Domain interpolasi: [%.3f, %.3f]%n", getDomainMin(), getDomainMax());
        System.out.print("Persamaan: P(x) = ");

        boolean pertama = true;
        for (int i = koefisien.length - 1; i >= 0; i--) {
            double c = koefisien[i];
            if (Math.abs(c) < 1e-9) continue;
            if (!pertama) {
                System.out.print(c < 0 ? " - " : " + ");
            } else if (c < 0) {
                System.out.print("-");
            }
            System.out.printf("%.3f", Math.abs(c));
            if (i == 1) System.out.print("x");
            else if (i > 1) System.out.print("x^" + i);
            pertama = false;
        }
        if (pertama) System.out.print("0.000");
        System.out.println();
    }
}
