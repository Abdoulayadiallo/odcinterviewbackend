package com.odk.odcinterview.util;

import com.odk.odcinterview.Model.Estatus;
import com.odk.odcinterview.Model.Participant;
import com.odk.odcinterview.Model.Postulant;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

public class ExcelHelper {
    public static String TYPE = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";
    static String[] HEADERs = { "Id", "Nom", "Prenom", "Email","Numero","Genre"};
    static String SHEET = "Postulant";

    public static boolean hasExcelFormat(MultipartFile file) {

        if (!TYPE.equals(file.getContentType())) {
            return false;
        }

        return true;
    }

    public static List<Postulant> excelToPostulants(InputStream is) {
        try {
            Workbook workbook = WorkbookFactory.create(is);
            Sheet sheet = workbook.getSheetAt(0);
            Iterator<Row> rows = sheet.iterator();

            List<Postulant> postulants = new ArrayList<Postulant>();

            int rowNumber = 0;
            while (rows.hasNext()) {
                Row currentRow = rows.next();

                // skip header
                if (rowNumber == 0) {
                    rowNumber++;
                    continue;
                }

                Iterator<Cell> cellsInRow = currentRow.iterator();

                Postulant postulant = new Postulant();

                int cellIdx = 0;
                while (cellsInRow.hasNext()) {
                    Cell currentCell = cellsInRow.next();

                    switch (cellIdx) {
                        case 0:
                            postulant.setId((long) currentCell.getNumericCellValue());
                            break;

                        case 1:
                            postulant.setNom(currentCell.getStringCellValue());
                            break;

                        case 2:
                            postulant.setPrenom(currentCell.getStringCellValue());
                            break;

                        case 3:
                            postulant.setEmail(currentCell.getStringCellValue());
                            break;

                        case 4:
                            postulant.setNumero(String.valueOf(currentCell.getNumericCellValue()));
                            break;

                        case 5:
                            postulant.setGenre(currentCell.getStringCellValue());
                            break;

                        default:
                            break;
                    }
                    cellIdx++;
                }

                postulants.add(postulant);
            }

            workbook.close();

            return postulants;
        } catch (IOException e) {
            throw new RuntimeException("fail to parse Excel file: " + e.getMessage());
        }
    }
}
