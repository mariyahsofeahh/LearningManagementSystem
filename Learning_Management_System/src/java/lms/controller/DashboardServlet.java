package lms.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

// MongoDB & BSON imports
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.MongoCursor;
import static com.mongodb.client.model.Filters.eq;
import lms.db.MongoConnection;
import org.bson.Document;

public class DashboardServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // 1. Strict Session Verification Check
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("userId") == null) {
            response.sendRedirect(request.getContextPath() + "/login.jsp");
            return;
        }

        String userId = (String) session.getAttribute("userId");
        String userRole = (String) session.getAttribute("userRole");
        String classCodeParam = request.getParameter("courseId");

        try {
            // =========================================================================
// SCENARIO A: Dynamic Interior Course Content Processing (Materials & Tasks)
// =========================================================================
            if (classCodeParam != null && !classCodeParam.trim().isEmpty()) {

                // 1. Gather MongoDB content payload datasets (materials, tasks, course meta)
                loadCourseWorkspace(request, classCodeParam);

                // 2. Dynamic Role-Based Redirection Engine for inside the course workspace
                if ("lecturer".equalsIgnoreCase(userRole)) {
                    request.getRequestDispatcher("/courseDetails.jsp").forward(request, response);
                } else {
                    // Route students to their clean tabular list layout view
                    request.getRequestDispatcher("/student/material-student.jsp").forward(request, response);
                }
                return;
            }

            // =========================================================================
            // SCENARIO B: Loading Central Dashboard Course List Matrix Maps (FIXED)
            // =========================================================================
            List<Map<String, String>> coordinatedCourses = new ArrayList<>();

            MongoDatabase db = MongoConnection.getDatabase();
            MongoCollection<Document> courseCollection = db.getCollection("courses");
            MongoCollection<Document> enrollmentCollection = db.getCollection("enrollments");

            // 🌟 BRANCH POINT: Pull information based entirely on security authorization roles
            if ("lecturer".equalsIgnoreCase(userRole)) {

                // Lecturer Pipeline: Find records where lecturer_id explicitly matches the teacher's profile key
                MongoCursor<Document> lecturerCursor = courseCollection.find(eq("lecturer_id", userId)).iterator();
                try {
                    while (lecturerCursor.hasNext()) {
                        Document courseDoc = lecturerCursor.next();
                        Map<String, String> courseMap = new HashMap<>();
                        // We map "id" to course_code here because your dashboard-lecturer.jsp template calls course.get("id") for the URL parameter
                        courseMap.put("id", courseDoc.getString("course_code"));
                        courseMap.put("name", courseDoc.getString("title"));
                        courseMap.put("code", courseDoc.getString("course_code"));
                        coordinatedCourses.add(courseMap);
                    }
                } finally {
                    lecturerCursor.close();
                }

            } else {

                // Student Pipeline: Scan enrollment collection logs first
                MongoCursor<Document> enrollCursor = enrollmentCollection.find(eq("student_id", userId)).iterator();
                try {
                    while (enrollCursor.hasNext()) {
                        String enrolledCode = enrollCursor.next().getString("course_code");

                        // Query structural metadata parameters for each verified code match block found
                        Document courseDoc = courseCollection.find(eq("course_code", enrolledCode)).first();

                        if (courseDoc != null) {
                            Map<String, String> courseMap = new HashMap<>();
                            courseMap.put("id", courseDoc.getString("course_code"));
                            courseMap.put("name", courseDoc.getString("title"));
                            courseMap.put("code", courseDoc.getString("course_code"));
                            coordinatedCourses.add(courseMap);
                        }
                    }
                } finally {
                    enrollCursor.close();
                }
            }

            // Pass the extracted collection maps list context down to the presentation engine layers uniformally
            request.setAttribute("courses", coordinatedCourses);

            // =========================================================================
            // ROLE-BASED DYNAMIC VIEW DISPATCHING ENGINE
            // =========================================================================
            if ("lecturer".equalsIgnoreCase(userRole)) {
                request.getRequestDispatcher("/lecturer/dashboard-lecturer.jsp").forward(request, response);
            } else {
                request.getRequestDispatcher("/student/dashboard-student.jsp").forward(request, response);
            }

        } catch (Exception e) {
            System.err.println("Exception caught navigating dashboard router engine workflows:");
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Dashboard operation routing failure.");
        }
    }

    private void loadCourseWorkspace(HttpServletRequest request, String classCode) throws Exception {
        MongoDatabase db = MongoConnection.getDatabase();

        // Item 1: Fetch Course Header Meta-Data Label Descriptors
        Document courseDoc = db.getCollection("courses").find(eq("course_code", classCode)).first();
        if (courseDoc != null) {
            lms.model.Course course = new lms.model.Course();
            course.setTitle(courseDoc.getString("title"));
            course.setDescription(courseDoc.getString("description"));
            course.setCourseCode(courseDoc.getString("course_code"));

            request.setAttribute("course", course);

            request.setAttribute("selectedCourseName", courseDoc.getString("title"));
            request.setAttribute("selectedCourseCode", courseDoc.getString("course_code"));
            request.setAttribute("selectedCourseId", classCode);
            request.setAttribute("selectedCourseDesc", courseDoc.getString("description"));
            request.setAttribute("selectedLecturerId", courseDoc.get("lecturer_id") != null ? courseDoc.get("lecturer_id").toString() : "N/A");
        }

        // Item 2: Pull Learning Materials mapped precisely to this Course
        List<Map<String, String>> materials = new ArrayList<>();
        MongoCursor<Document> matCursor = db.getCollection("learning_materials").find(eq("course_code", classCode)).iterator();
        try {
            while (matCursor.hasNext()) {
                Document doc = matCursor.next();
                Map<String, String> matItem = new HashMap<>();
                matItem.put("id", doc.getObjectId("_id").toString());
                matItem.put("fileName", doc.getString("file_name"));
                matItem.put("fileType", doc.getString("file_type"));
                matItem.put("uploadDate", doc.getString("upload_date"));
                materials.add(matItem);
            }
        } finally {
            matCursor.close();
        }
        request.setAttribute("materials", materials);

        // Item 3: Pull Task (Assignment) Records mapped precisely to this Course
        List<Map<String, String>> tasks = new ArrayList<>();
        MongoCursor<Document> taskCursor = db.getCollection("assignments").find(eq("course_code", classCode)).iterator();
        try {
            while (taskCursor.hasNext()) {
                Document doc = taskCursor.next();
                Map<String, String> taskItem = new HashMap<>();
                taskItem.put("id", doc.getObjectId("_id").toString());
                taskItem.put("title", doc.getString("title"));
                taskItem.put("description", doc.getString("description"));
                taskItem.put("dueDate", doc.getString("deadline"));
                tasks.add(taskItem);
            }
        } finally {
            taskCursor.close();
        }
        request.setAttribute("tasks", tasks);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }
}
