package org.king.projetinho;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

// Através desta classe, podemos nos conectar ao nosso banco de dados relacional através do MySQL.
// A biblioteca que usamos que para fazer essa conexão é a "Connector/j", presente na pasta "bin".
public class DbConnection  {

    /* Link de onde se localiza o banco de dados que queremos nos conectar.
    * No nosso caso, nosso banco de dados é local, por isso usamos 'localhost'.*/
    private static String url = "jdbc:mysql://localhost:3306/banco";

    // Credenciais de acesso ao banco de dados.
    private static String userName = "admin";
    private static String passWord = "1234";

    // Objeto para representar a conexão.
    public static Connection con;

    // Método construtor da nossa classe de conexão.
    /* Nós precisamos "lidar" com o tratamento de erros que podem ser gerados pela biblioteca que usamos.
    * Por isso, utilizamos o método "throws" e informamos qual erro esse método pode gerar.*/
    public DbConnection() throws SQLException, ClassNotFoundException {

        // Aqui testamos para ver se a biblioteca foi instalada e está sendo reconhecida.
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        // Aqui tentamos fazer a conexão com o banco de dados utilizando as nossas credenciais.
        try {
            con = DriverManager.getConnection(url, userName, passWord);
        }catch (SQLException e){
            e.printStackTrace();
        }
    }

    // Esse método serve para obtermos tabelas através de queries em SQL no nosso bando de dados.
    public String[][] stringQuery(String query) throws SQLException {
        Statement statement = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
        ResultSet result = statement.executeQuery(query);

        String[] row = new String[4];

        // Aqui, eu fiz com que o 'cursor' fosse até o último row, e lá embaixo eu volto para o primeiro com o 'beforeFirst'
        // Fiz isso pois não existe um método no 'ResultSet' que retorne o número de linhas, então vamos no último para pegar o número dele
        result.last();
        String[][] table = new String[result.getRow()][];
        result.beforeFirst();

        int counter = 0;
        while (result.next()){
            String dado = "";
            for (int i = 1; i <= 4; i++){
                row[i - 1] = result.getString(i);
            }
            table[counter] = row;
            counter++;
            row = new String[4];
        }
        return table;
    }

    // Esse método nos permite fazer queries sem nos preocuparmos com o retorno.
    public void simpleQuery(String query) throws SQLException{
        Statement statement = con.createStatement();
        statement.executeUpdate(query);
    }

    // Com esse método, podemos conferir se já existe algum animal com tal quarto reservado
    public boolean doesRoomExist(int room) throws SQLException{
        try{
            Statement statement = con.createStatement();
            ResultSet result = statement.executeQuery("SELECT * FROM animal_hotel WHERE room = " + room);
            return result.next();
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }


    // Método de inserção:

    // Esse método adiciona um novo animal ao nosso banco de dados.
    public void registerAnimal(Animal animal) throws SQLException {
        try {
            Statement statement = con.createStatement();

            /* Aqui, eu confiro se o cliente tem um quarto definido.
            * Quando a propriedade 'bookedRoom' de um 'Client' é 0, é considerado indefinido pelo meu algoritmo
            * exatamente como está acontecendo agora */

            // Se não tiver um quarto definido, ele vai para um quarto livre
            // (o banco de dados tem função de autoincrement na coluna 'room').
            if (animal.getBookedRoom() == 0) {
                statement.executeUpdate("INSERT INTO animal_hotel(animal, booked_nights, name) VALUES('"
                        + animal.getSpecie().toLowerCase() + "', "
                        + animal.getBookedNights() + ", '"
                        + animal.getName().toLowerCase() + "' )");
            }
            // Se ele tiver, ele vai para o quarto definido.
            else {
                statement.executeUpdate("INSERT INTO animal_hotel(room, animal, booked_nights, name) VALUES("
                        + animal.getBookedRoom() +", '"
                        + animal.getSpecie().toLowerCase() + "', "
                        + animal.getBookedNights() + ", '"
                        + animal.getName().toLowerCase() + "' )");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    // Métodos de consulta:

    // Consulte um animal pelo quarto que ele reservou.
    public Animal retrieveAnimalByRoom(int room) throws SQLException {
        String[][] table = stringQuery("SELECT * FROM animal_hotel WHERE room = " + room);
        String[] result = table[0];
        return new Animal(result[1], result[2], Integer.parseInt(result[0]), Integer.parseInt(result[3]));
    }
    // Consulte uma lista de animais pelo nome.
    public String[][] retrieveAnimalsByName(String name) throws SQLException {
        return stringQuery("SELECT * FROM animal_hotel WHERE name = '" + name.toLowerCase() + "'");
    }
    // Consulte uma lista de animais pela espécie.
    public String[][] retrieveAnimalsBySpecie(String specie) throws SQLException {
        return stringQuery("SELECT * FROM animal_hotel WHERE animal = '" + specie.toLowerCase() + "'");
    }
    // Consulte uma lista de animais pela quantidade de noites reservadas.
    public String[][] retrieveAnimalsByBookedNights(int bookedNights) throws SQLException {
        return stringQuery("SELECT * FROM animal_hotel WHERE booked_nights = " + bookedNights);
    }
    // Consulte uma lista de animais pela quantidade maior ou menor de noites reservadas.
    public String[][] retrieveAnimalsByLessOrBetterBookedNights(int bookedNights, boolean betterOrLess) throws SQLException {
        String[][] table;
        if (betterOrLess){
            table = stringQuery("SELECT * FROM animal_hotel WHERE booked_nights > " + bookedNights);
        } else {
            table = stringQuery("SELECT * FROM animal_hotel WHERE booked_nights < " + bookedNights);
        }
        return table;
    }


    // Métodos de modificação:

    // Modifica o quarto reservado do animal pelo quarto reservado
    public void modifyAnimalRoomByRoom(int room, int newRoom) throws SQLException {
        simpleQuery("UPDATE animal_hotel SET room = " + newRoom + " WHERE room = " + room);
    }
    // Modifica o nome do animal pelo quarto reservado
    public void modifyAnimalNameByRoom(int room, String name) throws SQLException {
        simpleQuery("UPDATE animal_hotel SET name = '" + name + "' WHERE room = " + room);
    }
    // Modifica a quantidade de noites reservadas do animal pelo quarto reservado
    public void modifyAnimalNightsByRoom(int room, int bookedNights) throws SQLException {
        simpleQuery("UPDATE animal_hotel SET booked_nights = " + bookedNights + " WHERE room = " + room);
    }


    // Método de exclusão:

    // Exclui um animal da lista pelo quarto reservado.
    public void deleteAnimalByRoom(int room) throws SQLException {
        simpleQuery("DELETE FROM animal_hotel WHERE room = " + room);
    }
    // Exclui os animais da lista que tenham tal nome.
    public void deleteAnimalsByName(String name) throws SQLException {
        simpleQuery("DELETE FROM animal_hotel WHERE name = '" + name.toLowerCase() + "'");
    }
    public void deleteAnimalsByName(String name, int limit) throws SQLException {
        simpleQuery("DELETE FROM animal_hotel WHERE name = '" + name.toLowerCase() + "' ORDER BY RAND() LIMIT " + limit);
    }
    // Exclui os animais da lista que sejam de tal espécie.
    public void deleteAnimalsBySpecie(String specie) throws SQLException {
        simpleQuery("DELETE FROM animal_hotel WHERE animal = '" + specie.toLowerCase() + "'");
    }
    public void deleteAnimalsBySpecie(String specie, int limit) throws SQLException {
        simpleQuery("DELETE FROM animal_hotel WHERE animal = '" + specie.toLowerCase() + "' ORDER BY RAND() LIMIT " + limit);
    }
    // Exclui os animais que tenham uma quantidade de noites expecífica
    public void deleteAnimalsByBookedNights(int bookedNights) throws SQLException{
        simpleQuery("DELETE FROM animal_hotel WHERE booked_nights = " + bookedNights);
    }
    public void deleteAnimalsByBookedNights(int bookedNights, int limit) throws SQLException{
        simpleQuery("DELETE FROM animal_hotel WHERE booked_nights = " + bookedNights + " ORDER BY RAND() LIMIT " + limit);
    }
    // Exclui os animais da lista que tenham uma quantidade de noites menor ou maior que tal.
    public void deleteAnimalsByBookedNights(int bookedNights, boolean betterOrLess) throws SQLException {
        if (betterOrLess){
            simpleQuery("DELETE FROM animal_hotel WHERE booked_nights > " + bookedNights);
        } else {
            simpleQuery("DELETE FROM animal_hotel WHERE booked_nights < " + bookedNights);
        }
    }
    public void deleteAnimalsByBookedNights(int bookedNights, boolean betterOrLess, int limit) throws SQLException {
        if (betterOrLess){
            simpleQuery("DELETE FROM animal_hotel WHERE booked_nights > " + bookedNights + " ORDER BY RAND() LIMIT " + limit);
        } else {
            simpleQuery("DELETE FROM animal_hotel WHERE booked_nights < " + bookedNights + " ORDER BY RAND() LIMIT " + limit);
        }
    }
    // Exclui uma quantidade de animais aleatórios do banco de dados
    public void deleteRandomAnimals(int limit) throws SQLException{
        simpleQuery("DELETE FROM animal_hotel ORDER BY RAND() LIMIT " + limit);
    }
    // Exclui TODOS os animais.
    public void purgeAnimals() throws SQLException {
        simpleQuery("DELETE FROM animal_hotel");
    }
    // Enche o banco de dados com dados aleatórios
    public void populate() throws SQLException {
        simpleQuery("INSERT INTO animal_hotel (animal, name, booked_nights)\n" +
                "VALUES\n" +
                "('cachorro', 'bruno', 3),\n" +
                "('gato', 'miau', 5),\n" +
                "('pássaro', 'lara', 2),\n" +
                "('cachorro', 'toby', 4),\n" +
                "('gato', 'whiskers', 7),\n" +
                "('pássaro', 'lucas', 1),\n" +
                "('peixe', 'nemo', 6),\n" +
                "('cachorro', 'bruno', 2),\n" +
                "('gato', 'miau', 3),\n" +
                "('pássaro', 'lara', 4),\n" +
                "('cachorro', 'toby', 5),\n" +
                "('gato', 'whiskers', 6),\n" +
                "('pássaro', 'lucas', 7),\n" +
                "('peixe', 'nemo', 1),\n" +
                "('cachorro', 'bruno', 5),\n" +
                "('gato', 'miau', 2),\n" +
                "('pássaro', 'lara', 3),\n" +
                "('cachorro', 'toby', 7),\n" +
                "('gato', 'whiskers', 4),\n" +
                "('pássaro', 'lucas', 6),\n" +
                "('peixe', 'nemo', 2),\n" +
                "('cachorro', 'bruno', 4),\n" +
                "('gato', 'miau', 6),\n" +
                "('pássaro', 'lara', 1),\n" +
                "('cachorro', 'toby', 3),\n" +
                "('gato', 'whiskers', 5),\n" +
                "('pássaro', 'lucas', 2),\n" +
                "('peixe', 'nemo', 7),\n" +
                "('cachorro', 'bruno', 1),\n" +
                "('gato', 'miau', 4),\n" +
                "('pássaro', 'lara', 5),\n" +
                "('cachorro', 'toby', 6),\n" +
                "('gato', 'whiskers', 3),\n" +
                "('pássaro', 'lucas', 4),\n" +
                "('peixe', 'nemo', 5),\n" +
                "('cachorro', 'bruno', 6),\n" +
                "('gato', 'miau', 1),\n" +
                "('pássaro', 'lara', 7),\n" +
                "('cachorro', 'toby', 2),\n" +
                "('gato', 'whiskers', 4),\n" +
                "('pássaro', 'lucas', 3),\n" +
                "('peixe', 'nemo', 4),\n" +
                "('cachorro', 'bruno', 7),\n" +
                "('gato', 'miau', 5),\n" +
                "('pássaro', 'lara', 6),\n" +
                "('cachorro', 'toby', 1),\n" +
                "('gato', 'whiskers', 2),\n" +
                "('pássaro', 'lucas', 4),\n" +
                "('peixe', 'nemo', 3),\n" +
                "('cachorro', 'bruno', 2),\n" +
                "('gato', 'miau', 6),\n" +
                "('pássaro', 'lara', 1),\n" +
                "('cachorro', 'toby', 7),\n" +
                "('gato', 'whiskers', 4),\n" +
                "('pássaro', 'lucas', 5),\n" +
                "('peixe', 'nemo', 2),\n" +
                "('cachorro', 'bruno', 3),\n" +
                "('gato', 'miau', 5),\n" +
                "('pássaro', 'lara', 7),\n" +
                "('cachorro', 'toby', 1),\n" +
                "('gato', 'whiskers', 6),\n" +
                "('pássaro', 'lucas', 4),\n" +
                "('peixe', 'nemo', 3),\n" +
                "('cachorro', 'bruno', 6),\n" +
                "('gato', 'miau', 2),\n" +
                "('pássaro', 'lara', 5),\n" +
                "('cachorro', 'toby', 4),\n" +
                "('gato', 'whiskers', 3),\n" +
                "('pássaro', 'lucas', 1),\n" +
                "('peixe', 'nemo', 7),\n" +
                "('cachorro', 'bruno', 5),\n" +
                "('gato', 'miau', 3),\n" +
                "('pássaro', 'lara', 6),\n" +
                "('cachorro', 'toby', 2),\n" +
                "('gato', 'whiskers', 4),\n" +
                "('pássaro', 'lucas', 7),\n" +
                "('peixe', 'nemo', 1),\n" +
                "('cachorro', 'bruno', 4),\n" +
                "('gato', 'miau', 1),\n" +
                "('pássaro', 'lara', 3),\n" +
                "('cachorro', 'toby', 5),\n" +
                "('gato', 'whiskers', 6),\n" +
                "('pássaro', 'lucas', 2),\n" +
                "('peixe', 'nemo', 4),\n" +
                "('cachorro', 'bruno', 7),\n" +
                "('gato', 'miau', 5),\n" +
                "('pássaro', 'lara', 4),\n" +
                "('cachorro', 'toby', 6),\n" +
                "('gato', 'whiskers', 3),\n" +
                "('pássaro', 'lucas', 5),\n" +
                "('peixe', 'nemo', 2),\n" +
                "('cachorro', 'bruno', 1),\n" +
                "('gato', 'miau', 4),\n" +
                "('pássaro', 'lara', 7),\n" +
                "('cachorro', 'toby', 3),\n" +
                "('gato', 'whiskers', 2),\n" +
                "('pássaro', 'lucas', 6),\n" +
                "('peixe', 'nemo', 1),\n" +
                "('cachorro', 'bruno', 6),\n" +
                "('gato', 'miau', 1),\n" +
                "('pássaro', 'lara', 5),\n" +
                "('cachorro', 'toby', 7),\n" +
                "('gato', 'whiskers', 4),\n" +
                "('pássaro', 'lucas', 3),\n" +
                "('peixe', 'nemo', 7),\n" +
                "('cachorro', 'bruno', 4),\n" +
                "('gato', 'miau', 6),\n" +
                "('pássaro', 'lara', 2),\n" +
                "('cachorro', 'toby', 5),\n" +
                "('gato', 'whiskers', 1),\n" +
                "('pássaro', 'lucas', 4),\n" +
                "('peixe', 'nemo', 6),\n" +
                "('cachorro', 'bruno', 3),\n" +
                "('gato', 'miau', 5),\n" +
                "('pássaro', 'lara', 7),\n" +
                "('cachorro', 'toby', 2),\n" +
                "('gato', 'whiskers', 4),\n" +
                "('pássaro', 'lucas', 6),\n" +
                "('peixe', 'nemo', 3),\n" +
                "('cachorro', 'bruno', 5),\n" +
                "('gato', 'miau', 4),\n" +
                "('pássaro', 'lara', 1),\n" +
                "('cachorro', 'toby', 7),\n" +
                "('gato', 'whiskers', 6),\n" +
                "('pássaro', 'lucas', 5),\n" +
                "('peixe', 'nemo', 2),\n" +
                "('cachorro', 'bruno', 2),\n" +
                "('gato', 'miau', 3),\n" +
                "('pássaro', 'lara', 6),\n" +
                "('cachorro', 'toby', 1),\n" +
                "('gato', 'whiskers', 5),\n" +
                "('pássaro', 'lucas', 4),\n" +
                "('peixe', 'nemo', 1),\n" +
                "('cachorro', 'bruno', 7),\n" +
                "('gato', 'miau', 2),\n" +
                "('pássaro', 'lara', 5),\n" +
                "('cachorro', 'toby', 4),\n" +
                "('gato', 'whiskers', 3),\n" +
                "('pássaro', 'lucas', 2),\n" +
                "('peixe', 'nemo', 7),\n" +
                "('cachorro', 'bruno', 6),\n" +
                "('gato', 'miau', 1),\n" +
                "('pássaro', 'lara', 4),\n" +
                "('cachorro', 'toby', 5),\n" +
                "('gato', 'whiskers', 6),\n" +
                "('pássaro', 'lucas', 3),\n" +
                "('peixe', 'nemo', 5),\n" +
                "('cachorro', 'bruno', 3),\n" +
                "('gato', 'miau', 5),\n" +
                "('pássaro', 'lara', 2),\n" +
                "('cachorro', 'toby', 4),\n" +
                "('gato', 'whiskers', 1),\n" +
                "('pássaro', 'lucas', 6),\n" +
                "('peixe', 'nemo', 4),\n" +
                "('cachorro', 'bruno', 5),\n" +
                "('gato', 'miau', 3),\n" +
                "('pássaro', 'lara', 7),\n" +
                "('cachorro', 'toby', 2),\n" +
                "('gato', 'whiskers', 4),\n" +
                "('pássaro', 'lucas', 5),\n" +
                "('peixe', 'nemo', 3),\n" +
                "('cachorro', 'bruno', 4),\n" +
                "('gato', 'miau', 6),\n" +
                "('pássaro', 'lara', 1),\n" +
                "('cachorro', 'toby', 7),\n" +
                "('gato', 'whiskers', 3),\n" +
                "('pássaro', 'lucas', 4),\n" +
                "('peixe', 'nemo', 2),\n" +
                "('cachorro', 'bruno', 1),\n" +
                "('gato', 'miau', 4),\n" +
                "('pássaro', 'lara', 6),\n" +
                "('cachorro', 'toby', 3),\n" +
                "('gato', 'whiskers', 2),\n" +
                "('pássaro', 'lucas', 7),\n" +
                "('peixe', 'nemo', 1),\n" +
                "('cachorro', 'bruno', 6),\n" +
                "('gato', 'miau', 2),\n" +
                "('pássaro', 'lara', 5),\n" +
                "('cachorro', 'toby', 4),\n" +
                "('gato', 'whiskers', 3),\n" +
                "('pássaro', 'lucas', 1),\n" +
                "('peixe', 'nemo', 7),\n" +
                "('cachorro', 'bruno', 5),\n" +
                "('gato', 'miau', 3),\n" +
                "('pássaro', 'lara', 6),\n" +
                "('cachorro', 'toby', 2),\n" +
                "('gato', 'whiskers', 4),\n" +
                "('pássaro', 'lucas', 5),\n" +
                "('peixe', 'nemo', 2),\n" +
                "('cachorro', 'bruno', 2),\n" +
                "('gato', 'miau', 1),\n" +
                "('pássaro', 'lara', 4),\n" +
                "('cachorro', 'toby', 6),\n" +
                "('gato', 'whiskers', 5),\n" +
                "('pássaro', 'lucas', 3),\n" +
                "('peixe', 'nemo', 6),\n" +
                "('cachorro', 'bruno', 7),\n" +
                "('gato', 'miau', 5),\n" +
                "('pássaro', 'lara', 2),\n" +
                "('cachorro', 'toby', 4),\n" +
                "('gato', 'whiskers', 6),\n" +
                "('pássaro', 'lucas', 1),\n" +
                "('peixe', 'nemo', 5),\n" +
                "('cachorro', 'bruno', 3),\n" +
                "('gato', 'miau', 6),\n" +
                "('pássaro', 'lara', 7),\n" +
                "('cachorro', 'toby', 2),\n" +
                "('gato', 'whiskers', 4),\n" +
                "('pássaro', 'lucas', 3),\n" +
                "('peixe', 'nemo', 1),\n" +
                "('cachorro', 'bruno', 5),\n" +
                "('gato', 'miau', 4),\n" +
                "('pássaro', 'lara', 6),\n" +
                "('cachorro', 'toby', 1),\n" +
                "('gato', 'whiskers', 5),\n" +
                "('pássaro', 'lucas', 2),\n" +
                "('peixe', 'nemo', 7),\n" +
                "('cachorro', 'bruno', 4),\n" +
                "('gato', 'miau', 2),\n" +
                "('pássaro', 'lara', 5),\n" +
                "('cachorro', 'toby', 7),\n" +
                "('gato', 'whiskers', 3),\n" +
                "('pássaro', 'lara', 4),\n" +
                "('peixe', 'nemo', 2),\n" +
                "('cachorro', 'bruno', 6),\n" +
                "('gato', 'miau', 1),\n" +
                "('pássaro', 'lara', 3),\n" +
                "('cachorro', 'toby', 5),\n" +
                "('gato', 'whiskers', 6),\n" +
                "('pássaro', 'lara', 7),\n" +
                "('peixe', 'nemo', 1),\n" +
                "('cachorro', 'bruno', 3),\n" +
                "('gato', 'miau', 5),\n" +
                "('pássaro', 'lara', 2),\n" +
                "('cachorro', 'toby', 4),\n" +
                "('gato', 'whiskers', 1),\n" +
                "('pássaro', 'lara', 6),\n" +
                "('peixe', 'nemo', 4),\n" +
                "('cachorro', 'bruno', 5),\n" +
                "('gato', 'miau', 3),\n" +
                "('pássaro', 'lara', 7),\n" +
                "('cachorro', 'toby', 2),\n" +
                "('gato', 'whiskers', 4),\n" +
                "('pássaro', 'lara', 5),\n" +
                "('peixe', 'nemo', 3),\n" +
                "('cachorro', 'bruno', 4),\n" +
                "('gato', 'miau', 6),\n" +
                "('pássaro', 'lara', 1),\n" +
                "('cachorro', 'toby', 7),\n" +
                "('gato', 'whiskers', 3);");
    }
}
