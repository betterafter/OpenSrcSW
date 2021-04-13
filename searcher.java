import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.SortedMap;

import javax.print.Doc;

import org.snu.ids.kkma.index.Keyword;
import org.snu.ids.kkma.index.KeywordExtractor;
import org.snu.ids.kkma.index.KeywordList;

public class searcher{

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

    public void Analyzer(String query, String path) throws Exception {

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
        ArrayList<ArrayList<Double> > docWeight = new ArrayList<>();
        for(int i = 0; i < documentSize + 1; i++) docWeight.add(new ArrayList<>());

        // query에서 추출한 keyword를 탐색하면서 Qid에 저장
        Iterator<String> it = keywordMap.keySet().iterator();
        while(it.hasNext()){
            String keyword = it.next();
            ArrayList<Object> value = (ArrayList<Object>) hashMap.get(keyword);
            if(value == null) continue;
            for(int i = 0; i < value.size(); i+=2){

                int id = (int)value.get(i);
                docWeight.get(id).add((double)keywordMap.get(keyword));
                docWeight.get(id).add((double)value.get(i + 1));
            }
        }

        double[] Qid = CalcSim(documentSize,docWeight);

        HashMap<Integer, Double> sortMap = new HashMap<>(); 
        for(int i = 0; i < documentSize; i++){ 
            sortMap.put(i, Qid[i]);
            //System.out.println(Qid[i] + " ");
        }
        ArrayList<Integer> sortedList = DocumentSort(sortMap);
            
        for(int i = 0; i < 3; i++){
            int id = sortedList.get(i);
            System.out.println(DocumentIdMap.get(id));
        }
    
    }

    public double[] InnerProduct(int documentSize, ArrayList<ArrayList<Double> > docWeight){

        double[] Qid = new double[documentSize + 1];


        for(int i = 1; i <= documentSize; i++){
            for(int j = 0; j < docWeight.get(i).size(); j+=2){
                Qid[i] += docWeight.get(i).get(j) * docWeight.get(i).get(j + 1); 
                //System.out.println(Qid[i]);
            }
        }

        return Qid;
    }

    // 0. master 브랜치에 체크아웃한다.
    // 1. searcher를 searcher_2의 내용으로 바꾼다. 이 때 바뀔 내용은 지운다. master에 우선 커밋 & 푸쉬한다.
    // 2. feature -> master로 머지해서 충돌을 발생시켜본다. 완전 같은 내용에서 CalcSim만 조금 달라졌으니 충돌이 날 것이다.
    // 3. feature 브랜치로 이동해서 CalcSim을 InnerProduct로 바꾼다. (searcher3로 바꾼다)
    // 4. master 브랜치로 체크아웃한 후 feature 브랜치를 머지한다.
    // 5. master 브랜치의 머지된 searcher의 CalcSim에서 InnerProduct를 사용하도록 구현한다. (searcher_3으로 바꾼다)

    public static ArrayList<Integer> DocumentSort(final HashMap<Integer, Double> sortMap){
        ArrayList<Integer> sortedList = new ArrayList<>();
        sortedList.addAll(sortMap.keySet());

        Collections.sort((ArrayList<Integer>)sortedList, new Comparator<Object>(){
            public int compare(Object o1, Object o2){
                if(sortMap.get(o1) - sortMap.get(o2) == 0){
                    return -((Comparable) (int)o1).compareTo((Comparable) ((int)o2));            
                }
                return ((Comparable) (Double)sortMap.get(o1)).compareTo((Comparable) ((Double)sortMap.get(o2)));
            }
        });
        Collections.reverse(sortedList);
        return sortedList;
    }

    public void start(String path, String query, String collectionPath) throws Exception{
        getDocumentTitleById(collectionPath);
        Analyzer(query, path);
    }
}