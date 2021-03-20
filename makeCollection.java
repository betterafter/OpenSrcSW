import java.io.*;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;




public class makeCollection {

    static ArrayList<File> files = new ArrayList<>();

    // ---------------------------------------------- 실습 1 ----------------------------------------------------------------
    public static void HTML_Searcher(final String path) throws Exception {

        DocumentBuilderFactory dFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = dFactory.newDocumentBuilder();
        Document doc = dBuilder.newDocument();
        doc.setXmlStandalone(true);

        org.w3c.dom.Element docs = doc.createElement("docs");
        doc.appendChild(docs);
        int index = 0;
        for (File info : new File(path).listFiles()) {
            if(info.isFile()){

                if(info.getName().contains(".html")){
                    //files.add(info);
                    HTML_Parser(doc, docs, index, info);
                    index++;
                }
            }
        }

        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer Transformer = transformerFactory.newTransformer();
        Transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
        Transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4"); //정렬 스페이스4칸
        Transformer.setOutputProperty(OutputKeys.INDENT, "yes"); //들여쓰기
        Transformer.setOutputProperty(OutputKeys.DOCTYPE_PUBLIC, "yes"); //doc.setXmlStandalone(true); 했을때 붙어서 출력되는부분 개행

        DOMSource dom = new DOMSource(doc);
        StreamResult res = new StreamResult(new File("collection.xml"));
        Transformer.transform(dom, res);
    }

    public static void HTML_Parser(Document doc, org.w3c.dom.Element docs, int index, File file) throws Exception{

        

        String content = "";
        FileReader fr = new FileReader(file);
        BufferedReader bf = new BufferedReader(fr);

        String readLine = "";
        while((readLine = bf.readLine()) != null){
            content += readLine.trim();
        }

        bf.close();
    
        String title, body;
        String title_startTag = "<title>", title_endTag = "</title>";
        String body_startTag = "<body>", body_endTag = "</body>";

        int sidx = content.indexOf(title_startTag), eidx = content.indexOf(title_endTag);
        title = content.substring(sidx + title_startTag.length(), eidx);

        sidx = content.indexOf(body_startTag); eidx = content.indexOf(body_endTag);
        body = content.substring(sidx + body_startTag.length(), eidx);

        String getBody = "";
        for(int i = 0; i < body.length(); i++){
            if(body.charAt(i) == '<'){
                while(body.charAt(i) != '>'){
                    i++;
                }
            }
            else getBody += body.charAt(i);
        }

        document_collector(doc, docs, index, title, getBody);
    }


    public static void document_collector(Document doc, org.w3c.dom.Element docs, 
    int index, String title, String body) throws Exception {

        org.w3c.dom.Element did = doc.createElement("doc");
        org.w3c.dom.Element t = doc.createElement("title");
        org.w3c.dom.Element b = doc.createElement("body");

        did.setAttribute("id", Integer.toString(index));
        docs.appendChild(did);
        did.appendChild(t);
        t.appendChild(doc.createTextNode(title));
        did.appendChild(b);
        b.appendChild(doc.createTextNode(body));
    }
    // -------------------------------------------------------------------------------------------------------------------------

    public void start(final String path) throws Exception{
        HTML_Searcher(path);
    }
}

