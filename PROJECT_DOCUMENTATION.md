# Detailed Project Documentation: Banking Management System

## 1. Overview
This system is designed as a backend-as-a-service for a digital banking platform. It handles the full lifecycle of a bank customer, from registration to complex financial transactions and administrative auditing.

---

## 2. System Architecture

### Authentication Flow
1. **Registration**: Users register via `/api/v1/auth/register`. 
   - Customers provide: Name, Email, Username, Password, Phone.
   - Admins provide the same plus `role: "ADMIN"`.
2. **Persistence**: A `User` entity is created for login credentials, and a `Customer` entity is created for personal profile data.
3. **Login**: Returns a JWT token which must be included in the `Authorization: Bearer <token>` header for all other requests.

### Data Model (Entities)
*   **User**: Handles security credentials, roles (ADMIN/CUSTOMER), and account status.
*   **Customer**: Holds profile information (Name, DOB, Address, Phone).
*   **Account**: Represents a bank account (Account Number, Type, Balance, PIN).
*   **Transaction**: Records every financial movement (Type, Amount, Timestamp, Status, Audit Names).

---

## 3. API Catalog

### 🔑 Authentication (`/api/v1/auth`)
| Endpoint | Method | Description |
| :--- | :--- | :--- |
| `/register` | POST | Registers a new Customer or Admin. |
| `/login` | POST | Authenticates user and returns JWT + Customer ID. |

### 💳 Customer Profile (`/api/v1/customers`)
| Endpoint | Method | Description |
| :--- | :--- | :--- |
| `/{id}` | GET | Retrieve full profile details. |
| `/{id}` | PUT | Update Address or DOB (Name/Email/Phone are fixed). |

### 💰 Accounts (`/api/v1/accounts`)
| Endpoint | Method | Description |
| :--- | :--- | :--- |
| `/` | POST | Create a new Savings/Current account (Requires 4-digit PIN). |
| `/{accountNo}` | GET | View balance and account status. |
| `/{accountNo}/deposit` | POST | Add funds to account. |
| `/{accountNo}/withdraw`| POST | Remove funds (Requires PIN verification). |
| `/{accountNo}/transfer`| POST | Send money to another account (Requires PIN). |

### 📊 Admin Operations (`/api/v1/admin`)
| Endpoint | Method | Description |
| :--- | :--- | :--- |
| `/dashboard` | GET | Global bank analytics and rankings. |
| `/customers` | GET | List all customers in the system. |
| `/transactions` | GET | Audit every transaction in the bank. |
| `/customers/active` | GET | Filter active users in the last X days (e.g., `?days=30`). |
| `/customers/{id}` | DELETE | Remove a customer and their accounts. |

---

## 4. Business Logic Rules
1. **Transaction Integrity**: All money movements use `@Transactional` with pessimistic locking to prevent race conditions or double-spending.
2. **PIN Security**: A 4-digit PIN is required for all outgoing funds (Withdraw/Transfer).
3. **Audit Logging**: Transactions store the `senderName` and `receiverName` at the moment of the transaction to ensure history remains accurate even if a user changes their name later.
4. **Validation**: All inputs are validated using `Jakarta Validation` (e.g., `@Email`, `@NotBlank`, `@Size`).

---

## 5. Deployment Notes
*   **Java Version**: 17+
*   **Environment**: Dev profile uses H2/MySQL; Production should use a managed DB instance.
*   **JWT Secret**: Should be moved to an environment variable in production.
