/*:-----------------------------------------------------------------------------
 *:                       INSTITUTO TECNOLOGICO DE LA LAGUNA
 *:                     INGENIERIA EN SISTEMAS COMPUTACIONALES
 *:                         LENGUAJES Y AUTOMATAS II           
 *: 
 *:        SEMESTRE: ______________            HORA: ______________ HRS
 *:                                   
 *:               
 *:    # Clase controladora que representa a todo el compilador
 *                 
 *:                           
 *: Archivo       : Compilador.java
 *: Autor         : Fernando Gil  
 *: Fecha         : 03/SEP/2014
 *: Compilador    : Java JDK 7
 *: Descripción   : Esta clase es la que interactua con las clases Lexico, 
 *:                 BufferEntrada, TablaSimbolos, SintacticoSemantico,
 *:                 GenCodigoInt, ManejErrores, ErroresListener, para
 *:                 realizar las funciones de un compilador para un subconjunto 
 *:                 de sentencias de lenguaje Pascal .
 *:           	     
 *: Ult.Modif.    :
 *:  Fecha      Modificó            Modificacion
 *:=============================================================================
 *:-----------------------------------------------------------------------------
 */


package compilador;

import general.ICompilador;
import general.ErroresListener;
import general.Linea_BE;
import general.Linea_TS;

public class Compilador implements ICompilador {

    // Composicion del compilador
	Lexico              lexico = new Lexico              ( this );
	SintacticoSemantico ss     = new SintacticoSemantico ( this );
	BufferEntrada       be     = new BufferEntrada       ( this );
	TablaSimbolos       ts     = new TablaSimbolos       ( this );
	ManejErrores        me     = new ManejErrores        ( this );

	ErroresListener erroresListener = null;

	GenCodigoInt gci = new GenCodigoInt(this);
    

	//--------------------------------------------------------------------------
	// Constructor de Default
	
    public Compilador(){
    }
	
	//--------------------------------------------------------------------------
	// Constructor para inicializar el objeto que escuchara los errores de compilacion
    
	public Compilador  ( ErroresListener errList ) {
		erroresListener  = errList;
	}
    
	//--------------------------------------------------------------------------
    
        @Override
	public void analizarLexico ( String codigoFuente ){
		me.inicializar ();
		be.inicializar (); 
		ts.inicializar ();
		lexico.Inicia  ();
		lexico.Analiza ( codigoFuente );
    }
    
	//--------------------------------------------------------------------------
    
        @Override
	public void analizarSintaxis(){
		me.inicializar ();
		be.restablecer ();             // Colocar el preAnalisis al inicio del buffer
		ss.analizar    ( false );      // Arrancar el analisis sintactico sin comprobacion semantica
	}
    
	//--------------------------------------------------------------------------
    
        @Override
	public void analizarSemantica(){
        me.inicializar ();
        be.restablecer ();             // Colocar el preAnalisis al inicio del buffer
        ss.analizar    ( true );       // Arrancar el analisis sintactico con comprobacion semantica
	}
    
	//--------------------------------------------------------------------------
    
        @Override
	public void generarCodigoInt(){
        me.inicializar ();
        be.restablecer ();             // Colocar el preAnalisis al inicio del buffer
        gci.generar    ();             // Arrancar la generacion de codigo intermedio
	}
    
	//--------------------------------------------------------------------------
    
        @Override
	public void agregErroresListener ( ErroresListener listener ) {
		erroresListener = listener;
	}
    
	//--------------------------------------------------------------------------
    
        @Override
	public  String[][] getBufferEntrada(){
		int tam = be.getTamaño();
		String [][] buffer = new String [ tam ][3];
        
		for ( int i = 0; i < tam; i++ ) {
			buffer [ i ][ 0 ] = be.obtElemento ( i ).getComplex ();
                        buffer [ i ][ 1 ] = be.obtElemento ( i ).getLexema  ();
                        buffer [ i ][ 2 ] = be.obtElemento ( i ).getEntrada () + "";
		}
		return buffer;
	}
    
	//--------------------------------------------------------------------------
    
	public  String [][] getTablaSimbolos(){
        
		int tam = ts.getTamaño();
		String [][] buffer = new String [ tam ][ 4 ];
        
		for ( int i = 0; i < tam; i++ ) {
                    buffer [ i ][ 0 ] = ts.obt_elemento( i ).getComplex ();
                    buffer [ i ][ 1 ] = ts.obt_elemento( i ).getLexema  ();
                    buffer [ i ][ 2 ] = ts.obt_elemento( i ).getTipo    ();
                    buffer [ i ][ 3 ] = ts.obt_elemento( i ).getAmbito  ();
		}
		return buffer;
	}
    
	//--------------------------------------------------------------------------
	
	//--------------------------------------------------------------------------
    // 24/Oct/2012

    public int getTotErrores ( int tipoError ) {
        int toterr = 0;
        switch ( tipoError ) {
            case Compilador.ERR_LEXICO     : toterr =  me.getTotErrLexico      ();
                                             break; 
            case Compilador.ERR_SINTACTICO : toterr =  me.getTotErrSintacticos ();   
                                             break;
            case Compilador.ERR_SEMANTICO  : toterr =  me.getTotErrSemanticos  ();
                                             break;
            case Compilador.ERR_CODINT     : toterr =  me.getTotErrCodInt      ();

            case Compilador.WARNING_SEMANT : toterr =  me.getTotWarningsSem    ();
        }
        return toterr;
    }
    
    //--------------------------------------------------------------------------
    
}
