package com.Board.Board;

import com.Board.Board.entity.AttachFile;
import com.Board.Board.repository.AttachFileRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Slf4j
@Component
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class FileStore {

    @Value("${file.dir}")
    private String fileDir;
    private final AttachFileRepository attachFileRepository;

    public String getFullPath(String filename) {
            return fileDir + filename;
        }

        public List<AttachFile> storeFiles(List<MultipartFile> files) throws IOException {
            List<AttachFile> storeFileResult = new ArrayList<>();
            if (files != null && files.size() > 0) {
                for (MultipartFile multipartFile : files) {
                    if (!multipartFile.isEmpty()) {
                        storeFileResult.add(storeFile(multipartFile));
                    }
                }
            }
            return storeFileResult;
        }

        public AttachFile storeFile(MultipartFile multipartFile) throws IOException {
            if (multipartFile.isEmpty()) {
                return null;
            }

            String originalFileName = multipartFile.getOriginalFilename();
            String storeFileName = createStoreFileName(originalFileName);
            String fullPath = getFullPath(storeFileName);

            multipartFile.transferTo(new File(fullPath));

            return AttachFile.builder()
                    .originalFileName(originalFileName)
                    .storeFileName(storeFileName)
                    .filePath(fullPath)
                    .build();
//            return new AttachFile(originalFilename, storeFileName, fullPath);
        }

        // 디렉토리 내 파일 삭제
        public void deleteFile(Long fileId){
            AttachFile storeFile = attachFileRepository.findById(fileId).get();
            //저장된 경로로
            String findPath = getFullPath(storeFile.getStoreFileName());

            File targetFile = new File(findPath);
            if (targetFile.exists()){
                targetFile.delete();
                log.info("delete File = {}" , targetFile);
            }
            else {
                log.error("target File not exists!");
            }
        }

        private String createStoreFileName(String originalFilename) {
            String ext = extractExt(originalFilename);
            String uuid = UUID.randomUUID().toString();
            return uuid + "." + ext;
        }

        private String extractExt(String originalFilename) {
            int pos = originalFilename.lastIndexOf(".");
            return originalFilename.substring(pos + 1);
        }

    }
