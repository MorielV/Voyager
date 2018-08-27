package Voyager;

import java.sql.SQLException;
import java.util.Set;

public interface SlicesDAOInterface {
    void insert(Slice s) throws SQLException;
    Slice getSliceByUrlAndSliceNum(String url, int sliceNum);
    Set<Slice> getAllSlicesByUrl(String url);


}
