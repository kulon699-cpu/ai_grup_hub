# AI Group Hub — Android

Proyek Android native (Kotlin) yang membungkus aplikasi web AI Group Hub dalam WebView, dengan penyimpanan alamat server, tombol pengaturan server, dan konfigurasi keamanan dasar.

## Penting
Aplikasi Android ini membutuhkan backend AI Group Hub yang sudah di-deploy ke hosting dengan HTTPS. Android tidak menjalankan server Node.js/SQLite dari dalam APK. Gunakan folder proyek web `ai-group-hub` sebagai backend dan deploy ke layanan Node.js yang mendukung persistent storage. Pastikan database persisten.

## Membuka dan membangun APK
1. Instal Android Studio (versi terbaru) dan Android SDK Platform 35.
2. Buka folder `AIGroupHub-Android` melalui Android Studio.
3. Tunggu Gradle Sync selesai.
4. Pilih **Build > Build Bundle(s) / APK(s) > Build APK(s)**.
5. APK debug akan muncul di `app/build/outputs/apk/debug/app-debug.apk`.
6. Instal APK di perangkat Android.
7. Saat pertama dibuka, masukkan URL HTTPS backend AI Group Hub.

## Backend
Jalankan/deploy proyek backend dari paket `ai-group-hub`:
- `npm install`
- atur `JWT_SECRET` dan API key bila perlu
- `npm start`

Untuk hosting publik, gunakan HTTPS, persistent volume untuk SQLite, dan lakukan hardening keamanan. Jangan gunakan server development untuk produksi.

## Catatan
- Ini adalah proyek Android Studio siap dibangun, bukan file APK yang sudah ditandatangani.
- Login, grup, dan chat tetap menggunakan backend yang sama sehingga data tersimpan server-side.
- Tombol **Server** di bagian atas dapat mengganti alamat backend.
- Gunakan HTTPS. Aplikasi menolak alamat HTTP biasa.

## Build APK tanpa komputer sendiri (GitHub Actions)
1. Buat repository GitHub baru dan upload seluruh isi folder proyek Android ini.
2. Buka tab **Actions** di repository.
3. Pilih workflow **Build Android APK**, lalu tekan **Run workflow** (atau push ke branch `main`).
4. Setelah selesai, buka hasil run dan unduh artifact `AI-Group-Hub-debug-APK`.
5. Ekstrak artifact, lalu instal `app-debug.apk` di Android. Jika diminta, izinkan instalasi dari sumber tersebut.

Workflow ini menghasilkan APK debug untuk uji coba, bukan APK release bertanda tangan untuk Play Store.
