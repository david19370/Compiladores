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
}