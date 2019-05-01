package waster;

import com.mxgraph.layout.hierarchical.mxHierarchicalLayout;
import com.mxgraph.layout.mxIGraphLayout;
import com.mxgraph.util.mxCellRenderer;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.jgrapht.Graph;
import org.jgrapht.ext.JGraphXAdapter;
import org.jgrapht.graph.DefaultEdge;
import org.knowm.xchart.*;
import org.knowm.xchart.style.Styler;
import waster.domain.entity.*;
import waster.domain.entity.calendar.Calendar;
import waster.domain.entity.calendar.Operation;
import waster.domain.entity.calendar.Schedule;
import waster.domain.repository.*;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.Instant;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class Main {
    private static FakeMachineRepository fakeMachineRepository = new FakeMachineRepository();
    private static FakeOrderRepository fakeOrderRepository = new FakeOrderRepository();
    private static FakeBenchRepository fakeBenchRepository = new FakeBenchRepository();
    private static BenchAndMachineRepository benchAndMachineRepository = new BenchAndMachineRepository();
    private static FakeStepRepository fakeStepRepository = new FakeStepRepository();

    public static void main(String[] args) {
        tryOrder();

        try {
            outputInFile(tryCalendar());
            tryPrintChart();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        try {
            treOputputShedule(tryCalendar());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void treOputputShedule(BenchSheduler benchSheduler) throws IOException {
        final double RED_LINE = 8 * 60 * 60 * 1000;

        Map<Bench, Calendar> benchCalendarMap = benchSheduler.getBenchCalendarMap();
        Set<Bench> benches = benchCalendarMap.keySet();
//        Calendar calendar = benchCalendarMap.get(benches.toArray()[1]);


        int k = 0;
        int temp = 0;
        boolean first = true;
        for (Bench bench : benches) {
            Calendar calendar = benchCalendarMap.get(bench);
            List<Schedule> schedules = calendar.getSchedule();
            temp += schedules.size() + 5;

            final XYChart chart = new XYChartBuilder().width(600).height(400).title("Машина номер:"+bench.getId()).xAxisTitle("Время с начала расчетного периода").yAxisTitle("Количесвто операций").build();

// Customize Chart
            chart.getStyler().setLegendPosition(Styler.LegendPosition.OutsideE);
            chart.getStyler().setDefaultSeriesRenderStyle(XYSeries.XYSeriesRenderStyle.Line);
            for (int i = 0; i < schedules.size(); i++) {
                int min = schedules.size() - i  > schedules.size() ? schedules.size() : schedules.size() - i ;
                double[] x, y = new double[]{min,min-1,min-1};

                Schedule schedule = schedules.get(i);
                x = new double[]{schedule.getStart().doubleValue(), schedule.getStart().doubleValue(), schedule.getEnd()};
                k = k + 1;
                chart.addSeries(+k + ")" + schedule.getOperation().getStep().getName(), x, y);
            }
                chart.addSeries("Конец смены", new double[]{RED_LINE, RED_LINE}, new double[]{0, schedules.size()}).setLineColor(Color.RED);


            BitmapEncoder.saveBitmapWithDPI(chart, "./benchGraph/" + bench.getId(), BitmapEncoder.BitmapFormat.PNG, 100);
        }

//        final SwingWrapper<XYChart> sw = new SwingWrapper<>(chart);
//        sw.displayChart();

    }

    private static void tryPrintChart() throws InterruptedException, IOException {
        final XYChart chart = new XYChartBuilder().width(600).height(400).title("Area Chart").xAxisTitle("X").yAxisTitle("Y").build();

// Customize Chart
        chart.getStyler().setLegendPosition(Styler.LegendPosition.InsideNE);
        chart.getStyler().setDefaultSeriesRenderStyle(XYSeries.XYSeriesRenderStyle.Area);

// Series
        chart.addSeries("a", new double[]{0, 3, 5, 7, 99}, new double[]{-3, 5, 9, 6, 5});
        chart.addSeries("b", new double[]{0, 2, 4, 6, 9}, new double[]{-1, 6, 4, 0, 4});
        chart.addSeries("c", new double[]{0, 1, 3, 8, 9}, new double[]{-2, -1, 1, 0, 1});

        // Show it
        final SwingWrapper<XYChart> sw = new SwingWrapper<>(chart);

        sw.displayChart();

        BitmapEncoder.saveBitmapWithDPI(chart, "./Sample_Chart_300_DPI", BitmapEncoder.BitmapFormat.PNG, 300);
    }


    static final String excelLoc = System.getProperty("user.dir") + "/src/main/resources/gantt\\" + "testing.xlsx";

    private static void outputInFile(BenchSheduler benchSheduler) throws IOException {
        FileOutputStream fos = new FileOutputStream(excelLoc);
        XSSFWorkbook workbook = new XSSFWorkbook();
        Map<Bench, Calendar> benchCalendarMap = benchSheduler.getBenchCalendarMap();
        Set<Bench> benches = benchCalendarMap.keySet();

        createCommonSheet(workbook, benchCalendarMap);

        for (Bench bench : benches) {
            createSheet(bench, benchCalendarMap.get(bench), workbook);
        }
        workbook.write(fos);
        fos.flush();
        fos.close();
    }

    private static void createCommonSheet(XSSFWorkbook workbook, Map<Bench, Calendar> benchCalendarMap) {
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

    private static void createSheet(Bench bench, Calendar calendar, XSSFWorkbook workbook) {
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


    private static BenchSheduler tryCalendar() {
        BenchSheduler benchSheduler = new BenchSheduler(8L);
        List<Order> orderList = fakeOrderRepository.getOrders();
        for (Order order : orderList) {
            Article article = order.getArticle();
            ProcessMap processMap = article.getProcessMap();

            Long initDate = 0L;
            processMap.getShortestPath().stream().map(step ->
                    Operation.builder()
                            .initialStartDate(initDate)
                            .step(Step.builder()
                                    .id(step.getId())
                                    .machine(step.getMachine())
                                    .name(article.getName() + article.getColoring())
                                    .setting(step.getSetting())

                                    .build())
                            .length(order.getLength())
                            .build()).
                    forEach(benchSheduler::addInBenchCalendar);
        }
        return benchSheduler;
    }

    private static void tryOrder() {
        List<Order> orderList = fakeOrderRepository.getOrders();
        List l = orderList.stream().map(order -> order.getArticle().getProcessMap().getShortestPath()).collect(Collectors.toList());
        orderList.stream().forEach(order -> tryView(order.getArticle()));
    }

    private static void tryView(Article article) {
        Graph graph = article.getProcessMap().getGraph();
        JGraphXAdapter<Step, DefaultEdge> graphAdapter =
                new JGraphXAdapter<>(graph);
        mxIGraphLayout layout = new mxHierarchicalLayout(graphAdapter);
        layout.execute(graphAdapter.getDefaultParent());
        BufferedImage image =
                mxCellRenderer.createBufferedImage(graphAdapter, null, 2, Color.WHITE, true, null);
        File imgFile = new File(System.getProperty("user.dir") + "/src/main/resources/graphs/" + article.getName() + "_" + article.getColoring() + ".png");

        try {
            ImageIO.write(image, "PNG", imgFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

