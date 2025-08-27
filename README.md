# Designation Management System

A Java **AWT/Swing** desktop application for managing designations (e.g., job titles) using a **three-layer architecture**:
- **PL (Presentation Layer)** – User Interface built with AWT/Swing.
- **BL (Business Logic Layer)** – Validates data and enforces rules.
- **DL (Data Layer)** – Handles data persistence (supports **File Handling** and **SQL Database** versions).

---

## Features
- **Add a new designation** with code and title.
- **View all designations** in a table.
- **Update** an existing designation.
- **Delete** a designation.
- **Search functionality** – type the designation title and click "Search" to filter results.
- **Prevent duplicate titles** with validation in BL layer.
- **PDF Generation** – on button click, generates a PDF containing:
  - Company name
  - Company logo
  - Designation table (code and title)
- **Two storage implementations**:
  1. **File Handling** – Data stored locally in a serialized file.
  2. **SQL Database** – Data stored in a relational database.

---

## Technologies Used
- **Java** (AWT & Swing for UI)
- **Java I/O Streams** (for File Handling version)
- **JDBC** (for SQL Database version)
- **MySQL** (for SQL version)
- **iText7** (for PDF generation)
- **Gradle** (for building JARs)

---

## Project Structure
   **pl/** # Presentation Layer (AWT/Swing UI) <br>
   **bl/** # Business Logic Layer <br>
   **dl/** # Data Layer - File Handling version <br>
   **dbdl/** # Data Layer - SQL Database version <br>
   **utils/** # Libraries (iText7, MySQL connector, etc.) <br>
   All layers have the exception classes for bl there is BLException for dl there is DAOException 
   the dl layer has dao and dto folders dto stands for data transfer object and dao stands for data access object are used to send data and perform operation on data <br>


---

## How to Run

### Prerequisites
- **Java JDK 17+**
- **Gradle**
- **MySQL** (only for SQL Database version)

---

### Steps

1. **Clone the Repository**
   ```bash
     git clone https://github.com/BrajRaj89/Designation-Management.git


2. **Run File Handling Version**

     Open a terminal/command prompt.

     Navigate to the pl folder.

     Type:designation1

3. **Run SQL Database Version**
     Ensure MySQL is running and create the table:
     CREATE TABLE designation (
     code INT PRIMARY KEY AUTO_INCREMENT,
     title VARCHAR(120) UNIQUE NOT NULL
      );

     Update DB credentials in the dbdl layer DAOConnection.java 

     Open a terminal/command prompt.

     Navigate to the pl folder.

     Type: gradle build ,after updating the db credentials and creating table then
     Type: designation2

## UI
### ScreenShot & Video
 assets/
 assets/


