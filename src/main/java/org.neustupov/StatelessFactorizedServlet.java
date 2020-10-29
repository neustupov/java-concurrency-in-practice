package org.neustupov;

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

@NotThreadSafe
public class StatelessFactorizedServlet extends HttpServlet {

    private static final long serialVersionUID = 1;

    private final AtomicReference<BigInteger> lastNumber = new AtomicReference<BigInteger>();
    private final AtomicReference<BigInteger[]> lastFactors = new AtomicReference<BigInteger[]>();

    private final AtomicLong count = new AtomicLong(0);

    public long getCount() {
        return count.get();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        System.out.println("StatelessFactorizedServlet doGet() method");
        BigInteger i = extractFromRequest(req);
        if (i.equals(lastNumber.get())) {
            encodeIntoResponse(resp, lastFactors.get());
        } else {
            BigInteger[] factors = factor(i);
            lastNumber.set(i);
            lastFactors.set(factors);
            count.incrementAndGet();
            encodeIntoResponse(resp, factors);
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
