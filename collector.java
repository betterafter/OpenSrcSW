import java.io.*;
import java.util.ArrayList;

import javax.lang.model.element.Element;
import javax.swing.text.html.HTML;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;




public class collector{

    final static String path = "/Users/singiyeol/Desktop/학교자료/4-1/opensource/SimpleIR";
    static ArrayList<File> files = new ArrayList<>();

    public static void HTML_Searcher() throws Exception {

        DocumentBuilderFactory dFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = dFactory.newDocumentBuilder();
        Document doc = dBuilder.newDocument();

        org.w3c.dom.Element docs = doc.createElement("docs");
        doc.appendChild(docs);
        doc.createTextNode("\n\n\n\n");
        doc.createTextNode("!!!!!!!!!!!!");
        
        docs.appendChild(doc.createTextNode("\n"));
        docs.appendChild(doc.createTextNode("\t"));

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
        did.appendChild(doc.createTextNode("\n"));
        did.appendChild(doc.createTextNode("\t\t"));
        docs.appendChild(did);

        did.appendChild(t);
        t.appendChild(doc.createTextNode(title));
        did.appendChild(doc.createTextNode("\n"));
        did.appendChild(doc.createTextNode("\t\t"));
        did.appendChild(b);
        b.appendChild(doc.createTextNode(body));
        did.appendChild(doc.createTextNode("\n"));
        did.appendChild(doc.createTextNode("\t"));
        docs.appendChild(doc.createTextNode("\n"));
        docs.appendChild(doc.createTextNode("\t"));
        // did.appendChild(doc.createTextNode("\n"));
        // did.appendChild(doc.createTextNode("\t"));
    }



    public static void main(String[] args) {
        try{
            HTML_Searcher();
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }
}

