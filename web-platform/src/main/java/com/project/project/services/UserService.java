package com.project.services;

import com.project.entities.User;


/**
 *
 * @author Home
 */
public interface UserService {

   
    public User userLogin(String email, String password, String ip);

    public Long getCount();

    public Long save(User entity);

    public void destroy(Long id);

    public boolean delete(Long id);

    public boolean update(User entity);
    
    public User getById(Long id);
    
    public User getByEmail(String email);
    
    public boolean checkUserEmailForUpdate(Long id, String email);
    
    public void updatePassword(Long userId, String password);
}
