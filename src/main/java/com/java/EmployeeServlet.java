package com.java;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/addEmployee")
public class EmployeeServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    // Database connection details
    private static final String DB_URL = "jdbc:mysql://localhost:3306/employee_db";
    private static final String USER = "root"; // Your MySQL username
    private static final String PASS = "admin"; // Your MySQL password

    // Register MySQL JDBC driver
    static {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");  // Register MySQL JDBC driver
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String name = request.getParameter("name");
        String position = request.getParameter("position");
        double salary = 0.0;

        // Validate salary input
        try {
            salary = Double.parseDouble(request.getParameter("salary"));
        } catch (NumberFormatException e) {
            response.getWriter().println("<h3>Error: Invalid salary format</h3>");
            e.printStackTrace();
            return;
        }

        // Insert employee into database
        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS)) {
            String sql = "INSERT INTO employees (name, position, salary) VALUES (?, ?, ?)";
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setString(1, name);
                pstmt.setString(2, position);
                pstmt.setDouble(3, salary);
                pstmt.executeUpdate();
            }
        } catch (Exception e) {
            response.getWriter().println("<h3>Error: " + e.getMessage() + "</h3>");
            e.printStackTrace();
        }

        // Redirect to GET method to display the updated employee list
        response.sendRedirect("addEmployee");
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();

        // Display employee input form
        out.println("<h1>Employee Management System</h1>");
        out.println("<form action='addEmployee' method='post'>");
        out.println("Name: <input type='text' name='name' required><br>");
        out.println("Position: <input type='text' name='position' required><br>");
        out.println("Salary: <input type='number' name='salary' step='0.01' required><br>");
        out.println("<input type='submit' value='Add Employee'>");
        out.println("</form>");

        // Display employee list
        out.println("<h2>Employee List</h2>");
        out.println("<table border='1'>");
        out.println("<tr><th>ID</th><th>Name</th><th>Position</th><th>Salary</th></tr>");

        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS)) {
            String query = "SELECT * FROM employees";
            try (Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(query)) {
                while (rs.next()) {
                    int id = rs.getInt("id");
                    String name = rs.getString("name");
                    String position = rs.getString("position");
                    double salary = rs.getDouble("salary");
                    out.println("<tr><td>" + id + "</td><td>" + name + "</td><td>" + position + "</td><td>" + salary + "</td></tr>");
                }
            }
        } catch (Exception e) {
            out.println("<h3>Error: " + e.getMessage() + "</h3>");
            e.printStackTrace();
        }

        out.println("</table>");
    }
}
