# Pengumpulan UTS Pemrograman Mobile

## Identitas Mahasiswa

- Nama: Azhis Fauzan Prasetiyo
- NIM: 2305101010
- Kelas: 7B
- Program Studi: Teknik Informatika
- Mata Kuliah: Pemrograman Mobile
- Tahun Akademik: 2026/2027

## Deskripsi

Repository ini berisi source code, dokumentasi, dan hasil implementasi
aplikasi mobile untuk pengerjaan Ujian Tengah Semester (UTS)
mata kuliah Pemrograman Mobile.

## Deskripsi Pengerjaan UTS

Aplikasi **Pengingat HST Tanaman** (`jadwal_perawatan`) adalah aplikasi Android native untuk
mengelola jadwal perawatan tanaman berdasarkan **HST (Hari Setelah Tanam)**. Pengguna mendaftarkan
tanaman beserta tanggal tanamnya, lalu aplikasi menghitung HST berjalan, fase pertumbuhan, progres
menuju panen, serta menyusun jadwal perawatan (pemupukan, penyiraman, penyiangan, pengendalian hama,
dan lainnya) pada HST tertentu. Aplikasi mengirim notifikasi pengingat pada jam yang dipilih pengguna.

## Teknologi yang Digunakan

- Bahasa Pemrograman: Kotlin
- Framework: Android SDK dengan Jetpack Compose (Material 3), ViewModel
- Tools: Android Studio, Gradle (Kotlin DSL), Android Emulator, Git & GitHub
- Database: SQLite (`SQLiteOpenHelper`, database lokal `plant_care_hst.db`)

## Fitur yang Diimplementasikan

1. **Manajemen tanaman (CRUD)**: tambah, ubah, dan hapus tanaman (nama, varietas, lokasi lahan, tanggal tanam, target HST panen, catatan).
2. **Perhitungan HST otomatis**: HST berjalan, fase pertumbuhan, progres panen, dan estimasi tanggal panen.
3. **Jadwal perawatan per HST**: tambah/ubah tugas perawatan dengan status Selesai, Hari Ini, Terlewat, dan Mendatang.
4. **Template jadwal tanaman**: jadwal perawatan siap pakai untuk tanaman tertentu.
5. **Penyelesaian tugas dengan catatan**: menandai tugas selesai beserta catatan pengerjaan.
6. **Pengingat/notifikasi**: alarm harian pada jam yang ditentukan, tetap aktif setelah perangkat di-restart (boot receiver).
7. **Halaman panduan**: informasi panduan perawatan tanaman.
8. **Penyimpanan lokal**: seluruh data tersimpan di SQLite sehingga dapat digunakan secara offline.

## Struktur Folder

- `uts/`: Source code dan file pendukung UTS.
- `uts/jadwal_perawatan/`: Project Android Studio (Kotlin + Jetpack Compose).
  - `app/src/main/java/com/example/belajar3/data/`: akses database SQLite (`PlantDatabaseHelper`, `PlantRepository`).
  - `app/src/main/java/com/example/belajar3/model/`: model data (`Plant`, `CareSchedule`, `CareType`, `PlantTemplate`).
  - `app/src/main/java/com/example/belajar3/reminder/`: alarm dan notifikasi pengingat.
  - `app/src/main/java/com/example/belajar3/ui/`: `PlantCareViewModel`, layar (`screens`), dialog (`dialogs`), komponen, dan tema.
  - `app/src/test/`: unit test perhitungan HST.
- `uts/assets/images/` dan `uts/assets/icons/`: aset pendukung.
- `uts/screenshots/`: Bukti visual hasil implementasi aplikasi.

## Persyaratan Menjalankan Aplikasi

- Android Studio versi terbaru (mendukung Android Gradle Plugin 9.4.1 dan Kotlin 2.2.10).
- JDK yang disertakan Android Studio (JBR).
- Android SDK Platform 37 (`compileSdk`/`targetSdk` 37, `minSdk` 28).
- Android Emulator atau perangkat fisik dengan Android 9 (API 28) atau lebih baru.
- Koneksi internet saat sinkronisasi Gradle pertama kali.

## Cara Menjalankan Aplikasi

1. Clone repository:
   ```bash
   git clone https://github.com/AzhisFauzan/2305101010-7B-PROJECT.git
   ```
2. Buka folder `uts/jadwal_perawatan` melalui Android Studio (**File > Open**).
3. Tunggu sinkronisasi Gradle selesai.
4. Jalankan emulator atau sambungkan perangkat Android.
5. Klik **Run 'app'**, atau lewat terminal:
   ```bash
   cd uts/jadwal_perawatan
   ./gradlew installDebug      # Windows: gradlew.bat installDebug
   ```
6. Izinkan notifikasi saat diminta agar pengingat perawatan dapat tampil.

Menjalankan unit test:

```bash
./gradlew test
```

## Screenshot Hasil Implementasi

Screenshot diambil dari aplikasi yang dijalankan pada Android Emulator dan disimpan di
folder [`uts/screenshots/`](uts/screenshots/).

| Halaman | Berkas |
| --- | --- |
| Halaman utama (Dashboard) | `uts/screenshots/halaman-utama.png` |
| Halaman kedua (Detail tanaman) | `uts/screenshots/halaman-kedua.png` |
| Hasil implementasi | `uts/screenshots/hasil-implementasi.png` |

## File APK

File APK tidak dilampirkan di repository (tidak diwajibkan pada instruksi). APK debug dapat
dibuat dengan menjalankan `./gradlew assembleDebug` di folder `uts/jadwal_perawatan`; hasilnya
berada di `app/build/outputs/apk/debug/app-debug.apk`.

## Referensi

- Dokumentasi Android Developers: https://developer.android.com
- Jetpack Compose: https://developer.android.com/jetpack/compose
- Material Design 3: https://m3.material.io
- SQLite pada Android: https://developer.android.com/training/data-storage/sqlite
