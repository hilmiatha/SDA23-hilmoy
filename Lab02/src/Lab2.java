import java.io.*;
import java.util.StringTokenizer;

public class Lab2 {
    private static InputReader in;
    private static PrintWriter out;

    static long maxOddEvenSubSum(int[] a) {
        // TODO: Implement this method
        long maxSum = Long.MIN_VALUE;
        long thisSum = 0;
        int placehold = -2147483648;
        boolean nothing = false;
        boolean even = Math.abs(a.length % 2) == 0;
        if (even) {
            for (int i : a) {
                if(i%2==0) {
                    nothing = true;
                    thisSum += i;
                    if(i > placehold){
                        placehold = i;
                    }
                }else{
                    thisSum = 0;
                }
                if (thisSum > maxSum) {
                    maxSum = thisSum;
                } else if (thisSum < 0) {
                    thisSum = 0;
                }
            }
            if (maxSum == 0 && nothing){
                maxSum = placehold;
            }
        }else {
            for (int i : a) {
                if (i%2==1 || i%2 == -1) {
                    nothing = true;
                    thisSum += i;
                    if(i > placehold){
                        placehold = i;
                    }
                } else {
                    thisSum = 0;
                }
                if (thisSum > maxSum) {
                    maxSum = thisSum;
                }else if (thisSum < 0) {
                    thisSum = 0;
                }
            }
            if (maxSum == 0 && nothing){
                maxSum = placehold;
            }
        }
        return maxSum;

    }



    public static void main(String[] args) throws IOException {
        InputStream inputStream = System.in;
        in = new InputReader(inputStream);
        OutputStream outputStream = System.out;
        out = new PrintWriter(outputStream);

        // Read value of N
        int N = in.nextInt();

        // Read value of x
        int[] x = new int[N];
        for (int i = 0; i < N; ++i) {
            x[i] = in.nextInt();
        }

        long ans = maxOddEvenSubSum(x);
        out.println(ans);

        // don't forget to close/flush the output
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

    }
}