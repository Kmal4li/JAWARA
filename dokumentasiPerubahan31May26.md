# Engineering Log & Architectural Review
**Project:** JAWARA (Jagoan Warung Nusantara) - Point of Sales System  
**Date:** May 31, 2026  
**Author:** Muhamad Wifi Shobron Aryapasha S & Faiz Cahyakumara Rosidi 

## 1. Executive Summary
Dokumen ini merangkum perubahan arsitektural dan implementasi sistem pada proyek JAWARA (Jagoan Warung Nusantara) Fase 1 dan 2. Pekerjaan difokuskan pada transisi dari *monolithic scripts* menjadi arsitektur berbasis *Clean Architecture* dan *Model-View-Controller* (MVC). Tujuan dari restrukturisasi ini adalah untuk meningkatkan skalabilitas, maintainabilitas, serta memastikan kepatuhan terhadap prinsip SOLID dan *Object-Oriented Programming* (OOP).

## 2. File Modifikasi dan Restrukturisasi Direktori
Perubahan mendasar pada tahap ini melibatkan migrasi file *source code* dari root direktori menuju standar struktur hierarki modern berbasis *Maven*.

### 2.1. File yang Dihapus/Dimigrasikan (Root Directory)
- `Admin.java`, `Kasir.java`, `User.java`, `StockProduct`, `LaporanKeuangan.java`, `Struk.java` 
  *(File-file ini telah dihapus dari root dan ditarik kembali ke dalam namespace `app.model` dan `app.service` untuk de-coupling sistem).*

### 2.2. File Baru yang Diimplementasikan
Sistem sekarang dipecah menjadi layer-layer spesifik:

**A. Configuration & Persistence Layer**
- `pom.xml`: Regulasi *dependency management* (MySQL Connector, FlatLaf, MigLayout).
- `database.sql`: Skema *Data Definition Language* (DDL) komprehensif, mencakup *users*, *categories*, *products*, *transactions*, dan *transaction_details*.
- `src/main/java/app/config/DatabaseConfig.java`: *Singleton pattern* pembungkus koneksi JDBC.

**B. Domain Model Layer (`app.model`)**
- `User.java`, `Admin.java`, `Kasir.java`: Hierarki *inheritance* untuk *Role-Based Access Control* (RBAC).
- `Produk.java`, `CategoryProduct.java`: Entitas inventaris.
- `ItemCart.java`, `Transaksi.java`, `DetailTransaksi.java`: *Data structure* in-memory untuk memfasilitasi transaksi POS (Point of Sales).

**C. Repository Layer (`app.repository`)**
- `UserRepository.java`: Menangani abstraksi *query* data pengguna (khusus otentikasi).
- `ProdukRepository.java`: Menangani operasi CRUD inventaris dengan `PreparedStatement` untuk mitigasi ancaman *SQL Injection*.
- `TransaksiRepository.java`: Menangani komitmen transaksi berskala besar.

**D. Service Layer / Business Logic (`app.service`)**
- `AuthService.java`: Evaluasi *credential* dan validasi keamanan logika login.
- `ProdukService.java`: Enkapsulasi *business rules* terkait penetapan harga dan batas minimum stok.
- `TransaksiService.java`: Engine kalkulasi dinamis dan orkestrasi pemanggilan ke *Repository*.

**E. Controller & Presentation Layer (`app.controller` & `app.view`)**
- `LoginController.java` & `LoginView.java`: Mekanisme *entry-point* UI dan otorisasi.
- `KasirController.java` & `KasirView.java`: Terminal POS interaktif untuk registrasi item keranjang dan penyelesaian tagihan.
- `app.Main.java`: Kelas eksekutor utama (*bootstrap application*).

---

## 3. Analisis dan Penjelasan Kode (Code Rationale)

Sebagai representasi *Engineering*, keputusan desain yang saya ambil dalam merancang ulang sistem ini dilandasi oleh best-practices rekayasa perangkat lunak tingkat *Enterprise*:

### 3.1. Penerapan Clean Architecture & MVC
Desain iterasi pertama yang sebelumnya meletakkan output terminal (`System.out.println`) ke dalam kelas *Entity* melanggar *Single Responsibility Principle (SRP)*. 
Pada rilis ini, arsitektur dirombak total:
- **View** murni mengurus I/O GUI (Java Swing). Ia dirancang responsif dan bersifat "bodoh" (*dumb UI*).
- **Controller** bertindak sebagai perantara yang mendengarkan aksi dari View lalu mendelegasikan beban komputasi.
- **Service** menjalankan validasi konseptual secara ketat (misal: stok negatif ditolak).
- **Repository** mengisolasi integrasi SQL secara eksklusif.

### 3.2. Implementasi Transactional Control (ACID Compliance)
Pada komponen `TransaksiRepository.java`, eksekusi logis checkout melibatkan tiga pergerakan data massal: mencatat di tabel `transactions`, merinci `transaction_details`, dan me-mutasi kolom `stok` di `products`.
```java
conn.setAutoCommit(false); 
// Eksekusi insert transaksi
// Eksekusi insert detail via Batching
// Eksekusi pemotongan stok
conn.commit(); 
```
Pendekatan eksplisit dengan menonaktifkan mekanisme `AutoCommit` memastikan properti **Atomicity** terpenuhi secara fundamental. Apabila terdeteksi insiden (seperti kegagalan validasi *race-condition* di mana stok mendadak berkurang), blok deklarasi `catch` akan memantik `conn.rollback()`. Hal ini menjamin basis data tidak akan pernah berada pada state yang korup atau asimetris (*partial writes*).

### 3.3. Optimasi Memory Management dalam Logika Point of Sales
Dalam struktur `KasirController`, objek `ItemCart` dimanfaatkan sebagai state-buffer (memori sementara / `ArrayList`) saat keranjang belanja diakumulasi. Validasi komparasi *stok aktual versus kuantitas keranjang* dilakukan pada memori lokal sesaat sebelum di-flush ke persisten database. 
Metode ini secara radikal menekan frekuensi *I/O (Input/Output) Bound* ke database MySQL, mengkonversi latensi pemindaian barang per kasir menjadi skala *O(1)* untuk *lookup* data internal; sangat ideal untuk lingkungan *High-Throughput*.

## 4. Konklusi Pengembangan
Evolusi kode ini secara efektif mengkatalisasi JAWARA dari prototipe fungsional menjadi *framework-ready application*. Sistem ini sekarang terkondisikan kebal terhadap celah sintaks SQL (*SQL Injection*), berdaya tahan ekstrim dalam mitigasi kegagalan input operasional (*Exception Handling* dan *Transactional Rollback*), serta terdesain sedemikian riil untuk memfasilitasi integrasi dan *scaling* fitur pada fase-fase *sprint* berikutnya (Loose Coupling).
