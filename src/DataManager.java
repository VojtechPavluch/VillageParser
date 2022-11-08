import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;

// Responsible for parsing the xml file and filling up the database
public class DataManager {
    private File file;
    private ArrayList<VillagePart> villageParts;
    private ArrayList<Village> villages;
    private final String URL = "jdbc:sqlite:kopidlno.sqlite";
    private Connection connection = null;

    public DataManager() {
        File[] file = new File(".\\xml").listFiles();
        if (file != null) {
            this.file = file[0];
            villageParts = new ArrayList<VillagePart>();
            villages = new ArrayList<Village>();
        }
    }

    public void parseAndFillUp () {
        this.parseFile();
        this.fillData();
    }

    // Adds the village parts and the village into proper arrays
    private void parseFile() {
        try {
            File xmlFile = new File(file.getPath());
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.parse(xmlFile);

            NodeList villagePartsList = document.getElementsByTagName("vf:CastObce");
            for (int i = 0; i < villagePartsList.getLength(); i++) {
                Node node = villagePartsList.item(i);

                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    Element element = (Element) node;
                    String villagePartCode = element.getElementsByTagName("coi:Kod").item(0).getTextContent();
                    String name = element.getElementsByTagName("coi:Nazev").item(0).getTextContent();
                    Node villageNode = ((Element) node).getElementsByTagName("coi:Obec").item(0);
                    Element villageElement = (Element) villageNode;
                    String villageCode = villageElement.getElementsByTagName("obi:Kod").item(0).getTextContent();
                    VillagePart tempVillagePart = new VillagePart(villagePartCode, name, villageCode);
                    villageParts.add(tempVillagePart);
                }
            }

            NodeList villageList = document.getElementsByTagName("vf:Obec");
            for (int i = 0; i < villageList.getLength(); i++) {
                Node node = villageList.item(i);

                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    Element element = (Element) node;
                    String code = element.getElementsByTagName("obi:Kod").item(0).getTextContent();
                    String name = element.getElementsByTagName("obi:Nazev").item(0).getTextContent();
                    Village tempVillage = new Village(code, name);
                    villages.add(tempVillage);
                }
            }
        } catch (ParserConfigurationException | IOException | SAXException e) {
            e.printStackTrace();
        }
    }

    // Fills in table "obec" and "cast_obce"
    private void fillData() {
        try {
            connection = DriverManager.getConnection(URL);
            if (connection != null) {

                for (Village village: villages) {
                    Statement statement = connection.createStatement();
                    String sql = "INSERT INTO obec (kod_obce, nazev) " + "VALUES(?,?)";
                    PreparedStatement prepStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
                    prepStatement.setString(1, village.getVillageCode());
                    prepStatement.setString(2, village.getName());
                    prepStatement.executeUpdate();
                }

                for (VillagePart villagePart: villageParts) {
                    Statement statement = connection.createStatement();
                    String sql = "INSERT INTO cast_obce (kod_casti, nazev, kod_obce) " + "VALUES(?,?,?)";
                    PreparedStatement prepStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
                    prepStatement.setString(1, villagePart.getVillagePartCode());
                    prepStatement.setString(2, villagePart.getName());
                    prepStatement.setString(3, villagePart.getVillageCode());
                    prepStatement.executeUpdate();
                }
                connection.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
