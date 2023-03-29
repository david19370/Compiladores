import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Scanner 
{

    private final String source;

    private final List<Token> tokens = new ArrayList<>();

    private int linea = 1;

    private static final Map<String, TipoToken> palabrasReservadas;
    static {
        palabrasReservadas = new HashMap<>();
        palabrasReservadas.put("y", TipoToken.Y);
        palabrasReservadas.put("clase", TipoToken.CLASE);
        palabrasReservadas.put("ademas", TipoToken.ADEMAS);
        palabrasReservadas.put("falso", TipoToken.FALSO);
        palabrasReservadas.put("para", TipoToken.PARA);
        palabrasReservadas.put("fun", TipoToken.FUN); //definir funciones
        palabrasReservadas.put("si", TipoToken.SI);
        palabrasReservadas.put("nulo", TipoToken.NULO);
        palabrasReservadas.put("o", TipoToken.O);
        palabrasReservadas.put("imprimir", TipoToken.IMPRIMIR);
        palabrasReservadas.put("retornar", TipoToken.RETORNAR);
        palabrasReservadas.put("super", TipoToken.SUPER);
        palabrasReservadas.put("este", TipoToken.ESTE);
        palabrasReservadas.put("verdadero", TipoToken.VERDADERO);
        palabrasReservadas.put("var", TipoToken.VAR); //definir variables
        palabrasReservadas.put("mientras", TipoToken.MIENTRAS);
    }

    private static final Map<String, TipoToken> signosLenguaje;
    static {
        signosLenguaje = new HashMap<>();
        signosLenguaje.put("(", TipoToken.PARENTESIS_DERECHO);
        signosLenguaje.put(")", TipoToken.PARENTESIS_IZQUIERDO);
        signosLenguaje.put("{", TipoToken.LLAVE_DERECHA);
        signosLenguaje.put("}", TipoToken.LLAVE_IZQUIERDA);
        signosLenguaje.put(",", TipoToken.COMA);
        signosLenguaje.put(".", TipoToken.PUNTO);
        signosLenguaje.put(";", TipoToken.PUNTO_COMA);
        signosLenguaje.put("-", TipoToken.GUION_MEDIO);
        signosLenguaje.put("+", TipoToken.MAS);
        signosLenguaje.put("*", TipoToken.ASTERISCO);
        signosLenguaje.put("/", TipoToken.BARRA_INCLINADA);
        signosLenguaje.put("!", TipoToken.ADMIRACION);
        signosLenguaje.put("!=", TipoToken.OP_DIFERENTE);
        signosLenguaje.put("=", TipoToken.IGUAL_QUE);
        signosLenguaje.put("==", TipoToken.OP_IGUAL_QUE);
        signosLenguaje.put("<", TipoToken.MENOR_QUE);
        signosLenguaje.put("<=", TipoToken.OP_MENOR_IGUAL_QUE);
        signosLenguaje.put(">", TipoToken.MAYOR_QUE);
        signosLenguaje.put(">=", TipoToken.OP_MAYOR_IGUAL_QUE);
    }

    private static final Map<String, TipoToken> identificadorCadenaNumero;
    static {
        identificadorCadenaNumero = new HashMap<>();
        identificadorCadenaNumero.put("REGEX_ID", TipoToken.IDENTIFICADOR);
        identificadorCadenaNumero.put("REGEX_CADENA", TipoToken.CADENA);
        identificadorCadenaNumero.put("REGEX_NUMERO", TipoToken.NUMERO);
    }

    Scanner(String source){
        this.source = source;
    }

    List<Token> scanTokens()
    {
       
        //Aquí va el corazón del scanner.

        /*
        Analizar el texto de entrada para extraer todos los tokens
        y al final agregar el token de fin de archivo
         */
        List<Token> tokens = new ArrayList<>();
         int estado, posicion = 0;
         StringBuilder lexema = new StringBuilder();

         while(posicion < input.length())
         {
            char currentChar = input.charAt(posicion);

            switch(estado)
            {
                case 0:
                    if(Character.isDigit(currentChar))
                    {
                        estado = 1;
                        lexema.append(currentChar);
                    }
                    else if(Character.isLetter(currentChar))
                    {
                        estado = 2;
                        lexema.append(currentChar);
                    }
                    else if(currentChar == '+')
                    {
                        posicion++;
                        tokens.add(new Token(TipoToken.MAS,"+",null, linea));
                        
                    }
                    else if(currentChar == '-')
                    {
                        posicion++;
                        tokens.add(new Token(TipoToken.MENOS,"+",null, linea));
                    }
                    else if(currentChar == '*')
                    {
                        posicion++;
                        tokens.add(new Token(TipoToken.MULTIPLICACION,"+",null, linea));
                    }
                    else if(currentChar == '/')
                    {
                        posicion++;
                        tokens.add(new Token(Token.Type.DIVISION,"/"));
                    }
                    else if(currentChar == '(')
                    {
                        posicion++;
                        tokens.add(new Token(TipoToken.PARENTESIS_DERECHO,"(",null, linea);
                    }
                    else if(currentChar == ')')
                    {
                        posicion++;
                        tokens.add(new Token(Token.Type.RIGHT_PARENTHESIS,")"));
                    }



            }

        }


    }


        tokens.add(new Token(TipoToken.EOF, "", null, linea));

        return tokens;
}
}

/*
Signos o símbolos del lenguaje:
(   PARENTESIS_DERECHO
)   PARENTESIS_IZQUIERDO
{   LLAVE_DERECHA
}   LLAVE_IZQUIERDA
,   COMA
.   PUNTO
;   PUNTO_COMA
-   GUION_MEDIO
+   MAS
*   ASTERISCO
/   BARRA_INCLINADA
!   ADMIRACION
!=  OP_DIFERENTE
=   IGUAL_QUE
==  OP_IGUAL_QUE
<   MENOR_QUE
<=  OP_MENOR_IGUAL_QUE
>   MAYOR_QUE
>=  OP_MAYOR_IGUAL_QUE
// -> comentarios (no se genera token)
/* ... * / -> comentarios (no se genera token)
Identificador,  IDENTIFICADOR
Cadena          CADENA
Numero          NUMERO
Cada palabra reservada tiene su nombre de token
 */