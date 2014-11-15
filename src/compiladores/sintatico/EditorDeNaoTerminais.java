package compiladores.sintatico;

import compiladores.Token;
import compiladores.enums.TipoToken;
import java.util.ArrayList;

/**
 *
 * @author Siloé
 */
public class EditorDeNaoTerminais {

    private static ArrayList<ErroSintatico> listaErros;

    public static void tratarErros(int linha, String encontrado, String esperado) {
        ErroSintatico erro = new ErroSintatico(linha, encontrado, esperado);
        listaErros.add(erro);
    }

    public static ArrayList<ErroSintatico> setNaoTerminais(final AnalisadorSintatico analisadorSintatico) {
        listaErros = new ArrayList<>();

        analisadorSintatico.addNaoTerminal(new NaoTerminal("id", new ExecutorNaoTerminal() {
            @Override
            public void executarLeitura(ArrayList<Token> listaTokens) {
                //EditorDeNaoTerminais.removePrimeiroItemDaLista(listaTokens);
                if (listaTokens.get(0).getTipoToken() == TipoToken.IDENTIFICADOR) {
                    EditorDeNaoTerminais.removePrimeiroItemDaLista(listaTokens);
                } else {
                    tratarErros(listaTokens.get(0).getLinha(), listaTokens.get(0).getLexema(), "identificador");
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
                    tratarErros(listaTokens.get(0).getLinha(), listaTokens.get(0).getLexema(), "registrou ou constantes");
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
                    analisadorSintatico.getNaoTerminal("lista_funções").executar(listaTokens);
                } else {
                    tratarErros(listaTokens.get(0).getLinha(), listaTokens.get(0).getLexema(), "constantes");
                }
            }
        }));

        analisadorSintatico.addNaoTerminal(new NaoTerminal("registro", new ExecutorNaoTerminal() {
            @Override
            public void executarLeitura(ArrayList<Token> listaTokens) {
                //EditorDeNaoTerminais.removePrimeiroItemDaLista(listaTokens);
                if (listaTokens.get(0).getLexema().equals("registro")) {
                    EditorDeNaoTerminais.removePrimeiroItemDaLista(listaTokens);
                    analisadorSintatico.getNaoTerminal("id").executar(listaTokens);
                    if (listaTokens.get(0).getLexema().equals("{")) {
                        EditorDeNaoTerminais.removePrimeiroItemDaLista(listaTokens);
                        analisadorSintatico.getNaoTerminal("lista_campos").executar(listaTokens);
                    } else {
                        tratarErros(listaTokens.get(0).getLinha(), listaTokens.get(0).getLexema(), "}");
                    }
                } else {
                    tratarErros(listaTokens.get(0).getLinha(), listaTokens.get(0).getLexema(), "registrou");
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
                    EditorDeNaoTerminais.removePrimeiroItemDaLista(listaTokens);
                } else {
                    tratarErros(listaTokens.get(0).getLinha(), listaTokens.get(0).getLexema(),
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
                        EditorDeNaoTerminais.removePrimeiroItemDaLista(listaTokens);
                    } else {
                        tratarErros(listaTokens.get(0).getLinha(), listaTokens.get(0).getLexema(), ";");
                    }
                } else {
                    tratarErros(listaTokens.get(0).getLinha(), listaTokens.get(0).getLexema(),
                            "inteiro ou real ou booleano ou char ou cadeia");
                }
            }
        }));

        analisadorSintatico.addNaoTerminal(new NaoTerminal("bloco_constantes", new ExecutorNaoTerminal() {
            @Override
            public void executarLeitura(ArrayList<Token> listaTokens) {
                //EditorDeNaoTerminais.removePrimeiroItemDaLista(listaTokens);                
                if (listaTokens.get(0).getLexema().equals("constantes")) {
                    EditorDeNaoTerminais.removePrimeiroItemDaLista(listaTokens);
                    if (listaTokens.get(0).getLexema().equals("{")) {
                        EditorDeNaoTerminais.removePrimeiroItemDaLista(listaTokens);
                        analisadorSintatico.getNaoTerminal("lista_constantes").executar(listaTokens);
                    } else {
                        tratarErros(listaTokens.get(0).getLinha(), listaTokens.get(0).getLexema(), "{");
                    }
                } else {
                    tratarErros(listaTokens.get(0).getLinha(), listaTokens.get(0).getLexema(), "constantes");
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
                    EditorDeNaoTerminais.removePrimeiroItemDaLista(listaTokens);
                } else {
                    tratarErros(listaTokens.get(0).getLinha(), listaTokens.get(0).getLexema(),
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
                        EditorDeNaoTerminais.removePrimeiroItemDaLista(listaTokens);
                    } else {
                        tratarErros(listaTokens.get(0).getLinha(), listaTokens.get(0).getLexema(), "=");
                    }
                    analisadorSintatico.getNaoTerminal("valor").executar(listaTokens);
                    if (listaTokens.get(0).getLexema().equals(";")) {
                        EditorDeNaoTerminais.removePrimeiroItemDaLista(listaTokens);
                    } else {
                        tratarErros(listaTokens.get(0).getLinha(), listaTokens.get(0).getLexema(), ";");
                    }
                } else {
                    tratarErros(listaTokens.get(0).getLinha(), listaTokens.get(0).getLexema(),
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
                    EditorDeNaoTerminais.removePrimeiroItemDaLista(listaTokens);
                } else {
                    tratarErros(listaTokens.get(0).getLinha(), listaTokens.get(0).getLexema(),
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
                    EditorDeNaoTerminais.removePrimeiroItemDaLista(listaTokens);
                } else {
                    tratarErros(listaTokens.get(0).getLinha(), listaTokens.get(0).getLexema(),
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
                    EditorDeNaoTerminais.removePrimeiroItemDaLista(listaTokens);
                } else {
                    tratarErros(listaTokens.get(0).getLinha(), listaTokens.get(0).getLexema(),
                            "inteiro ou real");
                }
            }
        }));

        analisadorSintatico.addNaoTerminal(new NaoTerminal("bloco_variaveis", new ExecutorNaoTerminal() {
            @Override
            public void executarLeitura(ArrayList<Token> listaTokens) {
                //EditorDeNaoTerminais.removePrimeiroItemDaLista(listaTokens);                
                if (listaTokens.get(0).getLexema().equals("variavel")) {
                    EditorDeNaoTerminais.removePrimeiroItemDaLista(listaTokens);
                    if (listaTokens.get(0).getLexema().equals("{")) {
                        EditorDeNaoTerminais.removePrimeiroItemDaLista(listaTokens);
                        analisadorSintatico.getNaoTerminal("lista_variaveis").executar(listaTokens);
                    } else {
                        tratarErros(listaTokens.get(0).getLinha(), listaTokens.get(0).getLexema(), "{");
                    }
                } else {
                    tratarErros(listaTokens.get(0).getLinha(), listaTokens.get(0).getLexema(), "variaveis");
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
                    analisadorSintatico.getNaoTerminal("variavel").executar(listaTokens);
                    analisadorSintatico.getNaoTerminal("lista_variaveis").executar(listaTokens);
                } else if (listaTokens.get(0).getLexema().equals("}")) {
                    EditorDeNaoTerminais.removePrimeiroItemDaLista(listaTokens);
                } else {
                    tratarErros(listaTokens.get(0).getLinha(), listaTokens.get(0).getLexema(),
                            "identificador ou inteiro ou real ou booleano ou char ou cadeia ou }");
                }
            }
        }));

        analisadorSintatico.addNaoTerminal(new NaoTerminal("variavel", new ExecutorNaoTerminal() {
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
                        EditorDeNaoTerminais.removePrimeiroItemDaLista(listaTokens);
                    } else {
                        tratarErros(listaTokens.get(0).getLinha(), listaTokens.get(0).getLexema(), ";");
                    }
                } else {
                    tratarErros(listaTokens.get(0).getLinha(), listaTokens.get(0).getLexema(),
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
                    EditorDeNaoTerminais.removePrimeiroItemDaLista(listaTokens);
                    analisadorSintatico.getNaoTerminal("valor").executar(listaTokens);
                    if (listaTokens.get(0).getLexema().equals(";")) {
                        EditorDeNaoTerminais.removePrimeiroItemDaLista(listaTokens);
                    } else {
                        tratarErros(listaTokens.get(0).getLinha(), listaTokens.get(0).getLexema(), ";");
                    }
                } else if (listaTokens.get(0).getLexema().equals("[") || listaTokens.get(0).getLexema().equals(";")) {
                    analisadorSintatico.getNaoTerminal("definicao_matriz").executar(listaTokens);
                } else {
                    tratarErros(listaTokens.get(0).getLinha(), listaTokens.get(0).getLexema(), "= ou [ ou ;");
                }
            }
        }));

        analisadorSintatico.addNaoTerminal(new NaoTerminal("definição_matriz", new ExecutorNaoTerminal() {
            @Override
            public void executarLeitura(ArrayList<Token> listaTokens) {
                //EditorDeNaoTerminais.removePrimeiroItemDaLista(listaTokens);                
                String temp = listaTokens.get(0).getLexema();
                if (temp.equals("[")) {
                    EditorDeNaoTerminais.removePrimeiroItemDaLista(listaTokens);
                    if (listaTokens.get(0).getTipoToken() == TipoToken.NUMERO_INTEIRO) {
                        EditorDeNaoTerminais.removePrimeiroItemDaLista(listaTokens);
                    } else {
                        tratarErros(listaTokens.get(0).getLinha(), listaTokens.get(0).getLexema(), "inteiro");
                    }
                    if (listaTokens.get(0).getLexema().equals("]")) {
                        EditorDeNaoTerminais.removePrimeiroItemDaLista(listaTokens);
                    } else {
                        tratarErros(listaTokens.get(0).getLinha(), listaTokens.get(0).getLexema(), "]");
                    }
                } else if (listaTokens.get(0).getLexema().equals(";")) {
                    EditorDeNaoTerminais.removePrimeiroItemDaLista(listaTokens);
                } else {
                    tratarErros(listaTokens.get(0).getLinha(), listaTokens.get(0).getLexema(), "[ ou ;");
                }
            }
        }));

        analisadorSintatico.addNaoTerminal(new NaoTerminal("chamada_funcao", new ExecutorNaoTerminal() {
            @Override
            public void executarLeitura(ArrayList<Token> listaTokens) {
                //EditorDeNaoTerminais.removePrimeiroItemDaLista(listaTokens);                
                String temp = listaTokens.get(0).getLexema();
                if (temp.equals("(")) {
                    EditorDeNaoTerminais.removePrimeiroItemDaLista(listaTokens);
                    analisadorSintatico.getNaoTerminal("lista_argumentos").executar(listaTokens);
                } else {
                    tratarErros(listaTokens.get(0).getLinha(), listaTokens.get(0).getLexema(), "(");
                }
            }
        }));

        analisadorSintatico.addNaoTerminal(new NaoTerminal("chamada_funcao", new ExecutorNaoTerminal() {
            @Override
            public void executarLeitura(ArrayList<Token> listaTokens) {
                //EditorDeNaoTerminais.removePrimeiroItemDaLista(listaTokens);                
                String temp = listaTokens.get(0).getLexema();
                if (listaTokens.get(0).getLexema().equals("(")) {
                    EditorDeNaoTerminais.removePrimeiroItemDaLista(listaTokens);
                    analisadorSintatico.getNaoTerminal("lista_argumentos").executar(listaTokens);
                } else {
                    tratarErros(listaTokens.get(0).getLinha(), listaTokens.get(0).getLexema(), "(");
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
                    EditorDeNaoTerminais.removePrimeiroItemDaLista(listaTokens);
                    if (listaTokens.get(0).getLexema().equals(")")) {
                        EditorDeNaoTerminais.removePrimeiroItemDaLista(listaTokens);
                    } else {
                        tratarErros(listaTokens.get(0).getLinha(), listaTokens.get(0).getLexema(), ")");
                    }

                } else {
                    tratarErros(listaTokens.get(0).getLinha(), listaTokens.get(0).getLexema(), "identificador ou vazio");
                }
            }
        }));

        analisadorSintatico.addNaoTerminal(new NaoTerminal("prox_argumento", new ExecutorNaoTerminal() {
            @Override
            public void executarLeitura(ArrayList<Token> listaTokens) {
                //EditorDeNaoTerminais.removePrimeiroItemDaLista(listaTokens);
                String temp = listaTokens.get(0).getLexema();
                if (listaTokens.get(0).getLexema().equals(",")) {
                    EditorDeNaoTerminais.removePrimeiroItemDaLista(listaTokens);
                    analisadorSintatico.getNaoTerminal("argumento").executar(listaTokens);
                    analisadorSintatico.getNaoTerminal("prox_argumento").executar(listaTokens);
                } else if (listaTokens.get(0).getLexema().equals(")")) {
                    EditorDeNaoTerminais.removePrimeiroItemDaLista(listaTokens);
                } else {
                    tratarErros(listaTokens.get(0).getLinha(), listaTokens.get(0).getLexema(), ", ou )");
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
                    tratarErros(listaTokens.get(0).getLinha(), listaTokens.get(0).getLexema(), "identificador");
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
                    EditorDeNaoTerminais.removePrimeiroItemDaLista(listaTokens);
                    analisadorSintatico.getNaoTerminal("id").executar(listaTokens);
                } else {
                    tratarErros(listaTokens.get(0).getLinha(), listaTokens.get(0).getLexema(), ".");
                }
            }
        }));

        analisadorSintatico.addNaoTerminal(new NaoTerminal("acesso_matriz", new ExecutorNaoTerminal() {
            @Override
            public void executarLeitura(ArrayList<Token> listaTokens) {
                //EditorDeNaoTerminais.removePrimeiroItemDaLista(listaTokens);
                String temp = listaTokens.get(0).getLexema();
                if (temp.equals("[")) {
                    EditorDeNaoTerminais.removePrimeiroItemDaLista(listaTokens);
                    analisadorSintatico.getNaoTerminal("indice").executar(listaTokens);
                    if (listaTokens.get(0).getLexema().equals("]")) {
                        EditorDeNaoTerminais.removePrimeiroItemDaLista(listaTokens);
                    } else {
                        tratarErros(listaTokens.get(0).getLinha(), listaTokens.get(0).getLexema(), "]");
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
                    EditorDeNaoTerminais.removePrimeiroItemDaLista(listaTokens);
                    analisadorSintatico.getNaoTerminal("indice").executar(listaTokens);
                    if (listaTokens.get(0).getLexema().equals("]")) {
                        EditorDeNaoTerminais.removePrimeiroItemDaLista(listaTokens);
                    } else {
                        tratarErros(listaTokens.get(0).getLinha(), listaTokens.get(0).getLexema(), "]");
                    }
                    analisadorSintatico.getNaoTerminal("acesso_matriz").executar(listaTokens);
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
                    tratarErros(listaTokens.get(0).getLinha(), listaTokens.get(0).getLexema(), "funcao ou algoritimo");
                }
            }
        }));

        analisadorSintatico.addNaoTerminal(new NaoTerminal("funcao", new ExecutorNaoTerminal() {
            @Override
            public void executarLeitura(ArrayList<Token> listaTokens) {
                //EditorDeNaoTerminais.removePrimeiroItemDaLista(listaTokens);
                String temp = listaTokens.get(0).getLexema();
                if (temp.equals("funcao")) {
                    EditorDeNaoTerminais.removePrimeiroItemDaLista(listaTokens);
                    analisadorSintatico.getNaoTerminal("tipo_arg_retorno").executar(listaTokens);
                    analisadorSintatico.getNaoTerminal("id").executar(listaTokens);
                    if (temp.equals("(")) {
                        EditorDeNaoTerminais.removePrimeiroItemDaLista(listaTokens);
                    } else {
                        tratarErros(listaTokens.get(0).getLinha(), listaTokens.get(0).getLexema(), "(");
                    }
                    analisadorSintatico.getNaoTerminal("lista_parametros").executar(listaTokens);
                    if (temp.equals("{")) {
                        EditorDeNaoTerminais.removePrimeiroItemDaLista(listaTokens);
                    } else {
                        tratarErros(listaTokens.get(0).getLinha(), listaTokens.get(0).getLexema(), "{");
                    }
                    analisadorSintatico.getNaoTerminal("corpo").executar(listaTokens);
                    analisadorSintatico.getNaoTerminal("inst_retorno").executar(listaTokens);
                    if (temp.equals("}")) {
                        EditorDeNaoTerminais.removePrimeiroItemDaLista(listaTokens);
                    } else {
                        tratarErros(listaTokens.get(0).getLinha(), listaTokens.get(0).getLexema(), "}");
                    }
                } else {
                    tratarErros(listaTokens.get(0).getLinha(), listaTokens.get(0).getLexema(), "funcao");
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
                    EditorDeNaoTerminais.removePrimeiroItemDaLista(listaTokens);
                } else {
                    tratarErros(listaTokens.get(0).getLinha(), listaTokens.get(0).getLexema(),
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
                    EditorDeNaoTerminais.removePrimeiroItemDaLista(listaTokens);
                    if (listaTokens.get(0).getLexema().equals(")")) {
                        EditorDeNaoTerminais.removePrimeiroItemDaLista(listaTokens);
                    } else {
                        tratarErros(listaTokens.get(0).getLinha(), listaTokens.get(0).getLexema(), ")");
                    }
                } else {
                    tratarErros(listaTokens.get(0).getLinha(), listaTokens.get(0).getLexema(),
                            "inteiro ou real ou booleano ou char ou cadeia ou identificador ou vazio");
                }
            }
        }));
        
        analisadorSintatico.addNaoTerminal(new NaoTerminal("prox_parametro", new ExecutorNaoTerminal() {
            @Override
            public void executarLeitura(ArrayList<Token> listaTokens) {
                //EditorDeNaoTerminais.removePrimeiroItemDaLista(listaTokens);
                String temp = listaTokens.get(0).getLexema();
                if (temp.equals(",")) {
                    analisadorSintatico.getNaoTerminal("parametro").executar(listaTokens);
                    analisadorSintatico.getNaoTerminal("prox_parametro").executar(listaTokens);
                } else if (temp.equals(")")) {
                    EditorDeNaoTerminais.removePrimeiroItemDaLista(listaTokens);                    
                } else {
                    tratarErros(listaTokens.get(0).getLinha(), listaTokens.get(0).getLexema(),
                            ", ou )");
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
                    EditorDeNaoTerminais.removePrimeiroItemDaLista(listaTokens);
                    if (listaTokens.get(0).getLexema().equals(")")) {
                        EditorDeNaoTerminais.removePrimeiroItemDaLista(listaTokens);
                    } else {
                        tratarErros(listaTokens.get(0).getLinha(), listaTokens.get(0).getLexema(), ")");
                    }
                } else {
                    tratarErros(listaTokens.get(0).getLinha(), listaTokens.get(0).getLexema(),
                            "inteiro ou real ou booleano ou char ou cadeia ou identificador ou vazio");
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
