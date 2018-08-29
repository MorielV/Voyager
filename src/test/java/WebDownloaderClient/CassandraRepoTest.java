package WebDownloaderClient;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CassandraRepoTest {

    private CassandraRepo repo;

    @BeforeEach
    void setUp() {
        repo = CassandraRepo.getInstance();
        assertNotNull(repo);
    }

    @AfterEach
    void tearDown() {
        repo = null;
    }

    @Test
    void getInstance() {
        repo = null;
        repo = CassandraRepo.getInstance();
        assertNotNull(repo);
    }

}