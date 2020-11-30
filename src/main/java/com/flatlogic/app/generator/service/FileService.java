package com.flatlogic.app.generator.service;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

public interface FileService {

    Resource downloadFile(String privateUrl);

    void uploadProductsFile(MultipartFile file, String filename);

    void uploadUsersFile(MultipartFile file, String filename);

}
