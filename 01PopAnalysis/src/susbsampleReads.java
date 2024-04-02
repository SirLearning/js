import java.io.*;
import java.util.*;

public class susbsampleReads {

    private static  final int CHUNK_SIZE = 10000;
    private final Random rnd;

    public static void main(String[] args) {
        susbsampleReads ssReads = new susbsampleReads(args[0]);
        ssReads.subsample(args[1], args[2], args[3], args[4], args[5]);
    }

    public susbsampleReads(String seed) {
        this.rnd = new Random(Integer.parseInt(seed));
    }

    public void shuffleAndWriteTofile(HashMap<String, List<String>> IDseq, BufferedWriter bw, BufferedWriter bwR, int readNum) throws IOException {
        List<String> list_shuffle = new ArrayList<>(IDseq.keySet());
        Collections.shuffle(list_shuffle, rnd);
        for (int i = 0; i < readNum; i++) {
            String[] linesF = IDseq.get(list_shuffle.get(i)).get(0).split("\t");
            bw.write(list_shuffle.get(i));
            bw.write(" ");
            bw.write(linesF[0]);
            bw.newLine();
            for (int j = 1; j < 4; j++) {
                bw.write(linesF[j]);
                bw.newLine();
            }
            String[] linesR = IDseq.get(list_shuffle.get(i)).get(1).split("\t");
            bwR.write(list_shuffle.get(i));
            bwR.write(" ");
            bwR.write(linesR[0]);
            bwR.newLine();
            for (int j = 1; j < 4; j++) {
                bwR.write(linesR[j]);
                bwR.newLine();
            }
        }
    }

    public HashMap<String, List<String>> readLinesF(HashMap<String, List<String>> IDseq, String ID, BufferedReader br) throws IOException {
        String[] strID = ID.split(" ");
        String str = strID[1] + "\t" +
                br.readLine() + "\t" +
                br.readLine() + "\t" +
                br.readLine();
        List<String> strList = new ArrayList<>();
        strList.add(str);
        IDseq.put(strID[0], strList);
        return IDseq;
    }

    public HashMap<String, List<String>> readLinesR(HashMap<String, List<String>> IDseq, String ID, BufferedReader br) throws IOException {
        String[] strID = ID.split(" ");
        String str = strID[1] + "\t" +
                br.readLine() + "\t" +
                br.readLine() + "\t" +
                br.readLine();
        IDseq.get(strID[0]).add(str);
        return IDseq;
    }

    public void subsample(String infileF, String outfileF, String infileR, String outfileR, String readsNum) {
        long timeAll = System.currentTimeMillis();
        try {
            BufferedReader br = new BufferedReader(new FileReader(infileF));
            BufferedWriter bw = new BufferedWriter(new FileWriter(outfileF));
            BufferedReader brR = new BufferedReader(new FileReader(infileR));
            BufferedWriter bwR = new BufferedWriter(new FileWriter(outfileR));
            HashMap<String, List<String>> IDseq = new HashMap<>(700000);
            String ID = "";

            String line;
            int lineCount = 0;
            System.out.println(rnd.toString());

            while ((line = br.readLine()) != null) {
                IDseq = readLinesF(IDseq, line, br);
                IDseq = readLinesR(IDseq, brR.readLine(), brR);
                lineCount++;
                if (lineCount % CHUNK_SIZE == 0) {
                    shuffleAndWriteTofile(IDseq, bw, bwR, Integer.parseInt(readsNum));
                    IDseq.clear();
                }
            }
            br.close();
            bw.flush();
            bw.close();
            brR.close();
            bwR.flush();
            bwR.close();


        } catch (Exception e){
            e.printStackTrace();
        }
        System.out.println("all time: " + (System.currentTimeMillis() - timeAll));
    }
}

