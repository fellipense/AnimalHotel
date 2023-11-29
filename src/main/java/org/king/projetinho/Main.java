package org.king.projetinho;


import java.sql.SQLException;
import java.util.Scanner;

public class Main {
    static Display display; // Com "display", facilmente inserimos menus na tela.
    static DbConnection con; // "con" irá carregar nossa conexão com o banco de dados.
    // Com ele, poderemos usar vários métodos úteis diretamente no banco de dados.

    // Método principal do nosso programa.
    public static void main(String[] args) throws SQLException, ClassNotFoundException {

        // Conforme o GPT-4, esses dois comandos limpam o terminal do Windows.
        System.out.print("\033[H\033[2J");
        System.out.flush();

        // Aqui instanciamos um objeto para cada variável.
        display = new Display();
        con = new DbConnection();
        /* O motivo de criarmos as variáveis fora do método "main" depois instanciarmos seu objeto dentro do método,
        * é por que queremos usar esses dois objetos em qualquer lugar da classe. Para isso a gente tem que declarar
        * as variáveis do lado de fora, no escopo geral para que todos possam acessar. As variáveis não podem ficar
        * vazias, mas não podem ser manipuladas fora de um método, por isso, "enchemos" ela logo que o programa começa
        * a executar, pois ele lê o método main primeiro, garantindo que as duas variáveis possuam valor em tempo de
        * execução. */

        mainMenu(); // Chamando o menu principal do nosso programa.
        // Perceba que, depois que terminar a execução, o programa acaba no geral.
    }
    private static void mainMenu() throws SQLException {
        Scanner s = new Scanner(System.in);
        display.mainMenu();
        String mainAnswer = s.nextLine();
        String[] finalAnswer = argsSplitter(mainAnswer);

        if (finalAnswer.length == 0) {
            customWindow("Você precisa inserir, no mínimo, 1 valor");
            mainMenu();
            return;
        }

        switch (finalAnswer[0]) {
            // Opção 1 mostra o menu de registro.
            case "1" -> registerMenu();
            // Opção 2 mostra o menu de consulta.
            case "2" -> retrieveMenu();
            // Opção 3 mostra o menu de modificação.
            case "3" -> modifyMenu();
            // Opção 4 mostra o menu de remoção.
            case "4" -> deleteMenu();
            // Opção 5 encerra o programa.
            case "5" -> { return; }
            // Opção 6 é oculta, serve para encher o banco de dados com novos dados.
            case "6" -> con.populate();
            // Opção 7 é oculta e serve para mostrar a tabela inteira.
            case "7" -> display.customWindow(display.stringTable(con.stringQuery("SELECT * FROM animal_hotel")));
            // Caso o usuário não digite nenhuma das opções válidas:
            default -> {
                customWindow(
                    "O valor inserido como resposta não atende a nenhuma das opções. " +
                    "Por favor, Certifique-se de que inseriu o valor certo.");
                s.nextLine();
                mainMenu();
                return;
            }
        }
        mainMenu();
    }
    private static void registerMenu() throws SQLException {
        Scanner s = new Scanner(System.in);
        display.animalRegisterMenu();
        String mainAnswer = s.nextLine();
        String[] finalAnswer = argsSplitter(mainAnswer);

        if (finalAnswer[0].equals("v")) { mainMenu(); return; }
        if (finalAnswer.length < 3) {
            customWindow("Você precisa inserir, no mínimo, 3 valores");
            registerMenu();
            return;
        }

        try {
            if (finalAnswer.length >= 4){
                Integer.parseInt(finalAnswer[0]);
                Integer.parseInt(finalAnswer[3]);
            } else {
                Integer.parseInt(finalAnswer[2]);
            }
        } catch (Exception e){
            customWindow("Os valores não foram inseridos da forma correta! (<número>, <texto>, <texto>, <número>)");
            registerMenu();
            return;
        }

        Animal animal;

        if (finalAnswer.length >= 4 && !finalAnswer[0].equals("0")) {
            String specie = finalAnswer[1].replace("_", " ");
            String nome = finalAnswer[2].replace("_", " ");
            int bookedRoom = Integer.parseInt(finalAnswer[0]);
            if (bookedRoom <= 0) {
                customWindow("Nós não temos nenhum quarto 0 ou menor que zero. Isso nem sequer faria sentido...");
                registerMenu();
                return;
            }
            int bookedNights = Integer.parseInt(finalAnswer[3]);
            if (bookedNights <= 0) {
                customWindow("O animal precisa ter pelo menos 1 noite reservada!");
                registerMenu();
                return;
            }
            if (con.doesRoomExist(bookedRoom)) {
                customWindow("Já existe um animal no quarto " + Integer.parseInt(finalAnswer[0]) + ", escolha outro lugar!");
                registerMenu();
                return;
            }
            animal = new Animal(specie, nome, bookedRoom, bookedNights);
        } else {
            int recoil = 0;
            if (finalAnswer[0].equals("0")) { recoil = 1; }
            String specie = finalAnswer[recoil].replace("_", " ");
            String nome = finalAnswer[1 + recoil].replace("_", " ");
            int bookedNights = Integer.parseInt(finalAnswer[2 + recoil]);
            animal = new Animal(specie, nome, 0,bookedNights);
        }
        con.registerAnimal(animal);
        customWindow(animal.getSpecie() + " " + animal.getName() + " foi registrado(a)!");
        registerMenu();
    }
    private static void retrieveMenu() throws SQLException {
        Scanner s = new Scanner(System.in);
        display.animalRetrieveMenu();
        String mainAnswer = s.nextLine();
        String[] finalAnswer = argsSplitter(mainAnswer);


        if (finalAnswer[0].equals("v")) { mainMenu(); return; }
        if (finalAnswer.length < 2) {
            customWindow("Você precisa inserir, no mínimo, 2 valores.");
            retrieveMenu();
            return;
        }

        String[][] resultTable;
        switch (finalAnswer[0]) {
            case "1" -> {
                try {
                    Integer.parseInt(finalAnswer[1]);
                } catch (Exception e) {
                    customWindow("O valor inserido como número do quarto deve ser um valor numérico");
                    retrieveMenu();
                    return;
                }
                int room = Integer.parseInt(finalAnswer[1]);
                if (!con.doesRoomExist(room)) {
                    customWindow("Não achamos nenhum animal que tenha reservado este quarto :(");
                    retrieveMenu();
                    return;
                } else {
                    Animal animal = con.retrieveAnimalByRoom(room);
                    customWindow("No quarto " + room + " está hospedado um(a) " +
                            animal.getSpecie() + " chamado(a): '" +
                            animal.getName() + "'. \n" +
                            "Este animal reservou " + animal.getBookedNights() + " noite(s).");
                }
            }
            case "2" -> {
                String name = finalAnswer[1].replace("_", " ");
                resultTable = con.retrieveAnimalsByName(name);
                if (resultTable.length > 0) {
                    retrieveResultMenu(resultTable);
                } else {
                    customWindow("Nenhum animal com o nome '" + name + "' está hospedado neste hotel :(");
                }
            }
            case "3" -> {
                String specie = finalAnswer[1].replace("_", " ");
                resultTable = con.retrieveAnimalsBySpecie(specie);
                if (resultTable.length > 0) {
                    retrieveResultMenu(resultTable);
                } else {
                    customWindow("Nenhum(a) '" + specie + "' está hospedado neste hotel :(");
                }
            }
            case "4" -> {
                String value;
                String operator = "";
                if (finalAnswer.length > 2) {
                    value = finalAnswer[2];
                    operator = finalAnswer[1];
                } else {
                    value = finalAnswer[1];
                }
                try {
                    Integer.parseInt(value);
                } catch (Exception e) {
                    customWindow("O valor inserido como número de noites reservadas deve ser um valor numérico");
                    retrieveMenu();
                    return;
                }
                if (!"<>".contains(operator)) {
                    customWindow("Operador '" + operator + "' não identificado! Aceitamos apenas os operadores '<' e '>'.");
                    retrieveMenu();
                    return;
                }
                int bookedNights;
                String[][] finalResult;
                if (operator.equals("")) {
                    bookedNights = Integer.parseInt(finalAnswer[1]);
                    finalResult = con.retrieveAnimalsByBookedNights(bookedNights);
                } else {
                    bookedNights = Integer.parseInt(finalAnswer[2]);
                    if (operator.equals("<")) {
                        finalResult = con.retrieveAnimalsByLessOrBetterBookedNights(bookedNights, false);
                    } else {
                        finalResult = con.retrieveAnimalsByLessOrBetterBookedNights(bookedNights, true);
                    }
                }
                if (finalResult.length > 0) {
                    retrieveResultMenu(finalResult);
                } else {
                    customWindow("Não foi achado nenhum animal que reservou um quarto com essa quantidade de noites :(");
                }
            }
            default -> {
                customWindow(
                        "O valor inserido como resposta não atende a nenhuma das opções. " +
                        "Por favor, Certifique-se de que inseriu o valor certo.");
                s.nextLine();
                retrieveMenu();}
        }
        retrieveMenu();
    }
    private static void modifyMenu() throws SQLException {
        Scanner s = new Scanner(System.in);
        display.animalModifyMenu();
        String mainAnswer = s.nextLine();
        String[] finalAnswer = argsSplitter(mainAnswer);


        if (finalAnswer[0].equals("v")) { mainMenu(); return; }

        int room;
        try {
            Integer.parseInt(finalAnswer[0]);
        } catch (Exception e) {
            customWindow("Você precisa inserir um valor numérico como número do quarto!");
            modifyMenu();
            return;
        }
        room = Integer.parseInt(finalAnswer[0]);

        if (!con.doesRoomExist(room)){
            customWindow("Não tem nenhum animal neste quarto :(");
        }

        if (finalAnswer.length < 2) {
            customWindow("Você precisa inserir 3 valores.");
            modifyMenu();
            return;
        }

        switch (finalAnswer[1]){
            case "quarto", "room", "booked_room" -> {
                int newRoom;
                try {
                    Integer.parseInt(finalAnswer[2]);
                }catch (Exception e){
                    customWindow("O valor do novo quarto precisa ser outro valor numérico!");
                    modifyMenu();
                    return;
                }
                newRoom = Integer.parseInt(finalAnswer[2]);

                if (newRoom <= 0) {
                    customWindow("Nós não temos nenhum quarto 0 ou menor que zero. Isso nem sequer faria sentido...");
                    modifyMenu();
                    return;
                }

                if (!con.doesRoomExist(newRoom)){
                    con.modifyAnimalRoomByRoom(room, newRoom);
                    customWindow("O animal do quarto " + room + " foi transferido para o quarto " + newRoom + " com sucesso!");
                } else {
                    customWindow("Opa! O quarto " + newRoom + " já está sendo usado.");
                }
            }
            case "nome", "name" -> {
                con.modifyAnimalNameByRoom(room, finalAnswer[2].replace("_", " "));
                customWindow("O animal do quarto " + room + " agora se chama " + finalAnswer[2].replace("_", " ") + ".");
            }
            case "noites", "noite", "night", "nights", "booked_nights" -> {
                int newNights;
                try {
                    Integer.parseInt(finalAnswer[2]);
                } catch (Exception e){
                    customWindow("A quantidade de noites precisa ser um valor numérico!");
                    modifyMenu();
                    return;
                }
                newNights = Integer.parseInt(finalAnswer[2]);

                if (newNights <= 0) {
                    customWindow("O animal precisa ter pelo menos 1 noite reservada!");
                    modifyMenu();
                    return;
                }

                con.modifyAnimalNightsByRoom(room, newNights);
                customWindow("O animal do quarto " + room + " agora tem " + finalAnswer[2] + " noite(s) reservada(s).");
            }
            default -> {
                customWindow("Você só pode escolher entre as opções: 'Quarto', 'Nome' e 'Noites'.");
                modifyMenu();
                return;
            }
        }
        modifyMenu();
    }
    private static void deleteMenu() throws SQLException {
        Scanner s = new Scanner(System.in);
        display.animalDeleteMenu();
        String mainAnswer = s.nextLine();
        String[] finalAnswer = argsSplitter(mainAnswer);


        if (finalAnswer[0].equals("v")) { mainMenu(); return; }
        if (finalAnswer.length < 2) {
            customWindow("Você precisa inserir pelo menos 3 valores!");
        }

        switch (finalAnswer[0]){
            case "1" -> {
                int room;
                try {
                    Integer.parseInt(finalAnswer[1]);
                } catch (Exception e) {
                    customWindow("O valor do quarto precisa ser um valor numérico!");
                    deleteMenu();
                    return;
                }
                room = Integer.parseInt(finalAnswer[1]);

                if (con.doesRoomExist(room)){
                    if (confirmMenu("Você tem certeza que deseja expulsar o animal do quarto " + room + "? (y)")){
                        con.deleteAnimalByRoom(room);
                        customWindow("Animal do quarto " + room + " foi expulso...");
                    }
                    deleteMenu();
                    return;
                } else {
                    customWindow("Já não existe nenhum animal neste quarto.");
                    deleteMenu();
                    return;
                }
            }
            case "2" -> {
                String name = finalAnswer[1];
                String[][] target = con.retrieveAnimalsByName(name);

                if (target.length == 0) {
                    customWindow("Já não havia nenhum animal com o nome " + name + "...");
                    deleteMenu();
                    return;
                } else if (target.length == 1) {
                    if (confirmMenu("Apenas o(a) " + target[0][1] + " chamado(a) " + target[0][2] + " será expulso. Tem certeza? (y)")){
                        con.deleteAnimalsByName(name);
                        customWindow(name + " foi expulso do hotel...");
                    }
                    deleteMenu();
                    return;
                } else {
                    int limit;
                    if (finalAnswer.length > 2){
                        try {
                            limit = Integer.parseInt(finalAnswer[2]);
                        } catch (Exception e) {
                            limit = 0;
                        }
                    } else { limit = 0; }

                    if (limit == 0) {
                        if (confirmMenu("Você tem certeza que deseja expulsar todos esses animais? (y) \n\n" +
                                display.stringTable(target))){
                            con.deleteAnimalsByName(name);
                            customWindow("Todos os animais chamados " + name + " foram expulsos com sucesso...");
                        } else {
                            deleteMenu();
                            return;
                        }
                    } else {
                        if (confirmMenu("Você tem certeza que deseja expulsar " + limit + " animais chamados " + name + "? (y)")){
                            con.deleteAnimalsByName(name, limit);
                            customWindow(limit + " animais chamados " + name + " foram expulsos...");
                        }
                    }
                }
            }
            case "3" -> {
                String specie = finalAnswer[1];
                String[][] target = con.retrieveAnimalsBySpecie(specie);

                if (target.length == 0) {
                    customWindow("Já não havia nenhum(a) " + specie + "...");
                    deleteMenu();
                    return;
                } else if (target.length == 1) {
                    if (confirmMenu("Apenas o(a) " + target[0][1] + " chamado(a) " + target[0][2] + " será expulso. Tem certeza? (y)")){
                        con.deleteAnimalsBySpecie(specie);
                        customWindow("O(a) único(a) " + specie + " do hotel foi expulso...");
                    }
                    deleteMenu();
                    return;
                } else {
                    int limit;
                    if (finalAnswer.length > 2){
                        try {
                            limit = Integer.parseInt(finalAnswer[2]);
                        } catch (Exception e) {
                            limit = 0;
                        }
                    } else { limit = 0; }

                    if (limit == 0) {
                        if (confirmMenu("Você tem certeza que deseja expulsar todos esses animais? (y) \n\n" +
                                display.stringTable(target))){
                            con.deleteAnimalsBySpecie(specie);
                            customWindow("Todos os animais chamados " + specie + " foram expulsos com sucesso...");
                        } else {
                            deleteMenu();
                            return;
                        }
                    } else {
                        if (confirmMenu("Você tem certeza que deseja expulsar " + limit + " animais chamados " + specie + "? (y)")){
                            con.deleteAnimalsBySpecie(specie, limit);
                            customWindow(limit + " " + specie + "(s) foram expulsos...");
                        }
                    }
                }
            }
            case "4" -> {
                int amount;
                try {
                    Integer.parseInt(finalAnswer[1]);
                } catch (Exception e) {
                    customWindow("Informe um valor numérico");
                    deleteMenu();
                    return;
                }
                amount = Integer.parseInt(finalAnswer[1]);

                if (amount < 0) {
                    customWindow("A quantidade precisa ser maior ou igual a 0");
                    deleteMenu();
                    return;
                } else if (amount == 0){
                    if (confirmMenu("Você tem certeza que deseja expulsar todos os animais do hotel? (y)")){
                        con.purgeAnimals();
                        customWindow("Todos os animais foram expulsos...");
                    }
                    registerMenu();
                    return;
                } else {
                    if (confirmMenu("Você tem certeza que deseja expulsar " + amount + " animais aleatórios? (y)")){
                        con.deleteRandomAnimals(amount);
                        customWindow(amount + " animais foram expulsos...");
                    }
                    deleteMenu();
                    return;
                }
            }
        }
    }
    private static boolean confirmMenu(String message){
        Scanner s = new Scanner(System.in);
        display.customWindow(message);
        String[] answer = argsSplitter(s.nextLine());
        return answer[0].equals("y");
    }
    private static void retrieveResultMenu(String[][] table){
        Scanner s = new Scanner(System.in);
        display.animalRetrieveResultMenu(table);
        s.nextLine();
    }
    private static void customWindow(String message){
        Scanner s = new Scanner(System.in);
        display.customWindow(message);
        s.nextLine();
    }
    private static String[] argsSplitter(String args){
        args = args.toLowerCase().trim().replace(",", "");
        return args.split(" ");
    }
}