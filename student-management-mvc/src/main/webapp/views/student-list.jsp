<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Student List - MVC</title>
    <style>
        * {
            margin: 0;
            padding: 0;
            box-sizing: border-box;
        }
        
        body {
            font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            min-height: 100vh;
            padding: 20px;
        }
        
        .container {
            max-width: 1200px;
            margin: 0 auto;
            background: white;
            border-radius: 10px;
            padding: 30px;
            box-shadow: 0 10px 40px rgba(0,0,0,0.2);
        }
        
        h1 {
            color: #333;
            margin-bottom: 10px;
            font-size: 32px;
        }
        
        .subtitle {
            color: #666;
            margin-bottom: 30px;
            font-style: italic;
        }
        
        .message {
            padding: 15px;
            margin-bottom: 20px;
            border-radius: 5px;
            font-weight: 500;
        }
        
        .success {
            background-color: #d4edda;
            color: #155724;
            border: 1px solid #c3e6cb;
        }
        
        .error {
            background-color: #f8d7da;
            color: #721c24;
            border: 1px solid #f5c6cb;
        }
        
        .btn {
            display: inline-block;
            padding: 12px 24px;
            text-decoration: none;
            border-radius: 5px;
            font-weight: 500;
            transition: all 0.3s;
            border: none;
            cursor: pointer;
            font-size: 14px;
        }
        
        .btn-primary {
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            color: white;
        }
        
        .btn-primary:hover {
            transform: translateY(-2px);
            box-shadow: 0 5px 15px rgba(102, 126, 234, 0.4);
        }
        
        .btn-secondary {
            background-color: #6c757d;
            color: white;
        }
        
        .btn-danger {
            background-color: #dc3545;
            color: white;
            padding: 8px 16px;
            font-size: 13px;
        }
        
        .btn-danger:hover {
            background-color: #c82333;
        }
        
        table {
            width: 100%;
            border-collapse: collapse;
            margin-top: 20px;
        }
        
        thead {
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            color: white;
        }
        
        th, td {
            padding: 15px;
            text-align: left;
            border-bottom: 1px solid #ddd;
        }
        
        th {
            font-weight: 600;
            text-transform: uppercase;
            font-size: 13px;
            letter-spacing: 0.5px;
        }
        
        tbody tr {
            transition: background-color 0.2s;
        }
        
        tbody tr:hover {
            background-color: #f8f9fa;
        }
        
        .actions {
            display: flex;
            gap: 10px;
        }
        
        .empty-state {
            text-align: center;
            padding: 60px 20px;
            color: #999;
        }
        
        .empty-state-icon {
            font-size: 64px;
            margin-bottom: 20px;
        }
        .pagination {
            margin: 20px 0;
            text-align: center;
        }

        .pagination a {
            padding: 8px 12px;
            margin: 0 4px;
            border: 1px solid #ddd;
            text-decoration: none;
            color: #333;
            border-radius: 4px;
        }

        .pagination strong {
            padding: 8px 12px;
            margin: 0 4px;
            background-color: #4CAF50;
            color: white;
            border: 1px solid #4CAF50;
            border-radius: 4px;
        }

        .pagination .disabled {
            padding: 8px 12px;
            margin: 0 4px;
            color: #bbb;
            border: 1px solid #eee;
            border-radius: 4px;
        }
    </style>
</head>
<body>
    <div class="container">
        <h1>📚 Student Management System</h1>
        <p class="subtitle">MVC Pattern with Jakarta EE & JSTL</p>
        
        <!-- Success Message -->
        <c:if test="${not empty param.message}">
            <div class="message success">
                ✅ ${param.message}
            </div>
        </c:if>
        
        <!-- Error Message -->
        <c:if test="${not empty param.error}">
            <div class="message error">
                ❌ ${param.error}
            </div>
        </c:if>
        
        <!-- Add New Student Button -->
        <div style="margin-bottom: 20px;">
            <a href="student?action=new" class="btn btn-primary">
                ➕ Add New Student
            </a>
        </div>
        
        <!-- Search Form -->
        <div style="margin-bottom: 20px; display:flex; gap:10px; align-items:center;">
            <form action="student" method="get" style="display:flex; gap:8px; align-items:center;">
                <input type="hidden" name="action" value="list" />
          <input type="text" name="keyword" placeholder="Search by code, name or email" 
              value="<c:out value='${keyword}'/>" 
              style="padding:10px; border-radius:6px; border:1px solid #ccc; width:320px;" />
                <button type="submit" class="btn btn-primary">🔎 Search</button>
                <a href="student?action=list" class="btn btn-secondary">Clear</a>
            </form>
        </div>
        
        <!-- Search result message -->
        <c:if test="${not empty keyword}">
            <div style="margin-bottom:12px; font-weight:600; color:#333;">
                Kết quả tìm kiếm cho "<c:out value="${keyword}"/>"
            </div>
        </c:if>

        <!-- Filter by Major -->
        <div class="filter-box" style="margin-bottom:16px;">
            <form action="student" method="get" style="display:flex; gap:8px; align-items:center;">
                <input type="hidden" name="action" value="filter" />
                <input type="hidden" name="sortBy" value="${sortBy}" />
                <input type="hidden" name="order" value="${order}" />
                <label for="major">Filter by Major:</label>
                <select id="major" name="major" style="padding:8px; border-radius:6px;">
                    <option value="">All Majors</option>
                    <option value="Computer Science" <c:if test="${selectedMajor == 'Computer Science'}">selected</c:if>>Computer Science</option>
                    <option value="Information Technology" <c:if test="${selectedMajor == 'Information Technology'}">selected</c:if>>Information Technology</option>
                    <option value="Software Engineering" <c:if test="${selectedMajor == 'Software Engineering'}">selected</c:if>>Software Engineering</option>
                    <option value="Business Administration" <c:if test="${selectedMajor == 'Business Administration'}">selected</c:if>>Business Administration</option>
                </select>
                <button type="submit" class="btn btn-primary">Apply Filter</button>
                <c:if test="${not empty selectedMajor}">
                    <a href="student?action=list" class="btn btn-secondary">Clear Filter</a>
                </c:if>
            </form>
        </div>
        
        <!-- Student Table -->
        <c:choose>
            <c:when test="${not empty students}">
                <table>
                    <thead>
                        <tr>
                            <!-- ID -->
                            <th>
                                <c:set var="nextOrderId">
                                    <c:choose>
                                        <c:when test="${sortBy == 'id' && order == 'asc'}">desc</c:when>
                                        <c:otherwise>asc</c:otherwise>
                                    </c:choose>
                                </c:set>
                                <c:url var="sortIdUrl" value="student">
                                    <c:param name="action" value="sort" />
                                    <c:param name="sortBy" value="id" />
                                    <c:param name="order" value="${nextOrderId}" />
                                    <c:if test="${not empty selectedMajor}">
                                        <c:param name="major" value="${selectedMajor}" />
                                    </c:if>
                                </c:url>
                                <a href="${sortIdUrl}">ID</a>
                                <c:if test="${sortBy == 'id'}">
                                    <c:choose>
                                        <c:when test="${order == 'asc'}"> ▲</c:when>
                                        <c:otherwise> ▼</c:otherwise>
                                    </c:choose>
                                </c:if>
                            </th>

                            <!-- Code -->
                            <th>
                                <c:set var="nextOrderCode">
                                    <c:choose>
                                        <c:when test="${sortBy == 'student_code' && order == 'asc'}">desc</c:when>
                                        <c:otherwise>asc</c:otherwise>
                                    </c:choose>
                                </c:set>
                                <c:url var="sortCodeUrl" value="student">
                                    <c:param name="action" value="sort" />
                                    <c:param name="sortBy" value="student_code" />
                                    <c:param name="order" value="${nextOrderCode}" />
                                    <c:if test="${not empty selectedMajor}">
                                        <c:param name="major" value="${selectedMajor}" />
                                    </c:if>
                                </c:url>
                                <a href="${sortCodeUrl}">Code</a>
                                <c:if test="${sortBy == 'student_code'}">
                                    <c:choose>
                                        <c:when test="${order == 'asc'}"> ▲</c:when>
                                        <c:otherwise> ▼</c:otherwise>
                                    </c:choose>
                                </c:if>
                            </th>

                            <!-- Name -->
                            <th>
                                <c:set var="nextOrderName">
                                    <c:choose>
                                        <c:when test="${sortBy == 'full_name' && order == 'asc'}">desc</c:when>
                                        <c:otherwise>asc</c:otherwise>
                                    </c:choose>
                                </c:set>
                                <c:url var="sortNameUrl" value="student">
                                    <c:param name="action" value="sort" />
                                    <c:param name="sortBy" value="full_name" />
                                    <c:param name="order" value="${nextOrderName}" />
                                    <c:if test="${not empty selectedMajor}">
                                        <c:param name="major" value="${selectedMajor}" />
                                    </c:if>
                                </c:url>
                                <a href="${sortNameUrl}">Name</a>
                                <c:if test="${sortBy == 'full_name'}">
                                    <c:choose>
                                        <c:when test="${order == 'asc'}"> ▲</c:when>
                                        <c:otherwise> ▼</c:otherwise>
                                    </c:choose>
                                </c:if>
                            </th>

                            <!-- Email -->
                            <th>
                                <c:set var="nextOrderEmail">
                                    <c:choose>
                                        <c:when test="${sortBy == 'email' && order == 'asc'}">desc</c:when>
                                        <c:otherwise>asc</c:otherwise>
                                    </c:choose>
                                </c:set>
                                <c:url var="sortEmailUrl" value="student">
                                    <c:param name="action" value="sort" />
                                    <c:param name="sortBy" value="email" />
                                    <c:param name="order" value="${nextOrderEmail}" />
                                    <c:if test="${not empty selectedMajor}">
                                        <c:param name="major" value="${selectedMajor}" />
                                    </c:if>
                                </c:url>
                                <a href="${sortEmailUrl}">Email</a>
                                <c:if test="${sortBy == 'email'}">
                                    <c:choose>
                                        <c:when test="${order == 'asc'}"> ▲</c:when>
                                        <c:otherwise> ▼</c:otherwise>
                                    </c:choose>
                                </c:if>
                            </th>

                            <!-- Major -->
                            <th>
                                <c:set var="nextOrderMajor">
                                    <c:choose>
                                        <c:when test="${sortBy == 'major' && order == 'asc'}">desc</c:when>
                                        <c:otherwise>asc</c:otherwise>
                                    </c:choose>
                                </c:set>
                                <c:url var="sortMajorUrl" value="student">
                                    <c:param name="action" value="sort" />
                                    <c:param name="sortBy" value="major" />
                                    <c:param name="order" value="${nextOrderMajor}" />
                                    <c:if test="${not empty selectedMajor}">
                                        <c:param name="major" value="${selectedMajor}" />
                                    </c:if>
                                </c:url>
                                <a href="${sortMajorUrl}">Major</a>
                                <c:if test="${sortBy == 'major'}">
                                    <c:choose>
                                        <c:when test="${order == 'asc'}"> ▲</c:when>
                                        <c:otherwise> ▼</c:otherwise>
                                    </c:choose>
                                </c:if>
                            </th>

                            <th>Actions</th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:forEach var="student" items="${students}">
                            <tr>
                                <td>${student.id}</td>
                                <td><strong>${student.studentCode}</strong></td>
                                <td>${student.fullName}</td>
                                <td>${student.email}</td>
                                <td>${student.major}</td>
                                <td>
                                    <div class="actions">
                                        <a href="student?action=edit&id=${student.id}" class="btn btn-secondary">
                                            ✏️ Edit
                                        </a>
                                        <a href="student?action=delete&id=${student.id}" 
                                           class="btn btn-danger"
                                           onclick="return confirm('Are you sure you want to delete this student?')">
                                            🗑️ Delete
                                        </a>
                                    </div>
                                </td>
                            </tr>
                        </c:forEach>
                    </tbody>
                </table>
            </c:when>
            <c:otherwise>
                <div class="empty-state">
                    <div class="empty-state-icon">📭</div>
                    <h3>No students found</h3>
                    <p>Start by adding a new student</p>
                </div>
            </c:otherwise>
        </c:choose>
            
            <!-- Pagination controls -->
            <c:if test="${not empty totalPages && totalPages > 1}">
                <div class="pagination">
                    <!-- Previous / First -->
                    <c:choose>
                        <c:when test="${currentPage > 1}">
                            <c:url var="firstUrl" value="student">
                                <c:param name="action" value="list" />
                                <c:param name="page" value="1" />
                                <c:if test="${not empty keyword}"><c:param name="keyword" value="${keyword}"/></c:if>
                                <c:if test="${not empty selectedMajor}"><c:param name="major" value="${selectedMajor}"/></c:if>
                                <c:if test="${not empty sortBy}"><c:param name="sortBy" value="${sortBy}"/></c:if>
                                <c:if test="${not empty order}"><c:param name="order" value="${order}"/></c:if>
                            </c:url>
                            <a href="${firstUrl}">First</a>

                            <c:url var="prevUrl" value="student">
                                <c:param name="action" value="list" />
                                <c:param name="page" value="${currentPage - 1}" />
                                <c:if test="${not empty keyword}"><c:param name="keyword" value="${keyword}"/></c:if>
                                <c:if test="${not empty selectedMajor}"><c:param name="major" value="${selectedMajor}"/></c:if>
                                <c:if test="${not empty sortBy}"><c:param name="sortBy" value="${sortBy}"/></c:if>
                                <c:if test="${not empty order}"><c:param name="order" value="${order}"/></c:if>
                            </c:url>
                            <a href="${prevUrl}">« Previous</a>
                        </c:when>
                        <c:otherwise>
                            <span class="disabled">First</span>
                            <span class="disabled">« Previous</span>
                        </c:otherwise>
                    </c:choose>

                    <!-- Page window: show up to 5 pages (current ±2) -->
                    <c:set var="startPage" value="${currentPage - 2}" />
                    <c:if test="${startPage < 1}"><c:set var="startPage" value="1"/></c:if>
                    <c:set var="endPage" value="${currentPage + 2}" />
                    <c:if test="${endPage > totalPages}"><c:set var="endPage" value="${totalPages}"/></c:if>
                    <c:if test="${endPage - startPage < 4}">
                        <c:set var="startPage" value="${endPage - 4}" />
                        <c:if test="${startPage < 1}"><c:set var="startPage" value="1"/></c:if>
                    </c:if>

                    <c:if test="${startPage > 1}">
                        <span>...</span>
                    </c:if>

                    <c:forEach begin="${startPage}" end="${endPage}" var="i">
                        <c:choose>
                            <c:when test="${i == currentPage}">
                                <strong>${i}</strong>
                            </c:when>
                            <c:otherwise>
                                <c:url var="pageUrl" value="student">
                                    <c:param name="action" value="list" />
                                    <c:param name="page" value="${i}" />
                                    <c:if test="${not empty keyword}"><c:param name="keyword" value="${keyword}"/></c:if>
                                    <c:if test="${not empty selectedMajor}"><c:param name="major" value="${selectedMajor}"/></c:if>
                                    <c:if test="${not empty sortBy}"><c:param name="sortBy" value="${sortBy}"/></c:if>
                                    <c:if test="${not empty order}"><c:param name="order" value="${order}"/></c:if>
                                </c:url>
                                <a href="${pageUrl}">${i}</a>
                            </c:otherwise>
                        </c:choose>
                    </c:forEach>

                    <c:if test="${endPage < totalPages}">
                        <span>...</span>
                    </c:if>

                    <!-- Next / Last -->
                    <c:choose>
                        <c:when test="${currentPage < totalPages}">
                            <c:url var="nextUrl" value="student">
                                <c:param name="action" value="list" />
                                <c:param name="page" value="${currentPage + 1}" />
                                <c:if test="${not empty keyword}"><c:param name="keyword" value="${keyword}"/></c:if>
                                <c:if test="${not empty selectedMajor}"><c:param name="major" value="${selectedMajor}"/></c:if>
                                <c:if test="${not empty sortBy}"><c:param name="sortBy" value="${sortBy}"/></c:if>
                                <c:if test="${not empty order}"><c:param name="order" value="${order}"/></c:if>
                            </c:url>
                            <a href="${nextUrl}">Next »</a>

                            <c:url var="lastUrl" value="student">
                                <c:param name="action" value="list" />
                                <c:param name="page" value="${totalPages}" />
                                <c:if test="${not empty keyword}"><c:param name="keyword" value="${keyword}"/></c:if>
                                <c:if test="${not empty selectedMajor}"><c:param name="major" value="${selectedMajor}"/></c:if>
                                <c:if test="${not empty sortBy}"><c:param name="sortBy" value="${sortBy}"/></c:if>
                                <c:if test="${not empty order}"><c:param name="order" value="${order}"/></c:if>
                            </c:url>
                            <a href="${lastUrl}">Last</a>
                        </c:when>
                        <c:otherwise>
                            <span class="disabled">Next »</span>
                            <span class="disabled">Last</span>
                        </c:otherwise>
                    </c:choose>
                </div>

                <!-- Record range display -->
                <c:set var="startRecord" value="${(currentPage - 1) * recordsPerPage + 1}" />
                <c:set var="endRecordCandidate" value="${currentPage * recordsPerPage}" />
                <c:choose>
                    <c:when test="${endRecordCandidate > totalRecords}">
                        <c:set var="endRecord" value="${totalRecords}" />
                    </c:when>
                    <c:otherwise>
                        <c:set var="endRecord" value="${endRecordCandidate}" />
                    </c:otherwise>
                </c:choose>

                <p style="text-align:center; margin-top:8px;">Showing ${startRecord} - ${endRecord} of ${totalRecords} records (page ${currentPage} of ${totalPages})</p>
            </c:if>
    </div>
</body>
</html>
