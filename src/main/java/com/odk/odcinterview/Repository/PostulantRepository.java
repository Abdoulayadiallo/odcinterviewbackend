package com.odk.odcinterview.Repository;

import com.odk.odcinterview.Model.Critere;
import com.odk.odcinterview.Model.Postulant;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PostulantRepository extends JpaRepository<Postulant,Long> {
    Postulant findPostulantById(Long id);
    boolean existsPostulantByEmail(String email);

    Page<Postulant> findByGenreContaining(String genre, Pageable pageable);
   // Page<Postulant> findByNomOrPrenomContaining(String nomOrprenom, Pageable pageable);

}
