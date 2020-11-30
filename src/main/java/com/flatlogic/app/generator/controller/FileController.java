package com.flatlogic.app.generator.controller;

import com.flatlogic.app.generator.controller.request.MultipartRequest;
import com.flatlogic.app.generator.exception.CreatePathException;
import com.flatlogic.app.generator.exception.DownloadException;
import com.flatlogic.app.generator.exception.UploadException;
import com.flatlogic.app.generator.service.FileService;
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
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

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
     */
    @PostMapping("upload/products/image")
    public ResponseEntity<Void> uploadProductsFile(@Valid MultipartRequest file, BindingResult result) {
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
     */
    @PostMapping("upload/users/avatar")
    public ResponseEntity<Void> uploadUsersFile(@Valid MultipartRequest file, BindingResult result) {
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
     * @return Error message
     */
    @ExceptionHandler(CreatePathException.class)
    public ResponseEntity<String>  handleCreatePathException(CreatePathException e) {
        LOGGER.error("CreatePathException handler.", e);
        return new ResponseEntity<>(e.getMessage(), HttpStatus.CONFLICT);
    }

    /**
     * UploadException handler.
     *
     * @param e UploadException
     * @return Error message
     */
    @ExceptionHandler(UploadException.class)
    public ResponseEntity<String> handleUploadException(UploadException e) {
        LOGGER.error("UploadException handler.", e);
        return new ResponseEntity<>(e.getMessage(), HttpStatus.CONFLICT);
    }

    /**
     * DownloadException handler.
     *
     * @param e DownloadException
     * @return Error message
     */
    @ExceptionHandler(DownloadException.class)
    public ResponseEntity<String>  handleDownloadException(DownloadException e) {
        LOGGER.error("DownloadException handler.", e);
        return new ResponseEntity<>(e.getMessage(), HttpStatus.CONFLICT);
    }

}
