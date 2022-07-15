package project.toyproject.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

@Component
public class FileUpload {
    /**
     * @param filename: 서버에 업로드되는 파일명
     * @param fileDir: 파일이 저장되는 경로
     */
    public String getFullPath(String filename, String fileDir) {
        return fileDir + filename;
    }

    public String serverUploadFile(MultipartFile multipartFile, String fileDir) throws IOException {
        if (multipartFile.isEmpty()) { //파일 없으면 null 반환
            return null;
        }
        String originalFilename = multipartFile.getOriginalFilename(); //원래 파일명

        String serverUploadFileName = createServerFileName(originalFilename); //uuid 생성해서 뒤에 원래파일명의 확장자명 붙이기
        multipartFile.transferTo(new File(getFullPath(serverUploadFileName, fileDir)));//저장: (서버에 업로드되는 파일명, 업로드 되는 경로)

        return serverUploadFileName;
    }

    //uuid 생성해서 뒤에 원래파일명의 확장자명 붙이기
    private String createServerFileName(String originalFilename) {
        String uuid = UUID.randomUUID().toString();
        String ext = extractExt(originalFilename);
        return uuid + "." + ext;
    }

    //원래 파일명에서 확장자 뽑기(.jpg, .png ...)
    private String extractExt(String originalFilename) {
        int pos = originalFilename.lastIndexOf(".");
        return originalFilename.substring(pos + 1);
    }
}
