package com.odk.odcinterview.Payload;

import com.odk.odcinterview.Model.Critere;
import com.odk.odcinterview.Model.Note;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CritereResponse {
    List<Critere> contenu;
    private int pageNo;
    private int pageSize;
    private long totalElements;
    private int totalPages;
    private boolean last;
}
