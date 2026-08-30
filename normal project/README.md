# Starter E-commerce

This is a beginner-friendly Spring Boot, MySQL, JWT and vanilla JavaScript e-commerce project.

## Requirements

- Java 21 or newer
- MySQL 8

## Run

Create a MySQL user that can create databases, then set environment variables in PowerShell:

```powershell
$env:DB_URL="jdbc:mysql://localhost:3306/ecommerce_db?createDatabaseIfNotExist=true&useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC"
$env:DB_USERNAME="root"
$env:DB_PASSWORD="your-mysql-password"
$env:JWT_SECRET="replace-with-a-long-random-secret-at-least-32-bytes"
.\mvnw.cmd spring-boot:run
```

Open <http://localhost:8080>. The home route forwards to `home.html`.

Register a `BUYER` account to shop or a `SELLER` account to sell and shop. Uploaded images are stored under `uploads/images` and are not committed.

## Main flows

- Public product browsing, search and detail
- JWT registration/login/profile
- Seller-owned create, edit, delete and sales list
- Database-backed cart, checkout and customer order history
- Persisted contact messages

Card fields are display-only. Card numbers, expiry dates and CVC values are never sent to or stored by the server.
