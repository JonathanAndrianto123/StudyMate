# 📚 StudyMate

StudyMate adalah aplikasi Android untuk membantu pengguna **mencatat waktu belajar, memantau progress, dan meningkatkan fokus belajar** per mata kuliah atau skill.

Aplikasi ini dirancang untuk mahasiswa, pelajar, maupun self-learner yang ingin konsisten belajar, memiliki data yang terukur, dan meminimalkan distraksi.

---

## 👨‍💻 Authors

| Nama                               | NIM      |
| ---------------------------------- | -------- |
| Rheinata Tamaiska Gracia           | 71220879 |
| Fransiska Endah Kusuma Wardani     | 71220864 |
| Jonathan Satriani Gracio Andrianto | 71230978 |

---

## ✨ Fitur Utama

* ⏱️ **Timer Belajar** — Mulai, pause, dan stop sesi belajar yang terhubung ke materi tertentu
* 📘 **Manajemen Materi (CRUD)** — Tambah, edit, dan hapus materi beserta target jam belajar
* 📊 **Statistik & Progress** — Total waktu belajar dan persentase pencapaian target per materi
* 🕘 **Riwayat Sesi** — Lihat histori sesi belajar berdasarkan tanggal dan materi
* 🔔 **Reminder Belajar** — Notifikasi harian/mingguan agar tetap konsisten
* 🔄 **Flip-to-Focus (Sensor Gyroscope)** — Timer otomatis pause jika HP dibalik (face-up)
* 📍 **Study Location History** — Simpan lokasi setiap sesi belajar untuk melihat spot belajar favorit
* 📤 **Export Data** — Ringkasan belajar bulanan dalam format CSV / PDF (opsional)

---

## 👤 User Persona

* **Mahasiswa Informatika** — Tracking jam belajar kuliah & bootcamp
* **Pelajar SMA** — Membangun kebiasaan belajar mandiri
* **Self-Learner / Job Seeker** — Mengukur progres belajar skill online

---

## 🧩 Functional Requirements (Ringkas)

* Authentication (Google Sign-In – opsional)
* CRUD Materi
* Timer Belajar & Simpan Sesi
* Statistik & Progress
* Riwayat Sesi
* Reminder / Notifikasi
* Flip-to-Focus (Gyroscope)
* Location History

---

## ⚙️ Technology Stack

* **Language**: Kotlin
* **UI**: Jetpack Compose
* **Architecture**: MVVM (Model–View–ViewModel)
* **Database**: Room Database
* **Background Task**: WorkManager & Foreground Service
* **Sensor**: SensorManager (Gyroscope / Accelerometer)
* **Location**: FusedLocationProvider (Google Play Services)
* **Auth**: Firebase Authentication (Google Sign-In)
* **Charts**: MPAndroidChart / Compose Charts
* **Dependency Injection**: Hilt
* **Testing**: JUnit, Espresso, Mockito
* **Build System**: Gradle (Kotlin DSL)

---

## 🗂️ Project Structure (MVVM)

```
com.app.studymate
 ├─ ui
 │   ├─ main
 │   ├─ materi
 │   ├─ session
 │   └─ stats
 ├─ data
 │   ├─ local
 │   │   ├─ db
 │   │   ├─ dao
 │   │   └─ entities
 │   └─ repository
 ├─ domain
 │   ├─ model
 │   └─ usecase
 ├─ service
 │   ├─ TimerForegroundService
 │   └─ SensorManagerHelper
 ├─ util
 └─ di
```

---

## 🧪 Testing Plan

* **Unit Testing**: Logika timer, progress, dan CRUD Room Database
* **UI Testing**: Navigasi layar, tombol Start/Pause/Stop
* **Sensor & Location Testing**: Flip-to-focus & penyimpanan lokasi
* **Integration Testing**: Penyimpanan sesi, statistik, dan login Google

---

## 🔐 Privacy & Security

* Data disimpan secara lokal (internal storage)
* Lokasi hanya direkam saat sesi dihentikan
* Tidak ada pengiriman data ke server eksternal tanpa izin pengguna
* Autentikasi menggunakan OAuth 2.0 (Google Sign-In)

---

## 📌 Status Project

🚧 **In Development / Academic Project**
Dikembangkan sebagai tugas mata kuliah **Pemrograman Android**.

---

> *"Belajar bukan tentang seberapa lama, tapi seberapa konsisten."*
