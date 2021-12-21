package mars.logic.data;

import mars.logic.domain.Subscription;

import java.util.List;

public interface SubscriptionsRepository {
    void generateData();
    List<Subscription> getSubscriptions();
}
