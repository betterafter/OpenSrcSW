import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import org.snu.ids.kkma.index.KeywordExtractor;
import org.snu.ids.kkma.index.KeywordList;

public class genSnippet{


    public static void main(String[] args) throws Exception {
        
        // java midterm -f input.txt -q "키워드열"
        // String fileName = args[3];
        String keyword = args[5];

        File file = new File("input.txt");

        FileReader fr = new FileReader(file);
        BufferedReader bf = new BufferedReader(fr);

        String readLine = "";
        ArrayList<String> list = new ArrayList<>();
        while((readLine = bf.readLine()) != null){
            list.add(readLine);
        }
        bf.close();

        for(int i = 0; i < list.size(); i++){
            System.out.println(list.get(i));
        }
        
        HashMap<String, Integer> m = new HashMap<>();

        int idx = 0;
        while(idx < keyword.length()){
            String kw = "";
            if(keyword.charAt(idx) == ' '){
                if(m.containsKey(kw)) m.put(kw, m.get(kw) + 1);
                else m.put(kw, 1);
                kw = "";
            }
            else{
                kw += keyword.charAt(idx);
            }
            
            idx++;
        }

        int size = list.size();
        Integer[] res = new Integer[size];
        for(int i = 0; i < list.size(); i++){
            String keyword1 = list.get(i);

            while(idx < keyword1.length()){
                String kw = "";
                if(keyword1.charAt(idx) == ' '){
                    if(m.containsKey(kw)) res[i]++;
                    kw = "";
                }
                else{
                    kw += keyword1.charAt(idx);
                }
                
                idx++;
            }
        }

        int ii = 0; int MAX = 0;
        for(int i = 0; i < list.size(); i++){
            if(MAX < res[i]){
                ii = i; MAX = res[i];
            }
        }
     
        System.out.println(list.get(ii));
    }

}