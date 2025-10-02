import java.util.Random;

public class Terrorista {
    private String nome;
    private int energia;
    private int granadas;
    private String armamento;
    private boolean bombaPlantada;

    private final Random rnd = new Random();

    public Terrorista(String nome, int energia, int granadas, String armamento) {
        this.nome = (nome != null && nome.length() >= 4) ? nome : "SemNome";
        setEnergia(energia);
        setGranadas(granadas);
        this.armamento = armamento; 
        this.bombaPlantada = false;
    }

    public String getNome() { return nome; }
    public int getEnergia() { return energia; }
    public String getArmamento() { return armamento; }
    public boolean isBombaPlantada() { return bombaPlantada; }

    public void setEnergia(int energia) { this.energia = Math.max(0, Math.min(energia, 10)); }
    public void setGranadas(int granadas) { this.granadas = Math.max(0, Math.min(granadas, 5)); }
    public void setBombaPlantada(boolean bombaPlantada) { this.bombaPlantada = bombaPlantada; }


    public void plantarBomba(String mapa) {
        bombaPlantada = true;
        System.out.printf("%s plantou a bomba em %s!\n", nome, mapa);
    }

    public void lancarGranada(Policial alvo, String mapa) {
        int danoGranada = 5;
        if (granadas > 0) {
            granadas--;
            alvo.setEnergia(alvo.getEnergia() - danoGranada);
            System.out.printf("%s lançou granada (restam %d) e causou %d de dano ao Policial em %s.\n", nome, granadas, danoGranada, mapa);
        } else {
            System.out.printf("%s tentou lançar granada, mas não tem em %s.\n", nome, mapa);
        }
    }

    public void atacar(Policial alvo, String mapa) {
        int dano = switch (armamento) {
            case "Faca" -> 1;
            case "Pistola" -> 2;
            case "Fuzil" -> 3;
            case "Granada" -> 6;
            default -> 0;
        };
        alvo.setEnergia(alvo.getEnergia() - dano);
        System.out.printf("%s atacou com %s e causou %d de dano ao Policial em %s.\n", nome, armamento, dano, mapa);
    }

    public void passarAVez(Policial alvo, String mapa) {
        int recupera = rnd.nextInt(2) + 1;
        alvo.setEnergia(alvo.getEnergia() + recupera);
        System.out.printf("%s passou a vez. O Policial recuperou %d de energia em %s.\n", nome, recupera, mapa);
    }
}
