import java.util.ArrayList;
import java.util.List;

public class Tarefa{
    public String nome;
    public int tempo;
    public int dependenciasRestantes = 0; //quantas tarefas precisam terminar antes dela começar
    public List<Tarefa> proximas = new ArrayList<>();

    //Construtor: inicializa com o nome e a tarefa
    public Tarefa(String nome, int tempo){
        this.nome = nome;
        this.tempo = tempo;
    }
}