package Voyager;

import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.Row;

public class Cassandra_Tester {

    public static void main(String args[]) {
        CassandraDB db = CassandraDB.getInstance();
        db.insert(1, "blabla", "www.ynet.co.il");
        db.insert(2, "blabla2", "www.ynet.co.il");
        db.insert(3, "blabla3", "www.ynet.co.il");
        db.insert(4, "blabla4", "www.ynet.co.il");
        db.insert(1, "dladla1", "www.google.co.il");
        db.insert(2, "dladla2","www.google.co.il");
        db.insert(3, "dladla3","www.google.co.il");
        db.insert(4, "dladla4","www.google.co.il");
        String slice, content, url;
       // ResultSet resultset = db.selectByURL("www.ynet.co.il");
        ResultSet resultset = db.selectByURLAndSlice("www.google.co.il" , 2);
        for (Row row : resultset) {
            slice = Integer.toString(row.getInt("slice"));
            content = row.getString("content");
            url = row.getString("url");
            System.out.println("slice ID: " + slice);
            System.out.println("Content is: " + content);
            System.out.println("url is: " + url);
        }
        db.close();
    }


}
