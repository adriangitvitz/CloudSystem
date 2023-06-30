package com.example.springapi.repositories;

import com.example.springapi.models.entities.Uploadfiles;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface PostgresRepo extends CrudRepository<Uploadfiles, Long> {
    List<Uploadfiles> findUploadfilesByFilename(String filename);
}
