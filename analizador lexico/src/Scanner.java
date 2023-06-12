import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Scanner 
{

    private String source;

    private int linea = 1;

    private static final Map<String, TipoToken> palabrasReservadas;
    static {
        palabrasReservadas = new HashMap<>();
        palabrasReservadas.put("y", TipoToken.Y);
        palabrasReservadas.put("clase", TipoToken.CLASE);
        palabrasReservadas.put("ademas", TipoToken.ADEMAS);
        palabrasReservadas.put("falso", TipoToken.FALSO);
        palabrasReservadas.put("para", TipoToken.PARA);
        palabrasReservadas.put("fun", TipoToken.FUN); 
        palabrasReservadas.put("si", TipoToken.SI);
        palabrasReservadas.put("nulo", TipoToken.NULO);
        palabrasReservadas.put("o", TipoToken.O);
        palabrasReservadas.put("imprimir", TipoToken.IMPRIMIR);
        palabrasReservadas.put("retornar", TipoToken.RETORNAR);
        palabrasReservadas.put("super", TipoToken.SUPER);
        palabrasReservadas.put("este", TipoToken.ESTE);
        palabrasReservadas.put("verdadero", TipoToken.VERDADERO);
        palabrasReservadas.put("var", TipoToken.VAR); 
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
        identificadorCadenaNumero.put("Identificador", TipoToken.IDENTIFICADOR);
        identificadorCadenaNumero.put("Cadena", TipoToken.CADENA);
        identificadorCadenaNumero.put("Numero", TipoToken.NUMERO);
    }

    Scanner(String source){
        this.source = source;
    }
    
    List<Token> scanTokens()
    {
        List<Token> tokens = new ArrayList<>();
        int estado = 0;
        int posicion = 0;
        String lexema = "";
        source = source + " ";

        while(posicion < source.length())   
        {
            char currentChar = source.charAt(posicion);

            switch(estado)
            {
                case 0:
                    if(currentChar == '+') //Signo del lenguaje +
                    {
                        tokens.add(new Token(TipoToken.MAS,"+",null, linea));
                        posicion++;
                    }
                    else if(currentChar == '-') //Signo del lenguaje -
                    {
                        tokens.add(new Token(TipoToken.GUION_MEDIO,"-",null, linea));
                        posicion++;
                    }
                    else if(currentChar == '*') //Signo del lenguaje *
                    {
                        tokens.add(new Token(TipoToken.ASTERISCO,"*",null, linea));
                        posicion++;
                    }
                    else if(currentChar == '/') 
                    {
                        posicion++;
                        estado = 7;
                    }
                    else if(currentChar == '(') //Signo del lenguaje (
                    {
                        tokens.add(new Token(TipoToken.PARENTESIS_DERECHO,"(",null, linea));
                        posicion++;
                    }
                    else if(currentChar == ')') //Signo del lenguaje )
                    {
                        tokens.add(new Token(TipoToken.PARENTESIS_IZQUIERDO,")",null, linea));
                        posicion++;
                    }
                    else if(currentChar == '{') //Signo del lenguaje {
                    {
                        tokens.add(new Token(TipoToken.LLAVE_DERECHA,"{",null, linea));
                        posicion++;
                    }
                    else if(currentChar == '}') //Signo del lenguaje }
                    {
                        tokens.add(new Token(TipoToken.LLAVE_IZQUIERDA,"}",null, linea));
                        posicion++;
                    }
                    else if(currentChar == ',') //Signo del lenguaje ,
                    {
                        tokens.add(new Token(TipoToken.COMA,",",null, linea));
                        posicion++;
                    }
                    else if(currentChar == '.') //Signo del lenguaje .
                    {
                        tokens.add(new Token(TipoToken.PUNTO,".",null, linea));
                        posicion++;
                    }
                    else if(currentChar == ';') //Signo del lenguaje ;
                    {
                        tokens.add(new Token(TipoToken.PUNTO_COMA,";",null, linea));
                        posicion++; 
                    }
                    else if(currentChar == '!')
                    {
                        posicion++;
                        estado = 1;
                    }
                    
                    else if(currentChar == '=')
                    {
                        posicion++;
                        estado=2;
                    }
                    else if(currentChar == '<')
                    {
                        posicion++;
                        estado = 3;
                    }

                    else if(currentChar == '>')
                    {
                        posicion++;
                        estado = 4;
                    }
                    else if(currentChar == 'y') //Palabra reservada Y
                    {
                        tokens.add(new Token(TipoToken.Y,"y",null, linea));
                        posicion++; 
                    }
                    else if(currentChar == 'o') //Palabra reservada O
                    {
                        tokens.add(new Token(TipoToken.O,"o",null, linea));
                        posicion++; 
                    }
                    else if(currentChar == ' ')
                    {
                        posicion++;
                        estado = 0;
                    }

                    else if(Character.isDigit(currentChar))
                    {
                        lexema = lexema + currentChar;
                        estado = 5;
                        posicion++;
                    }

                    else if(currentChar == '"')
                    {
                        lexema = lexema + currentChar;
                        posicion++;
                        estado = 6;
                    }
                    
                    else if(Character.isAlphabetic(currentChar))
                    {
                        lexema = lexema + currentChar;
                        posicion++;
                        estado = 11;
                    }
                    else if(currentChar == '\r' || currentChar == '\n')
                    {
                        posicion++;
                    }
                    else{
                        lexema = lexema + currentChar;
                        posicion++;
                        estado = 0;
                    }

                    break;   //Caso 0


                case 1:    //Signos del lenguaje ! y !=
                    if(currentChar == '=') //Signo del lenguaje !=
                    {
                        tokens.add(new Token(TipoToken.OP_DIFERENTE,"!=",null, linea));
                        posicion++;
                        estado=0;
                    }
                    else                    //Signo del lenguaje !
                    {    
                        tokens.add(new Token(TipoToken.ADMIRACION,"!",null, linea));
                        estado=0;
                    }
                    break;   //Caso1

                case 2:   //Signos del lenguaje = y ==
                    if(currentChar == '=')//Signo del lenguaje ==
                    {
                        tokens.add(new Token(TipoToken.OP_IGUAL_QUE,"==",null, linea)); 
                        posicion++;
                        estado=0;
                    }
                    else                   //Signo del lenguaje =
                    {
                        tokens.add(new Token(TipoToken.IGUAL_QUE,"=",null, linea));
                        estado=0;
                    }
                    break;   //Caso 2

                case 3:   //Signos del lenguaje < y <=
                    if(currentChar == '=') //Signo del lenguaje <=
                    {
                        tokens.add(new Token(TipoToken.OP_MENOR_IGUAL_QUE,"<=",null, linea)); 
                        posicion++;
                        estado=0;
                    }
                    else                //Signo del lenguaje <
                    {
                        tokens.add(new Token(TipoToken.MENOR_QUE,"<",null, linea));                        
                        estado=0;
                    }
                    break;   //Caso 3

                case 4:    //Signos del lenguaje > y >=
                    if(currentChar == '=')  //Signo del lenguaje >=
                    {
                        tokens.add(new Token(TipoToken.OP_MAYOR_IGUAL_QUE,">=",null, linea)); 
                        posicion++;
                        estado=0;
                    }
                    else                    //Signo del lenguaje >
                    {
                        tokens.add(new Token(TipoToken.MAYOR_QUE,">",null, linea));
                        estado=0;
                    }    
                    break;      //Caso 4

                case 5: //Numeros
                    if(Character.isDigit(currentChar))
                    {
                        lexema = lexema + currentChar;
                        posicion++;
                        estado=5;
                    }else if(currentChar == '.'){
                        lexema = lexema + currentChar;
                        posicion++;
                        estado=5;
                    }else{
                        tokens.add(new Token(TipoToken.NUMERO,lexema,null, linea));
                        lexema = "";
                        estado=0;
                    }
                    break; //Numeros 

                case 6: //Cadenas " "
                    if(currentChar == '"'){
                        lexema = lexema + currentChar;
                        tokens.add(new Token(TipoToken.CADENA,lexema,null, linea));
                        posicion++;
                        lexema = "";
                        estado = 0;
                    }else{
                        lexema = lexema + currentChar;
                        posicion++;
                        estado = 6;
                    }
                    break; //Case 6
                    
                case 7:
                    if(currentChar == '/'){ 
                        posicion++;
                        estado = 8;
                    }else if(currentChar == '*'){
                        posicion++;
                        estado = 9;
                    }else{      //Signo del lenguaje /
                        tokens.add(new Token(TipoToken.BARRA_INCLINADA,"/",null,linea));
                        estado = 0;
                    }
                    break; //Case 7
                    
                case 8:        //Comentarios tipo //
                    if(currentChar != '\0'){ 
                        posicion++;
                        estado = 8;
                    }else{
                        estado = 0;
                    }
                    break; //Case 8

                case 9:
                    if(currentChar == '*'){ 
                        posicion++;
                        estado = 10;
                    }else{
                        posicion++;
                        estado = 9;
                    }
                    break; //Case 9

                case 10:       //Comentarios tipo /* */
                    if(currentChar == '/'){ 
                        posicion++;
                        estado = 0;
                    }else{
                        posicion++;
                        estado = 9;
                    }
                    break; //Case 10

                case 11:       //Identificadores
                    if(Character.isAlphabetic(currentChar) || Character.isDigit(currentChar)){ 
                        lexema = lexema + currentChar;
                        posicion++;
                        estado = 11;
                    }else{ 
                        TipoToken tipoToken = palabrasReservadas.get(lexema);
                        if(tipoToken == null){
                            tokens.add(new Token(TipoToken.IDENTIFICADOR, lexema, null,linea));
                            lexema = "";
                            estado = 0;
                        }
                        else{
                            tokens.add(new Token(tipoToken, lexema, null, linea));
                            lexema = "";
                            estado = 0;
                        }
                        estado = 0;
                    }
                    break; //Case 11
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