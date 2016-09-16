package com.project.dao.user;

import com.project.controllers.exceptions.NonexistentEntityException;

import com.project.entities.User;


import com.project.utils.CommonConstants;

import com.project.utils.CommonUtils;
import java.util.Date;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityNotFoundException;
import javax.persistence.Persistence;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

/**
 *
 * @author Home
 */
@Component
@Repository("userDAO")
public class UserDAOImpl implements UserDAO {

    private static final org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(UserDAOImpl.class);

    private EntityManagerFactory emf;

   

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public UserDAOImpl() {

        emf = Persistence.createEntityManagerFactory("smsysPU");
    }

    @Override
    public User userLogin(String email, String password, String ip) {
        EntityManager em = getEntityManager();
        User entity = null;
        try {
            em.getTransaction().begin();
            Query query = em.createQuery("SELECT c FROM User c WHERE c.email=?1 AND c.passwd=?2")
                    .setParameter(1, email)
                    .setParameter(2,
                            CommonUtils.hashPassword(password));
            //  .setParameter(3, CommonConstants.ACTIVATED);

            if (query.getSingleResult() != null) {
                entity = (User) query.getSingleResult();
            }
            em.getTransaction().commit();

            
        } catch (Exception e) {
         
            

            System.out.println("No result, method login " + e.getLocalizedMessage());
        } finally {
            em.close();
        }
        return entity;
    }

    
    

    @Override
    public Long getCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<User> rt = cq.from(User.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult());
        } finally {
            em.close();
        }

    }

  
   
  
    @Override
    public Long save(User entity) {
        Date currentDate = new Date(System.currentTimeMillis());
        EntityManager em = getEntityManager();
        Long id = 0L;
        try {
            em.getTransaction().begin();
            String hashedPasswd = CommonUtils.hashPassword(entity.getPasswd());
            entity.setStatus(CommonConstants.ACTIVATED);
            entity.setRole(CommonConstants.USER);
            entity.setPasswd(hashedPasswd);
            entity.setRegistereddate(new Date(System.currentTimeMillis()));
            entity.setLastupdate(new Date(System.currentTimeMillis()));        
        
            em.persist(entity);
            em.flush();

            id = entity.getId();
           
            em.getTransaction().commit();

           
        } catch (Exception e) {
            e.printStackTrace();

        }
        return id;
    }

    public void destroy(Long id) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            User entity = null;
            try {
                entity = em.getReference(User.class, id);
                entity.getId();
            } catch (EntityNotFoundException enfe) {
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
    public boolean delete(Long id) {
        this.destroy(id);
        return true;
    }

    @Override
    public boolean update(User entity) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            entity.setLastupdate(new Date(System.currentTimeMillis()));
          
            entity = em.merge(entity);
            em.flush();

            em.getTransaction().commit();

           
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Long id = entity.getId();
                if (findUser(id) == null) {
                    try {
                        throw new NonexistentEntityException("The user with id " + id + " no longer exists.");
                    } catch (NonexistentEntityException ex1) {

                    }
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

 
  

    private User findUser(Long id) {
        EntityManager em = getEntityManager();
        try {
            User entity = em.find(User.class, id);
            
            return entity;
        } finally {
            em.close();
        }
    }

    @Override
    public User getById(Long id) {
        EntityManager em = getEntityManager();
        try {
            User entity = em.find(User.class, id);
           
            return entity;
        } finally {
            em.close();
        }
    }

    @Override
    public User getByEmail(String email) {
        EntityManager em = getEntityManager();
        User entity = null;
        try {
            em.getTransaction().begin();
            Query query = em.createQuery("SELECT c FROM User c WHERE c.email=:email").setParameter("email", email);
            entity = (User) query.getSingleResult();

            em.getTransaction().commit();
            if (entity == null) {
                return null;
            }
        } catch (Exception e) {
            return null;
        }
        return entity;
    }

    @Override
    public boolean checkUserEmailForUpdate(Long id, String email) {
        EntityManager em = getEntityManager();
        boolean retValue = false;

        try {
            em.getTransaction().begin();
            Query query = em.createQuery("SELECT c FROM User c WHERE c.email=:email and c.id != :id").setParameter("email", email).setParameter("id", id);
            User entity = (User) query.getSingleResult();
            em.getTransaction().commit();
            if (entity != null) {
                retValue = true;
            }
        } catch (Exception e) {
        }
        return retValue;
    }

 

    private void updateLastLogin(final User entity) {
        //Runnable taskAsync = new Runnable() {
        Runnable taskAsync = () -> {
            try {
                EntityManager em = getEntityManager();
                em.getTransaction().begin();
                log.info("Update user last login");
                entity.setLastlogindate(new Date(System.currentTimeMillis()));
                em.merge(entity);
                em.flush();
                em.getTransaction().commit();
            } catch (Exception ex) {
                //handle error which cannot be thrown back
            }
        };
        new Thread(taskAsync, "UserUpdate").start();
    }

    @Override
    public void updatePassword(Long userId, String password) {
        EntityManager em = getEntityManager();
        try {
            em.getTransaction().begin();
            int executeUpdate = em.createQuery("UPDATE User o SET o.passwd=?1 WHERE o.id=?2").setParameter(1, CommonUtils.hashPassword(password)).setParameter(2, userId).executeUpdate();
            em.getTransaction().commit();
        } catch (Exception e) {

        }
    }

    //@Override
    private void disableUser(String ip) {
        EntityManager em = getEntityManager();
        try {
            em.getTransaction().begin();
            int executeUpdate = em.createQuery("UPDATE User o SET o.status=?1 WHERE o.ip=?2").setParameter(1, CommonConstants.BLOCKED)
                    .setParameter(2, ip).executeUpdate();
            em.getTransaction().commit();
        } catch (Exception e) {

        }
    }

 

   
   




  

}
