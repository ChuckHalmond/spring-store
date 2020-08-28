/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mygroup.springstore.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.mygroup.springstore.model.AddressModel;

@Repository
public interface AddressRepository extends JpaRepository<AddressModel, Integer> {
    
}