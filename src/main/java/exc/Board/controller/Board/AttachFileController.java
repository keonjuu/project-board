package exc.Board.controller.Board;

import exc.Board.domain.board.AttachFile;
import exc.Board.domain.board.FileStore;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.UrlResource;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.net.MalformedURLException;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
@Slf4j
public class AttachFileController {

    private final AttachFileService attachFileService;
    private final FileStore fileStore;

    @GetMapping("/download/{fileId}")
    public ResponseEntity<UrlResource> downloadFile(@PathVariable Long fileId) throws MalformedURLException {
        // 저장된 천부파일 정보 가져오기
        Optional<AttachFile> attachFile = attachFileService.findAttachFileById(fileId);

        log.info("attatchFile = {}", attachFile);

 /*       String storeFileName = attachFile.get().getStoreFileName();
        String uploadFileName = attachFile.get().getOriginalFileName();

        UrlResource resource = new UrlResource("file:" + fileStore.getFullPath(storeFileName));
        log.info("uploadFileName={}", uploadFileName);

        String encodedUploadFileName = UriUtils.encode(uploadFileName, StandardCharsets.UTF_8);
        String contentDisposition = "attachment; filename=\"" + encodedUploadFileName + "\"";

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, contentDisposition)
                .body(resource);

    */
        return null;
    }
}
