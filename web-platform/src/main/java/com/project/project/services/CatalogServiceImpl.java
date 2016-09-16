package com.project.services;

import com.project.dao.catalog.CatalogDAO;

import com.project.entities.Catalog;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

/**
 *
 * @author Home
 */
@Service("catalogService")
@Component
public class CatalogServiceImpl implements CatalogService {
    
    @Autowired
    private CatalogDAO dao;

    @Override
    public Long save(Catalog entity) {
        return dao.save(entity);
    }

    @Override
    public Catalog findById(Long id) {
        return dao.findById(id);
    }

    @Override
    public boolean update(Catalog entity) {
        return dao.update(entity);
    }

    @Override
    public void destroy(Long id) {
        dao.destroy(id);
    }

    @Override
    public List<Catalog> getCatalogList() {
        return dao.getCatalogList();
    }
    
   
}
