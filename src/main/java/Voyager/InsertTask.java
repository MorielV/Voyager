package Voyager;

import java.nio.ByteBuffer;
import java.sql.SQLException;

public class InsertTask implements Runnable {
    private byte[] bytesRead;
    private int numOfBytes;
    private CassandraRepo repo;
    private String urlAsString;
    private int sliceNum;

    /**
     * constructor
     * @param bytesRead buffer with max 10kb content
     * @param numOfBytes number of actual bytes from the web page in the bytesRead right now
     * @param repo repository to handle data base insertion
     * @param urlAsString the web page address
     * @param sliceNum slice Number
     */
    InsertTask(byte[] bytesRead, int numOfBytes, CassandraRepo repo, String urlAsString, int sliceNum) {
        this.bytesRead = bytesRead;
        this.numOfBytes = numOfBytes;
        this.repo = repo;
        this.urlAsString=urlAsString;
        this.sliceNum=sliceNum;
    }

    /**
     *copying the actual bytes from our web page from bytesRead to temp,
     * and inserting new slice to cassandra db.
     */
    @Override
    public void run() {
        byte[] temp = new byte[numOfBytes];
        System.arraycopy(bytesRead, 0, temp, 0, numOfBytes);
        try {
            repo.slices.insert(new Slice(sliceNum, ByteBuffer.wrap(temp), urlAsString));
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
