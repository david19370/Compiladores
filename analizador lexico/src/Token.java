public class Token {

    final TipoToken tipo;
    final String lexema;
    final Object literal;
    final int posicion;

    public Token(TipoToken tipo, String lexema,Object literal, int posicion) {
        this.tipo = tipo;
        this.lexema = lexema;
        this.literal = literal;
        this.posicion = posicion;
    }

    public Token(TipoToken tipo, String lexema,Object literal) {
        this.tipo = tipo;
        this.lexema = lexema;
        this.literal = literal;
        this.posicion = 0;
    }

    public String toString(){
        return tipo + " " + lexema + " ";
    }


    // Métodos auxiliares
    public boolean esOperando(){
        switch (this.tipo){
            case IDENTIFICADOR:
            case NUMERO:
                return true;
            default:
                return false;
        }
    }

    public boolean esOperador(){
        switch (this.tipo){
            case SUMA:
            case RESTA:
            case MULTIPLICACION:
            case DIVISION:
            case IGUAL_QUE:
            case MAYOR_QUE:
            case OP_MAYOR_IGUAL_QUE:
                return true;
            default:
                return false;
        }
    }

    public boolean esPalabraReservada(){
        switch (this.tipo){
            case VAR:
            case SI:
            case IMPRIMIR:
            case ADEMAS:
                return true;
            default:
                return false;
        }
    }

    public boolean esEstructuraDeControl(){
        switch (this.tipo){
            case SI:
            case ADEMAS:
                return true;
            default:
                return false;
        }
    }

    public boolean precedenciaMayorIgual(Token t){
        return this.obtenerPrecedencia() >= t.obtenerPrecedencia();
    }

    private int obtenerPrecedencia(){
        switch (this.tipo){
            case MULTIPLICACION:
                return 7;
            case DIVISION:
                return 7;
            case SUMA:
                return 6;
            case RESTA:
                return 6;
            case MENOR_QUE:
                return 5;
            case OP_MENOR_IGUAL_QUE:
                return 5;
            case MAYOR_QUE:
                return 5;
            case OP_MAYOR_IGUAL_QUE:
                return 5;
            case OP_IGUAL_QUE:
                return 4;
            case OP_DIFERENTE:
                return 4;
            case Y:
                return 3;
            case O:
                return 2;
            case IGUAL_QUE:
                return 1;
        }

        return 0;
    }

    public int aridad(){
        switch (this.tipo) {
            case MULTIPLICACION:
            case DIVISION:
            case SUMA:
            case RESTA:
            case MENOR_QUE:
            case OP_MENOR_IGUAL_QUE:
            case MAYOR_QUE:
            case OP_MAYOR_IGUAL_QUE:
            case OP_IGUAL_QUE:
            case OP_DIFERENTE:
            case Y:
            case O:
            case IGUAL_QUE:
                return 2;
        }
        return 0;
    }
}