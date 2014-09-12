/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package compiladores.utils;

import compiladores.enums.Primitiva;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Siloe
 */
public class IdentificadorTest {
    
    public IdentificadorTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }

    /**
     * Test of getPrimitiva method, of class Identificador.
     */
    @Test
    public void testGetPrimitiva() {
        System.out.println("getPrimitiva");
        char a = ';';
        Primitiva expResult = Primitiva.SIMBOLO;
        Primitiva result = Identificador.getPrimitiva(a);
        assertEquals(expResult, result);        
    }
    
}
