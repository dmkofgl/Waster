package waster.domain.helper;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import waster.domain.entity.Bench;
import waster.domain.entity.calendar.Calendar;
import waster.domain.entity.calendar.Schedule;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.Instant;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class BenchScheduleExcelFileBuilder {


    public static void createCommonSheet(XSSFWorkbook workbook, Map<Bench, Calendar> benchCalendarMap) {
        String sheetName = "Main";
        Date startDate = new Date();
        XSSFSheet sheet = workbook.createSheet(sheetName);

        int counter = 0;
        createRow(sheet, counter++, "Номер оборудования", "Метраж", "Артикул", "Время начала", "Длительность", "Время окончания");
        Set<Bench> benches = benchCalendarMap.keySet();
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        for (Bench bench : benches) {
            Calendar calendar = benchCalendarMap.get(bench);
            List<Schedule> schedules = calendar.getSchedule();
            Boolean isFirst = true;
            for (Schedule schedule : schedules) {
                Row row = sheet.createRow(counter++);
                Cell c0 = row.createCell(0);
                if (isFirst) {
                    c0.setCellValue(bench.getId());
                    isFirst = false;
                }
                Cell c1 = row.createCell(1);
                c1.setCellValue(schedule.getOperation().getLength());

                Cell c2 = row.createCell(2);
                c2.setCellValue(schedule.getOperation().getStep().getName());

                Cell c3 = row.createCell(3);
                Date date = new Date(startDate.getTime() + schedule.getStart());
                c3.setCellValue(dateFormat.format(date));
                Instant end = Instant.ofEpochMilli(schedule.getEnd());
                Instant start = Instant.ofEpochMilli(schedule.getStart());
                Duration duration = Duration.between(start, end);

                Cell c4 = row.createCell(4);
                c4.setCellValue(duration.toHours() + ":" + duration.toMinutes());

                Cell c5 = row.createCell(5);
                c5.setCellValue(dateFormat.format(new Date(date.getTime() + schedule.getEnd() - schedule.getStart())));
            }
        }

    }

    public static void createSheet(Bench bench, Calendar calendar, XSSFWorkbook workbook) {
        XSSFSheet sheet = workbook.createSheet(String.valueOf((bench.getId())));
        int counter = 0;
        createRow(sheet, counter++, "Артикул", "Время начала", "Длительность");

        List<Schedule> schedules = calendar.getSchedule();

        for (Schedule schedule : schedules) {
            createNumRow(sheet, counter++, schedule.getOperation().getStep().getName(), schedule.getStart(), schedule.getEnd() - schedule.getStart());
            createChart(bench.getMachine().getName());
        }
    }

    //TODO IMPLEMENT IT https://github.com/knowm/XChart
    private static void createChart(String name) {

    }

    private static void createRow(XSSFSheet sheet, int rowNum, String... cellNames) {
        Row row = sheet.createRow(rowNum);
        for (int i = 0; i < cellNames.length; i++) {
            Cell cell = row.createCell(i);
            cell.setCellValue(cellNames[i]);
        }
    }

    private static void createNumRow(XSSFSheet sheet, int rowNum, String c1, Long c2, Long c3) {
        Row row = sheet.createRow(rowNum);
        Cell cell0 = row.createCell(0);
        cell0.setCellValue(c1);

        Cell cell1 = row.createCell(1);

        cell1.setCellValue(c2);

        Cell cell2 = row.createCell(2);
        cell2.setCellValue(c3);

    }
}
