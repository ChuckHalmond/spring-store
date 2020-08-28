package com.mygroup.springstore.dao;

import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.mygroup.springstore.exception.DataAccessException;
import com.mygroup.springstore.exception.DataAccessException.DAESource;
import com.mygroup.springstore.model.PromotionModel;
import com.mygroup.springstore.repository.PromotionRepository;

@Component
public class PromotionDAO {
    
    private static final Logger logger = LoggerFactory.getLogger(PromotionDAO.class);

    @Autowired
    private PromotionRepository repo;

    public PromotionModel getById(int id) throws DataAccessException {
        if (this.repo.existsById(id)) {
            return this.repo.findById(id).get();
        }
        else {
            String errorMsg = String.format(
                    "Promotion with id = %d doesn't exists!",
                    id
            );
            logger.error(errorMsg);
            throw new DataAccessException(errorMsg, DAESource.PK_MISSING);
        }
    }
    
    public PromotionModel create(PromotionModel pm) throws DataAccessException {
        if (!this.repo.existsById(pm.getId())) {
            if (this.repo.findByKey(pm.getKey()) == null) {
                return this.repo.save(pm);
            }
            else {
                String errorMsg = String.format(
                        "Promotion with key = %s already exists!",
                        pm.getKey()
                );
                logger.error(errorMsg);
                throw new DataAccessException(errorMsg, DAESource.UNIQUE_ATTR_DUP);
            }
        }
        else {
            String errorMsg = String.format(
                    "Promotion with id = %d already exists!",
                    pm.getId()
            );
            logger.error(errorMsg);
            throw new DataAccessException(errorMsg, DAESource.PK_EXISTING);
        }
    }
    
    public PromotionModel update(PromotionModel pm) throws DataAccessException {
        if (this.repo.existsById(pm.getId())) {
            PromotionModel existingWithKey = this.repo.findByKey(pm.getKey());
            if (existingWithKey == null || existingWithKey.getId() == pm.getId()) {
                return this.repo.save(pm);
            }
            else {
                String errorMsg = String.format(
                        "Promotion with key = %s already exists!",
                        pm.getKey()
                );
                logger.error(errorMsg);
                throw new DataAccessException(errorMsg, DAESource.UNIQUE_ATTR_DUP);
            }
        }
        else {
            String errorMsg = String.format(
                    "Promotion with id = %d doesn't exists!",
                    pm.getId()
            );
            logger.error(errorMsg);
            throw new DataAccessException(errorMsg, DAESource.PK_MISSING);
        }
    }

    public ArrayList<PromotionModel> getAll() {
        ArrayList<PromotionModel> pmList = new ArrayList<PromotionModel>();
        
        pmList.addAll(this.repo.findAll());
        
        return pmList;
    }
    
    public void deleteById(int id) throws DataAccessException {
        if (this.repo.existsById(id)) {
            this.repo.deleteById(id);
        }
        else {
            String errorMsg = String.format(
                    "Promotion with id = %d doesn't exists!",
                    id
            );
            logger.error(errorMsg);
            throw new DataAccessException(errorMsg, DAESource.PK_MISSING);
        }
    }
}