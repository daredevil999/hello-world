import java.util.*;
import java.io.*;

// write your matric number here: A0127570H 
// write your name here:  Phua Kian Ming
// year 2016 hash code: gNtTshxeEknrkS9YCKdh (do NOT delete this line)

class OutForAWalk {
  private int V; // number of vertices in the graph (number of rooms in the building)
  private Vector <IntegerTriple> EdgeList;
  private UnionFind UF;
  private int[][] maxEffort;

  public OutForAWalk() {
    V = 0;
  }

  void print () {
    for (int i=0;i<EdgeList.size();i++) {
      IntegerTriple edge = EdgeList.get(i);
      System.out.println(edge.first() + " " + edge.second() + " " + edge.third());
    }
  }

  int Query(int source, int destination) {
    int s,d,ans;
    if (source < destination){ 
      s = source;
      d = destination;
    }
    else {
      d = source;
      s = destination;
    }
    ans = maxEffort[s][d];
    if (ans == 0)
      return maxEffort[s][s];
    else
      return ans;
  }
  
  void processAll() {
    for (int s = 0;s<10;s++) {
      Boolean foundSource = false;
      for (int i = 0; i < EdgeList.size(); i++) { // process all edges, O(E)
        IntegerTriple e = EdgeList.get(i);
        int u = e.second(), v = e.third(), w = e.first(); // note that we have re-ordered the integer triple
        if (!foundSource) {
          if (!UF.isSameSet(u, v))  // if no cycle
            UF.unionSet(u, v); // link these two vertices
          if (u == s || v == s) {
            foundSource = true;
            maxEffort[s][s] = w;
          }          
        }
        else {
          if (!UF.isSameSet(u,v)) {
            if (isSameSet(s,u)) 
              maxEffort[s][u] = w;
            if (isSameSet(s,v)) 
              maxEffort[s][v] = w;
            UF.unionSet(u,v);
          }
        }
      }
    }
  }

  void run() throws Exception {
    // do not alter this method
    IntegerScanner sc = new IntegerScanner(System.in);
    PrintWriter pw = new PrintWriter(new BufferedWriter(new OutputStreamWriter(System.out)));

    int TC = sc.nextInt(); // there will be several test cases
    while (TC-- > 0) {
      V = sc.nextInt();

      // clear the graph and read in a new graph as Adjacency List
      EdgeList = new Vector <IntegerTriple>();
      UF = new UnionFind(V);
      maxEffort = new int[10][V];
      for (int i = 0; i < V; i++) {

        int k = sc.nextInt();
        while (k-- > 0) {
          int j = sc.nextInt(), w = sc.nextInt();
          if (j > i)
            EdgeList.add(new IntegerTriple(w,i,j));
        }
      }

      Collections.sort(EdgeList);; // you may want to use this function or leave it empty if you do not need it
      
      int Q = sc.nextInt();
      processAll();
      while (Q-- > 0) {
        pw.println(Query(sc.nextInt(), sc.nextInt()));
      }
      pw.println(); // separate the answer between two different graphs
    }

    pw.close();
  }

  public static void main(String[] args) throws Exception {
    // do not alter this method
    OutForAWalk ps4 = new OutForAWalk();
    ps4.run();
  }
}

class IntegerScanner { // coded by Ian Leow, using any other I/O method is not recommended
  BufferedInputStream bis;
  IntegerScanner(InputStream is) {
    bis = new BufferedInputStream(is, 1000000);
  }
  
  public int nextInt() {
    int result = 0;
    try {
      int cur = bis.read();
      if (cur == -1)
        return -1;

      while ((cur < 48 || cur > 57) && cur != 45) {
        cur = bis.read();
      }

      boolean negate = false;
      if (cur == 45) {
        negate = true;
        cur = bis.read();
      }

      while (cur >= 48 && cur <= 57) {
        result = result * 10 + (cur - 48);
        cur = bis.read();
      }

      if (negate) {
        return -result;
      }
      return result;
    }
    catch (IOException ioe) {
      return -1;
    }
  }
}

class IntegerPair implements Comparable < IntegerPair > {
  Integer _first, _second;

  public IntegerPair(Integer f, Integer s) {
    _first = f;
    _second = s;
  }

  public int compareTo(IntegerPair o) {
    if (!this.second().equals(o.second()))
      return this.second() - o.second();
    else
      return this.first() - o.first();
  }

  Integer first() { return _first; }
  Integer second() { return _second; }
}



class IntegerTriple implements Comparable < IntegerTriple > {
  Integer _first, _second, _third;

  public IntegerTriple(Integer f, Integer s, Integer t) {
    _first = f;
    _second = s;
    _third = t;
  }

  public int compareTo(IntegerTriple o) {
    if (!this.first().equals(o.first()))
      return this.first() - o.first();
    else if (!this.second().equals(o.second()))
      return this.second() - o.second();
    else
      return this.third() - o.third();
  }

  Integer first() { return _first; }
  Integer second() { return _second; }
  Integer third() { return _third; }
}

class UnionFind {                                              // OOP style
  private Vector<Integer> p, rank, setSize;
  private int numSets;

  public UnionFind(int N) {
    p = new Vector<Integer>(N);
    rank = new Vector<Integer>(N);
    setSize = new Vector<Integer>(N);
    numSets = N;
    for (int i = 0; i < N; i++) {
      p.add(i);
      rank.add(0);
      setSize.add(1);
    }
  }

  public int findSet(int i) { 
    if (p.get(i) == i) return i;
    else {
      int ret = findSet(p.get(i)); p.set(i, ret);
      return ret; } }

  public Boolean isSameSet(int i, int j) { return findSet(i) == findSet(j); }

  public void unionSet(int i, int j) { 
    if (!isSameSet(i, j)) { numSets--; 
    int x = findSet(i), y = findSet(j);
    // rank is used to keep the tree short
    if (rank.get(x) > rank.get(y)) { p.set(y, x); setSize.set(x, setSize.get(x) + setSize.get(y)); }
    else                           { p.set(x, y); setSize.set(y, setSize.get(y) + setSize.get(x));
                                     if (rank.get(x) == rank.get(y)) rank.set(y, rank.get(y)+1); } } }
  public int numDisjointSets() { return numSets; }
  public int sizeOfSet(int i) { return setSize.get(findSet(i)); }
}
