package com.julio.encurtadorUrl.controller;

import com.julio.encurtadorUrl.domain.Url;
import com.julio.encurtadorUrl.service.UrlService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/url")
public class UrlController {

    @Autowired
    private UrlService service;

    @PostMapping
    public ResponseEntity<Map<String,String>> shortenUrl (@RequestBody Map<String, String> request) {
        String originalUrl = request.get("url");
        String shortUrl = service.shortenUrl(originalUrl);
        Map<String, String> response = new HashMap<>();
        response.put("url", "https://google.com/"+shortUrl);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{shortUrl}")
    public ResponseEntity<Object> redirectToOriginalUrl(@PathVariable String shortUrl){
        Optional<Url> urlOptional = service.getOriginalUrl(shortUrl);
        if (urlOptional.isPresent()){
            Url url = urlOptional.get();
            System.out.println("Redirecionando para: " + url.getOriginalUrl());
            return ResponseEntity.status(200).location(URI.create(url.getOriginalUrl())).build();
        }
        System.out.println("Not found");
        return ResponseEntity.notFound().build();
    }

}
