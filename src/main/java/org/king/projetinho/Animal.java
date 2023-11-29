package org.king.projetinho;

// Herança
// A classe 'Animal' é uma subclasse de 'Client', herda todos os seus atributos e métodos.
// Afinal, por mais que os animais sejam diferentes, todos são clientes.
public class Animal extends Client {

    // Encapsulamento
    // Algumas propriedades do cliente são privadas, acessíveis apenas através dos métodos get e set.
    private String nome;
    private String specie;

    public String getName(){
        return nome;
    }
    public String getSpecie(){
        return specie;
    }
    public Animal(String specie, String nome){
        this.nome = nome;
        this.specie = specie;
        changeBookedRoom(0);
        changeBookedNights(1);
    }
    public Animal(String specie, String nome, int bookedRoom){
        this.nome = nome;
        this.specie = specie;
        changeBookedRoom(bookedRoom);
        changeBookedNights(1);
    }
    public Animal(String specie, String nome, int bookedRoom, int bookedNights){
        this.nome = nome;
        this.specie = specie;
        changeBookedRoom(bookedRoom);
        changeBookedNights(bookedNights);
    }

    // Polimorfismo
    // O método 'checkin' é sobrescrito para fornecer a implementação específica para 'Animal'.
    // Cada classe que herde 'Client' pode ter seu método 'checkin' funcionando totalmente diferente.
    @Override
    public void checkin(){
        System.out.println("Um(a) " + specie + " chamado(a) '" + nome + "' acaba de fazer checkin no hotel!");
    }
}
