package ru.dude.cloudstore.service;

import org.springframework.core.io.Resource;
import ru.dude.cloudstore.dto.FileUploadRequest;

import java.io.IOException;
import java.util.List;

public interface FileService<FileResponse, FileRequest, FileRenameRequest, FileUploadRequest> {
    void upload(FileUploadRequest fileUploadRequest, String filename) throws RuntimeException, IOException;

    List<FileResponse> getFileInfoList(int limit) throws IOException;

    void deleteFile(FileRequest fileRequest) throws RuntimeException, IOException;

    Resource getFileResource(FileRequest fileRequest) throws IOException;

    void renameFile(FileRenameRequest fileRenameRequest) throws RuntimeException, IOException;
}
