CREATE DATABASE IF NOT EXISTS jawara_db;
USE jawara_db;

CREATE TABLE IF NOT EXISTS users (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nama VARCHAR(100) NOT NULL,
    username VARCHAR(50) UNIQUE NOT NULL,
    PASSWORD VARCHAR(255) NOT NULL,
    role ENUM('Admin', 'Kasir') NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS categories (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nama_kategori VARCHAR(100) NOT NULL,
    deskripsi TEXT
);

CREATE TABLE IF NOT EXISTS products (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nama_produk VARCHAR(150) NOT NULL,
    harga_beli DECIMAL(15, 2) NOT NULL,
    harga_jual DECIMAL(15, 2) NOT NULL,
    stok INT NOT NULL DEFAULT 0,
    barcode VARCHAR(100),
    category_id INT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (category_id) REFERENCES categories(id) ON DELETE SET NULL
);

-- Insert dummy data
INSERT IGNORE INTO users (nama, username, PASSWORD, role) VALUES 
('Admin Utama', 'admin', 'admin123', 'Admin'),
('Kasir 1', 'kasir1', 'kasir123', 'Kasir');

CREATE TABLE IF NOT EXISTS transactions (
    id INT AUTO_INCREMENT PRIMARY KEY,
    kasir_id INT NOT NULL,
    tanggal TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    total_harga DECIMAL(15, 2) NOT NULL,
    diskon DECIMAL(15, 2) DEFAULT 0,
    total_bayar DECIMAL(15, 2) NOT NULL,
    kembalian DECIMAL(15, 2) DEFAULT 0,
    payment_method VARCHAR(50) NOT NULL,
    status VARCHAR(50) NOT NULL,
    FOREIGN KEY (kasir_id) REFERENCES users(id)
);

CREATE TABLE IF NOT EXISTS transaction_details (
    id INT AUTO_INCREMENT PRIMARY KEY,
    transaction_id INT NOT NULL,
    product_id INT NOT NULL,
    qty INT NOT NULL,
    harga_satuan DECIMAL(15, 2) NOT NULL,
    subtotal DECIMAL(15, 2) NOT NULL,
    FOREIGN KEY (transaction_id) REFERENCES transactions(id) ON DELETE CASCADE,
    FOREIGN KEY (product_id) REFERENCES products(id)
);

CREATE TABLE IF NOT EXISTS payment_methods (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nama_metode VARCHAR(100) NOT NULL,
    status BOOLEAN NOT NULL DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS promos (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nama_promo VARCHAR(150) NOT NULL,
    tipe_promo ENUM('persen', 'nominal') NOT NULL,
    nilai_diskon DECIMAL(15, 2) NOT NULL,
    tanggal_mulai DATE NOT NULL,
    tanggal_akhir DATE NOT NULL,
    status BOOLEAN NOT NULL DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS vouchers (
    id INT AUTO_INCREMENT PRIMARY KEY,
    kode_voucher VARCHAR(50) UNIQUE NOT NULL,
    tipe_promo ENUM('persen', 'nominal') NOT NULL,
    nilai_diskon DECIMAL(15, 2) NOT NULL,
    tanggal_kadaluarsa DATE NOT NULL,
    status BOOLEAN NOT NULL DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);
