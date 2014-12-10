package compiladores.sintatico;

import compiladores.Token;
import compiladores.enums.TipoToken;
import java.util.ArrayList;

/**
 *
 * @author Siloé
 */
public class EditorDeNaoTerminaisPraPro {

    private static ArrayList<ErroSintatico> listaErros;
  

    public static void tratarErros(ArrayList<Token> listaTokens, int linha, String encontrado, String esperado) {
        ErroSintatico erro = new ErroSintatico(linha, encontrado, esperado);
        listaErros.add(erro);

        if (esperado.equals(";")) {
            int i = 0;
            while (i < listaTokens.size()) {
                String lexema = listaTokens.get(0 + i).getLexema();
                if (lexema.equals(";")) {
                    removePrimeiroItemDaLista(listaTokens);
                    break;
                } else if (listaTokens.get(0 + i).getTipoToken() == TipoToken.PALAVRA_RESERVADA) {
                    break;
                } else if (listaTokens.get(0 + i).getTipoToken() == TipoToken.DELIMITADOR_FECHAR_CHAVES) {
                    break;
                } else {
                    removePrimeiroItemDaLista(listaTokens);
                }
            }
        }
    }

    public static ArrayList<ErroSintatico> setNaoTerminais(final AnalisadorSintatico analisadorSintatico) {
        listaErros = new ArrayList<>();

        analisadorSintatico.addNaoTerminal(new NaoTerminal("id", new ExecutorNaoTerminal() {
            @Override
            public void executarLeitura(ArrayList<Token> listaTokens) {
                //EditorDeNaoTerminais.removePrimeiroItemDaLista(listaTokens);
                if (listaTokens.get(0).getTipoToken() == TipoToken.IDENTIFICADOR) {
                    EditorDeNaoTerminaisPraPro.removePrimeiroItemDaLista(listaTokens);
                } else {
                    tratarErros(listaTokens, listaTokens.get(0).getLinha(), listaTokens.get(0).getLexema(), "identificador");
                }
            }
        }));

        analisadorSintatico.addNaoTerminal(new NaoTerminal("lista_registros", new ExecutorNaoTerminal() {
            @Override
            public void executarLeitura(ArrayList<Token> listaTokens) {
                //EditorDeNaoTerminais.removePrimeiroItemDaLista(listaTokens);                
                if (listaTokens.get(0).getLexema().equals("registro")) {
                    analisadorSintatico.getNaoTerminal("registro").executar(listaTokens);
                    analisadorSintatico.getNaoTerminal("lista_registros").executar(listaTokens);
                } else if (listaTokens.get(0).getLexema().equals("constantes")) {
                    analisadorSintatico.getNaoTerminal("const_vars_proc").executar(listaTokens);
                } else {
                    tratarErros(listaTokens, listaTokens.get(0).getLinha(), listaTokens.get(0).getLexema(), "registrou ou constantes");
                }

            }
        }));

        analisadorSintatico.addNaoTerminal(new NaoTerminal("const_vars_proc", new ExecutorNaoTerminal() {
            @Override
            public void executarLeitura(ArrayList<Token> listaTokens) {
                //EditorDeNaoTerminais.removePrimeiroItemDaLista(listaTokens);
                if (listaTokens.get(0).getLexema().equals("constantes")) {
                    analisadorSintatico.getNaoTerminal("bloco_constantes").executar(listaTokens);
                    analisadorSintatico.getNaoTerminal("bloco_variaveis").executar(listaTokens);
                    analisadorSintatico.getNaoTerminal("lista_funcoes").executar(listaTokens);
                } else {
                    tratarErros(listaTokens, listaTokens.get(0).getLinha(), listaTokens.get(0).getLexema(), "constantes");
                }
            }
        }));

        analisadorSintatico.addNaoTerminal(new NaoTerminal("registro", new ExecutorNaoTerminal() {
            @Override
            public void executarLeitura(ArrayList<Token> listaTokens) {
                //EditorDeNaoTerminais.removePrimeiroItemDaLista(listaTokens);
                if (listaTokens.get(0).getLexema().equals("registro")) {
                    EditorDeNaoTerminaisPraPro.removePrimeiroItemDaLista(listaTokens);
                    
                    analisadorSintatico.getNaoTerminal("id").executar(listaTokens);
                    if (listaTokens.get(0).getLexema().equals("{")) {
                        EditorDeNaoTerminaisPraPro.removePrimeiroItemDaLista(listaTokens);
                        analisadorSintatico.getNaoTerminal("lista_campos").executar(listaTokens);
                    } else {
                        tratarErros(listaTokens, listaTokens.get(0).getLinha(), listaTokens.get(0).getLexema(), "}");
                    }
                } else {
                    tratarErros(listaTokens, listaTokens.get(0).getLinha(), listaTokens.get(0).getLexema(), "registrou");
                }
            }
        }));

        analisadorSintatico.addNaoTerminal(new NaoTerminal("lista_campos", new ExecutorNaoTerminal() {
            @Override
            public void executarLeitura(ArrayList<Token> listaTokens) {
                //EditorDeNaoTerminais.removePrimeiroItemDaLista(listaTokens);
                String temp = listaTokens.get(0).getLexema();
                if (temp.equals("inteiro") || temp.equals("real") || temp.equals("booleano")
                        || temp.equals("char") || temp.equals("cadeia")) {

                    analisadorSintatico.getNaoTerminal("campo").executar(listaTokens);
                    analisadorSintatico.getNaoTerminal("lista_campos").executar(listaTokens);
                } else if (listaTokens.get(0).getLexema().equals("}")) {
                    EditorDeNaoTerminaisPraPro.removePrimeiroItemDaLista(listaTokens);
                } else {
                    tratarErros(listaTokens, listaTokens.get(0).getLinha(), listaTokens.get(0).getLexema(),
                            "inteiro ou real ou booleano ou char ou cadeia ou }");
                }
            }
        }));

        analisadorSintatico.addNaoTerminal(new NaoTerminal("campo", new ExecutorNaoTerminal() {
            @Override
            public void executarLeitura(ArrayList<Token> listaTokens) {
                //EditorDeNaoTerminais.removePrimeiroItemDaLista(listaTokens);
                String temp = listaTokens.get(0).getLexema();
                if (temp.equals("inteiro") || temp.equals("real") || temp.equals("booleano")
                        || temp.equals("char") || temp.equals("cadeia")) {

                    analisadorSintatico.getNaoTerminal("tipo_primitivo").executar(listaTokens);
                    analisadorSintatico.getNaoTerminal("id").executar(listaTokens);

                    if (listaTokens.get(0).getLexema().equals(";")) {
                        EditorDeNaoTerminaisPraPro.removePrimeiroItemDaLista(listaTokens);
                    } else {
                        tratarErros(listaTokens, listaTokens.get(0).getLinha(), listaTokens.get(0).getLexema(), ";");
                    }
                } else {
                    tratarErros(listaTokens, listaTokens.get(0).getLinha(), listaTokens.get(0).getLexema(),
                            "inteiro ou real ou booleano ou char ou cadeia");
                }
            }
        }));

        analisadorSintatico.addNaoTerminal(new NaoTerminal("bloco_constantes", new ExecutorNaoTerminal() {
            @Override
            public void executarLeitura(ArrayList<Token> listaTokens) {
                //EditorDeNaoTerminais.removePrimeiroItemDaLista(listaTokens);                
                if (listaTokens.get(0).getLexema().equals("constantes")) {
                    EditorDeNaoTerminaisPraPro.removePrimeiroItemDaLista(listaTokens);
                    if (listaTokens.get(0).getLexema().equals("{")) {
                        EditorDeNaoTerminaisPraPro.removePrimeiroItemDaLista(listaTokens);
                        analisadorSintatico.getNaoTerminal("lista_constantes").executar(listaTokens);
                    } else {
                        tratarErros(listaTokens, listaTokens.get(0).getLinha(), listaTokens.get(0).getLexema(), "{");
                    }
                } else {
                    tratarErros(listaTokens, listaTokens.get(0).getLinha(), listaTokens.get(0).getLexema(), "constantes");
                }
            }
        }));

        analisadorSintatico.addNaoTerminal(new NaoTerminal("lista_constantes", new ExecutorNaoTerminal() {
            @Override
            public void executarLeitura(ArrayList<Token> listaTokens) {
                //EditorDeNaoTerminais.removePrimeiroItemDaLista(listaTokens);                
                String temp = listaTokens.get(0).getLexema();
                if (temp.equals("inteiro") || temp.equals("real") || temp.equals("booleano")
                        || temp.equals("char") || temp.equals("cadeia")) {

                    analisadorSintatico.getNaoTerminal("constante").executar(listaTokens);
                    analisadorSintatico.getNaoTerminal("lista_constantes").executar(listaTokens);
                } else if (listaTokens.get(0).getLexema().equals("}")) {
                    EditorDeNaoTerminaisPraPro.removePrimeiroItemDaLista(listaTokens);
                } else {
                    tratarErros(listaTokens, listaTokens.get(0).getLinha(), listaTokens.get(0).getLexema(),
                            "inteiro ou real ou booleano ou char ou cadeia ou }");
                }
            }
        }));

        analisadorSintatico.addNaoTerminal(new NaoTerminal("constante", new ExecutorNaoTerminal() {
            @Override
            public void executarLeitura(ArrayList<Token> listaTokens) {
                //EditorDeNaoTerminais.removePrimeiroItemDaLista(listaTokens);                
                String temp = listaTokens.get(0).getLexema();
                if (temp.equals("inteiro") || temp.equals("real") || temp.equals("booleano")
                        || temp.equals("char") || temp.equals("cadeia")) {
                    
                    analisadorSintatico.getNaoTerminal("tipo_primitivo").executar(listaTokens);
                    analisadorSintatico.getNaoTerminal("id").executar(listaTokens);
                    if (listaTokens.get(0).getLexema().equals("=")) {
                        EditorDeNaoTerminaisPraPro.removePrimeiroItemDaLista(listaTokens);
                    } else {
                        tratarErros(listaTokens, listaTokens.get(0).getLinha(), listaTokens.get(0).getLexema(), "=");
                    }
                    analisadorSintatico.getNaoTerminal("valor").executar(listaTokens);
                    if (listaTokens.get(0).getLexema().equals(";")) {
                        EditorDeNaoTerminaisPraPro.removePrimeiroItemDaLista(listaTokens);
                    } else {
                        tratarErros(listaTokens, listaTokens.get(0).getLinha(), listaTokens.get(0).getLexema(), ";");
                    }
                } else {
                    tratarErros(listaTokens, listaTokens.get(0).getLinha(), listaTokens.get(0).getLexema(),
                            "inteiro ou real ou booleano ou char ou cadeia");
                }
            }
        }));

        analisadorSintatico.addNaoTerminal(new NaoTerminal("tipo_primitivo", new ExecutorNaoTerminal() {
            @Override
            public void executarLeitura(ArrayList<Token> listaTokens) {
                //EditorDeNaoTerminais.removePrimeiroItemDaLista(listaTokens);                
                String temp = listaTokens.get(0).getLexema();
                if (temp.equals("inteiro") || temp.equals("real") || temp.equals("booleano")
                        || temp.equals("char") || temp.equals("cadeia")) {
                    EditorDeNaoTerminaisPraPro.removePrimeiroItemDaLista(listaTokens);
                } else {
                    tratarErros(listaTokens, listaTokens.get(0).getLinha(), listaTokens.get(0).getLexema(),
                            "inteiro ou real ou booleano ou char ou cadeia");
                }
            }
        }));

        analisadorSintatico.addNaoTerminal(new NaoTerminal("valor", new ExecutorNaoTerminal() {
            @Override
            public void executarLeitura(ArrayList<Token> listaTokens) {
                //EditorDeNaoTerminais.removePrimeiroItemDaLista(listaTokens);
                TipoToken tipoToken = listaTokens.get(0).getTipoToken();

                if ((tipoToken == TipoToken.NUMERO_INTEIRO) || (tipoToken == TipoToken.NUMERO_REAL)
                        || (listaTokens.get(0).getLexema().equals("true"))
                        || (listaTokens.get(0).getLexema().equals("false"))
                        || (tipoToken == TipoToken.CARACTERE_CONSTANTE) || (tipoToken == TipoToken.CADEIA_CONSTANTE)) {
                    EditorDeNaoTerminaisPraPro.removePrimeiroItemDaLista(listaTokens);
                } else {
                    tratarErros(listaTokens, listaTokens.get(0).getLinha(), listaTokens.get(0).getLexema(),
                            "valor inteiro ou real ou booleano ou char ou cadeia");
                }
            }
        }));

        analisadorSintatico.addNaoTerminal(new NaoTerminal("numero", new ExecutorNaoTerminal() {
            @Override
            public void executarLeitura(ArrayList<Token> listaTokens) {
                //EditorDeNaoTerminais.removePrimeiroItemDaLista(listaTokens);
                TipoToken tipoToken = listaTokens.get(0).getTipoToken();
                if ((tipoToken == TipoToken.NUMERO_INTEIRO) || (tipoToken == TipoToken.NUMERO_REAL)) {
                    EditorDeNaoTerminaisPraPro.removePrimeiroItemDaLista(listaTokens);
                } else {
                    tratarErros(listaTokens, listaTokens.get(0).getLinha(), listaTokens.get(0).getLexema(),
                            "inteiro ou real");
                }
            }
        }));

        analisadorSintatico.addNaoTerminal(new NaoTerminal("bloco_variaveis", new ExecutorNaoTerminal() {
            @Override
            public void executarLeitura(ArrayList<Token> listaTokens) {
                //EditorDeNaoTerminais.removePrimeiroItemDaLista(listaTokens);                
                if (listaTokens.get(0).getLexema().equals("variaveis")) {
                    EditorDeNaoTerminaisPraPro.removePrimeiroItemDaLista(listaTokens);
                    if (listaTokens.get(0).getLexema().equals("{")) {
                        EditorDeNaoTerminaisPraPro.removePrimeiroItemDaLista(listaTokens);
                        analisadorSintatico.getNaoTerminal("lista_variaveis").executar(listaTokens);
                    } else {
                        tratarErros(listaTokens, listaTokens.get(0).getLinha(), listaTokens.get(0).getLexema(), "{");
                    }
                } else {
                    tratarErros(listaTokens, listaTokens.get(0).getLinha(), listaTokens.get(0).getLexema(), "variaveis");
                }
            }
        }));

        analisadorSintatico.addNaoTerminal(new NaoTerminal("lista_variaveis", new ExecutorNaoTerminal() {
            @Override
            public void executarLeitura(ArrayList<Token> listaTokens) {
                //EditorDeNaoTerminais.removePrimeiroItemDaLista(listaTokens);                
                String temp = listaTokens.get(0).getLexema();
                if ((listaTokens.get(0).getTipoToken() == TipoToken.IDENTIFICADOR) || temp.equals("inteiro")
                        || temp.equals("real") || temp.equals("booleano")
                        || temp.equals("char") || temp.equals("cadeia")) {
                    analisadorSintatico.getNaoTerminal("variaveis").executar(listaTokens);
                    analisadorSintatico.getNaoTerminal("lista_variaveis").executar(listaTokens);
                } else if (listaTokens.get(0).getLexema().equals("}")) {
                    EditorDeNaoTerminaisPraPro.removePrimeiroItemDaLista(listaTokens);
                } else {
                    tratarErros(listaTokens, listaTokens.get(0).getLinha(), listaTokens.get(0).getLexema(),
                            "identificador ou inteiro ou real ou booleano ou char ou cadeia ou }");
                }
            }
        }));

        analisadorSintatico.addNaoTerminal(new NaoTerminal("variaveis", new ExecutorNaoTerminal() {
            @Override
            public void executarLeitura(ArrayList<Token> listaTokens) {
                //EditorDeNaoTerminais.removePrimeiroItemDaLista(listaTokens);
                String temp = listaTokens.get(0).getLexema();
                if (temp.equals("inteiro")
                        || temp.equals("real") || temp.equals("booleano")
                        || temp.equals("char") || temp.equals("cadeia")) {


                    analisadorSintatico.getNaoTerminal("tipo_primitivo").executar(listaTokens);
                    analisadorSintatico.getNaoTerminal("id").executar(listaTokens);
                    analisadorSintatico.getNaoTerminal("compl_declaracao_var").executar(listaTokens);
                } else if (listaTokens.get(0).getTipoToken() == TipoToken.IDENTIFICADOR) {
                    analisadorSintatico.getNaoTerminal("id").executar(listaTokens);
                    analisadorSintatico.getNaoTerminal("id").executar(listaTokens);
                    if (listaTokens.get(0).getLexema().equals(";")) {
                        EditorDeNaoTerminaisPraPro.removePrimeiroItemDaLista(listaTokens);
                    } else {
                        tratarErros(listaTokens, listaTokens.get(0).getLinha(), listaTokens.get(0).getLexema(), ";");
                    }
                } else {
                    tratarErros(listaTokens, listaTokens.get(0).getLinha(), listaTokens.get(0).getLexema(),
                            "identificador ou inteiro ou real ou booleano ou char ou cadeia");
                }
            }
        }));

        analisadorSintatico.addNaoTerminal(new NaoTerminal("compl_declaracao_var", new ExecutorNaoTerminal() {
            @Override
            public void executarLeitura(ArrayList<Token> listaTokens) {
                //EditorDeNaoTerminais.removePrimeiroItemDaLista(listaTokens);                
                String temp = listaTokens.get(0).getLexema();
                if (temp.equals("=")) {
                    EditorDeNaoTerminaisPraPro.removePrimeiroItemDaLista(listaTokens);
                    analisadorSintatico.getNaoTerminal("valor").executar(listaTokens);
                    if (listaTokens.get(0).getLexema().equals(";")) {
                        EditorDeNaoTerminaisPraPro.removePrimeiroItemDaLista(listaTokens);
                    } else {
                        tratarErros(listaTokens, listaTokens.get(0).getLinha(), listaTokens.get(0).getLexema(), ";");
                    }
                } else if (listaTokens.get(0).getLexema().equals("[") || listaTokens.get(0).getLexema().equals(";")) {
                    analisadorSintatico.getNaoTerminal("definicao_matriz").executar(listaTokens);
                } else {
                    tratarErros(listaTokens, listaTokens.get(0).getLinha(), listaTokens.get(0).getLexema(), "= ou [ ou ;");
                }
            }
        }));

        analisadorSintatico.addNaoTerminal(new NaoTerminal("definicao_matriz", new ExecutorNaoTerminal() {
            @Override
            public void executarLeitura(ArrayList<Token> listaTokens) {
                //EditorDeNaoTerminais.removePrimeiroItemDaLista(listaTokens);                
                String temp = listaTokens.get(0).getLexema();
                if (temp.equals("[")) {
                    EditorDeNaoTerminaisPraPro.removePrimeiroItemDaLista(listaTokens);
                    if (listaTokens.get(0).getTipoToken() == TipoToken.NUMERO_INTEIRO) {
                        EditorDeNaoTerminaisPraPro.removePrimeiroItemDaLista(listaTokens);
                    } else {
                        tratarErros(listaTokens, listaTokens.get(0).getLinha(), listaTokens.get(0).getLexema(), "inteiro");
                    }
                    if (listaTokens.get(0).getLexema().equals("]")) {
                        EditorDeNaoTerminaisPraPro.removePrimeiroItemDaLista(listaTokens);
                    } else {
                        tratarErros(listaTokens, listaTokens.get(0).getLinha(), listaTokens.get(0).getLexema(), "]");
                    }
                } else if (listaTokens.get(0).getLexema().equals(";")) {
                    EditorDeNaoTerminaisPraPro.removePrimeiroItemDaLista(listaTokens);
                } else {
                    tratarErros(listaTokens, listaTokens.get(0).getLinha(), listaTokens.get(0).getLexema(), "[ ou ;");
                }
            }
        }));

        analisadorSintatico.addNaoTerminal(new NaoTerminal("chamada_funcao", new ExecutorNaoTerminal() {
            @Override
            public void executarLeitura(ArrayList<Token> listaTokens) {
                //EditorDeNaoTerminais.removePrimeiroItemDaLista(listaTokens);                
                String temp = listaTokens.get(0).getLexema();
                if (temp.equals("(")) {
                    EditorDeNaoTerminaisPraPro.removePrimeiroItemDaLista(listaTokens);
                    analisadorSintatico.getNaoTerminal("lista_argumentos").executar(listaTokens);
                } else {
                    tratarErros(listaTokens, listaTokens.get(0).getLinha(), listaTokens.get(0).getLexema(), "(");
                }
            }
        }));

        analisadorSintatico.addNaoTerminal(new NaoTerminal("chamada_funcao", new ExecutorNaoTerminal() {
            @Override
            public void executarLeitura(ArrayList<Token> listaTokens) {
                //EditorDeNaoTerminais.removePrimeiroItemDaLista(listaTokens);                
                String temp = listaTokens.get(0).getLexema();
                if (listaTokens.get(0).getLexema().equals("(")) {
                    EditorDeNaoTerminaisPraPro.removePrimeiroItemDaLista(listaTokens);
                    analisadorSintatico.getNaoTerminal("lista_argumentos").executar(listaTokens);
                } else {
                    tratarErros(listaTokens, listaTokens.get(0).getLinha(), listaTokens.get(0).getLexema(), "(");
                }
            }
        }));

        analisadorSintatico.addNaoTerminal(new NaoTerminal("lista_argumentos", new ExecutorNaoTerminal() {
            @Override
            public void executarLeitura(ArrayList<Token> listaTokens) {
                //EditorDeNaoTerminais.removePrimeiroItemDaLista(listaTokens);
                String temp = listaTokens.get(0).getLexema();
                if ((listaTokens.get(0).getTipoToken() == TipoToken.IDENTIFICADOR)) {
                    analisadorSintatico.getNaoTerminal("argumento").executar(listaTokens);
                    analisadorSintatico.getNaoTerminal("prox_argumento").executar(listaTokens);
                } else if (listaTokens.get(0).getLexema().equals("vazio")) {
                    EditorDeNaoTerminaisPraPro.removePrimeiroItemDaLista(listaTokens);
                    if (listaTokens.get(0).getLexema().equals(")")) {
                        EditorDeNaoTerminaisPraPro.removePrimeiroItemDaLista(listaTokens);
                    } else {
                        tratarErros(listaTokens, listaTokens.get(0).getLinha(), listaTokens.get(0).getLexema(), ")");
                    }

                } else {
                    tratarErros(listaTokens, listaTokens.get(0).getLinha(), listaTokens.get(0).getLexema(), "identificador ou vazio");
                }
            }
        }));

        analisadorSintatico.addNaoTerminal(new NaoTerminal("prox_argumento", new ExecutorNaoTerminal() {
            @Override
            public void executarLeitura(ArrayList<Token> listaTokens) {
                //EditorDeNaoTerminais.removePrimeiroItemDaLista(listaTokens);
                String temp = listaTokens.get(0).getLexema();
                if (listaTokens.get(0).getLexema().equals(",")) {
                    EditorDeNaoTerminaisPraPro.removePrimeiroItemDaLista(listaTokens);
                    analisadorSintatico.getNaoTerminal("argumento").executar(listaTokens);
                    analisadorSintatico.getNaoTerminal("prox_argumento").executar(listaTokens);
                } else if (listaTokens.get(0).getLexema().equals(")")) {
                    EditorDeNaoTerminaisPraPro.removePrimeiroItemDaLista(listaTokens);
                } else {
                    tratarErros(listaTokens, listaTokens.get(0).getLinha(), listaTokens.get(0).getLexema(), ", ou )");
                }
            }
        }));

        analisadorSintatico.addNaoTerminal(new NaoTerminal("argumento", new ExecutorNaoTerminal() {
            @Override
            public void executarLeitura(ArrayList<Token> listaTokens) {
                //EditorDeNaoTerminais.removePrimeiroItemDaLista(listaTokens);
                String temp = listaTokens.get(0).getLexema();
                if ((listaTokens.get(0).getTipoToken() == TipoToken.IDENTIFICADOR)) {
                    analisadorSintatico.getNaoTerminal("id").executar(listaTokens);
                    analisadorSintatico.getNaoTerminal("compl_tipo_parametro").executar(listaTokens);
                } else {
                    tratarErros(listaTokens, listaTokens.get(0).getLinha(), listaTokens.get(0).getLexema(), "identificador");
                }
            }
        }));

        analisadorSintatico.addNaoTerminal(new NaoTerminal("compl_tipo_parametro", new ExecutorNaoTerminal() {
            @Override
            public void executarLeitura(ArrayList<Token> listaTokens) {
                //EditorDeNaoTerminais.removePrimeiroItemDaLista(listaTokens);
                String temp = listaTokens.get(0).getLexema();
                if (temp.equals(".")) {
                    analisadorSintatico.getNaoTerminal("acesso_campo_registro").executar(listaTokens);
                } else if (temp.equals("[")) {
                    analisadorSintatico.getNaoTerminal("acesso_matriz").executar(listaTokens);
                } else if (temp.equals("(")) {
                    analisadorSintatico.getNaoTerminal("chamada_funcao").executar(listaTokens);
                }
                //tem lambda
            }
        }));

        analisadorSintatico.addNaoTerminal(new NaoTerminal("acesso_campo_registro", new ExecutorNaoTerminal() {
            @Override
            public void executarLeitura(ArrayList<Token> listaTokens) {
                //EditorDeNaoTerminais.removePrimeiroItemDaLista(listaTokens);
                String temp = listaTokens.get(0).getLexema();
                if (temp.equals(".")) {
                    EditorDeNaoTerminaisPraPro.removePrimeiroItemDaLista(listaTokens);
                    analisadorSintatico.getNaoTerminal("id").executar(listaTokens);
                } else {
                    tratarErros(listaTokens, listaTokens.get(0).getLinha(), listaTokens.get(0).getLexema(), ".");
                }
            }
        }));

        analisadorSintatico.addNaoTerminal(new NaoTerminal("acesso_matriz", new ExecutorNaoTerminal() {
            @Override
            public void executarLeitura(ArrayList<Token> listaTokens) {
                //EditorDeNaoTerminais.removePrimeiroItemDaLista(listaTokens);
                String temp = listaTokens.get(0).getLexema();
                if (temp.equals("[")) {
                    EditorDeNaoTerminaisPraPro.removePrimeiroItemDaLista(listaTokens);
                    analisadorSintatico.getNaoTerminal("indice").executar(listaTokens);
                    if (listaTokens.get(0).getLexema().equals("]")) {
                        EditorDeNaoTerminaisPraPro.removePrimeiroItemDaLista(listaTokens);
                    } else {
                        tratarErros(listaTokens, listaTokens.get(0).getLinha(), listaTokens.get(0).getLexema(), "]");
                    }
                    analisadorSintatico.getNaoTerminal("acesso_matriz").executar(listaTokens);
                }
            }
        }));

        analisadorSintatico.addNaoTerminal(new NaoTerminal("indice", new ExecutorNaoTerminal() {
            @Override
            public void executarLeitura(ArrayList<Token> listaTokens) {
                //EditorDeNaoTerminais.removePrimeiroItemDaLista(listaTokens);
                String temp = listaTokens.get(0).getLexema();
                if ((listaTokens.get(0).getTipoToken() == TipoToken.NUMERO_INTEIRO)
                        || (listaTokens.get(0).getTipoToken() == TipoToken.IDENTIFICADOR)) {
                    EditorDeNaoTerminaisPraPro.removePrimeiroItemDaLista(listaTokens);
                } else {
                    tratarErros(listaTokens, listaTokens.get(0).getLinha(), listaTokens.get(0).getLexema(), "inteiro ou identificador");
                }
            }
        }));

        analisadorSintatico.addNaoTerminal(new NaoTerminal("lista_funcoes", new ExecutorNaoTerminal() {
            @Override
            public void executarLeitura(ArrayList<Token> listaTokens) {
                //EditorDeNaoTerminais.removePrimeiroItemDaLista(listaTokens);
                String temp = listaTokens.get(0).getLexema();
                if (temp.equals("funcao")) {
                    analisadorSintatico.getNaoTerminal("funcao").executar(listaTokens);
                    analisadorSintatico.getNaoTerminal("lista_funcoes").executar(listaTokens);
                } else if (temp.equals("algoritimo")) {
                    analisadorSintatico.getNaoTerminal("algoritimo").executar(listaTokens);
                } else {
                    tratarErros(listaTokens, listaTokens.get(0).getLinha(), listaTokens.get(0).getLexema(), "funcao ou algoritimo");
                }
            }
        }));

        analisadorSintatico.addNaoTerminal(new NaoTerminal("funcao", new ExecutorNaoTerminal() {
            @Override
            public void executarLeitura(ArrayList<Token> listaTokens) {
                //EditorDeNaoTerminais.removePrimeiroItemDaLista(listaTokens);
                String temp = listaTokens.get(0).getLexema();
                if (temp.equals("funcao")) {
                    EditorDeNaoTerminaisPraPro.removePrimeiroItemDaLista(listaTokens);
                    analisadorSintatico.getNaoTerminal("tipo_arg_retorno").executar(listaTokens);
                    analisadorSintatico.getNaoTerminal("id").executar(listaTokens);
                    if (listaTokens.get(0).getLexema().equals("(")) {
                        EditorDeNaoTerminaisPraPro.removePrimeiroItemDaLista(listaTokens);
                    } else {
                        tratarErros(listaTokens, listaTokens.get(0).getLinha(), listaTokens.get(0).getLexema(), "(");
                    }
                    analisadorSintatico.getNaoTerminal("lista_parametros").executar(listaTokens);
                    if (listaTokens.get(0).getLexema().equals("{")) {
                        EditorDeNaoTerminaisPraPro.removePrimeiroItemDaLista(listaTokens);
                    } else {
                        tratarErros(listaTokens, listaTokens.get(0).getLinha(), listaTokens.get(0).getLexema(), "{");
                    }
                    analisadorSintatico.getNaoTerminal("corpo").executar(listaTokens);
                    analisadorSintatico.getNaoTerminal("inst_retorno").executar(listaTokens);
                    if (listaTokens.get(0).getLexema().equals("}")) {
                        EditorDeNaoTerminaisPraPro.removePrimeiroItemDaLista(listaTokens);
                    } else {
                        tratarErros(listaTokens, listaTokens.get(0).getLinha(), listaTokens.get(0).getLexema(), "}");
                    }
                } else {
                    tratarErros(listaTokens, listaTokens.get(0).getLinha(), listaTokens.get(0).getLexema(), "funcao");
                }
            }
        }));

        analisadorSintatico.addNaoTerminal(new NaoTerminal("tipo_arg_retorno", new ExecutorNaoTerminal() {
            @Override
            public void executarLeitura(ArrayList<Token> listaTokens) {
                //EditorDeNaoTerminais.removePrimeiroItemDaLista(listaTokens);
                String temp = listaTokens.get(0).getLexema();
                if (temp.equals("inteiro") || temp.equals("real") || temp.equals("booleano")
                        || temp.equals("char") || temp.equals("cadeia")) {
                    analisadorSintatico.getNaoTerminal("tipo_primitivo").executar(listaTokens);
                } else if (listaTokens.get(0).getTipoToken() == TipoToken.IDENTIFICADOR) {
                    analisadorSintatico.getNaoTerminal("id").executar(listaTokens);
                } else if (temp.equals("vazio")) {
                    EditorDeNaoTerminaisPraPro.removePrimeiroItemDaLista(listaTokens);
                } else {
                    tratarErros(listaTokens, listaTokens.get(0).getLinha(), listaTokens.get(0).getLexema(),
                            "inteiro ou real ou booleano ou char ou cadeia ou identificador ou vazio");
                }
            }
        }));

        analisadorSintatico.addNaoTerminal(new NaoTerminal("lista_parametros", new ExecutorNaoTerminal() {
            @Override
            public void executarLeitura(ArrayList<Token> listaTokens) {
                //EditorDeNaoTerminais.removePrimeiroItemDaLista(listaTokens);
                String temp = listaTokens.get(0).getLexema();
                if (temp.equals("inteiro") || temp.equals("real") || temp.equals("booleano")
                        || temp.equals("char") || temp.equals("cadeia")
                        || (listaTokens.get(0).getTipoToken() == TipoToken.IDENTIFICADOR)) {
                    analisadorSintatico.getNaoTerminal("parametro").executar(listaTokens);
                    analisadorSintatico.getNaoTerminal("prox_parametro").executar(listaTokens);
                } else if (temp.equals("vazio")) {
                    EditorDeNaoTerminaisPraPro.removePrimeiroItemDaLista(listaTokens);
                    if (listaTokens.get(0).getLexema().equals(")")) {
                        EditorDeNaoTerminaisPraPro.removePrimeiroItemDaLista(listaTokens);
                    } else {
                        tratarErros(listaTokens, listaTokens.get(0).getLinha(), listaTokens.get(0).getLexema(), ")");
                    }
                } else {
                    tratarErros(listaTokens, listaTokens.get(0).getLinha(), listaTokens.get(0).getLexema(),
                            "inteiro ou real ou booleano ou char ou cadeia ou identificador ou vazio");
                }
            }
        }));

        analisadorSintatico.addNaoTerminal(new NaoTerminal("prox_parametro", new ExecutorNaoTerminal() {
            @Override
            public void executarLeitura(ArrayList<Token> listaTokens) {
                //EditorDeNaoTerminais.removePrimeiroItemDaLista(listaTokens);
                if (listaTokens.get(0).getLexema().equals(",")) {
                    EditorDeNaoTerminaisPraPro.removePrimeiroItemDaLista(listaTokens);
                    analisadorSintatico.getNaoTerminal("parametro").executar(listaTokens);
                    analisadorSintatico.getNaoTerminal("prox_parametro").executar(listaTokens);
                } else if (listaTokens.get(0).getLexema().equals(")")) {
                    EditorDeNaoTerminaisPraPro.removePrimeiroItemDaLista(listaTokens);
                } else {
                    tratarErros(listaTokens, listaTokens.get(0).getLinha(), listaTokens.get(0).getLexema(),
                            ", ou )");
                }
            }
        }));

        analisadorSintatico.addNaoTerminal(new NaoTerminal("parametro", new ExecutorNaoTerminal() {
            @Override
            public void executarLeitura(ArrayList<Token> listaTokens) {
                //EditorDeNaoTerminais.removePrimeiroItemDaLista(listaTokens);
                String temp = listaTokens.get(0).getLexema();
                if (listaTokens.get(0).getLexema().equals("inteiro") || listaTokens.get(0).getLexema().equals("real") 
                        || listaTokens.get(0).getLexema().equals("booleano")
                        || listaTokens.get(0).getLexema().equals("char")
                        || listaTokens.get(0).getLexema().equals("cadeia")
                        || (listaTokens.get(0).getTipoToken() == TipoToken.IDENTIFICADOR)) {
                    analisadorSintatico.getNaoTerminal("tipo_parametro").executar(listaTokens);
                    analisadorSintatico.getNaoTerminal("id").executar(listaTokens);
                    analisadorSintatico.getNaoTerminal("colchete").executar(listaTokens);
                } else {
                    tratarErros(listaTokens, listaTokens.get(0).getLinha(), listaTokens.get(0).getLexema(),
                            "inteiro ou real ou booleano ou char ou cadeia ou identificador");
                }
            }
        }));

        analisadorSintatico.addNaoTerminal(new NaoTerminal("tipo_parametro", new ExecutorNaoTerminal() {
            @Override
            public void executarLeitura(ArrayList<Token> listaTokens) {
                //EditorDeNaoTerminais.removePrimeiroItemDaLista(listaTokens);
                String temp = listaTokens.get(0).getLexema();
                if (temp.equals("inteiro") || temp.equals("real") || temp.equals("booleano")
                        || temp.equals("char") || temp.equals("cadeia")) {
                    analisadorSintatico.getNaoTerminal("tipo_primitivo").executar(listaTokens);
                } else if (listaTokens.get(0).getTipoToken() == TipoToken.IDENTIFICADOR) {
                    analisadorSintatico.getNaoTerminal("id").executar(listaTokens);
                } else {
                    tratarErros(listaTokens, listaTokens.get(0).getLinha(), listaTokens.get(0).getLexema(),
                            "inteiro ou real ou booleano ou char ou cadeia ou identificador");
                }
            }
        }));

        analisadorSintatico.addNaoTerminal(new NaoTerminal("colchete", new ExecutorNaoTerminal() {
            @Override
            public void executarLeitura(ArrayList<Token> listaTokens) {
                //EditorDeNaoTerminais.removePrimeiroItemDaLista(listaTokens);
                String temp = listaTokens.get(0).getLexema();
                if (temp.equals("[")) {
                    EditorDeNaoTerminaisPraPro.removePrimeiroItemDaLista(listaTokens);
                    analisadorSintatico.getNaoTerminal("inteiro").executar(listaTokens);
                    if ((listaTokens.get(0).getTipoToken() == TipoToken.NUMERO_INTEIRO)) {
                        EditorDeNaoTerminaisPraPro.removePrimeiroItemDaLista(listaTokens);
                    } else {
                        tratarErros(listaTokens, listaTokens.get(0).getLinha(), listaTokens.get(0).getLexema(), "inteiro");
                    }
                    if (listaTokens.get(0).getLexema().equals("]")) {
                        EditorDeNaoTerminaisPraPro.removePrimeiroItemDaLista(listaTokens);
                    } else {
                        tratarErros(listaTokens, listaTokens.get(0).getLinha(), listaTokens.get(0).getLexema(), "]");
                    }
                    analisadorSintatico.getNaoTerminal("colchete").executar(listaTokens);
                }
                //tem lambda
            }
        }));

        analisadorSintatico.addNaoTerminal(new NaoTerminal("corpo", new ExecutorNaoTerminal() {
            @Override
            public void executarLeitura(ArrayList<Token> listaTokens) {
                //EditorDeNaoTerminais.removePrimeiroItemDaLista(listaTokens);
                String temp = listaTokens.get(0).getLexema();
                if (temp.equals("variaveis")) {
                    analisadorSintatico.getNaoTerminal("bloco_variaveis").executar(listaTokens);
                    analisadorSintatico.getNaoTerminal("lista_comandos").executar(listaTokens);
                } else {
                    tratarErros(listaTokens, listaTokens.get(0).getLinha(), listaTokens.get(0).getLexema(), "variaveis");
                }
            }
        }));

        analisadorSintatico.addNaoTerminal(new NaoTerminal("corpo", new ExecutorNaoTerminal() {
            @Override
            public void executarLeitura(ArrayList<Token> listaTokens) {
                //EditorDeNaoTerminais.removePrimeiroItemDaLista(listaTokens);
                String temp = listaTokens.get(0).getLexema();
                if (temp.equals("variaveis")) {
                    analisadorSintatico.getNaoTerminal("bloco_variaveis").executar(listaTokens);
                    analisadorSintatico.getNaoTerminal("lista_comandos").executar(listaTokens);
                } else {
                    tratarErros(listaTokens, listaTokens.get(0).getLinha(), listaTokens.get(0).getLexema(), "variaveis");
                }
            }
        }));

        analisadorSintatico.addNaoTerminal(new NaoTerminal("inst_retorno", new ExecutorNaoTerminal() {
            @Override
            public void executarLeitura(ArrayList<Token> listaTokens) {
                //EditorDeNaoTerminais.removePrimeiroItemDaLista(listaTokens);
                String temp = listaTokens.get(0).getLexema();
                if (temp.equals("retorno")) {
                    EditorDeNaoTerminaisPraPro.removePrimeiroItemDaLista(listaTokens);
                    analisadorSintatico.getNaoTerminal("argumento_retorno").executar(listaTokens);
                    if (listaTokens.get(0).getLexema().equals(";")) {
                        EditorDeNaoTerminaisPraPro.removePrimeiroItemDaLista(listaTokens);
                    } else {
                        tratarErros(listaTokens, listaTokens.get(0).getLinha(), listaTokens.get(0).getLexema(), ";");
                    }
                } else {
                    tratarErros(listaTokens, listaTokens.get(0).getLinha(), listaTokens.get(0).getLexema(), "retorno");
                }
            }
        }));

        analisadorSintatico.addNaoTerminal(new NaoTerminal("argumento_retorno", new ExecutorNaoTerminal() {
            @Override
            public void executarLeitura(ArrayList<Token> listaTokens) {
                //EditorDeNaoTerminais.removePrimeiroItemDaLista(listaTokens);
                String temp = listaTokens.get(0).getLexema();
                if (listaTokens.get(0).getTipoToken() == TipoToken.NUMERO_INTEIRO
                        || listaTokens.get(0).getTipoToken() == TipoToken.NUMERO_REAL
                        || listaTokens.get(0).getLexema().equals("true")
                        || listaTokens.get(0).getLexema().equals("false")
                        || listaTokens.get(0).getTipoToken() == TipoToken.CARACTERE_CONSTANTE
                        || listaTokens.get(0).getTipoToken() == TipoToken.CADEIA_CONSTANTE) {
                    
                    analisadorSintatico.getNaoTerminal("valor").executar(listaTokens);
                } else if (listaTokens.get(0).getTipoToken() == TipoToken.IDENTIFICADOR) {
                    analisadorSintatico.getNaoTerminal("argumento").executar(listaTokens);
                } else if(temp.equals("vazio")){
                    EditorDeNaoTerminaisPraPro.removePrimeiroItemDaLista(listaTokens);
                }else{
                    tratarErros(listaTokens, listaTokens.get(0).getLinha(), listaTokens.get(0).getLexema(),
                            "inteiro ou real ou booleano ou char ou cadeia ou vazio ou identificador");
                }
            }
        }));

        analisadorSintatico.addNaoTerminal(new NaoTerminal("algoritmo", new ExecutorNaoTerminal() {
            @Override
            public void executarLeitura(ArrayList<Token> listaTokens) {
                //EditorDeNaoTerminais.removePrimeiroItemDaLista(listaTokens);
                String temp = listaTokens.get(0).getLexema();
                if (temp.equals("algoritimo")) {
                    EditorDeNaoTerminaisPraPro.removePrimeiroItemDaLista(listaTokens);
                    if (listaTokens.get(0).getLexema().equals("{")) {
                        EditorDeNaoTerminaisPraPro.removePrimeiroItemDaLista(listaTokens);
                    } else {
                        tratarErros(listaTokens, listaTokens.get(0).getLinha(), listaTokens.get(0).getLexema(), "{");
                    }
                    analisadorSintatico.getNaoTerminal("corpo").executar(listaTokens);
                    if (listaTokens.get(0).getLexema().equals("}")) {
                        EditorDeNaoTerminaisPraPro.removePrimeiroItemDaLista(listaTokens);
                    } else {
                        tratarErros(listaTokens, listaTokens.get(0).getLinha(), listaTokens.get(0).getLexema(), "}");
                    }
                } else {
                    tratarErros(listaTokens, listaTokens.get(0).getLinha(), listaTokens.get(0).getLexema(), "algoritimo");
                }
            }
        }));

        analisadorSintatico.addNaoTerminal(new NaoTerminal("algoritimo", new ExecutorNaoTerminal() {
            @Override
            public void executarLeitura(ArrayList<Token> listaTokens) {
                //EditorDeNaoTerminais.removePrimeiroItemDaLista(listaTokens);
                String temp = listaTokens.get(0).getLexema();
                if (temp.equals("algoritimo")) {
                    EditorDeNaoTerminaisPraPro.removePrimeiroItemDaLista(listaTokens);
                    if (listaTokens.get(0).getLexema().equals("{")) {
                        EditorDeNaoTerminaisPraPro.removePrimeiroItemDaLista(listaTokens);
                    } else {
                        tratarErros(listaTokens, listaTokens.get(0).getLinha(), listaTokens.get(0).getLexema(), "{");
                    }
                    analisadorSintatico.getNaoTerminal("corpo").executar(listaTokens);
                    if (listaTokens.get(0).getLexema().equals("}")) {
                        EditorDeNaoTerminaisPraPro.removePrimeiroItemDaLista(listaTokens);
                    } else {
                        tratarErros(listaTokens, listaTokens.get(0).getLinha(), listaTokens.get(0).getLexema(), "}");
                    }
                } else {
                    tratarErros(listaTokens, listaTokens.get(0).getLinha(), listaTokens.get(0).getLexema(), "algoritimo");
                }
            }
        }));

        analisadorSintatico.addNaoTerminal(new NaoTerminal("lista_comandos", new ExecutorNaoTerminal() {
            @Override
            public void executarLeitura(ArrayList<Token> listaTokens) {
                //EditorDeNaoTerminais.removePrimeiroItemDaLista(listaTokens);
                String temp = listaTokens.get(0).getLexema();
                if (temp.equals("escreva") || temp.equals("leia") || temp.equals("enquanto")
                        || temp.equals("se") || temp.equals("para")
                        || (listaTokens.get(0).getTipoToken() == TipoToken.IDENTIFICADOR)) {
                    analisadorSintatico.getNaoTerminal("comando").executar(listaTokens);
                    analisadorSintatico.getNaoTerminal("lista_comandos").executar(listaTokens);
                }
                //Tem lambda
            }
        }));

        analisadorSintatico.addNaoTerminal(new NaoTerminal("comando", new ExecutorNaoTerminal() {
            @Override
            public void executarLeitura(ArrayList<Token> listaTokens) {
                //EditorDeNaoTerminais.removePrimeiroItemDaLista(listaTokens);
                String temp = listaTokens.get(0).getLexema();
                if (temp.equals("escreva")) {
                    analisadorSintatico.getNaoTerminal("escreva").executar(listaTokens);
                } else if (temp.equals("leia")) {
                    analisadorSintatico.getNaoTerminal("leia").executar(listaTokens);
                } else if (temp.equals("enquanto")) {
                    analisadorSintatico.getNaoTerminal("enquanto").executar(listaTokens);
                } else if (temp.equals("se")) {
                    analisadorSintatico.getNaoTerminal("se").executar(listaTokens);
                } else if (temp.equals("para")) {
                    analisadorSintatico.getNaoTerminal("para").executar(listaTokens);
                } else if (listaTokens.get(0).getTipoToken() == TipoToken.IDENTIFICADOR) {
                    analisadorSintatico.getNaoTerminal("id").executar(listaTokens);
                    analisadorSintatico.getNaoTerminal("atrib_ou_chamaFuncao").executar(listaTokens);
                } else {
                    tratarErros(listaTokens, listaTokens.get(0).getLinha(), listaTokens.get(0).getLexema(),
                            "escreva ou leia ou enquanto ou se ou para ou identificador");
                }
            }
        }));

        analisadorSintatico.addNaoTerminal(new NaoTerminal("atrib_ou_chamaFuncao", new ExecutorNaoTerminal() {
            @Override
            public void executarLeitura(ArrayList<Token> listaTokens) {
                //EditorDeNaoTerminais.removePrimeiroItemDaLista(listaTokens);
                String temp = listaTokens.get(0).getLexema();
                if (temp.equals("[") || temp.equals(".") || temp.equals("=")) {
                    analisadorSintatico.getNaoTerminal("atribuicao").executar(listaTokens);
                } else if (listaTokens.get(0).getLexema().equals("(")) {
                    analisadorSintatico.getNaoTerminal("chamada_funcao").executar(listaTokens);
                    if (listaTokens.get(0).getLexema().equals(";")) {
                        EditorDeNaoTerminaisPraPro.removePrimeiroItemDaLista(listaTokens);
                    } else {
                        tratarErros(listaTokens, listaTokens.get(0).getLinha(), listaTokens.get(0).getLexema(), ";");
                    }
                } else if (temp.equals("++") || temp.equals("--")) {
                    EditorDeNaoTerminaisPraPro.removePrimeiroItemDaLista(listaTokens);
                    if (listaTokens.get(0).getLexema().equals(";")) {
                        EditorDeNaoTerminaisPraPro.removePrimeiroItemDaLista(listaTokens);
                    } else {
                        tratarErros(listaTokens, listaTokens.get(0).getLinha(), listaTokens.get(0).getLexema(), ";");
                    }
                } else {
                    tratarErros(listaTokens, listaTokens.get(0).getLinha(), listaTokens.get(0).getLexema(),
                            "[ ou . ou = ou operador unario");
                }
            }
        }));

        analisadorSintatico.addNaoTerminal(new NaoTerminal("atribuicao", new ExecutorNaoTerminal() {
            @Override
            public void executarLeitura(ArrayList<Token> listaTokens) {
                //EditorDeNaoTerminais.removePrimeiroItemDaLista(listaTokens);
                String temp = listaTokens.get(0).getLexema();
                if (temp.equals("[") || temp.equals("=")) {
                    if (listaTokens.get(0).getLexema().equals("[")) {
                        analisadorSintatico.getNaoTerminal("acesso_matriz").executar(listaTokens);
                    }
                    if (listaTokens.get(0).getLexema().equals("=")) {
                        EditorDeNaoTerminaisPraPro.removePrimeiroItemDaLista(listaTokens);
                    } else {
                        tratarErros(listaTokens, listaTokens.get(0).getLinha(), listaTokens.get(0).getLexema(), "=");
                    }
                    //Mudado
                    if (listaTokens.get(0).getTipoToken() == TipoToken.CADEIA_CONSTANTE
                            || listaTokens.get(0).getTipoToken() == TipoToken.CARACTERE_CONSTANTE) {
                        EditorDeNaoTerminaisPraPro.removePrimeiroItemDaLista(listaTokens);

                    } else {
                        analisadorSintatico.getNaoTerminal("exp_aritmetica").executar(listaTokens);
                    }

                    if (listaTokens.get(0).getLexema().equals(";")) {
                        EditorDeNaoTerminaisPraPro.removePrimeiroItemDaLista(listaTokens);
                    } else {
                        tratarErros(listaTokens, listaTokens.get(0).getLinha(), listaTokens.get(0).getLexema(), ";");
                    }

                } else if (temp.equals(".")) {
                    analisadorSintatico.getNaoTerminal("id").executar(listaTokens);
                    if (listaTokens.get(0).getLexema().equals("=")) {
                        EditorDeNaoTerminaisPraPro.removePrimeiroItemDaLista(listaTokens);
                    } else {
                        tratarErros(listaTokens, listaTokens.get(0).getLinha(), listaTokens.get(0).getLexema(), "=");
                    }
                    analisadorSintatico.getNaoTerminal("exp_aritmetica").executar(listaTokens);
                    if (listaTokens.get(0).getLexema().equals(";")) {
                        EditorDeNaoTerminaisPraPro.removePrimeiroItemDaLista(listaTokens);
                    } else {
                        tratarErros(listaTokens, listaTokens.get(0).getLinha(), listaTokens.get(0).getLexema(), ";");
                    }
                } else {
                    tratarErros(listaTokens, listaTokens.get(0).getLinha(), listaTokens.get(0).getLexema(),
                            "[ ou . ou =");
                }
            }
        }));

        analisadorSintatico.addNaoTerminal(new NaoTerminal("escreva", new ExecutorNaoTerminal() {
            @Override
            public void executarLeitura(ArrayList<Token> listaTokens) {
                //EditorDeNaoTerminais.removePrimeiroItemDaLista(listaTokens);
                String temp = listaTokens.get(0).getLexema();
                if (temp.equals("escreva")) {
                    EditorDeNaoTerminaisPraPro.removePrimeiroItemDaLista(listaTokens);
                    if (listaTokens.get(0).getLexema().equals("(")) {
                        EditorDeNaoTerminaisPraPro.removePrimeiroItemDaLista(listaTokens);
                    }
                    analisadorSintatico.getNaoTerminal("lista_arg_escrita").executar(listaTokens);
                } else {
                    tratarErros(listaTokens, listaTokens.get(0).getLinha(), listaTokens.get(0).getLexema(),
                            "escreva");
                }
            }
        }));

        analisadorSintatico.addNaoTerminal(new NaoTerminal("lista_arg_escrita", new ExecutorNaoTerminal() {
            @Override
            public void executarLeitura(ArrayList<Token> listaTokens) {
                //EditorDeNaoTerminais.removePrimeiroItemDaLista(listaTokens);
                String temp = listaTokens.get(0).getLexema();
                if ((listaTokens.get(0).getTipoToken() == TipoToken.CARACTERE_CONSTANTE)
                        || (listaTokens.get(0).getTipoToken() == TipoToken.CADEIA_CONSTANTE)
                        || (listaTokens.get(0).getTipoToken() == TipoToken.IDENTIFICADOR)) {
                    analisadorSintatico.getNaoTerminal("arg_escrita").executar(listaTokens);
                    analisadorSintatico.getNaoTerminal("prox_arg_escrita").executar(listaTokens);
                } else {
                    tratarErros(listaTokens, listaTokens.get(0).getLinha(), listaTokens.get(0).getLexema(),
                            "caractere ou cadeia ou identificador");
                }
            }
        }));

        analisadorSintatico.addNaoTerminal(new NaoTerminal("prox_arg_escrita", new ExecutorNaoTerminal() {
            @Override
            public void executarLeitura(ArrayList<Token> listaTokens) {
                //EditorDeNaoTerminais.removePrimeiroItemDaLista(listaTokens);
                String temp = listaTokens.get(0).getLexema();
                if (temp.equals(",")) {
                    EditorDeNaoTerminaisPraPro.removePrimeiroItemDaLista(listaTokens);
                    analisadorSintatico.getNaoTerminal("arg_escrita").executar(listaTokens);
                    analisadorSintatico.getNaoTerminal("prox_arg_escrita").executar(listaTokens);
                } else if (temp.equals(")")) {
                    EditorDeNaoTerminaisPraPro.removePrimeiroItemDaLista(listaTokens);
                    if (listaTokens.get(0).getLexema().equals(";")) {
                        EditorDeNaoTerminaisPraPro.removePrimeiroItemDaLista(listaTokens);
                    } else {
                        tratarErros(listaTokens, listaTokens.get(0).getLinha(), listaTokens.get(0).getLexema(), ";");
                    }
                } else {
                    tratarErros(listaTokens, listaTokens.get(0).getLinha(), listaTokens.get(0).getLexema(),
                            ", ou )");
                }
            }
        }));

        analisadorSintatico.addNaoTerminal(new NaoTerminal("arg_escrita", new ExecutorNaoTerminal() {
            @Override
            public void executarLeitura(ArrayList<Token> listaTokens) {
                //EditorDeNaoTerminais.removePrimeiroItemDaLista(listaTokens);
                String temp = listaTokens.get(0).getLexema();
                if ((listaTokens.get(0).getTipoToken() == TipoToken.CARACTERE_CONSTANTE)
                        || (listaTokens.get(0).getTipoToken() == TipoToken.CADEIA_CONSTANTE)) {
                    EditorDeNaoTerminaisPraPro.removePrimeiroItemDaLista(listaTokens);
                    analisadorSintatico.getNaoTerminal("arg_escrita").executar(listaTokens);
                    analisadorSintatico.getNaoTerminal("prox_arg_escrita").executar(listaTokens);
                } else if (listaTokens.get(0).getTipoToken() == TipoToken.IDENTIFICADOR) {
                    analisadorSintatico.getNaoTerminal("argumento").executar(listaTokens);
                } else {
                    tratarErros(listaTokens, listaTokens.get(0).getLinha(), listaTokens.get(0).getLexema(),
                            "caractere ou cadeia ou identificador");
                }
            }
        }));

        analisadorSintatico.addNaoTerminal(new NaoTerminal("leia", new ExecutorNaoTerminal() {
            @Override
            public void executarLeitura(ArrayList<Token> listaTokens) {
                //EditorDeNaoTerminais.removePrimeiroItemDaLista(listaTokens);
                String temp = listaTokens.get(0).getLexema();
                if (temp.equals("leia")) {
                    EditorDeNaoTerminaisPraPro.removePrimeiroItemDaLista(listaTokens);
                    if (listaTokens.get(0).getLexema().equals("(")) {
                        EditorDeNaoTerminaisPraPro.removePrimeiroItemDaLista(listaTokens);
                    } else {
                        tratarErros(listaTokens, listaTokens.get(0).getLinha(), listaTokens.get(0).getLexema(), "(");
                    }
                    analisadorSintatico.getNaoTerminal("argumento").executar(listaTokens);
                    analisadorSintatico.getNaoTerminal("prox_argumento").executar(listaTokens);

//                    if (listaTokens.get(0).getLexema().equals(")")) {
//                        EditorDeNaoTerminais.removePrimeiroItemDaLista(listaTokens);
//                    } else {
//                        tratarErros(listaTokens,listaTokens.get(0).getLinha(), listaTokens.get(0).getLexema(), ")");
//                    }
                    if (listaTokens.get(0).getLexema().equals(";")) {
                        EditorDeNaoTerminaisPraPro.removePrimeiroItemDaLista(listaTokens);
                    } else {
                        tratarErros(listaTokens, listaTokens.get(0).getLinha(), listaTokens.get(0).getLexema(), ";");
                    }
                } else {
                    tratarErros(listaTokens, listaTokens.get(0).getLinha(), listaTokens.get(0).getLexema(),
                            "leia");
                }
            }
        }));

        analisadorSintatico.addNaoTerminal(new NaoTerminal("enquanto", new ExecutorNaoTerminal() {
            @Override
            public void executarLeitura(ArrayList<Token> listaTokens) {
                //EditorDeNaoTerminais.removePrimeiroItemDaLista(listaTokens);
                String temp = listaTokens.get(0).getLexema();
                if (temp.equals("enquanto")) {
                    EditorDeNaoTerminaisPraPro.removePrimeiroItemDaLista(listaTokens);
                    if (listaTokens.get(0).getLexema().equals("(")) {
                        EditorDeNaoTerminaisPraPro.removePrimeiroItemDaLista(listaTokens);
                    } else {
                        tratarErros(listaTokens, listaTokens.get(0).getLinha(), listaTokens.get(0).getLexema(), "(");
                    }
                    analisadorSintatico.getNaoTerminal("exp_logica").executar(listaTokens);
                    if (listaTokens.get(0).getLexema().equals(")")) {
                        EditorDeNaoTerminaisPraPro.removePrimeiroItemDaLista(listaTokens);
                    } else {
                        tratarErros(listaTokens, listaTokens.get(0).getLinha(), listaTokens.get(0).getLexema(), ")");
                    }
                    if (listaTokens.get(0).getLexema().equals("{")) {
                        EditorDeNaoTerminaisPraPro.removePrimeiroItemDaLista(listaTokens);
                    } else {
                        tratarErros(listaTokens, listaTokens.get(0).getLinha(), listaTokens.get(0).getLexema(), "{");
                    }
                    analisadorSintatico.getNaoTerminal("lista_comandos").executar(listaTokens);
                    if (listaTokens.get(0).getLexema().equals("}")) {
                        EditorDeNaoTerminaisPraPro.removePrimeiroItemDaLista(listaTokens);
                    } else {
                        tratarErros(listaTokens, listaTokens.get(0).getLinha(), listaTokens.get(0).getLexema(), "}");
                    }
                    if (listaTokens.get(0).getLexema().equals(";")) {
                        EditorDeNaoTerminaisPraPro.removePrimeiroItemDaLista(listaTokens);
                    } else {
                        tratarErros(listaTokens, listaTokens.get(0).getLinha(), listaTokens.get(0).getLexema(), ";");
                    }
                } else {
                    tratarErros(listaTokens, listaTokens.get(0).getLinha(), listaTokens.get(0).getLexema(),
                            "enquanto");
                }
            }
        }));

        analisadorSintatico.addNaoTerminal(new NaoTerminal("se", new ExecutorNaoTerminal() {
            @Override
            public void executarLeitura(ArrayList<Token> listaTokens) {
                //EditorDeNaoTerminais.removePrimeiroItemDaLista(listaTokens);
                String temp = listaTokens.get(0).getLexema();
                if (temp.equals("se")) {
                    EditorDeNaoTerminaisPraPro.removePrimeiroItemDaLista(listaTokens);
                    if (listaTokens.get(0).getLexema().equals("(")) {
                        EditorDeNaoTerminaisPraPro.removePrimeiroItemDaLista(listaTokens);
                    } else {
                        tratarErros(listaTokens, listaTokens.get(0).getLinha(), listaTokens.get(0).getLexema(), "(");
                    }
                    analisadorSintatico.getNaoTerminal("exp_logica").executar(listaTokens);
                    if (listaTokens.get(0).getLexema().equals(")")) {
                        EditorDeNaoTerminaisPraPro.removePrimeiroItemDaLista(listaTokens);
                    } else {
                        tratarErros(listaTokens, listaTokens.get(0).getLinha(), listaTokens.get(0).getLexema(), ")");
                    }
                    if (listaTokens.get(0).getLexema().equals("{")) {
                        EditorDeNaoTerminaisPraPro.removePrimeiroItemDaLista(listaTokens);
                    } else {
                        tratarErros(listaTokens, listaTokens.get(0).getLinha(), listaTokens.get(0).getLexema(), "}");
                    }
                    analisadorSintatico.getNaoTerminal("lista_comandos").executar(listaTokens);
                    if (listaTokens.get(0).getLexema().equals("}")) {
                        EditorDeNaoTerminaisPraPro.removePrimeiroItemDaLista(listaTokens);
                    } else {
                        tratarErros(listaTokens, listaTokens.get(0).getLinha(), listaTokens.get(0).getLexema(), "}");
                    }
                    analisadorSintatico.getNaoTerminal("compl_se").executar(listaTokens);
                } else {
                    tratarErros(listaTokens, listaTokens.get(0).getLinha(), listaTokens.get(0).getLexema(),
                            "se");
                }
            }
        }));

        analisadorSintatico.addNaoTerminal(new NaoTerminal("compl_se", new ExecutorNaoTerminal() {
            @Override
            public void executarLeitura(ArrayList<Token> listaTokens) {
                //EditorDeNaoTerminais.removePrimeiroItemDaLista(listaTokens);
                String temp = listaTokens.get(0).getLexema();
                if (temp.equals("senao")) {
                    analisadorSintatico.getNaoTerminal("senao").executar(listaTokens);
                } else if (temp.equals(";")) {
                    EditorDeNaoTerminaisPraPro.removePrimeiroItemDaLista(listaTokens);
                } else {
                    tratarErros(listaTokens, listaTokens.get(0).getLinha(), listaTokens.get(0).getLexema(),
                            "senao ou ;");
                }
            }
        }));

        analisadorSintatico.addNaoTerminal(new NaoTerminal("senao", new ExecutorNaoTerminal() {
            @Override
            public void executarLeitura(ArrayList<Token> listaTokens) {
                //EditorDeNaoTerminais.removePrimeiroItemDaLista(listaTokens);
                String temp = listaTokens.get(0).getLexema();
                if (temp.equals("senao")) {
                    EditorDeNaoTerminaisPraPro.removePrimeiroItemDaLista(listaTokens);
                    if (listaTokens.get(0).getLexema().equals("{")) {
                        EditorDeNaoTerminaisPraPro.removePrimeiroItemDaLista(listaTokens);
                    } else {
                        tratarErros(listaTokens, listaTokens.get(0).getLinha(), listaTokens.get(0).getLexema(), "{");
                    }
                    analisadorSintatico.getNaoTerminal("lista_comandos").executar(listaTokens);
                    if (listaTokens.get(0).getLexema().equals("}")) {
                        EditorDeNaoTerminaisPraPro.removePrimeiroItemDaLista(listaTokens);
                    } else {
                        tratarErros(listaTokens, listaTokens.get(0).getLinha(), listaTokens.get(0).getLexema(), "}");
                    }
                    if (listaTokens.get(0).getLexema().equals(";")) {
                        EditorDeNaoTerminaisPraPro.removePrimeiroItemDaLista(listaTokens);
                    } else {
                        tratarErros(listaTokens, listaTokens.get(0).getLinha(), listaTokens.get(0).getLexema(), ";");
                    }
                } else {
                    tratarErros(listaTokens, listaTokens.get(0).getLinha(), listaTokens.get(0).getLexema(),
                            "senao");
                }
            }
        }));

        analisadorSintatico.addNaoTerminal(new NaoTerminal("para", new ExecutorNaoTerminal() {
            @Override
            public void executarLeitura(ArrayList<Token> listaTokens) {
                //EditorDeNaoTerminais.removePrimeiroItemDaLista(listaTokens);
                String temp = listaTokens.get(0).getLexema();
                if (temp.equals("para")) {
                    EditorDeNaoTerminaisPraPro.removePrimeiroItemDaLista(listaTokens);
                    if (listaTokens.get(0).getLexema().equals("(")) {
                        EditorDeNaoTerminaisPraPro.removePrimeiroItemDaLista(listaTokens);
                    } else {
                        tratarErros(listaTokens, listaTokens.get(0).getLinha(), listaTokens.get(0).getLexema(), "(");
                    }
                    analisadorSintatico.getNaoTerminal("exp_atribuicao").executar(listaTokens);
                    if (listaTokens.get(0).getLexema().equals(";")) {
                        EditorDeNaoTerminaisPraPro.removePrimeiroItemDaLista(listaTokens);
                    } else {
                        tratarErros(listaTokens, listaTokens.get(0).getLinha(), listaTokens.get(0).getLexema(), ";");
                    }
                    analisadorSintatico.getNaoTerminal("exp_logica").executar(listaTokens);
                    if (listaTokens.get(0).getLexema().equals(";")) {
                        EditorDeNaoTerminaisPraPro.removePrimeiroItemDaLista(listaTokens);
                    } else {
                        tratarErros(listaTokens, listaTokens.get(0).getLinha(), listaTokens.get(0).getLexema(), ";");
                    }
                    analisadorSintatico.getNaoTerminal("exp_atribuicao").executar(listaTokens);
                    if (listaTokens.get(0).getLexema().equals(")")) {
                        EditorDeNaoTerminaisPraPro.removePrimeiroItemDaLista(listaTokens);
                    } else {
                        tratarErros(listaTokens, listaTokens.get(0).getLinha(), listaTokens.get(0).getLexema(), ")");
                    }
                    if (listaTokens.get(0).getLexema().equals("{")) {
                        EditorDeNaoTerminaisPraPro.removePrimeiroItemDaLista(listaTokens);
                    } else {
                        tratarErros(listaTokens, listaTokens.get(0).getLinha(), listaTokens.get(0).getLexema(), "{");
                    }
                    analisadorSintatico.getNaoTerminal("lista_comandos").executar(listaTokens);
                    if (listaTokens.get(0).getLexema().equals("}")) {
                        EditorDeNaoTerminaisPraPro.removePrimeiroItemDaLista(listaTokens);
                    } else {
                        tratarErros(listaTokens, listaTokens.get(0).getLinha(), listaTokens.get(0).getLexema(), "}");
                    }
                    if (listaTokens.get(0).getLexema().equals(";")) {
                        EditorDeNaoTerminaisPraPro.removePrimeiroItemDaLista(listaTokens);
                    } else {
                        tratarErros(listaTokens, listaTokens.get(0).getLinha(), listaTokens.get(0).getLexema(), ";");
                    }
                } else {
                    tratarErros(listaTokens, listaTokens.get(0).getLinha(), listaTokens.get(0).getLexema(),
                            "para");
                }
            }
        }));

        analisadorSintatico.addNaoTerminal(new NaoTerminal("exp_logica", new ExecutorNaoTerminal() {
            @Override
            public void executarLeitura(ArrayList<Token> listaTokens) {
                //EditorDeNaoTerminais.removePrimeiroItemDaLista(listaTokens);
                String temp = listaTokens.get(0).getLexema();
                if (temp.equals("(")
                        || (listaTokens.get(0).getTipoToken() == TipoToken.NUMERO_INTEIRO)
                        || (listaTokens.get(0).getTipoToken() == TipoToken.NUMERO_REAL)
                        || (listaTokens.get(0).getTipoToken() == TipoToken.IDENTIFICADOR)) {
                    analisadorSintatico.getNaoTerminal("termo_logico").executar(listaTokens);
                    analisadorSintatico.getNaoTerminal("exp_logica_ou").executar(listaTokens);
                }
//                else {
//                    tratarErros(listaTokens,listaTokens.get(0).getLinha(), listaTokens.get(0).getLexema(),
//                            "inteiro ou real ou identificador ou (");
//                }
            }
        }));

        analisadorSintatico.addNaoTerminal(new NaoTerminal("termo_logico", new ExecutorNaoTerminal() {
            @Override
            public void executarLeitura(ArrayList<Token> listaTokens) {
                //EditorDeNaoTerminais.removePrimeiroItemDaLista(listaTokens);
                String temp = listaTokens.get(0).getLexema();
                if (temp.equals("(")
                        || (listaTokens.get(0).getTipoToken() == TipoToken.NUMERO_INTEIRO)
                        || (listaTokens.get(0).getTipoToken() == TipoToken.NUMERO_REAL)
                        || (listaTokens.get(0).getTipoToken() == TipoToken.IDENTIFICADOR)) {
                    analisadorSintatico.getNaoTerminal("exp_relacional").executar(listaTokens);
                    analisadorSintatico.getNaoTerminal("exp_logica_e").executar(listaTokens);
                } else {
                    tratarErros(listaTokens, listaTokens.get(0).getLinha(), listaTokens.get(0).getLexema(),
                            "inteiro ou real ou identificador ou (");
                }
            }
        }));

        analisadorSintatico.addNaoTerminal(new NaoTerminal("exp_logica_ou", new ExecutorNaoTerminal() {
            @Override
            public void executarLeitura(ArrayList<Token> listaTokens) {
                //EditorDeNaoTerminais.removePrimeiroItemDaLista(listaTokens);
                String temp = listaTokens.get(0).getLexema();
                if (temp.equals("||")) {
                    EditorDeNaoTerminaisPraPro.removePrimeiroItemDaLista(listaTokens);
                    analisadorSintatico.getNaoTerminal("termo_logico").executar(listaTokens);
                    analisadorSintatico.getNaoTerminal("exp_logica_ou").executar(listaTokens);
                }
                //tem lambda
            }
        }));

        analisadorSintatico.addNaoTerminal(new NaoTerminal("exp_logica_e", new ExecutorNaoTerminal() {
            @Override
            public void executarLeitura(ArrayList<Token> listaTokens) {
                //EditorDeNaoTerminais.removePrimeiroItemDaLista(listaTokens);
                String temp = listaTokens.get(0).getLexema();
                if (temp.equals("&&")) {
                    EditorDeNaoTerminaisPraPro.removePrimeiroItemDaLista(listaTokens);
                    analisadorSintatico.getNaoTerminal("exp_relacional").executar(listaTokens);
                    analisadorSintatico.getNaoTerminal("exp_logica_e").executar(listaTokens);
                }
                //tem lambda
            }
        }));

        analisadorSintatico.addNaoTerminal(new NaoTerminal("exp_relacional", new ExecutorNaoTerminal() {
            @Override
            public void executarLeitura(ArrayList<Token> listaTokens) {
                //EditorDeNaoTerminais.removePrimeiroItemDaLista(listaTokens);
                String temp = listaTokens.get(0).getLexema();
                if (temp.equals("(")
                        || (listaTokens.get(0).getTipoToken() == TipoToken.NUMERO_INTEIRO)
                        || (listaTokens.get(0).getTipoToken() == TipoToken.NUMERO_REAL)
                        || (listaTokens.get(0).getTipoToken() == TipoToken.IDENTIFICADOR)) {
                    analisadorSintatico.getNaoTerminal("exp_aritmetica").executar(listaTokens);
                    analisadorSintatico.getNaoTerminal("compl_exp_rel").executar(listaTokens);
                } else {
                    tratarErros(listaTokens, listaTokens.get(0).getLinha(), listaTokens.get(0).getLexema(),
                            "inteiro ou real ou identificador ou (");
                }
            }
        }));

        analisadorSintatico.addNaoTerminal(new NaoTerminal("compl_exp_rel", new ExecutorNaoTerminal() {
            @Override
            public void executarLeitura(ArrayList<Token> listaTokens) {
                //EditorDeNaoTerminais.removePrimeiroItemDaLista(listaTokens);
                String temp = listaTokens.get(0).getLexema();
                if (temp.equals("==")) {
                    EditorDeNaoTerminaisPraPro.removePrimeiroItemDaLista(listaTokens);
                    analisadorSintatico.getNaoTerminal("termo_igualdade").executar(listaTokens);
                } else if (listaTokens.get(0).getTipoToken() == TipoToken.OPERADOR_RELACIONAL) {
                    EditorDeNaoTerminaisPraPro.removePrimeiroItemDaLista(listaTokens);
                    analisadorSintatico.getNaoTerminal("exp_aritmetica").executar(listaTokens);
                } else {
                    tratarErros(listaTokens, listaTokens.get(0).getLinha(), listaTokens.get(0).getLexema(),
                            "operador relacional ou ==");
                }
            }
        }));

        analisadorSintatico.addNaoTerminal(new NaoTerminal("termo_igualdade", new ExecutorNaoTerminal() {
            @Override
            public void executarLeitura(ArrayList<Token> listaTokens) {
                //EditorDeNaoTerminais.removePrimeiroItemDaLista(listaTokens);
                String temp = listaTokens.get(0).getLexema();
                if (temp.equals("(")
                        || (listaTokens.get(0).getTipoToken() == TipoToken.NUMERO_INTEIRO)
                        || (listaTokens.get(0).getTipoToken() == TipoToken.NUMERO_REAL)
                        || (listaTokens.get(0).getTipoToken() == TipoToken.IDENTIFICADOR)) {
                    analisadorSintatico.getNaoTerminal("exp_aritmetica").executar(listaTokens);
                } else if (temp.equals("verdadeiro") || temp.equals("falso")) {
                    EditorDeNaoTerminaisPraPro.removePrimeiroItemDaLista(listaTokens);
                } else {
                    tratarErros(listaTokens, listaTokens.get(0).getLinha(), listaTokens.get(0).getLexema(),
                            "inteiro ou real ou identificador ou verdadeiro ou falso ou (");
                }
            }
        }));

        analisadorSintatico.addNaoTerminal(new NaoTerminal("exp_aritmetica", new ExecutorNaoTerminal() {
            @Override
            public void executarLeitura(ArrayList<Token> listaTokens) {
                //EditorDeNaoTerminais.removePrimeiroItemDaLista(listaTokens);
                String temp = listaTokens.get(0).getLexema();
                if (temp.equals("(")
                        || (listaTokens.get(0).getTipoToken() == TipoToken.NUMERO_INTEIRO)
                        || (listaTokens.get(0).getTipoToken() == TipoToken.NUMERO_REAL)
                        || (listaTokens.get(0).getTipoToken() == TipoToken.IDENTIFICADOR)) {
                    analisadorSintatico.getNaoTerminal("termo_aritmetico").executar(listaTokens);
                    analisadorSintatico.getNaoTerminal("compl_exp_arit").executar(listaTokens);
                } else {
                    tratarErros(listaTokens, listaTokens.get(0).getLinha(), listaTokens.get(0).getLexema(),
                            "inteiro ou real ou identificador ou (");
                }
            }
        }));

        analisadorSintatico.addNaoTerminal(new NaoTerminal("termo_aritmetico", new ExecutorNaoTerminal() {
            @Override
            public void executarLeitura(ArrayList<Token> listaTokens) {
                //EditorDeNaoTerminais.removePrimeiroItemDaLista(listaTokens);
                String temp = listaTokens.get(0).getLexema();
                if (temp.equals("(")
                        || (listaTokens.get(0).getTipoToken() == TipoToken.NUMERO_INTEIRO)
                        || (listaTokens.get(0).getTipoToken() == TipoToken.NUMERO_REAL)
                        || (listaTokens.get(0).getTipoToken() == TipoToken.IDENTIFICADOR)) {
                    analisadorSintatico.getNaoTerminal("operando").executar(listaTokens);
                    analisadorSintatico.getNaoTerminal("compl_termo_aritmetico").executar(listaTokens);
                } else {
                    tratarErros(listaTokens, listaTokens.get(0).getLinha(), listaTokens.get(0).getLexema(),
                            "inteiro ou real ou identificador ou (");
                }
            }
        }));

        analisadorSintatico.addNaoTerminal(new NaoTerminal("compl_termo_aritmetico", new ExecutorNaoTerminal() {
            @Override
            public void executarLeitura(ArrayList<Token> listaTokens) {
                //EditorDeNaoTerminais.removePrimeiroItemDaLista(listaTokens);
                String temp = listaTokens.get(0).getLexema();
                if (temp.equals("*") || temp.equals("/")) {
                    analisadorSintatico.getNaoTerminal("op_arit_primario").executar(listaTokens);
                    analisadorSintatico.getNaoTerminal("termo_aritmetico").executar(listaTokens);
                }
                //Tem lambda
            }
        }));

        analisadorSintatico.addNaoTerminal(new NaoTerminal("compl_exp_arit", new ExecutorNaoTerminal() {
            @Override
            public void executarLeitura(ArrayList<Token> listaTokens) {
                //EditorDeNaoTerminais.removePrimeiroItemDaLista(listaTokens);
                String temp = listaTokens.get(0).getLexema();
                if (temp.equals("+") || temp.equals("-")) {
                    analisadorSintatico.getNaoTerminal("op_arit_secundario").executar(listaTokens);
                    analisadorSintatico.getNaoTerminal("termo_aritmetico").executar(listaTokens);
                    analisadorSintatico.getNaoTerminal("compl_exp_arit").executar(listaTokens);
                }
                //Tem lambda
            }
        }));

        analisadorSintatico.addNaoTerminal(new NaoTerminal("op_arit_primario", new ExecutorNaoTerminal() {
            @Override
            public void executarLeitura(ArrayList<Token> listaTokens) {
                //EditorDeNaoTerminais.removePrimeiroItemDaLista(listaTokens);
                String temp = listaTokens.get(0).getLexema();
                if (temp.equals("*") || temp.equals("/")) {
                    EditorDeNaoTerminaisPraPro.removePrimeiroItemDaLista(listaTokens);
                } else {
                    tratarErros(listaTokens, listaTokens.get(0).getLinha(), listaTokens.get(0).getLexema(),
                            "* ou /");
                }
            }
        }));

        analisadorSintatico.addNaoTerminal(new NaoTerminal("op_arit_secundario", new ExecutorNaoTerminal() {
            @Override
            public void executarLeitura(ArrayList<Token> listaTokens) {
                //EditorDeNaoTerminais.removePrimeiroItemDaLista(listaTokens);
                String temp = listaTokens.get(0).getLexema();
                if (temp.equals("+") || temp.equals("-")) {
                    EditorDeNaoTerminaisPraPro.removePrimeiroItemDaLista(listaTokens);
                } else {
                    tratarErros(listaTokens, listaTokens.get(0).getLinha(), listaTokens.get(0).getLexema(),
                            "+ ou -");
                }
            }
        }));

        analisadorSintatico.addNaoTerminal(new NaoTerminal("operando", new ExecutorNaoTerminal() {
            @Override
            public void executarLeitura(ArrayList<Token> listaTokens) {
                //EditorDeNaoTerminais.removePrimeiroItemDaLista(listaTokens);
                String temp = listaTokens.get(0).getLexema();
                if ((listaTokens.get(0).getTipoToken() == TipoToken.NUMERO_INTEIRO)
                        || (listaTokens.get(0).getTipoToken() == TipoToken.NUMERO_REAL)
                        || (listaTokens.get(0).getTipoToken() == TipoToken.IDENTIFICADOR)) {
                    analisadorSintatico.getNaoTerminal("tipos_operando").executar(listaTokens);
                } else if (temp.equals("(")) {
                    EditorDeNaoTerminaisPraPro.removePrimeiroItemDaLista(listaTokens);
                    analisadorSintatico.getNaoTerminal("exp_logica").executar(listaTokens);
                    if (listaTokens.get(0).getLexema().equals(")")) {
                        EditorDeNaoTerminaisPraPro.removePrimeiroItemDaLista(listaTokens);
                        analisadorSintatico.getNaoTerminal("exp_logica").executar(listaTokens);
                    } else {
                        tratarErros(listaTokens, listaTokens.get(0).getLinha(), listaTokens.get(0).getLexema(),
                                ")");
                    }
                } else {
                    tratarErros(listaTokens, listaTokens.get(0).getLinha(), listaTokens.get(0).getLexema(),
                            "inteiro ou real ou identificador ou (");
                }
            }
        }));

        analisadorSintatico.addNaoTerminal(new NaoTerminal("tipos_operando", new ExecutorNaoTerminal() {
            @Override
            public void executarLeitura(ArrayList<Token> listaTokens) {
                //EditorDeNaoTerminais.removePrimeiroItemDaLista(listaTokens);
                String temp = listaTokens.get(0).getLexema();
                if (listaTokens.get(0).getTipoToken() == TipoToken.IDENTIFICADOR) {
                    analisadorSintatico.getNaoTerminal("acesso_var").executar(listaTokens);
                } else if ((listaTokens.get(0).getTipoToken() == TipoToken.NUMERO_INTEIRO)
                        || (listaTokens.get(0).getTipoToken() == TipoToken.NUMERO_REAL)) {
                    EditorDeNaoTerminaisPraPro.removePrimeiroItemDaLista(listaTokens);
                } else {
                    tratarErros(listaTokens, listaTokens.get(0).getLinha(), listaTokens.get(0).getLexema(),
                            "inteiro ou real ou identificador");
                }
            }
        }));

        analisadorSintatico.addNaoTerminal(new NaoTerminal("acesso_var", new ExecutorNaoTerminal() {
            @Override
            public void executarLeitura(ArrayList<Token> listaTokens) {
                //EditorDeNaoTerminais.removePrimeiroItemDaLista(listaTokens);
                String temp = listaTokens.get(0).getLexema();
                if (listaTokens.get(0).getTipoToken() == TipoToken.IDENTIFICADOR) {
                    analisadorSintatico.getNaoTerminal("id").executar(listaTokens);
                    analisadorSintatico.getNaoTerminal("compl_tipo_parametro").executar(listaTokens);
                } else {
                    tratarErros(listaTokens, listaTokens.get(0).getLinha(), listaTokens.get(0).getLexema(),
                            "identificador");
                }
            }
        }));

        analisadorSintatico.addNaoTerminal(new NaoTerminal("exp_atribuicao", new ExecutorNaoTerminal() {
            @Override
            public void executarLeitura(ArrayList<Token> listaTokens) {
                //EditorDeNaoTerminais.removePrimeiroItemDaLista(listaTokens);
                String temp = listaTokens.get(0).getLexema();
                if (listaTokens.get(0).getTipoToken() == TipoToken.IDENTIFICADOR) {
                    analisadorSintatico.getNaoTerminal("acesso_var").executar(listaTokens);
                    analisadorSintatico.getNaoTerminal("compl_atribuicao").executar(listaTokens);
                } else {
                    tratarErros(listaTokens, listaTokens.get(0).getLinha(), listaTokens.get(0).getLexema(),
                            "identificador");
                }
            }
        }));

        analisadorSintatico.addNaoTerminal(new NaoTerminal("compl_atribuicao", new ExecutorNaoTerminal() {
            @Override
            public void executarLeitura(ArrayList<Token> listaTokens) {
                //EditorDeNaoTerminais.removePrimeiroItemDaLista(listaTokens);
                String temp = listaTokens.get(0).getLexema();
                if (temp.equals("=")) {
                    EditorDeNaoTerminaisPraPro.removePrimeiroItemDaLista(listaTokens);
                    analisadorSintatico.getNaoTerminal("exp_aritmetica").executar(listaTokens);
                } else if (listaTokens.get(0).getTipoToken() == TipoToken.OPERADOR_ARITMETICO_UNARIO) {
                    EditorDeNaoTerminaisPraPro.removePrimeiroItemDaLista(listaTokens);
                } else {
                    tratarErros(listaTokens, listaTokens.get(0).getLinha(), listaTokens.get(0).getLexema(),
                            "= ou operador unário");
                }
            }
        }));

        return listaErros;

    }

    public static void removePrimeiroItemDaLista(ArrayList<Token> listaTokens) {
        listaTokens.remove(0);
        System.out.println("" + listaTokens);
    }

    public static void reportarErroSintatico() {

    }

}
