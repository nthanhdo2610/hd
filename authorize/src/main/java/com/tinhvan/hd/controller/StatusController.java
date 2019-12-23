package com.tinhvan.hd.controller;

import com.tinhvan.hd.base.HDController;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class StatusController extends HDController {

    /**
     * get status authorize service
     *
     *
     * @return http status code
     */
    @GetMapping(value = "/status")
    public ResponseEntity<?> checkStatus() {
        return ok();
    }
}
