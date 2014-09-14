/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package compiladores.utils;

import compiladores.enums.Primitiva;

/**
 *
 * @author Siloe
 */
public class Identificador {
    
    public static String[] listaPalavraReservada = {"algoritimos","variaveis","constantes","registro",
    "funcao","retorno","vazio","se","senao","enquanto","para","leia","escreva","inteiro","real","booleano",
    "char","cadeia","verdadeiro","falso"};
    
    public static Primitiva getPrimitiva(char a){
        System.out.println("ASCII de "+a+" = "+(int)a);
        
        if( a >= 48 && a <= 57){
            return  Primitiva.DIGITO;
        }
        if( (a >= 65 && a <= 90) || (a >= 97 && a <= 122)){
            return Primitiva.LETRA;
        }
        if(a == 32 || a == 11 || a == 9){
            return Primitiva.ESPACO;
        }
        if((a >=33 && a < 47) || (a >= 58 && a <= 64) || (a >= 91 && a <= 96) || (a >= 123 && a <= 126)){
            return Primitiva.SIMBOLO;            
        }
        if( a == 182){//representado por "¶"
            return Primitiva.FIM_DE_LINHA;
        }
        /*if(a < 32){
            return Primitiva.OUTROS;
        }
        if(a > 126){
            return Primitiva.OUTROS;
        }*/
        
        return Primitiva.OUTROS;
    }
    
    public static boolean verificarPalavraReservada(String id){
        for (String pr : listaPalavraReservada) {
            if(pr.equals(id)){
                return true;
            }
        }
        return false;
    }
    
}
