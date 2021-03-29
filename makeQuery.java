import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import javax.print.Doc;

import org.snu.ids.kkma.index.Keyword;
import org.snu.ids.kkma.index.KeywordExtractor;
import org.snu.ids.kkma.index.KeywordList;

public class makeQuery{

    public HashMap<Integer, String> DocumentIdMap = new HashMap<>();

    // collection.xml 파일에서 title과 id 같이 가져와서 저장
    public void getDocumentTitleById(String path) throws Exception {
        
        File file = new File(path);
        if(file.isFile()){

            String content = "";
            String readLine = "";

            FileReader fr = new FileReader(file);
            BufferedReader bf = new BufferedReader(fr);

            // collection.xml 읽기
            while((readLine = bf.readLine()) != null){
                content += readLine.trim();
            } bf.close();

            String sdocId = "<doc id=\"", edocId = "\">";
            String stitle = "<title>", etitle = "</title>";

            int sdocIdx = content.indexOf(sdocId);
            int edocIdx = content.indexOf(edocId, sdocIdx);
            int tsIdx = content.indexOf(stitle, edocIdx);
            int teIdx = content.indexOf(etitle, tsIdx);

            while(sdocIdx >= 0){
                
                int id = Integer.parseInt(content.substring(sdocIdx + sdocId.length(), edocIdx));
                String title = content.substring(tsIdx + stitle.length(), teIdx);
                DocumentIdMap.put(id, title);

                sdocIdx = content.indexOf(sdocId, teIdx);
                edocIdx = content.indexOf(edocId, sdocIdx);
                tsIdx = content.indexOf(stitle, edocIdx);
                teIdx = content.indexOf(etitle, tsIdx);
            }
        }
    }

    public void CalcSim(String query, String path) throws Exception {

        KeywordExtractor ke = new KeywordExtractor();
        KeywordList kl = ke.extractKeyword(query, true);

        // query 내용 형태소 분석기로 분석 후 저장
        HashMap<String, Integer> keywordMap = new HashMap<>();
        for(int i = 0; i < kl.size(); i++){
            Keyword kword = kl.get(i);
            keywordMap.put(kword.getString(), kword.getCnt());
        }

        
        // index.post 가져오기
        FileInputStream fileStream = new FileInputStream(path);
        ObjectInputStream objectInputStream = new ObjectInputStream(fileStream);

        Object object = objectInputStream.readObject();
        objectInputStream.close();
        // 역파일을 hashMap으로 가져옴
        HashMap hashMap = (HashMap)object;

        int documentSize = DocumentIdMap.size();
        double[] Qid = new double[documentSize];
        // query에서 추출한 keyword를 탐색하면서 Qid에 저장
        Iterator<String> it = keywordMap.keySet().iterator();
        while(it.hasNext()){
            String keyword = it.next();
            System.out.println(keyword);
            ArrayList<Object> value = (ArrayList<Object>) hashMap.get(keyword);
            if(value == null) continue;
            for(int i = 0; i < value.size(); i+=2){
                int id = (int)value.get(i);
                double weight = (double)value.get(i + 1);
                Qid[id] += keywordMap.get(keyword) * weight;
            }
        }

        for(int i = 0; i < documentSize; i++){
            System.out.println(Qid[i] + "  ");
        }
    }

    // 파일 이름은?
    // 인자로 들어가는 것은?
    // 0이면 상위 3개를 어떻게 뽑는지?

    public static void main(String[] args) {
        
        try{
            String collectionPath = "./collection.xml";
            String q = "라면, 초밥, 스파게티, 초밥, 파스타, 초밥, 초밥, 초반, 초밥, 아이스, 라면, 초밥";
            String indexPath = "./index.post";

            makeQuery query = new makeQuery();
            query.getDocumentTitleById(collectionPath);
            query.CalcSim(q, indexPath);
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }

}