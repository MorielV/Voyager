package Voyager;

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
    //Getters and Setters

    public int getSliceNum() {
        return sliceNum;
    }

    public void setSliceNum(int sliceNum) {
        this.sliceNum = sliceNum;
    }

    public ByteBuffer getContent() {
        return content;
    }

    public void setContent(ByteBuffer content) {
        this.content = content;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
