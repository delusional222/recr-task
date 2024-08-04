package com.atipera.demo.GitHubRepositories.Controller;


import com.atipera.demo.GitHubRepositories.Dto.RepositoryDTO;
import com.atipera.demo.GitHubRepositories.Service.GithubService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/github")
@RequiredArgsConstructor
public class GithubRepositoriesController {

    private final GithubService gitHubService;

    @GetMapping("/repositories/{username}")
    public ResponseEntity<List<RepositoryDTO>> getRepositories(@PathVariable String username) {
        return gitHubService.getRepositories(username)
                .map(ResponseEntity::ok)
                .block();

    }
}
