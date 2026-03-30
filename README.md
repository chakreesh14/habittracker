Habit Tracker API

Features:
- JWT-based authentication
- User-specific habits
- SQLite database (no external DB required)
- Swagger UI for API testing
- Spring Security

Tech Stack:
- Java 21
- Spring Boot 3.5.12
- Spring Web
- Spring Data JPA
- Spring Security
- JWT (jjwt)
- SQLite
- Hibernate Community Dialects
- Swagger UI

Prerequisites:
- Install Java 21

Check installation:
java -version

Note:
Maven is not required. This project uses Maven Wrapper.

Project Setup:
1. Clone the repository:
git clone <your-repo-url>
cd demo

(Optional) 2. Configure application.properties:
Path: src/main/resources/application.properties

Add:
spring.datasource.url=jdbc:sqlite:habit.db
spring.datasource.driver-class-name=org.sqlite.JDBC
spring.jpa.database-platform=org.hibernate.community.dialect.SQLiteDialect
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
server.port=8080

Run the Application:

Windows:
mvnw.cmd spring-boot:run

Mac/Linux:
./mvnw spring-boot:run

Application runs at:
http://localhost:8080

Database:
- Uses SQLite
- No MySQL required
- Database file (habit.db) is created automatically

Swagger UI:
http://localhost:8080/swagger-ui/index.html

Authentication Flow:

1. Sign Up:
POST /auth/signup

Example
Body:
{
  "name": "Chakreesh",
  "email": "test@gmail.com",
  "password": "test123"
}

2. Login:
POST /auth/login

Body:
{
  "email": "test@gmail.com",
  "password": "test123"
}

Copy the JWT token from response.

3. Authorize in Swagger:
Click Authorize and enter:
Bearer your_token_here

Main APIs:

1. Create Habit:
POST /habits
Body:
{
  "name": "reading"
}

2. Get Habits:
GET /habits

3. Complete Habit:
POST /habits/{id}/complete

4. Get Streak:
GET /habits/{id}/streak

Example Flow:
1. Sign up
2. Login
3. Copy token
4. Authorize
5. Create habit
6. Get habits
7. Complete habit
8. Check streak

Common Issues:

1. App not starting:
Run from project root:
mvn spring-boot:run

2. Port already in use:
Change:
server.port=8081

3. Unauthorized (401):
- Login first
- Copy token correctly
- Add "Bearer " before token

4. SQLite issues:
Ensure dependencies:
- sqlite-jdbc
- hibernate-community-dialects

5. Dependency issues:
mvn clean install

Build JAR:

mvn clean package

Run JAR:
java -jar target/demo-0.0.1-SNAPSHOT.jar

Notes:
- No external database needed
- SQLite auto-creates DB file
- Use Swagger for testing APIs
- Always authorize before calling secured APIs
