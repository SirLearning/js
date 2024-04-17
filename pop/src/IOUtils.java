import java.io.*;
import java.util.zip.GZIPInputStream;

public class IOUtils {

    public static BufferedWriter getOutfile(String outfile){
        BufferedWriter bw = null;
        if(outfile.endsWith("gz")){
            bw = IOUtils.getTextWriter(outfile);
        }else {
            bw = IOUtils.getTextWriter(outfile);
        }
        return bw;
    }

    public static BufferedReader getInfile(String infile){
        BufferedReader br = null;

        if(infile.endsWith("gz")){
            br = IOUtils.getTextGzipReader(infile);
        }else {
            br = IOUtils.getTextReader(infile);
        }
        return br;
    }

    public static BufferedWriter getTextWriter (String outfileS) {
        BufferedWriter bw = null;
        try {
            bw = new BufferedWriter (new FileWriter(outfileS), 65536);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return bw;
    }

    public static BufferedReader getTextReader (String infileS) {
        BufferedReader br = null;
        try {
            br = new BufferedReader (new FileReader(infileS), 65536);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return br;
    }


    public static BufferedReader getTextGzipReader(String infileS) {
        BufferedReader br = null;
        try {
            //br = new BufferedReader(new InputStreamReader(new MultiMemberGZIPInputStream(new FileInputStream(infileS))));
            br = new BufferedReader(new InputStreamReader(new GZIPInputStream(new FileInputStream(infileS), 65536)), 65536);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return br;
    }

    public static BufferedReader getTextGzipReader(String infileS, int bufferSize) {
        BufferedReader br = null;
        try {
            //br = new BufferedReader(new InputStreamReader(new MultiMemberGZIPInputStream(new FileInputStream(infileS))));
            br = new BufferedReader(new InputStreamReader(new GZIPInputStream(new FileInputStream(infileS), bufferSize)), bufferSize);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return br;
    }
}