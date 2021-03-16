import java.io.*;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.snu.ids.kkma.index.Keyword;
import org.snu.ids.kkma.index.KeywordExtractor;
import org.snu.ids.kkma.index.KeywordList;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;




public class collector2 {
    
     // ----------------------------------------------------- 실습2 --------------------------------------------------------------
     public static void kkma_analyzer() throws Exception {
        String path = "/Users/singiyeol/Desktop/학교자료/4-1/opensource/SimpleIR/";
        File file = new File(path + "collection.xml");
        if(file.isFile()){
            collection_to_index(file);
        }
    }

    public static void collection_to_index(File file) throws Exception{

        String content = "";
        FileReader fr = new FileReader(file);
        BufferedReader bf = new BufferedReader(fr);

        String readLine = "";
        while((readLine = bf.readLine()) != null){
            content += readLine.trim();
        }

        bf.close();

        String fbody = "<body>", ebody = "</body>";
        int idx = 0;
        int fidx = content.indexOf(fbody, idx), eidx = content.indexOf(ebody, fidx);  
        while(fidx != -1){

            String text = content.substring(fidx + fbody.length(), eidx);
            int isidx = fidx + fbody.length();

            KeywordExtractor ke = new KeywordExtractor();
            KeywordList kl = ke.extractKeyword(text, true);

            String newText = "";
            for(int i = 0; i < kl.size(); i++){
                Keyword kword = kl.get(i);
                newText += kword.getString() + ":" + kword.getCnt() + "#"; 
            }

            StringBuffer sb = new StringBuffer(content);
            sb.insert(isidx, newText);
            content = sb.toString();
            content = content.replace(text, "");

            idx++;
            fidx = 0; eidx = 0;
            for(int i = 0; i < idx; i++){
                fidx = content.indexOf(fbody, eidx); eidx = content.indexOf(ebody, fidx);
            }
        }
        
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document doc = builder.parse(new InputSource(new StringReader(content)));
        doc.setXmlStandalone(true);

        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer Transformer = transformerFactory.newTransformer();
        Transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
        Transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4"); //정렬 스페이스4칸
        Transformer.setOutputProperty(OutputKeys.INDENT, "yes"); //들여쓰기
        Transformer.setOutputProperty(OutputKeys.DOCTYPE_PUBLIC, "yes"); //doc.setXmlStandalone(true); 했을때 붙어서 출력되는부분 개행

        DOMSource DOMsource = new DOMSource(doc);
        StreamResult result =  new StreamResult(new File("index.xml"));
        Transformer.transform(DOMsource, result);
    }
    // -------------------------------------------------------------------------------------------------------------------------
    public void start() throws Exception{
        kkma_analyzer();
    }
}
