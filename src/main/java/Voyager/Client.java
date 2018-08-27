package Voyager;

import org.apache.commons.codec.binary.Base32;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.Set;

class Client {
    private CassandraRepo repo = CassandraRepo.getInstance();
    private Base32 base32 = new Base32();
    /**
     * Storing a web page in cassandra
     */
    void storeWebPage(String urlAsString) {
        while (true) {
            String urlWithProtocol;
            final int limit = 10240; // this is 10kb in binary.
            final byte[] bytesRead = new byte[limit];
            final Base32 base32 = new Base32();
            if (!urlAsString.startsWith("http://") && !urlAsString.startsWith("https://"))
                urlWithProtocol = "https://" + urlAsString;
            else
                urlWithProtocol = urlAsString;
            try {
                URL url = new URL(urlWithProtocol);
                InputStream is = url.openStream();
                int numOfBytes;
                for (int i = 0; ((numOfBytes = is.read(bytesRead, 0, limit)) != -1); i++) {
                    byte[] temp = new byte[numOfBytes];
                    System.arraycopy(bytesRead, 0, temp, 0, numOfBytes);
                    repo.slices.insert(new Slice(i, temp, urlAsString));
                }
                is.close();
                break;
            } catch (UnknownHostException e) {
                System.out.println("Error: Web address does not exist\nTry again.");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Get all Slices of the given URL
     *
     * @param urlAsString given URL as string
     */
    void printAllSlicesByUrl(String urlAsString) {
        Set<Slice> sliceSet  = repo.slices.getAllSlicesByUrl(urlAsString);
        for(Slice slice : sliceSet){
            System.out.println("\nSlice Number : " + slice.getSliceNum());
            System.out.println("Content : " + base32.encodeAsString(slice.getContent()));
            System.out.println("URL : " + slice.getUrl());
        }
    }

    /**
     * Get a specific slice from db
     *
     * @param urlAsString given URL as string
     * @param SliceNum    given Slice Number
     */
    void printSliceByUrlAndSliceNum(String urlAsString, int SliceNum) {
        Slice slice = repo.slices.getSliceByUrlAndSliceNum(urlAsString, SliceNum);
        System.out.println("\nSlice Number : " + slice.getSliceNum());
        System.out.println("Content : " + base32.encodeAsString(slice.getContent()));
        System.out.println("URL : " + slice.getUrl());
    }

    /**
     * closing the data base
     */
    void exit() {
        repo.close();
    }
}