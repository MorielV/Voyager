package WebDownloaderClient;

import org.apache.commons.codec.binary.Base32;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.UnknownHostException;
import java.sql.SQLException;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

class Client {
    private CassandraRepo repo;
    private Base32 base32;
    private ExecutorService service;

    private Client() {
        repo = CassandraRepo.getInstance();
        base32 = new Base32();
        service = Executors.newFixedThreadPool(10);

    }

    /**
     * main function, just checking number of arguments and sending to the right function to handle.
     *
     * @param args program argument
     */
    public static void main(String[] args) {
        final Client client = new Client();
        try {
            checkArgsAndCallProperFunction(client, args);
        }catch (Exception e){
            //bad input do nothing
        }
        client.exit();
    }

    private static void checkArgsAndCallProperFunction(Client client, String[] args) {
        //comment
        if (args.length == 2) {
            if (args[0].toLowerCase().equals("put"))                                 //put www.ynet.co.il
                client.storeWebPage(args[1].toLowerCase());
            else if (args[0].toLowerCase().equals("get"))
                client.printAllSlicesByUrl(args[1].toLowerCase());                   //get www.ynet.co.il
        } else if (args.length == 3 && (args[0].toLowerCase().equals("get"))) {      //get www.ynet.co.il 0
            client.printSliceByUrlAndSliceNum(args[1].toLowerCase(), Integer.parseInt(args[2]));
        }
    }

    /**
     * Storing a web page in cassandra
     */
    private void storeWebPage(String urlAsString) {
        String urlWithProtocol;
        final int limit = 10240;
        final byte[] bytesRead = new byte[limit];

        //making url with protocol if not mentioned
        if (!urlAsString.startsWith("http://") && !urlAsString.startsWith("https://"))
            urlWithProtocol = "https://" + urlAsString;
        else
            urlWithProtocol = urlAsString;

        try {
            URL url = new URL(urlWithProtocol);
            InputStream is = url.openStream();
            readInputStreamAndStoreSlices(is, bytesRead, urlAsString);
            System.out.println("Web Page Stored");
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
     * sending threads to handle the insert task for us .
     *
     * @param is          input stream from which we read
     * @param bytesRead   bytes array of length limit that we use to store bytes read from input stream
     * @param urlAsString url as a string
     * @throws IOException  .
     * @throws SQLException .
     */
    private void readInputStreamAndStoreSlices(InputStream is, byte[] bytesRead, String urlAsString) throws IOException, SQLException {
        int numOfBytes;
        for (int i = 0; ((numOfBytes = is.read(bytesRead, 0, bytesRead.length)) != -1); i++) {
            service.execute(new InsertTask(bytesRead, numOfBytes, repo, urlAsString, i));
            bytesRead = new byte[bytesRead.length]; //create a new object with different address for the next thread
        }
        service.shutdown();
        try {
            service.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
        } catch (InterruptedException e) {
            System.out.println("Thread interrupted!");
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
     *
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