package mars.logic.domain;

import java.util.Objects;

public class Subscription {
    private final String name;
    private final String description;
    private final double price;
    private final String startDate;
    private String endDate;
    private boolean reimbursed;

    public Subscription(String name, String description, double price) {
        this(name, description, price, null, null, false);
    }

    public Subscription(String name, String description, double price, String startDate, String endDate, boolean reimbursed) {
        this.name = name;
        this.description = description;
        this.price = price;
        this.startDate = startDate;
        this.endDate = endDate;
        this.reimbursed = reimbursed;
    }

    public String getStartDate() {
        return startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public boolean isReimbursed() {
        return reimbursed;
    }

    public void setReimbursed(boolean reimbursed) {
        this.reimbursed = reimbursed;
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Subscription)) return false;
        Subscription that = (Subscription) o;
        return name.equals(that.name);
    }

    @Override
    public String toString() {
        return "Subscription{" +
                "name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", price=" + price +
                '}';
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public double getPrice() {
        return price;
    }
}
