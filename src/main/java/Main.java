public class Main {

    public static void main(String[] args) {
        FileCoding.encodeToFile("00000000000000001", "data/data");
        String decodedText = FileCoding.decodeFile("data/data");
        System.out.println("Decoded text: " + decodedText);
    }

}
