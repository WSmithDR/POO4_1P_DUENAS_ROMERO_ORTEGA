package utils;

import java.text.SimpleDateFormat;
import java.util.Date;


public class ManejoFechas {
    /**
     * Obtiene la fecha del pedido en formato yyyy-MM-dd
     * @param fecha Fecha a formatear
     * @return String con la fecha formateada
     */
    public static String setFechaSimple(Date fecha) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        return sdf.format(fecha);
    }

}
