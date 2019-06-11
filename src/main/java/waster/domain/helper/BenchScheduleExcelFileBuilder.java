package waster.domain.helper;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFDrawing;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.knowm.xchart.XYChart;
import waster.domain.entity.Bench;
import waster.domain.entity.calendar.Calendar;
import waster.domain.entity.calendar.Schedule;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

public class BenchScheduleExcelFileBuilder {
    private Date startDate;

    private XSSFWorkbook workbook;


    public BenchScheduleExcelFileBuilder(Date startDate, XSSFWorkbook workbook) {
        this.startDate = startDate;
        this.workbook = workbook;
    }

    public void createCommonSheet(Map<Bench, Calendar> benchCalendarMap, Date startDate) {
        String sheetName = "Общий";

        XSSFSheet sheet = workbook.createSheet(sheetName);

        int counter = 0;

        createRow(sheet, counter++, "Номер оборудования", "Метраж", "Артикул", "Колорит", "Номер заказа", "Время начала", "Длительность", "Время окончания");
        Set<Bench> benches = benchCalendarMap.keySet();
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");

        List<Bench> benchArrayList = new ArrayList<>(benches);
        benchArrayList.sort((x, y) -> (int) (x.getId() - y.getId()));
        for (Bench bench : benchArrayList) {
            Calendar calendar = benchCalendarMap.get(bench);
            List<Schedule> schedules = calendar.getSchedules();
            boolean isFirst = true;

            for (Schedule schedule : schedules) {
                Date date = new Date(startDate.getTime() + schedule.getStart());
                if (isFirst) {
                    createRow(sheet, counter,
                            bench.getId().toString(),
                            schedule.getOperation().getLength().toString(),
                            schedule.getOperation().getOrder().getArticle().getName(),
                            schedule.getOperation().getOrder().getArticle().getColoring(),
                            schedule.getOperation().getOrder().getId().toString(),
                            dateFormat.format(date),
                            durationHourAndMinutes(schedule),
                            dateFormat.format(new Date(date.getTime() + schedule.getEnd() - schedule.getStart()))
                    );
                    isFirst = false;
                } else {
                    createRow(sheet, counter
                            , "",
                            schedule.getOperation().getLength().toString(),
                            schedule.getOperation().getOrder().getArticle().getName(),
                            schedule.getOperation().getOrder().getArticle().getColoring(),
                            schedule.getOperation().getOrder().getId().toString(),
                            dateFormat.format(date),
                            durationHourAndMinutes(schedule),
                            dateFormat.format(new Date(date.getTime() + schedule.getEnd() - schedule.getStart()))
                    );
                }
                counter++;
            }
        }
    }

    protected String durationHourAndMinutes(Schedule schedule) {
        Long different = schedule.getEnd() - schedule.getStart() ;

        long secondsInMilli = 1000;
        long minutesInMilli = secondsInMilli * 60;
        long hoursInMilli = minutesInMilli * 60;
        long daysInMilli = hoursInMilli * 24;

        long elapsedDays = different / daysInMilli;
        different = different % daysInMilli;

        long elapsedHours = different / hoursInMilli;
        different = different % hoursInMilli;

        long elapsedMinutes = different / minutesInMilli;
        different = different % minutesInMilli;

        long elapsedSeconds = different / secondsInMilli;
        return String.format("%d:%02d:%02d:%02d", elapsedDays, elapsedHours, elapsedMinutes, elapsedSeconds);
    }

    public void createOverWorkedList(Iterable<Bench> benches) {
        XSSFSheet sheet = workbook.createSheet("Перегрузка");
        int counter = 0;
        for (Bench bench : benches) {
            createRow(sheet, counter++, bench.getId().toString());

        }
    }

    public void createSheet(Bench bench, Calendar calendar, double limit) {
        XSSFSheet sheet = workbook.createSheet(String.valueOf((bench.getId())));
        int counter = 0;
        createRow(sheet, counter++, "Артикул", "Колорит", "Номер заказа", "Длина", "Время начала", "Длительность", "Время окончания");

        List<Schedule> schedules = calendar.getSchedules();
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        for (Schedule schedule : schedules) {
            createRow(sheet,
                    counter++,
                    schedule.getOperation().getOrder().getArticle().getName(),
                    schedule.getOperation().getOrder().getArticle().getColoring(),
                    schedule.getOperation().getOrder().getId().toString(),
                    schedule.getOperation().getOrder().getLength().toString(),
                    dateFormat.format(new Date(startDate.getTime() + schedule.getStart())),
                    durationHourAndMinutes(schedule),
                    dateFormat.format(new Date(startDate.getTime() + schedule.getEnd() - schedule.getStart())));
        }
        try {
            createChart(sheet, bench, calendar, limit);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void createChart(XSSFSheet sheet, Bench bench, Calendar calendar, double limit) throws IOException {
        final CreationHelper helper = workbook.getCreationHelper();
        final XSSFDrawing drawing = sheet.createDrawingPatriarch();
        final ClientAnchor anchor = helper.createClientAnchor();

        BenchScheduleChartBuilder benchScheduleChartBuilder = new BenchScheduleChartBuilder(limit);
        XYChart chart = benchScheduleChartBuilder.getChartBytesForBench(bench, calendar);
        byte[] bytes = benchScheduleChartBuilder.getChartAsByteArray(chart);
        final int pictureIndex = workbook.addPicture(bytes, Workbook.PICTURE_TYPE_PNG);

        anchor.setCol1(9);
        anchor.setRow1(1); // same row is okay
        anchor.setRow2(20);
        anchor.setCol2(26);
        final Picture pict = drawing.createPicture(anchor, pictureIndex);
    }

    private static void createRow(XSSFSheet sheet, int rowNum, String... cellNames) {
        Row row = sheet.createRow(rowNum);
        for (int i = 0; i < cellNames.length; i++) {
            Cell cell = row.createCell(i);
            cell.setCellValue(cellNames[i]);
        }
    }
}
