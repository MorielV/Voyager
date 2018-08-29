package WebDownloaderClient;

import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.Session;
import com.datastax.driver.core.exceptions.AlreadyExistsException;

/**
 * A singleton class represents our data base repository
 */
class CassandraRepo {
    private Cluster cluster;
    private Session session;
    SlicesDAOInterface slices;

    /**
     * Private constructor
     */
    private CassandraRepo() {
        cluster = Cluster.builder().addContactPoint("localhost").build();
        session = cluster.connect();
        slices = new SlicesDAOImpl(session);
        createKeyspace("myks");
        createTable();
    }

    public boolean isClosed() {
        return (session.isClosed()&&cluster.isClosed());
    }

    /**
     * Holder for lazy initialization
     */
    private static class CassandraDBHolder {
        private final static CassandraRepo INSTANCE = new CassandraRepo();
    }

    /**
     * Function to get the only repository instance
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
                + "content blob, "
                + "url text, "
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
}
