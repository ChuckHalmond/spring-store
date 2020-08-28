package com.mygroup.springstore.service;

import static com.mygroup.springstore.viewmodel.UserType.ADMINISTRATOR;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mygroup.springstore.dao.StoreDAO;
import com.mygroup.springstore.exception.AuthorizationException;
import com.mygroup.springstore.exception.DataAccessException;
import com.mygroup.springstore.manager.SessionManager;
import com.mygroup.springstore.mapper.StoreMapper;
import com.mygroup.springstore.model.StoreModel;
import com.mygroup.springstore.model.UserModel;
import com.mygroup.springstore.viewmodel.StoreFormViewModel;
import com.mygroup.springstore.viewmodel.StoreListViewModel;

@Service
public class StoreService {

    private static final Logger logger = LoggerFactory.getLogger(StoreService.class);
        
    @Autowired
    private StoreDAO storeDAO;
    
    @Autowired
    private SessionManager sessionManager;
    
    public void createStore(StoreFormViewModel sfvm) throws DataAccessException {
        UserModel um = sessionManager.getUserSession().getUser();

        StoreModel sm = StoreMapper.formViewModelToModel(sfvm);

        // Current date
        LocalDateTime ldt = LocalDateTime.now();
        Instant instant = ldt.atZone(ZoneId.systemDefault()).toInstant();
        Date date = Date.from(instant);

        // Set meta data
        sm.setLastModifiedDate(date);
        sm.setLastModifiedBy(um);
        sm.setOwnedBy(um);

        storeDAO.create(sm);
    }
    
    public StoreFormViewModel getStoreById(int id) throws DataAccessException {
        StoreModel sm = storeDAO.getById(id);

        return StoreMapper.modelToFormViewModel(sm);
    }
    
    public StoreFormViewModel getStoreByKey(String key) throws DataAccessException {
        StoreModel sm = storeDAO.getByKey(key);

        return StoreMapper.modelToFormViewModel(sm);
    }
    
    public ArrayList<StoreListViewModel> geStoresList() {
        
        ArrayList<StoreListViewModel> slvmList = new ArrayList<StoreListViewModel>();
        
        ArrayList<StoreModel> smList = storeDAO.getAll();

        for (StoreModel sm : smList) {
            slvmList.add(StoreMapper.modelToListViewModel(sm));
        }
        
        return slvmList;
    }
        
    public void updateStore(StoreFormViewModel sfvm) 
            throws DataAccessException, AuthorizationException {
        
        UserModel um = sessionManager.getUserSession().getUser();

        // TODO: Retrieve the id from a safer source (session ?)
        StoreModel sm = storeDAO.getById(sfvm.getId());
        
        sm = StoreMapper.applyFormViewModelOnModel(sfvm, sm);

        // Check if the current user is the owner of the store or an administrator
        if (sm.getOwnedBy().getId() == um.getId() || 
                um.getUserType() == ADMINISTRATOR) {
            
            // Set lastModifiedBy
            sm.setLastModifiedBy(um);

            // Set lastModifiedDate
            LocalDateTime ldt = LocalDateTime.now();
            Instant instant = ldt.atZone(ZoneId.systemDefault()).toInstant();
            Date date = Date.from(instant);

            sm.setLastModifiedDate(date);

            storeDAO.update(sm);
        }
        else {
            String errorMsg = String.format(
                    "User %d has not the right to perform this action.",
                    um.getId()
            );
            logger.error(errorMsg);
            throw new AuthorizationException(errorMsg);
        }
    }
    
    public void deleteStoreById(int storeId) throws DataAccessException,
            AuthorizationException {
        UserModel um = sessionManager.getUserSession().getUser();
        
        StoreModel sm = storeDAO.getById(storeId);
        
        // Check if the current user is the owner of the store or an administrator
        if (sm.getOwnedBy().getId() == um.getId() || 
                um.getUserType() == ADMINISTRATOR) {
            storeDAO.deleteById(storeId);
        }
        else {
            String errorMsg = String.format(
                    "User %d has not the right to perform this action.",
                    um.getId()
            );
            logger.error(errorMsg);
            throw new AuthorizationException(errorMsg);
        }
    }
}
