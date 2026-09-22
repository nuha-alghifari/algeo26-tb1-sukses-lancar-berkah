package algeo.modules;

import java.util.Scanner;

public class Matriks {
    private double[][] data;
    private int rows;
    private int cols;

    public Matriks(int rows, int cols){
        this.rows = rows;
        this.cols = cols;
        this.data = new double[rows][cols];
    }

    public void inputMatriks(Scanner scanner){
        if(this.rows > 12 || this.cols > 11){
            throw new IllegalArgumentException("Masukkan matriks maksimal 11 x 11 atau 11 x 12 untuk matriks augmented");
        }

        System.out.println("Masukkan matriks (" + this.rows + "x" + this.cols +") [Spasi sebagai pemisah]: ");

        for(int i = 0; i < this.rows; i++){
            for(int j = 0; j < this.cols; j++){
                boolean inputValid = false;

                while(!inputValid){
                    try{
                        String input = scanner.next();

                        input = input.replace(',', '.');

                        this.data[i][j] = Double.parseDouble(input);

                        inputValid = true;
                    } catch (NumberFormatException e){
                        throw new NumberFormatException("Input tidak valid, hanya masukkan angka untuk elemen matriks");
                    }
                }
            }
        }
    }

    public Matriks tambah(Matriks M2){
        if(this.rows != M2.rows || this.cols != M2.cols){
            throw new IllegalArgumentException("Ukuran matriks tidak sama");
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
            throw new IllegalArgumentException("Ukuran matriks tidak sama");
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
            throw new IllegalArgumentException("Matriks tidak bisa dikalikan karena jumlah elemen di baris 1 matriks 1 tidak sama dengan jumlah elemen di baris 1 matriks 2");
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

    public void tukarBaris(int row1, int row2){
        if(row1 < 0 || row1 >= this.rows || row2 < 0 || row2 >= this.rows){
            throw new IllegalArgumentException("Indeks Baris tidak valid");
        }

        if(row1 == row2){
            return;
        }

        for(int j = 0; j < this.cols; j++){
            double temp = this.data[row1][j];
            this.data[row1][j] = this.data[row2][j];
            this.data[row2][j] = temp;
        }
    }

    public Matriks getSubMatriksKofaktor(int skipRow, int skipCol){
        Matriks hasil = new Matriks(this.rows - 1, this.cols - 1);

        int r = 0;
        for(int i = 0; i < this.rows; i++){
            if(i == skipRow) continue;
            
            int c = 0;
            for(int j = 0; j < this.cols; j++){
                if(j == skipCol) continue;

                hasil.data[r][c] = this.data[i][j];
                c++;
            }
            r++;
        }
        return hasil;
    }
}

