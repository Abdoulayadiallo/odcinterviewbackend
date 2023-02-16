package com.odk.odcinterview.util;


import com.odk.odcinterview.Model.Postulant;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

public class ExcelHelper {
    public static String TYPE = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";
    static String[] HEADERs = {"Nom", "Prenom", "Email","Numero","Genre","Decision final","Note final","Rang","Numero matricule","Resultat final"};
    static String[] HEADERExports = {"Id","Nom", "Prenom", "Email","Numero","Genre","Decision final","Note final","Rang","Numero matricule","Resultat final"};
    static String SHEET = "Postulant";

    public static boolean hasExcelFormat(MultipartFile file) {

        if (!TYPE.equals(file.getContentType())) {
            return false;
        }

        return true;
    }
    //Une methode permettant de importer les postulants de fichier excel dans la table Postulant

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
                            postulant.setNom(currentCell.getStringCellValue());
                            break;

                        case 1:
                            postulant.setPrenom(currentCell.getStringCellValue());
                            break;

                        case 2:
                            postulant.setEmail(currentCell.getStringCellValue());
                            break;

                        case 3:
                            try {
                                postulant.setNumero(currentCell.getStringCellValue());
                            }catch (Exception e){
                                postulant.setNumero(String.valueOf(currentCell.getNumericCellValue()));
                            }
                            break;

                        case 4:
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

    //Une methode permettant de exporter les postulants de base en fichier excel
    public static ByteArrayInputStream postulantsToExcel(List<Postulant> postulants) {

        try (Workbook workbook = new XSSFWorkbook(); ByteArrayOutputStream out = new ByteArrayOutputStream();) {
            Sheet sheet = workbook.createSheet(SHEET);

            // Header
            Row headerRow = sheet.createRow(0);

            for (int col = 0; col < HEADERExports.length; col++) {
                Cell cell = headerRow.createCell(col);
                cell.setCellValue(HEADERExports[col]);
            }

            int rowIdx = 1;
            for (Postulant postulant : postulants) {
                Row row = sheet.createRow(rowIdx++);
                try {
                    row.createCell(0).setCellValue(postulant.getId());
                    row.createCell(1).setCellValue(postulant.getNom());
                    row.createCell(2).setCellValue(postulant.getPrenom());
                    row.createCell(3).setCellValue(postulant.getEmail());
                    row.createCell(4).setCellValue(postulant.getNumero());
                    row.createCell(5).setCellValue(postulant.getGenre());
                    row.createCell(6).setCellValue(postulant.getDecisionFinal().toString());
                    row.createCell(7).setCellValue(postulant.getNoteFinal());
                    row.createCell(8).setCellValue(postulant.getRang());
                    row.createCell(9).setCellValue(postulant.getNumeroMTCL());
                    row.createCell(10).setCellValue(postulant.getResultatFinal());

                }catch (Exception e){

                }




            }

            workbook.write(out);
            return new ByteArrayInputStream(out.toByteArray());
        } catch (IOException e) {
            throw new RuntimeException("fail to import data to Excel file: " + e.getMessage());
        }
    }
}
