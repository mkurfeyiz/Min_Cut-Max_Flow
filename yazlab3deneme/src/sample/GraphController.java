package sample;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Set;


import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class GraphController implements Initializable {

    @FXML Label maxFlowAmount;
    @FXML TextField minCutAmount;
    @FXML TextArea verilenMatris;

    int[][] graph;
    int muslukSayisi = 0,giris = 0,cikis = 0;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    /*
    * int maxFlow;

        MaxFlowMinCut maxFlowMinCut = new MaxFlowMinCut(muslukSayisi);
        maxFlow = maxFlowMinCut.maxFlowMinCut(graph, giris, cikis);

        System.out.println("The Max Flow is " + maxFlow);
        System.out.println("The Cut Set is ");

        maxFlowMinCut.printCutSet();*/

    public void setGraph(int muslukSayisi,int[][] graph,int giris,int cikis,ArrayList<String> sayilar){
        this.muslukSayisi = muslukSayisi;
        this.graph = graph;
        this.giris = giris;
        this.cikis = cikis;

        int maxFlow;
        String matris="";

        MaxFlowMinCut maxFlowMinCut = new MaxFlowMinCut(muslukSayisi);
        maxFlow = maxFlowMinCut.maxFlowMinCut(graph, giris, cikis);

        System.out.println("The Max Flow is " + maxFlow);
        System.out.println("The Cut Set is ");

        //Max Flow
        maxFlowAmount.setText(Integer.toString(maxFlow));

        //Min Cuti yazdiriyoruz.
        maxFlowMinCut.printCutSet(minCutAmount);
        //

        //Matrisi ekrana yazdiriyoruz.
        int sayac = 0;
        for (Object item : sayilar){
            matris += String.format("%3s", item);
            sayac++;
            if(sayac == muslukSayisi){
                matris += String.format("%n",item);
                sayac=0;
            }
        }
        verilenMatris.setText(matris);
        //

    }

    public void getGraph(ActionEvent event){
        for (int i = 1;i<=muslukSayisi ;i++){
            for (int j = 1;j<=muslukSayisi ;j++){
                System.out.print(graph[i][j]+" ");
            }
            System.out.println();
        }
    }

    public void previousPage(ActionEvent event) throws IOException {
        System.out.println(muslukSayisi+" "+giris+" "+cikis);
        Parent firstPage = FXMLLoader.load(getClass().getResource("sample.fxml"));
        Scene fpage = new Scene(firstPage);

        Stage window2 = (Stage)((Node)event.getSource()).getScene().getWindow();

        window2.setScene(fpage);
        window2.show();
    }

}
class MaxFlowMinCut
{
    private int[] parent;
    private Queue<Integer> sira;
    private int nodeSayisi;
    private boolean[] ugranan;
    private Set<Pair> cutSet;
    private ArrayList<Integer> ulasilan;
    private ArrayList<Integer> ulasilamayan;

    public MaxFlowMinCut (int nodeSayisi)
    {
        this.nodeSayisi = nodeSayisi;
        this.sira = new LinkedList<Integer>();
        parent = new int[nodeSayisi + 1];
        ugranan = new boolean[nodeSayisi + 1];
        cutSet = new HashSet<Pair>();
        ulasilan = new ArrayList<Integer>();
        ulasilamayan = new ArrayList<Integer>();
    }

    public boolean bfs (int giris, int hedef, int graph[][]) // breadth first search algoritmasi
    {
        //alınan graph'ta yolları bu algoritma ile dolaşıyoruz
        boolean bulunanYol = false;
        int cikis, eleman;
        for (int i = 1; i <= nodeSayisi; i++)
        {
            parent[i] = -1;
            ugranan[i] = false;
        }
        sira.add(giris);
        parent[giris] = -1;
        ugranan[giris] = true;

        while (!sira.isEmpty())
        {
            eleman = sira.remove();
            cikis = 1;
            while (cikis <= nodeSayisi)
            {
                if (graph[eleman][cikis] > 0 &&  !ugranan[cikis])
                {
                    parent[cikis] = eleman;
                    sira.add(cikis);
                    ugranan[cikis] = true;
                }
                cikis++;
            }
        }

        if (ugranan[hedef])
        {
            bulunanYol = true;
        }
        return bulunanYol;
    }

    public int  maxFlowMinCut (int graph[][], int giris, int cikis)
    {
        int u, v;
        int maxFlow = 0;
        int akisYolu;
        int[][] residualGraph = new int[nodeSayisi + 1][nodeSayisi + 1];

        for (int girisMuslugu = 1; girisMuslugu <= nodeSayisi; girisMuslugu++)
        {
            for (int cikisMuslugu = 1; cikisMuslugu <= nodeSayisi; cikisMuslugu++)
            {
                residualGraph[girisMuslugu][cikisMuslugu] = graph[girisMuslugu][cikisMuslugu];
            }
        }

        /*max flow*/
        while (bfs(giris, cikis, residualGraph))
        {
            akisYolu = Integer.MAX_VALUE;
            for (v = cikis; v != giris; v = parent[v])
            {
                u = parent[v];
                akisYolu = Math.min(akisYolu,residualGraph[u][v]);
            }
            for (v = cikis; v != giris; v = parent[v])
            {
                u = parent[v];
                residualGraph[u][v] -= akisYolu;
                residualGraph[v][u] += akisYolu;
            }
            maxFlow += akisYolu;
        }

        /*calculate the cut set*/
        for (int i = 1; i <= nodeSayisi; i++)
        {
            if (bfs(giris, i, residualGraph))
            {
                ulasilan.add(i);
            }
            else
            {
                ulasilamayan.add(i);
            }
        }
        for (int i = 0; i < ulasilan.size(); i++)
        {
            for (int j = 0; j < ulasilamayan.size(); j++)
            {
                if (graph[ulasilan.get(i)][ulasilamayan.get(j)] > 0)
                {
                    cutSet.add(new Pair(ulasilan.get(i), ulasilamayan.get(j)));
                }
            }
        }
        return maxFlow;
    }

    public void printCutSet (TextField textField)
    {
        String minCut="";
        Iterator<Pair> iterator = cutSet.iterator();
        while (iterator.hasNext())
        {
            Pair pair = iterator.next();
            System.out.println(pair.giris + "-" + pair.cikis);
            minCut += pair.giris + "-" + pair.cikis+" ";
        }
        textField.setText(minCut);
    }
}

class Pair
{
    public int giris;
    public int cikis;

    public Pair (int giris, int cikis)
    {
        this.giris = giris;
        this.cikis = cikis;
    }

}
