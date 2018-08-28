package Voyager;

import java.sql.SQLException;
import java.util.Set;

public interface SlicesDAOInterface {
    /**
     * Inserting a slice to Cassandra Data Base
     *
     * @param s slice to insert
     */
    void insert(Slice s) throws SQLException;
    /**
     * Getting a slice by URL and Slice Number
     *
     * @param url      the web address
     * @param sliceNum the slice
     * @return new Slice object
     */
    Slice getSliceByUrlAndSliceNum(String url, int sliceNum);
    /**
     * Getting all slices by URL
     *
     * @param url the web address
     * @return Set of slices
     */
    Set<Slice> getAllSlicesByUrl(String url);


}
