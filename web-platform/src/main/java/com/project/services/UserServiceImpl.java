package com.project.services;

import com.project.entities.User;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import com.project.dao.user.UserDAO;

/**
 *
 * @author Home
 */
@Service("userService")
@Component
//@Transactional
public class UserServiceImpl implements UserService {

    @Autowired
    private UserDAO dao;

    @Override
    public Long getCount() {
        return dao.getCount();
    }


    @Override
    public Long save(User entity) {
        return dao.save(entity);
    }

    @Override
    public boolean delete(Long id) {
        return dao.delete(id);
    }

    @Override
    public boolean update(User entity) {
        return dao.update(entity);
    }

    @Override
    public User getById(Long id) {
        return dao.getById(id);
    }

   

    @Override
    public User getByEmail(String email) {
        return dao.getByEmail(email);
    }

    @Override
    public User userLogin(String email, String password, String ip) {
        return dao.userLogin(email, password, ip);
    }

   

    @Override
    public void updatePassword(Long userId, String password) {
        dao.updatePassword(userId, password);
    }

    @Override
    public void destroy(Long id) {
      dao.destroy(id);
    }

    @Override
    public boolean checkUserEmailForUpdate(Long id, String email) {
        return dao.checkUserEmailForUpdate(id, email);
    }

  


  



}
