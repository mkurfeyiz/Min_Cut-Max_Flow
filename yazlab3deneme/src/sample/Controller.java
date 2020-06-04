package sample;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;


public class Controller implements Initializable {
    @FXML private TextField muslukSayisi;
    @FXML private TextField matris;
    @FXML private TextField girisMuslugu;
    @FXML private TextField cikisMuslugu;

    int[][] graph;
    int giris=0,cikis=0,nodeSayisi=0;
    ArrayList<String> sayilar;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    public void setMuslukSayisi(){
        System.out.println(muslukSayisi.getText());
        nodeSayisi = Integer.parseInt(muslukSayisi.getText());
        graph = new int[nodeSayisi + 1][nodeSayisi + 1];
        sayilar = new ArrayList<>(nodeSayisi);
    }

    public void splitText(String matris){
        if(!sayilar.isEmpty()){
            sayilar.clear();
        }
        String[] parse = matris.split(" ");
        for (int i = 0;i<parse.length;i++){
            //System.out.println(parse[i]);
            sayilar.add(parse[i]);
        }
    }

    public void setMatris(){
        int index = 0;
        splitText(matris.getText());
        for (int i = 1; i <= nodeSayisi; i++)
        {
            for (int j = 1; j <= nodeSayisi ; j++)
            {
                graph[i][j] = Integer.parseInt(sayilar.get(index));
                index++;
            }
        }
    }

    public void setGirisMuslugu(){
        //Mantiken giris muslugu musluk sayisindan fazla olamaz.Istersen onu kontrol et.Cok da gerekli degil.O zaman ne tatava yaptın aq.
        giris = Integer.parseInt(girisMuslugu.getText());
        System.out.println(giris);
    }

    public void setCikisMuslugu(){
        cikis = Integer.parseInt(cikisMuslugu.getText());
        System.out.println(cikis);
        for (int i = 1;i<=nodeSayisi ;i++){
            for (int j = 1;j<=nodeSayisi ;j++){
                System.out.print(graph[i][j]+" ");
            }
            System.out.println();
        }
    }

    public void graphPage(ActionEvent event) throws IOException {
        //Burada boyle yazmamizin sebebi diger controllera erismek icin loaderi kullanmamiz gerekiyor.
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("graphPage.fxml"));
        Parent graphPage = loader.load();
        Scene graphScene = new Scene(graphPage);
        //Asagida gecis yapmak istedigimiz sayfanin controllerinin methodunu kullanarak bilgileri aktardik.
        GraphController controller = loader.getController();
        controller.setGraph(nodeSayisi,graph,giris,cikis,sayilar);
        //
        Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
        window.setScene(graphScene);
        window.show();
    }
}