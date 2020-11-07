package org.neustupov;

import net.jcip.annotations.ThreadSafe;
import org.neustupov.memoization.Computable;
import org.neustupov.memoization.Memoizer1;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.math.BigInteger;

@ThreadSafe
public class StatelessFactorizedServlet extends HttpServlet {

    private static final long serialVersionUID = 1;

    private final Computable<BigInteger, BigInteger[]> cache = new Memoizer1<>(this::factor);

    @Override
    protected synchronized void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        System.out.println("StatelessFactorizedServlet doGet() method");
        try {
            BigInteger i = extractFromRequest(req);
            encodeIntoResponse(resp, cache.compute(i));
        } catch (InterruptedException e) {
            System.out.println("Interrupted");
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
