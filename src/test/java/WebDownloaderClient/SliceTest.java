package WebDownloaderClient;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.nio.ByteBuffer;

import static org.junit.jupiter.api.Assertions.*;

class SliceTest {

    private Slice slice;

    @BeforeEach
    void setUp() {
        slice = new Slice(1, ByteBuffer.wrap(new byte[]{1, 2, 3}), "www.ynet.co.il");
        assertNotNull(slice);
    }

    @AfterEach
    void tearDown() {
        slice = null;
    }

    @Test
    void getSliceNum() {
        assertEquals(1, slice.getSliceNum());
    }

    @Test
    void setSliceNum() {
        slice.setSliceNum(5);
        assertEquals(5, slice.getSliceNum());
    }

    @Test
    void getContent() {
        assertEquals(3, slice.getContent().array().length);
        assertEquals(1, slice.getContent().array()[0]);
    }

    @Test
    void setContent() {
        slice.setContent(ByteBuffer.wrap(new byte[]{10, 20, 30, 40}));
        assertEquals(4, slice.getContent().array().length);
        assertEquals(10, slice.getContent().array()[0]);
    }

    @Test
    void getUrl() {
        assertEquals("www.ynet.co.il", slice.getUrl());
    }

    @Test
    void setUrl() {
        slice.setUrl("www.google.co.il");
        assertEquals("www.google.co.il", slice.getUrl());
    }
}