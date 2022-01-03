import algorithm.Huffman;
import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import com.google.common.primitives.Bytes;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;

import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
public class HuffmanUtil {

    private final static String COMPRESSED_FILEPATH = "data/data.compressed";
    private final static String DECOMPRESSED_FILEPATH = "data/data.decompressed";

    @SneakyThrows
    public static void huffmanCoding(String FILEPATH) {
        log.info("Starting Huffman coding...");
        byte[] bytes = new byte[0];
        try {
            bytes = Files.readAllBytes(Paths.get(FILEPATH));
        } catch (IOException e) {
            e.printStackTrace();
        }

        Map<Byte, Integer> byteFrequency = new HashMap<>();
        for (byte b : bytes) {
            byteFrequency.put(b, byteFrequency.getOrDefault(b, 0) + 1);
        }

        Map<Byte, String> byteCoding = Huffman.getHuffmanCoding(byteFrequency);
        printByteCoding(byteCoding);
        StringBuilder sb = new StringBuilder();
        for (byte b : bytes) {
            sb.append(byteCoding.get(b));
        }
        FileCoding.encodeToFile(sb.toString(), COMPRESSED_FILEPATH);
        log.info("Compressing using Huffman coding has ended successfully: " + COMPRESSED_FILEPATH);
        long compressedFileSize = Files.size(Paths.get(COMPRESSED_FILEPATH));
        log.info("Size after compressing: " + FileUtils.byteCountToDisplaySize(compressedFileSize));

        String decodedText = FileCoding.decodeFile(COMPRESSED_FILEPATH);
        BiMap<String, Byte> byteCodingReversed = HashBiMap.create(byteCoding).inverse();
        ArrayList<Byte> byteList = new ArrayList<>();
        sb = new StringBuilder();
        for (int i = 0; i < decodedText.length(); i++) {
            sb.append(decodedText.charAt(i));
            if (byteCodingReversed.containsKey(sb.toString())) {
                byteList.add(byteCodingReversed.get(sb.toString()));
                sb.setLength(0);
            }
        }

        try (FileOutputStream fos = new FileOutputStream(DECOMPRESSED_FILEPATH)) {
            fos.write(Bytes.toArray(byteList));
            log.info("Decompressing using Huffman coding has ended successfully: " + DECOMPRESSED_FILEPATH);
            long decompressedFileSize = Files.size(Paths.get(DECOMPRESSED_FILEPATH));
            double compressionRate = (double) 100 * (decompressedFileSize - compressedFileSize) / decompressedFileSize;
            log.info("Size after decompressing: " + FileUtils.byteCountToDisplaySize(decompressedFileSize));
            log.info("Compressing rate: " + String.format("%.2f", compressionRate) + "%");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void printByteCoding(Map<Byte, String> coding) {
        BiMap<String, Byte> codingReversed = HashBiMap.create(coding).inverse();
        List<String> codes = coding.values().stream()
                .sorted(Comparator.comparingInt(String::length).thenComparing(String::compareTo))
                .collect(Collectors.toList());
        System.out.println("Huffman coding: ");
        codes.forEach(code -> {
            byte value = codingReversed.get(code);
            System.out.println(String.format("%3s", Byte.toUnsignedInt(value))
                    + "(" + (char) value + ")"
                    + ": " + code);
        });
    }

}
