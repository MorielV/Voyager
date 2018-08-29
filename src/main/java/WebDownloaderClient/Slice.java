package WebDownloaderClient;

import java.nio.ByteBuffer;

/**
 * A DTO class
 */
public class Slice {
    private int sliceNum;
    private ByteBuffer content;
    private String url;

    /**
     * constructor
     *
     * @param sliceNum slice Number
     * @param content  content
     * @param url      web address
     */
    Slice(int sliceNum, ByteBuffer content, String url) {
        this.sliceNum = sliceNum;
        this.content = content;
        this.url = url;
    }

    int getSliceNum() {
        return sliceNum;
    }

    void setSliceNum(int sliceNum) {
        this.sliceNum = sliceNum;
    }

    ByteBuffer getContent() {
        return content;
    }

    void setContent(ByteBuffer content) {
        this.content = content;
    }

    String getUrl() {
        return url;
    }

    void setUrl(String url) {
        this.url = url;
    }
}
