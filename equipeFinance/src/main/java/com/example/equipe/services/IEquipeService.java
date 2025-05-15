package com.example.equipe.services;

import com.example.equipe.entities.Equipe;

import java.util.List;

public interface IEquipeService {

    List<Equipe> findEquipesByProjectId(int projectId);
}
