
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/prime")
public class PrimeServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("text/html");

        PrintWriter out = response.getWriter();

        out.println("<html><body>");
        out.println("<h2>Prime Number Checker</h2>");
        out.println("<form method='post'>");
        out.println("Enter a number: ");
        out.println("<input type='number' name='number' required>");
        out.println("<input type='submit' value='Check'>");
        out.println("</form>");
        out.println("</body></html>");
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        int n = Integer.parseInt(request.getParameter("number"));

        boolean prime = n > 1;

        for (int i = 2; i <= n / i; i++) {
            if (n % i == 0) {
                prime = false;
                break;
            }
        }

        response.setContentType("text/html");

        PrintWriter out = response.getWriter();

        out.println("<html><body>");

        if (prime) {
            out.println("<h2>" + n + " is a Prime Number</h2>");
        } else {
            out.println("<h2>" + n + " is not a Prime Number</h2>");
        }

        out.println("<a href='prime'>Check Another Number</a>");
        out.println("</body></html>");
    }
}
