import lombok.extern.slf4j.Slf4j;

import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * The class {@code FileCoding} is used to encode and decode data stored after Huffman coding.<br>
 * Last byte stored in a file contains leading zeros to have 8 bits<br>
 * Eg. 1011 is converted to 00001011 by adding 4 leading zeros<br>
 * In order to decode extra byte is added which informs about number of zeros added to last byte<br>
 * In above example this extra byte will have value of 4
 */
@Slf4j
public class FileCoding {

    /**
     * @param text in binary format eg. 111010100101101001
     */
    public static void encodeToFile(String text, String pathName) {
        int zerosAddedToLastByte = 8 - (text.length() % 8);
        text = convertToModulo8Length(text, zerosAddedToLastByte);
        byte[] bytes = convertToByteArray(text, zerosAddedToLastByte);

        try (FileOutputStream fos = new FileOutputStream(pathName)) {
            fos.write(bytes);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * @return text in binary format eg. 111010100101101001
     */
    public static String decodeFile(String pathName) {
        StringBuilder sb = new StringBuilder();
        try {
            byte[] bytes = Files.readAllBytes(Paths.get(pathName));
            for (int i = 0; i < bytes.length - 2; i++) {
                sb.append(String.format("%8s", Integer.toBinaryString(bytes[i] & 0xFF))
                        .replace(' ', '0'));
            }
            sb.append(processLastTwoBytes(bytes[bytes.length - 2], bytes[bytes.length - 1]));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return sb.toString();
    }

    private static String processLastTwoBytes(byte previousByte, byte lastByte) {
        String s = String.format("%8s", Integer.toBinaryString(previousByte & 0xFF))
                .replace(' ', '0');
        int zerosToRemove = lastByte & 0xFF;
        if (s.startsWith("0".repeat(zerosToRemove))) {
            s = s.substring(zerosToRemove);
        }
        return s;
    }

    private static byte[] convertToByteArray(String text, int zerosAddedToLastByte) {
        byte[] bytes = new byte[(text.length() / 8) + 1];
        for (int i = 0; i < text.length() / 8; i++) {
            bytes[i] = (byte) Integer.parseInt(text.substring(i * 8, i * 8 + 8), 2);
        }
        bytes[bytes.length - 1] = (byte) zerosAddedToLastByte;
        return bytes;
    }

    private static String convertToModulo8Length(String text, int zerosAddedToLastByte) {
        int modulo = text.length() % 8;
        if (modulo == 0) {
            return text;
        }
        String zeros = "0".repeat(zerosAddedToLastByte);
        return new StringBuilder(text).insert(text.length() - modulo, zeros).toString();
    }

}
