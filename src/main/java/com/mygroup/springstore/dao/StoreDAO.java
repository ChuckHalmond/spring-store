package com.mygroup.springstore.dao;

import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.mygroup.springstore.exception.DataAccessException;
import com.mygroup.springstore.exception.DataAccessException.DAESource;
import com.mygroup.springstore.model.StoreModel;
import com.mygroup.springstore.repository.StoreRepository;

@Component
public class StoreDAO {
    
    private static final Logger logger = LoggerFactory.getLogger(StoreDAO.class);

    @Autowired
    private StoreRepository repo;

    public StoreModel getById(int id) throws DataAccessException {
        if (this.repo.existsById(id)) {
            return this.repo.findById(id).get();
        }
        else {
            String errorMsg = String.format(
                    "Store with id = %d doesn't exists!",
                    id
            );
            logger.error(errorMsg);
            throw new DataAccessException(errorMsg, DAESource.PK_MISSING);
        }
    }
    
    public StoreModel getByKey(String key) throws DataAccessException {
        StoreModel sm = repo.findByKey(key);
        
        if (sm != null) {
            return sm;
        }
        else {
            String errorMsg = String.format(
                    "Store with key = %s doesn't exists!",
                    key
            );
            logger.error(errorMsg);
            throw new DataAccessException(errorMsg, DAESource.ATTR_MISSING);
        }
    }
    
    public StoreModel create(StoreModel sm) throws DataAccessException {
        if (!this.repo.existsById(sm.getId())) {
            if (this.repo.findByKey(sm.getKey()) == null) {
                return this.repo.save(sm);
            }
            else {
                String errorMsg = String.format(
                        "Store with key = %s already exists!",
                        sm.getKey()
                );
                logger.error(errorMsg);
                throw new DataAccessException(errorMsg, DAESource.UNIQUE_ATTR_DUP);
            }
        }
        else {
            String errorMsg = String.format(
                    "Store with id = %d already exists!",
                    sm.getId()
            );
            logger.error(errorMsg);
            throw new DataAccessException(errorMsg, DAESource.PK_EXISTING);
        }
    }
    
    public StoreModel update(StoreModel sm) throws DataAccessException {
        if (this.repo.existsById(sm.getId())) {
            StoreModel existingWithKey = this.repo.findByKey(sm.getKey());
            if (existingWithKey == null || existingWithKey.getId() == sm.getId()) {
                return this.repo.save(sm);
            }
            else {
                String errorMsg = String.format(
                        "Store with key = %s already exists!",
                        sm.getKey()
                );
                logger.error(errorMsg);
                throw new DataAccessException(errorMsg, DAESource.UNIQUE_ATTR_DUP);
            }
        }
        else {
            String errorMsg = String.format(
                    "Store with id = %d doesn't exists!",
                    sm.getId()
            );
            logger.error(errorMsg);
            throw new DataAccessException(errorMsg, DAESource.PK_MISSING);
        }
    }

    public ArrayList<StoreModel> getAll() {
        ArrayList<StoreModel> smList = new ArrayList<StoreModel>();
        
        smList.addAll(this.repo.findAll());
        
        return smList;
    }
    
    public ArrayList<StoreModel> getAllFromOwnerId(int ownerId) {
        ArrayList<StoreModel> smList = this.repo.findFromOwnerId(ownerId);
        
        return (smList == null) ? new ArrayList<StoreModel>() : smList;
    }

    public void deleteById(int id) throws DataAccessException {
        if (this.repo.existsById(id)) {
            this.repo.deleteById(id);
        }
        else {
            String errorMsg = String.format(
                    "Store with id = %d doesn't exists!",
                    id
            );
            logger.error(errorMsg);
            throw new DataAccessException(errorMsg, DAESource.PK_MISSING);
        }
    }
}
