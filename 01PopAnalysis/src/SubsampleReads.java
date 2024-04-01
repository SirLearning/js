

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.*;

public class SubsampleReads {
    public void Subsample(String infile, String outfile, String bases) {
        this.Subsample("1", infile, outfile, bases, "11000");
    }
    public void Subsample(String seed, String infile, String outfile, String bases, String averageLength) {
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
                if (temp.startsWith(">")) {
                    IDseq.put(ID, sb.toString());
                    ID = temp.substring(1);
                    sb.delete(0, sb.length());
                } else {
                    sb.append(temp);
                }
            }
            IDseq.put(ID, sb.toString());
            br.close();
            System.out.println("time cost on reading file: " + (System.currentTimeMillis() - timeRead));

            // shuffle
            long timeShuffle = System.currentTimeMillis();
            List list_shuffle = new ArrayList<>();
            list_shuffle.addAll(IDseq.keySet());
            Random rnd = new Random(Integer.parseInt(seed));
            Collections.shuffle(list_shuffle, rnd);

            // sample of reads
            int readsNum = Math.round(Float.parseFloat(bases)/Float.parseFloat(averageLength));
            sampleSet.addAll(list_shuffle.subList(0, readsNum));
            System.out.println("Shuffle and hashset time: " + (System.currentTimeMillis() - timeShuffle));

            long timeWrite = System.currentTimeMillis();
            long sumBases = 0;
            long readCount = 0;
            for (String s : sampleSet) {
                bw.write(">" + s + "\n");
                bw.write(IDseq.get(s) + "\n");
                readCount++;
                sumBases += IDseq.get(s).length();
                if (sumBases > Long.parseLong(bases)) break;
            }

            bw.flush();
            bw.close();
            System.out.println("final time: " + (System.currentTimeMillis() - timeWrite));
            System.out.println("final bases count: " + sumBases);
            System.out.println("final reads count: " + readCount);

        } catch (Exception e){
            e.printStackTrace();
        }
        System.out.println("all time: " + (System.currentTimeMillis() - timeAll));
    }
}
