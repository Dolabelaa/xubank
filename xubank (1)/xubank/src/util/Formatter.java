package util;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.*;

public class Formatter {
    public static String formatCurrency(double value) {
        NumberFormat nf = NumberFormat.getCurrencyInstance(new Locale("pt", "BR"));
        return nf.format(value);
    }

    public static String formatDate(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        return sdf.format(date);
    }
}
