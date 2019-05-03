package waster.domain.helper;

import org.knowm.xchart.BitmapEncoder;
import org.knowm.xchart.XYChart;
import org.knowm.xchart.XYChartBuilder;
import org.knowm.xchart.XYSeries;
import org.knowm.xchart.style.Styler;
import waster.domain.entity.Bench;
import waster.domain.entity.BenchScheduler;
import waster.domain.entity.calendar.Calendar;
import waster.domain.entity.calendar.Schedule;

import java.awt.*;
import java.io.IOException;
import java.util.List;

public class BenchScheduleChartBuilder {
    double deadlineTime;
    private BenchScheduler benchScheduler;
    private XYChart chart = defaultChart(0L);
    private int scheduleCounter = 0;

    public BenchScheduleChartBuilder(double deadlineTime, BenchScheduler benchScheduler) {
        this.deadlineTime = deadlineTime;
        this.benchScheduler = benchScheduler;
    }

    public void createChartForBench(Bench bench, Calendar calendar) throws IOException {
        chart = defaultChart(bench.getId());
        List<Schedule> schedules = calendar.getSchedule();
        for (int i = 0; i < schedules.size(); i++) {
            Schedule schedule = schedules.get(i);
            int currentYStage = schedules.size() - i;
            addStepInGantChart(schedule, currentYStage);
        }
        addDeadlineInGantChart(schedules.size());

        BitmapEncoder.saveBitmapWithDPI(chart, "./benchGraph/" + bench.getId(), BitmapEncoder.BitmapFormat.PNG, 100);
    }

    private void addStepInGantChart(Schedule schedule, int currentStage) {
        int currentY = currentStage;
        int nextY = currentY - 1;
        double[] x;
        double[] y = new double[]{currentY, nextY, nextY};

        x = new double[]{schedule.getStart(), schedule.getStart(), schedule.getEnd()};
        scheduleCounter = scheduleCounter + 1;
        String seriesTitle = scheduleCounter + ")" + schedule.getOperation().getStep().getName();
        chart.addSeries(seriesTitle, x, y);
    }

    private void addDeadlineInGantChart(int scheduleSize) {
        chart.addSeries("Конец смены",
                new double[]{deadlineTime, deadlineTime},
                new double[]{0, scheduleSize
                })
                .setLineColor(Color.RED);
    }


    private static XYChart defaultChart(Long benchId) {
        String chartTitle = "Машина номер:" + benchId;
        final String X_AXIS_LABEL = "Время с начала расчетного периода";
        final String Y_AXIS_LABEL = "Количесвто операций";

        return getChart(chartTitle, X_AXIS_LABEL, Y_AXIS_LABEL);
    }

    private static XYChart getChart(String title, String xAxisLabel, String yAxisLabel) {
        XYChart chart = new XYChartBuilder()
                .width(600)
                .height(400)
                .title(title)
                .xAxisTitle(xAxisLabel)
                .yAxisTitle(yAxisLabel)
                .build();

        chart.getStyler().setLegendPosition(Styler.LegendPosition.OutsideE);
        chart.getStyler().setDefaultSeriesRenderStyle(XYSeries.XYSeriesRenderStyle.Line);
        return chart;
    }
}
