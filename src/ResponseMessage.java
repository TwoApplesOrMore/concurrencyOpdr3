public class ResponseMessage {
    private String type;
    private int vak;
    private int[] kaarten;
    private int rij;

    public ResponseMessage(String type, int vak, int rij, int[] kaarten) {
        this.type = type;
        this.vak = vak;
        this.kaarten = kaarten;
        this.rij = rij;


    }

    public String getType() {
        return type;
    }

    public int getVak() {
        return vak;
    }

    public int[] getKaarten() {
        return kaarten;
    }

    public int getRij() {
        return rij;
    }
}
