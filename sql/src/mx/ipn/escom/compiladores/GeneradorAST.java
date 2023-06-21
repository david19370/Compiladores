package mx.ipn.escom.compiladores;

import java.util.List;
import java.util.Stack;

public class GeneradorAST {

    private final List<Token> postfija;
    private final Stack<Nodo> pila;

    public GeneradorAST(List<Token> postfija){
        this.postfija = postfija;
        this.pila = new Stack<>();
    }

    public Arbol generarAST(){
        for(Token t : postfija){
            if(t.tipo == TipoToken.EOF){
                continue;
            }

            if(t.esPalabraReservada()){
                Nodo n = new Nodo(t);
                pila.push(n);
            }
            else if(t.esCaracter()){
                int aridad = t.aridad();
                Nodo n = new Nodo(t);
                for(int i=1; i<=aridad; i++){
                    Nodo nodoAux = pila.pop();
                    n.insertarHijo(nodoAux);
                }
                pila.push(n);
            }
        }

        // Suponiendo que en la pila sólamente queda un nodo
        Nodo nodoAux = pila.pop();
        Arbol programa = new Arbol(nodoAux);

        return programa;
    }
}
