package org.neustupov.quotetask;

import java.util.*;
import java.util.concurrent.*;

public class TravelQuoteTask {

    ExecutorService exec = Executors.newCachedThreadPool();

    public List<TravelQoute> getRankedTravelQuotes(TravelInfo travelInfo,
                                                   Set<TravelCompany> companies,
                                                   Comparator<TravelQoute> ranking,
                                                   long time,
                                                   TimeUnit unit) throws InterruptedException {
        List<QuoteTask> tasks = new ArrayList<>();
        for (TravelCompany company : companies) {
            tasks.add(new QuoteTask(company, travelInfo));
        }
        List<Future<TravelQoute>> futures = exec.invokeAll(tasks, time, unit);
        List<TravelQoute> quotes = new ArrayList<>(tasks.size());
        Iterator<QuoteTask> taskIterator = tasks.iterator();
        for (Future<TravelQoute> f : futures) {
            QuoteTask task = taskIterator.next();
            try {
                quotes.add(f.get());
            } catch (ExecutionException e) {
                quotes.add(task.getFailureQuote());
            } catch (CancellationException e) {
                quotes.add(task.getTimeoutQuote());
            }
        }

        quotes.sort(ranking);
        return quotes;
    }
}
