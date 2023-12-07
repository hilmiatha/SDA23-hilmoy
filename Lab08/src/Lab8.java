import java.io.*;
import java.util.*;
import java.util.StringTokenizer;

public class Lab8 {
    private static InputReader in;
    private static PrintWriter out;

    public static void main(String[] args) {
        InputStream inputStream = System.in;
        in = new InputReader(inputStream);
        OutputStream outputStream = System.out;
        out = new PrintWriter(outputStream);

        int N = in.nextInt();
        int E = in.nextInt();
        Graph graph = new Graph(N);

        // TODO: Bangun graf
        for (int i = 0; i < E; i++) {
            int A = in.nextInt();
            int B = in.nextInt();
            long W = in.nextLong();
            // TODO: Inisiasi jalur antara A dan B
            graph.addEdge(B,A,W);

        }

        int H = in.nextInt();
        ArrayList<Integer> treasureNodes = new ArrayList<Integer>();
        treasureNodes.add(1);
        for (int i = 0; i < H; i++) {
            int K = in.nextInt();
            // TODO: Simpan titik yang memiliki harta karun
            treasureNodes.add(K);

        }
        // store the result of dijkstra algorithm
        HashMap<Integer,ArrayList<Long>> idToShortestPath = new HashMap<>();
        ArrayList<Long> minDist = new ArrayList<>();
        for (int benteng : treasureNodes) {
            ArrayList<Long> dist = graph.dijkstra(benteng);
//            out.println("Benteng " + benteng + " : " + dist);
            idToShortestPath.put(benteng, dist);

//            // update the minimum distance from dist to minDist
//            if (minDist.isEmpty()) {
//                minDist = dist;
//            } else {
//                for (int i = 1; i < dist.size(); i++) {
//                    if (dist.get(i) < minDist.get(i)) {
//                        minDist.set(i, dist.get(i));
//                    }
//                }
//            }
        }

        int Q = in.nextInt(); //berapa query
        int O = in.nextInt(); //oksigen
        while (Q-- > 0) {
            Long totalOxygenNeeded = (long) 0;

            int T = in.nextInt();
            int davePosition = 1;
            while (T-- > 0) {
                int D = in.nextInt();
                // TODO: Update total oksigen dibutuhkan
                ArrayList<Long> arr = idToShortestPath.get(davePosition);
                totalOxygenNeeded += arr.get(D);


                // TODO: Update posisi Dave
                davePosition = D;
            }

            // TODO: Implementasi Dave kembali ke daratan
            ArrayList<Long> arr = idToShortestPath.get(davePosition);
            totalOxygenNeeded += arr.get(1);
            davePosition = 1;
            // TODO: Cetak 0 (rute tidak aman) atau 1 (rute aman)
            if ((O <= totalOxygenNeeded)) {
                out.println(0);
            } else {
                out.println(1);
            }

        }

        out.close();
    }

    // taken from https://codeforces.com/submissions/Petr
    // together with PrintWriter, these input-output (IO) is much faster than the
    // usual Scanner(System.in) and System.out
    // please use these classes to avoid your fast algorithm gets Time Limit
    // Exceeded caused by slow input-output (IO)
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

        public long nextLong() {
            return Long.parseLong(next());
        }
    }
}
class Pair implements Comparable<Pair> {
    // first = source; second = jumlah bala bantuan
    int src;
    long troops;

    public Pair(int src, long troops) {
        this.src = src;
        this.troops = troops;
    }

    @Override
    public int compareTo(Pair o) {
        return (int) (this.troops - o.troops);
    }
}

class Edge {
    // from = source; to = destination; weight = jumlah musuh
    int to;
    long weight;

    public Edge(int to, long weight) {
        this.to = to;
        this.weight = weight;
    }
}

// TODO: Implementasi Graph
class Graph {

    public int V;
    public ArrayList<ArrayDeque<Edge>> adj;
    public Graph(int v) {
        this.V = v;
        this.adj = new ArrayList<>();
        for (int i = 0; i <= v; i++)
            this.adj.add(new ArrayDeque<>());

    }




    // TODO: Implementasi tambahkan edge ke graph
    public void addEdge(int from, int to, long weight) {
        this.adj.get(from).add(new Edge(to, weight));
        this.adj.get(to).add(new Edge(from,weight));
    }

    // TODO: Implementasi shortest path (bebas mengubah fungsi ini)
    public ArrayList<Long> dijkstra(int source) {
        if (source == 0)
            return null;
        ArrayList<Long> dist = new ArrayList<>();
        for (int i = 0; i <= this.V; i++)
            dist.add(Long.MAX_VALUE);
        dist.set(source, (long) 0);

        PriorityQueue<Pair> pq = new PriorityQueue<>();
        pq.add(new Pair(source, 0));

        while (!pq.isEmpty()) {
            Pair curr = pq.poll();
            int v = curr.src; // source
            long w = curr.troops; // jumlah bala bantuan

            if (w > dist.get(v))
                continue;

            for (Edge e : this.adj.get(v)) {
                int u = e.to;
                long weight = e.weight;
                if (dist.get(v) + weight < dist.get(u)) {
                    dist.set(u, dist.get(v) + weight);
                    pq.add(new Pair(u, dist.get(u)));
                }
            }
        }

        return dist;
    }
}