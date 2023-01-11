package com.odk.odcinterview.Service.impl;

import com.odk.odcinterview.Model.Participant;
import com.odk.odcinterview.Repository.ParticipantRepository;
import com.odk.odcinterview.Service.ParticipantService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ParticipantServiceImpl implements ParticipantService {
    private final ParticipantRepository participantRepository;

    public ParticipantServiceImpl(ParticipantRepository participantRepository) {
        this.participantRepository = participantRepository;
    }

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
        return null;
    }

    @Override
    public Participant readParticipantByid(Long id) {
        return null;
    }
}
