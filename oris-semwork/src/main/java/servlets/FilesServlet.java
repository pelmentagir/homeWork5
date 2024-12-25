package servlets;

import models.PhotoFile;
import services.FileService;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

@WebServlet("/file")
public class FilesServlet extends HttpServlet {

    private FileService fileService;

    @Override
    public void init(ServletConfig config) throws ServletException {
        this.fileService = (FileService) config.getServletContext().getAttribute("filesService");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        Long fileId = Long.parseLong(req.getParameter("fileId"));
        PhotoFile file = fileService.getFilesByRecipeId(fileId).get(0);

        File diskFile = new File("C://recipes/photos/" + file.getStorageFileName());
        resp.setContentType(file.getType());
        Files.copy(diskFile.toPath(), resp.getOutputStream());
    }
}