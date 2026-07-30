# 📦 Inventory Management System

A modern Inventory Management System built using **Spring Boot**, **Java**, and **MySQL** to efficiently manage products, inventory, and stock operations through RESTful APIs.

---

## 🚀 Features

- ➕ Add New Products
- ✏️ Update Product Details
- ❌ Delete Products
- 📋 View All Products
- 🔍 Search Product by ID
- 📦 Stock Management
- ✅ Input Validation using Jakarta Validation
- 🌐 RESTful API Architecture
- 🗄️ MySQL Database Integration
- ⚡ Exception Handling
- 📡 API Testing with Postman

---

# 🛠️ Tech Stack

| Technology | Usage |
|------------|-------|
| Java 17 | Programming Language |
| Spring Boot | Backend Framework |
| Spring Web | REST API Development |
| Spring Data JPA | Database Operations |
| MySQL | Database |
| Maven | Dependency Management |
| Jakarta Validation | Request Validation |
| Postman | API Testing |

---

# 📂 Project Structure

```
InventoryManagementSystem
│
├── controller/
│      ProductController.java
│
├── service/
│      ProductService.java
│
├── repository/
│      ProductRepository.java
│
├── entity/
│      Product.java
│
├── dto/
│
├── exception/
│
├── config/
│
├── resources/
│      application.properties
│
└── pom.xml
```

---

# ⚙️ Installation

### Clone Repository

```bash
git clone https://github.com/yourusername/InventoryManagementSystem.git
```

### Navigate

```bash
cd InventoryManagementSystem
```

### Configure Database

Update your **application.properties**

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/inventory_db
spring.datasource.username=root
spring.datasource.password=yourpassword

spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
```

### Run Project

```bash
mvn spring-boot:run
```

Application runs at

```
http://localhost:8080
```

---

# 📡 REST APIs

| Method | Endpoint | Description |
|---------|----------|-------------|
| POST | /products | Add Product |
| GET | /products | Get All Products |
| GET | /products/{id} | Get Product By ID |
| PUT | /products/{id} | Update Product |
| DELETE | /products/{id} | Delete Product |

---

# 🗃️ Database

Example Product Table

| Field | Type |
|-------|------|
| id | Long |
| productName | String |
| category | String |
| price | Double |
| quantity | Integer |

---

# 🧪 API Testing

All REST APIs were tested using **Postman**.

Example JSON

```json
{
  "productName": "Laptop",
  "category": "Electronics",
  "price": 55000,
  "quantity": 15
}
```

---

# 📈 Future Enhancements

- User Authentication (JWT)
- Role-Based Access Control (Admin/User)
- Product Image Upload
- Inventory Dashboard
- Low Stock Notifications
- Barcode/QR Code Support
- Sales & Purchase Management
- Export Reports (PDF/Excel)

---

# 🎯 Learning Outcomes

Through this project, I gained practical experience in:

- Building RESTful APIs
- Spring Boot Architecture
- CRUD Operations
- Database Integration using JPA
- Exception Handling
- Validation
- API Testing
- Backend Project Structure
- Maven Dependency Management

---

# 👩‍💻 Author

**Kavya S**

- Java Developer
- Spring Boot Developer
- Full Stack Developer

---

## ⭐ Support

If you found this project useful, consider giving it a **⭐ Star** on GitHub.

```

## 📸 Screenshots

Add these images if available:

```
screenshots/
│
├── home.png
├── postman-get.png
├── postman-post.png
├── database.png
```

Then include:

```md
## 📸 Screenshots

### Product APIs

![API](screenshots/postman-get.png)

### Database

![Database](screenshots/database.png)
```

---

### 💡 GitHub Tips
- Replace `yourusername` with your actual GitHub username (for example, `kavya2005-hub` if that's your current username).
- Add Postman screenshots and a database screenshot to make the repository look more professional.
- Pin this repository on your GitHub profile so recruiters can find it easily.
