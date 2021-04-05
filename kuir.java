import java.io.*;

import org.snu.ids.kkma.*;
import org.snu.ids.kkma.index.Keyword;
import org.snu.ids.kkma.index.KeywordExtractor;
import org.snu.ids.kkma.index.KeywordList;

public class kuir {

    public static void main(String[] args) {
        try{
            makeCollection collection = new makeCollection();
            makeKeyword keyword = new makeKeyword();
            indexer indexer = new indexer();
            searcher searcher = new searcher();

            if(args.length == 0) return;

            if(args[0].equals("-c")){
                final String path = args[1];
                collection.start(path);
            }
            else if(args[0].equals("-k")){
                final String path = args[1];
                keyword.start(path);
            }
            else if(args[0].equals("-i")){
                final String path = args[1];
                indexer.start(path); 
            }
            else if(args[0].equals("-s") && args[2].equals("-q")){
                final String path = args[1];
                String query = "";
                for(int i = 3; i < args.length; i++){
                    query += args[i];
                }
                final String collectionPath = "./collection.xml";
                searcher.start(path, query, collectionPath);
            }
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }
}