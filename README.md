# Order Book System

A **stock order book system** that simulates concurrent buy/sell orders from multiple brokers using **Java multithreading** and **HikariCP** for database connection pooling. This project demonstrates handling shared resources with multithreading, preventing race conditions, and ensuring thread safety using Java's concurrency utilities.

## Table of Contents
- [Features](#features)
- [Technologies Used](#technologies-used)
- [Project Structure](#project-structure)
- [Getting Started](#getting-started)
  - [Prerequisites](#prerequisites)
  - [Installation](#installation)
- [Usage](#usage)
- [Testing](#testing)

---

## Features
- Simulates a centralized stock order book where multiple brokers place buy and sell orders concurrently.
- Uses **ReentrantLock** to handle thread safety and prevent race conditions when accessing shared stock resources.
- **ExecutorService** is used to manage the broker threads efficiently.
- Integrated with **HikariCP** for efficient database connection pooling.
- Connects to a **MySQL database** for storing stock and order data.
- Demonstrates handling of concurrency using locks, synchronized blocks, and thread management techniques.

---

## Technologies Used
- **Java**: The core language used for developing the application.
- **HikariCP**: For managing the database connection pool.
- **MySQL**: As the database for storing stock and order data.
- **Maven**: For dependency management and building the project.
- **JUnit**: For testing the functionality of the order book system.

---

## Project Structure

<img width="560" alt="image" src="https://github.com/user-attachments/assets/cf672264-a35e-416f-97b7-f595429e9d71">

---

## Getting Started

### Prerequisites
Before you begin, ensure you have the following installed:
- **Java JDK 8 or higher**: Download from [Oracle's JDK page](https://www.oracle.com/java/technologies/javase-jdk11-downloads.html).
- **Maven**: For building and running the project. Install from [Maven’s website](https://maven.apache.org/install.html).
- **MySQL**: Install MySQL for the backend database. Download from [MySQL](https://dev.mysql.com/downloads/installer/).

### Installation

1. **Clone the Repository**:

```bash
git clone https://github.com/syahmimscs/order-book.git
cd order-book
```

2. **Build the Project**:
Navigate to the project root directory and run Maven to install dependencies and compile the project:
```bash
mvn clean compile
```

## Usage
### Running The Application
To run the application and simulate brokers placing orders, use the following Maven command:
```bash
mvn exec:java -Dexec.mainClass="com.orderbook.Main"
```

This will start the program and simulate multiple brokers placing buy and sell orders concurrently.

## Testing
The project includes JUnit tests to verify the functionality of the order book system. The tests cover stock management, order placement, and concurrency handling.

### Running the Tests

To run all the unit tests, use the following Maven command:

```bash
mvn test
```

You will see a report summarizing the test results, ensuring that the order book behaves as expected under different scenarios, including concurrent operations.
