
package Util;

import java.io.Serializable;
import java.util.Iterator;
import java.util.PriorityQueue;
import Exceptions.*;

/**
 *Essa classe será responsável por armazenar os Clientes do Sistema, de tal modo,
 * que as operações feitas com ela sejam na ordem de Complexidade (log N), 
 * sendo N a quantidade de clientes presentes na <b>Árvore</b>.
 * 
 * @author AlmirNeto
 */
public class Tree implements ITree, Serializable{
    
    
    private Node root; //Atributo que armazena a referência para a raiz da Árvore.
    private int size; //Atributo que armazenará o tamanho da Árvore.
    /**
     * Construtor da Classe <b>Árvore</b>.
     * 
     */
    public Tree(){
        size = 0;
    }

    /**
     * Método que retorna TRUE caso a <b>Árvore</b> esteja vazia.
     * 
     * @return TRUE caso a <b>Árvore</b> esteja vazia.
     */
    public boolean isEmpty(){
        return (root == null);
    }
    /**
     * Método que retorna o tamanho atual da <b>Árvore</b>.
     * 
     * @return O Tamanho atual da <b>Árvore</b>.
     */
    public int size(){
        return size;
    }
    /**
     * Método que retorna a raiz da <b>Árvore</b>.
     * 
     * @return A Raiz da <b>Árvore</b> se existir.
     */
    public Node getRoot(){
        return root != null ? root : null;
    }
    /**
     * Método que busca determinado elemento na <b>Árvore</b> e o retorna para o método que o chamou.
     * Caso o Objeto não exista na <b>Árvore</b> esse método lançará uma exceção.
     * 
     * @param item Objeto que será procurado na <b>Árvore</b>
     * @return Retorna o Objeto se esse for encontrado na <b>Árvore</b>
     * @throws NotFoundException Caso o elemento a ser procurado não seja encontrado.
     * @see br.uefs.ecomp.treeStock.util.Arvore.Node
     */
    @Override
    public Object buscar(Comparable item) throws NotFoundException {
        if(isEmpty()){
           throw new NotFoundException();
        }
        else{
          Node verifica = root;
          while(verifica != null){
              if(item.compareTo(verifica.getDados()) == 0){
                return verifica.getDados();
              }
              else if(item.compareTo(verifica.getDados()) < 0){
                  verifica = verifica.getLeft();
              }
              else if(item.compareTo(verifica.getDados()) > 0){
                  verifica = verifica.getRight();
              }
          }
          throw new NotFoundException();   
        }
    }
    /**
     * Método que insere um novo elemento na sua devida posição na <b>Árvore</b>.
     * Caso a <b>Árvore</b> esteja vazia, insere na <b>Raiz</b>.
     * 
     * @param item Item a ser inserido na <b>Árvore</b>.
     * @throws DuplicatedDataException Caso o <b>Item</b> que está sendo inserido
     * já exista na <b>Árvore</b>.
     */
    @Override
    public void inserir(Comparable item) throws DuplicatedDataException {
        if(isEmpty()){ //Caso a Árvore esteja vazia, insere na Raiz.
            root = new Node(item);
            size++;
        }
        else{
            /*Procura a posição correta na qual o elemento deverá ser inserido.
            Caso o elemento já exista na Árvore uma exceção será lançada.*/
            Node pai = root;
            while(pai != null){
                if(item.compareTo(pai.getDados()) == 0){
                    throw new DuplicatedDataException();
                }
                else if(item.compareTo(pai.getDados()) < 0){
                    if(pai.getLeft() == null){
                        Node add = new Node(item, pai);
                        pai.setLeft(add);
                        verificarBalanceamento(pai);
                        size++;
                        return; //Encerra o laço caso o elemento seja inserido Corretamente.
                    }
                    pai = pai.getLeft();
                }
                else if(item.compareTo(pai.getDados()) > 0){
                    if(pai.getRight() == null){
                        Node add = new Node(item, pai);
                        pai.setRight(add);
                        verificarBalanceamento(pai);
                        size++;
                        return; //Encerra o laço caso o elemento seja inserido Corretamente.
                    }
                    pai = pai.getRight();
                }
            }
        }
    }
    /**
     * Método que remove um Certo elemento da <b>Árvore</b> que foi passado como parâmetro.
     * 
     * @param item Elemento que será buscado na <b>Árvore</b> e removido.
     * 
     * @throws NotFoundException Caso o elemento passado como parâmetro não exista na <b>Árvore</b>.
     */
    @Override
    public void remover(Comparable item) throws NotFoundException {
        if(this.isEmpty()){ //Caso a Árvore esteja vazia, lança uma exceção.
            throw new NotFoundException();
        }
        else{
            /*Procura o item na Árvore..
            Caso o Elemento exista o remove da Árvore.*/
            Node pai = root;
            while(pai != null){
                if(item.compareTo(pai.getDados()) == 0){
                    removerNoEncontrado(pai);
                    return;
                }
                else if(item.compareTo(pai.getDados()) < 0){
                    pai = pai.getLeft();
                }
                else if(item.compareTo(pai.getDados()) > 0){
                    pai = pai.getRight();
                }
            }
            throw new NotFoundException();
        }
    }

    @Override
    public Iterator iterator() {
        return new myIt();
    }
    
    private class myIt implements Iterator, Serializable{
        PriorityQueue<Node> iterar = ordena();
        @Override
        public boolean hasNext() {
            return iterar.isEmpty() == false;
        }

        @Override
        public Object next() {
            return iterar.poll();
        }
    
    }
    
    private PriorityQueue ordena(){
        PriorityQueue ordenado = new PriorityQueue();
        ordena(ordenado, root);
        return ordenado;
    }
    
    private void ordena(PriorityQueue ordenar, Node atual){
        if(atual == null){
            return;
        }
        ordena(ordenar, atual.getLeft());
        ordenar.add(atual.getDados());
        ordena(ordenar, atual.getRight());
    }
    
    /**
     * Método auxiliar que retorna a altura de Determinado Nó na Árvore para que seja
     * possível saber se será necessário Balancear a <b>Árvore</b>.
     * 
     *@param verifica Nó o qual será verifica a sua altura.
     * @return A Altura do Nó passado como parâmetro.
     */
    private int altura(Node verifica){
        if(verifica == null){
            return -1;
        }
        else if(verifica.getLeft() == null && verifica.getRight() == null){
            return 0;
        }
        else if(verifica.getLeft() == null){
            return 1 + altura(verifica.getRight());
        }
        else if(verifica.getRight() == null){
            return 1 + altura(verifica.getLeft());
        }
        else{
            return 1 + (Math.max(altura(verifica.getLeft()), altura(verifica.getRight())));
        }
    }
    /**
     * Método que verifica o balanceamento de determinado Nó e aplica a rotação correta
     * naquele Nó.<br><br>
     * 
     * Rotações que podem ser aplicadas ao Nó:
     * 
     * <ul>
     * <li>Rotação à Direita</li>
     * <li>Rotação à Esquerda</li>
     * <li>Rotação Dupla à Esquerda</li>
     * <li>Rotação Dupla à Direita</li>
     * </ul>
     * 
     * @param verifica Nó o qual será verificado e baçanceado.
     */
    private void verificarBalanceamento(Node verifica){
        setBalanceamento(verifica);
        int balanceamento = verifica.getFator_Balanceamento();
        
        if(balanceamento == -2){
            if(altura(verifica.getLeft().getLeft()) >= altura(verifica.getLeft().getRight())){
                verifica = rotacaoDireita(verifica);
            }
            else{
                verifica = duplaRotacaoEsquerdaDireita(verifica);
            }
        }
        else if(balanceamento == 2){
            if (altura(verifica.getRight().getRight()) >= altura(verifica.getRight().getLeft())){
		verifica = rotacaoEsquerda(verifica);

            } 
            else{
		verifica = duplaRotacaoDireitaEsquerda(verifica);
            }
        }
        if(verifica.getParent() != null){
            verificarBalanceamento(verifica.getParent());
        }
        else{
            this.root = verifica;
        }
    }
    /**Método que define o Balanceamento do Nó passado como Parâmetro.
     * 
     * Tal método verifica a altura de sua própria Branch e define seu Fator de Balanceamento,
     * subtraindo a altura da Sub-Árvore da Esquerda com a da Direita.
     * 
     * @param balanceamento Nó o qual será calculado o Fator de Balanceamento.
     * @see altura
     * 
     */
    private void setBalanceamento(Node balanceamento){
        balanceamento.setFator_Balanceamento(altura(balanceamento.getRight()) - altura(balanceamento.getLeft()));
    }
    /**
     * Método auxiliar que aplica uma Rotação a Esquerda para manter o Balanceamento da <b>Árvore</b>.
     * 
     * @param rotacionado Elemento que será rotacionado pelo Método.
     * 
     * @return Retorna o Nó que foi rotacionado.
     */
    private Node rotacaoEsquerda(Node rotacionado){
        
        Node right = rotacionado.getRight(); //Pega o filho da Direita do Nó a ser rotacionado.
        right.setParent(rotacionado.getParent()); //Atualiza o Pai do seu ex-Filho da Direita.
        
        rotacionado.setRight(right.getLeft()); //Pega o Filho da Direita do seu ex-filho da Direita.
        
        if(rotacionado.getRight() != null){ //Caso esse novo filho exista, define-se como pai do seu novo filho.
            rotacionado.getRight().setParent(rotacionado);
        }
        
        //O nó rotacionado transforma-se em filho do seu ex-Filho da Direita.
        right.setLeft(rotacionado); 
        rotacionado.setParent(right);
        //Faz com que o novo pai do Filho da Direita aponte para ele.
        if(right.getParent() != null){
            if(right.getParent().getRight() == rotacionado){
                right.getParent().setRight(right);
            }
            else if(right.getParent().getLeft() == rotacionado){
                right.getParent().setLeft(right);
            }
        }
        
        setBalanceamento(rotacionado); //Verifica o balanceamento do Nó rotacionado.
        setBalanceamento(right); //Verifica o balanceamento do Seu novo pai.
        
        return right; //Retorna o novo Pai dessa sub-árvore.
    }
    /**
     * Método auxiliar que aplica um Rotação à Direita para manter o Balanceamento da <b>Árvore</b>.
     * 
     * @param rotacionado Elemento que será rotacionado pelo Método.
     * 
     * @return Retorna o Nó que foi rotacionado.
     */
    private Node rotacaoDireita(Node rotacionado){
        
        Node left = rotacionado.getLeft(); //Pega o filho da Esquerda do Nó a ser rotacionado.
        left.setParent(rotacionado.getParent()); //Atualiza o pai do Ex-filho da Esquerda.

	rotacionado.setLeft(left.getRight()); //Pega o filho da Direita do Ex-Filho da Esquerda.

	if (rotacionado.getLeft() != null){
            rotacionado.getLeft().setParent(rotacionado);
	}

	left.setRight(rotacionado);
	rotacionado.setParent(left);
        //Faz com que o novo pai do Filho da Esquerda aponte para ele.
	if (left.getParent() != null){
            
            if (left.getParent().getRight() == rotacionado){
                left.getParent().setRight(left);
			
            } 
            else if(left.getParent().getLeft() == rotacionado){
		left.getParent().setLeft(left);
            }
	}

	setBalanceamento(rotacionado); //Verifica o Balanceamento do Nó rotacionado.
	setBalanceamento(left); //Verifica o Balanceamento do seu Novo Pai.

	return left;
    }
    /**
     * Método auxiliar que aplica uma Rotação Dupla Esquerda-Direita quando necessário
     * para balancear a Árvore novamente, após uma inserção ou remoção.
     * 
     * @param rotacionado Nó o qual deverá ocorrer a Rotação.
     * 
     * @return O Nó rotacionado.
     */
    private Node duplaRotacaoEsquerdaDireita(Node rotacionado){
        rotacionado.setLeft(rotacaoEsquerda(rotacionado.getLeft()));
	return rotacaoDireita(rotacionado);
    }
    /**
     * Método auxiliar que aplica uma Rotação Dupla Direita-Esquerda quando necessário
     * para balancear a Árvore novamente, após uma inserção ou remoção.
     * 
     * @param rotacionado Nó o qual deverá ocorrer a Rotação.
     * 
     * @return O Nó rotacionado.
     */
    private Node duplaRotacaoDireitaEsquerda(Node rotacionado){
        rotacionado.setRight(rotacaoDireita(rotacionado.getRight()));
	return rotacaoEsquerda(rotacionado);
    }
    /**
     * Método auxiliar que remove o Nó passado como parâmetro da <b>Árvore</b>.
     * 
     * @param removido Nó que será removido da Árvore.
     */
    private void removerNoEncontrado(Node removido){
        
        Node remove; //Nó auxiliar.
	if (removido.getLeft() == null || removido.getRight() == null){
            //Caso o nó a ser removido não possua filhos, o mesmo será removido.
            if (removido.getParent() == null) {
                //Caso o Nó a ser removido seja a raiz, remove a raiz e encerra a execução do método.
                this.root = null;
                removido = null;
                return;
            }			
        remove = removido;
	} 
        else{
            remove = sucessor(removido);
            removido.setDados(remove.getDados());
	}

	Node p;
	if(remove.getLeft() != null){
            p = remove.getLeft();
	} 
        else{
            p = remove.getRight();
	}
	if (p != null) {
            p.setParent(remove.getParent());
	}

	if (remove.getParent() == null) {
            this.root = p;
	} 
        else {
            if (remove == remove.getParent().getLeft()) {
		remove.getParent().setLeft(p);
            } 
            else {
                remove.getParent().setRight(p);
            }
	verificarBalanceamento(remove.getParent()); //Verifica o balanceamento após a remoção.
        }
	remove = null; //Remove o Nó.
    }
    /**
     * Método auxiliar que verifica qual o Nó correto que substituirá um Nó removido.
     * 
     * @param search Nó que será Removido da <b>Árvore</b>.
     * 
     * @return O método que substituirá o Nó que será removido.
     */
    private Node sucessor(Node search){
        
        if (search.getRight() != null){
            Node next = search.getRight();
            while (next.getLeft() != null){
		next = next.getLeft();
            }
	return next;
	} 
        else{
            Node pai = search.getParent();
            while (pai != null && search == pai.getRight()) {
		search = pai;
		pai = search.getParent();
            }
            return pai;
	}
    }

    
    /**
     * Classe responsável por armazenar os dados que serão inseridos na <b>Árvore</b>.
     * Tais como seu Fator de Balanceamento, Nó Pai e seus 2 filhos (direita e esquerda).
     * 
     * Os Objetos do Tipo de Node são os Objetos da Árvore porém encapsulados e com
     * informações adicionais.
     */
    private class Node implements Serializable{
        
        private Comparable dados; //Atributo de Armazenamento do Dado.
        private Node parent; //Nó responsável por armazenar a referência do "Pai" do Nó Atual.
        private Node left; //Nó responsável por armazenar a referência do "Filho" da esquerda do Nó Atual.
        private Node right; //Nó responsável por armazenar a referência do "Filho" da direita do Nó Atual.
        private int fator_Balanceamento; //Atributo responsável por armazenar o fator de balanceamento do Nó.
        
        public Node(Comparable data){
            fator_Balanceamento = 0;
            dados = data;
            parent = null;
        }
        
        public Node(Comparable data, Node pai){
            fator_Balanceamento = 0;
            dados = data;
            parent = pai;
        }

        public Comparable getDados() {
            return dados;
        }

        public void setDados(Comparable dados) {
            this.dados = dados;
        }

        public Node getParent() {
            return parent;
        }

        public void setParent(Node parent) {
            this.parent = parent;
        }

        public Node getLeft() {
            return left;
        }

        public void setLeft(Node left) {
            this.left = left;
        }

        public Node getRight() {
            return right;
        }

        public void setRight(Node right) {
            this.right = right;
        } 
        
        public int getFator_Balanceamento() {
            return fator_Balanceamento;
        }

        public void setFator_Balanceamento(int fator_Balanceamento) {
            this.fator_Balanceamento = fator_Balanceamento;
        }
    }
    
}
