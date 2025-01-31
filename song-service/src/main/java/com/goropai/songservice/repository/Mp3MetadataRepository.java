package com.goropai.songservice.repository;

import com.goropai.songservice.entity.Mp3Metadata;
import org.springframework.data.jpa.repository.JpaRepository;

public interface Mp3MetadataRepository extends JpaRepository<Mp3Metadata, Integer> {
}
