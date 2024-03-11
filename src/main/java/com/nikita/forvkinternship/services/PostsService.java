package com.nikita.forvkinternship.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
public class PostsService {
    @Value("${my.url}")
    public String BASE_URL;

    @Autowired
    private RestTemplate restTemplate;

    public Object getPosts(Long postId){
        String url = BASE_URL + "/api/posts";

        if (postId != 0) {
            url += "?userId=" + postId;
            return restTemplate.getForObject(url, Object.class);
        }

        return restTemplate.exchange(url, HttpMethod.GET, null,
                new ParameterizedTypeReference<List<Object>>() {
                });
    }

    public void savePost(Object post){
        String url = BASE_URL + "/api/posts";
        restTemplate.postForEntity(url, post, String.class);
    }

    public void updatePost(Long postId,Object post){
        String url = BASE_URL + "/api/posts"+"/"+postId;
        restTemplate.put(url, post);
    }

    public void deletePost(Long postId){
        String url = BASE_URL + "/api/posts"+"/"+postId;
        restTemplate.delete(url);
    }

}
