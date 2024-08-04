package com.atipera.demo.GitHubRepositories.Config;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {

    @Value("${github.api.url}")
    private String githubApiBaseUrl;

    @Value("${git.api.token}")
    private String gitToken;

    @Bean
    public WebClient gitHubWebClient(WebClient.Builder builder) {
        return builder.baseUrl(githubApiBaseUrl)
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .defaultHeader(HttpHeaders.AUTHORIZATION, "Bearer " + gitToken)
                .build();
    }
}
