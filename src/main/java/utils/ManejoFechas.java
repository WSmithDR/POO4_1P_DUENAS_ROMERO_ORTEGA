package utils;

import java.text.SimpleDateFormat;
import java.util.Date;


public class ManejoFechas {
    /**
     * Convierte una fecha a formato yyyy-MM-dd.
     * @param fecha Fecha a formatear
     * @return String con la fecha formateada
     */
    public static String setFechaSimple(Date fecha) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        return sdf.format(fecha);
    }

    /**
     * Parsea una fecha en formato yyyy-MM-dd a un objeto Date.
     * @param fechaStr Fecha en formato String (yyyy-MM-dd)
     * @return Objeto Date correspondiente, o null si hay error
     */
    public static Date parseFechaSimple(String fechaStr) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            return sdf.parse(fechaStr);
        } catch (Exception e) {
            return null;
        }
    }

}
