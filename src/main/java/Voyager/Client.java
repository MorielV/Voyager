package Voyager;

import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.Row;
import org.apache.commons.codec.binary.Base32;

import java.io.*;
import java.net.URL;
import java.util.Scanner;

class Client {
    private CassandraDB db = CassandraDB.getInstance();
    private static Scanner reader = new Scanner(System.in);
    private static int limit = 10240; // this is 10kb in binary.
    private URL url;
    private InputStream is = null;
    private BufferedReader br;
    private String line;
    private byte[] bytesRead = new byte[limit];
    private int numOfBytes;
    private static Base32 base32 = new Base32();

    void storeWebPage() {
        System.out.println("Please Enter a web address to store");
        String urlAsString = reader.nextLine();
        try {

            url = new URL(urlAsString);
            is = url.openStream();
            int i = 0; //slice number
            while(is.read()){
                try {
                    ((DataInputStream) is).readFully(bytesRead);
                } catch (EOFException e) {
                    e.printStackTrace();
                }
            }while()
                System.out.println("in loop!!!");
                db.insert(i, base32.encodeAsString(bytesRead), urlAsString);
                i++;
            System.out.println("The number of bytes read at last was :: " + numOfBytes);
            db.insert(i, base32.encodeAsString(bytesRead), urlAsString); // one last time
        } catch (IOException e) {
            e.printStackTrace();
        }
//////////////////////////////////////////////////////////////
        String slice, content, url;
        ResultSet resultset = db.selectByURL("https://www.ynet.co.il/");
        //ResultSet resultset = db.selectByURLAndSlice("www.google.co.il" , 2);
        for (Row row : resultset) {
            slice = Integer.toString(row.getInt("slice"));
            content = row.getString("content");
            url = row.getString("url");
            System.out.println("slice ID: " + slice);
            System.out.println("Content is : " + content);
            System.out.println("url is: " + url);
        }
        db.close();
//////////////////////////////////////////////////////////////
    }
}