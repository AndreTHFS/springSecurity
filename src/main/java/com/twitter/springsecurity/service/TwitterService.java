package com.twitter.springsecurity.service;

import com.twitter.springsecurity.dto.CreateTweetDto;
import com.twitter.springsecurity.dto.FeedDto;
import com.twitter.springsecurity.dto.FeedItemDto;
import com.twitter.springsecurity.entities.Role;
import com.twitter.springsecurity.entities.Tweet;
import com.twitter.springsecurity.repository.TweetRepository;
import com.twitter.springsecurity.repository.UserRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class TwitterService {
    private final TweetRepository tweetRepository;
    private final UserRepository userRepository;


    public TwitterService(TweetRepository tweetRepository, UserRepository userRepository) {
        this.tweetRepository = tweetRepository;
        this.userRepository = userRepository;
    }

    public void createTweet(CreateTweetDto dto, JwtAuthenticationToken token){
        var user = userRepository.findById(UUID.fromString(token.getName()));
        var tweet = new Tweet();
        tweet.setUser(user.get());
        tweet.setContent(dto.content());
        tweetRepository.save(tweet);
    }

    public void deteteTweet(Long idTweet, JwtAuthenticationToken token){
        var user = userRepository.findById(UUID.fromString(token.getName()));
        var isAdmin = user.get().getRoles()
                .stream()
                .anyMatch( role -> role.getName().equalsIgnoreCase(Role.Values.ADMIN.name()));
        if(isAdmin || user.get().getUserId().equals(UUID.fromString(token.getName()))) {
            tweetRepository.deleteById(idTweet);
        }

    }
    public FeedDto feed(int page, int pageSize){
        var tweets = tweetRepository.findAll(
                PageRequest.of(page, pageSize, Sort.Direction.DESC,"creationTimestamp"))
                .map(tweet -> new FeedItemDto(
                    tweet.getTweetId(),
                    tweet.getContent(),
                    tweet.getUser().getUsername()));
                return new FeedDto(tweets.getContent(), page, pageSize, tweets.getTotalPages(), tweets.getTotalElements());
    }
}
