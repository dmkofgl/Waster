package waster.domain;

import org.junit.Test;
import waster.domain.entity.*;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class ReadDataFromFoxproTest {
    @Test
    public void tryToRead() throws SQLException, ClassNotFoundException {
        List<Article> articles = new ArrayList<>();

        final String filePath = System.getProperty("user.dir")
                + "/src/test/resources/data/",
                strName = "kartatp.dbf";
        String jdbURL = "jdbc:dbf:/." + "/src/main/resources/Академия/of_proj/dbf";

        Class.forName("com.caigen.sql.dbf.DBFDriver");

        Properties props = new Properties();
        props.setProperty("delayedClose", "0");

        Connection conn = DriverManager.getConnection(jdbURL, props);
        Statement stmt = conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);

//            String sql = "select * from INFORMATION_SCHEMA.TABLES ";
        String sql = "select kartatp.art as artName, kartatp.naimk_o as modifyName, kartatp.id as processMapId, NKL as coloring " +
                "from  kartatp inner join kartatp1 " +
                "where kartatp.id =kartatp1.ID_KARTA ";
        System.out.println(sql);
        ResultSet rs = stmt.executeQuery(sql);
        ResultSetMetaData resultSetMetaData = rs.getMetaData();
        int iNumCols = resultSetMetaData.getColumnCount();
        for (int j = 1; j <= iNumCols; j++) {
            System.out.println(resultSetMetaData.getColumnName(j)
                    + "  " + resultSetMetaData.getColumnTypeName(j)
                    + "  " + resultSetMetaData.getPrecision(j)
                    + "  " + resultSetMetaData.getScale(j)
            );
        }
        Object colval;

        while (rs.next()) {
            String art = rs.getObject(1) + (String) rs.getObject(2);

            Article article = Article.builder()
                    .name(art)
                    .coloring((String) rs.getObject(4))
                    .build();
            articles.add(article);
        }
    }

    @Test
    public void readOrders() throws ClassNotFoundException, SQLException {
        final String filePath = System.getProperty("user.dir")
                + "/src/test/resources/data/",
                strName = "kartatp.dbf";
        String jdbURL = "jdbc:dbf:/." + "/src/main/resources/Академия/of_proj/dbf";

        Class.forName("com.caigen.sql.dbf.DBFDriver");

        Properties props = new Properties();
        props.setProperty("delayedClose", "0");

        Connection conn = DriverManager.getConnection(jdbURL, props);
        Statement stmt = conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
        String sql = "select" +
                " plan_pr.art," +//1
                "plan_pr.metr," +//2
                "plan_pr.dats," +//3
                "kartatp.id," +//4
                "plan_pr.nkol " +//5
                "from  plan_pr inner join kartatp " +
                "where plan_pr.art = kartatp.art + kartatp.naimk_o";
        System.out.println(sql);
        ResultSet rs = stmt.executeQuery(sql);

        List<Order> orders = new ArrayList<>();
        while (rs.next()) {
            String art = (String) rs.getObject(1);
            Double length = rs.getDouble(2);
            Date expireDate = rs.getDate(3);
            Long artId = rs.getLong(4);
            String coloring = rs.getString(5);
            Article article = Article.builder()
                    .id(artId)
                    .name(art)
                    .coloring(coloring)
                    .build();
            Order order = Order.builder()
                    .length(length)
                    .expireDate(expireDate)
                    .article(article)
                    .build();

            orders.add(order);
        }
    }

    @Test
    //TODO add settings and DONE
    public void readMachines() throws ClassNotFoundException, SQLException {
        final String filePath = System.getProperty("user.dir")
                + "/src/test/resources/data/",
                strName = "kartatp.dbf";
        String jdbURL = "jdbc:dbf:/." + "/src/main/resources/Академия/of_proj/dbf";

        Class.forName("com.caigen.sql.dbf.DBFDriver");

        Properties props = new Properties();
        props.setProperty("delayedClose", "0");

        Connection conn = DriverManager.getConnection(jdbURL, props);
        Statement stmt = conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
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
    }

    @Test
    public void readBenches() throws ClassNotFoundException, SQLException {
        final String filePath = System.getProperty("user.dir")
                + "/src/test/resources/data/",
                strName = "kartatp.dbf";
        String jdbURL = "jdbc:dbf:/." + "/src/main/resources/Академия/of_proj/dbf";

        Class.forName("com.caigen.sql.dbf.DBFDriver");

        Properties props = new Properties();
        props.setProperty("delayedClose", "0");

        Connection conn = DriverManager.getConnection(jdbURL, props);
        Statement stmt = conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
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
    }

    @Test
    public void readSettings() throws ClassNotFoundException, SQLException {
        final String filePath = System.getProperty("user.dir")
                + "/src/test/resources/data/",
                strName = "kartatp.dbf";
        String jdbURL = "jdbc:dbf:/." + "/src/main/resources/Академия/of_proj/dbf";

        Class.forName("com.caigen.sql.dbf.DBFDriver");

        Properties props = new Properties();
        props.setProperty("delayedClose", "0");

        Connection conn = DriverManager.getConnection(jdbURL, props);
        Statement stmt = conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
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
            Machine machine = Machine.builder()
                    .numberNew(machineId)
                    .build();
            Setting setting = Setting.builder()
                    .id(id)
                    .workingSpeed(speed)
                    .machine(machine)
                    .build();
            settings.add(setting);
        }
    }
}

