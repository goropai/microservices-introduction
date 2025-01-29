package com.goropai.resourceservice.repository;

import com.goropai.resourceservice.entity.Mp3File;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ResourceRepository extends JpaRepository<Mp3File, Integer> {
}