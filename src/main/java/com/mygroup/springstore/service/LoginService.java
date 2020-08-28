package com.mygroup.springstore.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mygroup.springstore.dao.UserDAO;
import com.mygroup.springstore.exception.BusinessException;
import com.mygroup.springstore.exception.DataAccessException;
import com.mygroup.springstore.manager.SessionManager;
import com.mygroup.springstore.mapper.UserMapper;
import com.mygroup.springstore.model.UserModel;
import com.mygroup.springstore.viewmodel.LoginFormViewModel;
import com.mygroup.springstore.viewmodel.UserFormViewModel;

@Service
public class LoginService {

    private static final Logger logger = LoggerFactory.getLogger(LoginService.class);
    
    @Autowired
    private UserDAO userDAO;
    
    @Autowired
    private SessionManager sessionService;

    public UserFormViewModel login(LoginFormViewModel vm) throws BusinessException,
            DataAccessException {
        UserModel um = userDAO.getByEmail(vm.getEmail());

        if (um != null && um.getPassword().equals(vm.getPassword())) {
            sessionService.getUserSession().setUser(um);

            return UserMapper.modelToFormViewModel(um);
        }
        else {
            String errorMsg = "Wrong login information, login impossible";
            logger.error(errorMsg);
            throw new BusinessException(errorMsg, null);
        }
    }
        
    public void logout() {
        sessionService.invalidateSession();
    }
}
