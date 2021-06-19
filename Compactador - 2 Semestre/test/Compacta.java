/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 *
 * @author bsantosdias
 */

public class Compacta {

    private String[] txtArray;

    public Compacta() {
    }

    @BeforeAll
    public static void setUpClass() {
    }

    @AfterAll
    public static void tearDownClass() {
    }

    @BeforeEach
    public void setUp() {
    }

    @AfterEach
    public void tearDown() {
    }

    public void criarArquivoDescompactado(String nomeArquivo) throws FileNotFoundException {
        txtArray = new String[6];
        txtArray[0] = ("Dear Sally");
        txtArray[1] = ("Please, please do it--it would please");
        txtArray[2] = ("Mary very, very much. And Mary would");
        txtArray[3] = ("do everything in Mary's power to make");
        txtArray[4] = ("it pay off for you.");
        txtArray[5] = ("-- Thank you very much --");
        txtArray[6] = ("0");

        try ( PrintWriter pw = new PrintWriter(nomeArquivo)) {
            for (String txt : txtArray) {
                pw.println(txt);
            }
        }
    }
}
