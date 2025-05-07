# 📚 Bookstore Management System
A comprehensive **Bookstore Management System** built using **Java** to manage books, authors, orders, and customers. This project is designed to simulate database operations using **plain IO** and CSV files as persistent storage. It follows a layered architecture to ensure clean modular design and scalability.
The system includes operations for managing books, customers, orders, and item orders. It validates user inputs, supports sorting and searching, and logs application events for detailed tracking.
## 🚀 Project Overview
The **Bookstore Management System** offers a fully functional console-based application that allows a small bookstore to operate efficiently. This system implements the core CRUD operations and validations while providing various features like data seeding, logging, and sorting.
## ✨ Current Features
### 📖 Books Management
- Add, update, delete, and view books.
- Validate input data such as ISBN, price, stock, and publication date.
- Seed books with pre-defined data for demonstration purposes (via ). `BookSeeder`
- Store books persistently using **CSV files**.

### 🧍 Customers Management
- Manage customers (add, update, delete, and view).
- Validate input data like names, emails, and phone numbers.
- Fetch customers by their **ID** or **email address**.

### 🚀 Orders Management
- Manage customer orders, including creating and viewing orders.
- Track **order status** (e.g., Pending, Processing, Shipped).
- Calculate order totals and validate loyalty points.

### 📦 Item Orders
- Track the relationship between books and orders through item orders.
- Log item orders for auditing purposes.

### 📈 Sorting and Searching
- Sort books by title using **bubble sort** and **quick sort** algorithms.
- Search books or customers using keywords.

### 🔐 Validations
The application includes comprehensive validation rules:
- Validate inputs such as book details, customer details, and order totals.
- Checks constraints such as lengths, valid formats, numerical ranges, and logic correctness.

### 🧹 Logs
- Detailed **log files** for tracking application events:
    - (for general application logs). `application.log`
    - (for error tracking). `error.log`

- Example: `"2025-05-03 - CustomerController - created: Customer details..."`.

### 🔄 File-based Data Simulation
- Simulate database operations using IO and CSV files for persistent storage. For example:
    - **`books.csv`**: Stores book information.
    - **`customers.csv`**: Stores customer data.

- Handles file operations through **specialized file manager classes**.

## 🛠️ Planned Future Enhancements
This project will evolve into a **production-ready system** with the following upgrades:
1. **Database Integration**:
    - Replace CSV file storage with **MySQL** or **PostgreSQL**.
    - Use **JDBC** to handle database queries.

2. **Frameworks**:
    - Refactor to use **Spring Boot** for dependency injection, RESTful services, and advanced configuration.
    - Migrate to **Spring Data** for seamless database interaction.

3. **Microservices**:
    - Convert into a **microservices architecture** for scalable system design.
    - Create services for managing books, authors, orders, and customers independently.

4. **Deployment**:
    - Host a RESTful API server.
    - Deploy the application on **AWS**, **Azure**, or **Heroku** for live demonstration.

## 🏗️ Project Structure
Here’s the folder structure and a high-level overview of each component/layer:
``` 
📂 src/
├── 📂 model/ ............ POJOs and data structures (e.g., Book, Customer, Order, ItemOrder, etc.).
├── 📂 service/ .......... Core business logic for books, orders, customers, etc.
├── 📂 controller/ ....... Controllers for application use cases (e.g., BookController, OrderController).
├── 📂 io/ ............... File manager classes for handling CSV files.
├── 📂 util/ ............. Utility classes for string management, numeric operations, and logging.
├── 📂 seeder/ ........... Classes for seeding test data (e.g., BookSeeder).
├── 📂 validation/ ....... Validators to enforce input correctness and constraints.
└── 📂 test/ ............. Unit tests for the application (JUnit 5 framework).
```
### Key Classes
Here are some important classes in the project:
- **`Book`**: Represents books in the system.
- **`Customer`**: Represents customers.
- **`Order`**: Tracks orders with details like customer, date, and order total.
- **`BookFileManager`**: Handles loading, saving, and fetching book data from . `books.csv`
- **`OrderController`**: Implements order-related actions like creating orders.
- **`BookSeeder`**: Pre-loads sample data for books.

## 💻 How to Run the System
### **Requirements**
- Java 17+ (or a compatible version).
- Git (to clone the repository).

### **Steps**
1. Clone this repository:
``` bash
   git clone https://github.com/your-username/bookstore-management-system.git
   cd bookstore-management-system/
```
1. Compile the code:
``` bash
   javac -d bin src/**/*.java
```
1. Run the main menu controller:
``` bash
   java -cp bin org.ignacioScript.co.controller.MenuController
```
1. The console displays a menu for interacting with different system components. Example:
``` 
   Welcome to the Library Management System
   1. Manage Authors And Books
   2. Manage Customers
   3. Manage Orders
   4. Exit and close the Library System
   Enter your choice:
```
1. Follow the prompts for managing books, customers, and orders.

## 📋 Example Files
### **Books Data ()`books.csv`**
``` csv
1,978-0439708180,Harry Potter,A magical journey,Bloomsbury,1997-06-26,25.99,100
2,978-0261103573,The Hobbit,A hobbit's journey,Allen & Unwin,1937-09-21,15.99,50
3,978-0141439518,Pride and Prejudice,A classic romance,Penguin,1813-01-28,10.99,200
```
### **Customers Data ()`customers.csv`**
``` csv
1,John,Doe,john.doe@example.com,1234567890,123 Main St,2025-05-07,10
2,Jane,Smith,jane.smith@example.com,9876543210,456 Elm St,2025-05-07,10
3,Alice,Johnson,alice.johnson@example.com,5551234567,789 Oak St,2025-05-07,10
```
## 🧪 Testing
The application includes unit tests built with **JUnit 5** to validate critical functionality:
1. Input validation (e.g., ISBN, price, and name formats).
2. File operations (e.g., loading and saving data).
3. Business logic (e.g., CRUD operations for books and orders).

### Run the tests:
``` bash
mvn test
```
## 🔍 Example Logs
### **Application Logs ()`application.log`**
``` log
2025-05-03 08:51:02 - BookController - Creating a new book
2025-05-03 08:54:18 - BookController - book created: Book{bookId=6, isbn='A187637894', bookTitle='Step Up Now'...}
2025-05-03 17:56:44 - ItemOrderController - Item Order created: ItemOrder{itemOrderId=1, book=1, order=50...}
```
### **Error Logs ()`error.log`**
``` log
2025-05-01 10:51:29 - AuthorService - Error finding author: Author with ID: 5 does not exist
2025-05-03 15:56:33 - BookFileManager - Error reading file for getBYId: books.csv (File not found)
```
## 🔗 How to Contribute
Contributions are welcome!
1. Fork the repository.
2. Create a new branch (`git checkout -b feature-name`).
3. Make your changes and commit (`git commit -m "Your message"`).
4. Push to your fork (`git push origin feature-name`).
5. Submit a pull request!

## 📜 License
This project is licensed under the **MIT License**. See the [LICENSE](LICENSE) file for more details.
Feel free to let me know if you want further customization, and I'll make the necessary adjustments! 😊
