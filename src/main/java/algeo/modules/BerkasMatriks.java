package algeo.modules;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class BerkasMatriks {

    public static Matriks baca(String lokasi){
        List<String> semuaBaris;
        try{
            semuaBaris = Files.readAllLines(Paths.get(lokasi), StandardCharsets.UTF_8);
        } catch (NoSuchFileException e){
            throw new IllegalArgumentException("Berkas tidak ditemukan: " + lokasi);
        } catch (IOException | RuntimeException e){
            throw new IllegalArgumentException("Berkas tidak dapat dibuka: " + lokasi);
        }

        int akhir = semuaBaris.size();
        while(akhir > 0 && semuaBaris.get(akhir - 1).trim().isEmpty()){
            akhir--;
        }
        if(akhir == 0){
            throw new IllegalArgumentException("Berkas kosong");
        }

        List<double[]> data = new ArrayList<>();
        int jumlahKolom = -1;

        for(int i = 0; i < akhir; i++){
            String baris = semuaBaris.get(i).trim();
            if(baris.isEmpty()){
                throw new IllegalArgumentException("Format berkas salah: ada baris kosong pada baris " + (i + 1));
            }

            String[] token = baris.split("\\s+");
            if(jumlahKolom == -1){
                jumlahKolom = token.length;
            } else if(token.length != jumlahKolom){
                throw new IllegalArgumentException("Format berkas salah: jumlah kolom pada baris " + (i + 1) + " tidak sama dengan baris sebelumnya");
            }

            double[] nilai = new double[jumlahKolom];
            for(int j = 0; j < jumlahKolom; j++){
                try{
                    nilai[j] = Double.parseDouble(token[j].replace(',', '.'));
                } catch (NumberFormatException e){
                    throw new IllegalArgumentException("Format berkas salah: \"" + token[j] + "\" pada baris " + (i + 1) + " bukan angka");
                }
            }
            data.add(nilai);
        }

        Matriks hasil = new Matriks(data.size(), jumlahKolom);
        for(int i = 0; i < data.size(); i++){
            for(int j = 0; j < jumlahKolom; j++){
                hasil.setElemen(i, j, data.get(i)[j]);
            }
        }
        return hasil;
    }

    public static void simpan(String lokasi, String teks){
        try{
            Path path = Paths.get(lokasi);
            Files.write(path, teks.getBytes(StandardCharsets.UTF_8));
        } catch (IOException | RuntimeException e){
            throw new IllegalArgumentException("Gagal menyimpan ke berkas: " + lokasi);
        }
    }
}
