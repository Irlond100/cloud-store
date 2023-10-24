package ru.dude.cloudstore.repository;


import org.springframework.core.io.Resource;
import ru.dude.cloudstore.model.FileResponse;

import java.io.IOException;
import java.util.List;

public interface FileStore {
    List<FileResponse> loadFileInfoList(String username, int limit) throws IOException;

    void store(String username, String filename, byte[] bytes) throws IOException;

    void delete(String username, String filename) throws IOException;

    Resource loadAsResource(String username, String filename) throws IOException;

    void updateFile(String name, String toUpdateFilename, String newFileName) throws IOException;

}
