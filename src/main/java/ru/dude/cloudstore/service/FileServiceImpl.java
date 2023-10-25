package ru.dude.cloudstore.service;

import lombok.AllArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import ru.dude.cloudstore.dto.FileRenameRequest;
import ru.dude.cloudstore.dto.FileRequest;
import ru.dude.cloudstore.dto.FileUploadRequest;
import ru.dude.cloudstore.exception.SuccessMessage;
import ru.dude.cloudstore.model.FileResponse;
import ru.dude.cloudstore.repository.FileSystemStorage;

import java.io.IOException;
import java.util.List;


@Service
@AllArgsConstructor
public class FileServiceImpl implements FileService<FileResponse, FileRequest,FileRenameRequest, FileUploadRequest> {

    private final SuccessMessage successMessage;
    private final FileSystemStorage fileSystemStorage;

    @Override
    public void upload(FileUploadRequest fileUploadRequest, String filename) throws RuntimeException, IOException {
        final var currentUsername = getCurrentUserName();
        fileSystemStorage.store(currentUsername, filename, fileUploadRequest.getFile().getBytes());
    }

    @Override
    public List<FileResponse> getFileInfoList(int limit) throws RuntimeException, IOException {
        final var currentUsername = getCurrentUserName();
        return fileSystemStorage.loadFileInfoList(currentUsername, limit);
    }

    @Override
    public String deleteFile(FileRequest fileRequest) throws RuntimeException, IOException {
        final var currentUsername = getCurrentUserName();
        fileSystemStorage.delete(currentUsername, fileRequest.getName());
        return successMessage.getDeleteMessage();
    }


    @Override
    public String renameFile(FileRenameRequest fileRenameRequest) throws RuntimeException, IOException {
        final var currentUsername = getCurrentUserName();
        fileSystemStorage.updateFile(
                currentUsername,
                fileRenameRequest.getToUpdateFilename(),
                fileRenameRequest.getNewFilename());
        return successMessage.getRenameMessage();
    }

    @Override
    public Resource getFileResource(FileRequest fileRequest) throws IOException {
        final var currentUsername = getCurrentUserName();
            return fileSystemStorage.loadAsResource(currentUsername, fileRequest.getName());
    }

    private String getCurrentUserName() {
        final String username = SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();
        if (username == null || username.isEmpty()) {
            throw new RuntimeException();
        }
        return username;
    }
}
