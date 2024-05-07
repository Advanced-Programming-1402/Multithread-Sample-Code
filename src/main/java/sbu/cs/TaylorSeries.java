package sbu.cs;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class TaylorSeries {
    public static class CalculateSin implements Runnable {
        MathContext mc = new MathContext(1000);     // use when calling BigDecimal operations like divide or multiply
        BigDecimal x;
        int n;
        public CalculateSin(BigDecimal x, int n) {
            this.x = x;
            this.n = n;
        }

        @Override
        public void run() {
            BigDecimal sign = new BigDecimal(1);
            if (n%2 == 1) {
                sign = new BigDecimal(-1);
            }

            BigDecimal numerator = x.pow(2*n + 1);
            numerator = numerator.multiply(sign, mc);
            BigDecimal denominator = factorial(2*n + 1);

            BigDecimal result = numerator.divide(denominator, mc);
            addTouSum(result);
        }

        public BigDecimal factorial(int n){
            BigDecimal temp = new BigDecimal(1);
            for (int i = 1; i <= n; i++) {
                temp = temp.multiply(new BigDecimal(i), mc);
            }

            return temp;
        }
    }

    public static BigDecimal sum;

    public static synchronized void addTouSum(BigDecimal value){
        sum = sum.add(value);
    }

    public static void main(String[] Args) {
//        BigDecimal decimal = new BigDecimal("4.12");
//        System.out.println(decimal);

//        ExecutorService cachedTP = Executors.newCachedThreadPool();
        ExecutorService threadPool = Executors.newFixedThreadPool(4);

        sum = new BigDecimal(0);
        BigDecimal x = new BigDecimal("0.01");  // sin(0.01) = ?

        for (int i = 0; i < 100; i++) {                          // increasing the number of iterations improves
            CalculateSin task = new CalculateSin(x, i);          // accuracy, try 200 and see the difference!
            threadPool.execute(task);
        }

        threadPool.shutdown();      // always call before awaitTermination

        try {
            threadPool.awaitTermination(10000, TimeUnit.MILLISECONDS);
        } catch (InterruptedException e) {
           e.printStackTrace();
        }

        // the accurate value of sin(0.01) up to 1000 decimal places
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

        sum = sum.setScale(1000, RoundingMode.HALF_DOWN);
        accurateValue = accurateValue.setScale(1000, RoundingMode.HALF_DOWN);

        System.out.println("sin(0.01) up to 1000 decimal places:");
        System.out.println("Calculated Value:  " + sum);
        System.out.println("Accurate Value:    " + accurateValue);

        // note that a BigDecimal object may be printed as a number in the scientific notation. To print a normal
        // decimal number use the toPlainString() method.
        System.out.println("Difference:        " + accurateValue.subtract(sum).abs().toPlainString());

    }

}
