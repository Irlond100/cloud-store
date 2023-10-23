package ru.dude.cloudstore.service;

import org.springframework.core.io.Resource;

import java.io.IOException;
import java.util.List;

public interface FileService<FileResponse, FileRequest, FileUploadRequest> {
    String upload(FileUploadRequest fileUploadRequest) throws RuntimeException, IOException;

    List<FileResponse> getFileInfoList(int limit) throws IOException;

    String deleteFile(FileRequest fileRequest) throws RuntimeException, IOException;

}
