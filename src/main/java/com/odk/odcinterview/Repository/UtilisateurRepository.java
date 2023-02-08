package com.odk.odcinterview.Repository;

import com.odk.odcinterview.Model.Postulant;
import com.odk.odcinterview.Model.Role;
import com.odk.odcinterview.Model.Utilisateur;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Repository
public interface UtilisateurRepository extends JpaRepository<Utilisateur,Long> {
    Utilisateur findByUsername(String username);
    boolean existsByUsername(String username);
    Utilisateur findUtilisteurById(Long id);
    List<Utilisateur> findUtilisteurByRole(Role role);
    Utilisateur findByEmail(String userEmail);
    List<Utilisateur> findByUsernameContaining(String username);
    @Query(value = "SELECT utilisateur.* FROM utilisateur,entretien WHERE utilisateur.role_id = 1 AND entretien.id =:idEntretien",nativeQuery = true)
    Page<Utilisateur> findUtilisateurByEntretien(@Param("idEntretien") Long idEntretien, Pageable pageable);

    @Query(value = "select distinct utilisateur.* from  utilisateur,entretien WHERE  (utilisateur.role_id = 1 AND utilisateur.entretien_id=:x) AND (utilisateur.email like %:keyword% or utilisateur.nom like %:keyword% or utilisateur.prenom like %:keyword% or utilisateur.numero like %:keyword%)", nativeQuery = true)
    Page<Utilisateur> findUtilisateurEntretien0rByKeyword(@Param("x") Long id, @RequestParam(value = "keyword", required = false) String keyword, Pageable pageable);

}
