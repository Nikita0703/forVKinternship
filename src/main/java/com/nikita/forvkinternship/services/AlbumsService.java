package com.nikita.forvkinternship.services;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AlbumsService {
    @Value("${my.url}")
    public String BASE_URL;

    private final RestTemplate restTemplate;


    public Object getAlbums(Long albumId){

        String url = BASE_URL + "/api/albums";

        if (albumId != 0) {
            url += "?albumId=" + albumId;
            return restTemplate.getForObject(url, Object.class);
        }

        return restTemplate.exchange(url, HttpMethod.GET, null,
                new ParameterizedTypeReference<List<Object>>() {
                });
    }

    public void saveAlbum(Object album){
        String url = BASE_URL + "/api/albums";
        restTemplate.postForEntity(url, album, String.class);
    }

    public void updateAlbum(Long albumId,Object album){
        String url = BASE_URL + "/api/albums"+"/"+albumId;
        restTemplate.put(url, album);
    }

    public void deleteAlbum(Long albumId){
        String url = BASE_URL + "/api/albums"+"/"+albumId;
        restTemplate.delete(url);
    }

}


