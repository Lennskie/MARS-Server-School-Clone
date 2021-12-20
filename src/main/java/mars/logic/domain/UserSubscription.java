package mars.logic.domain;

public class UserSubscription {
    private String userIdentifier;
    private String subscriptionName;
    private double price;
    private int startDate;
    private int endDate;
    private boolean reimbursed;

    public UserSubscription(String userIdentifier, String subscriptionName, double price, int startDate, int endDate, boolean reimbursed) {
        this.userIdentifier = userIdentifier;
        this.subscriptionName = subscriptionName;
        this.price = price;
        this.startDate = startDate;
        this.endDate = endDate;
        this.reimbursed = reimbursed;
    }

    public String getUserIdentifier() {
        return userIdentifier;
    }

    public void setUserIdentifier(String userIdentifier) {
        this.userIdentifier = userIdentifier;
    }

    public String getSubscriptionName() {
        return subscriptionName;
    }

    public void setSubscriptionName(String subscriptionName) {
        this.subscriptionName = subscriptionName;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getStartDate() {
        return startDate;
    }

    public void setStartDate(int startDate) {
        this.startDate = startDate;
    }

    public int getEndDate() {
        return endDate;
    }

    public void setEndDate(int endDate) {
        this.endDate = endDate;
    }

    public boolean isReimbursed() {
        return reimbursed;
    }

    public void setReimbursed(boolean reimbursed) {
        this.reimbursed = reimbursed;
    }
}
