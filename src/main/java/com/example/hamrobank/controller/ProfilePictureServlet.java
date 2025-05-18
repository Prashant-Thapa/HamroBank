package com.example.hamrobank.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Servlet for serving profile pictures
 */
@WebServlet(name = "profilePictureServlet", urlPatterns = {"/profile-picture/*"})
public class ProfilePictureServlet extends HttpServlet {

    private static final Logger LOGGER = Logger.getLogger(ProfilePictureServlet.class.getName());

    // Directory where profile pictures are stored
    private static final String UPLOAD_DIRECTORY = "uploads/profile_pictures";

    // Default profile picture
    private static final String DEFAULT_PROFILE_PICTURE = "default-profile.svg";

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String pathInfo = request.getPathInfo();

        if (pathInfo == null || pathInfo.equals("/") || pathInfo.isEmpty()) {
            // Serve default profile picture
            serveDefaultProfilePicture(request, response);
            return;
        }

        // Remove leading slash
        String fileName = pathInfo.substring(1);

        // Get the upload directory path
        String uploadPath = getServletContext().getRealPath("/") + UPLOAD_DIRECTORY;

        // Create the file path
        Path filePath = Paths.get(uploadPath, fileName);
        File file = filePath.toFile();

        // Check if the file exists
        if (!file.exists() || !file.isFile()) {
            LOGGER.log(Level.WARNING, "Profile picture not found: {0}", fileName);
            serveDefaultProfilePicture(request, response);
            return;
        }

        // Set content type based on file extension
        String contentType = getServletContext().getMimeType(fileName);
        if (contentType == null) {
            contentType = "application/octet-stream";
        }

        // Set response headers
        response.setContentType(contentType);
        response.setContentLength((int) file.length());

        // Serve the file
        try {
            Files.copy(filePath, response.getOutputStream());
            response.getOutputStream().flush();
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Error serving profile picture", e);
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Serve the default profile picture
     *
     * @param request HttpServletRequest
     * @param response HttpServletResponse
     * @throws IOException if an I/O error occurs
     */
    private void serveDefaultProfilePicture(HttpServletRequest request, HttpServletResponse response) throws IOException {
        // Get the default profile picture path
        String defaultPicturePath = getServletContext().getRealPath("/images") + File.separator + DEFAULT_PROFILE_PICTURE;
        File defaultPicture = new File(defaultPicturePath);

        // Check if the default picture exists
        if (!defaultPicture.exists() || !defaultPicture.isFile()) {
            LOGGER.log(Level.WARNING, "Default profile picture not found: {0}", defaultPicturePath);

            // Instead of sending an error, generate a simple colored circle with initials
            generateDefaultAvatar(response);
            return;
        }

        // Set content type
        String contentType = getServletContext().getMimeType(DEFAULT_PROFILE_PICTURE);
        if (contentType == null) {
            contentType = "image/svg+xml";
        }

        // Set response headers
        response.setContentType(contentType);
        response.setContentLength((int) defaultPicture.length());

        // Serve the file
        try {
            Files.copy(defaultPicture.toPath(), response.getOutputStream());
            response.getOutputStream().flush();
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Error serving default profile picture", e);
            generateDefaultAvatar(response);
        }
    }

    /**
     * Generate a simple SVG avatar with a colored circle
     *
     * @param response HttpServletResponse to write the SVG to
     * @throws IOException if an I/O error occurs
     */
    private void generateDefaultAvatar(HttpServletResponse response) throws IOException {
        // Set content type to SVG
        response.setContentType("image/svg+xml");

        // Create a simple SVG with a colored circle and user icon
        String svg = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                     "<svg xmlns=\"http://www.w3.org/2000/svg\" width=\"200\" height=\"200\" viewBox=\"0 0 200 200\">\n" +
                     "  <circle cx=\"100\" cy=\"100\" r=\"100\" fill=\"#0056b3\" />\n" +
                     "  <path d=\"M100,40 C77.909,40 60,57.909 60,80 C60,102.091 77.909,120 100,120 C122.091,120 140,102.091 140,80 C140,57.909 122.091,40 100,40 Z\" fill=\"#ffffff\" />\n" +
                     "  <path d=\"M155,155 C155,129.624 130.376,110 100,110 C69.624,110 45,129.624 45,155 C59.47,175.469 78.674,190 100,190 C121.326,190 140.53,175.469 155,155 Z\" fill=\"#ffffff\" />\n" +
                     "</svg>";

        // Write the SVG to the response
        response.getWriter().write(svg);
    }
}
