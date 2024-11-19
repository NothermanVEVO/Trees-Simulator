package projetoesdarvores;

import aesd.ds.interfaces.List;
import br.com.davidbuzatto.jsge.core.engine.EngineFrame;
import br.com.davidbuzatto.jsge.math.CollisionUtils;
import br.com.davidbuzatto.jsge.math.Vector2;
import java.awt.Color;
import java.util.ArrayList;
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
    
    private final double cooldown = 0.5;
    private double cooldownCount;
   
    // private Color noColor = WHITE;
    private Color linhaColor = BLACK;
    
    private static ArrayList<ArvoreBinariaBusca.Node<Integer, String>> ordemParanormal = new ArrayList<>();
    
    private ArvoreBinariaBusca.Node<Integer, String> lastAlteration = null;
    
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
            timeout();
            cooldownCount += cooldown;
        }
    
        Vector2 mousePos = getMousePositionPoint();
        
        if ( isMouseButtonPressed( MOUSE_BUTTON_LEFT ) ) {
            
            for ( ArvoreBinariaBusca.Node<Integer, String> no : nos ) {

                Vector2 centro = new Vector2( 
                    espacamento * no.ranque + margemEsquerda, 
                    espacamento * no.nivel + margemCima
                );

                if ( CollisionUtils.checkCollisionPointCircle( mousePos, centro, raio ) ) {
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
        
        if(isKeyPressed(KEY_KP_1) || isKeyPressed(KEY_ONE)){
            String opcao = JOptionPane.showInputDialog(rootPane,
                    "Qual o valor do nó a ser inserido?");
            
            if(opcao != null && opcao.matches("[+-]?[0-9]+")){
                arvore.put(Integer.valueOf(opcao), "");
                nos = arvore.coletarParaDesenho();
            }
        }
        
        if(isKeyPressed(KEY_KP_2) || isKeyPressed(KEY_TWO)){
            ordemParanormal = levelEmNivel(nos);
        }
        
        if(isKeyPressed(KEY_KP_3) || isKeyPressed(KEY_THREE)){
            ordemParanormal = preOrdem(nos);
        }
        
        if(isKeyPressed(KEY_KP_4) || isKeyPressed(KEY_FOUR)){
            ordemParanormal = emOrdem(nos);
        }
        
        if(isKeyPressed(KEY_KP_5) || isKeyPressed(KEY_FIVE)){
            ordemParanormal = getPostOrder(nos);
        }
    }

    @Override
    public void draw() {
        //Text on Screen
        
        setFontSize(20);
        drawText("1 - Inserir", new Vector2(5, 5 + 490), BLACK);
        drawText("2 - Em nível", new  Vector2(5, 25 + 490), BLACK);
        drawText("3 - Em pré-ordem", new  Vector2(5, 45 + 490), BLACK);
        drawText("4 - Em ordem", new  Vector2(5, 65 + 490), BLACK);
        drawText("5 - Em pós-ordem", new  Vector2(5, 85 + 490), BLACK);
        
        
        
        for ( ArvoreBinariaBusca.Node<Integer, String> no : nos ) {
            
            // Desenhar no
            desenharNo( no, espacamento , espacamento );
            drawText(no.key.toString(), new Vector2((((espacamento * no.ranque + margemEsquerda) - 10)), ((espacamento * no.nivel + margemCima) - 5)) , BLACK);
        
            if(no == getRoot(nos)){
                drawText("Raiz", new Vector2((((espacamento * no.ranque + margemEsquerda) - 23)), ((espacamento * no.nivel + margemCima) - 5) - 30) , PINK);
            }
            
            // Desenhar linha do no pai para o filho da esquerda, se existir
            if(no.left != null){
                drawLine((espacamento * no.ranque + margemEsquerda), (espacamento * no.nivel + margemCima) + raio,
                    (espacamento * no.left.ranque + margemEsquerda), (espacamento * no.left.nivel + margemCima) - raio, 
                    linhaColor);
            }
            
            // Desenhar linha do no pai para o filho da direita, se existir
            if(no.right != null){
                drawLine((espacamento * no.ranque + margemEsquerda), (espacamento * no.nivel + margemCima) + raio,
                    (espacamento * no.right.ranque + margemEsquerda), (espacamento * no.right.nivel + margemCima) - raio, 
                    linhaColor);
            }
        }
        
    }
    
    private ArrayList<ArvoreBinariaBusca.Node<Integer, String>> levelEmNivel(List<ArvoreBinariaBusca.Node<Integer, String>> nos){
        
        ArrayList<ArvoreBinariaBusca.Node<Integer, String>> level = new ArrayList<>();
        
        int currentLevel = 0;
        boolean alteration = true;
        
        while(alteration){
            alteration = false;
            for ( ArvoreBinariaBusca.Node<Integer, String> no : nos ) {
                if(no.nivel == currentLevel){
                    level.add(no);
                    alteration = true;
                }
            }
            currentLevel++;
        }
        
        
        return level;
        
    }
    
    private ArrayList<ArvoreBinariaBusca.Node<Integer, String>> emOrdem(List<ArvoreBinariaBusca.Node<Integer, String>> nos){
        
        ArrayList<ArvoreBinariaBusca.Node<Integer, String>> ordem = new ArrayList<>();
            for ( ArvoreBinariaBusca.Node<Integer, String> no : nos ) {
                ordem.add(no);
            }

        return ordem;
        
    }
    
    private void preOrdemAuxiliar(ArvoreBinariaBusca.Node<Integer, String> no, ArrayList<ArvoreBinariaBusca.Node<Integer, String>> ordem) {
    if (no == null) {
        return; // Caso base: nó nulo.
    }
    ordem.add(no); // Visita o nó atual.
    preOrdemAuxiliar(no.left, ordem); // Percorre a subárvore esquerda.
    preOrdemAuxiliar(no.right, ordem); // Percorre a subárvore direita.
}
    
    private ArrayList<ArvoreBinariaBusca.Node<Integer, String>> preOrdem(List<ArvoreBinariaBusca.Node<Integer, String>> nos){
        
        ArrayList<ArvoreBinariaBusca.Node<Integer, String>> ordem = new ArrayList<>();

            preOrdemAuxiliar(nos.get((int)nos.getSize()/2), ordem); // Chama o auxiliar para preencher a lista.

        return ordem;
        
    }
    
    
    
    private ArrayList<ArvoreBinariaBusca.Node<Integer, String>> getPostOrder(List<ArvoreBinariaBusca.Node<Integer, String>> nos) {
        ArrayList<ArvoreBinariaBusca.Node<Integer, String>> result = new ArrayList<>();
        postOrderTraversal(getRoot(nos), result);
        return result;
    }

    private void postOrderTraversal(ArvoreBinariaBusca.Node<Integer, String> node, ArrayList<ArvoreBinariaBusca.Node<Integer, String>> result) {
        if (node == null) {
            return;
        }
        // Recursivamente percorre o filho esquerdo
        postOrderTraversal(node.left, result);
        // Recursivamente percorre o filho direito
        postOrderTraversal(node.right, result);
        // Adiciona o nó atual à lista
        result.add(node);
    }
    
    // Metodo para pegar a raiz
    private ArvoreBinariaBusca.Node<Integer, String> getRoot(List<ArvoreBinariaBusca.Node<Integer, String>> nos){
        for ( ArvoreBinariaBusca.Node<Integer, String> no : nos ) {
            if(no.nivel == 0){
                return no;
            }
        }
        return null;
    }
    
    private void desenharNo( ArvoreBinariaBusca.Node<Integer, String> no, int espHorizontal, int espVertical ) {
        fillCircle( (espHorizontal * no.ranque + margemEsquerda), (espVertical * no.nivel + margemCima), raio, no.cor );
        drawCircle( (espHorizontal * no.ranque + margemEsquerda), (espVertical * no.nivel + margemCima), raio, BLACK );
    }
    
    private void timeout(){
       
        if(lastAlteration != null){
            lastAlteration.cor = WHITE;
            lastAlteration = null;
            
        }
        if(!ordemParanormal.isEmpty()){
            ordemParanormal.get(0).cor = BLUE;
            lastAlteration = ordemParanormal.get(0);
            ordemParanormal.remove(0);
        }
    }
    
    public static void main( String[] args ) {
        new SimuladorABB();
    }
    
}
