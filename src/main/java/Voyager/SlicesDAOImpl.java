package Voyager;

import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.Row;
import com.datastax.driver.core.Session;
import org.apache.commons.codec.binary.Base32;

import java.util.HashSet;
import java.util.Set;

public class SlicesDAOImpl implements SlicesDAO {
    private Base32 base32 = new Base32();
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
    public void insert(Slice s) {
        byte[] content = s.getContent();
        String contentAsString = base32.encodeAsString(content);
        session.execute("INSERT INTO Slices(slice, content , url) VALUES (" + s.getSliceNum() + ",'" + contentAsString + "','" + s.getUrl() + "')");
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
        ResultSet resultSet = session.execute("SELECT * FROM slices WHERE url='" + url + "' AND slice=" + sliceNum + " ALLOW FILTERING;");
        Row row = resultSet.one();
        String contentAsString = row.getString("content");
        byte[] content = base32.decode(contentAsString);
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
        ResultSet resultSet = session.execute("SELECT * FROM slices WHERE url='" + url + "' ALLOW FILTERING;");
        int sliceNum;
        String contentAsStirng;
        byte[] content;
        Set<Slice> sliceSet = new HashSet<>();
        for (Row row : resultSet) {
            sliceNum = (row.getInt("slice"));
            contentAsStirng = row.getString("content");
            content = base32.decode(contentAsStirng);
            sliceSet.add(new Slice(sliceNum, content, url));
        }
        return sliceSet;
    }
}
