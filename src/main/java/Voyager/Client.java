package Voyager;

import org.apache.commons.codec.binary.Base32;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import java.sql.SQLException;
import java.util.Set;

class Client {
    private CassandraRepo repo;
    private Base32 base32;

    private Client() {
        repo = CassandraRepo.getInstance();
        base32 = new Base32();
    }

    /**
     * main function, just checking number of arguments and sending to the right function to handle.
     *
     * @param args program argument
     */
    public static void main(String[] args) {
        //can make as private func to get argument , should make a test for it
        final Client client = new Client();
        if (args.length == 2) {
            if (args[0].equals("put"))                                  //put www.ynet.co.il
                client.storeWebPage(args[1]);
            else client.printAllSlicesByUrl(args[1]);                   //get www.ynet.co.il
        } else if (args.length == 3)                                    //get www.ynet.co.il 0
            client.printSliceByUrlAndSliceNum(args[1], Integer.parseInt(args[2]));
        client.exit();
    }

    /**
     * Storing a web page in cassandra
     */
    private void storeWebPage(String urlAsString) {
        String urlWithProtocol;                             // will be url with protocol
        final int limit = 10240;                            // this is 10kb in binary.
        final byte[] bytesRead = new byte[limit];           // array of bytes that we read from input stream

        //making url with protocol if not mentioned
        if (!urlAsString.startsWith("http://") && !urlAsString.startsWith("https://"))
            urlWithProtocol = "https://" + urlAsString;
        else
            urlWithProtocol = urlAsString;

        try {
            URL url = new URL(urlWithProtocol);            //creating url
            InputStream is = url.openStream();             //opening a input stream to this url
            readInputStreamAndStoreSlices(is, bytesRead, urlAsString);
            is.close();
        } catch (UnknownHostException e) {
            System.out.println("Error : Web address does not exist.");
        } catch (IOException | SQLException e) {
            System.out.println("Error : Bad Input.");
        }
    }

    /**
     * function with a for loop,
     * Every iteration reading from input stream to bytesRead,
     * copying to new array of size numOfBytes which is the return value of read(...),
     * and storing a new slice with the new array wrapped by ByteBuffer which is the content of the current slice
     *
     * @param is          input stream from which we read
     * @param bytesRead   bytes array of length limit that we use to store bytes read from input stream
     * @param urlAsString url as a string
     * @throws IOException
     * @throws SQLException
     */
    private void readInputStreamAndStoreSlices(InputStream is, byte[] bytesRead, String urlAsString) throws IOException, SQLException {
        int numOfBytes;
        for (int i = 0; ((numOfBytes = is.read(bytesRead, 0, bytesRead.length)) != -1); i++) {
            byte[] temp = new byte[numOfBytes];
            System.arraycopy(bytesRead, 0, temp, 0, numOfBytes);
            repo.slices.insert(new Slice(i, ByteBuffer.wrap(temp), urlAsString));
        }
    }

    /**
     * Get all Slices of the given URL
     *
     * @param urlAsString given URL as string
     */
    private void printAllSlicesByUrl(String urlAsString) {
        Set<Slice> sliceSet = repo.slices.getAllSlicesByUrl(urlAsString);
        for (Slice slice : sliceSet) {
            printSlice(slice);
        }
    }

    /**
     * Get a specific slice from db
     *
     * @param urlAsString given URL as string
     * @param SliceNum    given Slice Number
     */
    private void printSliceByUrlAndSliceNum(String urlAsString, int SliceNum) {
        Slice slice = repo.slices.getSliceByUrlAndSliceNum(urlAsString, SliceNum);
        if (slice == null)
            return;
        printSlice(slice);
    }

    /**
     * printing a single slice
     * @param slice DTO object
     */
    private void printSlice(Slice slice) {
        System.out.println("\nSlice Number : " + slice.getSliceNum());
        System.out.println("Content : " + base32.encodeAsString(slice.getContent().array()));
        System.out.println("URL : " + slice.getUrl());
    }

    /**
     * closing the data base
     */
    private void exit() {
        repo.close();
    }
}