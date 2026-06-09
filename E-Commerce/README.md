# E-Commerce Backend

Spring Boot monolithic backend for the final project E-Commerce website.

## Tech Stack

- Java 21
- Spring Boot 4
- MySQL
- Spring Security + JWT
- Swagger / OpenAPI
- Mailtrap (OTP email verification)

## Database Setup

Create the MySQL database (if not already created):

```sql
CREATE DATABASE IF NOT EXISTS `e-commerce_db`;
```

Update credentials in `src/main/resources/application.properties` if needed.

## Run the Application

```bash
cd E-Commerce
./mvnw spring-boot:run
```

On Windows:

```bash
mvnw.cmd spring-boot:run
```

Swagger UI: http://localhost:8080/swagger-ui.html

## Default Admin Account

| Field    | Value            |
|----------|------------------|
| Email    | admin@gmail.com  |
| Password | 123456           |
| Role     | ADMIN            |

Product management (create/update/delete) is **ADMIN-only**. The User Products and Create/Edit Product frontend pages should call admin product APIs. Normal users receive **403 Forbidden**.

## Swagger Testing Flow

1. Register a user via `POST /api/auth/register`
2. Check OTP from Mailtrap inbox or application console logs
3. Verify email via `POST /api/auth/verify-email`
4. Login via `POST /api/auth/login`
5. Copy the JWT token from the response
6. Click **Authorize** in Swagger and paste: `Bearer YOUR_TOKEN_HERE`
7. Test protected APIs (cart, checkout, admin endpoints)

## Frontend fetch() Examples

### Login

```javascript
fetch("http://localhost:8080/api/auth/login", {
  method: "POST",
  headers: { "Content-Type": "application/json" },
  body: JSON.stringify({
    username: "admin",
    password: "123456"
  })
})
.then(res => res.json())
.then(data => {
  localStorage.setItem("token", data.token);
});
```

### Protected request (cart)

```javascript
const token = localStorage.getItem("token");

fetch("http://localhost:8080/api/cart", {
  method: "GET",
  headers: {
    "Authorization": "Bearer " + token
  }
})
.then(res => res.json())
.then(data => console.log(data));
```

### Get products with pagination and filters

```javascript
fetch("http://localhost:8080/api/products?page=0&size=8&sortBy=price&direction=asc")
  .then(res => res.json())
  .then(data => console.log(data));
```

### Add to cart

```javascript
fetch("http://localhost:8080/api/cart/items", {
  method: "POST",
  headers: {
    "Content-Type": "application/json",
    "Authorization": "Bearer " + token
  },
  body: JSON.stringify({ productId: 1, quantity: 2 })
});
```

## API Endpoints Summary

| Module     | Endpoint                      | Access        |
|------------|-------------------------------|---------------|
| Auth       | POST /api/auth/register       | Public        |
| Auth       | POST /api/auth/verify-email   | Public        |
| Auth       | POST /api/auth/login          | Public        |
| Products   | GET /api/products             | Public        |
| Products   | GET /api/products/{id}        | Public        |
| Products   | GET /api/products/search      | Public        |
| Products   | POST/PUT/DELETE /api/products | ADMIN         |
| Categories | GET /api/categories           | Public        |
| Categories | POST/PUT/DELETE /api/categories | ADMIN       |
| Cart       | /api/cart/**                  | USER / ADMIN  |
| Orders     | POST /api/orders/checkout     | USER / ADMIN  |
| Orders     | GET /api/orders/my            | USER / ADMIN  |
| Orders     | GET /api/orders               | ADMIN         |
| Orders     | PUT /api/orders/{id}/status   | ADMIN         |
| Contact    | POST /api/contact             | Public        |
| Contact    | GET /api/contact              | ADMIN         |
