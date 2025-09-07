import java.util.*;
import java.io.*;

public class App {
    public static void main(String[] args) throws IOException {
       File pasta = new File("casos");

       File[]arquivos = pasta.listFiles((dir, name) -> name.endsWith(".txt"));
       if(arquivos == null || arquivos.length == 0){
            System.out.println("Nenhum arquivo .txt encontrado na pasta!");
            return;
    }
    Arrays.sort(arquivos);

    //Criar arquivo CSV para salvar os resultados 
    PrintWriter csv = new PrintWriter(new FileWriter("resultados.csv"));
    csv.println("Casos, Processadores, Tempo_MIN, Tempo_MAX");

    for(File arquivo:arquivos){
        Scanner sc = new Scanner(arquivo);

        String linha = sc.nextLine().trim();
        int nProc = Integer.parseInt(linha.split(" ")[2]);

        Map<String, Tarefa> tarefas = new HashMap<>();

        while (sc.hasNextLine()) {
            linha = sc.nextLine().trim();
            if(linha.isEmpty() || !linha.contains("->")) 
            continue;

            String[]partes = linha.split("->");
            String origem = partes[0].trim();
            String destino = partes[1].trim();

            String[] o = origem.split("_");
            String[] d = destino.split("_");

            String nomeOrigem = o[0] + "_" + o[1];
            int tempoOrigem = Integer.parseInt(o[1]);

            String nomeDestino = d[0] + "_" + d[1];
            int tempoDestino = Integer.parseInt(d[1]);

            tarefas.putIfAbsent(nomeOrigem, new Tarefa(nomeOrigem, tempoOrigem));
            tarefas.putIfAbsent(nomeDestino, new Tarefa(nomeDestino, tempoDestino));

            tarefas.get(nomeOrigem).proximas.add(tarefas.get(nomeDestino));
            tarefas.get(nomeDestino).dependenciasRestantes++;


            
        }

        sc.close();

        //Executa a simulação

      int tempoMin = Escalonador.simular(tarefas, nProc, true);
      int tempoMax = Escalonador.simular(tarefas, nProc, false);

      //Salva no CSV 
      csv.printf("%s,%d, %d, %d%n", arquivo.getName(), nProc , tempoMin , tempoMax);

      //Também mostra no console(opcional)

      System.out.println("=== " + arquivo.getName() + " ===");
      System.out.println("Processadores: " + nProc);
      System.out.println("Tempo MIN:" + tempoMin);
      System.out.println("Tempo MAX:" + tempoMax);
      System.out.println();
    }

    csv.close();
    System.out.println("Resultados salvos em resultados.csv");
}
}