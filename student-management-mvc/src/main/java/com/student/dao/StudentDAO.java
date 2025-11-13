/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.student.dao;

import com.student.model.Student;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class StudentDAO {
    
    // Database configuration
    private static final String DB_URL = "jdbc:mysql://localhost:3306/student_management";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "1234567890";
    
    // Get database connection
    private Connection getConnection() throws SQLException {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            return DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
        } catch (ClassNotFoundException e) {
            throw new SQLException("MySQL Driver not found", e);
        }
    }
    
    // Column name constants
    private static final String COL_ID = "id";
    private static final String COL_STUDENT_CODE = "student_code";
    private static final String COL_FULL_NAME = "full_name";
    private static final String COL_EMAIL = "email";
    private static final String COL_MAJOR = "major";
    private static final String COL_CREATED_AT = "created_at";

    // Get all students
    public List<Student> getAllStudents() {
        List<Student> students = new ArrayList<>();
        String sql = "SELECT " + COL_ID + ", " + COL_STUDENT_CODE + ", " + COL_FULL_NAME + ", "
                + COL_EMAIL + ", " + COL_MAJOR + ", " + COL_CREATED_AT + " FROM students ORDER BY " + COL_ID + " DESC";
        
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                Student student = new Student();
                student.setId(rs.getInt(COL_ID));
                student.setStudentCode(rs.getString(COL_STUDENT_CODE));
                student.setFullName(rs.getString(COL_FULL_NAME));
                student.setEmail(rs.getString(COL_EMAIL));
                student.setMajor(rs.getString(COL_MAJOR));
                student.setCreatedAt(rs.getTimestamp(COL_CREATED_AT));
                students.add(student);
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return students;
    }

    // Validate sortBy parameter against allowed columns
    private String validateSortBy(String sortBy) {
        if (sortBy == null) return COL_ID;
        switch (sortBy) {
            case "id":
            case "student_code":
            case "full_name":
            case "email":
            case "major":
                return sortBy;
            default:
                return COL_ID;
        }
    }

    // Validate order parameter
    private String validateOrder(String order) {
        if (order != null && "desc".equalsIgnoreCase(order)) {
            return "DESC";
        }
        return "ASC";
    }

    // Get students sorted by column and order (validated)
    public List<Student> getStudentsSorted(String sortBy, String order) {
        List<Student> students = new ArrayList<>();

        String col = validateSortBy(sortBy);
        String ord = validateOrder(order);

        String sql = "SELECT " + COL_ID + ", " + COL_STUDENT_CODE + ", " + COL_FULL_NAME + ", "
                + COL_EMAIL + ", " + COL_MAJOR + ", " + COL_CREATED_AT + " FROM students ORDER BY " + col + " " + ord;

        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Student student = new Student();
                student.setId(rs.getInt(COL_ID));
                student.setStudentCode(rs.getString(COL_STUDENT_CODE));
                student.setFullName(rs.getString(COL_FULL_NAME));
                student.setEmail(rs.getString(COL_EMAIL));
                student.setMajor(rs.getString(COL_MAJOR));
                student.setCreatedAt(rs.getTimestamp(COL_CREATED_AT));
                students.add(student);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return students;
    }

    // Get students filtered by major
    public List<Student> getStudentsByMajor(String major) {
        List<Student> students = new ArrayList<>();
        if (major == null || major.trim().isEmpty()) {
            return getAllStudents();
        }

        String sql = "SELECT " + COL_ID + ", " + COL_STUDENT_CODE + ", " + COL_FULL_NAME + ", "
                + COL_EMAIL + ", " + COL_MAJOR + ", " + COL_CREATED_AT + " FROM students WHERE " + COL_MAJOR + " = ? ORDER BY " + COL_ID + " DESC";

        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, major);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    Student student = new Student();
                    student.setId(rs.getInt(COL_ID));
                    student.setStudentCode(rs.getString(COL_STUDENT_CODE));
                    student.setFullName(rs.getString(COL_FULL_NAME));
                    student.setEmail(rs.getString(COL_EMAIL));
                    student.setMajor(rs.getString(COL_MAJOR));
                    student.setCreatedAt(rs.getTimestamp(COL_CREATED_AT));
                    students.add(student);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return students;
    }

    // Combined: filter by major (optional) and sort by column/order
    public List<Student> getStudentsFiltered(String major, String sortBy, String order) {
        List<Student> students = new ArrayList<>();

        String col = validateSortBy(sortBy);
        String ord = validateOrder(order);

        // If major not provided, just use getStudentsSorted
        if (major == null || major.trim().isEmpty()) {
            return getStudentsSorted(col, ord);
        }

        String sql = "SELECT " + COL_ID + ", " + COL_STUDENT_CODE + ", " + COL_FULL_NAME + ", "
                + COL_EMAIL + ", " + COL_MAJOR + ", " + COL_CREATED_AT + " FROM students WHERE " + COL_MAJOR + " = ? ORDER BY " + col + " " + ord;

        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, major);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    Student student = new Student();
                    student.setId(rs.getInt(COL_ID));
                    student.setStudentCode(rs.getString(COL_STUDENT_CODE));
                    student.setFullName(rs.getString(COL_FULL_NAME));
                    student.setEmail(rs.getString(COL_EMAIL));
                    student.setMajor(rs.getString(COL_MAJOR));
                    student.setCreatedAt(rs.getTimestamp(COL_CREATED_AT));
                    students.add(student);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return students;
    }
    
    // Search students by keyword (student_code, full_name, email)
    public List<Student> searchStudents(String keyword) {
        // If keyword is null or empty, return all students
        if (keyword == null || keyword.trim().isEmpty()) {
            return getAllStudents();
        }

        List<Student> students = new ArrayList<>();
        String sql = "SELECT " + COL_ID + ", " + COL_STUDENT_CODE + ", " + COL_FULL_NAME + ", "
                + COL_EMAIL + ", " + COL_MAJOR + ", " + COL_CREATED_AT + " FROM students WHERE "
                + COL_STUDENT_CODE + " LIKE ? OR " + COL_FULL_NAME + " LIKE ? OR " + COL_EMAIL + " LIKE ? ORDER BY " + COL_ID + " DESC";
        String searchPattern = "%" + keyword + "%";

        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, searchPattern);
            pstmt.setString(2, searchPattern);
            pstmt.setString(3, searchPattern);

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    Student student = new Student();
                    student.setId(rs.getInt(COL_ID));
                    student.setStudentCode(rs.getString(COL_STUDENT_CODE));
                    student.setFullName(rs.getString(COL_FULL_NAME));
                    student.setEmail(rs.getString(COL_EMAIL));
                    student.setMajor(rs.getString(COL_MAJOR));
                    student.setCreatedAt(rs.getTimestamp(COL_CREATED_AT));
                    students.add(student);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return students;
    }

    // Get total number of students
    public int getTotalStudents() {
        String sql = "SELECT COUNT(*) AS cnt FROM students";
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            if (rs.next()) {
                return rs.getInt("cnt");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return 0;
    }

    // Get students with pagination (limit, offset)
    public List<Student> getStudentsPaginated(int offset, int limit) {
        List<Student> students = new ArrayList<>();
        String sql = "SELECT " + COL_ID + ", " + COL_STUDENT_CODE + ", " + COL_FULL_NAME + ", "
                + COL_EMAIL + ", " + COL_MAJOR + ", " + COL_CREATED_AT + " FROM students ORDER BY " + COL_ID + " DESC LIMIT ? OFFSET ?";

        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, limit);
            pstmt.setInt(2, offset);

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    Student student = new Student();
                    student.setId(rs.getInt(COL_ID));
                    student.setStudentCode(rs.getString(COL_STUDENT_CODE));
                    student.setFullName(rs.getString(COL_FULL_NAME));
                    student.setEmail(rs.getString(COL_EMAIL));
                    student.setMajor(rs.getString(COL_MAJOR));
                    student.setCreatedAt(rs.getTimestamp(COL_CREATED_AT));
                    students.add(student);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return students;
    }

    // --- Pagination helpers for search and filter combinations ---

    // Get total count for a keyword search (across code, name, email)
    public int getTotalStudentsByKeyword(String keyword) {
        if (keyword == null || keyword.trim().isEmpty()) return getTotalStudents();
        String sql = "SELECT COUNT(*) AS cnt FROM students WHERE " + COL_STUDENT_CODE + " LIKE ? OR " + COL_FULL_NAME + " LIKE ? OR " + COL_EMAIL + " LIKE ?";
        String p = "%" + keyword + "%";
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, p);
            pstmt.setString(2, p);
            pstmt.setString(3, p);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) return rs.getInt("cnt");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    // Get paginated results for keyword search
    public List<Student> getStudentsPaginatedByKeyword(String keyword, int offset, int limit) {
        if (keyword == null || keyword.trim().isEmpty()) return getStudentsPaginated(offset, limit);
        List<Student> students = new ArrayList<>();
        String sql = "SELECT " + COL_ID + ", " + COL_STUDENT_CODE + ", " + COL_FULL_NAME + ", "
                + COL_EMAIL + ", " + COL_MAJOR + ", " + COL_CREATED_AT + " FROM students WHERE (" + COL_STUDENT_CODE + " LIKE ? OR " + COL_FULL_NAME + " LIKE ? OR " + COL_EMAIL + " LIKE ?) ORDER BY " + COL_ID + " DESC LIMIT ? OFFSET ?";
        String p = "%" + keyword + "%";

        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, p);
            pstmt.setString(2, p);
            pstmt.setString(3, p);
            pstmt.setInt(4, limit);
            pstmt.setInt(5, offset);

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    Student student = new Student();
                    student.setId(rs.getInt(COL_ID));
                    student.setStudentCode(rs.getString(COL_STUDENT_CODE));
                    student.setFullName(rs.getString(COL_FULL_NAME));
                    student.setEmail(rs.getString(COL_EMAIL));
                    student.setMajor(rs.getString(COL_MAJOR));
                    student.setCreatedAt(rs.getTimestamp(COL_CREATED_AT));
                    students.add(student);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return students;
    }

    // Get total count for students by major (optionally used with sort)
    public int getTotalStudentsByMajor(String major) {
        if (major == null || major.trim().isEmpty()) return getTotalStudents();
        String sql = "SELECT COUNT(*) AS cnt FROM students WHERE " + COL_MAJOR + " = ?";
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, major);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) return rs.getInt("cnt");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    // Get paginated results for a major with optional sort
    public List<Student> getStudentsFilteredPaginated(String major, String sortBy, String order, int offset, int limit) {
        List<Student> students = new ArrayList<>();
        String col = validateSortBy(sortBy);
        String ord = validateOrder(order);

        // If no major provided, fallback to sorted paginated
        if (major == null || major.trim().isEmpty()) {
            // Use a simple paginated sorted query
            String sql = "SELECT " + COL_ID + ", " + COL_STUDENT_CODE + ", " + COL_FULL_NAME + ", "
                    + COL_EMAIL + ", " + COL_MAJOR + ", " + COL_CREATED_AT + " FROM students ORDER BY " + col + " " + ord + " LIMIT ? OFFSET ?";

            try (Connection conn = getConnection();
                 PreparedStatement pstmt = conn.prepareStatement(sql)) {

                pstmt.setInt(1, limit);
                pstmt.setInt(2, offset);
                try (ResultSet rs = pstmt.executeQuery()) {
                    while (rs.next()) {
                        Student student = new Student();
                        student.setId(rs.getInt(COL_ID));
                        student.setStudentCode(rs.getString(COL_STUDENT_CODE));
                        student.setFullName(rs.getString(COL_FULL_NAME));
                        student.setEmail(rs.getString(COL_EMAIL));
                        student.setMajor(rs.getString(COL_MAJOR));
                        student.setCreatedAt(rs.getTimestamp(COL_CREATED_AT));
                        students.add(student);
                    }
                }

            } catch (SQLException e) {
                e.printStackTrace();
            }

            return students;
        }

        String sql = "SELECT " + COL_ID + ", " + COL_STUDENT_CODE + ", " + COL_FULL_NAME + ", "
                + COL_EMAIL + ", " + COL_MAJOR + ", " + COL_CREATED_AT + " FROM students WHERE " + COL_MAJOR + " = ? ORDER BY " + col + " " + ord + " LIMIT ? OFFSET ?";

        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, major);
            pstmt.setInt(2, limit);
            pstmt.setInt(3, offset);

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    Student student = new Student();
                    student.setId(rs.getInt(COL_ID));
                    student.setStudentCode(rs.getString(COL_STUDENT_CODE));
                    student.setFullName(rs.getString(COL_FULL_NAME));
                    student.setEmail(rs.getString(COL_EMAIL));
                    student.setMajor(rs.getString(COL_MAJOR));
                    student.setCreatedAt(rs.getTimestamp(COL_CREATED_AT));
                    students.add(student);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return students;
    }

    // Get total count for filtered by major (used with filtered pagination)
    public int getTotalStudentsFiltered(String major) {
        return getTotalStudentsByMajor(major);
    }

    // Get total count for keyword + major combined
    public int getTotalStudentsByKeywordAndMajor(String keyword, String major) {
        if ((keyword == null || keyword.trim().isEmpty()) && (major == null || major.trim().isEmpty())) return getTotalStudents();
        StringBuilder sql = new StringBuilder("SELECT COUNT(*) AS cnt FROM students WHERE ");
        List<Object> params = new ArrayList<>();

        if (keyword != null && !keyword.trim().isEmpty()) {
            sql.append("(" + COL_STUDENT_CODE + " LIKE ? OR " + COL_FULL_NAME + " LIKE ? OR " + COL_EMAIL + " LIKE ?)");
            String p = "%" + keyword + "%";
            params.add(p); params.add(p); params.add(p);
        }

        if (major != null && !major.trim().isEmpty()) {
            if (!params.isEmpty()) sql.append(" AND ");
            sql.append(COL_MAJOR + " = ?");
            params.add(major);
        }

        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql.toString())) {

            for (int i = 0; i < params.size(); i++) {
                Object p = params.get(i);
                pstmt.setObject(i + 1, p);
            }

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) return rs.getInt("cnt");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return 0;
    }

    // Paginated results for keyword + major combined
    public List<Student> getStudentsPaginatedByKeywordAndMajor(String keyword, String major, int offset, int limit) {
        List<Student> students = new ArrayList<>();
        StringBuilder sql = new StringBuilder("SELECT " + COL_ID + ", " + COL_STUDENT_CODE + ", " + COL_FULL_NAME + ", " + COL_EMAIL + ", " + COL_MAJOR + ", " + COL_CREATED_AT + " FROM students WHERE ");
        List<Object> params = new ArrayList<>();

        if (keyword != null && !keyword.trim().isEmpty()) {
            sql.append("(" + COL_STUDENT_CODE + " LIKE ? OR " + COL_FULL_NAME + " LIKE ? OR " + COL_EMAIL + " LIKE ?)");
            String p = "%" + keyword + "%";
            params.add(p); params.add(p); params.add(p);
        }

        if (major != null && !major.trim().isEmpty()) {
            if (!params.isEmpty()) sql.append(" AND ");
            sql.append(COL_MAJOR + " = ?");
            params.add(major);
        }

        sql.append(" ORDER BY " + COL_ID + " DESC LIMIT ? OFFSET ?");

        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql.toString())) {

            int idx = 1;
            for (Object p : params) {
                pstmt.setObject(idx++, p);
            }
            pstmt.setInt(idx++, limit);
            pstmt.setInt(idx++, offset);

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    Student student = new Student();
                    student.setId(rs.getInt(COL_ID));
                    student.setStudentCode(rs.getString(COL_STUDENT_CODE));
                    student.setFullName(rs.getString(COL_FULL_NAME));
                    student.setEmail(rs.getString(COL_EMAIL));
                    student.setMajor(rs.getString(COL_MAJOR));
                    student.setCreatedAt(rs.getTimestamp(COL_CREATED_AT));
                    students.add(student);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return students;
    }
    
    // Get student by ID
    public Student getStudentById(int id) {
        String sql = "SELECT " + COL_ID + ", " + COL_STUDENT_CODE + ", " + COL_FULL_NAME + ", "
                + COL_EMAIL + ", " + COL_MAJOR + ", " + COL_CREATED_AT + " FROM students WHERE " + COL_ID + " = ?";
        
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, id);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    Student student = new Student();
                    student.setId(rs.getInt(COL_ID));
                    student.setStudentCode(rs.getString(COL_STUDENT_CODE));
                    student.setFullName(rs.getString(COL_FULL_NAME));
                    student.setEmail(rs.getString(COL_EMAIL));
                    student.setMajor(rs.getString(COL_MAJOR));
                    student.setCreatedAt(rs.getTimestamp(COL_CREATED_AT));
                    return student;
                }
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return null;
    }
    
    // Add new student
    public boolean addStudent(Student student) {
        String sql = "INSERT INTO students (student_code, full_name, email, major) VALUES (?, ?, ?, ?)";
        
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, student.getStudentCode());
            pstmt.setString(2, student.getFullName());
            pstmt.setString(3, student.getEmail());
            pstmt.setString(4, student.getMajor());
            
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
            
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    // Update student
    public boolean updateStudent(Student student) {
        String sql = "UPDATE students SET student_code = ?, full_name = ?, email = ?, major = ? WHERE id = ?";
        
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, student.getStudentCode());
            pstmt.setString(2, student.getFullName());
            pstmt.setString(3, student.getEmail());
            pstmt.setString(4, student.getMajor());
            pstmt.setInt(5, student.getId());
            
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
            
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    // Delete student
    public boolean deleteStudent(int id) {
        String sql = "DELETE FROM students WHERE id = ?";
        
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, id);
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
            
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
