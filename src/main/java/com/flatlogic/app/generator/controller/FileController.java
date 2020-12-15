package com.flatlogic.app.generator.controller;

import com.flatlogic.app.generator.controller.request.MultipartRequest;
import com.flatlogic.app.generator.exception.CreatePathException;
import com.flatlogic.app.generator.exception.DownloadException;
import com.flatlogic.app.generator.service.FileService;
import com.flatlogic.app.generator.util.Constants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.io.IOException;

/**
 * FileController REST controller.
 */
@RestController
@RequestMapping("file")
public class FileController {

    /**
     * Logger constant.
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(FileController.class);

    /**
     * String constant.
     */

    /**
     * FileService instance.
     */
    @Autowired
    private FileService fileService;

    /**
     * Download file.
     *
     * @param privateUrl Private url
     * @return Resource
     */
    @GetMapping("download")
    public ResponseEntity<Resource> downloadFile(@RequestParam String privateUrl) {
        LOGGER.info("Download file.");
        return new ResponseEntity<>(fileService.downloadFile(privateUrl), HttpStatus.OK);
    }

    /**
     * Upload products file.
     *
     * @param file   MultipartRequest
     * @param result BindingResult
     * @return Void
     * @throws IOException IOException
     */
    @PostMapping("upload/products/image")
    public ResponseEntity<Void> uploadProductsFile(@Valid MultipartRequest file,
                                                   BindingResult result) throws IOException {
        LOGGER.info("Upload products file.");
        if (result.hasErrors()) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } else {
            fileService.uploadProductsFile(file.getFile(), file.getFilename());
            return new ResponseEntity<>(HttpStatus.OK);
        }
    }

    /**
     * Upload users file.
     *
     * @param file   MultipartRequest
     * @param result BindingResult
     * @return Void
     * @throws IOException IOException
     */
    @PostMapping("upload/users/avatar")
    public ResponseEntity<Void> uploadUsersFile(@Valid MultipartRequest file,
                                                BindingResult result) throws IOException {
        LOGGER.info("Upload users file.");
        if (result.hasErrors()) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } else {
            fileService.uploadUsersFile(file.getFile(), file.getFilename());
            return new ResponseEntity<>(HttpStatus.OK);
        }
    }

    /**
     * CreatePathException handler.
     *
     * @param e CreatePathException
     */
    @ResponseStatus(code = HttpStatus.CONFLICT, reason = Constants.ERROR_CREATE_PATH)
    @ExceptionHandler(CreatePathException.class)
    public void handleCreatePathException(CreatePathException e) {
        LOGGER.error("CreatePathException handler.", e);
    }

    /**
     * DownloadException handler.
     *
     * @param e DownloadException
     */
    @ResponseStatus(code = HttpStatus.CONFLICT, reason = Constants.ERROR_DOWNLOAD_FILE)
    @ExceptionHandler(DownloadException.class)
    public void handleDownloadException(DownloadException e) {
        LOGGER.error("DownloadException handler.", e);
    }

    /**
     * IOException handler.
     *
     * @param e IOException
     */
    @ResponseStatus(code = HttpStatus.CONFLICT, reason = Constants.ERROR_UPLOAD_FILE)
    @ExceptionHandler(IOException.class)
    public void handleIOException(IOException e) {
        LOGGER.error("IOException handler.", e);
    }

}
