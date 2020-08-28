package com.mygroup.springstore.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.mygroup.springstore.model.UserModel;

@Repository
public interface UserRepository extends JpaRepository<UserModel, Integer>  {
    
    @Query("SELECT um FROM UserModel um WHERE um.email = :email")
    public UserModel findByEmail(@Param("email") String email);
}
