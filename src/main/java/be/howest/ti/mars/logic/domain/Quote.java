package be.howest.ti.mars.logic.domain;

public class Quote {
    private int id;
    private String value;

    public Quote(String quote) {
        this.value = quote;
    }

    public Quote(int id, String quote) {
        this(quote);
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
