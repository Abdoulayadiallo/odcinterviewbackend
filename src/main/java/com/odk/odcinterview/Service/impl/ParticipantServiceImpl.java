package com.odk.odcinterview.Service.impl;

import com.odk.odcinterview.Model.Erole;
import com.odk.odcinterview.Model.Participant;
import com.odk.odcinterview.Model.Utilisateur;
import com.odk.odcinterview.Repository.ParticipantRepository;
import com.odk.odcinterview.Repository.UtilisateurRepository;
import com.odk.odcinterview.Service.ParticipantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ParticipantServiceImpl implements ParticipantService {
    @Autowired
    ParticipantRepository participantRepository;
    @Autowired
    private UtilisateurRepository utilisateurRepository;


    @Override
    public Participant saveParticipant(Participant participant) {
        return participantRepository.save(participant);
    }

    @Override
    public Participant updateParticipant(Participant participant, Long id) {
        return null;
    }

    @Override
    public Participant deleteParticipant(Participant participant) {
        return null;
    }

    @Override
    public List<Participant> readParticipants() {
        return participantRepository.findAll();
    }

    @Override
    public Participant readParticipantByid(Long id) {
        return null;
    }

    @Override
    public Participant getParticipantByJury(Long id) {
        Utilisateur utilisateur = utilisateurRepository.findUtilisteurById(id);
        List<Participant> participants = participantRepository.findParticipantByUtilisateur(utilisateur);
        if(utilisateur.getRole().getRoleName() == Erole.ADMIN){
            return utilisateur.getParticipant();
        }
        if(participants.size()>1){
            Participant participant = participants.get(1);
            return participant;
        }
        return participants.get(1);
    }
}
