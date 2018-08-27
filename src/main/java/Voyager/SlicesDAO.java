package Voyager;

import java.util.Set;

public interface SlicesDAO {
    void insert(Slice s);
    Slice getSliceByUrlAndSliceNum(String url, int sliceNum);
    Set<Slice> getAllSlicesByUrl(String url);


}
