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
    
    public static Primitiva getPrimitiva(char a){
        if(a < 32){
            return Primitiva.OUTROS;
        }
        if(a > 126){
            return Primitiva.OUTROS;
        }
        if( a >= 48 && a <= 57){
            return  Primitiva.DIGITO;
        }
        if( (a >= 65 && a <= 90) || (a >= 97 && a <= 122)){
            return Primitiva.LETRA;
        }
        if(a == 32){
            return Primitiva.ESPACO;
        }
        if((a >=33 && a < 47) || (a >= 58 && a <= 64) || (a >= 91 && a <= 96) || (a >= 123 && a <= 126)){
            return Primitiva.SIMBOLO;            
        }       
        
        return Primitiva.OUTROS;
    }
    
}
