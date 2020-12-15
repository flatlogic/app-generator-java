package com.flatlogic.app.generator.service.impl;

import com.flatlogic.app.generator.exception.CreatePathException;
import com.flatlogic.app.generator.exception.DownloadException;
import com.flatlogic.app.generator.repository.FileRepository;
import com.flatlogic.app.generator.service.FileService;
import com.flatlogic.app.generator.util.Constants;
import com.flatlogic.app.generator.util.MessageCodeUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Stream;

/**
 * FileService service.
 */
@Service
public class FileServiceImpl implements FileService {

    /**
     * Logger constant.
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(FileServiceImpl.class);

    /**
     * String constant.
     */
    private static final String UPLOAD_LOCATION = "upload";

    /**
     * String constant.
     */
    private static final String FOLDER_SEPARATE = "/";

    /**
     * String constant.
     */
    private static final String PRODUCT_LOCATION = UPLOAD_LOCATION + FOLDER_SEPARATE + "products";

    /**
     * String constant.
     */
    private static final String USER_LOCATION = UPLOAD_LOCATION + FOLDER_SEPARATE + "users";

    /**
     * String constant.
     */
    private static final String IMAGE_LOCATION = PRODUCT_LOCATION + FOLDER_SEPARATE + "image";

    /**
     * String constant.
     */
    private static final String AVATAR_LOCATION = USER_LOCATION + FOLDER_SEPARATE + "avatar";

    /**
     * FileRepository instance.
     */
    @Autowired
    private FileRepository fileRepository;

    /**
     * MessageCodeUtil instance.
     */
    @Autowired
    private MessageCodeUtil messageCodeUtil;

    /**
     * PostConstruct method.
     */
    @PostConstruct
    public void init() {
        try {
            Path root = Paths.get(UPLOAD_LOCATION);
            if (Files.notExists(root)) {
                Files.createDirectory(root);
            }
            Path product = Paths.get(PRODUCT_LOCATION);
            if (Files.notExists(product)) {
                Files.createDirectory(product);
            }
            Path user = Paths.get(USER_LOCATION);
            if (Files.notExists(user)) {
                Files.createDirectory(user);
            }
            Path image = Paths.get(IMAGE_LOCATION);
            if (Files.notExists(image)) {
                Files.createDirectory(image);
            }
            Path avatar = Paths.get(AVATAR_LOCATION);
            if (Files.notExists(avatar)) {
                Files.createDirectory(avatar);
            }
        } catch (IOException e) {
            throw new CreatePathException(messageCodeUtil.getFullErrorMessageByBundleCode(
                    Constants.MSG_FILE_FOLDER_FOR_UPLOAD));
        }
    }

    /**
     * Download file.
     *
     * @param privateUrl File private url
     * @return Resource
     */
    @Override
    public Resource downloadFile(final String privateUrl) {
        try {
            Path file = Paths.get(UPLOAD_LOCATION).resolve(privateUrl);
            Resource resource = new UrlResource(file.toUri());
            if (resource.exists() || resource.isReadable()) {
                return resource;
            } else {
                throw new DownloadException(messageCodeUtil.getFullErrorMessageByBundleCode(
                        Constants.MSG_FILE_DOWNLOAD_FILE, new Object[]{privateUrl}));
            }
        } catch (MalformedURLException e) {
            LOGGER.error(e.getMessage(), e);
            throw new DownloadException(messageCodeUtil.getFullErrorMessageByBundleCode(
                    Constants.MSG_FILE_DOWNLOAD_FILE, new Object[]{privateUrl}));
        }

    }

    /**
     * Upload products file.
     *
     * @param file     MultipartFile
     * @param filename File name
     * @throws IOException IOException
     */
    @Override
    public void uploadProductsFile(final MultipartFile file, final String filename) throws IOException {
        FileCopyUtils.copy(file.getBytes(),
                new File(IMAGE_LOCATION + FOLDER_SEPARATE + filename));
    }

    /**
     * Upload users file.
     *
     * @param file     MultipartFile
     * @param filename File name
     * @throws IOException IOException
     */
    @Override
    public void uploadUsersFile(final MultipartFile file, final String filename) throws IOException {
        FileCopyUtils.copy(file.getBytes(),
                new File(AVATAR_LOCATION + FOLDER_SEPARATE + filename));
    }
}
