package WebDownloaderClient;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.nio.ByteBuffer;
import java.sql.SQLException;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

class SlicesDAOImplTest {
    private CassandraRepo repo;
    private SlicesDAOInterface slices ;
    @BeforeEach
    void setUp() {
        repo = CassandraRepo.getInstance();
        slices = repo.slices;
    }

    @AfterEach
    void tearDown() {
        repo = null;
        slices = null;
    }

    @Test
    void TestInsertAndGetSliceByUrlAndSliceNum() throws SQLException {
        Slice s = new Slice(1, ByteBuffer.wrap(new byte[]{50,60,70,40}) ,"forTest");
        slices.insert(s);
        Slice result = slices.getSliceByUrlAndSliceNum("forTest",1);
        assertEquals(4,result.getContent().array().length);
        assertEquals(70,result.getContent().array()[2]);

        s = new Slice(1, ByteBuffer.wrap(new byte[]{10,20,30}) ,"forTest");
        slices.insert(s);
        result = slices.getSliceByUrlAndSliceNum("forTest",1);
        assertEquals(3,result.getContent().array().length);
        assertEquals(10,result.getContent().array()[0]);

    }

    @Test
    void getAllSlicesByUrl() throws SQLException {
        Slice s1 = new Slice(1, ByteBuffer.wrap(new byte[]{50,60,70,40}) ,"forTest");
        Slice s2 = new Slice(2, ByteBuffer.wrap(new byte[]{50,60,70,40}) ,"forTest");
        Slice s3 = new Slice(3, ByteBuffer.wrap(new byte[]{50,60,70,40}) ,"forTest");
        Slice s4 = new Slice(4, ByteBuffer.wrap(new byte[]{50,60,70,40}) ,"forTest");
        slices.insert(s1);
        slices.insert(s2);
        slices.insert(s3);
        slices.insert(s4);
        Set<Slice> set=slices.getAllSlicesByUrl("forTest");
        for (Slice s : set){
            assertEquals(4,s.getContent().array().length);
            assertEquals(50,s.getContent().array()[0]);
        }
    }
}