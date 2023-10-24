package ru.dude.cloudstore.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import ru.dude.cloudstore.dto.FileRenameRequest;
import ru.dude.cloudstore.dto.FileRequest;
import ru.dude.cloudstore.dto.FileUploadRequest;
import ru.dude.cloudstore.service.FileServiceImpl;

import java.io.ByteArrayInputStream;

import static ru.dude.cloudstore.controller.TestConstantHolder.*;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

//public class FileControllerTest {
//    private static FileServiceImpl mockFileServiceImpl;
//    private static MockMvc mockMvc;
//    private static ObjectMapper objectMapper;
//    private static FileRequest fileRequest;
//
//    @BeforeAll
//    public static void setup() {
//        mockFileServiceImpl = Mockito.mock(FileServiceImpl.class);
//        mockMvc = MockMvcBuilders.standaloneSetup(new FileController(mockFileServiceImpl)).build();
//        objectMapper = new ObjectMapper();
//        fileRequest = new FileRequest(TEST_FILE_NAME);
//    }
//
//
//    @Test
//    public void handleFileUploadSuccess() throws Exception {
//        // Arrange
//        final var multipartFile = new MockMultipartFile(
//                "file", TEST_FILE_CONTENT.getBytes());
//        final var fileUploadRequest = new FileUploadRequest(TEST_FILE_NAME, multipartFile);
//        when(mockFileServiceImpl.upload(fileUploadRequest)).thenReturn(TEST_SUCCESS_MESSAGE);
//        // Act
//        final var resultActions = mockMvc.perform(
//                multipart("/file")
//                        .file(multipartFile)
//                        .param("filename", TEST_FILE_NAME));
//        // Assert
//        resultActions.andExpect(status().isOk())
//                .andExpect(content().string(TEST_SUCCESS_MESSAGE));
//        verify(mockFileServiceImpl, times(1)).upload(fileUploadRequest);
//        verifyNoMoreInteractions(mockFileServiceImpl);
//    }
//
//    @Test
//    public void deleteSuccess() throws Exception {
//        // Arrange
//        when(mockFileServiceImpl.deleteFile(eq(fileRequest))).thenReturn(TEST_SUCCESS_MESSAGE);
//        // Act
//        final var resultActions = mockMvc.perform(delete("/file")
//                .contentType(MediaType.APPLICATION_JSON)
//                .param("filename", TEST_FILE_NAME));
//        // Assert
//        resultActions
//                .andExpect(status().isOk())
//                .andExpect(content().string(TEST_SUCCESS_MESSAGE));
//        verify(mockFileServiceImpl, times(1)).deleteFile(fileRequest);
//        verifyNoMoreInteractions(mockFileServiceImpl);
//    }
//
//    @Test
//    public void downloadByLinkKeySuccess() throws Exception {
//        // Arrange
//        final Resource resource = Mockito.mock(Resource.class);
//        when(resource.getInputStream()).thenReturn(new ByteArrayInputStream(TEST_FILE_CONTENT.getBytes()));
//        when(mockFileServiceImpl.getFileResource(fileRequest)).thenReturn(resource);
//        // Act
//        final var resultActions = mockMvc.perform(get("/file")
//                .contentType(MediaType.APPLICATION_JSON)
//                .param("filename", TEST_FILE_NAME));
//        // Assert
//        resultActions
//                .andExpect(status().isOk());
//        verify(mockFileServiceImpl, times(1)).getFileResource(fileRequest);
//        verifyNoMoreInteractions(mockFileServiceImpl);
//    }
//
//
//    @Test
//    public void handleFileRenameSuccess() throws Exception {
//        final var renamedFileRequest = new FileRequest(TEST_FILE_RENAMED_NAME);
//        // Arrange
//        final var fileRenameRequest = new FileRenameRequest();
//        fileRenameRequest.setToUpdateFilename(TEST_FILE_NAME);
//        fileRenameRequest.setNewFilename(renamedFileRequest.getFilename());
//        when(mockFileServiceImpl.renameFile(fileRenameRequest)).thenReturn(TEST_SUCCESS_MESSAGE);
//        // Act
//        final var resultActions = mockMvc.perform(put("/file")
//                .param("filename", TEST_FILE_NAME)
//                .contentType(MediaType.APPLICATION_JSON)
//                .content(objectMapper.writeValueAsString(renamedFileRequest)));
//        // Assert
//        resultActions
//                .andExpect(status().isOk())
//                .andExpect(content().string(TEST_SUCCESS_MESSAGE));
//        verify(mockFileServiceImpl, times(1)).renameFile(fileRenameRequest);
//        verifyNoMoreInteractions(mockFileServiceImpl);
//    }
//}