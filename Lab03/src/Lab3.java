import java.io.*;
import java.util.ArrayDeque;
import java.util.StringTokenizer;

public class Lab3{
    private static InputReader in;
    private static PrintWriter out;
    private static String arah = "KANAN";
    private static ArrayDeque<ArrayDeque<Integer>> gedung;
    private static long point = 0L;

    // Metode GA
    static String GA() {
        int length = gedung.size() - 1;
        if (arah.equals("KANAN")) {
            arah = "KIRI";
        } else {
            arah = "KANAN";
        }

        if(!gedung.isEmpty()) {
            ArrayDeque<Integer> gedung2 = gedung.pop();
            ArrayDeque<ArrayDeque<Integer>> temp = new ArrayDeque<>();

            for (int i = 0; i < length; i++) {
                ArrayDeque<Integer> elm1 = gedung.removeLast();
                temp.addLast(elm1);
            }
            temp.addFirst(gedung2);
            gedung = temp;
        }


        return arah;
    }

    // Metode S
    static long S(int Si){
        long takenPoints = 0;
        if (point > 0) {
            if (!gedung.isEmpty()) {
                ArrayDeque<Integer> elm1 = gedung.pop();
                for (int i = 0; i < Si; i++) {
                    if (!elm1.isEmpty()) {
                        int firstP = elm1.pop();
                        takenPoints = takenPoints + firstP;
                    }
                }
                point = point - takenPoints;
                if(!elm1.isEmpty()){
                    gedung.addLast(elm1);
                }
                if(point <= 0 || gedung.isEmpty()){
                    return -1;
                }
                return takenPoints;
            }else{
                return -1;
            }
        }else{
            return -1;
        }
    }

    // Template
    public static void main(String[] args) {
        InputStream inputStream = System.in;
        in = new InputReader(inputStream);
        OutputStream outputStream = System.out;
        out = new PrintWriter(outputStream);

        // Read input
        long T = in.nextLong();
        int X = in.nextInt();
        int C = in.nextInt();
        int Q = in.nextInt();

        point = T;
        gedung = new ArrayDeque<>();
        for (int i = 0; i < X; i++) {
            ArrayDeque<Integer> gedungtemp = new ArrayDeque<>();
            // Insert into ADT
            for (int j = 0; j < C; j++) {
                int Ci = in.nextInt();
                gedungtemp.add(Ci);
            }
            gedung.add(gedungtemp);
        }
        // Process the query
        for (int i = 0; i < Q; i++) {
            String perintah = in.next();
            if (perintah.equals("GA")) {
                out.println(GA());
            } else if (perintah.equals("S")) {
                int Si = in.nextInt();
                long result = S(Si);
                if(result == -1){
                    out.println("MENANG");
                }else{
                    out.println(result);
                }
            }
        }

        // don't forget to close the output
        out.close();
    }
    // taken from https://codeforces.com/submissions/Petr
    // together with PrintWriter, these input-output (IO) is much faster than the usual Scanner(System.in) and System.out
    // please use these classes to avoid your fast algorithm gets Time Limit Exceeded caused by slow input-output (IO)
    static class InputReader {
        public BufferedReader reader;
        public StringTokenizer tokenizer;

        public InputReader(InputStream stream) {
            reader = new BufferedReader(new InputStreamReader(stream), 32768);
            tokenizer = null;
        }

        public String next() {
            while (tokenizer == null || !tokenizer.hasMoreTokens()) {
                try {
                    tokenizer = new StringTokenizer(reader.readLine());
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
            return tokenizer.nextToken();
        }

        public int nextInt() {
            return Integer.parseInt(next());
        }

        public long nextLong(){
            return Long.parseLong(next());
        }

    }
}