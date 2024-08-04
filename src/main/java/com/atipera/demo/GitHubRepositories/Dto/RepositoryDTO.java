package com.atipera.demo.GitHubRepositories.Dto;


import java.util.List;

public record RepositoryDTO(
        String repositoryName,
        String ownerLogin,
        List<GitBranchDTO> branches
) {
}