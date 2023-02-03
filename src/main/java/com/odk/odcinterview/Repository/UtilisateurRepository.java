package com.odk.odcinterview.Repository;

import com.odk.odcinterview.Model.Critere;
import com.odk.odcinterview.Model.Participant;
import com.odk.odcinterview.Model.Role;
import com.odk.odcinterview.Model.Utilisateur;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UtilisateurRepository extends JpaRepository<Utilisateur,Long> {
    Utilisateur findByUsername(String username);
    boolean existsByUsername(String username);
    Utilisateur findUtilisteurById(Long id);
    List<Utilisateur> findUtilisteurByRole(Role role);
    Utilisateur findByEmail(String userEmail);
    List<Utilisateur> findByUsernameContaining(String username);
    Utilisateur findUtilisateurByParticipant(Participant participant);
    
}
