package com.example.hamrobank.util;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.Part;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Utility class for file operations
 */
public class FileUtil {
    
    private static final Logger LOGGER = Logger.getLogger(FileUtil.class.getName());
    
    // Allowed image file extensions
    private static final String[] ALLOWED_IMAGE_EXTENSIONS = {".jpg", ".jpeg", ".png", ".gif"};
    
    // Maximum file size in bytes (2MB)
    private static final long MAX_FILE_SIZE = 2 * 1024 * 1024;
    
    /**
     * Upload a file to the specified directory
     * 
     * @param request HttpServletRequest containing the file
     * @param partName Name of the file part in the request
     * @param uploadDir Directory to upload the file to
     * @return The filename of the uploaded file, or null if upload failed
     * @throws IOException if an I/O error occurs
     * @throws ServletException if the request is not multipart
     */
    public static String uploadFile(HttpServletRequest request, String partName, String uploadDir) throws IOException, ServletException {
        Part filePart = request.getPart(partName);
        
        // Check if file part exists and is not empty
        if (filePart == null || filePart.getSize() <= 0) {
            return null;
        }
        
        // Check file size
        if (filePart.getSize() > MAX_FILE_SIZE) {
            throw new IOException("File size exceeds the maximum allowed size of 2MB");
        }
        
        // Get file name and extension
        String fileName = getSubmittedFileName(filePart);
        if (fileName == null || fileName.isEmpty()) {
            return null;
        }
        
        String fileExtension = getFileExtension(fileName).toLowerCase();
        
        // Validate file extension
        if (!isAllowedImageExtension(fileExtension)) {
            throw new IOException("Only JPG, JPEG, PNG, and GIF files are allowed");
        }
        
        // Create upload directory if it doesn't exist
        Path uploadPath = Paths.get(uploadDir);
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }
        
        // Generate a unique file name to prevent overwriting
        String uniqueFileName = UUID.randomUUID().toString() + fileExtension;
        String filePath = uploadDir + File.separator + uniqueFileName;
        
        // Write the file
        try {
            filePart.write(filePath);
            LOGGER.log(Level.INFO, "File uploaded successfully: {0}", filePath);
            return uniqueFileName;
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Error uploading file", e);
            throw e;
        }
    }
    
    /**
     * Delete a file from the specified directory
     * 
     * @param fileName Name of the file to delete
     * @param uploadDir Directory containing the file
     * @return true if the file was deleted successfully, false otherwise
     */
    public static boolean deleteFile(String fileName, String uploadDir) {
        if (fileName == null || fileName.isEmpty()) {
            return false;
        }
        
        Path filePath = Paths.get(uploadDir + File.separator + fileName);
        
        try {
            return Files.deleteIfExists(filePath);
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Error deleting file", e);
            return false;
        }
    }
    
    /**
     * Get the submitted file name from a Part
     * 
     * @param part Part containing the file
     * @return The submitted file name
     */
    private static String getSubmittedFileName(Part part) {
        String contentDisposition = part.getHeader("content-disposition");
        String[] items = contentDisposition.split(";");
        
        for (String item : items) {
            if (item.trim().startsWith("filename")) {
                return item.substring(item.indexOf('=') + 1).trim().replace("\"", "");
            }
        }
        
        return null;
    }
    
    /**
     * Get the file extension from a file name
     * 
     * @param fileName Name of the file
     * @return The file extension including the dot
     */
    private static String getFileExtension(String fileName) {
        int lastDotIndex = fileName.lastIndexOf('.');
        if (lastDotIndex > 0) {
            return fileName.substring(lastDotIndex);
        }
        return "";
    }
    
    /**
     * Check if the file extension is allowed for images
     * 
     * @param extension File extension to check
     * @return true if the extension is allowed, false otherwise
     */
    private static boolean isAllowedImageExtension(String extension) {
        for (String allowedExtension : ALLOWED_IMAGE_EXTENSIONS) {
            if (allowedExtension.equalsIgnoreCase(extension)) {
                return true;
            }
        }
        return false;
    }
}
