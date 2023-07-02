import java.util.ArrayList;
import java.util.List;

public class Arbol {
    private final Nodo raiz;

    public Arbol(Nodo raiz){
        this.raiz = raiz;
    }

    public void recorrer(TablaSimbolos tablaSimbolos){
        for(Nodo n : raiz.getHijos()){
            Token t = n.getValue();
            switch(t.tipo){
                // Operadores logicos
                case MENOR_QUE:
                case OP_MENOR_IGUAL_QUE:
                case MAYOR_QUE:
                case OP_MAYOR_IGUAL_QUE:
                case OP_IGUAL_QUE:
                case OP_DIFERENTE:
                case Y:
                case O:
                    SolverAritmetico solver = new SolverAritmetico(n);
                    Object resultado = solver.resolver(tablaSimbolos);
                    System.out.println(resultado);
                break;
                
                case VAR:
                    if(n.getHijos().size() > 1){
                        Nodo izquierdoVAR = n.getHijos().get(0);
                        Nodo derechoVAR = n.getHijos().get(1);
                        switch(derechoVAR.getValue().tipo){
                            case MAS:
                            case GUION_MEDIO:
                            case ASTERISCO:
                            case BARRA_INCLINADA:
                                SolverAritmetico solverVAR = new SolverAritmetico(derechoVAR);
                                Object resultadoVAR = solverVAR.resolver(tablaSimbolos);
                                if(tablaSimbolos.existeIdentificador(izquierdoVAR.getValue().lexema)){
                                    Interprete.error(izquierdoVAR.getValue().posicion, "La variable ingresada ya existe");
                                }else{
                                    tablaSimbolos.asignar(izquierdoVAR.getValue().lexema, resultadoVAR);
                                }
                            break;
                            default:
                                if(tablaSimbolos.existeIdentificador(izquierdoVAR.getValue().lexema)){
                                    Interprete.error(izquierdoVAR.getValue().posicion, "La variable ingresada ya existe");
                                }else{
                                    tablaSimbolos.asignar(izquierdoVAR.getValue().lexema, derechoVAR.getValue().literal);
                                }
                            break;
                        }
                    }
                break;

                case IMPRIMIR:
                    Nodo izquierdoPRINT = n.getHijos().get(0);
                    switch(izquierdoPRINT.getValue().tipo){
                        case MAS:
                            SolverAritmetico solverPRINT = new SolverAritmetico(izquierdoPRINT);
                            Object resultadoP = solverPRINT.resolver(tablaSimbolos);
                            System.out.println(resultadoP);
                        default:
                            if(tablaSimbolos.existeIdentificador(izquierdoPRINT.getValue().lexema)){
                                System.out.println(tablaSimbolos.obtener(izquierdoPRINT.getValue().lexema));
                            }else{
                                if(izquierdoPRINT.getValue().literal != null){
                                    System.out.println(izquierdoPRINT.getValue().literal);
                                }
                            }
                    break;
                    }
                break;

                case SI:
                    Nodo izquierdoIF = n.getHijos().get(0);
                    SolverAritmetico IF = new SolverAritmetico(izquierdoIF);
                    Object condicionIF = IF.resolver(tablaSimbolos);
                    if((Boolean) condicionIF){
                        if(n.getHijos().size() > 2){
                            for(int i = 0; i < n.getHijos().size(); i++){
                                Nodo derechoIF = n.getHijos().get(i);
                                switch (derechoIF.getValue().tipo){
                                    case IMPRIMIR:
                                        Nodo izquierdaIF = derechoIF.getHijos().get(0);
                                        if(tablaSimbolos.existeIdentificador(izquierdaIF.getValue().lexema)){
                                            System.out.println(tablaSimbolos.obtener(izquierdaIF.getValue().lexema));
                                        }else{
                                            if(izquierdaIF.getValue().literal != null){
                                                System.out.println(izquierdaIF.getValue().literal);
                                            }
                                        }
                                        break;
                                    default:
                                    break;
                                }
                            }
                        }else{
                            Nodo derechoIF = n.getHijos().get(1);
                            switch(derechoIF.getValue().tipo){
                                case IMPRIMIR:
                                    Nodo izquierdaIF = derechoIF.getHijos().get(0);
                                    if(tablaSimbolos.existeIdentificador(izquierdaIF.getValue().lexema)){
                                        System.out.println(tablaSimbolos.obtener(izquierdaIF.getValue().lexema));
                                    }else{
                                        if(izquierdaIF.getValue().literal != null){
                                            System.out.println(izquierdaIF.getValue().literal);
                                        }
                                    }
                                    break;
                                default:
                                break;
                            }
                        }
                    }else{
                        if(n.getHijos().size() > 2){
                            Nodo aux = n.getHijos().get(2);
                            if(aux.getHijos().size() > 2){
                                for(int i = 0; i < aux.getHijos().size(); i++){
                                    Nodo derechoIF1 = aux.getHijos().get(i);
                                    switch (derechoIF1.getValue().tipo){
                                        case IMPRIMIR:
                                            Nodo izquierdaIF = derechoIF1.getHijos().get(0);
                                            if(tablaSimbolos.existeIdentificador(izquierdaIF.getValue().lexema)){
                                                System.out.print(tablaSimbolos.obtener(izquierdaIF.getValue().lexema));
                                            }else{
                                                if (izquierdaIF.getValue().literal != null){
                                                    System.out.println(izquierdaIF.getValue().literal);
                                                }
                                            }
                                            break;
                                        default:
                                    break;
                                    }
                                }
                            }else{
                                Nodo derechoIF1 = aux.getHijos().get(0);
                                switch(derechoIF1.getValue().tipo){
                                    case IMPRIMIR:
                                        Nodo izquierdaIF = derechoIF1.getHijos().get(0);
                                        if(tablaSimbolos.existeIdentificador(izquierdaIF.getValue().lexema)){
                                            System.out.print(tablaSimbolos.obtener(izquierdaIF.getValue().lexema));
                                        }else{
                                            if(izquierdaIF.getValue().literal != null){
                                                System.out.println(izquierdaIF.getValue().literal);
                                            }
                                        }
                                        break;
                                    default:
                                break;
                                }
                            }
                        }
                    }
                break;

                case MIENTRAS:
                    Nodo condicion = n.getHijos().get(0);
                    SolverAritmetico solverWHILE = new SolverAritmetico(condicion);
                    Object resultadoWHILE = solverWHILE.resolver(tablaSimbolos);                    
                    while((Boolean) resultadoWHILE){
                        if(n.getHijos().size() > 2){
                            for(int i=0;i < n.getHijos().size();i++){
                                Nodo derechoWHILE = n.getHijos().get(i);
                                    switch(derechoWHILE.getValue().tipo){
                                        case IMPRIMIR:
                                            Nodo izquierdaWHILE = derechoWHILE.getHijos().get(0);
                                            if(tablaSimbolos.existeIdentificador(izquierdaWHILE.getValue().lexema)){
                                                System.out.println(tablaSimbolos.obtener(izquierdaWHILE.getValue().lexema));
                                            }else if(izquierdaWHILE.getValue().literal != null) {
                                                System.out.println(izquierdaWHILE.getValue().literal);
                                            }
                                        break;
                                        default:
                                            Nodo izquierdaWHILE1 = derechoWHILE.getHijos().get(0);
                                            if(tablaSimbolos.existeIdentificador(izquierdaWHILE1.getValue().lexema)){
                                                System.out.println(tablaSimbolos.obtener(izquierdaWHILE1.getValue().lexema));
                                            }else if(izquierdaWHILE1.getValue().literal != null) {
                                                System.out.println(izquierdaWHILE1.getValue().literal);
                                            }
                                        break;
                                }
                            }
                        }else{
                            Nodo derechoWHILE = n.getHijos().get(1);
                            switch(derechoWHILE.getValue().tipo){
                                case IMPRIMIR:
                                    Nodo expresion = derechoWHILE.getHijos().get(0);
                                    if(tablaSimbolos.existeIdentificador(expresion.getValue().lexema)){
                                        System.out.println(tablaSimbolos.obtener(expresion.getValue().lexema));
                                    }else if(expresion.getValue().literal != null) {
                                        System.out.println(expresion.getValue().literal);
                                    }
                                break;
                            default:
                                break;
                            }
                        }
                        break;
                    }
                    resultado = solverWHILE.resolver(tablaSimbolos);
                break;
                
                case PARA:
                    //PARA
                    break;
                default:
                    break;

            }
        }
    }
}
