package waster.domain.repository.init;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import waster.domain.entity.*;
import waster.domain.repository.FakeMachineRepository;
import waster.domain.repository.FakeProcessMapRepository;
import waster.domain.repository.FakeSettingsRepository;
import waster.domain.repository.abstracts.*;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Component
public class DbInitializer implements CommandLineRunner {
    private ArticleRepository articleRepository;
    private ProcessMapRepository processMapRepository;
    private MachineRepository machineRepository;
    private BenchRepository benchRepository;
    private SettingsRepository settingsRepository;
    private StepRepository stepRepository;


    @Autowired
    public DbInitializer(ArticleRepository articleRepository, ProcessMapRepository processMapRepository, MachineRepository machineRepository, BenchRepository benchRepository, SettingsRepository settingsRepository, StepRepository stepRepository) {
        this.articleRepository = articleRepository;
        this.processMapRepository = processMapRepository;
        this.machineRepository = machineRepository;
        this.benchRepository = benchRepository;
        this.settingsRepository = settingsRepository;
        this.stepRepository = stepRepository;
    }



    @Override
    public void run(String... args) throws Exception {
        initProcessMap();
        initArticles();
        initMachines();
        initSettings();
        initBenches();
    }

    private void initArticles() {
        List<Article> articles = Arrays.asList(
                createArticle(0L, "00с65гл+ВОсн", "11001"),
                createArticle(1L, "00с65гл+ВОсн", "130706"),
                createArticle(2L, "00с65гл+ВОсн", "261005"),
                createArticle(3L, "08с6гл+во-у", "110701"),
                createArticle(4L, "00с65гл+ВОсн", "191862"),
                createArticle(5L, "08с6отб+гом", "10101"),
                createArticle(6L, "08с6гл+гом", "110701"),
                createArticle(7L, "08с6гл+гом", "191862"),
                createArticle(8L, "4с5гл+МВО", "261005")
        );
        articleRepository.saveAll(articles);

    }

    private Article createArticle(Long id, String art, String color) {

        Article article = new Article();
        article.setName(art);
        article.setColoring(color);
        article.setId(id);
        article.setProcessMap(processMapRepository.getByArticleId(id).get());
        return article;
    }

    private void initProcessMap() {
        List<ProcessMap> processMaps = FakeProcessMapRepository.processMaps;

        processMapRepository.saveAll(processMaps);
    }

    private void initMachines() {
        List<Machine> machines = FakeMachineRepository.machines;

        machineRepository.saveAll(machines);
    }

    private void initBenches() {
        List<Bench> benches = Arrays.asList(
                createBench(-1, -1L),
                createBench(-2, -2L),
                createBench(10, 10L),
                createBench(13, 10L),
                createBench(14, 10L),
                createBench(80, 10L),
                createBench(81, 10L),
                createBench(157, 10L),
                createBench(166, 10L),
                createBench(16, 14L),
                createBench(34, 14L),
                createBench(149, 14L),
                createBench(151, 14L),
                createBench(152, 14L),
                createBench(40, 20L),
                createBench(41, 21L),
                createBench(63, 30L),
                createBench(86, 44L),
                createBench(87, 45L),
                createBench(88, 45L),
                createBench(89, 45L),
                createBench(90, 45L),
                createBench(91, 45L),
                createBench(92, 45L),
                createBench(93, 45L),
                createBench(94, 45L),
                createBench(95, 45L),
                createBench(96, 45L),
                createBench(97, 45L),
                createBench(99, 45L),
                createBench(98, 45L),
                createBench(100, 45L),
                createBench(101, 45L),
                createBench(102, 45L),
                createBench(103, 45l),
                createBench(104, 45L),
                createBench(105, 45L),
                createBench(106, 45L),
                createBench(107, 45L),
                createBench(108, 45L),
                createBench(109, 45L),
                createBench(110, 45L),
                createBench(111, 45L),
                createBench(126, 56L),
                createBench(127, 56L),
                createBench(139, 61L),
                createBench(140, 61L),
                createBench(170, 61L),
                createBench(144, 64L),
                createBench(165, 76L),
                createBench(168, 78L),
                createBench(169, 79L),
                createBench(173, 80L),
                createBench(175, 82L)
        );
        benchRepository.saveAll(benches);
    }

    private Bench createBench(Integer id, Long machineId) {
        Machine machine = machineRepository.findById(machineId).get();
        Bench bench = new Bench();
        bench.setId(id);
        bench.setMachine(machine);
        return bench;
    }

    private void initSettings() {
        List<Setting> settings = FakeSettingsRepository.settings;

        settingsRepository.saveAll(settings);
    }

    private void initStep() {
        List<Step> steps = Arrays.asList(
                createStep(-1L, "ИСТОК", machineRepository.findById(-1L), settingsRepository.findById(-1L)),
                createStep(-2L, "ВТОК", machineRepository.findById(-2L), settingsRepository.findById(-2L)),
                //art 00 C65-KB
                createStep(0L, "Опаливание", machineRepository.findById(82L), settingsRepository.findById(274L)),
                createStep(1L, "", machineRepository.findById(20L), settingsRepository.findById(100L)),
                createStep(2L, "", machineRepository.findById(21L), settingsRepository.findById(115L)),
                createStep(3L, "", machineRepository.findById(79L), settingsRepository.findById(89L)),
                createStep(4L, "", machineRepository.findById(78L), settingsRepository.findById(105L)),
                createStep(5L, "", machineRepository.findById(20L), settingsRepository.findById(116L)),
                createStep(6L, "", machineRepository.findById(56L), settingsRepository.findById(90L)),
                createStep(7L, "", machineRepository.findById(61L), settingsRepository.findById(88L)),
                createStep(8L, "", machineRepository.findById(10L), settingsRepository.findById(86L)),
                createStep(9L, "", machineRepository.findById(44L), settingsRepository.findById(91L)),
                createStep(10L, "", machineRepository.findById(64L), settingsRepository.findById(94L)),
                createStep(11L, "", machineRepository.findById(45L), settingsRepository.findById(211L)),
                //art 08C6-KB
                createStep(12L, "", machineRepository.findById(30L), settingsRepository.findById(271L)),
                createStep(13L, "", machineRepository.findById(14L), settingsRepository.findById(266L)),
                createStep(14L, "", machineRepository.findById(14L), settingsRepository.findById(268L)),
                //TODO WARRING stap 45
                createStep(15L, "", machineRepository.findById(45L), settingsRepository.findById(269L)),
                //
                createStep(16L, "", machineRepository.findById(30L), settingsRepository.findById(260L)),
                createStep(17L, "", machineRepository.findById(30L), settingsRepository.findById(261L)),
                createStep(18L, "", machineRepository.findById(30L), settingsRepository.findById(262L)),
                createStep(19L, "", machineRepository.findById(30L), settingsRepository.findById(264L)),
                createStep(20L, "", machineRepository.findById(30L), settingsRepository.findById(265L)),
                //art 4C5-KB
                createStep(21L, "", machineRepository.findById(82L), settingsRepository.findById(259L)),
                createStep(22L, "", machineRepository.findById(20L), settingsRepository.findById(242L)),
                createStep(23L, "", machineRepository.findById(78L), settingsRepository.findById(243L)),
                createStep(24L, "", machineRepository.findById(22L), settingsRepository.findById(246L)),
                createStep(25L, "", machineRepository.findById(76L), settingsRepository.findById(247L)),
                createStep(26L, "", machineRepository.findById(10L), settingsRepository.findById(252L)),
                createStep(27L, "", machineRepository.findById(44L), settingsRepository.findById(253L)),
                createStep(28L, "", machineRepository.findById(45L), settingsRepository.findById(258L))

        );
        stepRepository.saveAll(steps);
    }

    private Step createStep(Long id, String name, Optional<Machine> machine, Optional<Setting> setting) {
        return Step.builder()
                .id(id)
                .name(name)
                .machine(machine.get())
                .setting(setting.get())
                .build();
    }
}
