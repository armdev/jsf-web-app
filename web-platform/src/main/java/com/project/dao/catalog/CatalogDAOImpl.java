package com.project.dao.catalog;

import com.project.controllers.exceptions.NonexistentEntityException;
import com.project.entities.Catalog;
import java.util.Date;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityNotFoundException;
import javax.persistence.Persistence;
import javax.persistence.Query;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

/**
 *
 * @author Home
 */
@Component
@Repository("catalogDAO")
public class CatalogDAOImpl implements CatalogDAO {

    private static final org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(CatalogDAOImpl.class);

    private final EntityManagerFactory emf;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public CatalogDAOImpl() {
        emf = Persistence.createEntityManagerFactory("smsysPU");
    }

    @Override
    public Long save(Catalog entity) {
        Date currentDate = new Date(System.currentTimeMillis());
        EntityManager em = getEntityManager();
        Long id = 0L;
        try {
            em.getTransaction().begin();
            entity.setInsertDate(currentDate);
            em.persist(entity);
            em.flush();
            em.getTransaction().commit();
        } catch (Exception e) {
        }
        return id;
    }

    @Override
    public Catalog findById(Long id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Catalog.class, id);
        } finally {
            em.close();
        }
    }

    @Override
    public boolean update(Catalog entity) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            entity = em.merge(entity);
            
            em.getTransaction().commit();

        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Long id = entity.getId();
                if (findById(id) == null) {

                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
        return true;
    }

    @Override
    public void destroy(Long id){
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Catalog entity = null;
            try {
                entity = em.getReference(Catalog.class, id);
                entity.getId();
            } catch (EntityNotFoundException enfe) {
              //  throw new NonexistentEntityException("The Catalog with id " + id + " no longer exists.", enfe);
            }
            em.remove(entity);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    @Override
    public List<Catalog> getCatalogList() {
        EntityManager em = getEntityManager();
        List<Catalog> finalList = null;
        try {
            em.getTransaction().begin();
            Query query = em.createQuery("SELECT o FROM Catalog o WHERE o.id != null ORDER BY o.insertDate DESC");

            finalList = query.getResultList();

            if (finalList == null) {
                return null;
            }
            em.getTransaction().commit();

        } catch (Exception e) {
        } finally {
            em.close();
        }

        return finalList;
    }

}
