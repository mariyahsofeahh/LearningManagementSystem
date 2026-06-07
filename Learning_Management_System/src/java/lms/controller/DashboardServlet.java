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
import lms.db.MongoConnection; // Points to your central cloud credentials helper
import org.bson.Document;

//@WebServlet(urlPatterns = { "/DashboardServlet" }) 
public class DashboardServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        // 1. Strict Session Verification Check
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("userId") == null) {
            response.sendRedirect("login.jsp");
            return;
        }

        // CONVERTED: MongoDB unique Hex Object IDs read cleanly as Strings from session context
        String userId = (String) session.getAttribute("userId");
        String userRole = (String) session.getAttribute("userRole");
        String classCodeParam = request.getParameter("courseId"); // Reads your target layout parameter indicator

        try {
            // =========================================================================
            // SCENARIO A: Dynamic Interior Course Content Processing (Materials & Tasks)
            // =========================================================================
            if (classCodeParam != null && !classCodeParam.trim().isEmpty()) {
                
                // Load core datasets matching this individual classroom layout code tracking indicator
                loadCourseWorkspace(request, classCodeParam);
                
                // Dispatch straight forward down into your interior shared workspace file view
                request.getRequestDispatcher("course-workspace.jsp").forward(request, response);
                return;
            }

            // =========================================================================
            // SCENARIO B: Loading Central Dashboard Course List Matrix Maps
            // =========================================================================
            List<Map<String, String>> enrolledCourses = new ArrayList<>();
            
            MongoDatabase db = MongoConnection.getDatabase();
            MongoCollection<Document> enrollmentCollection = db.getCollection("enrollments");
            MongoCollection<Document> courseCollection = db.getCollection("courses");

            // Step 1: Scan enrollment collection tracking logs for this student string signature
            MongoCursor<Document> enrollCursor = enrollmentCollection.find(eq("student_id", userId)).iterator();
            
            try {
                while (enrollCursor.hasNext()) {
                    String enrolledCode = enrollCursor.next().getString("course_code");
                    
                    // Step 2: Query matching collection metadata info payload for each code log pointer found
                    Document courseDoc = courseCollection.find(eq("course_code", enrolledCode)).first();
                    
                    if (courseDoc != null) {
                        Map<String, String> courseMap = new HashMap<>();
                        courseMap.put("id", courseDoc.getString("course_code")); // Alphanumeric string acts as structural identifier pointer
                        courseMap.put("name", courseDoc.getString("title"));
                        courseMap.put("code", courseDoc.getString("course_code"));
                        enrolledCourses.add(courseMap);
                    }
                }
            } finally {
                enrollCursor.close(); // Clean memory pools
            }
            
            // Pass the extracted dynamic data maps context down to the presentation layer
            request.setAttribute("courses", enrolledCourses);
            
            // =========================================================================
            // ROLE-BASED DYNAMIC VIEW DISPATCHING ENGINE
            // =========================================================================
            if ("lecturer".equalsIgnoreCase(userRole)) {
                request.getRequestDispatcher("dashboard-lecturer.jsp").forward(request, response);
            } else {
                request.getRequestDispatcher("dashboard-student.jsp").forward(request, response);
            }

        } catch (Exception e) {
            System.err.println("Exception caught navigating dashboard router engine workflows:");
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Dashboard operation routing failure.");
        }
    }

    /**
     * Helper routine method that pulls specific sub-module information arrays matching single courses.
     */
    private void loadCourseWorkspace(HttpServletRequest request, String classCode) throws Exception {
        MongoDatabase db = MongoConnection.getDatabase();
        
        // Item 1: Fetch Course Header Meta-Data Label Descriptors
        Document courseDoc = db.getCollection("courses").find(eq("course_code", classCode)).first();
        if (courseDoc != null) {
            request.setAttribute("selectedCourseName", courseDoc.getString("title"));
            request.setAttribute("selectedCourseCode", courseDoc.getString("course_code"));
            request.setAttribute("selectedCourseId", classCode);
        }

        // Item 2: Pull Learning Materials mapped precisely to this Course String Node Pointer
        List<Map<String, String>> materials = new ArrayList<>();
        MongoCursor<Document> matCursor = db.getCollection("learning_materials").find(eq("course_code", classCode)).iterator();
        try {
            while (matCursor.hasNext()) {
                Document doc = matCursor.next();
                Map<String, String> matItem = new HashMap<>();
                matItem.put("id", doc.getObjectId("_id").toString()); // Object IDs map down to text elements nicely
                matItem.put("fileName", doc.getString("file_name"));
                matItem.put("fileType", doc.getString("file_type"));
                matItem.put("uploadDate", doc.getString("upload_date"));
                materials.add(matItem);
            }
        } finally {
            matCursor.close();
        }
        request.setAttribute("materials", materials);

        // Item 3: Pull Task (Assignment) Records mapped precisely to this Course String Node Pointer
        List<Map<String, String>> tasks = new ArrayList<>();
        MongoCursor<Document> taskCursor = db.getCollection("assignments").find(eq("course_id", classCode)).iterator();
        try {
            while (taskCursor.hasNext()) {
                Document doc = taskCursor.next();
                Map<String, String> taskItem = new HashMap<>();
                taskItem.put("id", doc.getObjectId("_id").toString());
                taskItem.put("title", doc.getString("title"));
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