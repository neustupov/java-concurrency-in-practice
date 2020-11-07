package org.neustupov.quotetask;

import java.util.concurrent.Callable;

public class QuoteTask implements Callable<TravelQoute> {
    private final TravelCompany company;
    private final TravelInfo travelInfo;

    public QuoteTask(TravelCompany company, TravelInfo travelInfo) {
        this.company = company;
        this.travelInfo = travelInfo;
    }

    @Override
    public TravelQoute call() throws Exception {
        return company.solicitQuote(travelInfo);
    }

    public TravelQoute getFailureQuote(){
        return new TravelQoute();
    }

    public TravelQoute getTimeoutQuote(){
        return new TravelQoute();
    }
}
