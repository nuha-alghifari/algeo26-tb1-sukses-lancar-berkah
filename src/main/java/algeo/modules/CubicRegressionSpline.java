package algeo.modules;

public class CubicRegressionSpline {

    public double[] knots;
    public double[] beta;
    public int derajat = 3;

    public void hitungRegresi(double[][] data, double[] inputKnots) {
        hitungRegresi(data, inputKnots, 3);
    }

    public void hitungRegresi(double[][] data, double[] inputKnots, int derajat) {
        if (derajat < 1 || derajat > 3) {
            throw new IllegalArgumentException("Derajat spline harus 1, 2, atau 3");
        }
        this.derajat = derajat;
        int N = data.length;
        this.knots = inputKnots;
        int K = knots.length;
        int M = derajat + 1 + K;

        double[][] X = new double[N][M];
        double[] y = new double[N];

        for (int i = 0; i < N; i++) {
            double xi = data[i][0];
            y[i] = data[i][1];

            double pangkat = 1.0;
            for (int j = 0; j <= derajat; j++) {
                X[i][j] = pangkat;
                pangkat *= xi;
            }

            for (int k = 0; k < K; k++) {
                double diff = xi - knots[k];
                X[i][derajat + 1 + k] = (diff > 0) ? Math.pow(diff, derajat) : 0.0;
            }
        }

        double[][] XtX = new double[M][M];
        for (int i = 0; i < M; i++) {
            for (int j = 0; j < M; j++) {
                double sum = 0;
                for (int k = 0; k < N; k++) {
                    sum += X[k][i] * X[k][j];
                }
                XtX[i][j] = sum;
            }
        }

        double[] XtY = new double[M];
        for (int i = 0; i < M; i++) {
            double sum = 0;
            for (int k = 0; k < N; k++) {
                sum += X[k][i] * y[k];
            }
            XtY[i] = sum;
        }

        Matriks augmented = new Matriks(M, M + 1);
        for (int i = 0; i < M; i++) {
            for (int j = 0; j < M; j++) {
                augmented.setElemen(i, j, XtX[i][j]);
            }
            augmented.setElemen(i, M, XtY[i]);
        }

        HasilSPL hasil = SPL.gauss(augmented);

        if (hasil.getTipe() == HasilSPL.Tipe.TUNGGAL) {
            this.beta = hasil.getNilai();
        } else {
            throw new RuntimeException("Regresi Spline gagal: Sistem persamaan normal tidak memiliki solusi tunggal (jumlah titik data kurang dari jumlah basis " + M + ", atau posisi knot tidak sesuai).");
        }
    }

    public double prediksi(double xBaru) {
        double y = 0.0;
        double pangkat = 1.0;
        for (int j = 0; j <= derajat; j++) {
            y += beta[j] * pangkat;
            pangkat *= xBaru;
        }
        for (int k = 0; k < knots.length; k++) {
            double diff = xBaru - knots[k];
            if (diff > 0) {
                y += beta[derajat + 1 + k] * Math.pow(diff, derajat);
            }
        }
        return y;
    }

    public void printPersamaan() {
        System.out.println("Derajat spline: " + derajat);
        System.out.println("Posisi Knot yang digunakan:");
        for(int k = 0; k < knots.length; k++) System.out.printf("Knot[%d] = %.3f\n", k + 1, knots[k]);

        System.out.println("\nKoefisien Regresi (Beta):");
        for (int i = 0; i < beta.length; i++) {
            System.out.printf("Beta[%d] = %.3f\n", i, bersih(beta[i]));
        }

        System.out.print("\nPersamaan Regresi: y = ");
        System.out.printf("%.3f", bersih(beta[0]));
        for (int j = 1; j <= derajat; j++) {
            if (j == 1) System.out.printf(" + %.3f(x)", bersih(beta[j]));
            else System.out.printf(" + %.3f(x^%d)", bersih(beta[j]), j);
        }
        for (int k = 0; k < knots.length; k++) {
            System.out.printf(" + %.3f(x - %.3f)^%d_+", bersih(beta[derajat + 1 + k]), knots[k], derajat);
        }
        System.out.println();
    }

    private static double bersih(double v) {
        return Math.abs(v) < 0.0005 ? 0.0 : v;
    }
}
