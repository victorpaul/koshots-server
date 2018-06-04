package com.sukinsan.koshot.controller;

import com.sukinsan.koshot.response.MessageResponse;
import com.sukinsan.koshot.entity.ShotEntity;
import com.sukinsan.koshot.response.PublishHttpResponse;
import com.sukinsan.koshot.retrofit.Api;
import com.sukinsan.koshot.response.RedmineUserResponse;
import com.sukinsan.koshot.util.SecurityUtil;

import org.springframework.http.MediaType;
import org.springframework.util.StreamUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import retrofit2.Response;

import java.io.*;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@RestController
@RequestMapping("/api")
public class ShotsController {

    @Autowired
    private SecurityUtil securityUtil;

    @Autowired
    private Api api;

    @Deprecated
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
                .replaceQueryParam("ts", timestamp).build().toString().replaceFirst("//", "http://");

        return new ResponseEntity(new PublishHttpResponse(url), HttpStatus.OK);
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

    @PostMapping("/shot")
    public ResponseEntity publishot(@RequestParam("file") MultipartFile file, @RequestHeader("Authorization") String auth) throws IOException {
        Response<RedmineUserResponse> resp = api.redmineLoginBaseAuth(auth);
        if (resp.isSuccessful()) {
            long userID = resp.body().getUser().getId();
            long timestamp = System.currentTimeMillis();
            String fileName = "id" + userID + "_" + timestamp + "_" + new Random().nextInt(1000) + "_" + file.getOriginalFilename();
            File newFile = new File(getUploadFolder(), fileName);
            newFile.createNewFile();
            file.transferTo(newFile);
            return new ResponseEntity(new ShotEntity(fileName, getPublicUrl(fileName, timestamp)), HttpStatus.OK);
        }

        return new ResponseEntity(new MessageResponse("nope"), HttpStatus.UNAUTHORIZED);
    }

    @GetMapping("/shot/{filename}")
    public @ResponseBody
    byte[] getShot(@PathVariable("filename") String fileName, @RequestHeader("Authorization") String auth) throws IOException {
        Response<RedmineUserResponse> resp = api.redmineLoginBaseAuth(auth);
        if (resp.isSuccessful() && isMyFile(resp, fileName)) {

            File outFile = new File(getUploadFolder(), fileName);
            if (outFile.exists()) {
                InputStream in = new FileInputStream(outFile);
                return StreamUtils.copyToByteArray(in);
            }
        }
        return StreamUtils.copyToByteArray(getClass().getClassLoader().getResourceAsStream("mona.jpg"));
    }

    @DeleteMapping("/shot/{filename}")
    public ResponseEntity deleteShot(@PathVariable("filename") String fileName, @RequestHeader("Authorization") String auth) throws IOException {
        Response<RedmineUserResponse> resp = api.redmineLoginBaseAuth(auth);
        if (resp.isSuccessful() && isMyFile(resp, fileName)) {

            String deleteMessages[] = {
                    "Bloody kill",
                    "File located and terminated",
                    "Headshot!",
                    "You are monster",
                    "He was so young",
                    "Whyyyyyy..... khhhh....",
                    "Why did ya do this...."};
            String message = deleteMessages[new Random().nextInt(deleteMessages.length)];
            File outFile = new File(getUploadFolder(), fileName);
            outFile.delete();
            return new ResponseEntity(new MessageResponse(message), HttpStatus.OK);

        }
        return new ResponseEntity(new MessageResponse("nope"), HttpStatus.UNAUTHORIZED);
    }

    @GetMapping("/shot/{filename}/publicurl")
    public ResponseEntity getShotUrl(@PathVariable("filename") String fileName, @RequestHeader("Authorization") String auth) throws IOException {
        Response<RedmineUserResponse> resp = api.redmineLoginBaseAuth(auth);
        if (resp.isSuccessful() && isMyFile(resp, fileName)) {
            String url = getPublicUrl(fileName, System.currentTimeMillis());
            return new ResponseEntity(new ShotEntity(fileName, url), HttpStatus.OK);
        }
        return new ResponseEntity(new MessageResponse("nope"), HttpStatus.UNAUTHORIZED);
    }

    @GetMapping("/shots")
    public ResponseEntity getShots(@RequestHeader("Authorization") String auth) throws IOException {
        Response<RedmineUserResponse> resp = api.redmineLoginBaseAuth(auth);
        if (resp.isSuccessful()) {

            String fileNames[] = getUploadFolder().list((dir, name) -> isMyFile(resp, name));
            List<ShotEntity> fileDatas = new ArrayList<>();
            for (String fileName : fileNames) {
                fileDatas.add(new ShotEntity(fileName, getPublicUrl(fileName, System.currentTimeMillis())));
            }
            return new ResponseEntity(new ShotsResponse(fileDatas), HttpStatus.OK);
        }
        return new ResponseEntity(new MessageResponse("nope"), HttpStatus.UNAUTHORIZED);
    }

    private File getUploadFolder() {
        File uploadFOlder = new File(System.getProperty("user.home"), "koshot/uploads");
        if (!uploadFOlder.exists()) {
            uploadFOlder.mkdirs();
        }
        return uploadFOlder;
    }

    private boolean isMyFile(Response<RedmineUserResponse> resp, String fileName) {
        long userID = resp.body().getUser().getId();
        return fileName.startsWith("id" + userID + "_");
    }

    private String getPublicUrl(String fileName, long timestamp) {
        ServletUriComponentsBuilder builder = ServletUriComponentsBuilder.fromCurrentRequestUri();
        return builder
                .uri(URI.create("api/screenshot"))
                .replaceQueryParam("sc", securityUtil.getSecret(fileName, timestamp))
                .replaceQueryParam("fn", fileName)
                .replaceQueryParam("ts", timestamp)
                .build().toString().replaceFirst("//", "http://");
    }

}
