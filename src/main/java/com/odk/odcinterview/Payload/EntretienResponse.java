package com.odk.odcinterview.Payload;

import com.odk.odcinterview.Model.Entretien;
import com.odk.odcinterview.Model.Postulant;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EntretienResponse {
    List<Entretien> contenu;
    private int pageNo;
    private int pageSize;
    private long totalElements;
    private int totalPages;
    private boolean last;
    private String username;
    private String status;


}
