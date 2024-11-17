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
    
    private final double cooldown = 1.0;
    private double cooldownCount;
    
    public SimuladorABB() {
        super( 800, 600, "Simulador de Árvores Binárias de Busca", 60, true );
    }

    @Override
    public void create() {
        arvore = new ArvoreBinariaBusca<>();
        arvore.put( 5, "cinco" );
        arvore.put( 2, "dois" );
        arvore.put( 10, "dez" );
        arvore.put( 15, "quinze" );
        arvore.put( 12, "doze" );
        arvore.put( 1, "um" );
        arvore.put( 3, "três" );
        nos = arvore.coletarParaDesenho();
        margemCima = 100;
        margemEsquerda = 50;
        raio = 20;
        espacamento = 50;
        cooldownCount = cooldown;
    }

    @Override
    public void update( double delta ) {
        
        cooldownCount -= delta;
        if(cooldownCount <= 0){
            //System.out.println("TIMEOUT");
            cooldownCount += cooldown;
        }
        
    
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
            
            if(opcao != null && opcao.matches("[0-9]+")){
                arvore.put(Integer.valueOf(opcao), "");
                nos = arvore.coletarParaDesenho();
            }
        }
            
                           
        
        
    }

    @Override
    public void draw() {
        //Text on Screen
        
        //setBackground(GOLD);
        setFontSize(20);
        drawText("1 - Inserir", new Vector2(5, 5), BLACK);
        drawText("2 - Em nível", new  Vector2(5, 25), BLACK);
        drawText("3 - Em pré-ordem", new  Vector2(5, 45), BLACK);
        drawText("4 - Em ordem", new  Vector2(5, 65), BLACK);
        drawText("5 - Em pós-ordem", new  Vector2(5, 85), BLACK);
        
        
        
        for ( ArvoreBinariaBusca.Node<Integer, String> no : nos ) {
            
            // Desenhar no
            desenharNo( no, espacamento , espacamento );
            drawText(no.key.toString(), new Vector2((((espacamento * no.ranque + margemEsquerda) - 10)), ((espacamento * no.nivel + margemCima) - 5)) , BLACK);
        
            // Desenhar linha do no pai para o filho da esquerda, se existir
            if(no.left != null){
                drawLine((espacamento * no.ranque + margemEsquerda), (espacamento * no.nivel + margemCima) + raio,
                    (espacamento * no.left.ranque + margemEsquerda), (espacamento * no.left.nivel + margemCima) - raio, 
                    PINK);
            }
            
            // Desenhar linha do no pai para o filho da direita, se existir
            if(no.right != null){
                drawLine((espacamento * no.ranque + margemEsquerda), (espacamento * no.nivel + margemCima) + raio,
                    (espacamento * no.right.ranque + margemEsquerda), (espacamento * no.right.nivel + margemCima) - raio, 
                    PINK);
            }
        }
        
    }
    
    private void desenharNo( ArvoreBinariaBusca.Node<Integer, String> no, int espHorizontal, int espVertical ) {
        fillCircle( (espHorizontal * no.ranque + margemEsquerda), (espVertical * no.nivel + margemCima), raio, no.cor );
        drawCircle( (espHorizontal * no.ranque + margemEsquerda), (espVertical * no.nivel + margemCima), raio, BLACK );
    }
    
    public static void main( String[] args ) {
        new SimuladorABB();
    }
    
}
