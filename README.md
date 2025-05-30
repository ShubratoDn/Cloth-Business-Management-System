# Cloth Business Management System

## Overview

The Cloth Business Management System is a comprehensive, full-stack Spring Boot application designed to manage all aspects of a cloth trading business. It supports inventory, sales, purchases, stakeholders, users, roles, reporting (Excel/PDF), and more. The system is highly modular, secure, and extensible, with a focus on real-world business workflows.

---

## Features
- **User Authentication & Authorization** (JWT-based, role-based access)
- **Inventory Management** (Products, Stock, Categories)
- **Sales & Purchase Management**
- **Stakeholder Management** (Suppliers, Customers, etc.)
- **Store Management**
- **Comprehensive Reporting** (Excel and PDF, with JasperReports)
- **Role & Permission Management**
- **RESTful API** (with OpenAPI/Swagger UI)
- **Caching** (Spring Cache, Redis)
- **Custom Fonts & Multilingual Support** (Bengali fonts for reports)
- **Image Management** (logos, product images, etc.)

---

## Technology Stack
- **Java 17**
- **Spring Boot 3.3.2**
- **Spring Data JPA** (MySQL)
- **Spring Security** (JWT)
- **Spring Cache** (with Redis)
- **Apache POI** (Excel report generation)
- **JasperReports** (PDF report generation)
- **ModelMapper** (DTO mapping)
- **Lombok**
- **Swagger/OpenAPI** (API documentation)
- **ZXing** (QR code generation)
- **Custom Bengali Fonts** (for PDF reports)

---

## Project Structure

```
Cloth-Business-Management-System/
├── src/
│   ├── main/
│   │   ├── java/com/cloth/business/
│   │   │   ├── controllers/      # REST controllers (API endpoints)
│   │   │   ├── services/         # Service interfaces
│   │   │   ├── servicesImple/    # Service implementations
│   │   │   ├── repositories/     # Spring Data JPA repositories
│   │   │   ├── entities/         # JPA entities (data model)
│   │   │   ├── payloads/         # DTOs and response/request objects
│   │   │   ├── DTO/              # Additional DTOs
│   │   │   ├── configurations/   # Security, JWT, Redis, etc.
│   │   │   ├── helpers/          # Utility classes
│   │   │   └── ClothBusinessManagementSystemApplication.java # Main entry point
│   │   └── resources/
│   │       ├── application.properties # Main config
│   │       ├── static/
│   │       │   ├── images/           # Logos, product images, etc.
│   │       │   └── reports/pdfTemplates/ # JasperReports templates
│   │       └── fonts/                # Custom fonts
│   └── test/
│       └── java/com/cloth/business/  # Basic test class
├── pom.xml           # Maven build file
└── README.md         # Project documentation
```

---

## Main Modules & Components

### 1. **Authentication & Security**
- JWT-based authentication (see `AuthController`, `SecurityConfig`)
- Role-based access control (custom roles, permissions)
- Passwords hashed with BCrypt
- Custom user details and security expressions

### 2. **Business Logic**
- **Controllers:** Handle API requests for products, stock, sales, purchases, users, roles, stakeholders, reports, etc.
- **Services:** Business logic for each domain (purchase, sale, stock, user, etc.)
- **Repositories:** Data access using Spring Data JPA
- **Entities:** Data model for all business objects

### 3. **Reporting**
- **Excel Reports:** Generated using Apache POI (see `ReportExcelServiceImple`)
- **PDF Reports:** Generated using JasperReports (see `ReportPDFServicesImple`)
- **Custom Templates:** JasperReports `.jrxml` files in `resources/static/reports/pdfTemplates/`
- **Fonts:** Bengali and other fonts for multilingual support
- **QR Codes:** Generated for purchase orders

### 4. **Image & File Management**
- Store and serve images for products, users, stores, stakeholders
- Logo used in reports

### 5. **Caching**
- Spring Cache abstraction
- Redis integration for performance

---

## Setup & Installation

### Prerequisites
- Java 17+
- Maven
- MySQL (or compatible DB)
- Redis (for caching)

### 1. Clone the Repository
```bash
git clone <repo-url>
cd Cloth-Business-Management-System
```

### 2. Configure the Database
Edit `src/main/resources/application.properties` with your MySQL credentials and DB name.

### 3. Install Dependencies
```bash
mvn clean install
```

### 4. Run the Application
```bash
mvn spring-boot:run
```

### 5. Access the API
- API base URL: `http://localhost:8080/api/v1/`
- Swagger UI: `http://localhost:8080/swagger-ui/index.html`

---

## API Overview

### Authentication
- `POST /api/v1/auth/register` — Register a new user
- `POST /api/v1/auth/login` — Login and receive JWT token

### Users & Roles
- `GET /api/v1/users` — List users
- `POST /api/v1/users` — Create user
- `GET /api/v1/user-roles` — List roles

### Products & Stock
- `GET /api/v1/products` — List products
- `POST /api/v1/products` — Add product
- `GET /api/v1/stock` — View stock

### Purchases & Sales
- `POST /api/v1/purchases` — Create purchase
- `GET /api/v1/purchases/{id}/{po}` — Get purchase details
- `GET /api/v1/sales` — List sales

### Stakeholders
- `GET /api/v1/stakeholders` — List stakeholders

### Reporting
- `POST /api/v1/reports/profitability` — Download profitability report (Excel)
- `GET /api/v1/purchases/generate-pdf/{id}/{po}` — Download purchase order report (PDF)

---

## Reporting Details

### Excel Reports
- Generated using Apache POI
- Profitability and transaction reports
- Download via `/api/v1/reports/profitability`

### PDF Reports
- Generated using JasperReports
- Custom templates in `resources/static/reports/pdfTemplates/`
- Bengali font support
- QR code and logo included
- Download via `/api/v1/purchases/generate-pdf/{id}/{po}`

---

## Security & Authentication
- JWT token required for most endpoints
- Role-based access (e.g., `ROLE_ADMIN`, `ROLE_PURCHASE_CREATE`, etc.)
- BCrypt password hashing
- Custom security expressions and method-level security

---

## Customization & Extension
- **Add new reports:** Create new JasperReports templates and corresponding service methods
- **Add new roles/permissions:** Update `UserRolesList` and related logic
- **Add new entities:** Create new JPA entities, repositories, services, and controllers
- **Change branding:** Replace logo in `resources/static/images/logo-single.png`
- **Multilingual reports:** Add new fonts to `resources/fonts/` and update report templates

---

## Fonts & Images
- Custom Bengali fonts included for PDF reports
- Images for products, users, stores, stakeholders
- Logo used in all reports

---

## Testing
- Basic Spring Boot test in `src/test/java/com/cloth/business/ClothBusinessManagementSystemApplicationTests.java`
- Extend with more unit and integration tests as needed

---

## Dependencies
See `pom.xml` for a full list. Highlights:
- Spring Boot, Spring Data JPA, Spring Security, Redis, Apache POI, JasperReports, ZXing, ModelMapper, Lombok, Swagger/OpenAPI, MySQL Connector, JWT, custom fonts

---

## Notes
- Ensure MySQL and Redis are running before starting the application
- For Bengali/multilingual reports, ensure font JARs are present in `lib/`
- API is stateless (no session), uses JWT for authentication
- Swagger UI is enabled for easy API exploration

---

## License
This project is for educational purpose only. If you want to use the project for business purpose, please see the contact information below.

---

## Contact Information
- Email: <a href="mailto:shubratodn44985@gmail.com">shubratodn44985@gmail.com</a>
- Phone: <a href="tel:+8801759458961">+8801759458961</a>