# 🏦 Advanced Spring Boot Banking System

A robust, enterprise-grade banking backend built with **Spring Boot 3.4.4**, featuring secure JWT authentication, multi-role access control, and a comprehensive admin dashboard with real-time analytics.

---

## 🚀 Key Features

### 🔐 Security & Auth
*   **JWT Authentication**: Stateless authentication using secure Bearer tokens.
*   **Role-Based Access Control (RBAC)**: Distinct permissions for `CUSTOMER` and `ADMIN`.
*   **Encrypted Storage**: BCrypt hashing for passwords and 4-digit transaction PINs.

### 💰 Banking Operations
*   **Account Management**: Support for Savings/Current accounts with secure PIN protection.
*   **Transactions**: Real-time Deposits, Withdrawals, and Inter-account Transfers.
*   **Audit Trail**: Detailed transaction history with sender/receiver names and status tracking.

### 📊 Admin Dashboard
*   **Global Analytics**: Monitor total bank balance, total deposits vs withdrawals, and success rates.
*   **VIP Monitoring**: View top customers by transaction volume and highest balance accounts.
*   **Flexible Tracking**: Track active users over any timeframe (10, 30, or 365 days).

---

## 🛠️ Tech Stack
*   **Framework**: Spring Boot 3.4.4
*   **Database**: MySQL 8.0
*   **ORM**: Spring Data JPA (Hibernate)
*   **Security**: Spring Security 6.x + JWT
*   **Docs**: Swagger / OpenAPI 3.0
*   **Utilities**: Lombok, MapStruct

---

## 📂 Project Structure
```text
src/main/java/org/banking/
├── config/         # Security, JWT, and OpenAPI configs
├── controller/     # REST Controllers (Auth, Account, Customer, Admin)
├── dto/            # Data Transfer Objects (Requests & Responses)
├── entity/         # JPA Entities (User, Customer, Account, Transaction)
├── repository/     # Spring Data JPA Repositories
├── service/        # Business Logic Implementations
└── security/       # JWT Filters and Token Provider
```

---

## ⚙️ Setup Instructions

1. **Database Configuration**:
   * Create a MySQL database named `springbanking`.
   * Update `src/main/resources/application.properties` with your MySQL credentials.

2. **Run the Application**:
   ```bash
   mvn spring-boot:run
   ```

3. **API Documentation**:
   * Once running, visit: `http://localhost:8080/swagger-ui.html`

---
