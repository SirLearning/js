import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SubsampleReads {
    public static void main(String[] args) {
        SubsampleReads subsampleReads = new SubsampleReads();
        subsampleReads.Subsample(args[0], args[1], args[2], args[3]);
    }
    private static final Logger logger = LoggerFactory.getLogger(SubsampleReads.class);
    @SuppressWarnings("unused")
    public void Subsample(String infile, String outfile, String readsNum) {
        this.Subsample("1", infile, outfile, readsNum);
    }
    public void Subsample(String seed, String infile, String outfile, String readsNum) {
        long timeAll = System.currentTimeMillis();
        try {
            BufferedReader br = new BufferedReader(new FileReader(infile));
            BufferedWriter bw = new BufferedWriter(new FileWriter(outfile));
            String temp;
            HashMap<String, String> IDseq = new HashMap<>(700000);
            StringBuilder sb = new StringBuilder();
            HashSet<String> sampleSet = new HashSet<>(700000);

            long timeRead = System.currentTimeMillis();
            String ID = br.readLine().substring(1);
            while ((temp = br.readLine()) != null) {
                if (temp.startsWith("@")) {
                    IDseq.put(ID, sb.toString());
                    ID = temp.substring(1);
                    sb.delete(0, sb.length());
                    br.readLine();
                    br.readLine();
                } else {
                    sb.append(temp.substring(1));
                }
            }
            IDseq.put(ID, sb.toString());
            br.close();
            System.out.println("time cost on reading file: " + (System.currentTimeMillis() - timeRead));

            // shuffle
            long timeShuffle = System.currentTimeMillis();
            List<String> list_shuffle = new ArrayList<>(IDseq.keySet());
            Random rnd = new Random(Integer.parseInt(seed));
            Collections.shuffle(list_shuffle, rnd);
            // sample of reads
            int reads = Integer.parseInt(readsNum);
            sampleSet.addAll(list_shuffle.subList(0, reads));
            System.out.println("Shuffle and hashset time: " + (System.currentTimeMillis() - timeShuffle));

            long timeWrite = System.currentTimeMillis();
            long readCount = 0;
            for (String s : sampleSet) {
                bw.write(">" + s + "\n");
                bw.write(IDseq.get(s) + "\n");
                readCount++;
            }

            bw.flush();
            bw.close();
            System.out.println("final time: " + (System.currentTimeMillis() - timeWrite));
            System.out.println("final reads count: " + readCount);

        } catch (Exception e){
            logger.error("An error occurred while subsampling reads", e);
        }
        System.out.println("all time: " + (System.currentTimeMillis() - timeAll));
    }
}

