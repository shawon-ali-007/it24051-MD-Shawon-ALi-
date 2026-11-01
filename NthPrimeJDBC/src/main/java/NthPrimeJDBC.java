
import java.sql.*;
import java.util.Scanner;

public class NthPrimeJDBC {

    static boolean isPrime(long num) {
        if (num < 2) return false;

        for (long i = 2; i * i <= num; i++) {
            if (num % i == 0) return false;
        }

        return true;
    }

    static long nthPrime(int n) {
        int count = 0;
        long num = 1;

        while (count < n) {
            num++;

            if (isPrime(num)) {
                count++;
            }
        }

        return num;
    }

    public static void main(String[] args) {

        String url = "jdbc:mysql://localhost:3306/prime_db";
        String user = "root";
        String password = "Shawon007#";

        Scanner sc = new Scanner(System.in);

        System.out.print("Enter N: ");
        int n = sc.nextInt();

        if (n <= 0) {
            System.out.println("N must be positive.");
            return;
        }

        long prime = nthPrime(n);

        System.out.println("The " + n + "th prime number is: " + prime);

        String sql = "INSERT INTO prime_numbers (n, prime_number) VALUES (?, ?)";

        try (Connection con = DriverManager.getConnection(url, user, password);
             PreparedStatement pst = con.prepareStatement(sql)) {

            pst.setInt(1, n);
            pst.setLong(2, prime);

            pst.executeUpdate();

            System.out.println("Data inserted successfully!");

        } catch (SQLException e) {
            System.out.println("Database Error: " + e.getMessage());
        }

        sc.close();
    }
}
