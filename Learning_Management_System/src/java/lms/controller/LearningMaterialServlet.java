package lms.controller;

import lms.service.LearningMaterialService;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.http.Part;

// Nota: @WebServlet dibuang kerana anda menguruskannya melalui web.xml sahaja!
@MultipartConfig(
        fileSizeThreshold = 1024 * 1024 * 2, // 2MB
        maxFileSize = 1024 * 1024 * 50, // 50MB
        maxRequestSize = 1024 * 1024 * 100 // 100MB
)
public class LearningMaterialServlet extends HttpServlet {

    private final LearningMaterialService materialService = new LearningMaterialService();
    private static final String UPLOAD_DIR = "uploads";

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("userId") == null) {
            response.sendRedirect(request.getContextPath() + "/login.jsp");
            return;
        }

        String action = request.getParameter("action");
        String courseCode = request.getParameter("courseId");
        String currentRole = (String) session.getAttribute("userRole");

        if ("view".equals(action)) {
            // DIUBAH: Menggunakan String bagi menyokong format Alphanumeric ObjectId MongoDB
            String id = request.getParameter("id");

            Map<String, String> material = materialService.getMaterialById(id);
            if (material != null) {
                File file = new File(material.get("filePath"));
                response.setContentType(material.get("fileType"));
                response.setHeader("Content-Length", String.valueOf(file.length()));

                // Menyalin fail fizikal dari server folder terus ke paparan browser pengguna
                java.nio.file.Files.copy(file.toPath(), response.getOutputStream());
                return;
            } else {
                response.sendRedirect(request.getContextPath() + "/DashboardServlet?error=notfound");
                return;
            }

        } else if ("delete".equals(action)) {
            if (!"lecturer".equalsIgnoreCase(currentRole)) {
                response.sendRedirect(request.getContextPath() + "/DashboardServlet?error=unauthorized");
                return;
            }
            // DIUBAH: Menggunakan String bagi menyokong format Alphanumeric ObjectId MongoDB
            String id = request.getParameter("id");
            Map<String, String> material = materialService.getMaterialById(id);
            String redirectCourse = courseCode;
            if ((redirectCourse == null || redirectCourse.trim().isEmpty()) && material != null) {
                redirectCourse = material.get("courseCode");
            }

            materialService.deleteMaterial(id);
            if (redirectCourse != null && !redirectCourse.trim().isEmpty()) {
                response.sendRedirect(request.getContextPath() + "/DashboardServlet?courseId=" + redirectCourse + "&success=materialDeleted");
            } else {
                response.sendRedirect(request.getContextPath() + "/DashboardServlet?success=materialDeleted");
            }
            return;
        }

        // Laluan Default: Ambil senarai metadata dokumen dari MongoDB Cloud dan hantar ke JSP
        List<Map<String, String>> materials = (courseCode != null && !courseCode.trim().isEmpty())
                ? materialService.getMaterialsByCourse(courseCode)
                : materialService.getAllMaterials();
        request.setAttribute("materials", materials);
        request.setAttribute("courseId", courseCode);
        request.setAttribute("activePage", "materials");

        if ("lecturer".equalsIgnoreCase(currentRole)) {
            request.getRequestDispatcher("/lecturer/material-lecturer.jsp").forward(request, response);
        } else {
            request.getRequestDispatcher("/student/material-student.jsp").forward(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("userId") == null) {
            response.sendRedirect(request.getContextPath() + "/login.jsp");
            return;
        }

        if (!"lecturer".equalsIgnoreCase((String) session.getAttribute("userRole"))) {
            response.sendRedirect(request.getContextPath() + "/DashboardServlet?error=unauthorized");
            return;
        }

        String courseCode = request.getParameter("courseCode");
        if (courseCode == null || courseCode.trim().isEmpty()) {
            courseCode = request.getParameter("courseId");
        }
        if (courseCode == null || courseCode.trim().isEmpty()) {
            response.sendRedirect(request.getContextPath() + "/DashboardServlet?error=missingcourse");
            return;
        }

        // Membaca lokasi direktori penyimpanan folder di dalam Tomcat tempatan
        String applicationPath = request.getServletContext().getRealPath("");
        String uploadFilePath = applicationPath + File.separator + UPLOAD_DIR;

        File uploadFolder = new File(uploadFilePath);
        if (!uploadFolder.exists()) {
            uploadFolder.mkdirs(); // Bina folder uploads jika belum wujud
        }

        try {
            Part part = request.getPart("file");
            String fileName = part.getSubmittedFileName();
            String contentType = part.getContentType();
            String completePath = uploadFilePath + File.separator + fileName;

            // 1. Simpan fail fizikal (PDF/Video) ke dalam hard drive laptop (Tomcat server)
            part.write(completePath);

            // 2. Hantar maklumat nama fail, jenis fail, dan lokasi path ke Service Layer 
            // untuk disimpan ke dalam pangkalan data cloud MongoDB Atlas
            boolean isStored = materialService.processAndStoreMaterial(courseCode, fileName, contentType, completePath);

            if (isStored) {
                response.sendRedirect(request.getContextPath() + "/DashboardServlet?courseId=" + courseCode + "&success=materialUploaded");
            } else {
                response.sendRedirect(request.getContextPath() + "/DashboardServlet?courseId=" + courseCode + "&error=materialFailed");
            }
        } catch (Exception e) {
            System.err.println("Ralat pemprosesan muat naik di LearningMaterialServlet:");
            e.printStackTrace();
            response.sendRedirect(request.getContextPath() + "/DashboardServlet?courseId=" + courseCode + "&error=materialException");
        }
    }
}
