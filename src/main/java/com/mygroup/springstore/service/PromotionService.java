package com.mygroup.springstore.service;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mygroup.springstore.dao.PromotionDAO;
import com.mygroup.springstore.exception.DataAccessException;
import com.mygroup.springstore.mapper.PromotionMapper;
import com.mygroup.springstore.model.PromotionModel;
import com.mygroup.springstore.viewmodel.PromotionFormViewModel;
import com.mygroup.springstore.viewmodel.PromotionListViewModel;

@Service
public class PromotionService {

    @Autowired
    private PromotionDAO promotionDAO;
    
    public void createPromotion(PromotionFormViewModel pfvm) throws DataAccessException {

        PromotionModel sm = PromotionMapper.formViewModelToModel(pfvm);

        promotionDAO.create(sm);
    }
    
    public PromotionFormViewModel getPromotionById(int id) throws DataAccessException {
        PromotionModel pm = promotionDAO.getById(id);

        return PromotionMapper.modelToFormViewModel(pm);
    }
    
    public ArrayList<PromotionFormViewModel> getPromotions() {
        
        ArrayList<PromotionFormViewModel> pfvmList = new ArrayList<PromotionFormViewModel>();

        for (PromotionModel pm : promotionDAO.getAll()) {
            pfvmList.add(PromotionMapper.modelToFormViewModel(pm));
        }
        
        return pfvmList;
    } 
    
    public ArrayList<PromotionListViewModel> getPromotionsList() {
        
        ArrayList<PromotionListViewModel> plvmList = new ArrayList<PromotionListViewModel>();

        for (PromotionModel pm : promotionDAO.getAll()) {
            plvmList.add(PromotionMapper.modelToListViewModel(pm));
        }
        
        return plvmList;
    }
        
    public void updatePromotion(PromotionFormViewModel pfvm) 
            throws DataAccessException {

        // TODO: Retrieve the id from a safer source (session ?)
        PromotionModel pm = promotionDAO.getById(pfvm.getId());
        
        pm = PromotionMapper.applyFormViewModelOnModel(pfvm, pm);

        promotionDAO.update(pm);
    }
    
    public void deletePromotionById(int promotionId) throws DataAccessException {

        // TODO: Retrieve the id from a safer source (session ?)
        promotionDAO.deleteById(promotionId);
    }
}
