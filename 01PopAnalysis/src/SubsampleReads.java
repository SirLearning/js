import java.io.*;
import java.util.*;

public class SubsampleReads {

    private static  final int CHUNK_SIZE = 10000;

    public static void main(String[] args) throws FileNotFoundException {
        SubsampleReads subsampleReads = new SubsampleReads();
        subsampleReads.Subsample(args[0], args[1], args[2], args[3]);
    }

    @SuppressWarnings("unused")
    public void Subsample(String infile, String outfile, String readsNum) throws FileNotFoundException {
        this.Subsample("1", infile, outfile, readsNum);
    }

    public void shuffleAndWriteTofile(HashMap<String, String> IDseq, Random rnd, BufferedWriter bw, int readNum) throws IOException {
        List<String> list_shuffle = new ArrayList<>(IDseq.keySet());
        Collections.shuffle(list_shuffle, rnd);
        for (int i = 0; i < readNum; i++) {
            String str = IDseq.get(list_shuffle.get(i));
            System.out.println(str);
            String[] lines = str.split("\t");
            bw.write(list_shuffle.get(i));
            bw.newLine();
            for (String line : lines) {
                bw.write(line);
                bw.newLine();
            }
        }
    }

    public void Subsample(String seed, String infile, String outfile, String readsNum) throws FileNotFoundException {
        long timeAll = System.currentTimeMillis();
        try {
            BufferedReader br = new BufferedReader(new FileReader(infile));
            BufferedWriter bw = new BufferedWriter(new FileWriter(outfile));
            HashMap<String, String> IDseq = new HashMap<>(700000);
            String ID = "";
            StringBuilder sequence = new StringBuilder();
            String str = "";

            Random rnd = new Random(Integer.parseInt(seed));
            String line;
            int lineCount = 0;

            while ((line = br.readLine()) != null) {
                ID = line;
                sequence.append(br.readLine()).append("\t");
                sequence.append(br.readLine()).append("\t");
                sequence.append(br.readLine());
                str = sequence.toString();
                IDseq.put(ID, str);
                sequence.delete(0, sequence.length());
                lineCount++;
                if (lineCount % CHUNK_SIZE == 0) {
                    shuffleAndWriteTofile(IDseq, rnd, bw, Integer.parseInt(readsNum));
                    IDseq.clear();
                }
            }
            br.close();
            bw.flush();
            bw.close();

        } catch (Exception e){
            e.printStackTrace();
        }
        System.out.println("all time: " + (System.currentTimeMillis() - timeAll));
    }
}

