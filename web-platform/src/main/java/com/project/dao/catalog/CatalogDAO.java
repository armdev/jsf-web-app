package com.project.dao.catalog;

import com.project.entities.Catalog;
import java.util.List;

public interface CatalogDAO {

    public Long save(Catalog entity);

    public Catalog findById(Long id);

    public boolean update(Catalog entity);

    public void destroy(Long id);

    public List<Catalog> getCatalogList();

}
