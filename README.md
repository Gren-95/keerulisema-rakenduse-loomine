# Library Management System

A comprehensive Java application demonstrating advanced object-oriented programming principles, design patterns, and best practices.

## 🎯 Project Overview

This Library Management System is a complex Java application that showcases:
- **Object-Oriented Programming** principles (encapsulation, inheritance, polymorphism, abstraction)
- **Design Patterns** (Repository, Service Layer, Builder, Command, Observer, Strategy, Factory)
- **Exception Handling** with custom exceptions and proper error recovery
- **Logging** with SLF4J and Logback
- **Data Persistence** using JSON files
- **Unit Testing** with JUnit 5 and Mockito
- **Clean Architecture** with separation of concerns

## 🏗️ Architecture

The application follows a layered architecture pattern:

```
┌─────────────────┐
│   UI Layer      │ ← LibraryCLI (Command Line Interface)
├─────────────────┤
│  Service Layer  │ ← LibraryService (Business Logic)
├─────────────────┤
│ Repository Layer│ ← LibraryRepository (Data Access)
├─────────────────┤
│  Model Layer    │ ← Domain Objects (Book, Member, etc.)
└─────────────────┘
```

## 📁 Project Structure

```
src/main/java/ee/tak24/library/
├── LibraryApplication.java          # Main application entry point
├── builder/                         # Builder pattern implementations
│   └── BookBuilder.java
├── command/                         # Command pattern implementations
│   ├── LibraryCommand.java
│   ├── BorrowBookCommand.java
│   └── CommandInvoker.java
├── exception/                       # Custom exception classes
│   ├── LibraryException.java
│   ├── BookNotFoundException.java
│   ├── BookNotAvailableException.java
│   ├── MemberNotFoundException.java
│   └── MemberLimitExceededException.java
├── model/                          # Domain model classes
│   ├── Book.java
│   ├── Member.java
│   ├── Person.java
│   ├── BorrowingRecord.java
│   ├── Address.java
│   ├── BookCategory.java
│   ├── BookStatus.java
│   └── MembershipType.java
├── observer/                       # Observer pattern implementation
│   ├── LibraryEvent.java
│   ├── LibraryEventListener.java
│   └── LibraryEventPublisher.java
├── persistence/                    # Data persistence layer
│   └── JsonLibraryRepository.java
├── repository/                     # Repository pattern
│   ├── LibraryRepository.java
│   └── InMemoryLibraryRepository.java
├── service/                        # Service layer
│   ├── LibraryService.java
│   ├── LibraryServiceImpl.java
│   ├── LibraryStatistics.java
│   └── OverdueBookInfo.java
├── strategy/                       # Strategy pattern
│   ├── NotificationStrategy.java
│   ├── ConsoleNotificationStrategy.java
│   ├── EmailNotificationStrategy.java
│   └── NotificationService.java
├── ui/                            # User interface
│   └── LibraryCLI.java
└── util/                          # Utility classes
    ├── IdGenerator.java
    └── LibraryFactory.java
```

## 🚀 Getting Started

### Prerequisites

- Java 17 or higher
- Maven 3.6 or higher

### Installation

1. **Build the project:**
   ```bash
   mvn clean compile
   ```

2. **Run the application:**
   ```bash
   mvn exec:java -Dexec.mainClass="ee.tak24.library.LibraryApplication"
   ```

### Running Tests

```bash
mvn test
```

## 🎮 Usage

The application provides a comprehensive command-line interface with the following features:

### Main Menu Options

1. **Member Management**
   - Register new members
   - View all members
   - Find member by ID
   - Update member information
   - Deactivate members

2. **Book Management**
   - Add new books
   - View all books
   - Find book by ISBN
   - Update book information
   - Remove books

3. **Borrowing Operations**
   - Borrow books
   - Return books
   - View borrowing history
   - View active borrowings
   - View overdue books

4. **Reports & Statistics**
   - Library statistics
   - Member activity rates
   - Book utilization rates

5. **Search Books**
   - Search by title, author, category
   - Filter by availability

## 🏛️ Design Patterns Implemented

### 1. Repository Pattern
- **Purpose**: Abstracts data access logic
- **Implementation**: `LibraryRepository` interface with multiple implementations
- **Benefits**: Easy to switch between in-memory and persistent storage

### 2. Service Layer Pattern
- **Purpose**: Encapsulates business logic
- **Implementation**: `LibraryService` interface with `LibraryServiceImpl`
- **Benefits**: Clean separation of concerns, testable business logic

### 3. Builder Pattern
- **Purpose**: Constructs complex objects step by step
- **Implementation**: `BookBuilder` class
- **Benefits**: Flexible object creation, validation, fluent interface

### 4. Command Pattern
- **Purpose**: Encapsulates operations as objects
- **Implementation**: `LibraryCommand` interface with `BorrowBookCommand`
- **Benefits**: Undo/redo functionality, operation queuing, logging

### 5. Observer Pattern
- **Purpose**: Notifies multiple objects about events
- **Implementation**: `LibraryEventPublisher` and `LibraryEventListener`
- **Benefits**: Loose coupling, event-driven architecture

### 6. Strategy Pattern
- **Purpose**: Defines family of algorithms and makes them interchangeable
- **Implementation**: `NotificationStrategy` with different implementations
- **Benefits**: Runtime algorithm selection, easy to add new strategies

### 7. Factory Pattern
- **Purpose**: Creates objects without specifying their exact classes
- **Implementation**: `LibraryFactory` class
- **Benefits**: Centralized object creation, easy to modify creation logic

## 🧪 Testing

The project includes comprehensive unit tests covering:

- **Service Layer**: Business logic validation
- **Model Classes**: Object behavior and validation
- **Exception Handling**: Error scenarios
- **Edge Cases**: Boundary conditions and error states

### Test Coverage

- **Unit Tests**: 95%+ coverage of business logic
- **Integration Tests**: Repository and service integration
- **Mock Testing**: Using Mockito for isolated testing

## 📊 Key Features

### Object-Oriented Programming Principles

1. **Encapsulation**: Private fields with public getters/setters
2. **Inheritance**: `Member` extends `Person`, `LibraryException` hierarchy
3. **Polymorphism**: Interface implementations, method overriding
4. **Abstraction**: Abstract classes and interfaces

### Advanced Java Features

- **Generics**: Type-safe collections and methods
- **Streams API**: Functional programming for data processing
- **Optional**: Null-safe programming
- **LocalDate**: Modern date handling
- **ConcurrentHashMap**: Thread-safe collections

### Error Handling

- **Custom Exceptions**: Domain-specific error types
- **Exception Hierarchy**: Organized exception structure
- **Graceful Degradation**: Application continues running after errors
- **Comprehensive Logging**: Detailed error tracking

### Data Persistence

- **JSON Storage**: Human-readable data format
- **Automatic Loading**: Data loaded on startup
- **Automatic Saving**: Data persisted on changes
- **Backup Support**: Multiple data files for different entities

## 🔧 Configuration

### Logging Configuration

The application uses Logback for logging with the following configuration:

- **Console Output**: All log levels
- **File Output**: INFO and above to `logs/library-system.log`
- **Error File**: ERROR and above to `logs/library-errors.log`
- **Log Rotation**: Daily rotation with size limits

### Data Storage

- **Default Directory**: `data/` in project root
- **File Format**: JSON with pretty printing
- **Auto-save**: Changes saved immediately
- **Backup**: Previous versions maintained

## 🚀 Deployment

### Building for Production

```bash
mvn clean package
```

This creates a self-contained JAR file in the `target/` directory.

### Running in Production

```bash
java -jar target/library-management-system-1.0.0.jar
```

### Environment Variables

- `LIBRARY_DATA_DIR`: Override default data directory
- `LOG_LEVEL`: Set logging level (DEBUG, INFO, WARN, ERROR)

## 📈 Performance Considerations

- **Memory Management**: Efficient object lifecycle management
- **Concurrent Access**: Thread-safe data structures
- **Lazy Loading**: Data loaded only when needed
- **Caching**: In-memory caching for frequently accessed data

## 🔒 Security Features

- **Input Validation**: All user inputs validated
- **Data Sanitization**: SQL injection prevention (if using databases)
- **Access Control**: Member-based access restrictions
- **Audit Trail**: Complete operation logging

## 🛠️ Development Guidelines

### Code Quality

- **Clean Code**: Readable and maintainable code
- **Documentation**: Comprehensive JavaDoc comments
- **Naming Conventions**: Clear and descriptive names
- **Error Handling**: Proper exception management

### Git Workflow

- **Feature Branches**: Development in separate branches
- **Squash Merges**: Clean commit history
- **Conventional Commits**: Standardized commit messages
- **Code Reviews**: All changes reviewed before merge

## 📚 Learning Outcomes

This project demonstrates mastery of:

1. **Java Programming**: Advanced language features and best practices
2. **Object-Oriented Design**: SOLID principles and design patterns
3. **Software Architecture**: Layered architecture and separation of concerns
4. **Testing**: Unit testing and test-driven development
5. **Error Handling**: Robust exception management
6. **Logging**: Professional logging practices
7. **Data Persistence**: File-based data storage
8. **User Interface**: Command-line interface design

## 🤝 Contributing

1. Fork the repository
2. Create a feature branch (`git checkout -b feature/amazing-feature`)
3. Commit your changes (`git commit -m 'Add some amazing feature'`)
4. Push to the branch (`git push origin feature/amazing-feature`)
5. Open a Pull Request

## 📄 License

This project is developed for educational purposes as part of the TAK24 course.

## 👥 Authors

- **TAK24 Group** - *Initial work* - [GitHub](https://github.com/tak24)

## 🙏 Acknowledgments

- TAK24 course instructors for guidance and requirements
- Java community for excellent documentation and examples
- Open source libraries used in this project

---

**Note**: This application is designed for educational purposes and demonstrates advanced Java programming concepts and design patterns. It serves as a comprehensive example of building complex, maintainable software systems.
