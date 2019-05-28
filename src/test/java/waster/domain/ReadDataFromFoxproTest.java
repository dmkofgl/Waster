package waster.domain;

import org.junit.Test;
import waster.domain.entity.Article;
import waster.domain.entity.ProcessMap;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class ReadDataFromFoxproTest {
    @Test
    public void tryToRead() {
        List<Article> articles = new ArrayList<>();
        try {
            final String filePath = System.getProperty("user.dir")
                    + "/src/test/resources/data/",
                    strName = "kartatp.dbf";
            String jdbURL = "jdbc:dbf:/."+"/src/main/resources/data/";

            Class.forName("com.caigen.sql.dbf.DBFDriver");

            Properties props = new Properties();
            props.setProperty("delayedClose", "0");

            Connection conn = DriverManager.getConnection(jdbURL, props);
            Statement stmt = conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);

            String sql = "select * from INFORMATION_SCHEMA.TABLES ";
            sql = "select kartatp.art, kartatp.naimk_o,kartatp.id, NKL from  kartatp inner join kartatp1 where kartatp.id =kartatp1.ID_KARTA ";
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
                String art =  rs.getObject(1)+(String) rs.getObject(2);
                ProcessMap  processMap = ProcessMap.builder()
                        .articleId(Long.valueOf((Integer) rs.getObject(3)))
                        .build();
                Article article  = Article.builder()
                        .name(art)
                        .processMap(processMap)
                        .coloring((String) rs.getObject(4))
                        .build();
//                for (int j = 1; j <= iNumCols; j++) {
//
//                    colval = rs.getObject(j);
//                    System.out.print(colval + "\t");
//                }
//                System.out.println();
                articles.add(article);
            }

            rs.close();


            stmt.close();

            conn.close();
        } catch (SQLException sqle) {
            do {
                System.out.println(sqle.getMessage());
                System.out.println("Error Code:" + sqle.getErrorCode());
                System.out.println("SQL State:" + sqle.getSQLState());
                sqle.printStackTrace();
            } while ((sqle = sqle.getNextException()) != null);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }

    }
}
