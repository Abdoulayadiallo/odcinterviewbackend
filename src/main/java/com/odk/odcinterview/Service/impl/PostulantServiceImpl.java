package com.odk.odcinterview.Service.impl;

import com.odk.odcinterview.Model.*;
import com.odk.odcinterview.Payload.NombreResponse;
import com.odk.odcinterview.Payload.PostulantResponse;
import com.odk.odcinterview.Repository.EntretienRepository;
import com.odk.odcinterview.Repository.PostulantRepository;
import com.odk.odcinterview.Service.PostulantService;
import com.odk.odcinterview.util.EmailConstructor;
import com.odk.odcinterview.util.ExcelHelper;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;

@Service
@AllArgsConstructor
public class PostulantServiceImpl implements PostulantService {
    private final PostulantRepository postulantRepository;
    private final EntretienRepository entretienRepository;
    private final JavaMailSender mailSender;
    private final EmailConstructor emailConstructor;



    @Override
    public Postulant savePostulant(Postulant postulant, Long idEntretien) {
        Entretien entretien = entretienRepository.findEntretienById(idEntretien);
        Date date = new Date();
        postulant.setEntretien(entretien);
        mailSender.send(emailConstructor.constructNewPostulantEmail(postulant,entretien));
        return postulantRepository.save(postulant);
    }

    @Override
    public Postulant updatePostulant(Postulant postulant, Long id) {
        Postulant postulant1 = postulantRepository.findPostulantById(id);
        postulant1.setNumero(postulant.getNumero());
        postulant1.setNom(postulant.getNom());
        postulant1.setPrenom(postulant.getPrenom());
        postulant1.setEmail(postulant.getEmail());
        postulant1.setGenre(postulant.getGenre());
        return postulantRepository.save(postulant1);
    }

    @Override
    public void deletePostulant(Postulant postulant) {
        postulantRepository.delete(postulant);
    }

    @Override
    public PostulantResponse readPostulants(int pageNo,int pageSize,String sortBy, String sortDir, String genre,String keyword) {
        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(pageNo,pageSize,sort);
        Page<Postulant> postulants;
        if(genre == null && keyword==null)
            postulants = postulantRepository.findAll(pageable);
        else if(genre != null && keyword == null)
            postulants = postulantRepository.findByGenreContaining(genre, pageable);
        else
            postulants = postulantRepository.findByKeyword(keyword,pageable);

        List<Postulant> postulants1 = postulants.getContent();
        PostulantResponse postulantResponse = new PostulantResponse();
        postulantResponse.setContenu(postulants1);
        postulantResponse.setPageNo(postulants.getNumber());
        postulantResponse.setPageSize(postulants.getSize());
        postulantResponse.setTotalPages(postulants.getTotalPages());
        postulantResponse.setTotalElements(postulants.getTotalElements());
        postulantResponse.setLast(postulants.isLast());
        postulantResponse.setGenre(genre);
        postulantResponse.setKeyword(keyword);
        return postulantResponse;

    }

    @Override
    public PostulantResponse readPostulantsBYENTRETIEN(Long idEntretien,int pageNo, int pageSize, String sortBy, String sortDir, String genre, String keyword) {
        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(pageNo,pageSize,sort);
        Page<Postulant> postulants;
        Entretien entretien = entretienRepository.findEntretienById(idEntretien);
        if( keyword==null)
            postulants = postulantRepository.findPostulantByEntretien(entretien,pageable);
        else
            postulants = postulantRepository.findPostulantEntretien0rByKeyword(idEntretien,keyword,pageable);

        List<Postulant> postulants1 = postulants.getContent();
        PostulantResponse postulantResponse = new PostulantResponse();
        postulantResponse.setContenu(postulants1);
        postulantResponse.setPageNo(postulants.getNumber());
        postulantResponse.setPageSize(postulants.getSize());
        postulantResponse.setTotalPages(postulants.getTotalPages());
        postulantResponse.setTotalElements(postulants.getTotalElements());
        postulantResponse.setLast(postulants.isLast());
        postulantResponse.setGenre(genre);
        postulantResponse.setKeyword(keyword);
        return postulantResponse;    }

    @Override
    public Postulant readPostulantByid(Long id) {
        return postulantRepository.findPostulantById(id);
    }

    @Override
    public void ImportPostulant(MultipartFile multipartFile,Long idEntretien) {
        Entretien entretien = entretienRepository.findEntretienById(idEntretien);
        try {
            List<Postulant>  postulants = ExcelHelper.excelToPostulants(multipartFile.getInputStream());
            final Long[] id = {0L};
            postulants.forEach(postulant -> {
                id[0] = id[0] +1;
                LocalDate date = LocalDate.now();
                Date date1= new Date();
                String numeroMatricule = "ODCI"+ id[0]+date.getYear();
                postulant.setEntretien(entretien);
                postulant.setNumeroMTCL(numeroMatricule);
                postulant.setDateCreation(date1);
               // mailSender.send(emailConstructor.constructNewPostulantEmail(postulant,entretien));
            });
            postulantRepository.saveAll(postulants);
        }catch (IOException e){
            throw new RuntimeException("Echec du stockage de données excels",e);
        }
    }

    @Override
    public ByteArrayInputStream ExportPostulant() {
        List<Postulant> postulants = postulantRepository.findAll();
        ByteArrayInputStream inputStream = ExcelHelper.postulantsToExcel(postulants);
        return inputStream;
    }

    @Override
    public Postulant validerPostulant(Long idPostulant) {
        Postulant postulant = postulantRepository.findPostulantById(idPostulant);
        postulant.setDecisionFinal(DesisionFinal.Accepter);
        return postulantRepository.save(postulant);
    }

    @Override
    public Postulant refuserPostulant(Long idPostulant) {
        Postulant postulant = postulantRepository.findPostulantById(idPostulant);
        postulant.setDecisionFinal(DesisionFinal.Refuser);
        return postulantRepository.save(postulant);    }

    @Override
    public boolean existsPostulantByEmail(String email) {
        return postulantRepository.existsPostulantByEmail(email);
    }


    @Override
    public NombreResponse getNombre(String genre, Long idEntretien) {
        Entretien entretien = entretienRepository.findEntretienById(idEntretien);
        List<Postulant> postulants = entretien.getPostulants();
        int nombreTotale=postulants.size();
        NombreResponse nombreResponse = new NombreResponse();
        int nombreGenre = 0;
        for (Postulant postulant:postulants) {
            System.out.println(postulant);
            try{
            if(postulant.getGenre().equals(genre)){
                nombreGenre = nombreGenre + 1;
                System.out.println(nombreGenre);
            }
            }catch (Exception e){

            }
        }
        try {
            System.out.println(nombreTotale);
            System.out.println(nombreGenre);

            nombreResponse.setNombreParGenre(nombreGenre);
            nombreResponse.setTotalListe(nombreTotale);
            nombreResponse.setPourcentage(Math.round((nombreGenre*100f)/nombreTotale));
            nombreResponse.setContenu(postulants);
        }catch (Exception e){
            e.getMessage();
        }

        return nombreResponse;
    }
    @Override
    public NombreResponse getNombreAllPostulant(String genre) {
        List<Postulant> postulants = postulantRepository.findAll();
        int nombreTotale=postulants.size();
        NombreResponse nombreResponse = new NombreResponse();
        int nombreGenre = 0;
        for (Postulant postulant:postulants) {
            System.out.println(postulant);
            try{
            if(postulant.getGenre().equals(genre)){
                nombreGenre = nombreGenre + 1;
                System.out.println(nombreGenre);
            }
            }catch (Exception e){

            }
        }
        try {
            System.out.println(nombreTotale);
            System.out.println(nombreGenre);

            nombreResponse.setNombreParGenre(nombreGenre);
            nombreResponse.setTotalListe(nombreTotale);
            nombreResponse.setPourcentage(Math.round((nombreGenre*100f)/nombreTotale));
            nombreResponse.setContenu(postulants);
        }catch (Exception e){
            e.getMessage();
        }

        return nombreResponse;
    }

    @Override
    public PostulantResponse findPostulantsByUtilisateur(Long idEntretien, Long idUtilisateur,int pageNo, int pageSize, String sortBy, String sortDir, String keyword) {
        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(pageNo,pageSize,sort);
        Page<Postulant> postulants;
        Entretien entretien = entretienRepository.findEntretienById(idEntretien);
        if( keyword==null)
            postulants = postulantRepository.findPostulantEntretienAndUtilisateur(idEntretien,idUtilisateur,pageable);
        else
            postulants = postulantRepository.findPostulantEntretienAndUtilisateurOrKeyword(idEntretien,idUtilisateur,keyword,pageable);

        List<Postulant> postulants1 = postulants.getContent();
        PostulantResponse postulantResponse = new PostulantResponse();
        postulantResponse.setContenu(postulants1);
        postulantResponse.setPageNo(postulants.getNumber());
        postulantResponse.setPageSize(postulants.getSize());
        postulantResponse.setTotalPages(postulants.getTotalPages());
        postulantResponse.setTotalElements(postulants.getTotalElements());
        postulantResponse.setLast(postulants.isLast());
        postulantResponse.setKeyword(keyword);
        return postulantResponse;
    }
}
