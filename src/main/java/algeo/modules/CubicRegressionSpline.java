package algeo.modules;

public class CubicRegressionSpline {
    
    public double[] knots;
    public double[] beta;
    
    public void hitungRegresi(double[][] data, double[] inputKnots) {
        int N = data.length;
        this.knots = inputKnots;
        int K = knots.length;
        int M = 4 + K;
        
        double[][] X = new double[N][M];
        double[] y = new double[N];
        
        for (int i = 0; i < N; i++) {
            double xi = data[i][0];
            y[i] = data[i][1];
            
            X[i][0] = 1.0;
            X[i][1] = xi;
            X[i][2] = xi * xi;
            X[i][3] = xi * xi * xi;
            
            for (int k = 0; k < K; k++) {
                double diff = xi - knots[k];
                X[i][4 + k] = (diff > 0) ? (diff * diff * diff) : 0.0;
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
        
        this.beta = solveSPLGauss(XtX, XtY);
    }
    
    private double[] solveSPLGauss(double[][] A, double[] b) {
        int n = b.length;
        double[][] aug = new double[n][n + 1];
        
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) aug[i][j] = A[i][j];
            aug[i][n] = b[i];
        }
        
        for (int i = 0; i < n; i++) {
            int maxIter = i;
            for (int k = i + 1; k < n; k++) {
                if (Math.abs(aug[k][i]) > Math.abs(aug[maxIter][i])) maxIter = k;
            }
            double[] temp = aug[i];
            aug[i] = aug[maxIter];
            aug[maxIter] = temp;
            
            for (int k = i + 1; k < n; k++) {
                double factor = aug[k][i] / aug[i][i];
                for (int j = i; j <= n; j++) aug[k][j] -= factor * aug[i][j];
            }
        }
        
        double[] res = new double[n];
        for (int i = n - 1; i >= 0; i--) {
            double sum = 0.0;
            for (int j = i + 1; j < n; j++) sum += aug[i][j] * res[j];
            res[i] = (aug[i][n] - sum) / aug[i][i];
        }
        return res;
    }
    
    public double prediksi(double xBaru) {
        double y = beta[0] + beta[1] * xBaru + beta[2] * Math.pow(xBaru, 2) + beta[3] * Math.pow(xBaru, 3);
        for (int k = 0; k < knots.length; k++) {
            double diff = xBaru - knots[k];
            if (diff > 0) {
                y += beta[4 + k] * Math.pow(diff, 3);
            }
        }
        return y;
    }
    
    public void printPersamaan() {
        System.out.println("Posisi Knot yang digunakan:");
        for(int k=0; k<knots.length; k++) System.out.printf("Knot[%d] = %.3f\n", k+1, knots[k]);
        
        System.out.println("\nKoefisien Regresi (Beta):");
        for (int i = 0; i < beta.length; i++) {
            System.out.printf("Beta[%d] = %.3f\n", i, beta[i]);
        }
        
        System.out.print("\nPersamaan Regresi: y = ");
        System.out.printf("%.3f + %.3f(x) + %.3f(x^2) + %.3f(x^3)", beta[0], beta[1], beta[2], beta[3]);
        for (int k = 0; k < knots.length; k++) {
            System.out.printf(" + %.3f(x - %.3f)^3_+", beta[4 + k], knots[k]);
        }
        System.out.println();
    }
}