# Java Lab Report

**Name:** MD Shawon Ali\
**ID:** IT24051

## Contents

1.  JDBC: Calculate the Nth prime number and insert it into MySQL
2.  Configure Apache Tomcat and run a Hello World Servlet
3.  Check prime number from a browser using a Servlet
4.  Screenshots and conclusion

> **Note:** Replace `YOUR_MYSQL_PASSWORD` in the JDBC code with your
> local MySQL password before running. The JDBC code assumes MySQL is
> running on `localhost:3306`.

------------------------------------------------------------------------

# Lab 1 --- JDBC: Insert the Nth Prime Number into MySQL

## Objective

Calculate the Nth prime number in Java and save the input `n` and its
prime number in a MySQL table using JDBC.

## Software / tools

-   Java (JDK 17)
-   IntelliJ IDEA
-   Maven
-   MySQL Server and MySQL Workbench
-   MySQL Connector/J

## Database setup

Run the following SQL in MySQL Workbench:

``` sql
CREATE DATABASE IF NOT EXISTS prime_db;
USE prime_db;

CREATE TABLE IF NOT EXISTS prime_numbers (
    id INT AUTO_INCREMENT PRIMARY KEY,
    n INT NOT NULL,
    prime_number BIGINT NOT NULL
);
```

## Maven dependency

Add this dependency inside the `<dependencies>` section of `pom.xml`:

``` xml
<dependency>
    <groupId>com.mysql</groupId>
    <artifactId>mysql-connector-j</artifactId>
    <version>8.4.0</version>
</dependency>
```

## Java code --- `NthPrimeJDBC.java`

``` java
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Scanner;

public class NthPrimeJDBC {

    // Returns true if the given number is prime.
    private static boolean isPrime(long number) {
        if (number < 2) {
            return false;
        }

        // It is sufficient to test divisors up to sqrt(number).
        for (long i = 2; i <= number / i; i++) {
            if (number % i == 0) {
                return false;
            }
        }
        return true;
    }

    // Finds the Nth prime number (1st prime is 2).
    private static long nthPrime(int n) {
        int count = 0;
        long candidate = 1;

        while (count < n) {
            candidate++;

            if (isPrime(candidate)) {
                count++;
            }
        }
        return candidate;
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter N: ");
        int n = scanner.nextInt();

        if (n <= 0) {
            System.out.println("N must be a positive integer.");
            scanner.close();
            return;
        }

        long prime = nthPrime(n);
        System.out.println("The " + n + "th prime number is: " + prime);

        // Update these connection details to match your MySQL setup.
        String url = "jdbc:mysql://localhost:3306/prime_db";
        String username = "root";
        String password = "YOUR_MYSQL_PASSWORD";

        String sql = "INSERT INTO prime_numbers (n, prime_number) VALUES (?, ?)";

        // try-with-resources automatically closes the connection and statement.
        try (Connection connection =
                     DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, n);
            statement.setLong(2, prime);

            int rowsInserted = statement.executeUpdate();
            if (rowsInserted > 0) {
                System.out.println("Data inserted successfully.");
            }

        } catch (SQLException e) {
            System.out.println("Database error: " + e.getMessage());
        }

        scanner.close();
    }
}
```

## Expected result

For input `N = 7`, the 7th prime number is `17`. The program inserts
`n = 7` and `prime_number = 17` into the `prime_numbers` table.

## Screenshot

<img width="890" height="233" alt="IJ" src="https://github.com/user-attachments/assets/d25decc0-dde6-4562-a319-5126afe531e3" />

<img width="537" height="260" alt="DB" src="https://github.com/user-attachments/assets/5a31b6c7-979a-4934-89bd-edd82c2aec30" />

------------------------------------------------------------------------

# Lab 2 --- Configure Apache Tomcat and Run Hello World Servlet

## Objective

Configure Apache Tomcat and deploy a Java Servlet that displays a Hello
World message in a browser.

## Software / tools

-   Java JDK 17
-   IntelliJ IDEA
-   Maven
-   Apache Tomcat 10.1
-   Jakarta Servlet API

## Tomcat setup

1.  Download and extract Apache Tomcat 10.1.
2.  Run `bin\startup.bat` to start the server.
3.  Confirm Tomcat is available at `http://localhost:8080`.
4.  Build the Maven project as a WAR file.
5.  Copy the generated WAR file into Tomcat's `webapps` folder.

## Maven `pom.xml`

Use a Maven WAR project with these key settings/dependency:

``` xml
<packaging>war</packaging>

<properties>
    <maven.compiler.source>17</maven.compiler.source>
    <maven.compiler.target>17</maven.compiler.target>
    <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
</properties>

<dependencies>
    <dependency>
        <groupId>jakarta.servlet</groupId>
        <artifactId>jakarta.servlet-api</artifactId>
        <version>6.0.0</version>
        <scope>provided</scope>
    </dependency>
</dependencies>
```

## Java code --- `HelloWorldServlet.java`

``` java
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;

// Maps this servlet to the /hello URL.
@WebServlet("/hello")
public class HelloWorldServlet extends HttpServlet {

    // Handles HTTP GET requests from the browser.
    @Override
    protected void doGet(HttpServletRequest request,
                         HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("text/html;charset=UTF-8");

        // Send HTML output to the browser.
        PrintWriter out = response.getWriter();
        out.println("<!DOCTYPE html>");
        out.println("<html><head><title>Hello World</title></head><body>");
        out.println("<h1>Hello World!</h1>");
        out.println("<p>My first Java Servlet is working.</p>");
        out.println("</body></html>");
    }
}
```

## Run

After deployment, open:

`http://localhost:8080/HellowServlet-1.0-SNAPSHOT/hello`

## Result screenshot

<img width="920" height="504" alt="Hello World" src="https://github.com/user-attachments/assets/1615eac1-ec14-4b2f-8ff3-cc7d4e8a1d64" />


------------------------------------------------------------------------

# Lab 3 --- Prime Number Checker Using a Servlet

## Objective

Create a web form that accepts a number and displays whether it is prime
using a Java Servlet.

## Java code --- `PrimeServlet.java`

``` java
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;

// Maps this servlet to the /prime URL.
@WebServlet("/prime")
public class PrimeServlet extends HttpServlet {

    // Displays the input form when the user opens /prime.
    @Override
    protected void doGet(HttpServletRequest request,
                         HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();

        out.println("<!DOCTYPE html>");
        out.println("<html><head><title>Prime Number Checker</title></head><body>");
        out.println("<h1>Prime Number Checker</h1>");
        out.println("<form method='post' action='prime'>");
        out.println("Enter a number: ");
        out.println("<input type='number' name='number' required>");
        out.println("<button type='submit'>Check</button>");
        out.println("</form>");
        out.println("</body></html>");
    }

    // Processes the submitted number and returns the result.
    @Override
    protected void doPost(HttpServletRequest request,
                          HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();

        String input = request.getParameter("number");

        out.println("<!DOCTYPE html>");
        out.println("<html><head><title>Prime Result</title></head><body>");

        try {
            int number = Integer.parseInt(input);
            boolean prime = isPrime(number);

            if (prime) {
                out.println("<h1>" + number + " is a Prime Number</h1>");
            } else {
                out.println("<h1>" + number + " is not a Prime Number</h1>");
            }

        } catch (NumberFormatException e) {
            // Handles empty or invalid numeric input.
            out.println("<h1>Please enter a valid integer.</h1>");
        }

        out.println("<p><a href='prime'>Check Another Number</a></p>");
        out.println("</body></html>");
    }

    // A number below 2 is not prime; test possible divisors up to sqrt(n).
    private boolean isPrime(int number) {
        if (number < 2) {
            return false;
        }

        for (int i = 2; i <= number / i; i++) {
            if (number % i == 0) {
                return false;
            }
        }
        return true;
    }
}
```

## Run

Build the project using Maven `package`, deploy the updated WAR to
Tomcat, then open:

`http://localhost:8080/HellowServlet-1.0-SNAPSHOT/prime`

Enter a number and click **Check**.

## Screenshots

### Prime checker input form



### Prime checker result
<img width="922" height="472" alt="Output" src="https://github.com/user-attachments/assets/ee395a95-13b8-4af4-ac30-a5a29ea655e0" />

<img width="926" height="475" alt="prime check from browser" src="https://github.com/user-attachments/assets/061f203b-052a-4d90-a17b-c92960152c49" />


Example shown: `93 is not a Prime Number`.

------------------------------------------------------------------------

# Conclusion

All three Java lab tasks were implemented and tested: 1. Calculated the
Nth prime number and inserted it into MySQL using JDBC. 2. Configured
Apache Tomcat and ran the Hello World Servlet. 3. Built a browser-based
servlet to check whether a number is prime.

**Submitted by:** MD Shawon Ali\
**Student ID:** IT24051
