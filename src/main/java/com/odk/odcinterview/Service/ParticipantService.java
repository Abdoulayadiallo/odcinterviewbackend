package com.odk.odcinterview.Service;

import com.odk.odcinterview.Model.Participant;

import java.util.List;

public interface ParticipantService {
    Participant saveParticipant(Participant participant);
    Participant updateParticipant(Participant participant,Long id);
    Participant deleteParticipant(Participant participant);
    List<Participant> readParticipants();
    Participant readParticipantByid(Long id);

    Participant getParticipantByJury(Long id);
}
