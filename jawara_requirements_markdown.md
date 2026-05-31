# JAWARA (Jagoan Warung Nusantara)
## AI-Agent Ready Software Requirements & Development Blueprint

---

# 1. Project Overview

## Project Name
JAWARA (Jagoan Warung Nusantara)

## Project Type
Desktop-based Point of Sales (POS) and Inventory Management System for traditional retail stores / UMKM.

## Main Technology Stack
- Language: Java
- UI Framework: Java Swing
- Layout System: MigLayout / AbsoluteLayout / Anti-Gravity Layout Style
- Database: MySQL
- ORM / Database Access: JDBC
- Architecture: Clean Architecture + MVC
- Build Tool: Maven / Gradle
- Reporting: JasperReports (optional)
- Authentication: Session-based login

---

# 2. Main Goal

Membangun aplikasi desktop POS modern untuk warung kelontong yang:
- Mudah digunakan
- Cepat
- Minim human error
- Mendukung multi-role
- Memiliki sistem inventaris real-time
- Mendukung promo dan voucher
- Memiliki laporan keuangan dan stok
- Siap dikembangkan lebih lanjut

---

# 3. AI AGENT MASTER INSTRUCTION

## IMPORTANT
AI Agent HARUS:

1. Menggunakan prinsip OOP secara penuh
2. Menggunakan Clean Code
3. Menggunakan SOLID Principle
4. Menggunakan Java naming convention
5. Memisahkan folder berdasarkan tanggung jawab
6. Menggunakan MVC architecture
7. Semua logic bisnis harus dipisahkan dari UI
8. Tidak boleh menaruh SQL langsung di UI
9. Semua query harus menggunakan Repository/DAO layer
10. Semua class wajib memiliki:
   - constructor
   - getter/setter
   - validation
   - error handling
11. Semua transaksi database wajib menggunakan try-catch
12. Semua input user wajib divalidasi
13. Semua form harus reusable
14. Semua button harus memiliki loading/disabled state
15. Semua entity harus memiliki ID unik
16. Semua operasi CRUD wajib memiliki:
   - Create
   - Read
   - Update
   - Delete
17. Semua logika perhitungan harus berada di Service Layer
18. Gunakan PreparedStatement untuk menghindari SQL Injection
19. Gunakan hashing password
20. Jangan hardcode configuration

---

# 4. Recommended Project Structure

```plaintext
JAWARA/
│
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   ├── app/
│   │   │   │   ├── Main.java
│   │   │   │
│   │   │   ├── config/
│   │   │   │   ├── DatabaseConfig.java
│   │   │   │   ├── AppConfig.java
│   │   │   │
│   │   │   ├── model/
│   │   │   │   ├── User.java
│   │   │   │   ├── Admin.java
│   │   │   │   ├── Kasir.java
│   │   │   │   ├── Produk.java
│   │   │   │   ├── CategoryProduct.java
│   │   │   │   ├── StockProduct.java
│   │   │   │   ├── Transaksi.java
│   │   │   │   ├── DetailTransaksi.java
│   │   │   │   ├── ItemCart.java
│   │   │   │   ├── MetodePembayaran.java
│   │   │   │   ├── Promo.java
│   │   │   │   ├── Voucher.java
│   │   │   │   ├── Struk.java
│   │   │   │   └── LaporanKeuangan.java
│   │   │   │
│   │   │   ├── repository/
│   │   │   │   ├── UserRepository.java
│   │   │   │   ├── ProdukRepository.java
│   │   │   │   ├── TransaksiRepository.java
│   │   │   │   └── ...
│   │   │   │
│   │   │   ├── service/
│   │   │   │   ├── AuthService.java
│   │   │   │   ├── ProdukService.java
│   │   │   │   ├── TransaksiService.java
│   │   │   │   ├── PromoService.java
│   │   │   │   └── ReportService.java
│   │   │   │
│   │   │   ├── controller/
│   │   │   │   ├── LoginController.java
│   │   │   │   ├── DashboardController.java
│   │   │   │   ├── ProdukController.java
│   │   │   │   ├── TransaksiController.java
│   │   │   │   └── ...
│   │   │   │
│   │   │   ├── view/
│   │   │   │   ├── auth/
│   │   │   │   ├── admin/
│   │   │   │   ├── kasir/
│   │   │   │   ├── components/
│   │   │   │   └── dialogs/
│   │   │   │
│   │   │   ├── utils/
│   │   │   │   ├── Validator.java
│   │   │   │   ├── CurrencyFormatter.java
│   │   │   │   ├── DateUtil.java
│   │   │   │   └── SessionManager.java
│   │   │   │
│   │   │   └── exception/
│   │   │       ├── ValidationException.java
│   │   │       └── DatabaseException.java
│   │   │
│   │   └── resources/
│   │       ├── assets/
│   │       ├── icons/
│   │       └── sql/
│   │
│   └── test/
│
├── pom.xml
├── README.md
└── database.sql
```

---

# 5. Functional Requirements

# 5.1 Authentication Module

## Features
- Login
- Logout
- Session Management
- Role-based Access

## Roles
### Admin
Can:
- Manage products
- Manage categories
- Manage promo
- Manage voucher
- View reports
- Manage stock
- View transaction history

### Kasir
Can:
- Process transactions
- Add cart items
- Print receipt
- Apply promo/voucher
- View products

## Validation Rules
- Username cannot be empty
- Password cannot be empty
- Password minimum 8 characters
- Failed login must show clear message

---

# 5.2 Product Management Module

## Features
- Add product
- Edit product
- Delete product
- Search product
- Filter product
- Manage category
- Stock update

## Product Attributes
```java
idProduk
namaProduk
hargaBeli
hargaJual
stok
kategori
barcode
createdAt
updatedAt
```

## Validation
- Product name required
- Selling price cannot be negative
- Stock cannot be negative
- Duplicate product not allowed

---

# 5.3 Cart System

## Features
- Add item to cart
- Remove item
- Update quantity
- Auto subtotal
- Auto total
- Real-time calculation

## Rules
- Quantity cannot exceed stock
- Quantity minimum 1
- Cart auto recalculates total

---

# 5.4 Transaction Module

## Features
- Create transaction
- Payment processing
- Apply discount
- Apply voucher
- Calculate change
- Save transaction
- Print receipt

## Flow
```plaintext
Select Product
→ Add To Cart
→ Calculate Total
→ Apply Promo/Voucher
→ Select Payment Method
→ Input Customer Payment
→ Calculate Change
→ Save Transaction
→ Update Stock
→ Print Receipt
```

## Validation
- Cart cannot be empty
- Payment must be sufficient
- Voucher must be valid
- Promo must be active

---

# 5.5 Promo System

## Features
- Create promo
- Activate/deactivate promo
- Percentage discount
- Fixed amount discount
- Date-based promo

## Rules
- Promo cannot overlap invalidly
- Discount cannot exceed total
- Expired promo auto disabled

---

# 5.6 Voucher System

## Features
- Create voucher
- Validate voucher
- One-time usage
- Expiration system

## Rules
- Voucher unique
- Voucher cannot be reused
- Voucher must not expire

---

# 5.7 Reporting Module

## Reports
### Financial Report
- Total revenue
- Total transactions
- Daily report
- Monthly report

### Stock Report
- Stock in
- Stock out
- Low stock warning

### Transaction Report
- Transaction history
- Filter by date
- Search transaction

---

# 6. Non-Functional Requirements

## Performance
- Login < 2 seconds
- Transaction process < 1 second
- Search product realtime

## Security
- Password hashing
- Session timeout
- Role-based authorization
- SQL Injection protection

## Reliability
- Auto rollback transaction
- Error logging
- Exception handling

## Maintainability
- Modular architecture
- Reusable components
- Clear naming convention

## Scalability
- Ready for future API integration
- Ready for multi-store support
- Ready for cloud migration

---

# 7. Database Design Requirements

## Main Tables

### users
```sql
id
nama
username
password
role
created_at
updated_at
```

### products
```sql
id
nama_produk
harga_beli
harga_jual
stok
category_id
created_at
updated_at
```

### categories
```sql
id
nama_kategori
deskripsi
```

### transactions
```sql
id
kasir_id
tanggal
total_harga
diskon
total_bayar
kembalian
payment_method
status
```

### transaction_details
```sql
id
transaction_id
product_id
qty
harga_satuan
subtotal
```

### vouchers
```sql
id
kode_voucher
nilai_diskon
expired_date
status
```

### promos
```sql
id
nama_promo
tipe_promo
nilai_diskon
start_date
end_date
status
```

---

# 8. Recommended UI Design

# YES — Swing + Anti-Gravity Style CAN BE USED

## Recommendation
Gunakan:
- Java Swing
- FlatLaf untuk modern UI
- MigLayout untuk responsive layout
- Rounded Panel custom
- Custom Button UI
- Shadow Panel
- Card-based dashboard

## Kenapa Bagus?
Karena:
- Swing stabil
- Ringan
- Cocok untuk desktop POS
- Mudah dibuat AI agent
- Banyak dokumentasi
- Mudah custom design

---

# 9. Recommended UI Theme

## Theme Style
Modern Minimalist POS Dashboard

## Colors
```plaintext
Primary   : #4F46E5
Secondary : #7C3AED
Background: #F5F7FA
Success   : #22C55E
Danger    : #EF4444
Warning   : #F59E0B
Text      : #111827
```

## UI Components
- Sidebar Navigation
- Top Navbar
- Dashboard Cards
- Search Bar
- Product Table
- Cart Panel
- Transaction Panel
- Popup Dialog
- Toast Notification

---

# 10. Suggested Screens

## Authentication
- Login Screen

## Admin
- Dashboard
- Product Management
- Category Management
- Promo Management
- Voucher Management
- Stock Management
- Reports
- User Management

## Kasir
- POS Screen
- Cart Screen
- Payment Dialog
- Receipt Preview
- Transaction History

---

# 11. Business Rules

## Product Rules
- Product stock cannot go below 0
- Deleted products should be soft delete

## Transaction Rules
- Every successful transaction reduces stock
- Failed transaction should rollback

## Promo Rules
- Promo active only within date range

## Voucher Rules
- One voucher only for one transaction
- Voucher only usable once

---

# 12. Error Handling Standards

## Error Types
- Validation Error
- Database Error
- Authentication Error
- Business Logic Error

## UI Error Behavior
- Show dialog message
- Highlight invalid field
- Prevent crash

---

# 13. Logging System

## Log Important Events
- Login
- Logout
- Transaction success
- Failed payment
- Product update
- Stock changes

---

# 14. AI Agent Coding Rules

## STRICT RULES

AI Agent MUST:

1. Create code gradually
2. Generate model first
3. Generate repository second
4. Generate service third
5. Generate controller fourth
6. Generate UI last
7. Generate database.sql first before coding
8. Ensure all classes compile
9. Avoid duplicate methods
10. Avoid circular dependency
11. Ensure proper inheritance
12. Ensure encapsulation
13. Ensure polymorphism usage where needed
14. Use interfaces when possible
15. Use enums for status values
16. Create reusable dialog component
17. Create reusable table component
18. Use constants for repeated strings
19. Create utility formatter class
20. Use transaction management in database operation

---

# 15. Suggested Future Features

## Version 2 Features
- Barcode Scanner
- QRIS Payment
- Customer Membership
- Loyalty Points
- Supplier Management
- Multi Branch
- Export PDF
- Export Excel
- Dark Mode
- Analytics Dashboard
- AI Sales Prediction
- Telegram Notification
- WhatsApp Integration

---

# 16. Suggested Development Flow

## Phase 1
- Database setup
- Authentication
- Product CRUD

## Phase 2
- Cart system
- Transaction system
- Payment system

## Phase 3
- Promo and voucher
- Reporting
- Receipt printing

## Phase 4
- UI polishing
- Optimization
- Testing

---

# 17. Testing Requirements

## Unit Testing
- Service layer
- Validation logic
- Calculation logic

## Integration Testing
- Database connection
- Transaction flow

## UI Testing
- Button actions
- Form validation
- Navigation flow

---

# 18. Deployment Requirements

## Packaging
- Build executable JAR
- Include database setup script
- Include README

## Installation
- Java 17+
- MySQL Server
- JDBC Driver

---

# 19. Final AI Agent Instruction

AI Agent should behave as:
- Senior Java Software Engineer
- Java Swing UI Designer
- Backend Architect
- Database Engineer
- QA Engineer
- System Analyst

AI Agent must:
- Think before generating code
- Generate scalable architecture
- Prioritize maintainability
- Avoid spaghetti code
- Keep code modular
- Keep UI clean and modern
- Follow MVC strictly
- Ensure code can run directly

---

# 20. Conclusion

JAWARA adalah sistem POS modern berbasis desktop yang dirancang khusus untuk UMKM dan warung tradisional. Sistem ini menggunakan Java Swing, MySQL, dan arsitektur OOP modern untuk menciptakan aplikasi yang scalable, maintainable, dan siap dikembangkan lebih lanjut.

Dengan dokumen requirement ini, AI Agent diharapkan dapat:
- memahami struktur sistem
- memahami business logic
- memahami arsitektur
- memahami relasi antar class
- memahami aturan pengembangan
- menghasilkan code yang rapi dan scalable
- mempercepat development secara signifikan

