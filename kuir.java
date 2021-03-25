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

            for(int i = 0; i < args.length; i++){
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
            }
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }
}
