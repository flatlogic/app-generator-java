package com.flatlogic.app.generator.service.impl;

import com.flatlogic.app.generator.exception.CreatePathException;
import com.flatlogic.app.generator.exception.DownloadException;
import com.flatlogic.app.generator.exception.UploadException;
import com.flatlogic.app.generator.repository.FileRepository;
import com.flatlogic.app.generator.service.FileService;
import com.flatlogic.app.generator.type.BelongsToType;
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
                    Constants.ERROR_MSG_FILE_CREATE_FOLDER));
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
                        Constants.ERROR_MSG_FILE_DOWNLOAD_FILE, new Object[]{privateUrl}));
            }
        } catch (MalformedURLException e) {
            throw new DownloadException(messageCodeUtil.getFullErrorMessageByBundleCode(
                    Constants.ERROR_MSG_FILE_DOWNLOAD_FILE, new Object[]{privateUrl}));
        }

    }

    /**
     * Upload products file.
     *
     * @param file     MultipartFile
     * @param filename File name
     */
    @Override
    public void uploadProductsFile(final MultipartFile file, final String filename) {
        try {
            FileCopyUtils.copy(file.getBytes(),
                    new File(IMAGE_LOCATION + FOLDER_SEPARATE + filename));
        } catch (IOException e) {
            throw new UploadException(messageCodeUtil.getFullErrorMessageByBundleCode(
                    Constants.ERROR_MSG_FILE_UPLOAD_FILE, new Object[]{filename}));
        }
    }

    /**
     * Upload users file.
     *
     * @param file     MultipartFile
     * @param filename File name
     */
    @Override
    public void uploadUsersFile(final MultipartFile file, final String filename) {
        try {
            FileCopyUtils.copy(file.getBytes(),
                    new File(AVATAR_LOCATION + FOLDER_SEPARATE + filename));
        } catch (IOException e) {
            throw new UploadException(messageCodeUtil.getFullErrorMessageByBundleCode(
                    Constants.ERROR_MSG_FILE_UPLOAD_FILE, new Object[]{filename}));
        }
    }

    /**
     * Remove legacy files.
     */
    @Scheduled(cron = "${scheduled.remove.legacy.files}")
    public void removeLegacyFiles() {
        LOGGER.info("Remove legacy files.");
        Arrays.asList(IMAGE_LOCATION, AVATAR_LOCATION).forEach(path -> {
            removeFilesFromDisk(path);
            removeEntriesFromTable(path);
        });
    }

    private void removeFilesFromDisk(String path) {
        try (Stream<Path> pathStream = Files.walk(Paths.get(path))) {
            pathStream.filter(Files::isRegularFile).forEach(file -> {
                boolean result = fileRepository.existsFileByPrivateUrl(file.toString()
                        .substring(UPLOAD_LOCATION.length() + 1));
                if (!result) {
                    try {
                        Files.delete(file);
                    } catch (IOException e) {
                        LOGGER.error(e.getMessage(), e);
                    }
                }
            });
        } catch (IOException e) {
            LOGGER.error(e.getMessage(), e);
        }
    }

    private void removeEntriesFromTable(String path) {
        List<com.flatlogic.app.generator.entity.File> files = fileRepository.findFilesByBelongsTo(
                Objects.equals(path, IMAGE_LOCATION) ? BelongsToType.PRODUCTS.getType()
                        : BelongsToType.USERS.getType());
        files.forEach(entry -> {
            try (Stream<Path> pathStream = Files.walk(Paths.get(path))) {
                Optional<Path> any = pathStream.filter(file -> Files.isRegularFile(file) &&
                        Objects.equals(entry.getPrivateUrl(), file.toString()
                                .substring(UPLOAD_LOCATION.length() + 1))).findAny();
                if (any.isEmpty()) {
                    fileRepository.delete(entry);
                }
            } catch (IOException e) {
                LOGGER.error(e.getMessage(), e);
            }
        });
    }

}
