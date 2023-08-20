package exc.Board.domain.board;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Component
public class FileStore {

        @Value("${file.dir}")
        private String fileDir;

        public String getFullPath(String filename) {
            return fileDir + filename;
        }

        public List<AttachFile> storeFiles(List<MultipartFile> multipartFiles) throws IOException {
            List<AttachFile> storeFileResult = new ArrayList<>();
            for (MultipartFile multipartFile : multipartFiles) {
                if (!multipartFile.isEmpty()) {
                    storeFileResult.add(storeFile(multipartFile));
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
