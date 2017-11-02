public class Message {
    private String type;
    private int vak;
    private int kaarten;

    /**
     * Een bericht die meegegeven wordt als er kaartjes besteld gaan worden
     * @param type het type bericht
     * @param vak het gewenste vak
     * @param kaarten het aantal kaartjes die besteld worden
     */
    public Message(String type, int vak, int kaarten) {
        this.type = type;
        this.vak = vak;
        if(kaarten > 0 && kaarten < 5) {
            this.kaarten = kaarten;
        } else {
            this.kaarten = 1;
        }
    }

    public String getType() {
        return type;
    }

    public int getVak() {
        return vak;
    }

    public int getKaarten() {
        return kaarten;
    }
}
