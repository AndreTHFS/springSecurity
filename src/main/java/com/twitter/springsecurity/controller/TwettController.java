package com.twitter.springsecurity.controller;


import com.twitter.springsecurity.dto.CreateTweetDto;
import com.twitter.springsecurity.dto.FeedDto;
import com.twitter.springsecurity.dto.FeedItemDto;
import com.twitter.springsecurity.entities.Role;
import com.twitter.springsecurity.repository.TweetRepository;
import com.twitter.springsecurity.repository.UserRepository;
import com.twitter.springsecurity.service.TwitterService;
import com.twitter.springsecurity.service.UserService;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.UUID;

@RestController
public class TwettController {

    private final TwitterService twitterService;
    private final UserService userService;

    public TwettController(TwitterService twitterService, UserService userService) {
        this.twitterService = twitterService;
        this.userService = userService;
    }


    @PostMapping("/tweet")
    public ResponseEntity<Void> createTweet(@RequestBody CreateTweetDto dto, JwtAuthenticationToken token){
       if(dto.content().isEmpty()){
           return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
       }
        twitterService.createTweet(dto, token);

        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/tweet/{id}")
    public ResponseEntity<Void> deleteTweeet(@PathVariable("id") Long tweetId, JwtAuthenticationToken token){
        if(token.getName().isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        twitterService.deteteTweet(tweetId, token);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/feed")
    public ResponseEntity<FeedDto> feed(@RequestParam(value = "page", defaultValue = "0") int page,
                                        @RequestParam(value = "pageSize", defaultValue ="10" ) int pageSize){
                var twets = twitterService.feed(page,pageSize);
                return ResponseEntity.ok(twets);
    }
}
