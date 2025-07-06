package utils;

import java.text.SimpleDateFormat;
import java.util.Date;


public class ManejoFechas {
    /**
     * Obtiene la fecha del pedido en formato yyyy-MM-dd
     * @return String con la fecha formateada
     */
    public static String getFechaSimple(Date fecha) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        return sdf.format(fecha);
    }

}
