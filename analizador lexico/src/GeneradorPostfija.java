import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class GeneradorPostfija {

    private final List<Token> infija;
    private final Stack<Token> pila;
    private final List<Token> postfija;

    public GeneradorPostfija(List<Token> infija) {
        this.infija = infija;
        this.pila = new Stack<>();
        this.postfija = new ArrayList<>();
    }
    
    public List<Token> convertir(){
        boolean estructuraDeControl = false;
        Stack<Token> pilaEstructurasDeControl = new Stack<>();

        for(int i=0; i<infija.size(); i++){
            Token t = infija.get(i);

            if(t.tipo == TipoToken.EOF){
                break;
            }

            if(t.esPalabraReservada()){
                /*
                 Si el token actual es una palabra reservada, se va directo a la
                 lista de salida.
                 */
                postfija.add(t);
                if (t.esEstructuraDeControl()){
                    estructuraDeControl = true;
                    pilaEstructurasDeControl.push(t);
                }
            }
            else if(t.esOperando()){
                postfija.add(t);
            }
            else if(t.tipo == TipoToken.PARENTESIS_IZQUIERDO){
                pila.push(t);
            }
            else if(t.tipo == TipoToken.PARENTESIS_DERECHO){
                while(!pila.isEmpty() && pila.peek().tipo != TipoToken.PARENTESIS_IZQUIERDO){
                    Token temp = pila.pop();
                    postfija.add(temp);
                }
                if(pila.peek().tipo == TipoToken.PARENTESIS_IZQUIERDO){
                    pila.pop();
                }
                if(estructuraDeControl&&infija.get(i+1).tipo==TipoToken.LLAVE_IZQUIERDA){
                    postfija.add(new Token(TipoToken.PUNTO_COMA, ";", null,0));
                }
            }
            else if(t.esOperador()){
                while(!pila.isEmpty() && pila.peek().precedenciaMayorIgual(t)){
                    Token temp = pila.pop();
                    postfija.add(temp);
                }
                pila.push(t);
            }
            else if(t.tipo == TipoToken.PUNTO_COMA){
                while(!pila.isEmpty() && pila.peek().tipo != TipoToken.LLAVE_IZQUIERDA){
                    Token temp = pila.pop();
                    postfija.add(temp);
                }
                postfija.add(t);
            }
            else if(t.tipo == TipoToken.LLAVE_IZQUIERDA){
                // Se mete a la pila, tal como el parentesis. Este paso
                // pudiera omitirse, sólo hay que tener cuidado en el manejo
                // del "}".
                pila.push(t);
            }
            else if(t.tipo == TipoToken.LLAVE_DERECHA && estructuraDeControl){

                // Primero verificar si hay un else:
                if(infija.get(i + 1).tipo == TipoToken.ADEMAS){
                    // Sacar el "{" de la pila
                    pila.pop();
                }
                else{
                    // En este punto, en la pila sólo hay un token: "{"
                    // El cual se extrae y se añade un ";" a cadena postfija,
                    // El cual servirá para indicar que se finaliza la estructura
                    // de control.
                    pila.pop();
                    postfija.add(new Token(TipoToken.PUNTO_COMA, ";", null,0));
                    Token auxiliar = pilaEstructurasDeControl.pop();

                    // Se extrae de la pila de estrucuras de control, el elemento en el tope
                    if(auxiliar.tipo == TipoToken.ADEMAS){
                        pilaEstructurasDeControl.pop();
                        postfija.add(new Token(TipoToken.PUNTO_COMA,";",null,0));
                    }
                    if(pilaEstructurasDeControl.isEmpty()){
                        estructuraDeControl = false;
                    }
                }
            }
        }
        while(!pila.isEmpty()){
            Token temp = pila.pop();
            postfija.add(temp);
        }
        while(!pilaEstructurasDeControl.isEmpty()){
            pilaEstructurasDeControl.pop();
            postfija.add(new Token(TipoToken.PUNTO_COMA, ";", null,0));
        }
        return postfija;
    }
}