import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.util.*;

public class SubsampleByShuffle {


    public void Subsample(String infile, String bases, String outfile){
        this.Subsample("1", infile, bases, "11000", outfile);
    }



    /**
     * 读取文件并且构建ID和seq的list
     *
     * @param seed 随机种子
     * @param infile 输入文件
     * @param bases expected bases
     * @param averageLength 期望抽取总reads数
     * @param outfile 输出文件
     */

    public void Subsample(String seed, String infile, String bases, String averageLength, String outfile){
        long timeAll = System.currentTimeMillis();
        try{
            BufferedReader br = IOUtils.getInfile(infile);
            BufferedWriter bw = IOUtils.getOutfile(outfile);
            String temp;
//            List<String> list_ID = new ArrayList<>();
//            List<String> list_Seq = new ArrayList<>();
            HashMap<String,String> IDseq = new HashMap<>(700000);
            StringBuilder sb = new StringBuilder();
            HashSet<String> sampleSet = new HashSet<>(700000);

            long timeRead = System.currentTimeMillis();
//            list_ID.add(br.readLine().split(">")[1]);    // 先读一行
            String ID = br.readLine().substring(1);
            while ((temp = br.readLine()) != null) {
                // 由于seq并不是一行读完，需要不停的累计
                if (temp.startsWith(">")) {
//                    list_ID.add(temp.split(">")[1]);
                    IDseq.put(ID, sb.toString());
                    ID = temp.substring(1);
//                    list_Seq.add(sb.toString());
                    sb.delete(0, sb.length());    //clear
//                    sb.setLength(0);
                } else {
                    sb.append(temp);
                }
            }
//            list_Seq.add(sb.toString());    // 以上while循环使得最后一条read序列无法读取，因此在这读一下
            IDseq.put(ID, sb.toString());
            br.close();
            System.out.println("读取文件花费时间："+(System.currentTimeMillis()-timeRead));

            // shuffle
            long timeShuffle = System.currentTimeMillis();
            List list_shuffle = new ArrayList<>();
            list_shuffle.addAll(IDseq.keySet());
            Random rnd = new Random(Integer.parseInt(seed)); // 随机种子，测试
            Collections.shuffle(list_shuffle, rnd);

            // 需要抽取reads的条数
            int readsNum = Math.round(Float.parseFloat(bases)/Float.parseFloat(averageLength));    // expected number of reads
            sampleSet.addAll(list_shuffle.subList(0, readsNum)); // 这里测试一下，直接转为hashset还是循环添加快
            System.out.println("shuffle+导入hashset花费时间为：" + (System.currentTimeMillis()-timeShuffle));

            long timeWrite = System.currentTimeMillis();
            long sumBases = 0;
            long readsCount = 0;
            for(String s : sampleSet){
                bw.write(">" + s + "\n");
                // System.out.println(s);
                bw.write(IDseq.get(s) + "\n");
                readsCount++;
                sumBases += IDseq.get(s).length();
                if(sumBases > Long.parseLong(bases))break;

            }
            bw.flush();
            bw.close();
            System.out.println("最终抽取的bases数为："+(sumBases));
            System.out.println("最终抽取的reads数为："+(readsCount));
            System.out.println("写文件+计算总bases花费时间："+(System.currentTimeMillis()-timeWrite));




        }catch(Exception e){
            e.printStackTrace();
        }
        System.out.println("整个程序运行时间："+(System.currentTimeMillis()-timeAll));

    }
}
