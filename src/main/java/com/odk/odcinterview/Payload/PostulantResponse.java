package com.odk.odcinterview.Payload;

import com.odk.odcinterview.Model.Postulant;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PostulantResponse {
    List<Postulant> contenu;
    private int pageNo;
    private int pageSize;
    private long totalElements;
    private int totalPages;
    private boolean last;
    private String genre;
    private String Keyword;


}
