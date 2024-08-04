package com.atipera.demo.GitHubRepositories.Model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GithubRepository {
    private String name;
    private GithubOwner owner;
    private boolean fork;
}

