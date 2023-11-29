package org.king.projetinho;

import java.util.Arrays;
public class Display {
    public void mainMenu(){
        System.out.print("\033[H\033[2J");
        System.out.flush();
        System.out.println("-----------------------------------------------------------");
        System.out.println("\n");
        System.out.println("===========================================================");
        System.out.println("                    HOTEL DE ANIMAIS                       ");
        System.out.println("===========================================================");
        System.out.println("\n");
        System.out.println("Seja bem vindo(a) ao Hotel de Animais, como posso ajudá-lo?");
        System.out.println("\n");
        System.out.println("1 - Cadastrar um animal;");
        System.out.println("2 - Consultar animais;");
        System.out.println("3 - Modificar um animal;");
        System.out.println("4 - Deletar animais;");
        System.out.println("5 - Encerrar.");
        System.out.println("\n");
        System.out.println("-----------------------------------------------------------");
    }
    public void animalRegisterMenu(){
        System.out.print("\033[H\033[2J");
        System.out.flush();
        System.out.println("-----------------------------------------------------------");
        System.out.println("\n");
        System.out.println("===========================================================");
        System.out.println("                    HOTEL DE ANIMAIS                       ");
        System.out.println("===========================================================");
        System.out.println("\n");
        System.out.println("Escreva abaixo os dados do animal que deseja registrar, seguindo a seguinte ordem: " +
                "'<Quarto>, <Espécie do animal>, <Nome do animal>, <Quantidade de noites>'.");
        System.out.println("\n");
        System.out.println("Tenha em mente que os valores digitados devem ser, respectivamente: Número, texto, texto, número.");
        System.out.println("Coloque 0 ou nada (espaço em branco) no valor 'Quarto' caso não tenha preferência alguma.");
        System.out.println("Caso o nome ou espécie do animal possua mais de uma palavra, coloque-o separado por underlines, por favor.");
        System.out.println("Exemplo: 'Cavalo Marinho -> Cavalo_Marinho'");
        System.out.println("Todo texto será inserido no banco de dados com caracteres minúsculos, independente de como insira-os agora.");
        System.out.println("\n");
        System.out.println("Digite 'V' para voltar");
        System.out.println("\n");
        System.out.println("-----------------------------------------------------------");
    }
    public void animalRetrieveMenu(){
        System.out.print("\033[H\033[2J");
        System.out.flush();
        System.out.println("-----------------------------------------------------------");
        System.out.println("\n");
        System.out.println("===========================================================");
        System.out.println("                    HOTEL DE ANIMAIS                       ");
        System.out.println("===========================================================");
        System.out.println("\n");
        System.out.println("1 - Consultar animal de um quarto;");
        System.out.println("2 - Consultar animais com um determinado nome;");
        System.out.println("3 - Consultar animais de uma espécie;");
        System.out.println("4 - Consultar animais pela quantidade de noites reservadas.");
        System.out.println("\n");
        System.out.println("Responda digitando a opção e o parâmetro, exemplo: '1 303', '2 Rafael', '3 Gato', '4 2'");
        System.out.println("Use apenas o singular, não será aceito o uso de parâmetros como: 'Cães', 'Camaleões', etc.");
        System.out.println("Ao consultar pela quantidade de noites reservadas, você pode consultar quem reservou uma quantidade de noites menor ou maior que tal quantidade." +
                "É só responder de maneira parecida à essas: '4 > 3' (quem reservou mais de 3 noites), '4 < 5' (menos de 5 noites). Não serão aceitos '<=' nem '>='.");
        System.out.println("Caso o nome ou espécie do animal possua mais de uma palavra, coloque-o separado por underlines, por favor.");
        System.out.println("Exemplo: 'Cavalo Marinho -> Cavalo_Marinho'");
        System.out.println("Todo texto será inserido no banco de dados com caracteres minúsculos, independente de como insira-os agora.");
        System.out.println("\n");
        System.out.println("Digite 'V' para voltar");
        System.out.println("\n");
        System.out.println("-----------------------------------------------------------");
    }
    public void animalRetrieveResultMenu(String[][] table){
        System.out.print("\033[H\033[2J");
        System.out.flush();
        System.out.println("-----------------------------------------------------------");
        System.out.println("\n");
        System.out.println("===========================================================");
        System.out.println("                    HOTEL DE ANIMAIS                       ");
        System.out.println("===========================================================");
        System.out.println("\n");
        System.out.println("Foi isso que encontramos:");
        System.out.println("\n");
        System.out.println(stringTable(table));
        System.out.println("\n");
        System.out.println("Digite qualquer coisa para voltar.");
        System.out.println("\n");
        System.out.println("-----------------------------------------------------------");
    }
    public void animalModifyMenu(){
        System.out.print("\033[H\033[2J");
        System.out.flush();
        System.out.println("-----------------------------------------------------------");
        System.out.println("\n");
        System.out.println("===========================================================");
        System.out.println("                    HOTEL DE ANIMAIS                       ");
        System.out.println("===========================================================");
        System.out.println("\n");
        System.out.println("Quer modificar os dados de um animal? O procedimento é simples, você deve responder da seguinte maneira: " +
                "<Número do quarto do animal>, <'Nome', 'Noites' ou 'Quarto'>, <Novo valor>.");
        System.out.println("Exemplo: 102, Nome, Roberto_Júnior");
        System.out.println("Você quer modificar a espécie de um animal? Isso é cientificamente impossível ainda.");
        System.out.println("Caso o nome  do animal possua mais de uma palavra, coloque-o separado por underlines, por favor.");
        System.out.println("Exemplo: 'Pedro_Ricardo -> Pedro_Ricardo'");
        System.out.println("Todo texto será inserido no banco de dados com caracteres minúsculos, independente de como insira-os agora.");
        System.out.println("\n");
        System.out.println("Digite 'V' para voltar");
        System.out.println("\n");
        System.out.println("-----------------------------------------------------------");
    }
    public void animalDeleteMenu(){
        System.out.print("\033[H\033[2J");
        System.out.flush();
        System.out.println("-----------------------------------------------------------");
        System.out.println("\n");
        System.out.println("===========================================================");
        System.out.println("                    HOTEL DE ANIMAIS                       ");
        System.out.println("===========================================================");
        System.out.println("\n");
        System.out.println("Quer remover algum ou alguns animais?");
        System.out.println("\n");
        System.out.println("1 - Expulsar animal de um quarto;");
        System.out.println("2 - Expulsar animais com um determinado nome;");
        System.out.println("3 - Expulsar animais de uma determinada espécie;");
        System.out.println("4 - Expulsar uma quantidade específica de animais aleatórios ou todos.");
        System.out.println("\n");
        System.out.println("Responda digitando a opção, parâmetro e quantos quer excluir, exemplo: '1 303', '2 Rafael 5', '3 Gato 11'");
        System.out.println("Use apenas o singular, não será aceito o uso de parâmetros como: 'Cães', 'Camaleões', etc.");
        System.out.println("No momento de informar quantos animais deseja deletar, tenha em mente que todos no filtro serão deletados caso você digite 0 ou nada (espaço em branco)." +
                "Se você informar uma quantidade maior do que fora achado, não tem problema, deletaremos todos.");
        System.out.println("\n");
        System.out.println("Digite 'V' para voltar");
        System.out.println("\n");
        System.out.println("-----------------------------------------------------------");
    }
    public void customWindow(String message){
        System.out.print("\033[H\033[2J");
        System.out.flush();
        System.out.println("-----------------------------------------------------------");
        System.out.println("\n");
        System.out.println("===========================================================");
        System.out.println("                    HOTEL DE ANIMAIS                       ");
        System.out.println("===========================================================");
        System.out.println("\n");
        System.out.println(message);
        System.out.println("\n");
        System.out.println("Digite qualquer coisa para voltar.");
        System.out.println("\n");
        System.out.println("-----------------------------------------------------------");
    }

    public String stringTable(String[][] table){
        String resultTable = "";
        for (int i = 1 ; i <= table.length; i++){
            resultTable += Arrays.toString(table[i - 1]) + "\n";
        }
        return resultTable;
    }
}
