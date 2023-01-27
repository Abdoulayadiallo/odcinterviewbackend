package com.odk.odcinterview.Service.impl;

import com.odk.odcinterview.Model.*;
import com.odk.odcinterview.Payload.PostulantResponse;
import com.odk.odcinterview.Repository.EntretienRepository;
import com.odk.odcinterview.Repository.PostulantRepository;
import com.odk.odcinterview.Service.PostulantService;
import com.odk.odcinterview.util.EmailConstructor;
import com.odk.odcinterview.util.ExcelHelper;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
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
        Participant participant= new Participant();
        participant.setNom(postulant.getNom());
        participant.setPrenom(postulant.getPrenom());
        participant.setEmail(postulant.getEmail());
        participant.setStatus(Estatus.Postulant);
        postulant.setParticipant(participant);
        postulant.setDateCreation(date);
        List<Participant> participants= new ArrayList<>();
        participants.add(participant);
        entretien.setParticipants(participants);
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
    public PostulantResponse readPostulants(int pageNo,int pageSize,String sortBy, String sortDir, String genre,String nomOrprenom) {
        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(pageNo,pageSize,sort);
        Page<Postulant> postulants;
        if(genre == null && nomOrprenom==null)
            postulants = postulantRepository.findAll(pageable);
        else if(genre != null && nomOrprenom == null)
            postulants = postulantRepository.findByGenreContaining(genre, pageable);
        else
            postulants = postulantRepository.findByNomOrPrenomContaining(nomOrprenom,nomOrprenom,pageable);

        List<Postulant> postulants1 = postulants.getContent();
        PostulantResponse postulantResponse = new PostulantResponse();
        postulantResponse.setContenu(postulants1);
        postulantResponse.setPageNo(postulants.getNumber());
        postulantResponse.setPageSize(postulants.getSize());
        postulantResponse.setTotalPages(postulants.getTotalPages());
        postulantResponse.setTotalElements(postulants.getTotalElements());
        postulantResponse.setLast(postulants.isLast());
        postulantResponse.setGenre(genre);
        postulantResponse.setNomOrprenom(nomOrprenom);
        return postulantResponse;

    }

    @Override
    public Postulant readPostulantByid(Long id) {
        return postulantRepository.findPostulantById(id);
    }

    @Override
    public void ImportPostulant(MultipartFile multipartFile,Long idEntretien) {
        Entretien entretien = entretienRepository.findEntretienById(idEntretien);
        //List<Participant> participants = new ArrayList<>();
        try {
            List<Postulant>  postulants = ExcelHelper.excelToPostulants(multipartFile.getInputStream());
            postulants.forEach(postulant -> {
                LocalDate date = LocalDate.now();
                Date date1= new Date();
                String numeroMatricule = "ODCI"+postulant.getId().toString()+postulant.getGenre().substring(0,1)+date.getYear();
                Participant participant= new Participant();
                participant.setNom(postulant.getNom());
                participant.setPrenom(postulant.getPrenom());
                participant.setEmail(postulant.getEmail());
                participant.setEntretien(entretien);
                participant.setStatus(Estatus.Postulant);
                postulant.setParticipant(participant);
                postulant.setNumeroMTCL(numeroMatricule);
                postulant.setDateCreation(date1);
                //participants.add(participant);
                //System.out.println(participants);
                //entretien.setParticipants(participants);
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
}
