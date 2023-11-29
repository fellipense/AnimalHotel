package org.king.projetinho;

// Herança
// A classe 'Human' é uma subclasse de 'Client', herda todos os seus atributos e métodos.
public class Human extends Client{

    // Encapsulamento
    // Algumas propriedades do cliente são privadas, acessíveis apenas através dos métodos get e set.
    private static String nome;

    public Human(String nome){
        this.nome = nome;
    }

    public String getName(){
        return nome;
    }

    // Polimorfismo
    // O método 'checkin' é sobrescrito para fornecer a implementação específica para 'Human'.
    // Cada classe que herde 'Client' pode ter seu método 'checkin' funcionando totalmente diferente.
    @Override
    public void checkin(){
        System.out.println("Um humano chamado" + nome + "acaba de fazer checkin no hotel, algo está errado!");
    }

    /* Perceba que: por mais que exista a classe 'Human', nenhum humano é acrescentado no banco de dados.
    * Isso acontece pois o Hotel de Animais não considera humanos como animais também, devido a altos índices
    * de antropocentrismo. Assim que um humano faz checkin a equipe de segurança do Hotel de Animais, formada
    * por tigres e leões modernos, da um jeito na situação em alguns instantes. A classe 'Human' só existe
    * para que possamos adicionar mais herdeiros à classe 'Client' e poder demonstrar melhor os 4 conceitos
    * da programação orientada à objetos. Qualquer dano aos humanos testados serão ressarcidos pela faculdade
    * que é responsável pela aplicação do trabalho aos alunos testadores.*/
}
