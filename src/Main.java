import java.util.Random;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class Main {
    public static final int AMOUNT_TEXTS = 10_000;
    public static final int TEXT_LENGTH = 100_000;
    public static BlockingQueue<String> queueFirst = new ArrayBlockingQueue<>(100);
    public static BlockingQueue<String> queueSecond = new ArrayBlockingQueue<>(100);
    public static BlockingQueue<String> queueThird = new ArrayBlockingQueue<>(100);

    public static void main(String[] args) {
        new Thread(() -> {
            for (int i = 0; i < AMOUNT_TEXTS; i++) {
                String symbols = generateText("abc", TEXT_LENGTH);
                try {
                    queueFirst.put(symbols);
                    queueSecond.put(symbols);
                    queueThird.put(symbols);
                } catch (InterruptedException e) {
                    return;
                }
            }
        }).start();
        new Thread(() -> letterCount('a', queueFirst)).start();
        new Thread(() -> letterCount('b', queueSecond)).start();
        new Thread(() -> letterCount('c', queueThird)).start();
    }

    public static String generateText(String letters, int length) {
        Random random = new Random();
        StringBuilder text = new StringBuilder();
        for (int i = 0; i < length; i++) {
            text.append(letters.charAt(random.nextInt(letters.length())));
        }
        return text.toString();
    }

    public static void letterCount(char a, BlockingQueue<String> blockingQueue) {
        int count = 0;
        for (int i = 0; i < AMOUNT_TEXTS; i++) {
            try {
                String letter = blockingQueue.take();
                for (int j = 0; j < letter.length(); j++) {
                    if (letter.charAt(j) == a) count++;
                }
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        System.out.println("Количество букв '" + a + "' " + count + " шт.");
    }
}