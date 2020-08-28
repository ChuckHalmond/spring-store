package com.mygroup.springstore.service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mygroup.springstore.dao.UserDAO;
import com.mygroup.springstore.exception.DataAccessException;
import com.mygroup.springstore.manager.EncryptionManager;
import com.mygroup.springstore.manager.SessionManager;
import com.mygroup.springstore.mapper.UserMapper;
import com.mygroup.springstore.model.UserModel;
import com.mygroup.springstore.viewmodel.UserFormViewModel;

@Service
public class UserService {

    @Autowired
    private UserDAO userDAO;

    @Autowired
    private SessionManager sessionManager;
    
    @Autowired
    private EncryptionManager encryptionManager;

    public UserFormViewModel getSessionUserFormViewModel() {
        return UserMapper.modelToFormViewModel(
            sessionManager.getUserSession().getUser()
        );
    }
    
    public void createUser(UserFormViewModel ufvm) throws DataAccessException {
        UserModel um = UserMapper.formViewModelToModel(ufvm);

        // Current date
        LocalDateTime ldt = LocalDateTime.now();
        Instant instant = ldt.atZone(ZoneId.systemDefault()).toInstant();
        Date date = Date.from(instant);

        // Set meta data
        um.setActive(true);
        um.setRemoved(false);
        um.setLastModificationDate(date);
        um.setLastModificationDate(date);

        // Encrypt password
        um.setPassword(encryptionManager.encryptMD5(um.getPassword()));

        // Create the user
        userDAO.create(um);
    }

    public void updateSessionUser(UserFormViewModel ufvm) 
            throws DataAccessException {

            // Guarantee that the user id is correct
            ufvm.setId(sessionManager.getUserSession().getUserId());
            
            updateUser(ufvm);
    }
    
     public void updateUser(UserFormViewModel ufvm) 
            throws DataAccessException {

            UserModel um = userDAO.getById(ufvm.getId());

            um = UserMapper.applyFormViewModelOnModel(ufvm, um);

            // Current date
            LocalDateTime ldt = LocalDateTime.now();
            Instant instant = ldt.atZone(ZoneId.systemDefault()).toInstant();
            Date date = Date.from(instant);

            // Set meta data
            um.setLastModificationDate(date);
            
            // Encrypt password
            um.setPassword(encryptionManager.encryptMD5(ufvm.getPassword()));

            // Update the database
            userDAO.update(um);
   
            // Update the session attribute
            sessionManager.getUserSession().setUser(um);
    }
    
    public UserFormViewModel getUserById(int id) throws DataAccessException {
        UserModel um = userDAO.getById(id);

        UserFormViewModel ufvm = UserMapper.modelToFormViewModel(um);

        // Make sure the password is not sent
        ufvm.setPassword("");

        return ufvm;
    }
    
    public void removeSessionUser() throws DataAccessException {
        int getCurrentUserId = sessionManager.getUserSession().getUserId();
        
        userDAO.deleteById(getCurrentUserId);
    }
}
