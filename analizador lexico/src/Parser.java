import java.util.List;

public class Parser {

    private final List<Token> tokens;
    private final Token Y = new Token(TipoToken.Y, "Y",null);
    private final Token CLASE = new Token(TipoToken.CLASE, "CLASE",null);
    private final Token ADEMAS = new Token(TipoToken.ADEMAS, "ADEMAS",null);
    private final Token FALSO = new Token(TipoToken.FALSO, "FALSO",null);
    private final Token PARA = new Token(TipoToken.PARA, "PARA",null);
    private final Token FUN = new Token(TipoToken.FUN, "FUN",null);
    private final Token SI = new Token(TipoToken.SI, "SI",null);
    private final Token NULO = new Token(TipoToken.NULO, "NULO",null);
    private final Token O = new Token(TipoToken.O, "O",null);
    private final Token IMPRIMIR = new Token(TipoToken.IMPRIMIR, "IMPRIMIR",null);
    private final Token RETORNAR = new Token(TipoToken.RETORNAR, "RETORNAR",null);
    private final Token SUPER = new Token(TipoToken.SUPER, "SUPER",null);
    private final Token ESTE = new Token(TipoToken.ESTE, "ESTE",null);
    private final Token VERDADERO = new Token(TipoToken.VERDADERO, "VERDADERO",null);
    private final Token VAR = new Token(TipoToken.VAR, "VAR",null);
    private final Token MIENTRAS = new Token(TipoToken.MIENTRAS, "MIENTRAS",null);
    private final Token IDENTIFICADOR = new Token(TipoToken.IDENTIFICADOR, "",null);
    private final Token CADENA = new Token(TipoToken.CADENA, "",null); 
    private final Token NUMERO = new Token(TipoToken.NUMERO, "",null);

    private final Token PARENTESIS_DERECHO = new Token(TipoToken.PARENTESIS_DERECHO, "(",null);
    private final Token PARENTESIS_IZQUIERDO = new Token(TipoToken.PARENTESIS_IZQUIERDO, ")",null);
    private final Token LLAVE_DERECHA = new Token(TipoToken.LLAVE_DERECHA, "{",null);
    private final Token LLAVE_IZQUIERDA = new Token(TipoToken.LLAVE_IZQUIERDA, "}",null);
    private final Token COMA = new Token(TipoToken.COMA, ",",null);
    private final Token PUNTO = new Token(TipoToken.PUNTO, ".",null);
    private final Token PUNTO_COMA = new Token(TipoToken.PUNTO_COMA, ";",null);
    private final Token GUION_MEDIO = new Token(TipoToken.GUION_MEDIO, "-",null);
    private final Token MAS = new Token(TipoToken.MAS, "+",null);
    private final Token ASTERISCO = new Token(TipoToken.ASTERISCO, "*",null);
    private final Token BARRA_INCLINADA = new Token(TipoToken.BARRA_INCLINADA, "/",null);
    private final Token ADMIRACION = new Token(TipoToken.ADMIRACION, "!",null);
    private final Token OP_DIFERENTE = new Token(TipoToken.OP_DIFERENTE, "!=",null);
    private final Token IGUAL_QUE = new Token(TipoToken.IGUAL_QUE, "=",null);
    private final Token OP_IGUAL_QUE = new Token(TipoToken.OP_IGUAL_QUE, "==",null);
    private final Token MENOR_QUE = new Token(TipoToken.MENOR_QUE, "<",null);
    private final Token OP_MENOR_IGUAL_QUE = new Token(TipoToken.OP_MENOR_IGUAL_QUE, "<=",null);
    private final Token MAYOR_QUE = new Token(TipoToken.MAYOR_QUE, ">",null);
    private final Token OP_MAYOR_IGUAL_QUE = new Token(TipoToken.OP_MAYOR_IGUAL_QUE, ">=",null);
    private final Token EOF = new Token(TipoToken.EOF, "",null);

    private int i = 0;
    private boolean hayErrores = false;

    private Token preanalisis;

    public Parser(List<Token> tokens){
        this.tokens = tokens;
    }

    public void parse(){
        i = 0;
        preanalisis = tokens.get(i);

            PROGRAM();

            if(!hayErrores && !preanalisis.equals(EOF)){
                System.out.println("Error en la posición " + preanalisis.posicion + ". No se esperaba el token " + preanalisis.tipo);
            }
            else if(!hayErrores && preanalisis.equals(EOF)){
                System.out.println(" Lo que ingresaste es válido. ");
        
            }
            siguienteToken();
    }

    private void siguienteToken() {
        i++;
        if (i < tokens.size()) {
            preanalisis = tokens.get(i);
        } else {
            preanalisis = new Token(TipoToken.EOF, "", -1);
        }
    }

    void PROGRAM(){
        DECLARATION();
    }
 
//////////////////////////////////DECLARACIONES///////////////////////////////////////////////////////////////
    
    void DECLARATION(){ 
        if (preanalisis.equals(CLASE)) {            CLASS_DECL();        }
        else if(preanalisis.equals(FUN)){            FUN_DECL();        }
        else if(preanalisis.equals(VAR)){            VAR_DECL();        }
        else if(preanalisis.equals(EOF)){            return;        }    
        else{               STATEMENT();            }
    } 

    void CLASS_DECL(){       //clase id CLASS_INHER { FUNCTIONS }
        if(hayErrores) return;
            coincidir(CLASE);
            coincidir(IDENTIFICADOR);
            CLASS_INHER();
            coincidir(LLAVE_DERECHA);
            FUNCTIONS();
            coincidir(LLAVE_IZQUIERDA);
    }

    void CLASS_INHER(){     //   < id        EOF
        if(hayErrores) return;
        if(preanalisis.equals(MENOR_QUE)){
            coincidir(MENOR_QUE);
            coincidir(IDENTIFICADOR);
        }
        if(preanalisis.equals(EOF)){
            return;
        }
    }

    void FUN_DECL(){         //fun FUNCTION
        if(hayErrores) return;
            coincidir(FUN);
            FUNCTION();
    }

    void VAR_DECL(){     //var id  VAR_INIT  ;
        if(hayErrores) return;
            coincidir(VAR);
            coincidir(IDENTIFICADOR);
            VAR_INIT();
            coincidir(PUNTO_COMA);
    }

    void VAR_INIT(){     //   = numero
        if(hayErrores) return;
        if(preanalisis.equals(IGUAL_QUE)){
            coincidir(IGUAL_QUE);
            EXPRESSION();
        }
    }

////////////////////////////////////SENTENCIAS///////////////////////////////////////////////////////////////////
    void STATEMENT(){ // EXPR_STMT | FOR_STMT | IF_STMT | PRINT_STMT | RETURN_STMT | WHILE_STMT  | BLOCK
        if(hayErrores) return;
        else if(preanalisis.equals(PARA)){
            FOR_STMT();
        }else if(preanalisis.equals(SI)){
            IF_STMT();
        }else if(preanalisis.equals(IMPRIMIR)){
            PRINT_STMT();
        }else if(preanalisis.equals(RETORNAR)){
            RETURN_STMT();
        }else if(preanalisis.equals(MIENTRAS)){
            WHILE_STMT();
        }else if(preanalisis.equals(LLAVE_DERECHA)){
            BLOCK();
        }else{
            EXPR_STMT(); 
        }
    }

    void EXPR_STMT(){     // EXPRESSION ;
        if(hayErrores) return;
            EXPRESSION();
            coincidir(PUNTO_COMA);
    }

    void FOR_STMT(){     // for ( FOR_STMT_1  FOR_STMT_2  FOR_STMT_3 ) STATEMENT
        if(hayErrores) return;
            coincidir(PARA);
            coincidir(PARENTESIS_DERECHO);
            FOR_STMT_1();
            FOR_STMT_2();
            FOR_STMT_3();
            coincidir(PARENTESIS_IZQUIERDO);
            STATEMENT();
    }

    void FOR_STMT_1(){      //VAR_DECL / ; / EXPR_STMT
        if(hayErrores) return;
        if(preanalisis.equals(VAR)){
            VAR_DECL();
        }else if(preanalisis.equals(PUNTO_COMA)){
            coincidir(PUNTO_COMA);
        }else{
            EXPR_STMT();
        }
    }

    void FOR_STMT_2(){      // ; | EXPRESSION;
        if(hayErrores) return;
        if(preanalisis.equals(PUNTO_COMA)){
            coincidir(PUNTO_COMA);
        }else{
            EXPRESSION();
            coincidir(PUNTO_COMA);
        }
    }

    void FOR_STMT_3(){     //   ; | EXPRESSION |  EOF
        if(hayErrores) return;
        if(!preanalisis.equals(PARENTESIS_IZQUIERDO)){
            EXPRESSION();
        }
    }

    void IF_STMT(){    // if (EXPRESSION) STATEMENT ELSE_STATEMENT
        if(hayErrores) return;
            coincidir(SI);
            coincidir(PARENTESIS_DERECHO);
            EXPRESSION();
            coincidir(PARENTESIS_IZQUIERDO);
            STATEMENT();
            ELSE_STATEMENT();
    }

    void ELSE_STATEMENT(){     // else STATEMENT | EOF
        if(hayErrores) return;
        if(preanalisis.equals(LLAVE_DERECHA)){
            coincidir(ADEMAS);
            STATEMENT();
            coincidir(LLAVE_IZQUIERDA);
        }
        else if(preanalisis.equals(EOF)){
            return;
        }
    }

    void PRINT_STMT(){        //print STATEMENT | EOF
        if(hayErrores) return;
            coincidir(IMPRIMIR);
            EXPRESSION();
            coincidir(PUNTO_COMA);
    }

    void RETURN_STMT(){       //return RETURN_EXP_OPC ;
        if(hayErrores) return;
        if(preanalisis.equals(RETORNAR)){
            coincidir(RETORNAR);
            RETURN_EXP_OPC();     //algo
            coincidir(PUNTO_COMA);
        }else{
            hayErrores = true;
            System.out.println("Error en la posición " + preanalisis.posicion + ". Se esperaba RETURN");
        }
    }

    void RETURN_EXP_OPC(){    //  EXPRESSION | EOF
        if(hayErrores) return;
        if(!preanalisis.equals(PUNTO_COMA)){
            EXPRESSION();       
        }
        else if(preanalisis.equals(EOF)){
            return;
        }
        else{
            hayErrores = true;
            System.out.println("Error en la posición " + preanalisis.posicion + ". No se esperaba ;");
        }
    }

    void WHILE_STMT(){        //while(EXPRESIION) STATEMENT
        if(hayErrores) return;
            coincidir(MIENTRAS);
            coincidir(PARENTESIS_DERECHO);
            EXPRESSION();
            coincidir(PARENTESIS_IZQUIERDO);
            STATEMENT();
    }

    void BLOCK(){       // { BLOCK_DECL }   
        if(hayErrores) return;
        if(preanalisis.equals(LLAVE_DERECHA)){
            coincidir(LLAVE_DERECHA);
            BLOCK_DECL();
            coincidir(LLAVE_IZQUIERDA);
        }else{
            hayErrores = true;
            System.out.println("Error en la posición " + preanalisis.posicion + ". Se esperaba {");
        }
    }

    void BLOCK_DECL(){   // DECLARATION BLOCK_DECL    EOF
        if(hayErrores) return;

        DECLARATION();
        if(!preanalisis.equals(LLAVE_IZQUIERDA)){
            BLOCK_DECL();
        }
        else if(preanalisis.equals(EOF)){
            return;
        }
    }


//////////////////////////////////////////EXPRESIONES////////////////////////////////////////////////////////////////
    void EXPRESSION(){
        if(hayErrores) return;
        ASSIGNMENT();
    }

    void ASSIGNMENT(){     // LOGIC_OR ASSIGNMENT_OPC
        if(hayErrores) return;
            LOGIC_OR();
            ASSIGNMENT_OPC();
    }

    void ASSIGNMENT_OPC(){        // = ASSIGNMENT | EOF
        if(hayErrores) return;
        if(preanalisis.equals(IGUAL_QUE)){
            coincidir(IGUAL_QUE);
            EXPRESSION();
         }else if(preanalisis.equals(EOF)){
            return;
         }
         else{
         }
    }

    void LOGIC_OR(){    //LOGIC_AND LOGIC_OR_2
        if(hayErrores) return;
            LOGIC_AND();
            LOGIC_OR_2();
    }

    void LOGIC_OR_2(){         //  or LOGIC_AND  LOGIC_OR_2  |  EOF
        if(hayErrores) return;
        if(preanalisis.equals(O)){
            coincidir(O);
            LOGIC_AND();
            LOGIC_OR_2();
         }else if(preanalisis.equals(EOF)){
            return;
         }
         else{
         }
    }

    void LOGIC_AND(){      // EQUALITY LOGIC_AND_2
        if(hayErrores) return;
            EQUALITY();
            LOGIC_AND_2();
    }

    void LOGIC_AND_2(){        // and EQUALITY LOGIC_AND_2 | EOF
        if(hayErrores) return;
        if(preanalisis.equals(ADEMAS)){
            coincidir(ADEMAS);
            EQUALITY();
            LOGIC_AND_2();
         }else if(preanalisis.equals(EOF)){
            return;
         }
         else{}
    }

    void EQUALITY(){      //  COMPARISON EQUALITY_2 
        if(hayErrores) return;
        COMPARISON();
        EQUALITY_2();
    }

    void EQUALITY_2(){     // != COMPARISON EQUALITY_2  |  == COMPARISON EQUALITY_2 | EOF
        if(hayErrores) return;
        if(preanalisis.equals(OP_DIFERENTE) || preanalisis.equals(OP_IGUAL_QUE)){
            coincidir(preanalisis);
            COMPARISON();
            EQUALITY_2();
         }else if (preanalisis.equals(EOF)){
            return;
         }
         else{   }         
    }

    void COMPARISON(){    // TERM COMPARISON_2
        if(hayErrores) return;
            TERM();
            COMPARISON_2();
    }

    void COMPARISON_2(){   // > TERM COMPARISON_2 | >= TERM COMPARISON_2 | < TERM COMPARISON_2 | <= TERM COMPARISON_2 | EOF
        if(hayErrores) return;
        if(preanalisis.equals(MAYOR_QUE) || preanalisis.equals(OP_MENOR_IGUAL_QUE) || preanalisis.equals(MAYOR_QUE) || preanalisis.equals(OP_MAYOR_IGUAL_QUE) ){
            coincidir(preanalisis);
            TERM();
            COMPARISON_2();
         }else if(preanalisis.equals(EOF)){
            return;
         }
         else{
        }
    }

    void TERM(){     // FACTOR TERM_2
        if(hayErrores) return;
        FACTOR();
            TERM_2();
    }

    void TERM_2(){     // - FACTOR TERM_2  | + TERM_2 | EOF
        if(hayErrores) return;
        if(preanalisis.equals(GUION_MEDIO) || preanalisis.equals(MAS) ){
            coincidir(preanalisis);
            FACTOR();
            TERM_2();
         }else{}
    }

    void FACTOR(){   // UNARY FACTOR_2
        if(hayErrores) return;
            UNARY();
            FACTOR_2();
    }

    void FACTOR_2(){      //  / UNARY FACTOR_2 |  *UNARY FACTOR_2  | EOF
        if(hayErrores) return;
        if(preanalisis.equals(BARRA_INCLINADA) || preanalisis.equals(ASTERISCO)){
            coincidir(preanalisis);
            UNARY();
            FACTOR_2();
         }else if(preanalisis.equals(EOF)){
            return;
         }
         else{
        }
    }

    void UNARY(){    // ! UNARY FACTOR_2   | - UNARY FACTOR_2  / CALL
        if(hayErrores) return;
        if(preanalisis.equals(ADMIRACION) || preanalisis.equals(GUION_MEDIO)){
            coincidir(preanalisis);
            UNARY();
         }else{
            CALL();
        }
    }

    void CALL(){  //  PRIMARY CALL_2
        if(hayErrores) return;
            PRIMARY();
            CALL_2();
    }

    void CALL_2(){  //(ARGUMENTS_OPC) CALL_2 | .id CALL_2 | EOF
        if(hayErrores) return;
        if(preanalisis.equals(PARENTESIS_DERECHO)){
            coincidir(PARENTESIS_DERECHO);
            ARGUMENTS_OPC();
            coincidir(PARENTESIS_IZQUIERDO);
            CALL_2();
        }else if(preanalisis.equals(PUNTO)){
            coincidir(PUNTO);
            coincidir(IDENTIFICADOR);
            CALL_2();
        }else if(preanalisis.equals(EOF)){
            return;
        }
        else{
        }
    }

    void CALL_OPC(){   // CALL .  | EOF
        if(hayErrores) return;
        if(preanalisis.equals(PUNTO)){
            coincidir(PUNTO);
            coincidir(IDENTIFICADOR);
            CALL_OPC();
        }else{
            hayErrores = true;
            System.out.println("Error en la posición " + preanalisis.posicion + ". Se esperaba .");
        }
    }

    void PRIMARY(){   // true | false | null | this | number | string | id | (EXPRESSION) | super . id
        if(hayErrores) return;
        if(preanalisis.equals(VERDADERO) || preanalisis.equals(FALSO) || preanalisis.equals(NULO) || preanalisis.equals(ESTE) || preanalisis.equals(NUMERO) || preanalisis.equals(CADENA) || preanalisis.equals(IDENTIFICADOR)){
            coincidir(preanalisis);
        }else if(preanalisis.equals(PARENTESIS_DERECHO)){
            coincidir(PARENTESIS_DERECHO);
            EXPRESSION();
            coincidir(PARENTESIS_IZQUIERDO);
        }else if(preanalisis.equals(SUPER)){
            coincidir(SUPER);
            coincidir(PUNTO);
            coincidir(IDENTIFICADOR);
        }else{
            hayErrores = true;
            System.out.println("Error en la posición " + preanalisis.posicion + ". Se esperaba VERDADERO, FALSO, NULO, ESTE, NUMERO, CADENA, IDENTIFICADOR");
        }
    }


/////////////////////////////////////////OTRAS////////////////////////////////////////////////////////////////////////////
    void FUNCTION(){      // id (PARAMETERS_OPC) BLOCK
        if(hayErrores) return;
            coincidir(IDENTIFICADOR);
            coincidir(PARENTESIS_DERECHO);
            PARAMETERS_OPC();
            coincidir(PARENTESIS_IZQUIERDO);
            BLOCK();
    }

    void FUNCTIONS(){       // FUNCTION FUNCTIONS | EOF
        if(hayErrores) return;
        if(preanalisis.equals(IDENTIFICADOR)){
            FUNCTION();
            FUNCTIONS();
        }else if(preanalisis.equals(EOF)){
            return;
        }
        else{ }
    }

    void PARAMETERS_OPC(){       // PARAMETERS | EOF
        if(hayErrores) return;
        if(preanalisis.equals(IDENTIFICADOR)){
            PARAMETERS();
        }else if(preanalisis.equals(EOF)){
            return;
        }        
        else{
        }
    }

    void PARAMETERS(){      // id PARAMETERS_2
        if(hayErrores) return;
            coincidir(IDENTIFICADOR);
            PARAMETERS_2();
    }

    void PARAMETERS_2(){   // , id PARAMETERS_2 | EOF
        if(hayErrores) return;
        if(preanalisis.equals(COMA)){
            coincidir(COMA);
            coincidir(IDENTIFICADOR);
            PARAMETERS_2();
        }else if(preanalisis.equals(EOF)){
            return;
        }
        else{
        }
    }

    void ARGUMENTS_OPC(){    // ARGUMENTS | EOF
        if(hayErrores) return;

        if(preanalisis.equals(IDENTIFICADOR) || preanalisis.equals(VERDADERO) || preanalisis.equals(FALSO) || preanalisis.equals(NULO)
            || preanalisis.equals(NUMERO) || preanalisis.equals(CADENA) || preanalisis.equals(PARENTESIS_DERECHO) || preanalisis.equals(SUPER)){
                ARGUMENTS();
        }else if(preanalisis.equals(EOF)){
            return;
        }
        else{
        }
    }

    void ARGUMENTS(){  // EXPRESSION ARGUMENTS_2
        if(hayErrores) return;
            EXPRESSION();
            ARGUMENTS_2();
    }

    void ARGUMENTS_2(){   // , EXPRESSION ARGUMENTS_2 | EOF
        if(hayErrores) return;
        if(preanalisis.equals(COMA)){
            coincidir(COMA);
            EXPRESSION();
            ARGUMENTS_2();
        }else if(preanalisis.equals(EOF)){
            return;
        }
        else{
        }
    }

    void coincidir(Token t){
        if(hayErrores) return;

        if(preanalisis.tipo == t.tipo){
            i++;
            preanalisis = tokens.get(i);
        }else{
            hayErrores = true;
            System.out.println("Error en la posición " + preanalisis.posicion + ". Se esperaba un  " + t.tipo);
        }
    }
}