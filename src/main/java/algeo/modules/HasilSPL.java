package algeo.modules;

import java.util.List;

public class HasilSPL {
    public enum Tipe { TUNGGAL, TIDAK_ADA, TAK_HINGGA }

    private final Tipe tipe;
    private final double[] nilai;
    private final String[] parametrik;
    private final List<String> langkah;

    private HasilSPL(Tipe tipe, double[] nilai, String[] parametrik, List<String> langkah){
        this.tipe = tipe;
        this.nilai = nilai;
        this.parametrik = parametrik;
        this.langkah = langkah;
    }

    public static HasilSPL tunggal(double[] nilai, List<String> langkah){
        return new HasilSPL(Tipe.TUNGGAL, nilai, null, langkah);
    }

    public static HasilSPL tidakAda(List<String> langkah){
        return new HasilSPL(Tipe.TIDAK_ADA, null, null, langkah);
    }

    public static HasilSPL takHingga(String[] parametrik, List<String> langkah){
        return new HasilSPL(Tipe.TAK_HINGGA, null, parametrik, langkah);
    }

    public Tipe getTipe(){
        return this.tipe;
    }

    public double[] getNilai(){
        return this.nilai;
    }

    public String[] getParametrik(){
        return this.parametrik;
    }

    public List<String> getLangkah(){
        return this.langkah;
    }

    public String keTeks(){
        StringBuilder sb = new StringBuilder();

        if(tipe == Tipe.TIDAK_ADA){
            sb.append("Solusi tidak ada.\n");
        } else if(tipe == Tipe.TUNGGAL){
            sb.append("Solusi tunggal:\n");
            for(int i = 0; i < nilai.length; i++){
                sb.append("x").append(i + 1).append(" = ").append(SPL.format(nilai[i])).append("\n");
            }
        } else {
            sb.append("Solusi tak hingga banyak (parametrik):\n");
            for(int i = 0; i < parametrik.length; i++){
                sb.append("x").append(i + 1).append(" = ").append(parametrik[i]).append("\n");
            }
        }
        return sb.toString();
    }
}