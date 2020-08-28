package com.mygroup.springstore.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.mygroup.springstore.model.PromotionModel;

public interface PromotionRepository extends JpaRepository<PromotionModel, Integer> {

    @Query("SELECT pm FROM PromotionModel pm WHERE pm.key = :key")
    public PromotionModel findByKey(@Param("key") String key);
}
