package sbu.cs;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class TaylorSeries
{
    public static class CalculateSin implements Runnable {
        MathContext mc;
        int n;
        BigDecimal x;
        public CalculateSin(int n, BigDecimal x) {
            mc = new MathContext(1000, RoundingMode.HALF_DOWN);
            this.n = n;
            this.x = x;
        }

        @Override
        public void run() {
            BigDecimal sign = new BigDecimal("1");
            if (n % 2 == 1) {
                sign = new BigDecimal("-1");
            }

            x = x.pow(2*n + 1, mc);
            BigDecimal numerator = x.multiply(sign, mc);
            BigDecimal denominator = factorial(2*n + 1);

            BigDecimal result = numerator.divide(denominator, mc);
            addToSum(result);
        }

        private BigDecimal factorial(int n) {
            BigDecimal temp = new BigDecimal("1");
            for (int i = 1; i <= n; i++) {
                temp = temp.multiply(new BigDecimal(i), mc);
            }
            return temp;
        }


    }

    public static synchronized void addToSum(BigDecimal value) {
        sum = sum.add(value);
    }

    static BigDecimal sum;
    public static void main(String[] args) {
//        double a = 19.11;
//        double b = 7.43;
//
//        System.out.println(a*b);
//
//        BigDecimal a = new BigDecimal("19.11");
//        BigDecimal b = new BigDecimal("7.43");
//        System.out.println(a.multiply(b));

        sum = new BigDecimal("0");
        BigDecimal x = new BigDecimal("0.01");

        ExecutorService threadPool = Executors.newFixedThreadPool(4);
//        ExecutorService cachedThreadPool = Executors.newCachedThreadPool();

        for (int i = 0; i < 1000; i++) {
            CalculateSin task = new CalculateSin(i, x);
            threadPool.execute(task);
        }

        threadPool.shutdown();

        try {
            threadPool.awaitTermination(10000, TimeUnit.MILLISECONDS);
        } catch (InterruptedException e) {
            System.out.println(e.getMessage());
        }

        System.out.println(sum.toPlainString());

        BigDecimal accurateValue = new BigDecimal("0.009999833334166664682542438269099729038964385360169151033879" +
                "1124794097345090639159659426367929614989901525182568937606738071143914781018343679925045223748779233" +
                "4633395662957704288475175228160558357110105077439518716860615533070998720636987509269668842490541364" +
                "2046237535076816415219593915753970609625155007149734343650140126010756472960507873872984042987441343" +
                "4632784947709943715670321717675280271359744354619523360203501121465199963741173489051927920551271878" +
                "0890799396861427770050764919858890678677220571952090318596147460309593993397336341626522171452145068" +
                "2733933417711179372733354590095099959888961964291389175600643442999116373725594047366187408263088683" +
                "4976343405988894064532908768068263525544004211332459809773301487980907370431155859574851192235000645" +
                "6064003773432638837702651724406011257895842835907410397326351149391214249645075873929695397792073094" +
                "1188402364506858174852606021099282767046225114299907364917050686481947141812831031312882254660611456" +
                "524573907422800164140884130285721050703575");

        BigDecimal error = sum.subtract(accurateValue);
        System.out.println(error.abs().toPlainString());
    }
}
