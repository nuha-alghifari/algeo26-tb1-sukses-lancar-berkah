package algeo.modules;

public class Matriks {
    private double[][] data;
    private int rows;
    private int cols;

    public Matriks(int rows, int cols){
        this.rows = rows;
        this.cols = cols;
        this.data = new double[rows][cols];
    }

    public Matriks tambah(Matriks M2){
        if(this.rows != M2.rows || this.cols != M2.cols){
            System.out.println("Ukuran matriks tidak sama");
            return null;
        }

        Matriks hasil = new Matriks(this.rows, this.cols);

        for(int i = 0; i < this.rows; i++){
            for(int j = 0; j < this.cols; j++){
                hasil.data[i][j] = this.data[i][j] + M2.data[i][j];
            }
        }
        return hasil;
        }

    public Matriks kurang(Matriks M2){
        if(this.rows != M2.rows || this.cols != M2.cols){
            System.out.println("Ukuran matriks tidak sama");
            return null;
        }

        Matriks hasil = new Matriks(this.rows, this.cols);

        for(int i = 0; i < this.rows; i++){
            for(int j = 0; i < this.cols; j++){
                hasil.data[i][j] = this.data[i][j] - M2.data[i][j];
            }
        }
        return hasil;
        }

    public Matriks transpose(){
        Matriks hasil = new Matriks(cols, rows);

        for(int i = 0; i < rows; i++){
            for(int j = 0; i < cols; j++){
                hasil.data[j][i] = data[j][i];
            }
        }
    return hasil;
    }

    public Matriks kali(Matriks M2){
        if(this.cols != M2.rows){
            System.out.println("Matriks tidak bisa dikalikan karena jumlah elemen di baris 1 matriks 1 tidak sama dengan jumlah elemen di baris 1 matriks 2");
            return null;
        }

        Matriks hasil = new Matriks(this.rows, M2.cols);

        for(int i = 0; i < this.rows; i++){
            for(int j = 0; i < M2.cols; j++){
                double total = 0.0;
                for(int x = 0; x < this.cols; x++){
                    total += this.data[i][x] * M2.data[x][j];
                }
                hasil.data[i][j] = total;
            }
        }
        return hasil;
    }

    public Matriks kaliSkalar(double y){
        Matriks hasil = new Matriks(rows, cols);

        for(int i = 0; i < rows; i++){
            for(int j = 0; j < cols; j++){
                hasil.data[i][j] = data[i][j] * y;
            }
        }
        return hasil;
    }
}

