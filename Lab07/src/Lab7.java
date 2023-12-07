import java.util.*;
import java.io.*;


public class Lab7 {
    static class Box {
        int id;
        long value;
        String state;

        Box(int id, long value, String state) {
            this.id = id;
            this.value = value;
            this.state = state;
        }

        public int compareTo(Box other) {
            if (this.value < other.value) {
                return -1;
            } else if (this.value > other.value) {
                return 1;
            } else {
                if (this.id < other.id) {
                    return -1;
                } else if (this.id > other.id) {
                    return 1;
                } else {
                    return 0;
                }
            }
        }
    }

    static class BoxContainer {
        public ArrayList<Box> heap;
        public int size;
        public HashMap<Integer, Integer> idToIndexMap;

        public BoxContainer() {
            this.heap = new ArrayList<>();
            this.idToIndexMap = new HashMap<>();
        }

        public static int getParentIndex(int i) {
            return (i - 1) / 2;
        }

        public void percolateUp(int i) {
            while (i > 0 && bandingBox(heap.get(i), heap.get(getParentIndex(i)))) {
                swap(i, getParentIndex(i));
                i = getParentIndex(i);
            }
        }


        public void percolateDown(int i) {
            int left = 2 * i + 1;
            int right = 2 * i + 2;
            int curr = i;

            if (left < size && bandingBox(heap.get(left), heap.get(curr))) {
                curr = left;
            }

            if (right < size && bandingBox(heap.get(right), heap.get(curr))) {
                curr = right;
            }

            if (curr != i) {
                swap(i, curr);
                percolateDown(curr);
            }
        }


        private boolean bandingBox(Box a, Box b) {
            if (a.value == b.value) {
                return a.id < b.id;
            }
            return a.value > b.value;
        }

        public void insert(Box box) {
            heap.add(box);
            int currentIndex = size;
            idToIndexMap.put(box.id, currentIndex);
            size++;
            percolateUp(currentIndex);
        }

        public Box peek() {
            return heap.get(0);
        }

        public void swap(int firstIndex, int secondIndex) {
            Box temp = heap.get(firstIndex);
            heap.set(firstIndex, heap.get(secondIndex));
            heap.set(secondIndex, temp);
            idToIndexMap.put(heap.get(firstIndex).id, firstIndex);
            idToIndexMap.put(heap.get(secondIndex).id, secondIndex);
        }

        public void updateBox(Box box, long newValue) {
            int index = idToIndexMap.get(box.id);
            box.value = newValue;
            percolateUp(index);
            percolateDown(index);
        }

    }

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        PrintWriter pw = new PrintWriter(System.out);

        int N = Integer.parseInt(br.readLine());

        ArrayList<Box> boxes = new ArrayList<>();
        BoxContainer boxContainer = new BoxContainer();

        for (int i = 0; i < N; i++) {
            StringTokenizer st = new StringTokenizer(br.readLine());
            long value = Long.parseLong(st.nextToken());
            String state = st.nextToken();

            Box box = new Box(boxes.size(), value, state);
            boxes.add(box);
            boxContainer.insert(box);
        }

        int T = Integer.parseInt(br.readLine());

        for (int i = 0; i < T; i++) {
            StringTokenizer st = new StringTokenizer(br.readLine());
            String command = st.nextToken();

            if ("A".equals(command)) {
                long V = Long.parseLong(st.nextToken());
                String S = st.nextToken();
                Box box = new Box(boxes.size(), V, S);
                boxes.add(box);
                boxContainer.insert(box);

            } else if ("D".equals(command)) {
                int penantang = Integer.parseInt(st.nextToken());
                int ditantang = Integer.parseInt(st.nextToken());

                Box p = boxes.get(penantang);
                Box d = boxes.get(ditantang);

                if (!p.state.equals(d.state)) {
                    if ((p.state.equals("R") && d.state.equals("S")) ||
                            (p.state.equals("P") && d.state.equals("R")) ||
                            (p.state.equals("S") && d.state.equals("P"))) {
                        p.value += d.value;
                        d.value /= 2;
                    } else {
                        d.value += p.value;
                        p.value /= 2;
                    }
                    boxContainer.updateBox(p, p.value);
                    boxContainer.updateBox(d, d.value);
                }
            } else if ("N".equals(command)) {
                int penantang = Integer.parseInt(st.nextToken());
                Box p = boxes.get(penantang);

                int[] temp = new int[] {penantang - 1, penantang + 1};

                for (int neighborId : temp) {
                    if (neighborId >= 0 && neighborId < boxes.size()) {
                        Box d = boxes.get(neighborId);

                        if (!p.state.equals(d.state)) {
                            if ((p.state.equals("R") && d.state.equals("S")) ||
                                    (p.state.equals("P") && d.state.equals("R")) ||
                                    (p.state.equals("S") && d.state.equals("P"))) {
                                p.value += d.value;
                                boxContainer.updateBox(p, p.value);
                            }
                        }
                    }
                }
            }
            Box topBox = boxContainer.peek();
            pw.println(topBox.value + " " + topBox.state);
        }

        pw.flush();
        pw.close();
    }
}