package waster.domain.repository.init;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import waster.domain.entity.*;
import waster.domain.entity.calendar.Interruption;
import waster.domain.repository.abstracts.*;

import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.*;

@Component
public class DbInitFromFoxPro implements CommandLineRunner {
    @Autowired
    private ArticleRepository articleRepository;
    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private MachineRepository machineRepository;
    @Autowired
    private BenchRepository benchRepository;
    @Autowired
    private SettingsRepository settingsRepository;
    @Autowired
    private ProcessMapRepository processMapRepository;
    @Autowired
    private InterruptionRepository interruptionRepository;
    @Autowired
    private KPVRepository kpvRepository;

    @Value("${foxpro.path}")
    private String path;

    @Override
    public void run(String... args) throws Exception {
        String jdbURL ="jdbc:dbf:" + ( path != null ? path : "/./src/main/resources/Академия/of_proj/dbf");
        Class.forName("com.caigen.sql.dbf.DBFDriver");
        Properties props = new Properties();
        props.setProperty("delayedClose", "0");

        Connection conn = DriverManager.getConnection(jdbURL, props);

        copyArticles(conn);
        copyOrders(conn);
        copyMachines(conn);
        copyBenches(conn);
        readSettings(conn);
        copyProcessMap(conn);
        initInterruptions();
        copyKPV(conn);
    }

    public void copyKPV(Connection connection) throws SQLException {
        Statement stmt = connection.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
        String sql = "select" +
                " id," +//1
                "kod_ob," +//2
                "s_min," +//3
                "s_max," +//4
                "koef " +//5
                "from  sp_kpv";
        System.out.println(sql);
        ResultSet rs = stmt.executeQuery(sql);

        List<KPV> kpvs = new ArrayList<>();
        while (rs.next()) {
            Long id = rs.getLong(1);
            Long benchId = rs.getLong(2);
            Double minSpeed = rs.getDouble(3);
            Double maxSpeed = rs.getDouble(4);
            Double rate = rs.getDouble(5);
            Machine machine = machineRepository.findById(benchId).orElse(null);
            KPV kpv = KPV.builder()
                    .id(id)
                    .machine(machine)
                    .minSpeed(minSpeed)
                    .maxSpeed(maxSpeed)
                    .rate(rate)
                    .build();
            kpvs.add(kpv);
            kpvRepository.save(kpv);
            if (machine != null) {
                machine.addKPV(kpv);

                machineRepository.save(machine);
            }
        }
    }

    private void initInterruptions() throws ParseException {
        String sDate1 = "8/6/2019";
        String sDate2 = "10/6/2019";
        saveInterruption(sDate1, sDate2);
        sDate1 = "15/6/2019";
        sDate2 = "17/6/2019";
        saveInterruption(sDate1, sDate2);
        sDate1 = "22/6/2019";
        sDate2 = "24/6/2019";
        saveInterruption(sDate1, sDate2);
        sDate1 = "29/6/2019";
        sDate2 = "1/7/2019";
        saveInterruption(sDate1, sDate2);

    }

    private void saveInterruption(String startDate, String endDate) throws ParseException {
        Date date1 = new SimpleDateFormat("dd/MM/yyyy").parse(startDate);
        Date date2 = new SimpleDateFormat("dd/MM/yyyy").parse(endDate);
        Interruption interruption = new Interruption();
        interruption.setStart(date1);
        interruption.setEnd(date2);
        interruptionRepository.save(interruption);

    }

    private void copyArticles(Connection connection) throws SQLException {
        String sql = "select kartatp.art as artName, kartatp.naimk_o as modifyName, kartatp.id as processMapId, NKL as coloring " +
                "from  kartatp inner join kartatp1 " +
                "where kartatp.id =kartatp1.ID_KARTA ";
        Statement stmt = connection.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
        ResultSet rs = stmt.executeQuery(sql);


        List<Article> articles = new ArrayList<>();
        while (rs.next()) {
            String modify = (rs.getString(2) == null) ? "" : rs.getString(2);
            String art = rs.getString(1) + modify;
            String coloring = rs.getString(4);
            coloring = coloring == null ? "" : coloring;

            Article article = Article.builder()
                    .name(art)
                    .coloring(coloring)
                    .build();
            articles.add(article);
        }
        articleRepository.saveAll(articles);
    }

    private void copyProcessMap(Connection connection) throws SQLException {
        String selectArtWithColor = "select " +
                "kartatp.id as artId," +
                "kartatp1.id as colorId, " +
                "(kartatp.art + kartatp.naimk_o )as art," +
                "NKL as color " +
                " from kartatp inner join kartatp1 " +
                "where kartatp.id =kartatp1.ID_KARTA ";
        String select = " id_karta,id_karta1,num_p,id_nastr,art,color ";
        String sql = "select " +
                select +
                " from " +
                "(" + selectArtWithColor + ") as article " +
                "left join kartatp2 " +
                "on artId = kartatp2.id_karta and colorId = kartatp2.id_karta1 " +
                "order by id_karta,id_karta1,num_p";
        Statement stmt = connection.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
        ResultSet rs = stmt.executeQuery(sql);


        List<ProcessMap> processMaps = new ArrayList<>();
        while (rs.next()) {
            String art = rs.getString(5);
            String coloring = rs.getString(6);
            Long iterationIndex = rs.getLong(3);
            Long settingId = rs.getLong(4);
            Article article = articleRepository.findByColoringAndName(coloring, art).orElse(null);

            ProcessMap processMap;
            if (article == null) {
                processMap = ProcessMap.builder().build();
            } else {
                processMap = processMapRepository.getByArticleId(article.getId()).orElse(ProcessMap.builder().build());
//                processMap.setProcessMapId(article.getId());
                processMap.setArticleId(article.getId());
            }

            Setting setting = settingsRepository.findById(settingId).orElse(Setting.builder().id(settingId).build());

            processMap.getPath().add(iterationIndex, setting);
            processMaps.add(processMap);
            processMapRepository.save(processMap);
            if (article != null) {
                article.setProcessMap(processMap);
                articleRepository.save(article);
            }
        }
    }

    public void copyOrders(Connection connection) throws ClassNotFoundException, SQLException {

        Statement stmt = connection.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
        String sql = "select" +
                " plan_pr.art," +//1
                "plan_pr.metr," +//2
                "plan_pr.dats," +//3
                "plan_pr.nkol " +//4
                "from  plan_pr inner join kartatp " +
                "where plan_pr.art = kartatp.art + kartatp.naimk_o " +
                "and plan_pr.metr>0";
        System.out.println(sql);
        ResultSet rs = stmt.executeQuery(sql);

        List<Order> orders = new ArrayList<>();
        while (rs.next()) {
            String art = (String) rs.getObject(1);
            Double length = rs.getDouble(2) * 1000;
            Date expireDate = rs.getDate(3);
            String coloring = rs.getString(4);

            Article article = articleRepository.findByColoringAndName(coloring, art).orElse(null);
            if (article == null) {
                continue;
            }
            Order order = Order.builder()
                    .length(length)
                    .expireDate(expireDate)
                    .article(article)
                    .build();

            orders.add(order);
        }
        orderRepository.saveAll(orders);
    }

    @Transactional
    public List<Setting> readSettings(Connection connection) throws ClassNotFoundException, SQLException {
        Statement stmt = connection.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
        String sql = "select" +
                " z_nastr.id," +//1
                "z_nastr.kod_ob, " +//2
                "z_nastr.vmax " +//3
                "from  z_nastr ";
        System.out.println(sql);
        ResultSet rs = stmt.executeQuery(sql);

        List<Setting> settings = new ArrayList<>();
        while (rs.next()) {
            Long id = rs.getLong(1);
            Long machineId = rs.getLong(2);
            Double speed = rs.getDouble(3);
            boolean isDependOnLength = true;
            if (speed == 0) {
                isDependOnLength = false;
                //TODO crouch. stub default 2 hour
                speed = 2 * 60.0;

            }
            Machine machine = machineRepository.findById(machineId).get();

            Setting setting = Setting.builder()
                    .id(id)
                    .workingSpeed(speed)
                    .machine(machine)
                    .timeDependOnLength(isDependOnLength)
                    .build();
            settings.add(setting);
            Set<Setting> machineSetting = machine.getSetting();
            if (machineSetting == null) {
                machineSetting = new HashSet<>();
                machineSetting.add(setting);
                machine.setSetting(machineSetting);
            } else {
                machineSetting.add(setting);
            }
        }
        settingsRepository.saveAll(settings);
        return settings;
    }

    public void copyMachines(Connection connection) throws SQLException {
        Statement stmt = connection.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
        String sql = "select" +
                " sp_obor.id," +//1
                "sp_obor.naim," +//2
                "sp_obor.kod_ob " +//3
                "from  sp_obor";
        System.out.println(sql);
        ResultSet rs = stmt.executeQuery(sql);

        List<Machine> machines = new ArrayList<>();
        while (rs.next()) {
            Long id = rs.getLong(1);
            String name = rs.getString(2);
            Long oldId = rs.getLong(3);
            Machine machine = Machine.builder()
                    .numberNew(id)
                    .numberOld(oldId)
                    .name(name)
                    .build();

            machines.add(machine);
        }
        machineRepository.saveAll(machines);
    }

    public void copyBenches(Connection connection) throws SQLException {
        Statement stmt = connection.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
        String sql = "select" +
                " sp_nobor.id," +//1
                "sp_nobor.id_ob " +//2
                "from  sp_nobor " +
                "where pr_rab =0";
        System.out.println(sql);
        ResultSet rs = stmt.executeQuery(sql);

        List<Bench> benches = new ArrayList<>();
        while (rs.next()) {
            Long id = rs.getLong(1);
            Long machineId = rs.getLong(2);
            Machine machine = Machine.builder()
                    .numberNew(machineId)
                    .build();
            Bench bench = Bench.builder()
                    .id(id)
                    .machine(machine)
                    .build();

            benches.add(bench);
        }
        benchRepository.saveAll(benches);
    }
}
