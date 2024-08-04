package com.atipera.demo;

import com.atipera.demo.GitHubRepositories.Dto.GitBranchDTO;
import com.atipera.demo.GitHubRepositories.Dto.RepositoryDTO;
import com.atipera.demo.GitHubRepositories.Exception.UserNotFoundException;
import com.atipera.demo.GitHubRepositories.Model.GithubBranch;
import com.atipera.demo.GitHubRepositories.Model.GithubCommit;
import com.atipera.demo.GitHubRepositories.Model.GithubOwner;
import com.atipera.demo.GitHubRepositories.Model.GithubRepository;
import com.atipera.demo.GitHubRepositories.Service.GithubService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.List;

import static org.mockito.Mockito.when;
import static org.mockito.BDDMockito.given;


@ExtendWith(MockitoExtension.class)
public class GithubServiceTest {

    @Mock
    private WebClient gitHubWebClient;

    @Mock
    private WebClient.RequestHeadersUriSpec requestHeadersUriSpec;

    @Mock
    private WebClient.RequestHeadersSpec requestHeadersSpec;

    @Mock
    private WebClient.ResponseSpec responseSpec;

    @InjectMocks
    private GithubService githubService;

    private final String username = "testuser";
    private final String repoName = "testrepo";


    @BeforeEach
    public void setUp() {
        when(gitHubWebClient.get()).thenReturn(requestHeadersUriSpec);
        when(requestHeadersUriSpec.uri(ArgumentMatchers.anyString(), ArgumentMatchers.any(Object[].class))).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.bodyToFlux(ArgumentMatchers.<Class<GithubRepository>>any())).thenReturn(Flux.empty());
        when(responseSpec.bodyToFlux(ArgumentMatchers.<Class<GithubBranch>>any())).thenReturn(Flux.empty());
    }

    @Test
    public void getRepositories_success() {
        GithubRepository mockRepo = new GithubRepository();
        mockRepo.setName(repoName);
        mockRepo.setFork(false);
        GithubOwner owner = new GithubOwner();
        owner.setLogin(username);
        mockRepo.setOwner(owner);

        GithubBranch mockBranch = new GithubBranch("main", new GithubCommit("sha123"));
        RepositoryDTO mockRepositoryDTO = new RepositoryDTO(repoName, username, List.of(new GitBranchDTO("main", "sha123")));

        given(responseSpec.bodyToFlux(GithubRepository.class)).willReturn(Flux.just(mockRepo));
        given(responseSpec.bodyToFlux(GithubBranch.class)).willReturn(Flux.just(mockBranch));

        Mono<List<RepositoryDTO>> result = githubService.getRepositories(username);

        StepVerifier.create(result)
                .expectNext(List.of(mockRepositoryDTO))
                .verifyComplete();
    }

    @Test
    public void getRepositories_userNotFound() {
        WebClientResponseException exception = WebClientResponseException.create(HttpStatus.NOT_FOUND.value(), "Not Found", null, null, null);
        given(responseSpec.bodyToFlux(GithubRepository.class)).willReturn(Flux.error(exception));

        Mono<List<RepositoryDTO>> result = githubService.getRepositories(username);

        StepVerifier.create(result)
                .expectError(UserNotFoundException.class)
                .verify();
    }

    @Test
    public void getRepositories_otherError() {
        WebClientResponseException exception = WebClientResponseException.create(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Internal Server Error", null, null, null);
        given(responseSpec.bodyToFlux(GithubRepository.class)).willReturn(Flux.error(exception));

        Mono<List<RepositoryDTO>> result = githubService.getRepositories(username);

        StepVerifier.create(result)
                .expectError(WebClientResponseException.class)
                .verify();
    }
}