package ru.madrabit.scraper.scraper.poi;

import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import ru.madrabit.scraper.scraper.domen.Answer;
import ru.madrabit.scraper.scraper.domen.Question;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

@Slf4j
public class CreateExcelDemo {

    private String filePath = "C:/demo/test_rosteh.xlsx";

    public void createExcel(List<Question> questionList) {
        XSSFWorkbook workbook = new XSSFWorkbook();
        XSSFSheet sheet = workbook.createSheet("Test sheet");

        int rowNumber = 0;
        Cell cell;
        Row row;
        //
        XSSFCellStyle style = createStyleForTitle(workbook);

        row = sheet.createRow(rowNumber);

        // Id
        cell = row.createCell(0, CellType.STRING);
        cell.setCellValue("Код (первичный ключ)");
        cell.setCellStyle(style);
        // Title
        cell = row.createCell(1, CellType.STRING);
        cell.setCellValue("Заголовок");
        cell.setCellStyle(style);
        // Type
        cell = row.createCell(2, CellType.STRING);
        cell.setCellValue("Тип вопроса");
        cell.setCellStyle(style);
        // Question
        cell = row.createCell(3, CellType.STRING);
        cell.setCellValue("Вопрос");
        cell.setCellStyle(style);
        // Weight
        cell = row.createCell(4, CellType.STRING);
        cell.setCellValue("Вес вопроса (балл)");
        cell.setCellStyle(style);
        // Answer
        cell = row.createCell(5, CellType.STRING);
        cell.setCellValue("Тексты вариантов ответа");
        cell.setCellStyle(style);
        // Number of right answer
        cell = row.createCell(6, CellType.STRING);
        cell.setCellValue("Номера правильных ответов (начиная с 1)");
        cell.setCellStyle(style);

        // Data
        for (Question q : questionList) {
            row = sheet.createRow(++rowNumber);

            // Id (A)
            cell = row.createCell(0, CellType.STRING);
            cell.setCellValue("A.1-" + rowNumber);
            // Title (B)
            cell = row.createCell(1, CellType.STRING);
            cell.setCellValue(q.getText());
            // Type (C)
            cell = row.createCell(2, CellType.STRING);
            cell.setCellValue("multiple_choice");
            // Question (D)
            cell = row.createCell(3, CellType.STRING);
            cell.setCellValue(q.getText());
            // Weight (E)
            cell = row.createCell(4, CellType.NUMERIC);
            cell.setCellValue(1);
            // Weight (F)
            cell = row.createCell(5, CellType.STRING);
            StringBuilder answers = new StringBuilder();
            int answersLength = q.getAnswerSet().size();
            int i = 0;
            for (Answer answer : q.getAnswerSet()) {
                if (++i == answersLength) {
                    answers.append(answer.getText());
                } else {
                    answers.append(answer.getText()).append("#");
                }
            }
            cell.setCellValue(answers.toString());
            // Weight (G)
            cell = row.createCell(6, CellType.STRING);
            int sizeOfAnswerSet = q.getAnswerNumber().size();
            int j = 0;
            StringBuilder answerNum = new StringBuilder();
            for (Integer serialNum : q.getAnswerNumber()) {
                if (++j == sizeOfAnswerSet) {
                    answerNum.append(serialNum);
                } else {
                    answerNum.append(serialNum).append(", ");
                }
            }
            cell.setCellValue(answerNum.toString());
        }

        createFile(workbook);

    }


    private static XSSFCellStyle createStyleForTitle(XSSFWorkbook workbook) {
        XSSFFont font = workbook.createFont();
        font.setBold(true);
        XSSFCellStyle style = workbook.createCellStyle();
        style.setFont(font);
        return style;
    }

    private void createFile(XSSFWorkbook workbook) {
        File file = new File(filePath);
        file.getParentFile().mkdirs();
        try (FileOutputStream outFile = new FileOutputStream(file)) {
            workbook.write(outFile);
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        }
        log.info("Created file: {}", file.getAbsolutePath());

    }
}
