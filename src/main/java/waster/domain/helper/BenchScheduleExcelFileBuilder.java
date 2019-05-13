package waster.domain.helper;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xddf.usermodel.*;
import org.apache.poi.xddf.usermodel.chart.XDDFChartData;
import org.apache.poi.xssf.usermodel.XSSFChart;
import org.apache.poi.xssf.usermodel.XSSFDrawing;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.knowm.xchart.XYChart;
import org.openxmlformats.schemas.drawingml.x2006.chart.CTCatAx;
import org.openxmlformats.schemas.drawingml.x2006.chart.CTTitle;
import org.openxmlformats.schemas.drawingml.x2006.chart.CTValAx;
import org.openxmlformats.schemas.drawingml.x2006.main.CTTextBody;
import org.openxmlformats.schemas.drawingml.x2006.main.CTTextParagraph;
import waster.domain.entity.Bench;
import waster.domain.entity.calendar.Calendar;
import waster.domain.entity.calendar.Schedule;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.Instant;
import java.util.*;

public class BenchScheduleExcelFileBuilder {

    private XSSFWorkbook workbook;

    public BenchScheduleExcelFileBuilder(XSSFWorkbook workbook) {
        this.workbook = workbook;
    }

    public void createCommonSheet(Map<Bench, Calendar> benchCalendarMap) {
        String sheetName = "Main";

        Date startDate = new Date();
        XSSFSheet sheet = workbook.createSheet(sheetName);

        int counter = 0;

        createRow(sheet, counter++, "Номер оборудования", "Метраж", "Артикул", "Время начала", "Длительность", "Время окончания");
        Set<Bench> benches = benchCalendarMap.keySet();
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");

        List<Bench> benchArrayList = new ArrayList<>(benches);
        benchArrayList.sort((x, y) -> (int) (x.getId() - y.getId()));
        for (Bench bench : benchArrayList) {
            Calendar calendar = benchCalendarMap.get(bench);
            List<Schedule> schedules = calendar.getSchedules();
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

    public void createOverWorkedList(Iterable<Bench> benches) {
        XSSFSheet sheet = workbook.createSheet("Over");
        int counter = 0;
        for (Bench bench : benches) {
            createRow(sheet, counter++, bench.getId().toString());

        }
    }

    public void createSheet(Bench bench, Calendar calendar, double limit) {
        XSSFSheet sheet = workbook.createSheet(String.valueOf((bench.getId())));
        int counter = 0;
        createRow(sheet, counter++, "Артикул", "Время начала", "Длительность");

        List<Schedule> schedules = calendar.getSchedules();

        for (Schedule schedule : schedules) {
            createNumRow(sheet, counter++, schedule.getOperation().getStep().getName(), schedule.getStart(), schedule.getEnd() - schedule.getStart());
        }
        try {
            createChart(sheet, bench, calendar, limit);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //TODO IMPLEMENT IT https://github.com/knowm/XChart
    private void createChart(XSSFSheet sheet, Bench bench, Calendar calendar, double limit) throws IOException {
        final CreationHelper helper = workbook.getCreationHelper();
        final XSSFDrawing drawing = sheet.createDrawingPatriarch();
        final ClientAnchor anchor = helper.createClientAnchor();

        BenchScheduleChartBuilder benchScheduleChartBuilder = new BenchScheduleChartBuilder(limit);
        XYChart chart = benchScheduleChartBuilder.getChartBytesForBench(bench, calendar);
        byte[] bytes = benchScheduleChartBuilder.getChartAsByteArray(chart);
        final int pictureIndex = workbook.addPicture(bytes, Workbook.PICTURE_TYPE_PNG);

        anchor.setCol1(6);
        anchor.setRow1(1); // same row is okay
        anchor.setRow2(20);
        anchor.setCol2(26);
        final Picture pict = drawing.createPicture(anchor, pictureIndex);
        createRealChart(sheet);
    }

    private void createRealChart(XSSFSheet sheet) {
    }

    private static void solidLineSeries(XDDFChartData data, int index, PresetColor color) {
        XDDFSolidFillProperties fill = new XDDFSolidFillProperties(XDDFColor.from(color));
        XDDFLineProperties line = new XDDFLineProperties();
        line.setFillProperties(fill);
        XDDFChartData.Series series = data.getSeries().get(index);
        XDDFShapeProperties properties = series.getShapeProperties();
        if (properties == null) {
            properties = new XDDFShapeProperties();
        }
        properties.setLineProperties(line);
        series.setShapeProperties(properties);
    }

    private static void solidFillSeries(XDDFChartData data, int index, PresetColor color) {
        XDDFSolidFillProperties fill = new XDDFSolidFillProperties(XDDFColor.from(color));
        XDDFChartData.Series series = data.getSeries().get(index);
        XDDFShapeProperties properties = series.getShapeProperties();
        if (properties == null) {
            properties = new XDDFShapeProperties();
        }
        properties.setFillProperties(fill);
        series.setShapeProperties(properties);
    }

    public static void setCatAxisTitle(XSSFChart chart, int axisIdx, String title) {
        CTCatAx valAx = chart.getCTChart().getPlotArea().getCatAxArray(axisIdx);
        CTTitle ctTitle = valAx.addNewTitle();
        ctTitle.addNewLayout();
        ctTitle.addNewOverlay().setVal(false);
        CTTextBody rich = ctTitle.addNewTx().addNewRich();
        rich.addNewBodyPr();
        rich.addNewLstStyle();
        CTTextParagraph p = rich.addNewP();
        p.addNewPPr().addNewDefRPr();
        p.addNewR().setT(title);
        p.addNewEndParaRPr();
    }


    public static void setValueAxisTitle(XSSFChart chart, int axisIdx, String title) {
        CTValAx valAx = chart.getCTChart().getPlotArea().getValAxArray(axisIdx);
        CTTitle ctTitle = valAx.addNewTitle();
        ctTitle.addNewLayout();
        ctTitle.addNewOverlay().setVal(false);
        CTTextBody rich = ctTitle.addNewTx().addNewRich();
        rich.addNewBodyPr();
        rich.addNewLstStyle();
        CTTextParagraph p = rich.addNewP();
        p.addNewPPr().addNewDefRPr();
        p.addNewR().setT(title);
        p.addNewEndParaRPr();
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
