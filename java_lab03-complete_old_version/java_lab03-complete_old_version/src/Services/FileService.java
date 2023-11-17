package Services;

import Data.ProductInfo;
import Exceptions.DirectoryCreationException;
import Services.*;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class FileService {


    private FileService() {
    }

    public static void createDirectory(String path) {
        Path directoryPath = Path.of(path);
        if (!Files.exists(directoryPath)) {
            try {
                Files.createDirectories(directoryPath);
            } catch (IOException e) {
                throw new DirectoryCreationException("Failed to create directory at path: " + path, e);
            }
        }
    }

    public static void saveToTxt(String fileName, String filePath, String text, boolean append) {
        if (fileName == null || text == null) {
            return;
        }
        String fullFilePath = Path.of(filePath, fileName).toString();
        createDirectory(filePath);
        text = append ? "\n" + text : text;
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fullFilePath, append))) {
            writer.write(text);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



    public static String getStringFromTxt(String fileName, String filePath) {
        String fullFilePath = Path.of(filePath, fileName).toString();
        createDirectory(filePath);
        File file = new File(fullFilePath);
        if (!file.exists()) {
            return null;
        }
        try (Stream<String> lines = Files.lines(Path.of(fullFilePath))) {
            return lines.collect(Collectors.joining(System.lineSeparator()));
        } catch (IOException e) {
            return null;
        }
    }
    public static <T extends Serializable> T deserialize(String filePath, String fileName,Class<T> castClass) {
        String fullFilePath = Path.of(filePath, fileName).toString();
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(fullFilePath))) {
            Object obj = ois.readObject();
            if (obj instanceof Serializable) {
                System.out.println("Deserialization completed successfully.");
                return castClass.cast(obj);
            } else {
                System.out.println("Deserialization failed. Object is not Serializable.");
                return null;
            }
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Error during deserialization: " + e.getMessage());
            return null;
        }
    }
    public static <T extends Serializable> void serialize(T object, String filePath, String fileName) {
        String fullFilePath = Path.of(filePath, fileName).toString();
        createDirectory(filePath);
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(fullFilePath))) {
            oos.writeObject(object);
            System.out.println("Serialization completed successfully.");
        } catch (IOException e) {
            System.out.println("Error during serialization: " + e.getMessage());
        }
    }
    public static void serializeMarket(MarketService object, String filePath, String fileName)
    {
        createDirectory(filePath);
        String fullFilePath = Path.of(filePath, fileName).toString();
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(fullFilePath))) {
            oos.writeObject(object);
            System.out.println("Serialization completed successfully.");
        } catch (IOException e) {
            System.out.println("Error during serialization: " + e.getMessage());
        }
    }
    public static MarketService deserializeMarket(String filePath, String fileName) {
        String fullFilePath = Path.of(filePath, fileName).toString();
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(fullFilePath))) {
            Object obj = ois.readObject();
            if (obj instanceof Serializable&& MarketService.class.equals(obj.getClass())) {
                System.out.println("Deserialization completed successfully.");
                return  ((MarketService) obj);

            } else {
                System.out.println("Deserialization failed. Object is not Serializable.");
                return null;
            }
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Error during deserialization: " + e.getMessage());
            return null;
        }
    }
}
