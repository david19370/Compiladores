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
        Inicio();

        if(!hayErrores && !preanalisis.equals(EOF)){
            System.out.println("Error en la posición " + preanalisis.posicion + ". No se esperaba el token " + preanalisis.tipo);
        }
        else if(!hayErrores && preanalisis.equals(EOF)){
            System.out.println(" Lo que ingresaste es válido. ");
        }
    }

    void Inicio(){
        while(!preanalisis.equals(EOF) && !hayErrores){
            declaracion();
        }
    }

    void declaracion(){      //Posibles tipos de inicios de sentencia
        if (preanalisis.equals(CLASE)) {
            CLASE();
        }else if(preanalisis.equals(FUN)){
            FUN();
        }else if(preanalisis.equals(VAR)){
            VAR();
        }else if(preanalisis.equals(SI) || preanalisis.equals(PARA) || preanalisis.equals(IMPRIMIR) || preanalisis.equals(MIENTRAS) || preanalisis.equals(LLAVE_DERECHA) || preanalisis.equals(IDENTIFICADOR) || preanalisis.equals(RETORNAR)){
            sentencias();
        }else{}
    } 

    void CLASE(){       //clase id < id
        if(hayErrores) return;
            coincidir(CLASE);
            coincidir(IDENTIFICADOR);
            MENOR_QUE();
            coincidir(LLAVE_DERECHA);
            funciones_1();
            coincidir(LLAVE_IZQUIERDA);
    }

    void FUN(){
        if(hayErrores) return;
            coincidir(FUN);
            funciones();
    }

    void VAR(){     //var id = numero
        if(hayErrores) return;
            coincidir(VAR);
            coincidir(IDENTIFICADOR);
            inicializar();
            coincidir(PUNTO_COMA);
    }

    void MENOR_QUE(){     //   < id
        if(hayErrores) return;
        if(preanalisis.equals(MENOR_QUE)){
            coincidir(MENOR_QUE);
            coincidir(IDENTIFICADOR);
        }
    }

    void inicializar(){     //   = numero
        if(hayErrores) return;
        if(preanalisis.equals(IGUAL_QUE)){
            coincidir(IGUAL_QUE);
            Expresiones();
        }
    }

    void sentencias(){
        if(hayErrores) return;
        if(preanalisis.equals(PUNTO_COMA)){
            coincidir(PUNTO_COMA);
        }
        else if(preanalisis.equals(IDENTIFICADOR)){
            IDENTIFICADOR();
        }else if(preanalisis.equals(PARA)){
            PARA();
        }else if(preanalisis.equals(SI)){
            SI();
        }else if(preanalisis.equals(IMPRIMIR)){
            IMPRIMIR();
        }else if(preanalisis.equals(RETORNAR)){
            RETORNAR();
        }else if(preanalisis.equals(MIENTRAS)){
            MIENTRAS();
        }else if(preanalisis.equals(LLAVE_DERECHA)){
            LLAVES();
        }else{
            hayErrores = true;
            System.out.println("Error en la posición " + preanalisis.posicion + ". Se esperaba ;  , PARA, IF, PRINT, RETURN, MIENTRAS, IDENTIFICADOR, {");
        }
    }

    void IDENTIFICADOR(){     // (algo) = numero ;
        if(hayErrores) return;
            Expresiones();
            coincidir(PUNTO_COMA);
    }

    void PARA(){     //para ( 1 ; 2 ; 3 ) { }
        if(hayErrores) return;
            coincidir(PARA);
            coincidir(PARENTESIS_DERECHO);
            PARA_1();
            PARA_2();
            PARA_3();
            coincidir(PARENTESIS_IZQUIERDO);
            sentencias();
    }

    void PARA_1(){
        if(hayErrores) return;
        if(preanalisis.equals(VAR)){
            VAR();
        }else if(preanalisis.equals(PUNTO_COMA)){
            coincidir(PUNTO_COMA);
        }else{
            IDENTIFICADOR();
        }
    }

    void PARA_2(){
        if(hayErrores) return;
        if(preanalisis.equals(PUNTO_COMA)){
            coincidir(PUNTO_COMA);
        }else{
            Expresiones();
            coincidir(PUNTO_COMA);
        }
    }

    void PARA_3(){
        if(hayErrores) return;
        if(!preanalisis.equals(PARENTESIS_IZQUIERDO)){
            Expresiones();
        }
    }

    void SI(){    // if ( 1 ) { 2 }
        if(hayErrores) return;
            coincidir(Y);
            coincidir(PARENTESIS_DERECHO);
            Expresiones();
            coincidir(PARENTESIS_IZQUIERDO);
            sentencias();
    }

    void IMPRIMIR(){        //print ( 1 ) ;
        if(hayErrores) return;
            coincidir(IMPRIMIR);
            Expresiones();
            coincidir(PUNTO_COMA);
    }

    void RETORNAR(){       //return (algo) ;
        if(hayErrores) return;
        if(preanalisis.equals(RETORNAR)){
            coincidir(RETORNAR);
            RETORNAR_SIGUIENTE();     //algo
            coincidir(PUNTO_COMA);
        }else{
            hayErrores = true;
            System.out.println("Error en la posición " + preanalisis.posicion + ". Se esperaba RETURN");
        }
    }

    void RETORNAR_SIGUIENTE(){    //algo
        if(hayErrores) return;
        if(!preanalisis.equals(PUNTO_COMA)){
            Expresiones();       
        }else{
            hayErrores = true;
            System.out.println("Error en la posición " + preanalisis.posicion + ". No se esperaba ;");
        }
    }

    void MIENTRAS(){        //while(1){2}
        if(hayErrores) return;
            coincidir(MIENTRAS);
            coincidir(PARENTESIS_DERECHO);
            Expresiones();
            coincidir(PARENTESIS_IZQUIERDO);
            sentencias();
    }

    void LLAVES(){       // { (DENTRO DE LLAVES) }
        if(hayErrores) return;
        if(preanalisis.equals(LLAVE_DERECHA)){
            coincidir(LLAVE_DERECHA);
            DENTRO_DE_LLAVES();
            coincidir(LLAVE_IZQUIERDA);
        }else{
            hayErrores = true;
            System.out.println("Error en la posición " + preanalisis.posicion + ". Se esperaba {");
        }
    }

    void DENTRO_DE_LLAVES(){   //DENTRO DE LLAVES
        if(hayErrores) return;
        declaracion();
        if(!preanalisis.equals(LLAVE_IZQUIERDA)){
            DENTRO_DE_LLAVES();
        }
    }

    void Expresiones(){
        if(hayErrores) return;
        asignacion();
    }

    void asignacion(){     // (algo) = 1
        if(hayErrores) return;
            O();
           asignacion_1();
    }

    void asignacion_1(){
        if(hayErrores) return;
        if(preanalisis.equals(IGUAL_QUE)){
            coincidir(IGUAL_QUE);
            Expresiones();
         }else{
         }
    }

    void O(){
        if(hayErrores) return;
            ADEMAS();
            O_1();
    }

    void O_1(){
        if(hayErrores) return;
        if(preanalisis.equals(O)){
            coincidir(O);
            ADEMAS();
            O_1();
         }else{}
    }

    void ADEMAS(){      // and
        if(hayErrores) return;
        IGUALDAD();
            ADEMAS_1();
    }

    void ADEMAS_1(){
        if(hayErrores) return;
        if(preanalisis.equals(ADEMAS)){
            coincidir(ADEMAS);
            IGUALDAD();
            ADEMAS_1();
         }else{}
    }

    void IGUALDAD(){      //IGUALDAD o no 
        if(hayErrores) return;
        comparacion();
            IGUALDAD_1();
    }

    void IGUALDAD_1(){     // == / !=
        if(hayErrores) return;
        if(preanalisis.equals(OP_DIFERENTE) || preanalisis.equals(OP_IGUAL_QUE)){
            coincidir(preanalisis);
            comparacion();
            IGUALDAD_1();
         }else{
        }
    }

    void comparacion(){    // todos los comparadores
        if(hayErrores) return;
            adicion();
            comparacion_1();
    }

    void comparacion_1(){     // < / > / <= / >=
        if(hayErrores) return;
        if(preanalisis.equals(MENOR_QUE) || preanalisis.equals(MAYOR_QUE) || preanalisis.equals(OP_MENOR_IGUAL_QUE) || preanalisis.equals(OP_MAYOR_IGUAL_QUE) ){
            coincidir(preanalisis);
            adicion();
            comparacion_1();
         }else{
        }
    }

    void adicion(){     //operaciones aditivas
        if(hayErrores) return;
            multiplicativas();
            adicion_1();
    }

    void adicion_1(){     // suma o resta
        if(hayErrores) return;
        if(preanalisis.equals(MAS) || preanalisis.equals(GUION_MEDIO) ){
            coincidir(preanalisis);
            multiplicativas();
            adicion_1();
         }else{}
    }

    void multiplicativas(){   //operaciones multiplicativas
        if(hayErrores) return;
        unaria();
            multiplicativas_1();
    }

    void multiplicativas_1(){      //  multiplicacion o division
        if(hayErrores) return;
        if(preanalisis.equals(ASTERISCO) || preanalisis.equals(BARRA_INCLINADA)){
            coincidir(preanalisis);
            unaria();
            multiplicativas_1();
         }else{
        }
    }

    void unaria(){    //operacion unaria, negacion o negativo
        if(hayErrores) return;
        if(preanalisis.equals(ADMIRACION) || preanalisis.equals(GUION_MEDIO)){
            coincidir(preanalisis);
            unaria();
         }else{
            llamadaFunciones();
        }
    }

    void llamadaFunciones(){  // this.Funcion
        if(hayErrores) return;
            verificacion();
            llamadaFunciones_1();
    }

    void llamadaFunciones_1(){
        if(hayErrores) return;
        if(preanalisis.equals(PARENTESIS_DERECHO)){
            coincidir(PARENTESIS_DERECHO);
            argumentos_1();
            coincidir(PARENTESIS_IZQUIERDO);
            llamadaFunciones_1();
        }else if(preanalisis.equals(PUNTO)){
            coincidir(PUNTO);
            coincidir(IDENTIFICADOR);
            llamadaFunciones_1();
        }else{
        }
    }

    void llamadaFunciones_2(){
        if(hayErrores) return;

        if(preanalisis.equals(PUNTO)){
            coincidir(PUNTO);
            coincidir(IDENTIFICADOR);
            llamadaFunciones_2();
        }else{
            hayErrores = true;
            System.out.println("Error en la posición " + preanalisis.posicion + ". Se esperaba .");
        }
    }

    void verificacion(){   // verdadero / falso / null / this / numero / cadena / identificador
        if(hayErrores) return;
        if(preanalisis.equals(VERDADERO) || preanalisis.equals(FALSO) || preanalisis.equals(NULO) || preanalisis.equals(ESTE) || preanalisis.equals(NUMERO) || preanalisis.equals(CADENA) || preanalisis.equals(IDENTIFICADOR)){
            coincidir(preanalisis);
        }else if(preanalisis.equals(PARENTESIS_DERECHO)){
            coincidir(PARENTESIS_DERECHO);
            Expresiones();
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

    void funciones(){      //Nombre ( parametros ) { }
        if(hayErrores) return;
            coincidir(IDENTIFICADOR);
            coincidir(PARENTESIS_DERECHO);
            parametros();
            coincidir(PARENTESIS_IZQUIERDO);
            DENTRO_DE_LLAVES();
    }

    void funciones_1(){
        if(hayErrores) return;
        if(preanalisis.equals(IDENTIFICADOR)){
            funciones();
            funciones_1();
        }else{
        }
    }

    void parametros(){
        if(hayErrores) return;
        if(preanalisis.equals(IDENTIFICADOR)){
            parametros_1();
        }else{
        }
    }

    void parametros_1(){
        if(hayErrores) return;
            coincidir(IDENTIFICADOR);
            parametros_2();
    }

    void parametros_2(){
        if(hayErrores) return;

        if(preanalisis.equals(COMA)){
            coincidir(COMA);
            coincidir(IDENTIFICADOR);
            parametros_2();
        }else{
        }
    }

    void argumentos(){    // ID / true / false / ( / numero / cadena / null / super
        if(hayErrores) return;

        if(preanalisis.equals(IDENTIFICADOR) || preanalisis.equals(VERDADERO) || preanalisis.equals(FALSO) || preanalisis.equals(NULO)
            || preanalisis.equals(NUMERO) || preanalisis.equals(CADENA) || preanalisis.equals(PARENTESIS_DERECHO) || preanalisis.equals(SUPER)){
                argumentos_1();
        }else{
        }
    }

    void argumentos_1(){
        if(hayErrores) return;
            Expresiones();
            argumentos_2();
    }

    void argumentos_2(){
        if(hayErrores) return;

        if(preanalisis.equals(COMA)){
            coincidir(COMA);
            Expresiones();
            argumentos_2();
        }else{
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