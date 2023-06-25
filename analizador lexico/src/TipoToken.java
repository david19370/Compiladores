public enum TipoToken {
    // Crear un tipoToken por palabra reservada
    // Crear un tipoToken: identificador, una cadena y numero
    // Crear un tipoToken por cada "Signo del lenguaje" (ver clase Scanner)

    // Palabras clave:
    Y, CLASE, ADEMAS, FALSO, PARA, FUN,  SI, NULO, O, IMPRIMIR, RETORNAR, SUPER, ESTE, VERDADERO, VAR, MIENTRAS,

    //identificador, una cadena y numero
    IDENTIFICADOR, CADENA, NUMERO,

    //Signo del lenguaje
    PARENTESIS_DERECHO, PARENTESIS_IZQUIERDO, LLAVE_DERECHA, LLAVE_IZQUIERDA, COMA, PUNTO, PUNTO_COMA, GUION_MEDIO, MAS,
    ASTERISCO, BARRA_INCLINADA, ADMIRACION, OP_DIFERENTE, IGUAL_QUE, OP_IGUAL_QUE, MENOR_QUE, OP_MENOR_IGUAL_QUE, MAYOR_QUE,
    OP_MAYOR_IGUAL_QUE,

    // Final de cadena
    EOF
}

