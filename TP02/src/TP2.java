/*
SOURCE AND REFERENCE

Github kak Eugenius Mario : https://github.com/eugeniusms/SDA-2022

 */


import java.io.*;
import java.util.*;


public class TP2 {
    private static InputReader in;
    private static PrintWriter out;

    static int idKelas = 1;
    static int idMurid = 1;
    static CircularDoublyLL<Kelas> LLKelas = new CircularDoublyLL<>();


    public static void main(String[] args) {
        InputStream inputStream = System.in;
        in = new InputReader(inputStream);
        OutputStream outputStream = System.out;
        out = new PrintWriter(outputStream);

        int banyakKelas = in.nextInt();
        for (int i = 0 ; i < banyakKelas ; i++){
            long jumlahMurid = in.nextLong();
            AVLTree treeMurid = new AVLTree();
            int id = idKelas;
            idKelas++;
            Kelas k = new Kelas(treeMurid, 0, id);
            k.jumlahMurid = jumlahMurid;
            LLKelas.addLast(k);
        }
        Kelas current = LLKelas.header;

        for (int i = 0; i < LLKelas.size; i++){
            current = current.next;
            long sumScore = 0;
            for (long z = 0 ; z < current.jumlahMurid ; z++){
                int poin = in.nextInt();
                int id = idMurid;
                idMurid++;
                Siswa s = new Siswa(id, poin);
                current.hashIdSiswa.put(id,s);
                current.hashSiswaCurang.put(s,0);
                sumScore += poin;
                current.treeMurid.root = current.treeMurid.insert(current.treeMurid.root, poin,id);
            }
            current.sumScore = sumScore;
//            printTree(current.treeMurid.root, "", true);
        }

        LLKelas.setPakcilNow(LLKelas.header.next);

        int q = in.nextInt();

        for (int i = 0; i < q ; i++){
            String query = in.next();
            if ("T".equals(query)) {
                T();
//                printTree(LLKelas.PakcilNow.treeMurid.root, "", true);
            } else if ("C".equals(query)) {
                // Code for "C" case
                C();
            } else if ("G".equals(query)) {
                G();
                // Code for "G" case
            } else if ("S".equals(query)) {
                // Code for "S" case
                S();
            } else if ("K".equals(query)) {
                // Code for "K" case
                K();
            } else if ("A".equals(query)) {
                // Code for "A" case
                A();
            }
        }




















        out.close();

    }
    static void T(){
        long poin = in.nextLong();
        int id = in.nextInt();

        AVLTree tree = LLKelas.PakcilNow.treeMurid;

        Siswa s = LLKelas.PakcilNow.hashIdSiswa.get(id);

        if (s == null){
            out.println(-1);
        }else{
            long oldScore = s.poin;
            long tutorBonus = tree.countBefore(tree.root, s.poin);
            if (tutorBonus > poin){
                tutorBonus = poin;
            }
            long newScore = oldScore + tutorBonus + poin;

            tree.root = tree.delete(tree.root, s.poin, s.id);
            tree.root = tree.insert(tree.root, newScore,s.id);

            s.poin = newScore;
            LLKelas.PakcilNow.sumScore -= oldScore;
            LLKelas.PakcilNow.sumScore += newScore;

            out.println(newScore);
        }

    }
    static void C(){
        int id = in.nextInt();
        Kelas currentClass = LLKelas.PakcilNow;
        AVLTree tree = currentClass.treeMurid;

        Siswa s = currentClass.hashIdSiswa.get(id);

        if (s == null){
            out.println(-1);
        }else{
            int jumlahCurang = currentClass.hashSiswaCurang.get(s);
            jumlahCurang++;
            if (jumlahCurang == 1){
                currentClass.sumScore -= s.poin;
                tree.root = tree.delete(tree.root, s.poin, s.id);
                s.poin = 0;
                tree.root = tree.insert(tree.root, s.poin, s.id);
                currentClass.hashSiswaCurang.put(s,jumlahCurang);
                out.println(s.poin);

            }else if (jumlahCurang == 2){
                currentClass.sumScore -= s.poin;
                tree.root = tree.delete(tree.root, s.poin, s.id);
                s.poin = 0;

                Kelas target = LLKelas.getLast();
                if (target == currentClass){
                    tree.root = tree.insert(tree.root, s.poin, s.id);
                    currentClass.hashSiswaCurang.put(s,jumlahCurang);
                    out.println(currentClass.id);
                }else {
                    currentClass.hashIdSiswa.remove(id);
                    currentClass.hashSiswaCurang.remove(s);
                    currentClass.jumlahMurid--;
                    target.treeMurid.root = tree.insert(target.treeMurid.root, s.poin, s.id);
                    target.jumlahMurid++;
                    target.hashIdSiswa.put(id, s);
                    target.hashSiswaCurang.put(s, jumlahCurang);
                    out.println(target.id);
                }

            }else{
                currentClass.jumlahMurid--;
                currentClass.sumScore -= s.poin;
                tree.root = tree.delete(tree.root, s.poin, s.id);
                currentClass.hashSiswaCurang.remove(s);
                currentClass.hashIdSiswa.remove(id);
                out.println(id);

            }

            if (currentClass.hashIdSiswa.size() < 6){
                Kelas target = null;
                if (currentClass.next != LLKelas.footer){
                    target = currentClass.next;
                }else if(currentClass.prev != LLKelas.header){
                    target = currentClass.prev;
                }else {
                    target = LLKelas.header.next;
                }
                LLKelas.setPakcilNow(target);
                LLKelas.remove(currentClass);

                for (Integer i : currentClass.hashIdSiswa.keySet()){
                    Siswa m = currentClass.hashIdSiswa.get(i);
                    target.hashIdSiswa.put(i, m);
                    target.hashSiswaCurang.put(m, currentClass.hashSiswaCurang.get(m));
                    target.sumScore += m.poin;
                    target.jumlahMurid++;
                    target.treeMurid.root = target.treeMurid.insert(target.treeMurid.root, m.poin, m.id);
                }
            }
        }
    }

    static void G(){
        String arah = in.next();
        if (arah.equals("L")){
            LLKelas.gerakKiri();

        }else{
            LLKelas.gerakKanan();
        }
        out.println(LLKelas.PakcilNow.id);
    }

    static void S(){
        Kelas kelasPakcil = LLKelas.PakcilNow;
        Kelas kelasBetter = kelasPakcil.prev;
        Kelas kelasWorse = kelasPakcil.next;

        Kelas compared = null;
        if (kelasBetter == null || kelasWorse == null){
            if (kelasBetter != null){compared = kelasBetter;}
            else if (kelasWorse != null){compared = kelasWorse;}
        }
        if (kelasBetter != null && kelasWorse != null){
            Siswa top1;
            Siswa bot1;


        }else{
            if(compared == null){
                out.println("-1 -1");
            }else{
                //todo kelas 1 doang

            }
        }



    }

    static void K(){
        Kelas[] sortedKelas = LLKelas.sort();
        LLKelas.clear();
        for (Kelas k : sortedKelas){
            LLKelas.addLast(k);
        }
        int urutanPakcil = 1;
        Kelas curr = LLKelas.header.next;
        while (curr != LLKelas.footer){
            if (curr == LLKelas.PakcilNow){
                break;
            }
            curr = curr.next;
            urutanPakcil++;
        }

        out.println(urutanPakcil);

    }

    static void A(){
        int jumlahSiswa = in.nextInt();
        int id = idKelas;
        idKelas++;
        AVLTree t = new AVLTree();
        Kelas k = new Kelas(t, 0, id);
        k.jumlahMurid = jumlahSiswa;
        LLKelas.addLast(k);

        for (int i = 0; i < jumlahSiswa ; i++){
            int idm = idMurid;
            idMurid++;
            Siswa s = new Siswa(idm, 0);
            k.hashIdSiswa.put(idm, s);
            k.hashSiswaCurang.put(s, 0);
            k.treeMurid.root = k.treeMurid.insert(k.treeMurid.root, 0, idm);
        }
        out.println(k.id);
    }














    // taken from https://www.programiz.com/dsa/avl-tree
    // a method to print the contents of a Tree data structure in a readable
    // format. it is encouraged to use this method for debugging purposes.
    // to use, simply copy and paste this line of code:
    // printTree(tree.root, "", true);
    static void printTree(Node currPtr, String indent, boolean last) {
        if (currPtr != null) {
            out.print(indent);
            if (last) {
                out.print("R----");
                indent += "   ";
            } else {
                out.print("L----");
                indent += "|  ";
            }
            out.println(currPtr.key);
            printTree(currPtr.left, indent, false);
            printTree(currPtr.right, indent, true);
        }
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


    static class Kelas {
        Kelas prev,next;
        AVLTree treeMurid;
        int id;
        long jumlahMurid;
        long sumScore;

        double rerata = 0;

        HashMap<Integer,Siswa> hashIdSiswa= new HashMap<>();
        HashMap<Siswa,Integer> hashSiswaCurang = new HashMap<>();

        //struktur data isinya murid (maybe avl??)

        public Kelas(AVLTree treeMurid, long sumScore, int id){
            this.id = id;
            this.treeMurid = treeMurid;
            this.sumScore = sumScore;
        }

        void updateRerata(){
            rerata = sumScore / (double) hashIdSiswa.size();
        }

    }
    // Class DaftarKelas digunakan untuk menyimpan semua Kelas
    static class CircularDoublyLL<E> {
        int size; // Jumlah Kelas
        Kelas header, footer; // null node for easier add and remove
        Kelas PakcilNow; // untuk menyimpan lokasi Pakcil

        // construct empty list
        CircularDoublyLL() {
            this.size = 0;
            this.header = new Kelas(null, 0, 0);
            this.footer = new Kelas( null, 0, 0);
        }

        Kelas getLast(){
            return footer.prev;
        }

        // Method digunakan untuk menambahkan node baru di akhir linkedlist
        void addLast(Kelas Kelas) {
            if (this.size == 0) { // empty
                footer.prev = Kelas;
                Kelas.next = footer;
                header.next = Kelas;
                Kelas.prev = header;

            } else { // is exist
                footer.prev.next = Kelas;
                Kelas.prev = footer.prev;
                Kelas.next = footer;
                footer.prev = Kelas;
            }

            this.size += 1;
        }

        // Method digunakan untuk remove node Kelas dari linkedlist
        Kelas remove(Kelas Kelas) {
            if (this.size == 0) { // empty
                // do nothing
                throw new NullPointerException("LinkedList Size is 0");
            } else if (this.size == 1) { // tidak ada elemen kedua
                header.next = footer;
                footer.prev = header;
            } else { // saat ada lebih dari 1 node
                Kelas.prev.next = Kelas.next;
                Kelas.next.prev = Kelas.prev;
            }

            this.size -= 1;
            return Kelas;
        }

        // Method digunakan untuk set Kelas depan Pakcil saat ini
        void setPakcilNow(Kelas Kelas) {
            PakcilNow = Kelas;
        }

        // Method digunakan untuk mengecek letak urutan Kelas depan Pakcil saat ini
        int getPakcilKelasSortedNow() {
            Kelas check = header.next;
            int counter = 0;
            while (!check.equals(PakcilNow)) {
                counter++;
                check = check.next;
            }
            // return counter;
            return counter + 1;
        }

        // Menggerakan Pakcil ke kanan
        Kelas gerakKanan() {
            if (this.size == 0) { // empty
                // do nothing
                throw new NullPointerException("LinkedList Size is 0");
            } else if (this.size == 1) { // cuma satu elemen
                // do nothing
            } else if (PakcilNow.next.equals(footer)) { // elemen terakhir
                PakcilNow = header.next;
            } else { // kasus normal
                PakcilNow = PakcilNow.next;
            }
            return PakcilNow;
        }

        // Menggerakan Pakcil ke kiri
        Kelas gerakKiri() {
            if (this.size == 0) { // empty
                // do nothing
                throw new NullPointerException("LinkedList Size is 0");
            } else if (this.size == 1) { // cuma satu elemen
                // do nothing
            } else if (PakcilNow.prev.equals(header)) { // elemen pertama
                PakcilNow = footer.prev;
            } else { // kasus normal
                PakcilNow = PakcilNow.prev;
            }
            return PakcilNow;
        }

        // Pindah Kelas sekaligus mereturn Kelas untuk ditempati Pakcil
        void pindahKelas(Kelas Kelas) {
            if (this.size == 0) {
                // do nothing
            } else if (this.size == 1) { // cuma satu Kelas permainan
                // do nothing
            } else if (Kelas.next.equals(footer)) { // Kelas berada paling kanan
                // Kelas stay
                // Pakcil pindah ke depan
                PakcilNow = header.next;
            } else { // sisanya
                // Pakcil pindah ke kanannya
                PakcilNow = Kelas.next;
                // pindah Kelas ke pojok kanan
                Kelas KelasDipindah = remove(Kelas);
                this.addLast(KelasDipindah);
            }
        }

        // Clear all Kelas
        void clear() {
            header.next = footer;
            footer.prev = header;
            this.size = 0;
        }

        // Menggabungkan dua subarray dari array Kelas.
        // Subarray pertama adalah arr[l..m]
        // Subarray kedua adalah arr[m+1..r]
        void merge(Kelas[] arr, int l, int m, int r) {
            // Menentukan ukuran dari dua subarray yang akan digabungkan
            int n1 = m - l + 1;
            int n2 = r - m;

            /* Membuat array sementara */
            Kelas[] L = new Kelas[n1];
            Kelas[] R = new Kelas[n2];

            /* Menyalin data ke array sementara */
            for (int i = 0; i < n1; ++i)
                L[i] = arr[l + i];
            for (int j = 0; j < n2; ++j)
                R[j] = arr[m + 1 + j];

            /* Menggabungkan array sementara */

            // Index awal dari subarray pertama dan kedua
            int i = 0, j = 0;

            // Index awal dari array gabungan
            int k = l;
            while (i < n1 && j < n2) {
                // Membandingkan rerata dari Kelas di subarray
                if (L[i].rerata > R[j].rerata) {
                    arr[k] = L[i];
                    i++;
                } else if (L[i].rerata == R[j].rerata) {
                    // Jika rerata sama, bandingkan ID Kelas
                    if (L[i].id < R[j].id) {
                        arr[k] = L[i];
                        i++;
                    } else {
                        arr[k] = R[j];
                        j++;
                    }
                } else {
                    arr[k] = R[j];
                    j++;
                }
                k++;
            }

            /* Menyalin elemen yang tersisa dari L[] jika ada */
            while (i < n1) {
                arr[k] = L[i];
                i++;
                k++;
            }

            /* Menyalin elemen yang tersisa dari R[] jika ada */
            while (j < n2) {
                arr[k] = R[j];
                j++;
                k++;
            }
        }

        void mergesort(Kelas[] arr, int l, int r) {
            // Mengurutkan array Kelas menggunakan algoritma merge sort
            if (l < r) {
                // Menemukan titik tengah
                int m = l + (r - l) / 2;

                // Mengurutkan paruh pertama dan kedua
                mergesort(arr, l, m);
                mergesort(arr, m + 1, r);

                // Menggabungkan kedua paruh yang telah diurutkan
                merge(arr, l, m, r);
            }
        }

        // Metode untuk mengurutkan Kelas
        Kelas[] sort() {
            // Jika LinkedList tidak kosong
            // Membuat array Kelas sesuai dengan ukuran LinkedList
            Kelas[] arr = new Kelas[this.size];
            Kelas current = header.next;
            // Memasukkan semua Kelas ke dalam array
            for(int i = 0; i < this.size; i++) {
                current.updateRerata();
                arr[i] = current;
                current = current.next;
            }
            // Mengurutkan array dengan algoritma merge sort
            mergesort(arr, 0, this.size - 1);
            return arr;
        }



    }
    static class Siswa{
        int id;
        long poin;
        public Siswa(int id, long poin){
            this.id = id;
            this.poin = poin;
        }
    }

    static class Node { // AVL Node
        long key, height, count; // key => score, count => banyaknya node pada suatu subtree dengan root == node
        Node left, right;
        long jumlahSama; // jumlah isi key yg sama (duplicate)



        int jumlahCurang = 0;

        Node(long key, long id) {
            this.key = key;
            this.height = 1;
            this.count = 1;
            this.jumlahSama = 1;

        }
    }

    static class AVLTree {

        Node root;

        // Implement right rotate

        Node rightRotate(Node y) {
            Node x = y.left;
            Node T2 = x.right;

            // Perform rotation
            x.right = y;
            y.left = T2;

            // Update heights & count
            y.height = max(getHeight(y.left), getHeight(y.right)) + 1;
            y.count = y.jumlahSama + getCount(y.left) + getCount(y.right);

            x.height = max(getHeight(x.left), getHeight(x.right)) + 1;
            x.count = x.jumlahSama + getCount(x.left) + getCount(x.right);

            // Return new root
            return x;
        }

        // Implement left rotate
        Node leftRotate(Node y) {
            Node x = y.right;
            Node T2 = x.left;

            // Perform rotation
            x.left = y;
            y.right = T2;

            // Update heights & count
            y.height = max(getHeight(y.left), getHeight(y.right)) + 1;
            y.count = y.jumlahSama + getCount(y.left) + getCount(y.right);

            x.height = max(getHeight(x.left), getHeight(x.right)) + 1;
            x.count = x.jumlahSama + getCount(x.left) + getCount(x.right);

            // Return new root
            return x;
        }

        // Implement insert node to AVL Tree
        Node insert(Node node, long key, long id) {
            if (node == null) {
                return (new Node(key, id));
            }

            if (key < node.key) {
                node.left = insert(node.left, key, id);
            } else if (key > node.key) {
                node.right = insert(node.right, key, id);
            } else {
                //TODO: KALO POIN SAMA
                node.jumlahSama += 1;
                node.count += 1;
                return node;

            }

            // Update height & count
            node.height = 1 + max(getHeight(node.left), getHeight(node.right));
            node.count = node.jumlahSama + getCount(node.left) + getCount(node.right);

            // Get balance factor
            long balance = getBalance(node);

            // If this node becomes unbalanced, then there are 4 cases

            // Left Left Case
            if (balance > 1 && key < node.left.key) {
                return rightRotate(node);
            }

            // Right Right Case
            if (balance < -1 && key > node.right.key) {
                return leftRotate(node);
            }

            // Left Right Case
            if (balance > 1 && key > node.left.key) {
                node.left = leftRotate(node.left);
                return rightRotate(node);
            }

            // Right Left Case
            if (balance < -1 && key < node.right.key) {
                node.right = rightRotate(node.right);
                return leftRotate(node);
            }

            return node;
        }

        // TODO: Delete a node
        Node delete(Node root, long key, long id)
        {
            // STEP 1: PERFORM STANDARD BST DELETE
            if (root == null)
                return root;

            // If the key to be deleted is smaller than
            // the root's key, then it lies in left subtree
            if (key < root.key)
                root.left = delete(root.left, key, id);

                // If the key to be deleted is greater than the
                // root's key, then it lies in right subtree
            else if (key > root.key)
                root.right = delete(root.right, key, id);

                // if key is same as root's key, then this is the node
                // to be deleted
            else
            {
                // if jumlah sama masih ada rootnya jangan diilangin dulu gan, duplicatenya urusin
                if (root.jumlahSama >= 1) {
                    root.jumlahSama -= 1;
                    root.count -= 1;
                    if (root.jumlahSama == 0){
                        if ((root.left == null) || (root.right == null)) {
                            root = (root.left == null) ? root.right : root.left;
                        }
                    }
                } else {
                    // node with only one child or no child
                    if ((root.left == null) || (root.right == null)) {
                        root = (root.left == null) ? root.right : root.left;
                    } else {
                        // node with two children: Get the inorder
                        // successor (smallest in the right subtree)
                        Node temp = lowerBound(root.right);

                        // Copy the inorder successor's data to this node
                        root.key = temp.key;
                        // fixing yg keupdate ga cuma key doang, ada count juga
                        root.jumlahSama = temp.jumlahSama;
                        root.count = temp.count;
                        // Delete the inorder successor
                        root.right = delete(root.right, temp.key, id);
                    }
                }
            }

            // If the tree had only one node then return
            if (root == null)
                return root;

            // STEP 2: UPDATE HEIGHT OF THE CURRENT NODE
            root.height = max(getHeight(root.left), getHeight(root.right)) + 1;
            root.count = root.jumlahSama + getCount(root.left) + getCount(root.right);

            // STEP 3: GET THE BALANCE FACTOR OF THIS NODE (to check whether
            // this node became unbalanced)
            long balance = getBalance(root);

            // If this node becomes unbalanced, then there are 4 cases
            // Left Left Case
            if (balance > 1 && getBalance(root.left) >= 0)
                return rightRotate(root);

            // Left Right Case
            if (balance > 1 && getBalance(root.left) < 0)
            {
                root.left = leftRotate(root.left);
                return rightRotate(root);
            }

            // Right Right Case
            if (balance < -1 && getBalance(root.right) <= 0)
                return leftRotate(root);

            // Right Left Case
            if (balance < -1 && getBalance(root.right) > 0)
            {
                root.right = rightRotate(root.right);
                return leftRotate(root);
            }

            return root;
        }

        // Mencari lowerBound dari suatu node
        Node lowerBound(Node node) {
            // Return node with the lowest from this node
            Node current = node;
            while (current.left != null) {
                current = current.left;
            }
            return current;
        }

        // Mencari upperBound dari suatu node
        Node upperBound(Node node) {
            // Return node with the greatest from this node
            Node current = node;
            while (current.right != null) {
                current = current.right;
            }
            return current;
        }


        // Utility function to get height of node
        long getHeight(Node node) {
            if (node == null) {
                return 0;
            }
            return node.height;
        }

        // Utility function to get num of peoples
        long getCount(Node node) {
            if (node == null) {
                return 0;
            }
            return node.count;
        }

        // Utility function to get balance factor of node
        long getBalance(Node node) {
            if (node == null) {
                return 0;
            }

            return getHeight(node.left) - getHeight(node.right);
        }

        // Utility function to get max of 2 longs
        long max(long a, long b) {
            return (a > b) ? a : b;
        }

        // QUERY MAIN, LIHAT
        // Method digunakan untuk mencari jumlah score yang kurang dari inserted key
        long countBefore(Node node, long insertedKey) {
            if (node == null) { // Jika node kosong, return 0
                return 0;
            }
            // Jika sudah didapat insertedKey sama dengan key node, maka cari count di subtree kiri dulu
            if (node.key == insertedKey) {
                return node.jumlahSama + getCount(node.left) - 1;
            }
            // Jika insertedKey lebih kecil dari key node, maka cari di subtree kiri
            if (node.key < insertedKey) {
                // Cek kiri lalu, ke kanan
                if (node.left != null) {
                    // Jika ada node di subtree kiri, maka cari count di subtree kiri + duplicatenya
                    return node.jumlahSama + node.left.count + countBefore(node.right, insertedKey);
                } else {
                    return node.jumlahSama + countBefore(node.right, insertedKey);
                }
            }
            // Ke kiri untuk cari key yang cocok
            return countBefore(node.left, insertedKey);
        }

        // Method digunakan untuk mencari score max
        Node findMax() {
            Node temp = root;
            while (temp.right != null) {
                temp = temp.right;
            }
            return temp;
        }

        Node findMin() {
            Node temp = root;
            while (temp.left != null) {
                temp = temp.left;
            }
            return temp;
        }
    }

}