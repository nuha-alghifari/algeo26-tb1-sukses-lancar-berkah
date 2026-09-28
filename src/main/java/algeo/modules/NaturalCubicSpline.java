package algeo.modules;

public class NaturalCubicSpline {
    
    public double[] x;
    public double[] a; 
    public double[] b;
    public double[] c; 
    public double[] d;
    
    public void hitungSpline(double[][] titikData, int n) {
        x = new double[n];
        a = new double[n];
        b = new double[n];
        c = new double[n];
        d = new double[n];
        
        for (int i = 0; i < n; i++) {
            x[i] = titikData[i][0];
            a[i] = titikData[i][1];
        }
        
        double[] h = new double[n - 1];
        for (int i = 0; i < n - 1; i++) {
            h[i] = x[i + 1] - x[i];
        }
        
        double[] alpha = new double[n];
        for (int i = 1; i < n - 1; i++) {
            alpha[i] = 3.0 / h[i] * (a[i + 1] - a[i]) - 3.0 / h[i - 1] * (a[i] - a[i - 1]);
        }
        
        double[] l = new double[n];
        double[] mu = new double[n];
        double[] z = new double[n];
        
        l[0] = 1.0;
        mu[0] = 0.0;
        z[0] = 0.0;
        
        for (int i = 1; i < n - 1; i++) {
            l[i] = 2.0 * (x[i + 1] - x[i - 1]) - h[i - 1] * mu[i - 1];
            mu[i] = h[i] / l[i];
            z[i] = (alpha[i] - h[i - 1] * z[i - 1]) / l[i];
        }
        
        l[n - 1] = 1.0;
        z[n - 1] = 0.0;
        c[n - 1] = 0.0;
        
        for (int j = n - 2; j >= 0; j--) {
            c[j] = z[j] - mu[j] * c[j + 1];
            b[j] = (a[j + 1] - a[j]) / h[j] - h[j] * (c[j + 1] + 2.0 * c[j]) / 3.0;
            d[j] = (c[j + 1] - c[j]) / (3.0 * h[j]);
        }
    }

    public double evaluasiSpline(double xBaru) {
        int n = x.length;
        int j = n - 2;
        
        for (int i = 0; i < n - 1; i++) {
            if (xBaru <= x[i + 1]) {
                j = i;
                break;
            }
        }
        
        double dx = xBaru - x[j];
        return a[j] + (b[j] * dx) + (c[j] * dx * dx) + (d[j] * dx * dx * dx);
    }

    public void printPersamaan() {
        int n = x.length;
        
        System.out.println("Nilai turunan kedua pada setiap knot:");
        for (int i = 0; i < n; i++) {
            System.out.printf("M[%d] = %.3f\n", i, 2.0 * c[i]);
        }
        
        System.out.println("\nPersamaan polinom kubik untuk setiap segmen:");
        for (int i = 0; i < n - 1; i++) {
            System.out.printf("S%d(x) = %.3f + (%.3f)(x - %.3f) + (%.3f)(x - %.3f)^2 + (%.3f)(x - %.3f)^3\n", 
                i, a[i], b[i], x[i], c[i], x[i], d[i], x[i]);
            System.out.printf("Domain: %.3f <= x <= %.3f\n", x[i], x[i+1]);
        }
    }
}