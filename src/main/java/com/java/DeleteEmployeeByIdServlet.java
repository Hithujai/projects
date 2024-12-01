 package com.java;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation for deleting an employee by ID.
 */
@WebServlet("/deleteEmployeeById") // Maps this servlet to /deleteEmployeeById
public class DeleteEmployeeByIdServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    // Database connection details
    private static final String DB_URL = "jdbc:mysql://localhost:3306/employee_db";
    private static final String USER = "root";
    private static final String PASS = "admin";

    // Load the JDBC driver once when the class is loaded
    static {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * Handles GET requests to delete an employee by ID.
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Parse the employee ID from the request parameters
        int id;
        try {
            id = Integer.parseInt(request.getParameter("id"));
        } catch (NumberFormatException e) {
            response.getWriter().println("<h3>Error: Invalid employee ID.</h3>");
            return;
        }

        // Perform database operation to delete the employee
        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS)) {
            String sql = "DELETE FROM employees WHERE id = ?";
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setInt(1, id);
                int rowsAffected = pstmt.executeUpdate();

                if (rowsAffected > 0) {
                    response.getWriter().println("<h3>Employee with ID " + id + " deleted successfully.</h3>");
                } else {
                    response.getWriter().println("<h3>No employee found with ID " + id + ".</h3>");
                }
            }
        } catch (Exception e) {
            response.getWriter().println("<h3>Error: " + e.getMessage() + "</h3>");
            e.printStackTrace();
            return;
        }

        // Redirect back to the list page or perform other redirection as needed
        response.sendRedirect("employeeList"); // Update "employeeList" with the appropriate path
    }
}
