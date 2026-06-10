# Local Frontend

This folder contains the existing e-commerce UI connected to the local Spring Boot backend.

## Run

1. Start MySQL.
2. Start the backend from `E-Commerce`:

   ```powershell
   .\mvnw.cmd spring-boot:run
   ```

3. Serve this folder on port `5500`:

   ```powershell
   python -m http.server 5500 --bind 127.0.0.1
   ```

4. Open:

   ```text
   http://127.0.0.1:5500/home/home.html
   ```

## Local Admin

```text
Username: admin
Password: 123456
```

The frontend uses `http://localhost:8080/api` through the shared `api.js` helper.
Cart, checkout, orders, products, authentication, profile data, and contact messages are stored by the backend.
