package com.atipera.demo.GitHubRepositories.Service;


import com.atipera.demo.GitHubRepositories.Dto.GitBranchDTO;
import com.atipera.demo.GitHubRepositories.Dto.RepositoryDTO;
import com.atipera.demo.GitHubRepositories.Exception.UserNotFoundException;
import com.atipera.demo.GitHubRepositories.Model.GithubBranch;
import com.atipera.demo.GitHubRepositories.Model.GithubRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;

import java.util.List;

@Service
public class GithubService {


    private final WebClient gitHubWebClient;

    public static final String GITHUB_REPOS_URI = "/users/{username}/repos";
    public static final String GITHUB_REPOS_BRANCHES_URI = "/repos/{owner}/{repo}/branches";

    @Autowired
    public GithubService(@Qualifier("gitHubWebClient") WebClient gitHubWebClient) {
        this.gitHubWebClient = gitHubWebClient;
    }

    public Mono<List<RepositoryDTO>> getRepositories(String username) {
        return gitHubWebClient.get()
                .uri(GITHUB_REPOS_URI, username)
                .retrieve()
                .bodyToFlux(GithubRepository.class)
                .filter(repo -> !repo.isFork())
                .flatMap(this::mapToRepositoryDTO)
                .collectList()
                .onErrorResume(WebClientResponseException.class, ex -> {
                    if (ex.getStatusCode() == HttpStatus.NOT_FOUND) {
                        return Mono.error(new UserNotFoundException(ex.getStatusCode().value(), ex.getMessage()));
                    }
                    return Mono.error(ex);
                });

    }

    private Mono<RepositoryDTO> mapToRepositoryDTO(GithubRepository repo) {
        return gitHubWebClient.get()
                .uri(GITHUB_REPOS_BRANCHES_URI, repo.getOwner().getLogin(), repo.getName())
                .retrieve()
                .bodyToFlux(GithubBranch.class)
                .map(branch -> new GitBranchDTO(branch.getName(), branch.getCommit().getSha()))
                .collectList()
                .map(branches -> new RepositoryDTO(repo.getName(), repo.getOwner().getLogin(), branches));
    }


}
