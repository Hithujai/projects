 package com.java;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/deleteEmployee")
public class EmployeeDeleteServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    private static final String DB_URL = "jdbc:mysql://localhost:3306/employee_db";
    private static final String USER = "root";
    private static final String PASS = "admin";

    static {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();

        // Page header
        out.println("<h1>Delete Employee</h1>");
        out.println("<nav>");
        out.println("<a href='employeeList'>View Employees</a> | ");
        out.println("<a href='addEmployee'>Add New Employee</a>");
        out.println("</nav><hr>");

        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS)) {
            // Fetch employees
            String query = "SELECT * FROM employees";
            try (PreparedStatement stmt = conn.prepareStatement(query);
                 ResultSet rs = stmt.executeQuery()) {

                // Table for employee data
                out.println("<table border='1'>");
                out.println("<tr><th>ID</th><th>Name</th><th>Position</th><th>Salary</th><th>Actions</th></tr>");

                while (rs.next()) {
                    int id = rs.getInt("id");
                    String name = rs.getString("name");
                    String position = rs.getString("position");
                    double salary = rs.getDouble("salary");

                    out.println("<tr>");
                    out.println("<td>" + id + "</td>");
                    out.println("<td>" + name + "</td>");
                    out.println("<td>" + position + "</td>");
                    out.println("<td>" + salary + "</td>");
                    out.println("<td><a href='deleteEmployeeById?id=" + id +
                                "' onclick=\"return confirm('Are you sure you want to delete " + name + "?');\">Delete</a></td>");
                    out.println("</tr>");
                }

                out.println("</table>");
            }
        } catch (Exception e) {
            out.println("<h3>Error: " + e.getMessage() + "</h3>");
            e.printStackTrace();
        }
    }
}
