package org.neustupov.immutable;

import net.jcip.annotations.GuardedBy;
import net.jcip.annotations.ThreadSafe;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.math.BigInteger;

@ThreadSafe
public class VolatileCachedFactorized extends HttpServlet {

    private static final long serialVersionUID = 1;

    private volatile OneValueCache cache = new OneValueCache(null, null);

    @Override
    protected synchronized void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        System.out.println("StatelessFactorizedServlet doGet() method");
        BigInteger i = extractFromRequest(req);
        BigInteger[] factors = cache.getFactors(i);
        if (factors == null) {
            factors = factor(i);
            cache = new OneValueCache(i, factors);
        }
        encodeIntoResponse(resp, factors);
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
