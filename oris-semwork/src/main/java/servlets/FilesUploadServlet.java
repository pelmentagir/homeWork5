package servlets;

import services.FileService;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import java.io.IOException;

@WebServlet("/upload-file")
@MultipartConfig
public class FilesUploadServlet extends HttpServlet {

    private FileService fileService;

    @Override
    public void init(ServletConfig config) throws ServletException {
        this.fileService = (FileService) config.getServletContext().getAttribute("filesService");
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Long recipeId = (Long) req.getAttribute("recipeId");
        Part filePart = (Part) req.getAttribute("file");

        System.out.println("File received in FilesUploadServlet");
        System.out.println("Recipe ID: " + recipeId);
        System.out.println("File name: " + filePart.getSubmittedFileName());
        System.out.println("File size: " + filePart.getSize());

        if (recipeId != null) {
            fileService.uploadFile(
                    recipeId,
                    filePart.getInputStream(),
                    filePart.getSubmittedFileName(),
                    filePart.getContentType(),
                    filePart.getSize()
            );
        }

        resp.sendRedirect("/recipes?id=" + recipeId);
    }
}