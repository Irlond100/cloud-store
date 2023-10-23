package ru.dude.cloudstore.service;

import lombok.AllArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import ru.dude.cloudstore.dto.FileRenameRequest;
import ru.dude.cloudstore.dto.FileRequest;
import ru.dude.cloudstore.dto.FileUploadRequest;
import ru.dude.cloudstore.model.FileResponse;
import ru.dude.cloudstore.repository.FileSystemStorage;
import org.springframework.security.core.context.SecurityContextHolder;

import java.io.IOException;
import java.util.List;


@Service
@AllArgsConstructor
public class FileServiceImpl implements FileService<FileResponse, FileRequest, FileUploadRequest> {

    private final FileSystemStorage fileSystemStorage;

    @Override
    public String upload(FileUploadRequest fileUploadRequest) throws RuntimeException, IOException {
        final var currentUsername = getCurrentUserName();
        fileSystemStorage.store(currentUsername, fileUploadRequest.getFilename(), fileUploadRequest.getFile().getBytes());
        return currentUsername;
    }

    @Override
    public List<FileResponse> getFileInfoList(int limit) throws RuntimeException, IOException {
        final var currentUsername = getCurrentUserName();
        return fileSystemStorage.loadFileInfoList(currentUsername, limit);
    }

    @Override
    public String deleteFile(FileRequest fileRequest) throws RuntimeException, IOException {
        final var currentUsername = getCurrentUserName();
        fileSystemStorage.delete(currentUsername, fileRequest.getFilename());
        return currentUsername;
    }

    private String getCurrentUserName() {
        final String username = SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();
        if (username == null || username.isEmpty()) {
            throw new RuntimeException();
        }
        return username;
    }
}
