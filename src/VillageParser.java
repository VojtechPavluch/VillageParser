/**
 * The VillageParser downloads zipped file from provided link, extracts xml file
 * and fills up tables "obec" and "cast_obce" with data of proper elements
 * @author Vojtech Pavluch
 * @since 2022-11-08
 * jdk 17.05
 */
public class VillageParser {

    public static void main(String[] args) {

        FileManager.downloadFile("https://www.smartform.cz/download/kopidlno.xml.zip", ".\\downloads\\file.zip" );
        FileManager.unzipFile(".\\downloads\\file.zip");

        DataManager manager = new DataManager();
        manager.parseAndFillUp();
    }
}