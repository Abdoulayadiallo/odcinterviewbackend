package com.odk.odcinterview.Controller;

import com.odk.odcinterview.Model.Utilisateur;
import com.odk.odcinterview.Service.AccountService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;

@RestController
@RequestMapping("/utilisateur")
public class AccountController {
    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    AccountService accountService;

    @GetMapping("/list")
    public ResponseEntity<?> getUsersList() {
        List<Utilisateur> users = accountService.userList();
        if (users.isEmpty()) {
            return new ResponseEntity<>("Utilisateurs non trouvé.", HttpStatus.OK);
        }
        return new ResponseEntity<>(users, HttpStatus.OK);
    }
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody HashMap<String, String> request) {
        String username = request.get("username");
        if (accountService.findByUsername(username) != null) {
            return new ResponseEntity<>("username existe", HttpStatus.CONFLICT);
        }
        String email = request.get("email");
        if (accountService.findByEmail(email) != null) {
            return new ResponseEntity<>("email existe", HttpStatus.CONFLICT);
        }
        String nom = request.get("nom");
        String prenom = request.get("prenom");
        String numero = request.get("numero");
        String genre = request.get("genre");
        try {
            Utilisateur utilisateur = accountService.saveUser(nom, prenom, email,numero,genre);
            return new ResponseEntity<>(utilisateur, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Une erreur est survenue lors de l'incription", HttpStatus.BAD_REQUEST);
        }

    }

    @PostMapping("/update")
    public ResponseEntity<?> updateProfile(@RequestBody HashMap<String, String> request) {
        String username = request.get("username");
        Utilisateur utilisateur = accountService.findByUsername(username);
        if (utilisateur == null) {
            return new ResponseEntity<>("Utilisateur non trouvé", HttpStatus.NOT_FOUND);
        }
        try {
            accountService.updateUser(utilisateur, request);
            return new ResponseEntity<>(utilisateur, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Une erreur est survenue", HttpStatus.BAD_REQUEST);
        }
    }
    @PostMapping("/changePassword")
    public ResponseEntity<String> changePassword(@RequestBody HashMap<String, String> request) {
        String username = request.get("username");
        Utilisateur utilisateur = accountService.findByUsername(username);
        if (utilisateur == null) {
            return new ResponseEntity<>("Utilisateur non trouvé", HttpStatus.BAD_REQUEST);
        }
        String currentPassword = request.get("currentpassword");
        String newPassword = request.get("newpassword");
        String confirmpassword = request.get("confirmpassword");
        if (!newPassword.equals(confirmpassword)) {
            return new ResponseEntity<>("Mots de passe ne correpondent pas", HttpStatus.BAD_REQUEST);
        }
        String userPassword = utilisateur.getPassword();
        try {
            if (newPassword != null && !newPassword.isEmpty() && !StringUtils.isEmpty(newPassword)) {
                if (bCryptPasswordEncoder.matches(currentPassword, userPassword)) {
                    accountService.updateUserPassword(utilisateur, newPassword);
                }
            } else {
                return new ResponseEntity<>("Mot de passe actuel incorrect", HttpStatus.BAD_REQUEST);
            }
            return new ResponseEntity<>("Mots de passe modifier avec succès!", HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Une erreur est survenue: " + e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/resetPassword/{email}")
    public ResponseEntity<String> resetPassword(@PathVariable("email") String email) {
        Utilisateur utilisateur = accountService.findByEmail(email);
        if (utilisateur == null) {
            return new ResponseEntity<String>("email non trouvé", HttpStatus.BAD_REQUEST);
        }
        accountService.resetPassword(utilisateur);
        return new ResponseEntity<String>("email envoyé!", HttpStatus.OK);
    }

    @PostMapping("/delete")
    public ResponseEntity<String> deleteUser(@RequestBody HashMap<String, String> mapper) {
        String username = mapper.get("username");
        Utilisateur utilisateur = accountService.findByUsername(username);
        accountService.deleteUser(utilisateur);
        return new ResponseEntity<String>("Utilisateur supprimé avec succès!", HttpStatus.OK);
    }
}
