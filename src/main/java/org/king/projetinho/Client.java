package org.king.projetinho;

// Abstração
// A classe abstrata 'Client' representa o conceito abstrato de um cliente de hotel.
abstract class Client {

    // Encapsulamento
    // Algumas propriedades do cliente são privadas, acessíveis apenas através dos métodos get e set.
    private static int bookedRoom;
    private static int bookedNights;

    public int getBookedRoom(){
        return bookedRoom;
    }
    public int getBookedNights(){
        return bookedNights;
    }
    public void changeBookedRoom(int newRoom){
        bookedRoom = newRoom;
    }
    public void changeBookedNights(int newBookedNights){
        bookedNights = newBookedNights;
    }
    public abstract void checkin();
}
