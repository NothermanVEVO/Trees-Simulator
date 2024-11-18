package projetoesdarvores;

import br.com.davidbuzatto.jsge.core.engine.EngineFrame;
import projetoesdarvores.esd.ArvoreAVL;

import java.util.ArrayList;
import java.util.List;
import java.awt.Color;

/**
 * Simulador de árvores AVL:
 *     Simula as operações de inserir e remover chaves;
 *     Simula os percursos (pré-ordem, em ordem, pós-ordem e em nível).
 * 
 * @author Prof. Dr. David Buzatto
 */
public class SimuladorAVL extends EngineFrame {

    private ArvoreAVL<Integer, String> arvore;
    private List<ArvoreAVL.Node<Integer, String>> nos;
    private int margemCima;
    private int margemEsquerda;
    private int raio;
    private int espacamento;
    
    private final double cooldown = 0.5;
    private double cooldownCount;
   
    private Color noColor = WHITE;
    private Color linhaColor = BLACK;
    
    private static ArrayList<ArvoreAVL.Node<Integer, String>> ordemParanormal = new ArrayList<>();
    
    private ArvoreAVL.Node<Integer, String> lastAlteration = null;
    
    public SimuladorAVL() {
        super( 800, 600, "Simulador de Árvores AVL", 60, true );
    }

    @Override
    public void create() {
    }

    @Override
    public void update( double delta ) {
                            
    }

    @Override
    public void draw() {
        
    }
    
    public static void main( String[] args ) {
        new SimuladorAVL();
    }
    
}
