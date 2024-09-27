package com.julio.encurtadorUrl.service;

import com.julio.encurtadorUrl.domain.Url;
import com.julio.encurtadorUrl.repository.UrlRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Random;

@Service
public class UrlService {

    @Autowired
    private UrlRepository repository;

    public String shortenUrl(String originalurl){

        String shortUrl = generateShortUrl();
        Url url = new Url();
        url.setOriginalUrl(originalurl);
        url.setShortUrl(shortUrl);
        url.setExpirationDate(LocalDateTime.now().plusDays(30));
        repository.save(url);

        return shortUrl;
    }

    public Optional<Url> getOriginalUrl(String shortUrl){
        Optional<Url> urlOptional = repository.findByShortUrl(shortUrl);
        if (urlOptional.isPresent()){
            Url url = urlOptional.get();
            if (url.getExpirationDate().isAfter(LocalDateTime.now())){
                return Optional.of(url);
            }
            else {
                repository.delete(url);
            }
        }
        return Optional.empty();
    }

    private String generateShortUrl(){
        String caracteres = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";

        StringBuilder shortUrl = new StringBuilder();
        Random random = new Random();
        int length = 5 + random.nextInt(6);

        for (int i = 0; i < length; i++){
            shortUrl.append(caracteres.charAt(random.nextInt(caracteres.length())));
        }
        return shortUrl.toString();

    }
}
