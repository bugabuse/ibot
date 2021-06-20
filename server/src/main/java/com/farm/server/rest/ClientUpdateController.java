package com.farm.server.rest;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import javax.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PathVariable;
import java.net.MalformedURLException;
import com.farm.server.core.util.FileUtils;
import org.springframework.web.servlet.HandlerMapping;

@RestController
public class ClientUpdateController {
    private static final Logger logger = LoggerFactory.getLogger(ClientUpdateController.class);

    @RequestMapping("/download/**")
    public ResponseEntity<Resource> downloadFile(HttpServletRequest request) {
        try{
        //Path filePath = Paths.get(String.format("./server-files/{}",fileName));
        String fileName = (String)request.getAttribute( HandlerMapping.PATH_WITHIN_HANDLER_MAPPING_ATTRIBUTE );
        System.out.format("bot requests file: %s%n", fileName);

        fileName = fileName.replaceAll("/download/", "/");
        File file = FileUtils.getFile("files/"+fileName);
        
        if (!file.exists()){
            logger.info("file does not exist: " + file);
            return null;
        }

        // Load file as Resource
        Resource resource = new UrlResource(file.toURI());

        // Try to determine file's content type
        String contentType = null;
        try {
            contentType = request.getServletContext().getMimeType(resource.getFile().getAbsolutePath());
        } catch (IOException ex) {
            logger.info("Could not determine file type.");
        }

        // Fallback to the default content type if type could not be determined
        if(contentType == null) {
            contentType = "application/octet-stream";
        }

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
                .body(resource);
        }
        catch (MalformedURLException ex) {
            logger.error("Error: {}", ex);
            return null;
        }
    }

    @RequestMapping("/version")
    public ResponseEntity<Resource> version(HttpServletRequest request) {
        try{

        File file = FileUtils.getFile("files/versioncheck.html");

        // Load file as Resource
        Resource resource = new UrlResource(file.toURI());

        // Try to determine file's content type
        String contentType = null;
        try {
            contentType = request.getServletContext().getMimeType(resource.getFile().getAbsolutePath());
        } catch (IOException ex) {
            logger.info("Could not determine file type.");
        }

        // Fallback to the default content type if type could not be determined
        if(contentType == null) {
            contentType = "application/octet-stream";
        }

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
                .body(resource);
        }
        catch (MalformedURLException ex) {
            logger.error("Error: {}", ex);
            return null;
        }
    }
}
