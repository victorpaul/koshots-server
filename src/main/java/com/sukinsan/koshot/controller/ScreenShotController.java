package com.sukinsan.koshot.controller;

import com.sukinsan.koshot.response.PublishResponse;
import com.sukinsan.koshot.util.SecurityUtil;

import org.springframework.http.MediaType;
import org.springframework.util.StreamUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;

@RestController
@RequestMapping("/api")
public class ScreenShotController {

    @Autowired
    private SecurityUtil securityUtil;

    @PostMapping("/publish")
    public ResponseEntity publish(@RequestParam("file") MultipartFile file) throws IOException {
        long timestamp = System.currentTimeMillis();
        String fileName = timestamp + "_" + file.getOriginalFilename();
        File newFile = new File(getUploadFolder(), fileName);
        newFile.createNewFile();
        file.transferTo(newFile);

        String secret = securityUtil.getSecret(fileName, timestamp);

        ServletUriComponentsBuilder builder = ServletUriComponentsBuilder.fromCurrentRequestUri();
        String url = builder
                .uri(URI.create("api/screenshot"))
                .replaceQueryParam("sc", secret)
                .replaceQueryParam("fn", fileName)
                .replaceQueryParam("ts", timestamp).build().toString().replaceFirst("//","http://");

        return new ResponseEntity(new PublishResponse(url), HttpStatus.OK);
    }

    @GetMapping(value = "/screenshot", produces = MediaType.IMAGE_JPEG_VALUE)
    public @ResponseBody
    byte[] getScreenShot(
            @RequestParam(value = "sc", defaultValue = "oops") String secret,
            @RequestParam(value = "fn", defaultValue = "oops") String fileName,
            @RequestParam(value = "ts", defaultValue = "0") long timeStamp
    ) throws IOException {

        String generateSecret = securityUtil.getSecret(fileName, timeStamp);
        if (generateSecret.equals(secret)) {
            File outFile = new File(getUploadFolder(), fileName);
            if (outFile.exists()) {
                InputStream in = new FileInputStream(outFile);
                return StreamUtils.copyToByteArray(in);
            }
        }
        return StreamUtils.copyToByteArray(getClass().getClassLoader().getResourceAsStream("mona.jpg"));
    }

    private File getUploadFolder() {
        File uploadFOlder = new File(System.getProperty("user.home"), "koshot/uploads");
        if (!uploadFOlder.exists()) {
            uploadFOlder.mkdirs();
        }
        return uploadFOlder;
    }

}
