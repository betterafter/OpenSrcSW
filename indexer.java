import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

public class indexer {

    public class doc{
        public int id;
        public String content;
        public HashMap<String, Integer> tf = new HashMap<>();
    }

    public static ArrayList<doc> docList = new ArrayList<>();
    public static HashMap<String, Integer> df = new HashMap<>();


    public double weight(double tf, double df, double N){
        return Math.round(tf * Math.log(N / df) * 100) / 100.0;
    }

    // index.xml의 body를 doc 클래스로 저장
    public void parser(final String path) throws Exception {
        String content = "";
        String sdoc = "<doc id=\"", edoc = "</doc>";
        String sbody = "<body>", ebody = "</body>";

        File file = new File(path);
        if(file.isFile()){

            FileReader fr = new FileReader(file);
            BufferedReader bf = new BufferedReader(fr);

            String readLine = "";
            while((readLine = bf.readLine()) != null){
                content += readLine.trim();
            }

            bf.close();     
        }

        int sidx = content.indexOf(sdoc), eidx = content.indexOf(edoc, sidx);
        while(sidx >= 0){
            doc d = new doc();

            // document id 구하기
            int beforeId = sidx + sdoc.length() - 1, afterId = content.indexOf("\">", beforeId);
            String strId = content.substring(beforeId + 1, afterId);
            int iid = Integer.parseInt(strId);
            d.id = iid;

            // body 찾기
            int bsi = content.indexOf(sbody, sidx), bei = content.indexOf(ebody, bsi);
            String body = content.substring(bsi + sbody.length(), bei);
            d.content = body;
            
            
            // body에서 keyword:count를 분리
            int keyword_sidx = 0, keyword_modifier1 = keyword_sidx, keyword_modifier2 = keyword_sidx;
            while(keyword_modifier1 >= 0 && keyword_modifier2 >= 0){
                keyword_modifier1 = body.indexOf(":", keyword_sidx);
                keyword_modifier2 = body.indexOf("#", keyword_modifier1);

                if(keyword_modifier1 < 0 || keyword_modifier2 < 0) break;
                String kw = body.substring(keyword_sidx, keyword_modifier1);
                String cnt = body.substring(keyword_modifier1 + 1, keyword_modifier2);

                // df : 단어 x가 몇 개의 문서에서 등장하는 지 카운트하기 위해 HashMap<String, Integer> 형태로 저장
                if(df.containsKey(kw))
                    df.put(kw, df.get(kw) + 1);
                else df.put(kw, 1);

                // tf : 문서 y에서 단어 x가 등장한 횟수
                d.tf.put(kw, Integer.parseInt(cnt));
                
                keyword_sidx = keyword_modifier2 + 1;
            }

            docList.add(d);
            sidx = content.indexOf(sdoc, eidx); eidx = content.indexOf(edoc, sidx);
        }
        
        writeInvertedFile();
    }

    @SuppressWarnings({ "rawTypes", "unchecked", "nls" })
    public void writeInvertedFile() throws Exception {

        FileOutputStream fileStream = new FileOutputStream("index.post");
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileStream);

        // 전체 키워드를 담는 hashmap
        HashMap indexMap = new HashMap<>();

        // 문서를 돌면서 각 문서마다 keyword를 뽑아서 해당 keyword에 대한 hashmap을 생성하고 (이미 있으면 만들 필요 없음)
        // 그 hashmap에 key : 문서의 id, value : 가중치를 넣어서 저장하고 indexMap에 해당 Hashmap을 저장
        for(int i = 0; i < docList.size(); i++){
            doc d = docList.get(i);

            HashMap<String, Integer> tf = d.tf;
            Iterator<String> it = tf.keySet().iterator();
            while(it.hasNext()){
                String keyword = it.next();
                int value = tf.get(keyword);

                if(!indexMap.containsKey(keyword)){

                    ArrayList<Object> childList = new ArrayList<>();
                    childList.add(d.id); childList.add(weight(value, df.get(keyword), docList.size()));
                    indexMap.put(keyword, childList);
                }
                else{
                    ArrayList<Object> childList = (ArrayList<Object>)indexMap.get(keyword);
                    childList.add(d.id); childList.add(weight(value, df.get(keyword), docList.size()));
                }
            }
        }

        objectOutputStream.writeObject(indexMap);
        objectOutputStream.close();
    }

    @SuppressWarnings({ "rawTypes", "unchecked", "nls" })
    public void readInvertedFile(final String path) throws Exception {

        FileOutputStream resultFile = new FileOutputStream("result.txt");

        FileInputStream fileStream = new FileInputStream(path);
        ObjectInputStream objectInputStream = new ObjectInputStream(fileStream);

        Object object = objectInputStream.readObject();
        objectInputStream.close();

        
        HashMap hashMap = (HashMap)object;
        Iterator<String> it = hashMap.keySet().iterator();
        while(it.hasNext()){
            String keyword = it.next();
            ArrayList<Object> value = (ArrayList<Object>) hashMap.get(keyword);

            resultFile.write((keyword + '\n').getBytes());
            resultFile.write(value.toString().getBytes());
            resultFile.write(("\n").getBytes());
        }

        resultFile.close();
    }



    public void start(final String path) throws Exception{
        parser(path);

        // String readPath = "./index.post";
        // readInvertedFile(readPath);
    }
}