
package compiladores.sintatico;

/**
 *
 * @author Siloé
 */
public enum TipoDeErroSemantico {
    
    /*variavel não declarada, operação e atribuição entre tipos diferentes, atribuição constantes,
    quantidade de parametros de função invalida, tipo de parametro recebido invalido, uso fora do escopo*/
    VARIAVEL_NAO_DECLARADA, VARIAVEL_JA_EXISTENTE, REGISTRO_JA_EXISTENTE, FUNCAO_JA_EXISTENTE,
    OPERACAO_ENTRE_TIPOS_DIFERENTES, ATRIBUICAO_ENTRE_TIPOS_DIFERENTES,
    ATRIBUINDO_CONSTANTE, QUANTIDADE_DE_PARAMETROS_INVALIDA, PARAMETRO_TIPO_INVALIDO, 
    TIPO_DE_RETORNO_INVALIDO;
    
}
