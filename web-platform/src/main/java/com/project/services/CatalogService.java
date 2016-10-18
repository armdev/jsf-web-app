package com.project.services;


import com.project.entities.Catalog;
import java.util.List;

public interface CatalogService {

    public Long save(Catalog entity);

    public Catalog findById(Long id);

    public boolean update(Catalog entity);

    public void destroy(Long id);

    public List<Catalog> getCatalogList();

}
