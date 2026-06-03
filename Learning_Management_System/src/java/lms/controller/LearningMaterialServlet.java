/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package lms.controller;


import lms.service.LearningMaterialService;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

@WebServlet
@MultipartConfig(
    fileSizeThreshold = 1024 * 1024 * 2,  // 2MB
    maxFileSize = 1024 * 1024 * 50,       // 50MB
    maxRequestSize = 1024 * 1024 * 100    // 100MB
)
public class LearningMaterialServlet extends HttpServlet {
    
    private final LearningMaterialService materialService = new LearningMaterialService();
    private static final String UPLOAD_DIR = "uploads";

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        String action = request.getParameter("action");
        
        // Simulating sequence diagram roles. In production, pull role context from standard HttpSession.
        String currentRole = (request.getParameter("role") != null) ? request.getParameter("role") : "student";
        
        if ("view".equals(action)) {
            // Logic to fetch full file object stream and pipe into browser container window
            int id = Integer.parseInt(request.getParameter("id"));
            Map<String, String> material = materialService.getMaterialById(id);
            if (material != null) {
                File file = new File(material.get("filePath"));
                response.setContentType(material.get("fileType"));
                response.setHeader("Content-Length", String.valueOf(file.length()));
                java.nio.file.Files.copy(file.toPath(), response.getOutputStream());
                return;
            }
        } else if ("delete".equals(action)) {
            int id = Integer.parseInt(request.getParameter("id"));
            materialService.deleteMaterial(id);
            response.sendRedirect("LearningMaterialServlet?role=lecturer&success=true");
            return;
        }

        // Default: Fetch directory metadata and dispatch context forward
        List<Map<String, String>> materials = materialService.getAllMaterials();
        request.setAttribute("materials", materials);
        
        if ("lecturer".equalsIgnoreCase(currentRole)) {
            request.getRequestDispatcher("material-lecturer.jsp").forward(request, response);
        } else {
            request.getRequestDispatcher("material-student.jsp").forward(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        // Maps directly to sequence node: sendUploadRequest()
        String applicationPath = request.getServletContext().getRealPath("");
        String uploadFilePath = applicationPath + File.separator + UPLOAD_DIR;
        
        File uploadFolder = new File(uploadFilePath);
        if (!uploadFolder.exists()) {
            uploadFolder.mkdirs();
        }

        try {
            Part part = request.getPart("file");
            String fileName = part.getSubmittedFileName();
            String contentType = part.getContentType();
            String completePath = uploadFilePath + File.separator + fileName;
            
            // Execute physical file write target to localized server container storage
            part.write(completePath);
            
            // Pass parameters downwards to complete validateMaterial() and storeMaterial() workflows
            boolean isStored = materialService.processAndStoreMaterial(fileName, contentType, completePath);
            
            if (isStored) {
                response.sendRedirect("LearningMaterialServlet?role=lecturer&success=true");
            } else {
                response.sendRedirect("LearningMaterialServlet?role=lecturer&error=failed");
            }
        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect("LearningMaterialServlet?role=lecturer&error=exception");
        }
    }
}