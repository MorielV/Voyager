package Voyager;

import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.Session;
import com.datastax.driver.core.exceptions.AlreadyExistsException;

/**
 * a singleton class represents our cassandra data base
 */
class CassandraRepo {
    private Cluster cluster;
    private Session session;
    SlicesDAO slices;

    /**
     * private constructor
     */
    private CassandraRepo() {
        cluster = Cluster.builder().addContactPoint("localhost").build();
        session = cluster.connect();
        slices = new SlicesDAOImpl(session);
        createKeyspace("myks");
        createTable();
    }

    /**
     * Holder for lazy initialization
     */
    private static class CassandraDBHolder {
        private final static CassandraRepo INSTANCE = new CassandraRepo();
    }

    /**
     * function to get the only cassandra instance
     *
     * @return object of CassandraRepo
     */
    static CassandraRepo getInstance() {
        return CassandraDBHolder.INSTANCE;
    }

    /**
     * creating keyspace
     *
     * @param keyspaceName the name
     */
    private void createKeyspace(String keyspaceName) {
        try {
            session.execute("CREATE KEYSPACE " + keyspaceName + " WITH REPLICATION =" + "{ 'class' : 'SimpleStrategy', 'replication_factor' : 1}; ");
        } catch (AlreadyExistsException ignored) {
        }
        session.execute("USE " + keyspaceName);
    }

    /**
     * creating table : slices(slice int, content text, url text , PRIMARY KEY( slice , url ))
     */
    private void createTable() {
        String query = "CREATE TABLE slices(slice int, "
                + "content text, "
                + "url text , "
                + "PRIMARY KEY (slice,url));";
        try {
            session.execute(query);
        } catch (Exception ignored) {
        }
    }

    /**
     * close Cassandra data base
     */
    void close() {
        session.close();
        cluster.close();
    }

    //    /**
//     * inserting new slice to db.
//     *
//     * @param sliceNum slice id number
//     * @param content  hex string of the bytes from the slice
//     * @param url      web address
//     */
//    void insert(int sliceNum, String content, String url) {
//        session.execute("INSERT INTO slices(slice, content , url) VALUES (" + sliceNum + ",'" + content + "','" + url + "')");
//    }

//    /**
//     * Select rows by url only
//     *
//     * @param url the web address
//     * @return a ResultSet object, from which we can go over the rows.
//     */
//    ResultSet selectByURL(String url) {
//        return session.execute("SELECT * FROM slices WHERE url='" + url + "' ALLOW FILTERING;");
//    }

//    /**
//     * Select rows by url and slice part number
//     *
//     * @param url      the web address
//     * @param sliceNum the slice
//     * @return a ResultSet object, from which we can go over the rows.
//     */
//    ResultSet selectByURLAndSlice(String url, int sliceNum) {
//        return session.execute("SELECT * FROM slices WHERE url='" + url + "' AND slice=" + sliceNum + " ALLOW FILTERING;");
//    }

//    /**
//     * Printing the result
//     *
//     * @param resultset object, from which we can go over the rows.
//     */
//    void printResult(ResultSet resultset) {
//        String slice;
//        String content;
//        String url;
//        System.out.println("Result : ");
//        int r = resultset.getAvailableWithoutFetching();
//        if (r == 0) {
//            System.out.println("\nNo slices were found");
//            return;
//        }
//        for (Row row : resultset) {
//            slice = Integer.toString(row.getInt("slice"));
//            content = row.getString("content");
//            url = row.getString("url");
//            System.out.println();
//            System.out.println("slice ID : " + slice);
//            System.out.println("Content is : " + content);
//            System.out.println("url is : " + url);
//        }
//    }


}
