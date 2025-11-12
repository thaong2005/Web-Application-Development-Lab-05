# Nguyễn Việt Thảo - ITCSIU23058 

# Web-Application-Development-Lab-05
LAB 5: SERVLET &amp; MVC PATTERN

Work Flow Explain:

# 1. Add

(1) User enters list page: student-list.jsp

→ student?action=list

(2) Select "➕ Add New Student"

→ URL changes to
student?action=new

(3) Controller (StudentController) reads action=new

→ calls showNewForm()
→ forward to JSP
/views/student-form.jsp

(4) User enters information → Submit → POST

Form sent to:
student?action=insert

(5) doPost() reads action=insert → calls insertStudent()

Get data from form → create Student newStudent → calls DAO

(6) DAO → addStudent() → INSERT into DB

INSERT INTO students (student_code, full_name, email, major) VALUES ...

(7) Success → redirect

response.sendRedirect("student?action=list&message=Student added successfully");

→ use redirect to avoid duplicate submission error (POST–Redirect–GET)

(8) View re-displays list + message

# 2. Edit

(1) User clicks Edit → URL:

student?action=edit&id=3

(2) Controller reads action=edit

→ calls showEditForm()

(3) DAO → getStudentById(id)

Get Student object from DB

(4) Controller setAttribute("student", data) → forward

→ student-form.jsp displays form with old data

→ Student Code is usually readonly

(5) User edits → submit → POST

student?action=update

(6) updateStudent()

Get data → set to Student → calls DAO

(7) DAO → updateStudent() → UPDATE
UPDATE students SET ... WHERE id = ?

(8) Redirect to list + message

student?action=list&message=Student updated successfully

(9) Display list + message

# 3. Delete 
(1) User click Delete → confirm → send request

student?action=delete&id=3

(2) Controller processing

→ call deleteStudent()

(3) DAO → deleteStudent(id)
DELETE FROM students WHERE id = ?

(4) Redirect to list (GET)

student?action=list&message=Student deleted successfully

(5) View displays the new list



