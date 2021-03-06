package sic.util;

import java.util.Calendar;
import java.util.Date;
import java.util.regex.Pattern;

public class Validator {

    public static boolean esNumericoPositivo(String cadena) {
        for (int i = 0; i < cadena.length(); i++) {
            if (cadena.charAt(i) < '0' | cadena.charAt(i) > '9') {
                return false;
            }
        }
        return true;
    }

    public static boolean esNumericoDecimal(String cadena, char simboloDecimal) {
        try {
            if (cadena.equals("")) {
                return true;
            }
            cadena = cadena.replace(",", "");
            Double test = Double.valueOf(cadena);
            return true;

        } catch (NumberFormatException e) {
            return false;
        }
    }

    public static boolean esVacio(String campo) {
        if (campo == null) {
            return true;
        }

        if (campo.equals("")) {
            return true;
        }

        return false;
    }

    public static boolean esLongitudCaracteresValida(String cadena, int cantCaracteresValidos) {
        if (cadena == null) {
            return true;
        }

        if (cadena.length() > cantCaracteresValidos) {
            return false;
        } else {
            return true;
        }
    }

    public static boolean esLongitudNumericaValida(String cadena, double longitudValida) {
        if (cadena == null | cadena.equals("")) {
            return true;
        }

        cadena = cadena.replace(",", "");
        if (Double.parseDouble(cadena) > longitudValida) {
            return false;
        } else {
            return true;
        }
    }

    public static boolean esEmailValido(String cadena) {
        if (!cadena.equals("")) {
            return ((Pattern.compile("[a-zA-Z0-9_]+[.[a-zA-Z0-9]+]*@[[a-zA-Z0-9_]+.[a-zA-Z0-9]+]+")).matcher(cadena)).matches();
        } else {
            return true;
        }
    }

    /**
     * Compara dos fechas pasadas como Date
     *
     * @param fechaAnterior
     * @param fechaSiguiente
     * @return 0 si es igual, menor a 0 si fechaAnterior esta antes de
     * fechaSiguiente, mayor a 0 si fechaAnterior esta despues de fechaSiguiente
     */
    public static int compararFechas(Date fechaAnterior, Date fechaSiguiente) {
        Calendar anterior = Calendar.getInstance();
        Calendar siguiente = Calendar.getInstance();
        anterior.setTime(fechaAnterior);
        siguiente.setTime(fechaSiguiente);
        return siguiente.compareTo(anterior);
    }
}
