public class ResponseMessage {
    private String type;
    private int vak;
    private int[] kaarten;
    private int rij;

    /**
     * Een Message die wordt gebruikt als stoelnummers mee moeten worden gegeven
     * @param type het type response
     * @param vak het vak van de kaarten
     * @param rij de rij van de kaarten
     * @param kaarten de kaarten voor in de rij
     */

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
