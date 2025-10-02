import java.util.Random;
import java.util.Scanner;

public class App {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        Random rnd = new Random();

        String mapa = "";
        int rodadas = 0;

        // Configuração do Mapa
        while (true) {
            System.out.println("Escolha o mapa: [1] Roma | [2] Fatec Ipiranga");
            if (sc.hasNextInt()) {
                int escolha = sc.nextInt();
                if (escolha == 1) {
                    mapa = "Roma";
                    break;
                } else if (escolha == 2) {
                    mapa = "Fatec Ipiranga";
                    break;
                }
            }
            System.out.println("Opção inválida. Digite 1 ou 2.");
            sc.nextLine();
        }

        // Configuração do Número de Rodadas
        while (rodadas < 1 || rodadas > 20 || rodadas % 2 == 0) {
            System.out.println("Quantas rodadas? Escolha um número ímpar entre 1 e 20: ");
            if (sc.hasNextInt()) {
                rodadas = sc.nextInt();
            } else {
                sc.nextLine();
            }
        }
        sc.nextLine();

        // Criação dos personagens
        Terrorista terrorista = new Terrorista("Terrorista", 10, 3, "Fuzil");
        Policial policial = new Policial("Policial", 10, 2, "Pistola");

        int vitoriasT = 0;
        int vitoriasP = 0;

        // Loop principal do jogo
        for (int r = 1; r <= rodadas; r++) {
            System.out.println("\n--- Rodada " + r + " em " + mapa + " ---");
            
            // Reset de estado
            terrorista.setEnergia(10);
            policial.setEnergia(10);
            terrorista.setBombaPlantada(false);
            
            int estadoBomba = 0; // 0 = não plantada, 1 = plantada, -1 = desarmada
            
            // Loop da rodada atual: Continua enquanto houver energia E a bomba não tiver sido desarmada
            while (terrorista.getEnergia() > 0 && policial.getEnergia() > 0 && estadoBomba != -1) {
                
                if (rnd.nextInt(2) == 0) {
                    // Turno do Terrorista
                    int acao = rnd.nextInt(4);
                    switch (acao) {
                        case 0: // Plantar Bomba
                            terrorista.plantarBomba(mapa);
                            estadoBomba = 1; // A bomba está plantada
                            break;
                        case 1: // Lançar Granada
                            terrorista.lancarGranada(policial, mapa);
                            break;
                        case 2: // Atacar
                            terrorista.atacar(policial, mapa);
                            break;
                        case 3: // Passar a Vez
                            terrorista.passarAVez(policial, mapa);
                            break;
                    }
                } else {
                    // Turno do Policial (IA Melhorada)
                    int acao;
                    
                    if (estadoBomba == 1) {
                         // 80% de chance de tentar desarmar (acao 0)
                        if (rnd.nextDouble() < 0.8) {
                            acao = 0;
                        } else {
                            // Senão, ataca ou lança granada (acao 1 ou 2)
                            acao = rnd.nextInt(2) + 1; 
                        }
                    } else {
                        // Prioriza ataque (2) ou granada (1), removendo "Passar a Vez" (3) e "Desarmar Bomba" (0).
                        acao = rnd.nextInt(2) + 1; // acao é 1 ou 2
                    }

                    switch (acao) {
                        case 0: // Desarmar Bomba
                            if (estadoBomba == 1) {
                                if (policial.desarmarBomba(terrorista, mapa)) { 
                                     estadoBomba = -1; // Desarmada - loop deve terminar agora
                                }
                            } else {
                                // Esta linha só deve ser alcançada em caso de falha rara da IA
                                System.out.println(policial.getNome() + " desperdiçou o turno tentando desarmar a bomba (estadoBomba = " + estadoBomba + ").");
                            }
                            break;
                        case 1: // Lançar Granada
                            policial.lancarGranada(terrorista, mapa);
                            break;
                        case 2: // Atacar 
                            policial.atacar(terrorista, mapa);
                            break;
                        case 3: // Passar a Vez
                            policial.passarAVez(terrorista, mapa);
                            break;
                    }
                }
                
                // Exibe status atual
                System.out.println(terrorista.getNome() + " energia: " + terrorista.getEnergia() + 
                                 " | " + policial.getNome() + " energia: " + policial.getEnergia());
                System.out.println("++++++++++++++++++++++++++++++++++++++++");
                
                // O loop agora termina por KO ou por estadoBomba == -1 (Desarmada)
            }
            
            // Verifica resultado da rodada
            if (terrorista.getEnergia() <= 0) {
                // Vitória por KO
                System.out.println(policial.getNome() + " venceu a rodada por nocaute!");
                vitoriasP++;
            } else if (policial.getEnergia() <= 0) {
                // Vitória por KO
                System.out.println(terrorista.getNome() + " venceu a rodada por nocaute!");
                vitoriasT++;
            } else if (estadoBomba == 1) {
                // Vitória por explosão (bomba plantada e loop terminou sem desarmar ou KO)
                System.out.println("Bomba explodiu! " + terrorista.getNome() + " venceu a rodada!");
                vitoriasT++;
            } else if (estadoBomba == -1) {
                // Vitória por desarme (bomba plantada e desarmada)
                System.out.println(policial.getNome() + " venceu a rodada por desarmar a bomba!");
                vitoriasP++;
            }
        }

        // Resultado final
        System.out.println("\n=== Resultado Final ===");
        System.out.println("Terrorista venceu " + vitoriasT + " rodadas.");
        System.out.println("Policial venceu " + vitoriasP + " rodadas.");
        
        if (vitoriasT > vitoriasP) {
            System.out.println("Vitória final do Terrorista!");
        } else if (vitoriasP > vitoriasT) {
            System.out.println("Vitória final do Policial!");
        } else {
             System.out.println("Empate no placar!");
        }

        sc.close();
    }
}
