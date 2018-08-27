package Voyager;

/**
 * A DTO class
 */
public class Slice {
    private int sliceNum;
    private byte[] content;
    private String url;

    /**
     * constructor
     *
     * @param sliceNum slice Number
     * @param content  content
     * @param url      web address
     */
    Slice(int sliceNum, byte[] content, String url) {
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

    public byte[] getContent() {
        return content;
    }

    public void setContent(byte[] content) {
        this.content = content;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
