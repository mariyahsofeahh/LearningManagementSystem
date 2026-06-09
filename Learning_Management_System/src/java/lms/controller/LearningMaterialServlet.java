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

        String action = request.getParameter("action");

        // Mengambil peranan pengguna (Lecturer / Student) dari parameter atau session
        String currentRole = (request.getParameter("role") != null) ? request.getParameter("role") : "student";

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
                response.sendRedirect("LearningMaterialServlet?role=" + currentRole + "&error=notfound");
                return;
            }

        } else if ("delete".equals(action)) {
            // DIUBAH: Menggunakan String bagi menyokong format Alphanumeric ObjectId MongoDB
            String id = request.getParameter("id");

            materialService.deleteMaterial(id);
            response.sendRedirect("LearningMaterialServlet?role=lecturer&success=true");
            return;
        }

        // Laluan Default: Ambil senarai metadata dokumen dari MongoDB Cloud dan hantar ke JSP
        List<Map<String, String>> materials = materialService.getAllMaterials();
        request.setAttribute("materials", materials);

        if ("lecturer".equalsIgnoreCase(currentRole)) {
            request.getRequestDispatcher("/lecturer/material-lecturer.jsp").forward(request, response);
        } else {
            request.getRequestDispatcher("/student/material-student.jsp").forward(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

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
            boolean isStored = materialService.processAndStoreMaterial(fileName, contentType, completePath);

            if (isStored) {
                response.sendRedirect("LearningMaterialServlet?role=lecturer&success=true");
            } else {
                response.sendRedirect("LearningMaterialServlet?role=lecturer&error=failed");
            }
        } catch (Exception e) {
            System.err.println("Ralat pemprosesan muat naik di LearningMaterialServlet:");
            e.printStackTrace();
            response.sendRedirect("LearningMaterialServlet?role=lecturer&error=exception");
        }
    }
}
