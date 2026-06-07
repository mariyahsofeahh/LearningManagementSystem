/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package lms.service;

import lms.DAO.UserDAO;
import org.bson.Document;

public class UserService {

    private UserDAO userDAO = new UserDAO();

    public Document login(String email, String password) {
        return userDAO.authenticateUser(email, password);
    }

    public boolean register(String name, String email, String password, String role) {

        if (userDAO.emailExists(email)) {
            return false;
        }

        return userDAO.registerUser(name, email, password, role);
    }
}