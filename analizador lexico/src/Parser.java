import java.util.List;

public class Parser {

    private final List<Token> tokens;
    boolean valido = true;
    private int i=0;
    
    private Boolean hayerrores = false;
    private Token preanalisis;
    
    public Parser(List<Token> tokens){
        this.tokens = tokens;
    }

    void parse(){
        i = 0;
        preanalisis = tokens.get(i);
        PROGRAM();
        if(!hayerrores && preanalisis.tipo != TipoToken.EOF){
            System.out.println("Error en la posición " + preanalisis.posicion + ". No se esperaba el token " + preanalisis.tipo);
        }
        else if(!hayerrores && preanalisis.tipo == TipoToken.EOF){
            System.out.println("Lo que ingresaste es válido");
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
        if(hayerrores) return;
        DECLARATION();
    }

//////////////////////////////////DECLARACIONES///////////////////////////////////////////////////////////////
    void DECLARATION () {     //CLASS_DECL DECLARATION | FUN_DECL DECLARATION | VAR_DECL DECLARATION | STATEMENT DECLATAION | EOF
        if(hayerrores) return;
        if(preanalisis.tipo == TipoToken.CLASE){   
            CLASS_DECL();   
            DECLARATION();  
        }
        else if(preanalisis.tipo == TipoToken.FUN){  
            FUN_DECL();  
            DECLARATION();  
        }
        else if(preanalisis.tipo == TipoToken.VAR){   
            VAR_DECL();  
            DECLARATION();  
        }
        else if(preanalisis.tipo == TipoToken.ADMIRACION || preanalisis.tipo == TipoToken.GUION_MEDIO || preanalisis.tipo == TipoToken.VERDADERO 
        || preanalisis.tipo == TipoToken.FALSO  || preanalisis.tipo == TipoToken.NULO || preanalisis.tipo == TipoToken.ESTE
        || preanalisis.tipo == TipoToken.NUMERO || preanalisis.tipo == TipoToken.CADENA || preanalisis.tipo == TipoToken.IDENTIFICADOR 
        || preanalisis.tipo == TipoToken.PARENTESIS_IZQUIERDO || preanalisis.tipo == TipoToken.SUPER || preanalisis.tipo == TipoToken.PARA 
        || preanalisis.tipo == TipoToken.SI  || preanalisis.tipo == TipoToken.IMPRIMIR || preanalisis.tipo == TipoToken.RETORNAR
        || preanalisis.tipo == TipoToken.MIENTRAS || preanalisis.tipo == TipoToken.LLAVE_IZQUIERDA  || preanalisis.tipo == TipoToken.MAS){
            STATEMENT();
            DECLARATION();
        }
    }

    void CLASS_DECL(){   //clase id CLASS_INHER { FUNCTIONS }
        if(hayerrores) return; 
        if(TipoToken.CLASE == preanalisis.tipo)
        {
            coincidir(TipoToken.CLASE);
            coincidir(TipoToken.IDENTIFICADOR);
            CLASS_INHER();
            coincidir(TipoToken.LLAVE_IZQUIERDA);
            FUNCTIONS();
            coincidir(TipoToken.LLAVE_DERECHA);    
        }
        else{
            hayerrores = true;
            System.out.println("Error en la funcion CLASS_DECL, en la posición " + preanalisis.posicion + ". No se esperaba el token " + preanalisis.tipo);
        }
    }

    void CLASS_INHER(){     //   < id  | EOF
        if(hayerrores) return;
        if(preanalisis.tipo == TipoToken.MENOR_QUE)
        {
            coincidir(TipoToken.MENOR_QUE);
            coincidir(TipoToken.IDENTIFICADOR);
        }
    }

    void FUN_DECL (){    //fun FUNCTION
        if(hayerrores) return;
        if(TipoToken.FUN == preanalisis.tipo)
        {
            coincidir(TipoToken.FUN);
            FUNCTION();
        }
        else{
            hayerrores = true;
            System.out.println("Error en la funcion FUN_DECL, en la posición " + preanalisis.posicion + ". No se esperaba el token " + preanalisis.tipo);
        }
    }

    void VAR_DECL(){    //var id  VAR_INIT  ;
        if(hayerrores) return;
        if(TipoToken.VAR == preanalisis.tipo)
        {
            coincidir(TipoToken.VAR);
            coincidir(TipoToken.IDENTIFICADOR);
            VAR_INIT();    
            coincidir(TipoToken.PUNTO_COMA);        
        }
        else{
            hayerrores = true;
            System.out.println("Error en la funcion VAR_DECL, en la posición " + preanalisis.posicion + ". No se esperaba el token " + preanalisis.tipo);
        }
        
    }

    void VAR_INIT() {   //   = EXPRESSION | EOF
        if(hayerrores) return;
        if(preanalisis.tipo == TipoToken.IGUAL_QUE){
            coincidir(TipoToken.IGUAL_QUE);
            EXPRESSION();
        }
    }


////////////////////////////////////SENTENCIAS///////////////////////////////////////////////////////////////////    
    void STATEMENT(){  // EXPR_STMT | FOR_STMT | IF_STMT | PRINT_STMT | RETURN_STMT | WHILE_STMT  | BLOCK
        if(hayerrores) return;
        if(preanalisis.tipo == TipoToken.ADMIRACION || preanalisis.tipo == TipoToken.GUION_MEDIO || preanalisis.tipo == TipoToken.VERDADERO 
        || preanalisis.tipo == TipoToken.FALSO  || preanalisis.tipo == TipoToken.NULO || preanalisis.tipo == TipoToken.ESTE
        || preanalisis.tipo == TipoToken.NUMERO || preanalisis.tipo == TipoToken.CADENA || preanalisis.tipo == TipoToken.IDENTIFICADOR 
        || preanalisis.tipo == TipoToken.PARENTESIS_IZQUIERDO || preanalisis.tipo == TipoToken.SUPER || preanalisis.tipo == TipoToken.MAS){
            EXPR_STMT();
        }
        else if(preanalisis.tipo == TipoToken.PARA){
            FOR_STMT();
        }
        else if(preanalisis.tipo == TipoToken.SI){
            IF_STMT();
        }
        else if(preanalisis.tipo == TipoToken.IMPRIMIR){
            PRINT_STMT();
        }
        else if(preanalisis.tipo == TipoToken.RETORNAR){
            RETURN_STMT();
        }
        else if(preanalisis.tipo == TipoToken.MIENTRAS)
        {
            WHILE_STMT();
        }
        else if(preanalisis.tipo == TipoToken.LLAVE_IZQUIERDA){
            BLOCK();
        }
        else{
            hayerrores = true;
                System.out.println("Error en la funcion STATEMENT, en la posición " + preanalisis.posicion + ". No se esperaba el token " + preanalisis.tipo);
        }
    }

    void EXPR_STMT(){  // EXPRESSION ;
        if(hayerrores) return;
        if (preanalisis.tipo == TipoToken.ADMIRACION || preanalisis.tipo == TipoToken.GUION_MEDIO || preanalisis.tipo == TipoToken.VERDADERO 
        || preanalisis.tipo == TipoToken.FALSO  || preanalisis.tipo == TipoToken.NULO || preanalisis.tipo == TipoToken.ESTE 
        || preanalisis.tipo == TipoToken.NUMERO || preanalisis.tipo == TipoToken.CADENA  || preanalisis.tipo == TipoToken.IDENTIFICADOR 
        || preanalisis.tipo == TipoToken.PARENTESIS_IZQUIERDO  || preanalisis.tipo == TipoToken.SUPER || preanalisis.tipo == TipoToken.MAS)
        {
            EXPRESSION();
            coincidir(TipoToken.PUNTO_COMA);
        }
        else{
            hayerrores=true;
            System.out.println("Error en la funcion EXPR_STMT, en la posición " + preanalisis.posicion + ". No se esperaba el token " + preanalisis.tipo);
        }
    }

    void FOR_STMT(){   // for ( FOR_STMT_1  FOR_STMT_2  FOR_STMT_3 ) STATEMENT
        if(hayerrores) return;
        if (TipoToken.PARA == preanalisis.tipo)
        {
            coincidir(TipoToken.PARA);
            coincidir(TipoToken.PARENTESIS_IZQUIERDO);
            FOR_STMT_1();
            FOR_STMT_2();
            FOR_STMT_3();
            coincidir(TipoToken.PARENTESIS_DERECHO);
            STATEMENT();
        }
        else{
            hayerrores=true;
            System.out.println("Error en la funcion FOR_STMT, en la posición " + preanalisis.posicion + ". No se esperaba el token " + preanalisis.tipo);
        }
    }

    void FOR_STMT_1(){   //VAR_DECL | EXPR_STMT | ;
        if(hayerrores) return;
        if(preanalisis.tipo == TipoToken.VAR)
        {
            VAR_DECL();
        }
        else if(preanalisis.tipo == TipoToken.ADMIRACION || preanalisis.tipo == TipoToken.GUION_MEDIO || preanalisis.tipo == TipoToken.VERDADERO 
        || preanalisis.tipo == TipoToken.FALSO  || preanalisis.tipo == TipoToken.NULO || preanalisis.tipo == TipoToken.ESTE
        || preanalisis.tipo == TipoToken.NUMERO || preanalisis.tipo == TipoToken.CADENA  || preanalisis.tipo == TipoToken.IDENTIFICADOR
         || preanalisis.tipo == TipoToken.PARENTESIS_IZQUIERDO  || preanalisis.tipo == TipoToken.SUPER || preanalisis.tipo == TipoToken.COMA){
            EXPR_STMT();
        }
        else if(preanalisis.tipo == TipoToken.PUNTO_COMA)
        {
            coincidir(TipoToken.PUNTO_COMA);
        }
        else{
            hayerrores=true;
            System.out.println("FOR_STMT1 Error en la posición " + preanalisis.posicion + ". No se esperaba el token " + preanalisis.tipo);
        }
    }

    void FOR_STMT_2(){    //  EXPRESSION; | ; 
        if(hayerrores) return;
        if(preanalisis.tipo == TipoToken.ADMIRACION || preanalisis.tipo == TipoToken.GUION_MEDIO || preanalisis.tipo == TipoToken.VERDADERO 
        || preanalisis.tipo == TipoToken.FALSO  || preanalisis.tipo == TipoToken.NULO || preanalisis.tipo == TipoToken.ESTE 
         || preanalisis.tipo == TipoToken.NUMERO || preanalisis.tipo == TipoToken.CADENA   || preanalisis.tipo == TipoToken.IDENTIFICADOR 
         || preanalisis.tipo == TipoToken.PARENTESIS_IZQUIERDO  || preanalisis.tipo == TipoToken.SUPER) 
        {
            EXPRESSION();
            coincidir(TipoToken.PUNTO_COMA);
        }
        else if(preanalisis.tipo == TipoToken.PUNTO_COMA){
            coincidir(TipoToken.PUNTO_COMA);
        }
        else{
            hayerrores=true;
            System.out.println("Error en la funcion FOR_STMT_2, en la posición " + preanalisis.posicion + ". No se esperaba el token " + preanalisis.tipo);
        }
    }

    void FOR_STMT_3(){  //  EXPRESSION |  EOF 
        if(hayerrores) return;
        if(preanalisis.tipo == TipoToken.ADMIRACION || preanalisis.tipo == TipoToken.GUION_MEDIO || preanalisis.tipo == TipoToken.VERDADERO 
        || preanalisis.tipo == TipoToken.FALSO  || preanalisis.tipo == TipoToken.NULO || preanalisis.tipo == TipoToken.ESTE
        || preanalisis.tipo == TipoToken.NUMERO || preanalisis.tipo == TipoToken.CADENA || preanalisis.tipo == TipoToken.IDENTIFICADOR 
        || preanalisis.tipo == TipoToken.PARENTESIS_DERECHO || preanalisis.tipo == TipoToken.SUPER)
        {
            EXPRESSION();
        }
    }

    void IF_STMT(){   // if (EXPRESSION) STATEMENT ELSE_STATEMENT
        if(hayerrores) return;
        if(TipoToken.SI == preanalisis.tipo)
        {
            coincidir(TipoToken.SI);
            coincidir(TipoToken.PARENTESIS_IZQUIERDO);
            EXPRESSION();
            coincidir(TipoToken.PARENTESIS_DERECHO);
            STATEMENT();
            ELSE_STATEMENT();
        }
        else{
            hayerrores=true;
            System.out.println("Error en la funcion IF_STMT, en la posición " + preanalisis.posicion + ". No se esperaba el token " + preanalisis.tipo);
        }
    }

    void ELSE_STATEMENT (){     // else STATEMENT | EOF
        if(hayerrores) return;
        if(preanalisis.tipo == TipoToken.ADEMAS){
            coincidir(TipoToken.ADEMAS);
            STATEMENT();
        }
    }

    void PRINT_STMT (){     //print STATEMENT | EOF
        if(hayerrores) return;
        if(TipoToken.IMPRIMIR == preanalisis.tipo)
        {
            coincidir(TipoToken.IMPRIMIR);
            EXPRESSION();        
            coincidir(TipoToken.PUNTO_COMA);
        }
        else{
            hayerrores=true;
            System.out.println("Error en la funcion PRINT_STMT, en la posición " + preanalisis.posicion + ". No se esperaba el token " + preanalisis.tipo); 
        }
    }

    void RETURN_STMT (){        //return RETURN_EXP_OPC ;
        if(hayerrores) return;
        if(TipoToken.RETORNAR == preanalisis.tipo)
        {
            coincidir(TipoToken.RETORNAR);
            RETURN_EXP_OPC();
            coincidir(TipoToken.PUNTO_COMA);
        }
        else{
            hayerrores=true;
            System.out.println("Error en la funcion RETURN_STMT, en la posición " + preanalisis.posicion + ". No se esperaba el token " + preanalisis.tipo); 
        }

    }
    void RETURN_EXP_OPC(){  //  EXPRESSION | EOF
        if(hayerrores) return;
        if(preanalisis.tipo == TipoToken.ADMIRACION || preanalisis.tipo == TipoToken.GUION_MEDIO || preanalisis.tipo == TipoToken.VERDADERO 
        || preanalisis.tipo == TipoToken.FALSO   || preanalisis.tipo == TipoToken.NULO || preanalisis.tipo == TipoToken.ESTE
        || preanalisis.tipo == TipoToken.NUMERO || preanalisis.tipo == TipoToken.CADENA || preanalisis.tipo == TipoToken.IDENTIFICADOR 
        || preanalisis.tipo == TipoToken.PARENTESIS_IZQUIERDO  || preanalisis.tipo == TipoToken.SUPER){
            EXPRESSION();
        }
    }

    void WHILE_STMT(){       //while(EXPRESIION) STATEMENT
        if(hayerrores) return;
        if(TipoToken.MIENTRAS == preanalisis.tipo)
        {
            coincidir(TipoToken.MIENTRAS);
            coincidir (TipoToken.PARENTESIS_IZQUIERDO);
            EXPRESSION ();
            coincidir(TipoToken.PARENTESIS_DERECHO);
            STATEMENT();            
        }
        else{
            hayerrores=true;
            System.out.println("Error en la funcion WHILE_STMT, en la posición " + preanalisis.posicion + ". No se esperaba el token " + preanalisis.tipo);
        }
    }

    void BLOCK(){        // { BLOCK_DECL }  
        if(hayerrores) return;
        if(TipoToken.LLAVE_IZQUIERDA == preanalisis.tipo)
        {
            coincidir(TipoToken.LLAVE_IZQUIERDA);
            BLOCK_DECL();
            coincidir(TipoToken.LLAVE_DERECHA);            
        }
        else{
            hayerrores=true;
            System.out.println("Error en la funcion BLOCK, en la posición " + preanalisis.posicion + ". No se esperaba el token " + preanalisis.tipo);
        }
    }

    void BLOCK_DECL (){     // DECLARATION BLOCK_DECL |  EOF
        if(hayerrores) return;
        if(preanalisis.tipo == TipoToken.CLASE || preanalisis.tipo == TipoToken.FUN || preanalisis.tipo == TipoToken.VAR   
        || preanalisis.tipo == TipoToken.ADMIRACION || preanalisis.tipo == TipoToken.GUION_MEDIO || preanalisis.tipo == TipoToken.VERDADERO 
        || preanalisis.tipo == TipoToken.FALSO  || preanalisis.tipo == TipoToken.NULO || preanalisis.tipo == TipoToken.ESTE
        || preanalisis.tipo == TipoToken.NUMERO || preanalisis.tipo == TipoToken.CADENA || preanalisis.tipo == TipoToken.IDENTIFICADOR 
        || preanalisis.tipo == TipoToken.PARENTESIS_IZQUIERDO  || preanalisis.tipo == TipoToken.SUPER  || preanalisis.tipo == TipoToken.PARA 
        || preanalisis.tipo == TipoToken.SI  || preanalisis.tipo == TipoToken.IMPRIMIR  || preanalisis.tipo == TipoToken.RETORNAR 
        || preanalisis.tipo == TipoToken.MIENTRAS  || preanalisis.tipo == TipoToken.LLAVE_IZQUIERDA   || preanalisis.tipo == TipoToken.MAS){
            DECLARATION();
            BLOCK_DECL();
        }
    }

//////////////////////////////////////////EXPRESIONES////////////////////////////////////////////////////////////////
    void EXPRESSION() {     //ASSIGNMENT
        if(hayerrores) return;
        ASSIGNMENT();          
    }

    void ASSIGNMENT(){       //LOGIC_OR ASSIGNMENT_OPC
        if(hayerrores) return;
        LOGIC_OR();
        ASSIGNMENT_OPC();
    }

    void ASSIGNMENT_OPC(){    // = EXPRESSION | EOF
        if(hayerrores) return;
        if(preanalisis.tipo == TipoToken.IGUAL_QUE){
            coincidir(TipoToken.IGUAL_QUE);
            EXPRESSION();
        }
    }

    void LOGIC_OR(){   //LOGIC_AND LOGIC_OR_2
        if(hayerrores) return;
        LOGIC_AND();
        LOGIC_OR_2();        
    }

    void LOGIC_OR_2(){   // or LOGIC_AND LOGIC_OR_2 | EOF
        if(hayerrores) return;
        if(preanalisis.tipo == TipoToken.O){
            coincidir(TipoToken.O);
            LOGIC_AND();
            LOGIC_OR_2();
        }
    }

    void LOGIC_AND(){   //EQUALITY LOGIC_AND_2
        if(hayerrores) return;
        EQUALITY();
        LOGIC_AND_2();
    }

    void LOGIC_AND_2(){   //and EQUALITY LOGIC_AND_2 | EOF
        if(hayerrores) return;
        if(preanalisis.tipo == TipoToken.Y){
            coincidir(TipoToken.Y);
            EQUALITY();
            LOGIC_AND_2();
        }
    }

    void EQUALITY(){        //  COMPARISON EQUALITY_2 
        if(hayerrores) return;
        if(preanalisis.tipo == TipoToken.ADMIRACION || preanalisis.tipo == TipoToken.GUION_MEDIO  || preanalisis.tipo == TipoToken.VERDADERO 
        || preanalisis.tipo == TipoToken.FALSO  || preanalisis.tipo == TipoToken.NULO || preanalisis.tipo == TipoToken.ESTE 
        || preanalisis.tipo == TipoToken.NUMERO || preanalisis.tipo == TipoToken.CADENA  || preanalisis.tipo == TipoToken.IDENTIFICADOR
        || preanalisis.tipo == TipoToken.PARENTESIS_IZQUIERDO   || preanalisis.tipo == TipoToken.SUPER){
            COMPARISON();
            EQUALITY_2();
        }
    }

    void EQUALITY_2(){   // != COMPARISON EQUALITY_2  |  == COMPARISON EQUALITY_2 | EOF
        if(hayerrores) return;
        if(preanalisis.tipo == TipoToken.OP_DIFERENTE)
        {
            coincidir(TipoToken.OP_DIFERENTE);
            COMPARISON();
            EQUALITY_2();
        }
        else if(preanalisis.tipo == TipoToken.IGUAL_QUE)
        {
            coincidir(TipoToken.IGUAL_QUE);
            COMPARISON();
            EQUALITY_2();
        }
    }

    void COMPARISON(){       // TERM COMPARISON_2
        if(hayerrores) return;
        if(preanalisis.tipo == TipoToken.ADMIRACION || preanalisis.tipo == TipoToken.GUION_MEDIO  || preanalisis.tipo == TipoToken.VERDADERO 
        || preanalisis.tipo == TipoToken.FALSO  || preanalisis.tipo == TipoToken.NULO || preanalisis.tipo == TipoToken.ESTE  
        || preanalisis.tipo == TipoToken.NUMERO || preanalisis.tipo == TipoToken.CADENA || preanalisis.tipo == TipoToken.IDENTIFICADOR 
        || preanalisis.tipo == TipoToken.PARENTESIS_IZQUIERDO   || preanalisis.tipo == TipoToken.SUPER){
            TERM();
            COMPARISON_2();
        }
    }

    void COMPARISON_2(){   // > TERM COMPARISON_2 | >= TERM COMPARISON_2 | < TERM COMPARISON_2 | <= TERM COMPARISON_2 | EOF
        if(hayerrores) return;
        if(preanalisis.tipo == TipoToken.MAYOR_QUE)
        {
            coincidir(TipoToken.MAYOR_QUE);
            TERM();
            COMPARISON_2();
        }
        else if(preanalisis.tipo == TipoToken.OP_MAYOR_IGUAL_QUE)
        {
            coincidir(TipoToken.OP_MAYOR_IGUAL_QUE);
            TERM();
            COMPARISON();
        }
        else if(preanalisis.tipo == TipoToken.MENOR_QUE){
            coincidir(TipoToken.MENOR_QUE);
            TERM();
            COMPARISON_2();
        }
        else if(preanalisis.tipo == TipoToken.OP_MENOR_IGUAL_QUE)
        {
            coincidir(TipoToken.OP_MENOR_IGUAL_QUE);
            TERM();
            COMPARISON_2();
        }
    }
    
    void TERM() {  // FACTOR TERM_2
        if(hayerrores) return;
        if(preanalisis.tipo == TipoToken.ADMIRACION || preanalisis.tipo == TipoToken.GUION_MEDIO   || preanalisis.tipo == TipoToken.VERDADERO 
        || preanalisis.tipo == TipoToken.FALSO  || preanalisis.tipo == TipoToken.NULO || preanalisis.tipo == TipoToken.ESTE 
        || preanalisis.tipo == TipoToken.NUMERO || preanalisis.tipo == TipoToken.CADENA  || preanalisis.tipo == TipoToken.IDENTIFICADOR 
        || preanalisis.tipo == TipoToken.PARENTESIS_IZQUIERDO        || preanalisis.tipo == TipoToken.SUPER){    
            FACTOR();
            TERM_2();
        }
        else{
            hayerrores=true;
            System.out.println("Error en la funcion TERM, en la posición " + preanalisis.posicion + ". No se esperaba el token " + preanalisis.tipo);
        }
    }

    void TERM_2 ()   // - FACTOR TERM_2  | + TERM_2 | EOF
    {
        if(hayerrores) return;
        if(preanalisis.tipo == TipoToken.GUION_MEDIO)
        {
            coincidir(TipoToken.GUION_MEDIO);
            FACTOR();
            TERM_2();
        }
        else if(preanalisis.tipo == TipoToken.MAS)
        {
            coincidir(TipoToken.MAS);
            FACTOR();
            TERM_2();
        }
    }

    void FACTOR() {  // UNARY FACTOR_2
        if(hayerrores) return;
        if(preanalisis.tipo == TipoToken.ADMIRACION || preanalisis.tipo == TipoToken.GUION_MEDIO  || preanalisis.tipo == TipoToken.VERDADERO 
        || preanalisis.tipo == TipoToken.FALSO  || preanalisis.tipo == TipoToken.NULO || preanalisis.tipo == TipoToken.ESTE 
        || preanalisis.tipo == TipoToken.NUMERO || preanalisis.tipo == TipoToken.CADENA || preanalisis.tipo == TipoToken.IDENTIFICADOR 
        || preanalisis.tipo == TipoToken.PARENTESIS_IZQUIERDO   || preanalisis.tipo == TipoToken.SUPER){
            UNARY();
            FACTOR_2();
        }
        else{
            hayerrores=true;
            System.out.println("Error en la funcion FACTOR, en la posición " + preanalisis.posicion + ". No se esperaba el token " + preanalisis.tipo);
        }
    }

    void FACTOR_2(){   //  / UNARY FACTOR_2 |  *UNARY FACTOR_2  | EOF
        if(hayerrores) return;
        if(preanalisis.tipo == TipoToken.BARRA_INCLINADA)
        {
            coincidir(TipoToken.BARRA_INCLINADA);
            UNARY();
            FACTOR_2();
        }
        else if(preanalisis.tipo == TipoToken.ASTERISCO)
        {
            coincidir(TipoToken.ASTERISCO);
            UNARY();
            FACTOR_2();
        }
    }

    void UNARY(){   // ! UNARY FACTOR_2   | - UNARY FACTOR_2  / CALL
        if(hayerrores) return;
        if(preanalisis.tipo == TipoToken.ADMIRACION)
        {
            coincidir(TipoToken.ADMIRACION);
            UNARY();
        }
        else if(preanalisis.tipo == TipoToken.GUION_MEDIO)
        {
            coincidir(TipoToken.GUION_MEDIO);
            UNARY();
        }
        else if(preanalisis.tipo == TipoToken.VERDADERO || preanalisis.tipo == TipoToken.FALSO || preanalisis.tipo == TipoToken.NULO 
        || preanalisis.tipo == TipoToken.ESTE  || preanalisis.tipo == TipoToken.NUMERO || preanalisis.tipo == TipoToken.CADENA
        || preanalisis.tipo == TipoToken.IDENTIFICADOR || preanalisis.tipo == TipoToken.PARENTESIS_IZQUIERDO
        || preanalisis.tipo == TipoToken.SUPER)
        {
            CALL();
        }
        else {
            hayerrores=true;
            System.out.println("Error en la funcion UNARY, en la posición " + preanalisis.posicion + ". No se esperaba el token " + preanalisis.tipo);
        }
    }

    void CALL() {     //  PRIMARY CALL_2
        if(hayerrores) return;
        if(preanalisis.tipo == TipoToken.VERDADERO || preanalisis.tipo == TipoToken.FALSO || preanalisis.tipo == TipoToken.NULO 
        || preanalisis.tipo == TipoToken.ESTE  || preanalisis.tipo == TipoToken.NUMERO || preanalisis.tipo == TipoToken.CADENA
        || preanalisis.tipo == TipoToken.IDENTIFICADOR || preanalisis.tipo == TipoToken.PARENTESIS_IZQUIERDO || preanalisis.tipo == TipoToken.SUPER){
            PRIMARY ();
            CALL_2();
        }
        else {
            hayerrores=true;
            System.out.println("Error en la funcion CALL, en la posición " + preanalisis.posicion + ". No se esperaba el token " + preanalisis.tipo);
        }
    }

    void CALL_2() {    //(ARGUMENTS_OPC) CALL_2 | .id CALL_2 | EOF
        if(hayerrores) return;
        if(preanalisis.tipo == TipoToken.PARENTESIS_IZQUIERDO)
        {
            coincidir(TipoToken.PARENTESIS_IZQUIERDO);
            ARGUMENTS_OPC();
            coincidir(TipoToken.PARENTESIS_DERECHO);
            CALL_2();
        }
        else if(preanalisis.tipo == TipoToken.PUNTO)
        {
            coincidir(TipoToken.PUNTO);
            coincidir(TipoToken.IDENTIFICADOR);
            CALL_2();
        }
    }

    void CALL_OPC(){      // CALL .  | EOF
        if(hayerrores) return;
        if(preanalisis.tipo == TipoToken.VERDADERO || preanalisis.tipo == TipoToken.FALSO || preanalisis.tipo == TipoToken.NULO 
        || preanalisis.tipo == TipoToken.ESTE  || preanalisis.tipo == TipoToken.NUMERO || preanalisis.tipo == TipoToken.CADENA
        || preanalisis.tipo == TipoToken.IDENTIFICADOR || preanalisis.tipo == TipoToken.PARENTESIS_IZQUIERDO || preanalisis.tipo == TipoToken.SUPER)
        {
            CALL();
            coincidir(TipoToken.PUNTO);
        }
    }

    void PRIMARY (){      // true | false | null | this | number | string | id | (EXPRESSION) | super . id
        if(hayerrores) return;
        if(preanalisis.tipo == TipoToken.VERDADERO)
        {
            coincidir(TipoToken.VERDADERO);
        }
        else if(preanalisis.tipo == TipoToken.FALSO)
        {
            coincidir(TipoToken.FALSO);
        }
        else if(preanalisis.tipo == TipoToken.NULO)
        {
            coincidir(TipoToken.NULO);
        }
        else if(preanalisis.tipo == TipoToken.ESTE)
        {
            coincidir(TipoToken.ESTE);
        }
        else if(preanalisis.tipo == TipoToken.NUMERO)
        {
            coincidir(TipoToken.NUMERO);
        }
        else if(preanalisis.tipo == TipoToken.CADENA)
        {
            coincidir(TipoToken.CADENA);
        }
        else if(preanalisis.tipo == TipoToken.IDENTIFICADOR)
        {
            coincidir(TipoToken.IDENTIFICADOR);
        }
        else if(preanalisis.tipo == TipoToken.PARENTESIS_IZQUIERDO)
        {
            coincidir(TipoToken.PARENTESIS_IZQUIERDO);
            EXPRESSION();
            coincidir(TipoToken.PARENTESIS_DERECHO);
        }
        else if(preanalisis.tipo == TipoToken.SUPER)
        {
            coincidir(TipoToken.SUPER);
            coincidir(TipoToken.PUNTO);
            coincidir(TipoToken.IDENTIFICADOR);   
        }
        else{
            hayerrores=true;
            System.out.println("Error en la funcion PRIMARY, en la posición " + preanalisis.posicion + ". No se esperaba el token " + preanalisis.tipo);
        }
    }


/////////////////////////////////////////OTRAS////////////////////////////////////////////////////////////////////////////
    void FUNCTION (){    // id (PARAMETERS_OPC) BLOCK
        if(hayerrores) return;
        if(TipoToken.IDENTIFICADOR == preanalisis.tipo)
        {
            coincidir(TipoToken.IDENTIFICADOR);
            coincidir(TipoToken.PARENTESIS_IZQUIERDO);
            PARAMETERS_OPC();
            coincidir(TipoToken.PARENTESIS_DERECHO);
            BLOCK();
        }
        else{
            hayerrores=true;
            System.out.println("Error en la funcion FUNCTION, en la posición " + preanalisis.posicion + ". No se esperaba el token " + preanalisis.tipo);
        }
    }

    void FUNCTIONS (){    // FUNCTION FUNCTIONS | EOF
        if(hayerrores) return;
        if(preanalisis.tipo == TipoToken.IDENTIFICADOR)
        {
            FUNCTION();
            FUNCTIONS();
        }
    }

    void PARAMETERS_OPC(){       // PARAMETERS | EOF
        if(hayerrores) return;
        if(preanalisis.tipo == TipoToken.IDENTIFICADOR)
        {
            PARAMETERS();
        }
    }

    void PARAMETERS() {     // id PARAMETERS_2
        if(hayerrores) return;
        if(TipoToken.IDENTIFICADOR == preanalisis.tipo)
        {
            coincidir(TipoToken.IDENTIFICADOR);
            PARAMETERS_2();            
        }
        else{
            hayerrores=true;
            System.out.println("Error en la funcion PARAMETERS, en la posición " + preanalisis.posicion + ". No se esperaba el token " + preanalisis.tipo);
        }
    }

    void PARAMETERS_2 (){      // , id PARAMETERS_2 | EOF
        if(hayerrores) return;
        if(preanalisis.tipo == TipoToken.COMA)
        {
            coincidir(TipoToken.COMA);
            coincidir(TipoToken.IDENTIFICADOR);
            PARAMETERS_2();
        }
    }

    void ARGUMENTS_OPC() {      // ARGUMENTS | EOF
        if(hayerrores) return;
        if(preanalisis.tipo == TipoToken.ADMIRACION || preanalisis.tipo == TipoToken.GUION_MEDIO|| preanalisis.tipo == TipoToken.VERDADERO 
        || preanalisis.tipo == TipoToken.FALSO  || preanalisis.tipo == TipoToken.NULO || preanalisis.tipo == TipoToken.ESTE
        || preanalisis.tipo == TipoToken.NUMERO || preanalisis.tipo == TipoToken.CADENA  || preanalisis.tipo == TipoToken.IDENTIFICADOR 
        || preanalisis.tipo == TipoToken.PARENTESIS_IZQUIERDO || preanalisis.tipo == TipoToken.SUPER){
            ARGUMENTS();
        }    
    }

    void ARGUMENTS(){   // EXPRESSION ARGUMENTS_2
        if(hayerrores) return;
        if(preanalisis.tipo == TipoToken.ADMIRACION || preanalisis.tipo == TipoToken.GUION_MEDIO || preanalisis.tipo == TipoToken.VERDADERO 
        || preanalisis.tipo == TipoToken.FALSO   || preanalisis.tipo == TipoToken.NULO || preanalisis.tipo == TipoToken.ESTE 
        || preanalisis.tipo == TipoToken.NUMERO || preanalisis.tipo == TipoToken.CADENA  || preanalisis.tipo == TipoToken.IDENTIFICADOR 
        || preanalisis.tipo == TipoToken.PARENTESIS_IZQUIERDO  || preanalisis.tipo == TipoToken.SUPER || preanalisis.tipo == TipoToken.MAS){
            EXPRESSION();
            ARGUMENTS_2();
        }
        else{
            hayerrores=true;
            System.out.println("Error en la funcion ARGUMENTS, en la posición " + preanalisis.posicion + ". No se esperaba el token " + preanalisis.tipo);
        }
        
    }

    void ARGUMENTS_2(){    // , EXPRESSION ARGUMENTS_2 | EOF
        if(hayerrores) return;
        if(preanalisis.tipo == TipoToken.COMA)
        {
            coincidir(TipoToken.COMA);
            EXPRESSION();
            ARGUMENTS_2();
        }
    }

    void coincidir(TipoToken t){ 
        if(hayerrores) return;
        if (preanalisis.tipo == t){
            i++;
            preanalisis = tokens.get(i);
        }
        else{
            valido = false;
            hayerrores = true;
            System.out.println("Error en la posición " + preanalisis.posicion + ". Se esperaba un  " + t);
        }
    }
}