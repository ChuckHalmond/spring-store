package com.mygroup.springstore.dao;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.mygroup.springstore.exception.DataAccessException;
import com.mygroup.springstore.exception.DataAccessException.DAESource;
import com.mygroup.springstore.model.UserModel;
import com.mygroup.springstore.repository.UserRepository;

@Component
public class UserDAO {

    private static final Logger logger = LoggerFactory.getLogger(UserDAO.class);
        
    @Autowired
    private UserRepository repo;

    public UserModel getById(int id) throws DataAccessException {
        if (this.repo.existsById(id)) {
            return this.repo.findById(id).get();
        }
        else {
            String errorMsg = String.format(
                    "User with id = %d doesn't exist!",
                    id
            );
            logger.error(errorMsg);
            throw new DataAccessException(errorMsg, DAESource.PK_MISSING);
        }
    }
    
   public UserModel create(UserModel um) throws DataAccessException {
        if (!this.repo.existsById(um.getId())) {
            if (this.repo.findByEmail(um.getEmail()) == null) {
                return this.repo.save(um);
            }
            else {
                String errorMsg = String.format(
                        "User with email = %s already exists!",
                        um.getEmail()
                );
                logger.error(errorMsg);
                throw new DataAccessException(errorMsg, DAESource.UNIQUE_ATTR_DUP);
            }
        }
        else {
            String errorMsg = String.format(
                    "User with id = %d already exists!",
                    um.getId()
            );
            logger.error(errorMsg);
            throw new DataAccessException(errorMsg, DAESource.PK_EXISTING);
        }
    }
    
    public UserModel update(UserModel um) throws DataAccessException {
        if (this.repo.existsById(um.getId())) {
            UserModel existingWithEmail = this.repo.findByEmail(um.getEmail());
            if (existingWithEmail == null || existingWithEmail.getId() == um.getId()) {
                return this.repo.save(um);
            }
            else {
                String errorMsg = String.format(
                        "User with email = %s already exists!",
                        um.getEmail()
                );
                logger.error(errorMsg);
                throw new DataAccessException(errorMsg, DAESource.UNIQUE_ATTR_DUP);
            }
        }
        else {
            String errorMsg = String.format(
                    "User with id = %d doesn't exists!",
                    um.getId()
            );
            logger.error(errorMsg);
            throw new DataAccessException(errorMsg, DAESource.PK_MISSING);
        }
    }

    public void deleteById(int id) throws DataAccessException {
        if (this.repo.existsById(id)) {
            this.repo.deleteById(id);
        }
        else {
            String errorMsg = String.format(
                    "User with id = %d doesn't exist!",
                    id
            );
            logger.error(errorMsg);
            throw new DataAccessException(errorMsg, DAESource.PK_MISSING);
        }
    }

    // Specific query
    
    public UserModel getByEmail(String email) throws DataAccessException {
        UserModel um = this.repo.findByEmail(email);
        
        if (um != null) {
            return um;
        }         
        else {
            String errorMsg = String.format(
                    "User with email = %s doesn't exist!",
                    email
            );
            logger.error(errorMsg);
            throw new DataAccessException(errorMsg, DAESource.PK_MISSING);
        }
    }
    
    public boolean existsByEmail(String email) {
        UserModel um = this.repo.findByEmail(email);
        
        return (um != null);
    }
}