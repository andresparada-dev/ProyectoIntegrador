public class IngestaSensores {

    private static final double TEMPERATURA_MINIMA = -40.0;
    private static final double TEMPERATURA_MAXIMA = 60.0;

    private static final double HUMEDAD_MINIMA = 0.0;
    private static final double HUMEDAD_MAXIMA = 100.0;

    private static final double PM25_MINIMO = 0.0;


    public static void main(String[] args) {

        String linea =
                "EST-001,2026-09-07 08:00,18.5,75.2,32.4";

        procesarLinea(linea);
    }


    public static void procesarLinea(String linea) {

        String[] campos =
                separarCampos(linea);

        if (!tieneNumeroCorrectoDeCampos(campos)) {

            reportarRechazo(
                    "Cantidad incorrecta de campos"
            );

            return;
        }

        try {

            LecturaSensor lectura =
                    crearLectura(campos);

            validarYProcesarLectura(lectura);

        } catch (NumberFormatException e) {

            reportarRechazo(
                    "Valor numérico inválido"
            );
        }
    }


    public static String[] separarCampos(
            String linea) {

        return linea.split(",");
    }


    public static boolean tieneNumeroCorrectoDeCampos(
            String[] campos) {

        return campos.length == 5;
    }


    public static LecturaSensor crearLectura(
            String[] campos) {

        String id = campos[0];
        String fechaHora = campos[1];

        double temperatura =
                Double.parseDouble(campos[2]);

        double humedad =
                Double.parseDouble(campos[3]);

        double pm25 =
                Double.parseDouble(campos[4]);

        return new LecturaSensor(
                id,
                fechaHora,
                temperatura,
                humedad,
                pm25
        );
    }


    public static void validarYProcesarLectura(
            LecturaSensor lectura) {

        String motivo =
                obtenerMotivoInvalidez(lectura);

        if (motivo != null) {

            reportarRechazo(motivo);

            return;
        }

        aceptarLectura(lectura);
    }


    public static String obtenerMotivoInvalidez(
            LecturaSensor lectura) {

        if (!esTemperaturaValida(
                lectura.getTemperatura())) {

            return "Temperatura fuera de rango";
        }

        if (!esHumedadValida(
                lectura.getHumedad())) {

            return "Humedad fuera de rango";
        }

        if (!esPm25Valido(
                lectura.getPm25())) {

            return "PM2.5 negativo";
        }

        return null;
    }


    public static boolean esTemperaturaValida(
            double temperatura) {

        return temperatura >= TEMPERATURA_MINIMA &&
                temperatura <= TEMPERATURA_MAXIMA;
    }


    public static boolean esHumedadValida(
            double humedad) {

        return humedad >= HUMEDAD_MINIMA &&
                humedad <= HUMEDAD_MAXIMA;
    }


    public static boolean esPm25Valido(
            double pm25) {

        return pm25 >= PM25_MINIMO;
    }


    public static void aceptarLectura(
            LecturaSensor lectura) {

        System.out.println(
                "Lectura aceptada"
        );

        System.out.println(
                "Estación: " +
                        lectura.getIdEstacion()
        );

        System.out.println(
                "Temperatura: " +
                        lectura.getTemperatura() + "°C"
        );
        System.out.println(
                "Humedad: " +
                        lectura.getHumedad() + " %"
        );

        System.out.println(
                "PM2.5: " +
                        lectura.getPm25()
        );
    }


    public static void reportarRechazo(
            String motivo) {

        System.out.println(
                "Lectura rechazada: " +
                        motivo
        );
    }
}

