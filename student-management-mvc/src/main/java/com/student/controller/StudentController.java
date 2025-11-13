/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.student.controller;

import com.student.dao.StudentDAO;
import com.student.model.Student;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

@WebServlet("/student")
public class StudentController extends HttpServlet {
    
    private StudentDAO studentDAO;
    
    @Override
    public void init() {
        studentDAO = new StudentDAO();
    }
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        String action = request.getParameter("action");
        
        if (action == null) {
            action = "list";
        }
        
        switch (action) {
            case "new":
                showNewForm(request, response);
                break;
            case "edit":
                showEditForm(request, response);
                break;
            case "delete":
                deleteStudent(request, response);
                break;
            case "sort":
                sortStudents(request, response);
                break;
            case "filter":
                filterStudents(request, response);
                break;
            default:
                listStudents(request, response);
                break;
        }
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        String action = request.getParameter("action");
        
        switch (action) {
            case "insert":
                insertStudent(request, response);
                break;
            case "update":
                updateStudent(request, response);
                break;
        }
    }
    
    // List all students
    private void listStudents(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        // Parameters that can affect listing
        String keyword = request.getParameter("keyword");
        String major = request.getParameter("major");
        String sortBy = request.getParameter("sortBy");
        String order = request.getParameter("order");

        // Pagination parameters (page)
        String pageParam = request.getParameter("page");
        int currentPage = 1;
        try {
            if (pageParam != null) currentPage = Integer.parseInt(pageParam);
        } catch (NumberFormatException ex) {
            currentPage = 1;
        }
        if (currentPage < 1) currentPage = 1;

        int recordsPerPage = 10;

        List<Student> students;
        int totalRecords = 0;

        // Prioritize: keyword search (optionally combined with major), else filter by major (with optional sort), else default listing
        if (keyword != null && !keyword.trim().isEmpty()) {
            keyword = keyword.trim();
            request.setAttribute("keyword", keyword);

            if (major != null && !major.trim().isEmpty()) {
                // keyword + major
                totalRecords = studentDAO.getTotalStudentsByKeywordAndMajor(keyword, major);
                int totalPages = (int) Math.ceil((double) totalRecords / recordsPerPage);
                if (totalPages < 1) totalPages = 1;
                if (currentPage > totalPages) currentPage = totalPages;
                int offset = (currentPage - 1) * recordsPerPage;
                students = studentDAO.getStudentsPaginatedByKeywordAndMajor(keyword, major, offset, recordsPerPage);

                request.setAttribute("selectedMajor", major);
                request.setAttribute("students", students);
                request.setAttribute("currentPage", currentPage);
                request.setAttribute("totalPages", totalPages);
                request.setAttribute("totalRecords", totalRecords);
                request.setAttribute("recordsPerPage", recordsPerPage);
            } else {
                // only keyword search
                totalRecords = studentDAO.getTotalStudentsByKeyword(keyword);
                int totalPages = (int) Math.ceil((double) totalRecords / recordsPerPage);
                if (totalPages < 1) totalPages = 1;
                if (currentPage > totalPages) currentPage = totalPages;
                int offset = (currentPage - 1) * recordsPerPage;
                students = studentDAO.getStudentsPaginatedByKeyword(keyword, offset, recordsPerPage);

                request.setAttribute("students", students);
                request.setAttribute("currentPage", currentPage);
                request.setAttribute("totalPages", totalPages);
                request.setAttribute("totalRecords", totalRecords);
                request.setAttribute("recordsPerPage", recordsPerPage);
            }
        } else if (major != null && !major.trim().isEmpty()) {
            // Filter by major (with optional sort)
            totalRecords = studentDAO.getTotalStudentsByMajor(major);
            int totalPages = (int) Math.ceil((double) totalRecords / recordsPerPage);
            if (totalPages < 1) totalPages = 1;
            if (currentPage > totalPages) currentPage = totalPages;
            int offset = (currentPage - 1) * recordsPerPage;

            students = studentDAO.getStudentsFilteredPaginated(major, sortBy, order, offset, recordsPerPage);

            request.setAttribute("selectedMajor", major);
            request.setAttribute("students", students);
            request.setAttribute("currentPage", currentPage);
            request.setAttribute("totalPages", totalPages);
            request.setAttribute("totalRecords", totalRecords);
            request.setAttribute("recordsPerPage", recordsPerPage);
            request.setAttribute("sortBy", sortBy == null ? "id" : sortBy);
            request.setAttribute("order", order == null ? "asc" : order.toLowerCase());
        } else {
            // Default paginated listing (no keyword, no major)
            totalRecords = studentDAO.getTotalStudents();
            int totalPages = (int) Math.ceil((double) totalRecords / recordsPerPage);
            if (totalPages < 1) totalPages = 1;
            if (currentPage > totalPages) currentPage = totalPages;
            int offset = (currentPage - 1) * recordsPerPage;

            students = studentDAO.getStudentsPaginated(offset, recordsPerPage);

            request.setAttribute("students", students);
            request.setAttribute("currentPage", currentPage);
            request.setAttribute("totalPages", totalPages);
            request.setAttribute("totalRecords", totalRecords);
            request.setAttribute("recordsPerPage", recordsPerPage);
        }

        RequestDispatcher dispatcher = request.getRequestDispatcher("/views/student-list.jsp");
        dispatcher.forward(request, response);
    }

    // Sort students (controller)
    private void sortStudents(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String sortBy = request.getParameter("sortBy");
        String order = request.getParameter("order");
        String major = request.getParameter("major");

        List<Student> students;
        if (major != null && !major.trim().isEmpty()) {
            // if major provided, use combined filter+sort
            students = studentDAO.getStudentsFiltered(major, sortBy, order);
            request.setAttribute("selectedMajor", major);
        } else {
            students = studentDAO.getStudentsSorted(sortBy, order);
        }

        request.setAttribute("students", students);
        request.setAttribute("sortBy", sortBy == null ? "id" : sortBy);
        request.setAttribute("order", order == null ? "asc" : order.toLowerCase());

        RequestDispatcher dispatcher = request.getRequestDispatcher("/views/student-list.jsp");
        dispatcher.forward(request, response);
    }

    // Filter students by major (controller)
    private void filterStudents(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String major = request.getParameter("major");
        String sortBy = request.getParameter("sortBy");
        String order = request.getParameter("order");

        List<Student> students;
        if (sortBy != null || order != null) {
            students = studentDAO.getStudentsFiltered(major, sortBy, order);
            request.setAttribute("sortBy", sortBy == null ? "id" : sortBy);
            request.setAttribute("order", order == null ? "asc" : order.toLowerCase());
        } else {
            students = studentDAO.getStudentsByMajor(major);
        }

        request.setAttribute("students", students);
        request.setAttribute("selectedMajor", major);

        RequestDispatcher dispatcher = request.getRequestDispatcher("/views/student-list.jsp");
        dispatcher.forward(request, response);
    }
    
    // Show form for new student
    private void showNewForm(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        RequestDispatcher dispatcher = request.getRequestDispatcher("/views/student-form.jsp");
        dispatcher.forward(request, response);
    }
    
    // Show form for editing student
    private void showEditForm(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        int id = Integer.parseInt(request.getParameter("id"));
        Student existingStudent = studentDAO.getStudentById(id);
        
        request.setAttribute("student", existingStudent);
        
        RequestDispatcher dispatcher = request.getRequestDispatcher("/views/student-form.jsp");
        dispatcher.forward(request, response);
    }
    
    // Insert new student
    private void insertStudent(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {

        String studentCode = request.getParameter("studentCode");
        String fullName = request.getParameter("fullName");
        String email = request.getParameter("email");
        String major = request.getParameter("major");

        Student newStudent = new Student(studentCode, fullName, email, major);

        // Validate server-side
        if (!validateStudent(newStudent, request)) {
            // Preserve entered data and forward back to form
            request.setAttribute("student", newStudent);
            RequestDispatcher dispatcher = request.getRequestDispatcher("/views/student-form.jsp");
            dispatcher.forward(request, response);
            return;
        }

        if (studentDAO.addStudent(newStudent)) {
            response.sendRedirect("student?action=list&message=Student added successfully");
        } else {
            response.sendRedirect("student?action=list&error=Failed to add student");
        }
    }
    
    // Update student
    private void updateStudent(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {

        int id = Integer.parseInt(request.getParameter("id"));
        String studentCode = request.getParameter("studentCode");
        String fullName = request.getParameter("fullName");
        String email = request.getParameter("email");
        String major = request.getParameter("major");

        Student student = new Student(studentCode, fullName, email, major);
        student.setId(id);

        // Validate server-side
        if (!validateStudent(student, request)) {
            // Preserve entered data and forward back to form
            request.setAttribute("student", student);
            RequestDispatcher dispatcher = request.getRequestDispatcher("/views/student-form.jsp");
            dispatcher.forward(request, response);
            return;
        }

        if (studentDAO.updateStudent(student)) {
            response.sendRedirect("student?action=list&message=Student updated successfully");
        } else {
            response.sendRedirect("student?action=list&error=Failed to update student");
        }
    }

    // Server-side validation helper
    private boolean validateStudent(Student student, HttpServletRequest request) {
        boolean isValid = true;

        // Student code: normalize to uppercase, required and pattern 2 letters + 3+ digits
        String code = student.getStudentCode();
        if (code != null) code = code.trim();
        if (code == null || code.isEmpty()) {
            request.setAttribute("errorCode", "Student code is required");
            isValid = false;
        } else {
            // normalize to uppercase so validation is case-insensitive
            String normalizedCode = code.toUpperCase();
            student.setStudentCode(normalizedCode);
            String codePattern = "[A-Z]{2}\\d{3,}";
            if (!normalizedCode.matches(codePattern)) {
                request.setAttribute("errorCode", "Invalid format. Use 2 letters + 3+ digits (e.g., SV001)");
                isValid = false;
            }
        }

        // Full name: required, min length 2
        String name = student.getFullName();
        if (name == null || name.trim().isEmpty()) {
            request.setAttribute("errorName", "Full name is required");
            isValid = false;
        } else if (name.trim().length() < 2) {
            request.setAttribute("errorName", "Full name must be at least 2 characters");
            isValid = false;
        }

        // Email: optional, but if provided must be valid
        String email = student.getEmail();
        if (email != null && !email.trim().isEmpty()) {
            String emailPattern = "^[A-Za-z0-9+_.-]+@(.+)$";
            if (!email.matches(emailPattern)) {
                request.setAttribute("errorEmail", "Invalid email format");
                isValid = false;
            }
        }

        // Major: required
        String major = student.getMajor();
        if (major == null || major.trim().isEmpty()) {
            request.setAttribute("errorMajor", "Major is required");
            isValid = false;
        }

        return isValid;
    }
    
    // Delete student
    private void deleteStudent(HttpServletRequest request, HttpServletResponse response) 
            throws IOException {
        
        int id = Integer.parseInt(request.getParameter("id"));
        
        if (studentDAO.deleteStudent(id)) {
            response.sendRedirect("student?action=list&message=Student deleted successfully");
        } else {
            response.sendRedirect("student?action=list&error=Failed to delete student");
        }
    }
}
