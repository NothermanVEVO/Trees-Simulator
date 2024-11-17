package projetoesdarvores;

import aesd.ds.interfaces.List;
import br.com.davidbuzatto.jsge.core.engine.EngineFrame;
import br.com.davidbuzatto.jsge.math.CollisionUtils;
import br.com.davidbuzatto.jsge.math.Vector2;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import projetoesdarvores.esd.ArvoreBinariaBusca;

/**
 * Simulador de árvores binárias de busca:
 *     Simula as operações de inserir e remover chaves;
 *     Simula os percursos (pré-ordem, em ordem, pós-ordem e em nível).
 * 
 * @author Prof. Dr. David Buzatto
 */
public class SimuladorABB extends EngineFrame {
    
    private ArvoreBinariaBusca<Integer, String> arvore;
    private List<ArvoreBinariaBusca.Node<Integer, String>> nos;
    private int margemCima;
    private int margemEsquerda;
    private int raio;
    private int espacamento;
    
    public SimuladorABB() {
        super( 800, 600, "Simulador de Árvores Binárias de Busca", 60, true );
    }

    @Override
    public void create() {
        arvore = new ArvoreBinariaBusca<>();
        nos = arvore.coletarParaDesenho();
        margemCima = 100;
        margemEsquerda = 50;
        raio = 20;
        espacamento = 50;
    }

    @Override
    public void update( double delta ) {
        
        
    
        Vector2 mousePos = getMousePositionPoint();
        
        if ( isMouseButtonPressed( MOUSE_BUTTON_LEFT ) ) {
            
            for ( ArvoreBinariaBusca.Node<Integer, String> no : nos ) {

                Vector2 centro = new Vector2( 
                    espacamento * no.ranque + margemEsquerda, 
                    espacamento * no.nivel + margemCima
                );

                if ( CollisionUtils.checkCollisionPointCircle( mousePos.addValue(-100), centro, raio ) ) {
                    SwingUtilities.invokeLater( () -> {
                        int opcao = JOptionPane.showConfirmDialog( 
                                this, 
                                "Remover o nó " + no.key + "?",
                                "Confirmação", 
                                JOptionPane.YES_NO_OPTION );
                        if ( opcao == JOptionPane.YES_OPTION ) {
                            arvore.delete( no.key );
                            nos = arvore.coletarParaDesenho();
                        }
                    });
                }

            }
            
        }
        
        if(isKeyPressed(KEY_KP_1)){
            String opcao = JOptionPane.showInputDialog(rootPane,
                    "Qual o valor do nó a ser inserido?");
            
            arvore.put(Integer.valueOf(opcao), "");
            nos = arvore.coletarParaDesenho();
        }
            
                           
        
        
    }

    @Override
    public void draw() {
        //Text on Screen
        
        //setBackground(GOLD);
        setFontSize(20);
        drawText("1 - Inserir", new Vector2(5, 5), BLACK);
        drawText("2 - Em nível", new  Vector2(5, 25), BLACK);
        drawText("3 - Em pré-Ordem", new  Vector2(5, 45), BLACK);
        drawText("4 - Em ordem", new  Vector2(5, 65), BLACK);
        drawText("5 - Em pos-ordem", new  Vector2(5, 85), BLACK);
        
        
        
        
        
        for ( ArvoreBinariaBusca.Node<Integer, String> no : nos ) {
            
            desenharNo( no, espacamento , espacamento );
            drawText(no.key.toString(), new Vector2((((espacamento * no.ranque + margemEsquerda) - 10) + 100), ((espacamento * no.nivel + margemCima) - 5) + 100) , BLACK);
        }
    }
    
    private void desenharNo( ArvoreBinariaBusca.Node<Integer, String> no, int espHorizontal, int espVertical ) {
        fillCircle( (espHorizontal * no.ranque + margemEsquerda) + 100, (espVertical * no.nivel + margemCima) + 100, raio, no.cor );
        drawCircle( (espHorizontal * no.ranque + margemEsquerda) + 100, (espVertical * no.nivel + margemCima) + 100, raio, BLACK );
    }
    
    public static void main( String[] args ) {
        new SimuladorABB();
    }
    
}
