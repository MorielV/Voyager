package WebDownloaderClient;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class InsertTaskTest {
    private InsertTask task;
    private CassandraRepo repo = CassandraRepo.getInstance();
    @BeforeEach
    void setUp() {
        task= new InsertTask(new byte[]{1,2,3},1,repo,"www.ynet.co.il",0 );
        assertNotNull(task);
    }

    @AfterEach
    void tearDown() {
        task = null;
    }

    @Test
    void run() throws InterruptedException {
        Thread t1 = new Thread(task);
        t1.start();
        t1.join();
        Slice s =repo.slices.getSliceByUrlAndSliceNum("www.ynet.co.il",0);
        assertEquals(1,s.getContent().array().length);
        assertEquals(1,s.getContent().array()[0]);
    }
}