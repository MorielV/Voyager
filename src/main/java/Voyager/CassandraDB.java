package Voyager;

import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.Session;
import com.datastax.driver.core.exceptions.AlreadyExistsException;

/**
 * a singleton class represents our cassandra data base
 */
class CassandraDB {
    private Cluster cluster;
    private Session session;
    private String query;
    private String tableName;

    /**
     * private constructor
     */
    private CassandraDB() {
        cluster = Cluster.builder().addContactPoint("localhost").build();
        session = cluster.connect();
        createKeyspace("myks");
        createTable("slices");
        System.out.println("db constructed!");
    }

    /**
     * Holder for lazy initialization
     */
    private static class CassandraDBHolder {
        private final static CassandraDB INSTANCE = new CassandraDB();
    }

    /**
     * function to get the only cassandra instance
     *
     * @return object of CassandraDB
     */
    static CassandraDB getInstance() {
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
     * creating table
     *
     * @param tableName the name
     */
    private void createTable(String tableName) {
        this.tableName = tableName;
        query = "CREATE TABLE " + tableName + "(slice int, "
                + "content text, "
                + "url text , "
                + "PRIMARY KEY (slice,url));";
        try {
            session.execute(query);
        } catch (Exception ignored) {
        }
    }

    /**
     * inserting new slice to db.
     *
     * @param sliceNum slice id number
     * @param content  hex string of the bytes from the slice
     * @param url      web address
     */
    void insert(int sliceNum, String content, String url) {
        session.execute("INSERT INTO " + tableName + " (slice, content , url) VALUES (" + sliceNum + ",'" + content + "','" + url + "')");
    }

    /**
     * Select rows by url only
     *
     * @param url the web address
     * @return a ResultSet object, from which we can go over the rows.
     */
    ResultSet selectByURL(String url) {
        return session.execute("SELECT * FROM slices WHERE url='" + url + "' ALLOW FILTERING;");
    }

    /**
     * Select rows by url and slice part number
     *
     * @param url      the web address
     * @param sliceNum the slice
     * @return  a ResultSet object, from which we can go over the rows.
     */
    ResultSet selectByURLAndSlice(String url, int sliceNum) {
        return session.execute("SELECT * FROM slices WHERE url='" + url + "' AND slice=" + sliceNum + " ALLOW FILTERING;");
    }

    /**
     * close Cassandra data base
     */
    void close() {
        session.close();
        cluster.close();
    }
}
