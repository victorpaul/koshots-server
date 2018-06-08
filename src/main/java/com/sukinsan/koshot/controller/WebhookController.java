package com.sukinsan.koshot.controller;

import org.apache.commons.io.IOUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.logging.Logger;

@RestController
public class WebhookController {

    private Logger log = Logger.getLogger(CommonController.class.getSimpleName());

    @PostMapping("/webhook")
    public ResponseEntity facebookReceiveEvent(HttpServletRequest request) throws IOException {
        String raw = IOUtils.toString(request.getInputStream());

        log.info("**********************************");
        log.info("raw=" + raw);
        log.info("**********************************");

        return new ResponseEntity("EVENT_RECEIVED", HttpStatus.OK);
    }

    @GetMapping("/webhook")
    public ResponseEntity facebookSubscribe(
            @RequestParam("hub.mode") String mode,
            @RequestParam("hub.challenge") String challenge,
            @RequestParam("hub.verify_token") String token
    ) {

        log.info("mode=" + mode);
        log.info("challenge=" + challenge);
        log.info("token=" + token);

        if (!"mit".equals(token)) {
            return new ResponseEntity(HttpStatus.FORBIDDEN);
        }

        return new ResponseEntity(challenge, HttpStatus.OK);
    }

}