package com.Board.Board;

import com.Board.Board.entity.AttachFile;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriUtils;

import java.net.MalformedURLException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
@Slf4j
public class AttachFileController {

    private final AttachFileService attachFileService;
    private final FileStore fileStore;
    private Long boardNo;
    private List<Long> deletionQueue;

    // 다운로드
    @GetMapping("/download/{fileId}")
    public ResponseEntity<UrlResource> downloadFile(@PathVariable Long fileId) {

        // 저장된 천부파일 정보 가져오기
        Optional<AttachFile> attachFile = attachFileService.findAttachFileById(fileId);

        return attachFile.map(file -> {
            String storeFileName = file.getStoreFileName();
            String uploadFileName = file.getOriginalFileName();

            // 절대 파일 경로로 찾기
            UrlResource resource;
            try {
                resource = new UrlResource("file:" + fileStore.getFullPath(storeFileName));
            } catch (MalformedURLException e) {
                throw new RuntimeException(e);
            }
            String encodedUploadFileName = UriUtils.encode(uploadFileName, StandardCharsets.UTF_8);
            String contentDisposition = "attachment; filename=\"" + encodedUploadFileName + "\"";

            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, contentDisposition)
                    .body(resource);
        }).orElse(ResponseEntity.notFound().build());

    }

    // 첨부파일 삭제 - ajax
    @ResponseBody
    @PostMapping("delete/{fileId}")
    public String deleteFile(@PathVariable Long fileId) {
        try {
            // 디렉토리에 실제 파일 삭제
            fileStore.deleteFile(fileId);

            // DB 에서 파일 정보 삭제
            attachFileService.deleteAttachFileById(fileId);
            return "success"; // 삭제 성공 시 "success" 반환
        } catch (Exception e) {
            return "error"; // 삭제 오류 시 "error" 반환
        }

        // 파일 경로의 물리적 파일 삭제

    }

    @ResponseBody
    @PostMapping("/delete/{boardNo}/files")
    public String deleteFiles(@PathVariable Long boardNo,
                              @RequestBody List<Long> deletionQueue) {

        log.info("deletionQueue = {}", deletionQueue);

        try {
            for (Long i : deletionQueue) {
                // 디렉토리에 실제 파일 삭제
                fileStore.deleteFile(i);
                // DB 에서 파일 정보 삭제
                attachFileService.deleteAttachFileById(i);
            }
            return "success"; // 삭제 성공 시 "success" 반환
        } catch (Exception e) {
            return "error"; // 삭제 오류 시 "error" 반환
        }
    }

}
