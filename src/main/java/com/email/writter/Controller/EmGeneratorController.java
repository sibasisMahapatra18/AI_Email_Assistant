package com.email.writter.Controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.email.writter.Model.EmailRequest;
import com.email.writter.Services.EmGeneratorService;

import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/api/email")
@AllArgsConstructor
@CrossOrigin(origins = "*")
public class EmGeneratorController {
    
 private EmGeneratorService emGeneratorService;


    @PostMapping("/generate")
    public ResponseEntity<String> generateEmail(@RequestBody EmailRequest emailRequest){ {
        String response=emGeneratorService.genrateEmReply(emailRequest);
        return ResponseEntity.ok(response);
    }




    }

}
