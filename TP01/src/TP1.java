import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

public class TP1 {
    private static InputReader in;
    private static PrintWriter out;
    static int idPengunjung = 0;
    static int idWahana = 0;
    static int cariWahana=0;
    static Wahana[] wahana;
    static Pengunjung[] pengunjung;
    static ArrayDeque<Pengunjung> daftarKeluar = new ArrayDeque<>();
    public static int[][][] memo;
    static int minimumRideCost = Integer.MAX_VALUE;
    static int maximumMoneyHeld;
    static ArrayList<Integer> kumpulanPoin = new ArrayList<>();
    static ArrayList<Integer> kumpulanHarga = new ArrayList<>();

    public static void main(String[] args) {
        InputStream inputStream = System.in;
        in = new InputReader(inputStream);
        OutputStream outputStream = System.out;
        out = new PrintWriter(outputStream);

        // Read value of M (WAHANA)
        int M = in.nextInt();
        wahana = new Wahana[M];


        for (int i = 0; i < M; i++) {
            int h = in.nextInt();
            if (h < minimumRideCost){minimumRideCost = h;}
            kumpulanHarga.add(h);
            int p = in.nextInt();
            kumpulanPoin.add(p);
            int kp = in.nextInt();
            int ft = in.nextInt();
            //implementasi class wahana
            wahana[i] = new Wahana(h, p, kp, ft);
        }

        // Read value of N (PENGUNJUNG)
        int N = in.nextInt();
        pengunjung = new Pengunjung[N];
        maximumMoneyHeld = 0;

        for (int i = 0; i < N; i++) {
            String ti = in.next();
            int ui = in.nextInt();
            if (ui>maximumMoneyHeld){maximumMoneyHeld = ui;}
            pengunjung[i] = new Pengunjung(ti, ui);
        }

        // inisialisasi memo untuk query O
        memo = new int[wahana.length + 1][maximumMoneyHeld + 1][2];
        for (int j = 0; j <= wahana.length; j++) {
            for (int k = 0; k <= maximumMoneyHeld; k++) {
                Arrays.fill(memo[j][k], -1);
            }
        }


        // Read value of T (AKTIVITAS)
        int T = in.nextInt();

        for (int i = 0; i < T; i++) {
            String query = in.next();
            if (query.equals("A")) {
                int id = in.nextInt();
                int idw = in.nextInt();
                // implementasi query A
                Pengunjung p = pengunjung[id - 1];
                Wahana w = wahana[idw - 1];
                cariWahana = idw;
                if (p.uang >= w.harga && !p.isKeluar) {
                    if (p.jenis.equals("FT")){
                        w.mapTotalBermain.put(p.id,p.totalBermain);
                        w.antreanFT.add(p);
                    }else{
                        w.mapTotalBermain.put(p.id,p.totalBermain);
                        w.antreanReg.add(p);
                    }
                    out.println(w.antreanFT.size()+w.antreanReg.size());
                } else {
                    out.println("-1");
                }

            } else if (query.equals("E")) {
                int idw = in.nextInt();
                // implementasi query E
                Wahana w = wahana[idw - 1];
                cariWahana = idw;
                int maxCap = w.kapasitas;
                int maxPrioCap = w.maxPrio;
                boolean adaOutput = false;
                if (w.antreanFT.isEmpty() && w.antreanReg.isEmpty()){
                    out.println(-1);
                }else{
                    while(w.antreanFT.size() + w.antreanReg.size() > 0){
                        if(maxCap == 0){
                            break;
                        }
                        if (maxPrioCap > 0){
                            if(!w.antreanFT.isEmpty()){
                                Pengunjung p = w.antreanFT.poll();
                                if (p.uang < w.harga || p.isKeluar){continue;}
                                p.point += w.poin;
                                p.uang -= w.harga;
                                p.totalBermain++;
                                if (p.uang == 0){
                                    p.isKeluar = true;
                                    daftarKeluar.add(p);
                                }
                                maxCap--;
                                maxPrioCap--;
                                out.print(p.id + " ");
                                adaOutput = true;
                                continue;
                            }
                        }
                        Pengunjung p = w.antreanReg.poll();
                        if (p == null){
                            if(!w.antreanFT.isEmpty()){
                                Pengunjung z = w.antreanFT.poll();
                                if (z.uang < w.harga || z.isKeluar){continue;}
                                z.point += w.poin;
                                z.uang -= w.harga;
                                z.totalBermain++;
                                if (z.uang == 0){
                                    z.isKeluar = true;
                                    daftarKeluar.add(z);
                                }
                                maxCap--;
                                maxPrioCap--;
                                out.print(z.id + " ");
                                adaOutput = true;

                            }
                        }else {
                            if (p.uang < w.harga || p.isKeluar) {
                                continue;
                            }
                            maxCap--;
                            p.point += w.poin;
                            p.uang -= w.harga;
                            p.totalBermain++;
                            if (p.uang == 0) {
                                p.isKeluar = true;
                                daftarKeluar.add(p);
                            }
                            out.print(p.id + " ");
                            adaOutput = true;
                        }
                    }
                    if (!adaOutput){
                        out.println(-1);
                    }else{
                        out.println();
                    }

                }



            } else if (query.equals("S")) {
                int id = in.nextInt();
                int idw = in.nextInt();
                // implementasi query S
                int ans = 0;
                int counterIndex = 1;

                Pengunjung p = pengunjung[id - 1];
                Wahana w = wahana[idw-1];
                cariWahana = idw;
                int maxCap = w.kapasitas;
                int maxPrioCap = w.maxPrio;
                boolean adaDiAntrean = false;

                PriorityQueue<Pengunjung> antreanFTTemp = new PriorityQueue<>();
                PriorityQueue<Pengunjung> antreanRegTemp = new PriorityQueue<>();
                if ((p.isKeluar)){
                    out.println(-1);
                } else {
                    while (w.antreanReg.size() + w.antreanFT.size() > 0){
                        if (maxCap == 0){
                            maxCap = w.kapasitas;
                            maxPrioCap = w.maxPrio;
                        }
                        if (maxPrioCap > 0){
                            if (!w.antreanFT.isEmpty()){
                                Pengunjung pp = w.antreanFT.poll();
                                antreanFTTemp.add(pp);
                                if (pp.uang < w.harga || pp.isKeluar){}
                                else{
                                    maxCap--;
                                    maxPrioCap--;
                                    if (pp.id == p.id){
                                        ans = counterIndex;
                                        adaDiAntrean = true;
                                    }
                                    counterIndex++;
                                }
                                continue;
                            }
                        }
                        Pengunjung pp = w.antreanReg.poll();
                        if (pp != null){
                            antreanRegTemp.add(pp);
                        } else if (!w.antreanFT.isEmpty()) {
                            Pengunjung z = w.antreanFT.poll();
                            antreanFTTemp.add(z);
                            if (z.uang < w.harga || z.isKeluar){}
                            else{
                                maxCap--;
                                maxPrioCap--;
                                if (z.id == p.id){
                                    ans = counterIndex;
                                    adaDiAntrean = true;
                                }
                                counterIndex++;
                            }
                            continue;
                        }
                        if (pp.uang < w.harga || pp.isKeluar){}
                        else{
                            maxCap--;
                            maxPrioCap--;
                            if (pp.id == p.id){
                                ans = counterIndex;
                                adaDiAntrean = true;
                            }
                            counterIndex++;
                        }
                    }
                    w.antreanFT = antreanFTTemp;
                    w.antreanReg = antreanRegTemp;
                    if (!adaDiAntrean){
                        out.println(-1);
                    }else {
                        out.println(ans);
                    }
                }

            } else if (query.equals("F")) {
                int p = in.nextInt();
                if (daftarKeluar.isEmpty()) {
                    out.println(-1);
                } else {
                    Pengunjung temp;
                    if (p == 1) {
                        temp = daftarKeluar.pollLast();

                    }else{
                        temp = daftarKeluar.pollFirst();
                    }
                    out.println(temp.point);
                }

            }else{
                int id = in.nextInt();
                // implementasi query O
                Pengunjung p = pengunjung[id-1];
                if (p.uang < minimumRideCost){
                    out.println(0);

                }
                else {
                    int minMoneyRequiredEven = Integer.MAX_VALUE;
                    int minMoneyRequiredOdd = Integer.MAX_VALUE;
                    int minMoneyRequired = Integer.MAX_VALUE;
                    int currentPointsOdd = Integer.MIN_VALUE;
                    int currentPointsEven = Integer.MIN_VALUE;
                    int finalPoints = 0;
                    int resultParity = -1;
                    boolean multipleParities = false;

                    // Iterasi untuk setiap kemungkinan jumlah uang dengan parity genap
                    for (int money = minimumRideCost; money <= p.uang; money++) {
                        // Hitung poin maksimal menggunakan dynamic programming
                        int points = maxPoin(0, money, 0);
                        if (points > currentPointsEven || (points == currentPointsEven && money < minMoneyRequiredEven)) {
                            currentPointsEven = points;
                            minMoneyRequiredEven = money;
                            resultParity = 0;

                        }
                    }

                    // Iterasi untuk setiap kemungkinan jumlah uang dengan parity ganjil
                    for (int money = minimumRideCost; money <= p.uang; money++) {
                        // Hitung poin maksimal menggunakan dynamic programming
                        int points = maxPoin(0, money, 1);
                        if (points > currentPointsOdd || (points == currentPointsOdd&& money < minMoneyRequiredOdd)) {
                            currentPointsOdd = points;
                            minMoneyRequiredOdd = money;
                            resultParity = 1;
                        }
                    }
                    if (currentPointsEven > currentPointsOdd){
                        finalPoints = currentPointsEven;
                        minMoneyRequired = minMoneyRequiredEven;
                        resultParity = 0;
                    }else if(currentPointsEven < currentPointsOdd){
                        finalPoints = currentPointsOdd;
                        minMoneyRequired = minMoneyRequiredOdd;
                    }else {
                        if (minMoneyRequiredOdd > minMoneyRequiredEven){
                            finalPoints = currentPointsEven;
                            minMoneyRequired = minMoneyRequiredEven;
                            resultParity = 0;
                        } else if (minMoneyRequiredOdd < minMoneyRequiredEven) {
                            finalPoints = currentPointsOdd;
                            minMoneyRequired = minMoneyRequiredOdd;
                        }else {
                            multipleParities = true;
                        }
                    }


                    printResult(finalPoints, minMoneyRequired, resultParity, multipleParities);
                }



            }

        }
        out.close();
    }




    // Fungsi rekursif untuk menghitung poin maksimal
    static int maxPoin(int idx, int remainingMoney, int parity) {
        int wahanaTotal = wahana.length;
        //base case apabila index berada di wahana terakhir atau uang sudah habis
        if (idx == wahanaTotal || remainingMoney == 0) {
            return 0;
        }
        //apabila data maxPoin sudah ada di memo
        if (memo[idx][remainingMoney][parity] != -1){
            return memo[idx][remainingMoney][parity];
        }
        // Hitung poin tanpa  wahana saat ini
        int tanpaWahana = maxPoin(idx + 1, remainingMoney, parity);
        int denganWahana = Integer.MIN_VALUE;
        if (parity != (idx + 1) % 2 && remainingMoney >= kumpulanHarga.get(idx)) {
            // Hitung poin dengan  wahana saat ini
            denganWahana = maxPoin(idx + 1, remainingMoney - kumpulanHarga.get(idx), (parity + 1) % 2) + kumpulanPoin.get(idx);
        }
        //membandingkan poin maksimal disertai wahana atau tidak disertai dengan wahana
        memo[idx][remainingMoney][parity] = Math.max(tanpaWahana, denganWahana);

        // Kembalikan hasil poin maksimal
        return memo[idx][remainingMoney][parity];
    }



    // Fungsi untuk mengembalikan daftar item yang dipilih berdasarkan poin maksimal
    static LinkedList<Integer> backtrackPath(int result, int remainingMoney, int parity) {
        int wahanaTotal = wahana.length;
        LinkedList<Integer> path = new LinkedList<>();
        int pointsRemaining = result;

        // Untuk setiap wahana, cek apakah wahana tersebut dipilih atau tidak
        for (int i = 0; i < wahanaTotal && pointsRemaining > 0; i++) {
            if (remainingMoney >= kumpulanHarga.get(i) && parity != (i + 1) % 2 &&
                    pointsRemaining - kumpulanPoin.get(i) == maxPoin(i + 1, remainingMoney - kumpulanHarga.get(i), (parity + 1) % 2)) {
                path.add(i + 1);
                pointsRemaining -= kumpulanPoin.get(i);
                remainingMoney -= kumpulanHarga.get(i);
                parity = (parity + 1) % 2;
            }
        }

        // Kembalikan daftar wahana yang dipilih
        return path;
    }

    static void printResult(int currentPoints, int minMoneyRequired, int resultParity, boolean multipleParities) {
        // Mencetak poin maksimal yang dicapai.
        out.print(currentPoints);

        // Jika hanya ada satu parity untuk mencapai poin maksimal.
        if (!multipleParities) {
            // Mendapatkan jalur item yang dipilih.
            LinkedList<Integer> itemsPath = backtrackPath(currentPoints, minMoneyRequired, resultParity);

            // Mencetak setiap item dalam jalur yang dipilih.
            for (Integer item : itemsPath) {
                out.print(" " + item);
            }
            out.println();

            // Jika ada dua parity yang bisa mencapai poin maksimal.
        } else {
            // Mendapatkan jalur item untuk parity genap dan ganjil.
            LinkedList<Integer> pathEven = backtrackPath(currentPoints, minMoneyRequired, 0);
            LinkedList<Integer> pathOdd = backtrackPath(currentPoints, minMoneyRequired, 1);

            // Mengonversi linkedlist kedua jalur menjadi string.
            String strPathEven = pathEven.stream().map(String::valueOf).collect(Collectors.joining(" "));
            String strPathOdd = pathOdd.stream().map(String::valueOf).collect(Collectors.joining(" "));

            // Memilih jalur yang memiliki urutan leksikografis lebih kecil dan mencetaknya.
            if (strPathEven.compareTo(strPathOdd) < 0) {
                out.println(" " + strPathEven);
            } else {
                out.println(" " + strPathOdd);
            }
        }

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
    }
    static class Pengunjung implements Comparable<Pengunjung>{
        String jenis;
        int uang;
        int id;
        int totalBermain = 0;
        int point = 0;

        boolean isKeluar = false;
        public Pengunjung(String ti, int ui){
            this.jenis = ti;
            this.uang = ui;
            this.id = ++idPengunjung;
        }

        @Override
        public int compareTo(Pengunjung o) {
            int totalMain1 = wahana[cariWahana-1].mapTotalBermain.get(this.id);
            int totalMain2 = wahana[cariWahana-1].mapTotalBermain.get(o.id);
            if (totalMain1 < totalMain2){
                return -1;
            } else if (totalMain1 > totalMain2) {
                return 1;
            }else {
                return Integer.compare(this.id,o.id);
            }
        }
    }

    static class Wahana{
        int harga;
        int poin;
        int kapasitas;
        int maxPrio;
        int id = 0;

        PriorityQueue<Pengunjung> antreanFT;
        PriorityQueue<Pengunjung> antreanReg;
        public HashMap<Integer,Integer> mapTotalBermain = new HashMap<>();
        public Wahana(int h, int p, int kp, int ft){
            this.harga = h;
            this.poin = p;
            this.kapasitas = kp;
            this.maxPrio = (int) Math.ceil((kp * ft)/100.0);
            this.id = ++idWahana;
            antreanFT = new PriorityQueue<>();
            antreanReg = new PriorityQueue<>();

        }
    }



}

