import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

// Responsible for downloading zipped file and extracting xml file
public class FileManager {

    private static final int SIZE = 1024;

    // Downloads the zip file located at the link and saves it to the filePath (downloads folder)
    public static void downloadFile(String link, String filePath) {
        try {
            File file = new File(filePath);
            file.getParentFile().mkdirs();
            file.createNewFile();
            FileOutputStream outputStream = new FileOutputStream(file);
            URL url = new URL(link);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            BufferedInputStream inputStream = new BufferedInputStream(connection.getInputStream());
            BufferedOutputStream bufferedOutStream = new BufferedOutputStream(outputStream, SIZE);

            byte[] buffer = new byte[SIZE];
            int readBytes;
            while ((readBytes = inputStream.read(buffer, 0, SIZE)) >= 0)
                bufferedOutStream.write(buffer, 0, readBytes);

            inputStream.close();
            bufferedOutStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Unzips the file in "downloads" folder and saves it into the "xml" folder
    public static void unzipFile(String fileName) {
        try {
            File folder = new File(".\\xml");
            if (!folder.exists())
                folder.mkdir();
            ZipInputStream zipInputStream = new ZipInputStream(new FileInputStream(fileName));
            ZipEntry zipEntry = zipInputStream.getNextEntry();

            byte[] buffer = new byte[SIZE];
            while (zipEntry != null) {
                FileOutputStream fileOutputStream = new FileOutputStream(".\\xml" + File.separator + zipEntry.getName());
                int readBytes;
                while ((readBytes = zipInputStream.read(buffer)) > 0)
                    fileOutputStream.write(buffer, 0, readBytes);
                fileOutputStream.close();
                zipInputStream.closeEntry();
                zipEntry = zipInputStream.getNextEntry();
            }
            zipInputStream.closeEntry();
            zipInputStream.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
