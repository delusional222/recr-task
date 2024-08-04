package com.atipera.demo.GitHubRepositories.Model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GithubBranch  {
    private String name;
    private GithubCommit commit;
}
