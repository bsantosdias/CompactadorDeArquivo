import java.io.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author bsantosdias
 */

public class Compactador {

    public static void main(String[] args) throws FileNotFoundException, IOException {
        String[][] entrada = LerArquivo();
        String[] textoCompactado = CompactaTexto(entrada);
        CriaTxtCompactado(textoCompactado);
        System.out.println("\nFim de programa.");
    }

    private static String[][] LerArquivo() throws FileNotFoundException, IOException {
        /*Função para ler e guardar arquivo de entrada. A primeira casa do array é o valor do caractere
        e a segunda é a posiçao. Usei esse tipo porque achei que usaria busca binaria, mas não foi necessario e fiquei com
        um codigo enorme.*/
        String[][] texto = new String[1000][2];
        boolean letraAnteriorDiferenteAZ = false;
        int textoPonteiro = 0;
        int contadorPosicao = 0;
        FileReader leEntrada = new FileReader("NaoCompactado.txt");
        Pattern pattern = Pattern.compile("[a-z]", Pattern.CASE_INSENSITIVE); // Padrão para ler só letras. 

        String caractere;
        do {
            caractere = String.valueOf((char) leEntrada.read());
            Matcher matcher = pattern.matcher(caractere); //Verifica se o caractere em questão é uma letra e armazena na matcher. 
            boolean matchFound = matcher.find(); //Se for letra, matchfound recebe a letra da vez. 

            if (!matchFound && contadorPosicao == 0 && texto[0][0] == null) { //Se for primeira rodada e for letra, a mesma será armazenada na primeira casa.
                texto[textoPonteiro][0] = caractere;
                texto[textoPonteiro][1] = String.valueOf(contadorPosicao);
                textoPonteiro++;
                contadorPosicao++;
            } else {
                if (matchFound) { //Se for alguma letra, verificará: 
                    if (letraAnteriorDiferenteAZ) {//Se caractere anterior não era uma letra, pulará para a proxima posição do array. 
                        textoPonteiro++;
                        contadorPosicao++;
                    }
                    if (texto[textoPonteiro][0] == null) { //Se casa atual da array estiver vazia, letra será adicionada a ela sem sobrepor.
                        texto[textoPonteiro][0] = caractere;
                    } else { //Se já possuir algo na casa atual, adicionara a nova letra aos que já existem na casa. 
                        texto[textoPonteiro][0] += caractere;
                    }
                    texto[textoPonteiro][1] = String.valueOf(contadorPosicao);
                    letraAnteriorDiferenteAZ = false;
                } else {//Se não for letra: 
                    textoPonteiro++; //Vai pra casa da frente. 
                    contadorPosicao++; //Vai pra casa da frente. 

                    if (texto[textoPonteiro][0] == null) { //Se casa atual da array estiver vazia, caractere será adicionado a ela sem sobrepor.
                        texto[textoPonteiro][0] = caractere;
                    } else { //Se já possuir algo na casa atual, adicionara o novo caractere aos que já existem. 
                        texto[textoPonteiro][0] += caractere;
                    }
                    texto[textoPonteiro][1] = String.valueOf(contadorPosicao);
                    letraAnteriorDiferenteAZ = true;
                }
            }
        } while (caractere.compareTo("0") != 0); //Leitura rodará enquanto caractere da vez for diferente de zero. 
        leEntrada.close();

        return texto;
    }

    private static String[] CompactaTexto(String[][] entrada) {
        String[] lista = new String[1000]; //Guarda somente palavras.
        String[] textoCompactado = new String[1000]; //Retorno final. 
        int contadorEntrada = 0;
        int contadorLista = 0;
        Pattern pattern = Pattern.compile("[a-z]"); //Verifica se é uma palavra. 

        do {
            Matcher matcher = pattern.matcher(entrada[contadorEntrada][0]); //Verifica se o caractere em questão é uma palavra e armazena na matcher.
            boolean matchFound = matcher.find(); //Se for palavra, matchfound recebe a palavra da vez.

            if (matchFound) { //É uma palavra, então: 
                if (lista[0] == null) {//Se for primeira rodada e for palavra, a mesma será armazenada na primeira casa do texto compactado.
                    textoCompactado[contadorEntrada] = entrada[contadorEntrada][0];
                    lista[0] = entrada[contadorEntrada][0]; //Lista de verificação de repetições recebe palavra. 
                    contadorLista++;
                } else { //Se não estiver vazia: 
                    int retorno = BuscaLinearRec(lista, entrada[contadorEntrada][0], 0); //Chama busca linear, passa a lista como parametro, palavra atual e ponteiro.

                    if (retorno > -1) { //Palavra foi encontrada, então compactado recebe posição atual da palavra +1, para não aparecer um 0 no arquivo. 
                        textoCompactado[contadorEntrada] = String.valueOf(retorno + 1);
                        contadorLista++;
                    } else {/*Palavra não foi achada na lista, então é adicionada ao compactado 
                        e a lista de verificação de repetidos a partir da função OrdenaLista.*/
                        textoCompactado[contadorEntrada] = entrada[contadorEntrada][0];
                        lista = OrdenaLista(lista, textoCompactado[contadorEntrada]);
                    }
                }
            } else {//Se não for palavra, conteudo será adicionado na lista compactada. 
                textoCompactado[contadorEntrada] = entrada[contadorEntrada][0];
            }
            contadorEntrada++;
        } while (entrada[contadorEntrada][0] != null); //Rodará enquanto posição atual não estiver vazia. 

        return textoCompactado;
    }

    private static String[] OrdenaLista(String[] lista, String palavra) {
        /*Coloca palavra nova primeira posição da
        lista de verificação de repetidos. */
        
        String auxiliar = "";
        int contador = 0;

        do {
            if (contador == 0) {
                auxiliar = lista[contador];
                lista[contador] = palavra;
            } else {
                palavra = auxiliar;
                auxiliar = lista[contador];
                lista[contador] = palavra;
            }
            contador++;
        } while (auxiliar != null);

        return lista;
    }

    public static int BuscaLinearRec(String lista[], String palavra, int ponteiro) {
        /*Verifica se palava atual já foi utilizada. 
        Usa lista como parametro, compara com palavra atual e ponteiro para indicar sua posição dentro do array. */

        if (ponteiro >= lista.length || lista[ponteiro] == null) {
            /*Se ponteiro for maior que tamanho maximo da lista ou chegar numa casa que esteja vazia, 
            significa que palavra atual não é repetida */
            return -1;
        }

        if (lista[ponteiro].equals(palavra)) {//Palavra é uma repetida, então retornará ponteiro que é a posição atual dela dentro da lista. 
            return ponteiro;
        } else {
            return BuscaLinearRec(lista, palavra, ponteiro + 1);
        }
    }

    private static void CriaTxtCompactado(String[] textoCompactado) throws IOException {// Escreve texto compactado. 
        FileWriter escreveSaida = new FileWriter("Compactado.txt");
        int contador = 0;

        do {//Escreve palavras uma a uma enquanto houver palavras. 
            escreveSaida.write(textoCompactado[contador]);
            contador++;
        } while (textoCompactado[contador].compareTo("0") != 0);
        escreveSaida.close();
    }
}
