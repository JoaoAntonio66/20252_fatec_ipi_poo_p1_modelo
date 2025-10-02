import java.util.Random;

public class Policial {
    private String nome;
    private int energia;
    private int granadas;
    private String armamento;

    private final Random rnd = new Random();

    public Policial(String nome, int energia, int granadas, String armamento) {
        this.nome = (nome != null && nome.length() >= 4) ? nome : "SemNome";
        setEnergia(energia);
        setGranadas(granadas);
        this.armamento = armamento;
    }

    public String getNome() { return nome; }
    public int getEnergia() { return energia; }
    public String getArmamento() { return armamento; }

    public void setEnergia(int energia) { this.energia = Math.max(0, Math.min(energia, 10)); }
    public void setGranadas(int granadas) { this.granadas = Math.max(0, Math.min(granadas, 5)); }

    public boolean isBombaPlantada() { return false; }

    public boolean desarmarBomba(Terrorista terrorista, String mapa) {
        if (!terrorista.isBombaPlantada()) {
            System.out.println(nome + " tentou desarmar a bomba, mas ela não estava plantada em " + mapa + ".");
            return false;
        }
        System.out.println(nome + " desarmou a bomba na " + mapa + "!");
        terrorista.setBombaPlantada(false);
        return true;
    }

    public void lancarGranada(Terrorista alvo, String mapa) {
        int danoGranada = 4;
        if (granadas > 0) {
            granadas--;
            alvo.setEnergia(alvo.getEnergia() - danoGranada); 
            System.out.println(nome + " lançou granada (restam " + granadas + ") e causou " + danoGranada + " de dano ao Terrorista em " + mapa + ".");
        } else {
            System.out.println(nome + " não tem granadas para lançar em " + mapa + ".");
        }
    }

    public void atacar(Terrorista alvo, String mapa) {
        int dano = switch (armamento) {
            case "Faca" -> 1;
            case "Pistola" -> 2;
            case "Fuzil" -> 3;
            default -> 0;
        };
        alvo.setEnergia(alvo.getEnergia() - dano);
        System.out.printf("%s atacou com %s e causou %d de dano ao Terrorista em %s.\n", nome, armamento, dano, mapa);
    }

    public void passarAVez(Terrorista alvo, String mapa) {
        int recupera = rnd.nextInt(2) + 1;
        alvo.setEnergia(alvo.getEnergia() + recupera);
        System.out.printf("%s passou a vez. O Terrorista recuperou %d de energia em %s.\n", nome, recupera, mapa);
    }
}
