package com.odk.odcinterview.Service;

import com.odk.odcinterview.Model.Role;
import com.odk.odcinterview.Model.Utilisateur;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;


public interface AccountService {
    Utilisateur saveUser(String nom,String prenom, String email,String numero, String genre);
    Utilisateur saveAdmin(Utilisateur utilisateur);
    Role saveRole(Role role);
    public void updateUserPassword(Utilisateur utilisateur, String newpassword);

    public Utilisateur updateUser(Utilisateur utilisateur, HashMap<String, String> request);
    public void deleteUser(Utilisateur utilisateur);

    public void resetPassword(Utilisateur utilisateur);

    public List<Utilisateur> getUsersListByUsername(String username);


    Utilisateur findByUsername(String username);

    List<Utilisateur> userList();

    Utilisateur findByEmail(String userEmail);
}
