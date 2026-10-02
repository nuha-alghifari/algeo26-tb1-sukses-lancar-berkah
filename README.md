# Tugas Besar 1 IF2123 Aljabar Linier dan Geometri

**Kelompok:** sukses-lancar-berkah
**Semester I Tahun 2026/2027, Program Studi Teknik Informatika, STEI ITB**

Pustaka dan program Command Line Interface (CLI) berbahasa Java untuk menyelesaikan sistem persamaan linier (SPL), menghitung determinan, mencari matriks balikan (invers), interpolasi polinomial, interpolasi spline kubik natural, dan regresi spline. Seluruh perhitungan matriks ditulis sendiri (tanpa JAMA, EJML, Commons Math, atau pustaka aljabar linier lain) di atas kelas `Matriks` buatan kelompok.

## Anggota Kelompok

| Nama | NIM |
|------|-----|
| Ahmad Humam Isma| 13525032 |
| Mochammad Nuha Al Ghifari | 13525056 |
| Athallah Nanda Andita  | 13525148 |

## Fitur

| Menu | Fitur | Metode |
|------|-------|--------|
| 1 | Sistem Persamaan Linier | Eliminasi Gauss, Eliminasi Gauss-Jordan, Matriks Balikan (x = A^-1 b), Kaidah Cramer |
| 2 | Determinan | Reduksi baris (OBE), Ekspansi kofaktor |
| 3 | Matriks Balikan | Augmentasi [A\|I] dengan Gauss-Jordan, Adjoin |
| 4 | Interpolasi Polinomial | Polinomial derajat n-1 melalui n titik (matriks Vandermonde diselesaikan dengan Gauss) |
| 5 | Natural Cubic Spline | Spline kubik per segmen dengan syarat batas natural (turunan kedua di ujung = 0) |
| 6 | Regresi Spline Kubik | Basis daya terpancung (truncated power basis) dan persamaan normal; derajat dapat dipilih 1 sampai 3 (default 3 sesuai spesifikasi) |

Catatan perilaku:

- Eliminasi Gauss dan Gauss-Jordan memakai **partial pivoting**.
- Solusi SPL dilaporkan sebagai solusi tunggal, tidak ada solusi, atau tak hingga (bentuk parametrik dengan variabel bebas `t`, `s`, atau `a1`..`an`).
- Seluruh keluaran angka dibulatkan sampai 3 angka di belakang koma. Angka masukan boleh memakai titik atau koma sebagai tanda desimal.
- Langkah perhitungan (tahapan OBE, matriks yang terbentuk) ditampilkan di layar. Untuk matriks lebih dari 11 baris, langkah per baris diringkas agar memori tidak habis.
- Hasil dapat disimpan ke berkas `.txt` setelah setiap perhitungan.
- Kesalahan (masukan bukan angka, dimensi tidak cocok, berkas tidak ditemukan atau formatnya salah, matriks singular, matriks tidak persegi, dsb.) ditangkap dan ditampilkan sebagai pesan `[ERROR]`, program tidak berhenti.

## Struktur Repositori

```text
.
├── bin/                 berkas JAR program
├── docs/                laporan  (docs)
├── src/main/java/algeo/
│   ├── App.java         menu CLI (kelas utama)
│   └── modules/         pustaka aljabar linier
├── test/                data kasus uji (.txt)
├── pom.xml              konfigurasi Maven
└── README.md
```

Kelas di `src/main/java/algeo/modules`:

| Kelas | Peran |
|-------|-------|
| `Matriks` | struktur data `double[][]`, operasi dasar, dan operasi baris elementer |
| `SPL` | Gauss, Gauss-Jordan, Matriks Balikan, Cramer, partial pivoting |
| `HasilSPL` | hasil SPL (tunggal / tidak ada / tak hingga) beserta langkahnya |
| `Invers` | invers dengan [A\|I] dan Adjoin |
| `Determinan` | determinan dengan reduksi baris dan ekspansi kofaktor |
| `PolynomialInterpolation` | interpolasi polinomial |
| `NaturalCubicSpline` | interpolasi spline kubik natural |
| `CubicRegressionSpline` | regresi spline |
| `BerkasMatriks` | membaca dan menyimpan berkas `.txt` |

Diagram arsitektur, diagram kelas, dan diagram alur ada di [docs/diagram](docs/diagram):
`arsitektur-pustaka`, `diagram-kelas-inti`, `diagram-kelas-fitur`, `alur-program`, dan `alur-eliminasi-spl` (masing-masing tersedia dalam format `.svg` dan `.png`).

## Persyaratan

- Java 17 atau lebih baru
- Apache Maven 3.6.3 atau lebih baru

Periksa instalasi:

```bash
java --version
mvn --version
```

## Cara Menjalankan

Kompilasi:

```bash
mvn clean compile
```

Jalankan program dari Maven:

```bash
mvn exec:java
```

Membuat berkas JAR (hasilnya langsung ditulis ke folder `bin`):

```bash
mvn clean package
```

Jalankan program dari JAR (cukup Java 17 atau lebih baru, tanpa Maven):

```bash
java -jar bin/matrix-calculator-1.0-SNAPSHOT.jar
```

Jalankan perintah di folder utama repositori supaya lokasi relatif berkas uji seperti `test/spl_kasus1.txt` terbaca.

## Alur Penggunaan

1. Pilih fitur dari **MENU UTAMA** (1 sampai 7; 7 untuk keluar).
2. Untuk menu 1, 2, dan 3, pilih **metode** pada sub-menu. Untuk menu 6, program menanyakan derajat spline (isi `3` untuk spline kubik) dan posisi knot.
3. Pilih **sumber input**: keyboard atau berkas `.txt`.
4. Program menampilkan langkah perhitungan dan hasilnya.
5. Untuk menu 4, 5, dan 6, program dapat mengevaluasi nilai `x` tambahan berulang kali.
6. Program menawarkan penyimpanan hasil ke berkas `.txt`, lalu kembali ke menu utama.

Contoh sesi (SPL dengan Eliminasi Gauss, masukan dari `test/spl_kasus1.txt`):

```text
MENU UTAMA
1. Sistem Persamaan Linier (SPL)
...
Pilih menu: 1

SUB-MENU SPL
1. Metode Eliminasi Gauss
2. Metode Eliminasi Gauss-Jordan
3. Metode Matriks Balikan
4. Kaidah Cramer
Pilih metode: 1
Sumber input:
1. Keyboard
2. Berkas .txt
Pilih sumber: 2
Lokasi berkas: test/spl_kasus1.txt
... (langkah OBE) ...
Solusi tunggal:
x1 = 1.000
x2 = -2.000
x3 = 3.000
x4 = 2.000

Simpan hasil ke berkas .txt? (y/n):
```

## Format Berkas Masukan

Elemen dipisahkan spasi, satu baris per baris matriks atau per titik data. Baris kosong di akhir berkas diabaikan; baris kosong di tengah berkas, jumlah kolom yang tidak sama, dan isi yang bukan angka dilaporkan sebagai kesalahan format.

SPL (matriks augmented, kolom terakhir adalah konstanta), contoh `spl_kasus1.txt`:

```text
0 2 -1 1 -5
1 -1 2 3 15
2 1 1 -1 1
-1 3 0 2 -3
```

Determinan dan Invers (matriks persegi), contoh `invers_kasus1.txt`:

```text
0 1 2 -1
1 0 1 2
2 1 0 1
-1 2 1 0
```

Interpolasi, spline natural, dan regresi (satu pasangan `x y` per baris), contoh `interpolasi_kasus2.txt`:

```text
0 25.0
2 31.2
4 38.6
6 47.5
8 58.1
10 70.8
```

## Batasan

| Hal | Batas |
|-----|-------|
| Masukan keyboard | matriks maks 11 x 11 (augmented 11 x 12) |
| Masukan berkas | matriks maks 1001 x 1001 (augmented 1001 x 1002) |
| Titik data interpolasi / spline / regresi | maks 10 titik |
| Spline natural | minimal 3 titik |
| Kaidah Cramer dan Adjoin | maks n = 100 (butuh banyak determinan; untuk matriks lebih besar gunakan Gauss, Gauss-Jordan, atau [A\|I]) |

## Kasus Uji

Data uji ada di folder `test/`. Nama berkas mengikuti pola `spl_xxx.txt`, `determinan_xxx.txt`, `invers_xxx.txt`, `interpolasi_xxx.txt`, `spline_natural_xxx.txt`, dan `regresi_xxx.txt`.

| Berkas | Isi | Hasil yang diharapkan |
|--------|-----|-----------------------|
| `spl_kasus1.txt` | SPL 4 x 4 (kasus 5.3.1) | x = (1, -2, 3, 2) |
| `spl_kasus2.txt` | SPL 3 x 5 (kasus 5.3.2) | tak hingga, 2 variabel bebas |
| `spl_kasus3.txt` | SPL 4 x 4 (kasus 5.3.3) | tidak ada solusi |
| `spl_laplace_n6.txt`, `spl_laplace_n10.txt` | discrete Laplacian (kasus 5.3.4) | n = 6: (3, 5, 6, 6, 5, 3); n = 10: (5, 9, 12, 14, 15, 15, 14, 12, 9, 5) |
| `spl_augmented1.txt`, `spl_augmented2.txt` | matriks augmented (kasus 5.4) | tunggal (1, -2, 3, 2); tak hingga dengan 2 variabel bebas |
| `spl_umum_kasus1.txt`, `spl_umum_kasus2.txt` | SPL bentuk umum (kasus 5.5) | tunggal; tak hingga dengan 1 variabel bebas |
| `spl_aplikasi_regresi.txt` | sistem normal regresi linier (kasus 5.6) | theta0 = 1.667, theta1 = 1.5 |
| `determinan_kasus1.txt` .. `3` | determinan (kasus 5.1) | -259, 0, 0.010 |
| `invers_kasus1.txt`, `invers_kasus2.txt` | invers (kasus 5.2) | invers 4 x 4; matriks singular |
| `interpolasi_kasus1.txt`, `interpolasi_kasus2.txt` | interpolasi polinomial (kasus 5.7) | polinomial derajat 6 dan derajat 5 |
| `spline_natural_kasus1.txt` | natural cubic spline (kasus 5.8) | turunan kedua semua knot = 0 (data linear) |
| `regresi_kasus1.txt` | regresi spline (kasus 5.9) | pilih derajat 1, 1 knot di x = 3: y = 2x - 3(x - 3)+ |
|
