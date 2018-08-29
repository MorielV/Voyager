package WebDownloaderClient;

import com.datastax.driver.core.*;

import java.nio.ByteBuffer;
import java.util.HashSet;
import java.util.Set;

public class SlicesDAOImpl implements SlicesDAOInterface {
    private Session session;

    SlicesDAOImpl(Session session) {
        this.session = session;
    }
    /**
     * Inserting a slice to Cassandra Data Base
     *
     * @param s slice to insert
     */
    @Override
    public void insert(Slice s){
        PreparedStatement statement =session.prepare("INSERT INTO Slices(slice, content , url) VALUES(?,?,?)");
        BoundStatement bound = new BoundStatement(statement);
        bound.setInt(0, s.getSliceNum());
        bound.setBytes(1, s.getContent());
        bound.setString(2, s.getUrl());
        session.execute(bound);
    }
    /**
     * Getting a slice by URL and Slice Number
     *
     * @param url      the web address
     * @param sliceNum the slice
     * @return new Slice object
     */
    @Override
    public Slice getSliceByUrlAndSliceNum(String url, int sliceNum) {
        // Avoiding SQL injection
        PreparedStatement statement =session.prepare("SELECT * FROM slices WHERE url = ? AND slice = ?");
        BoundStatement bound = new BoundStatement(statement);
        bound.setString(0, url);
        bound.setInt(1, sliceNum);
        ResultSet resultSet = session.execute(bound);
        Row row = resultSet.one();
        if(row==null)
            return null;
        ByteBuffer content = row.getBytes("content");
        return new Slice(sliceNum, content, url);
    }

    /**
     * Getting all slices by URL
     *
     * @param url the web address
     * @return Set of slices
     */
    @Override
    public Set<Slice> getAllSlicesByUrl(String url) {
        PreparedStatement statement =session.prepare("SELECT * FROM slices WHERE url = ?  ALLOW FILTERING;");
        BoundStatement bound = new BoundStatement(statement);
        bound.setString(0, url);
        ResultSet resultSet = session.execute(bound);
        return getSliceSetFromResult(resultSet,url);
}

    /**
     * get a slice set from result,
     * each row in result is a slice we add.
     * @param resultSet a result object returned from our query
     * @param url web address
     * @return set of slices
     */
    private Set<Slice> getSliceSetFromResult( ResultSet resultSet , String url){
        int sliceNum;
        ByteBuffer content;
        Set<Slice> sliceSet = new HashSet<>();
        for (Row row : resultSet) {
            sliceNum = row.getInt("slice");
            content = row.getBytes("content");
            sliceSet.add(new Slice(sliceNum, content, url));
        }
        return sliceSet;
    }
}