package org.neustupov;

import net.jcip.annotations.GuardedBy;
import net.jcip.annotations.NotThreadSafe;
import net.jcip.annotations.ThreadSafe;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.math.BigInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;

@ThreadSafe
public class StatelessFactorizedServlet extends HttpServlet {

    private static final long serialVersionUID = 1;

    @GuardedBy("this")
    private BigInteger lastNumber;

    @GuardedBy("this")
    private BigInteger[] lastFactors;

    @GuardedBy("this")
    private long hits;

    @GuardedBy("this")
    private long cacheHits;

    public synchronized long getHits() {
        return hits;
    }

    public synchronized double getCacheHitRatio() {
        return (double) cacheHits / (double) hits;
    }

    @Override
    protected synchronized void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        System.out.println("StatelessFactorizedServlet doGet() method");
        BigInteger i = extractFromRequest(req);
        BigInteger[] factors = null;
        synchronized (this){
            ++hits;
            if(i.equals(lastNumber)){
                ++cacheHits;
                factors = lastFactors.clone();
            }
        }
        if(factors == null){
            factors = factor(i);
            synchronized (this){
                lastNumber = i;
                lastFactors = factors.clone();
            }
        }
    }

    private BigInteger extractFromRequest(ServletRequest request) {
        return (BigInteger) request.getAttribute("BigInteger");
    }

    private BigInteger[] factor(BigInteger bigInteger) {
        return new BigInteger[10];
    }

    private void encodeIntoResponse(ServletResponse response, BigInteger[] bigIntegers) throws IOException {
        StringBuilder sb = new StringBuilder();
        for (BigInteger bigInteger : bigIntegers) {
            sb.append(bigInteger);
            sb.append(" - ");
        }
        response.setContentType("text/plain");
        response.getWriter().write(sb.toString());
        response.getWriter().close();
    }
}
