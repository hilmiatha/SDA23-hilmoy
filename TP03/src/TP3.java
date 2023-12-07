/*
SOURCE AND REFERENCE

Github kak Eugenius Mario : https://github.com/eugeniusms/SDA-2022

Github kak Edu : https://github.com/edutjie/sda222

 */

import java.io.*;
import java.util.*;


public class TP3 {
    private static InputReader in;
    private static PrintWriter out;

    public static long[][] memoMaxEdge;
    public static ArrayList<Integer> treasureVertex;
    public static Graph graph;


    public static void main(String[] args) {
        InputStream inputStream = System.in;
        in = new InputReader(inputStream);
        OutputStream outputStream = System.out;
        out = new PrintWriter(outputStream);

        int vertex = in.nextInt(); int edge = in.nextInt();
        //TODO buat graph
        graph = new Graph(vertex);
        memoMaxEdge = new long[vertex+1][vertex+1];

        treasureVertex = new ArrayList<>();
        for (int i = 1; i <= vertex ; i++){
            String s = in.next();
            if (Objects.equals(s, "S")) {
                treasureVertex.add(i);
            }
        }

        for (int i = 0; i < edge; i++){
            int source = in.nextInt(); int destination = in.nextInt(); long size = in.nextLong();
            graph.addEdge(source,destination,size);
            graph.addEdge(destination,source,size);
        }

        graph.KruskalMST();

        for (int i : treasureVertex){
            long[] temp = graph.dijkstra(i);
//            out.println(Arrays.toString(temp));
            memoMaxEdge[i] = temp;
        }

        int Q = in.nextInt();
        for (int i = 0 ; i < Q ; i++){
            String query = in.next();
            if (query.equals("M")){
                long group_size = in.nextLong();
                M(group_size);
            }
            else if (query.equals("S")) {
                int startId = in.nextInt();
                S(startId);
            }
            else{
                int startId = in.nextInt();
                int middleId= in.nextInt();
                int endId = in.nextInt();
                long groupSize= in.nextLong();
                T(startId,middleId,endId,groupSize);
            }

        }


        out.close();

    }
    static void T(int startId, int middleId, int endId, long groupSize) {
        long[] midDijk = graph.dijkstra(middleId);
//        out.println(Arrays.toString(memoMaxEdge[endId]));
        if (midDijk[startId] > groupSize){
            out.println("N");
            return;
        }

        if (memoMaxEdge[endId][middleId] > groupSize){
            out.println("H");
            return;
        }

        out.println("Y");

    }

    static void S(int startId) {
        long output = Long.MAX_VALUE;
        for (int i : treasureVertex){
            long minimumOrang = memoMaxEdge[i][startId];
            if (minimumOrang < output && minimumOrang != Long.MIN_VALUE){
                output = minimumOrang;
            }
        }
        out.println(output);
    }

    static void M(long groupSize) {
        int output = 0;
        for (int i : treasureVertex){
            if (groupSize > memoMaxEdge[i][1] && memoMaxEdge[i][1] != Long.MIN_VALUE){
                output +=1;
            }
        }
        out.println(output);
    }



    // taken from https://codeforces.com/submissions/Petr
    // together with PrintWriter, these input-output (IO) is much faster than the
    // usual Scanner(System.in) and System.out
    // please use these classes to avoid your fast algorithm gets Time Limit
    // Exceeded caused by slow input-output (IO)
    private static class InputReader {

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

        public char nextChar() {
            return next().charAt(0);
        }

        public int nextInt() {
            return Integer.parseInt(next());
        }

        public long nextLong() {
            return Long.parseLong(next());
        }
    }

    static class Pair implements Comparable<Pair> {
        // first = source; second = jumlah bala bantuan
        int src;
        long troops;

        public Pair(int src, long troops) {
            this.src = src;
            this.troops = troops;
        }

        @Override
        public int compareTo(Pair o) {
            return Long.compare(this.troops, o.troops);
        }
    }

    static class Edge implements Comparable<Edge> {
        int src, dest;
        long weight;

        public Edge(int src, int dest, long weight) {
            this.src = src;
            this.dest = dest;
            this.weight = weight;
        }

        @Override
        public int compareTo(Edge compareEdge) {
            return Long.compare(this.weight, compareEdge.weight);
        }
    }

    static class Subset {
        int parent, rank;
    }

    static class Graph {
        private List<Edge> edges;
        private List<List<Edge>> mstAdjList;
        private int V; // Number of vertices

        public Graph(int v) {
            this.V = v;
            this.edges = new ArrayList<>();
            this.mstAdjList = new ArrayList<>();
            for (int i = 0; i <= V; ++i) {
                mstAdjList.add(new ArrayList<>());
            }
        }

        public void addEdge(int src, int dest, long weight) {
            Edge edge = new Edge(src, dest, weight);
            edges.add(edge);
        }

        private int find(Subset[] subsets, int i) {
            if (subsets[i].parent != i) {
                subsets[i].parent = find(subsets, subsets[i].parent);
            }
            return subsets[i].parent;
        }

        private void union(Subset[] subsets, int x, int y) {
            int xroot = find(subsets, x);
            int yroot = find(subsets, y);

            if (subsets[xroot].rank < subsets[yroot].rank) {
                subsets[xroot].parent = yroot;
            } else if (subsets[xroot].rank > subsets[yroot].rank) {
                subsets[yroot].parent = xroot;
            } else {
                subsets[yroot].parent = xroot;
                subsets[xroot].rank++;
            }
        }

        public void KruskalMST() {
            Edge[] edgesArray = edges.toArray(new Edge[0]);
            sort(edgesArray, 0, edgesArray.length - 1);

            Subset[] subsets = new Subset[V + 1]; // Adjust for 1-based indexing
            for (int i = 0; i <= V; ++i) {
                subsets[i] = new Subset();
                subsets[i].parent = i;
                subsets[i].rank = 0;
            }

            int e = 0;
            int i = 0;
            while (e < V - 1 && i < edgesArray.length) {
                Edge next_edge = edgesArray[i++];

                int x = find(subsets, next_edge.src);
                int y = find(subsets, next_edge.dest);

                if (x != y) {
//                    System.out.println(next_edge.src + " -- " + next_edge.dest + " == " + next_edge.weight);
                    union(subsets, x, y);
                    e++;

                    // Add edge to MST adjacency list
                    mstAdjList.get(next_edge.src).add(new Edge(next_edge.src, next_edge.dest, next_edge.weight));
                    mstAdjList.get(next_edge.dest).add(new Edge(next_edge.dest, next_edge.src, next_edge.weight));
                }
            }
        }

        //reference : github kak edu (lab 8 & tp 3)
        public long[] dijkstra(int src) {
            if (memoMaxEdge[src] == null){return memoMaxEdge[src];}

            long[] dist = new long[V + 1];
            Arrays.fill(dist, Long.MIN_VALUE);
            dist[src] = 0;
            boolean[] visited = new boolean[V+1];

            Heap pq = new Heap(V, true); 
            pq.add(new Pair(src, 0L));

            while (!pq.isEmpty()) {
                Pair curr = pq.poll();
                int u = curr.src;
                if (visited[u]){continue;}
                visited[u] = true;

                for (Edge next : mstAdjList.get(u)) {
                    int v = next.dest;
                    long weight = next.weight;

                    long edgeMax = Math.max(dist[u], weight);
                    if (edgeMax < dist[v] || dist[v] == Long.MIN_VALUE) {
                        dist[v] = edgeMax;
                        pq.add(new Pair(v, edgeMax));
                    }
                }
            }

//            // You might want to print the max edge weights to verify the result
//            for (int i = 1; i < dist.length; i++) {
//                if (dist[i] != Long.MAX_VALUE) {
//                    System.out.println("Maximum edge weight from 1 to " + i + " is " + dist[i]);
//                } else {
//                    System.out.println("No path from 1 to " + i);
//                }
//            }

            return dist;
        }


        // REFERENCE : https://www.geeksforgeeks.org/merge-sort/ && GITHUB KAK EUGENIUS
        static void merge(Edge arr[], int l, int m, int r) {
            // Find sizes of two subarrays to be merged
            int n1 = m - l + 1;
            int n2 = r - m;

            /* Create temp arrays */
            Edge L[] = new Edge[n1];
            Edge R[] = new Edge[n2];

            /*Copy data to temp arrays*/
            for (int i = 0; i < n1; ++i)
                L[i] = arr[l + i];
            for (int j = 0; j < n2; ++j)
                R[j] = arr[m + 1 + j];

            /* Merge the temp arrays */

            // Initial indexes of first and second subarrays
            int i = 0, j = 0;

            // Initial index of merged subarray array
            int k = l;
            while (i < n1 && j < n2) {
                if (L[i].compareTo(R[j]) <= 0) {
                    arr[k] = L[i];
                    i++;
                } else {
                    arr[k] = R[j];
                    j++;
                }
                k++;
            }

            /* Copy remaining elements of L[] if any */
            while (i < n1) {
                arr[k] = L[i];
                i++;
                k++;
            }

            /* Copy remaining elements of R[] if any */
            while (j < n2) {
                arr[k] = R[j];
                j++;
                k++;
            }
        }


        // Main function that sorts arr[l..r] using
        // merge()
        static void sort(Edge arr[], int l, int r) {
            if (l < r) {
                // Find the middle point
                int m = l + (r - l) / 2;

                // Sort first and second halves
                sort(arr, l, m);
                sort(arr, m + 1, r);

                // Merge the sorted halves
                merge(arr, l, m, r);
            }
        }
    }

    static class Heap {
        // referensi Min Heap:
        // https://medium.com/@ankur.singh4012/implementing-min-heap-in-java-413d1c20f90d

        // referensi Max Heap:
        // https://medium.com/@ankur.singh4012/implementing-max-heap-in-java-ea368dadd273
        // GITHUB KAK EDU

        public int capacity;
        public int size;
        public Pair[] data;
        public boolean isMinHeap = false;

        public Heap(int capacity) {
            this.capacity = capacity;
            this.size = 0;
            this.data = new Pair[capacity];
        }

        public Heap(int capacity, boolean isMinHeap) {
            this(capacity);
            this.isMinHeap = isMinHeap;
        }

        public boolean isEmpty() {
            return this.size == 0;
        }

        public int getLeftChildIndex(int parentIndex) {
            return 2 * parentIndex + 1;
        }

        public int getRightChildIndex(int parentIndex) {
            return 2 * parentIndex + 2;
        }

        public int getParentIndex(int childIndex) {
            return (childIndex - 1) / 2;
        }

        public boolean hasLeftChild(int index) {
            return getLeftChildIndex(index) < size;
        }

        public boolean hasRightChild(int index) {
            return getRightChildIndex(index) < size;
        }

        public boolean hasParent(int index) {
            return getParentIndex(index) >= 0;
        }

        public Pair leftChild(int index) {
            return data[getLeftChildIndex(index)];
        }

        public Pair rightChild(int index) {
            return data[getRightChildIndex(index)];
        }

        public Pair parent(int index) {
            return data[getParentIndex(index)];
        }

        public void swap(int a, int b) {
            Pair temp = data[a];
            data[a] = data[b];
            data[b] = temp;
        }

        public void ensureCapacity() {
            if (size == capacity) {
                if (capacity == 0) {
                    // handle rte
                    data = Arrays.copyOf(data, 1);
                    capacity = 1;
                } else {
                    data = Arrays.copyOf(data, capacity * 2);
                    capacity *= 2;
                }
            }
        }

        // Time Complexity : O(1)
        public Pair peek() {
            if (size == 0) {
                return null;
            }
            return data[0];
        }

        // Time Complexity : O(log n)
        public Pair poll() {
            if (size == 0) {
                return null;
            }
            Pair item = data[0];
            data[0] = data[size - 1];
            size--;

            heapifyDown(0);
            return item;
        }

        // Time Complexity : O(log n)
        public void add(Pair item) {
            ensureCapacity();
            data[size] = item;
            size++;

            heapifyUp(size - 1);
        }

        public void heapifyUp(int index) {
            if (isMinHeap) {
                while (hasParent(index) && parent(index).compareTo(data[index]) > 0) {
                    swap(getParentIndex(index), index);
                    index = getParentIndex(index);
                }
            } else {
                while (hasParent(index) && parent(index).compareTo(data[index]) < 0) {
                    swap(getParentIndex(index), index);
                    index = getParentIndex(index);
                }
            }
        }

        public void heapifyDown(int index) {
            while (hasLeftChild(index)) {
                int smallestChildIndex = getLeftChildIndex(index);

                if (isMinHeap) {
                    if (hasRightChild(index) && rightChild(index).compareTo(leftChild(index)) < 0) {
                        smallestChildIndex = getRightChildIndex(index);
                    }
                    if (data[index].compareTo(data[smallestChildIndex]) < 0) {
                        break;
                    } else {
                        swap(index, smallestChildIndex);
                    }
                } else {
                    if (hasRightChild(index) && rightChild(index).compareTo(leftChild(index)) > 0) {
                        smallestChildIndex = getRightChildIndex(index);
                    }
                    if (data[index].compareTo(data[smallestChildIndex]) > 0) {
                        break;
                    } else {
                        swap(index, smallestChildIndex);
                    }
                }

                index = smallestChildIndex;
            }
        }
    }




}
