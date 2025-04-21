# Bookstore Online App

## Overview
This project is a **raw implementation** of file handling, testing, and persistence in Java, with integrated logging functionality. It serves as a foundational project to demonstrate knowledge in Java and to provide a base for continuous learning and improvement. The project is designed to evolve over time, incorporating various frameworks and technologies as it progresses.

## Current Features
- **File Persistence**: Save and load book data to and from a file in CSV format.
- **Logging**: Logs all operations (e.g., saving, loading) to a log file for better traceability.
- **Testing**: Includes unit tests for file handling and data validation using JUnit 5.
- **Simple Design**: Focused on core Java concepts like `BufferedReader`, `BufferedWriter`, and exception handling.

## Future Roadmap
This project is designed to evolve through different branches, each introducing new frameworks and technologies:
1. **JDBC**: Transition to database persistence using raw SQL.
2. **JPA/Hibernate**: Implement ORM for database interaction.
3. **Spring Framework**: Introduce Spring Boot for dependency injection, REST APIs, and more.
4. **Microservices**: Refactor the project into a microservices architecture.
5. **Cloud-Native**: Deploy the application to cloud platforms (e.g., AWS, Azure, GCP).
6. **Vaadin**: Add a modern UI for managing books.
7. **Testing Enhancements**: Expand test coverage with integration and performance tests.

## Project Goals
- **Showcase Java Knowledge**: Demonstrate proficiency in Java fundamentals and file handling.
- **Continuous Learning**: Serve as a platform for learning and applying new frameworks and technologies.
- **Version Control**: Each framework or technology upgrade will be developed in a separate branch to track progress and changes.

## How to Run
1. Clone the repository:
   ```bash
   git clone https://github.com/Ignacioscript/bookstore_online_app.git
   ```
2. Navigate to the project directory:
   ```bash
   cd bookstore_online_app
   ```
3. Build the project using Maven:
   ```bash
   mvn clean install
   ```
4. Run the application:
   ```bash
   java -jar target/bookstore-online-app.jar
   ```

## Project Structure
- `src/main/java`: Contains the main application code.
  - `org.ignacioScript.co.io`: File handling logic.
  - `org.ignacioScript.co.model`: Book model class.
  - `org.ignacioScript.co.util`: Utility classes like the logger.
- `src/test/java`: Contains unit tests for the application.
- `src/main/resources`: Stores the book data file (`books.txt`).
- `logs`: Stores the application logs (`bookstore.logs`).

## Contributing
Contributions are welcome! Feel free to fork the repository, create a branch, and submit a pull request. Each new feature or framework integration should be developed in a separate branch.

## License
This project is licensed under the MIT License. See the `LICENSE` file for details.

---

This project is a journey of learning and growth. Stay tuned for updates as it evolves into a robust, modern application!
