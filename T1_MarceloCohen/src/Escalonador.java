import java.util.*;

//Classe que faz a simulação das tarefas
public class Escalonador{
//Método estático que simula a execução das tarefas
//Recebe: mapa de tarefas, numero de processadores, e a politica (MIN ou MAX)
public static int simular(Map<String,Tarefa> tarefas ,int nProc, boolean politicaMin)
{
    //Guarda o número de dependencias de cada tarefa (copia dos dados)
    Map<String, Integer> dependencias = new HashMap<>();
    for(Tarefa t : tarefas.values()){
        dependencias.put(t.nome, t.dependenciasRestantes);
}

//Fila de prioridade para escolher tarefas disponiveis
PriorityQueue<Tarefa> disponiveis;
    if(politicaMin){
        //Politica MIN -> tarefas com menor tempo primeiro
        disponiveis = new PriorityQueue<>((a,b) -> a.tempo - b.tempo);
    }
    else{
        //Politica MAX -> tarefas com maior tempo primeiro
        disponiveis = new PriorityQueue<>((a,b)-> b.tempo - a.tempo);
    }

    //Adiciona as tarefas que já podem ser usadas (sem dependencias)
        for(Tarefa t : tarefas.values()){
            if(t.dependenciasRestantes == 0)
                disponiveis.add(t);
        }
    //Fila de tarefas de eexecucao -> guarda [tempoDeConclusao, idTarefa]
    PriorityQueue<int[]> emExecucao = new PriorityQueue<>(Comparator.comparingInt(a-> a[0]));
    Map<Integer, Tarefa> idParaTarefa = new HashMap<>();

    int tempoAtual = 0;//relogio do sistema
    int id = 0; //identificador incremental para as tarefas

    //Continua enquanto ainda houver tarefas disponiveis ou em execucao

    while(!disponiveis.isEmpty() || !emExecucao.isEmpty()){
        //coloca novas tarefas em execucao (se houver processadores livres)
        while(!disponiveis.isEmpty() && emExecucao.size() < nProc){
            Tarefa t = disponiveis.poll(); //pega para a proxima tarefa
            id++; //gera um id unico para ela

            emExecucao.add(new int[]{tempoAtual + t.tempo,id}); // registra quando ela terminar
            idParaTarefa.put(id, t); //guarda a referencia para depois

        }

        //Pega a tarefa que termina primeiro
        int[] fim = emExecucao.poll();  
        tempoAtual = fim[0]; //avança o relogio ate este tempo
        Tarefa concluida = idParaTarefa.get(fim[1]);// pega a tarefa concluida

        //Libera as tarefas pendentes

        for(Tarefa prox: concluida.proximas){
            dependencias.put(prox.nome, dependencias.get(prox.nome) -1);
            //Se todas as dependencias dessa tarefa já terminaram -> fica disponivel
            if(dependencias.get(prox.nome) == 0){
                disponiveis.add(prox);
            }
        }
    }

//Quando todas as tarefas terminaram, retorna o tempo total gasto
return tempoAtual;                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                              

}
}