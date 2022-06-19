package project.toyproject;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

@Component
public class FileUpload {

/*    @Value("${file.dir}")
    private String fileDir;*/

    public String getFullPath(String filename, String fileDir) {
        return fileDir + filename;
    }

    public String serverUploadFile(MultipartFile multipartFile, String fileDir) throws IOException {
        if (multipartFile.isEmpty()) { //파일 없으면 null 반환
            return null;
        }
        String originalFilename = multipartFile.getOriginalFilename(); //원래 파일명

        String serverUploadFileName = createServerFileName(originalFilename);
        multipartFile.transferTo(new File(getFullPath(serverUploadFileName, fileDir)));

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
