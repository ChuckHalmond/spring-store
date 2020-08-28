package com.mygroup.springstore.repository;

import java.util.ArrayList;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.mygroup.springstore.model.StoreModel;

@Repository
public interface StoreRepository extends JpaRepository<StoreModel, Integer>  {
    
    @Query("SELECT sm FROM StoreModel sm JOIN FETCH sm.ownedBy um WHERE um.id = :ownerId")
    public ArrayList<StoreModel> findFromOwnerId(@Param("ownerId") int ownerId);
    
    @Query("SELECT sm FROM StoreModel sm WHERE sm.key = :key")
    public StoreModel findByKey(@Param("key") String key);
}