import java.awt.*;//es otra herramienta para crear interfaces gráficas. Incluye cosas como botones, etiquetas, paneles, y más.
import java.awt.event.*;//Importa clases relacionadas con eventos, como clics de botones, teclas presionadas, etc.
import java.util.ArrayList;//que es una estructura de datos dinámica para almacenar listas de elementos
import java.util.Random;// crear puntos aleatorios, en este caso comida
import javax.swing.*;


public class SnakeGame extends JPanel implements ActionListener, KeyListener {//es una clase de Swing que te permite crear paneles donde puedes dibujar o agregar componentes gráficos
    private class Tile{//casillas
        int x;
        int y;

        Tile(int x, int y){//constructor de casillas
        this.x =x;
        this.y =y;
        }

    }




    int boardWidth;//declaramos variables
    int boardHeight;
    int tileSize = 25;//tamaño de la casilla del pixel 

    //Snake
    Tile snakeHead;//Tile porque la cabeza de la serpiente va a tener las propiedades de Tile que es casillas
    ArrayList<Tile> snakeBody;// Lista para guardar todas las partes del cuerpo de la serpiente (además de la cabeza)

    //food
    Tile food;//Tile para comida, porque lleva las dimensiones de casilla
    Random random;//este es para colocar comida de la serpiente alazar

    //game logic
    Timer gameLoop;//variable para la velocidad de dibujado, ya que este dibuja constante mente la variable en nuestro Frame
    int velocityX;//esta va hacer la direccion en el eje x
    int velocityY;//direccion y
    boolean gameOver =  false;//declaramos una variable para finalizar el juego

    SnakeGame(int boardWidth, int boardHeight){//Un constructor se usa para inicializar un objeto cuando se crea una instancia de la clase.
        this.boardWidth=boardWidth;
        this.boardHeight=boardHeight;
        
        setPreferredSize(new Dimension(this.boardWidth, this.boardHeight));//la cantidad de pixeles en eje x y y
        setBackground(Color.BLACK);//color de nuestra dimension o fondo
        addKeyListener(this);// Le dice al programa que esta clase (this) va a escuchar los eventos del teclado
        setFocusable(true);// Permite que el componente actual pueda recibir eventos del teclado

        snakeHead = new Tile(5, 5);//esto significa que la cabeza de la serpiente va a estar en la casilla 5 * 5 del eje x y y
        snakeBody = new ArrayList<Tile>();// Creamos la lista para almacenar cada parte del cuerpo de la serpiente

        food = new Tile(10, 10); // significa que va a aprecer la comida de la serpiente en la casilla 10*10
        random = new Random();//cramos un objeto para que la comida se cree aleatoriamente
        placeFood();//funcion
        
        velocityX = 0;//esta es la direcion en la que se mueve la sepiente si es negativa va para arriba, si es positiva para abajo
        velocityY = 0;// si es negativa va para la izquierda y si es positiva para la derecha

        gameLoop = new Timer(100, this);//velocidad, quiere decir que se movera en algun eje cada 100 milisegundos
        gameLoop.start();
    }

    public void paintComponent(Graphics g){//para personalizar cómo se dibuja el contenido en el panel de tu juego
        super.paintComponent(g);// Llama al método de la clase base para realizar el dibujo predeterminado
        draw(g);// Llama a tu propio método 'draw' para dibujar en el panel
    }

    public void draw(Graphics g){
        //cuadricula
        for (int i=0; i<boardWidth/tileSize; i++){
            //x1,y1,x2,t2
            //lineas verticales
            g.drawLine(i*tileSize, 0, i*tileSize, boardHeight);
            //lineas horizontales
            g.drawLine(0, i*tileSize, boardWidth, i*tileSize);
        }

        // food
        g.setColor(Color.BLUE);//color de la comida
        g.fillRect(food.x*tileSize, food.y*tileSize, tileSize, tileSize);//tamaño y donde estara ubicacion incial
        

        
        //Snake head
        g.setColor(Color.ORANGE);//color de la serpiente
        g.fillRect(snakeHead.x*tileSize, snakeHead.y*tileSize, tileSize, tileSize);//tamaño y ubicacion
    
        //Snake body 
        for (int i = 0; i < snakeBody.size(); i++){// Recorremos todas las partes del cuerpo
            Tile snakePart = snakeBody.get(i);// Obtenemos cada parte del cuerpo
            g.fillRect(snakePart.x * tileSize, snakePart.y * tileSize, tileSize, tileSize);// Dibujamos cada parte en el tablero (convertido a píxeles)
        }

        //score
        g.setFont(new Font("Arial", Font.PLAIN, 16));//Establece el tipo y tamaño de letra
        if (gameOver){// Si el juego terminó
            g.setColor(Color.red);// Cambia el color a rojo
            // Muestra "FIN DEL JUEGO" y el puntaje
            g.drawString("FIN DEL JUEGO: "+String.valueOf(snakeBody.size()), tileSize - 16, tileSize );
        }else {
            g.setColor(Color.white);// miestras juega el color es blanco
            g.drawString("PUNTAJE: "+ String.valueOf(snakeBody.size()), tileSize - 16, tileSize );
        }
    }

    public void placeFood(){//vamos a crear la funcion para que este coloque la comida aleatoria

        food.x = random.nextInt(boardWidth/tileSize);//ancho se divide por las casillas== 600/25=24
        food.y = random.nextInt(boardHeight/tileSize);//largo se divide por las casillas == 600/25=24

    }

    public boolean collision (Tile tile1, Tile tile2){
        // Compara dos casillas (tile1 y tile2) y devuelve true si están en la misma posición
        return tile1.x == tile2.x && tile1.y == tile2.y;
    }

    public void move (){
        // eat food 
        if (collision(snakeHead, food)){
            snakeBody.add(new Tile(food.x , food.y));// Agrega una nueva parte al cuerpo donde estaba la comida
            placeFood();// Coloca una nueva comida en una posición aleatoria
        }

        //snake body
        for(int i = snakeBody.size()-1; i >= 0; i--){// Recorremos el cuerpo desde la cola hasta el inicio
            Tile snakePart = snakeBody.get(i);// Obtenemos la parte actual del cuerpo
            if (i == 0){// Si es la primera parte del cuerpo, la movemos a donde estaba la cabeza
                snakePart.x = snakeHead.x;
                snakePart.y = snakeHead.y;
            }else {// Si no es la primera parte, la movemos a donde estaba la parte anterior
                Tile prevSnakePart = snakeBody.get(i-1);
                snakePart.x = prevSnakePart.x;
                snakePart.y = prevSnakePart.y;
            }
        }

        //Snake head
        snakeHead.x += velocityX;//la velocidad 
        snakeHead.y += velocityY;//velocidad

        // game over condition
        for (int i = 0; i < snakeBody.size(); i++){// Recorremos todas las partes del cuerpo de la serpiente
            Tile snakePart = snakeBody.get(i);// Obtenemos cada parte del cuerpo
            if (collision(snakeHead, snakePart)) {// Verificamos si la cabeza colisiona con esa parte
                gameOver = true; // Si hay colisión, se termina el juego
            }
        }
        if (snakeHead.x*tileSize < 0 || snakeHead.x*tileSize > boardWidth ||
            snakeHead.y*tileSize < 0 || snakeHead.y*tileSize > boardHeight){ // Si la cabeza de la serpiente se sale del área del tablero
            gameOver = true; 
            }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        move();
        repaint();
        if (gameOver){
            gameLoop.stop();
        }

    }

    @Override
    public void keyTyped(KeyEvent e) {
        
    }

    @Override
    public void keyPressed(KeyEvent e) {
        // Este método se ejecuta cuando el usuario escribe una tecla
        // Si la tecla presionada es la flecha "arriba" y la serpiente no se está moviendo hacia la abajo
        if (e.getKeyCode() == KeyEvent.VK_UP && velocityY != 1){
            velocityX = 0;
            velocityY = -1 ;
        }// Si la tecla presionada es la flecha "abajo" y la serpiente no se está moviendo hacia la arriba
        else if (e.getKeyCode() == KeyEvent.VK_DOWN && velocityY != -1){
            velocityX = 0;
            velocityY = 1 ;
        }// Si la tecla presionada es la flecha "izquierda" y la serpiente no se está moviendo hacia la derecha
        else if (e.getKeyCode() == KeyEvent.VK_LEFT && velocityX != 1){
            velocityX = -1;
            velocityY = 0 ;
        }// Si la tecla presionada es la flecha "derecha" y la serpiente no se está moviendo hacia la izquierda
        else if (e.getKeyCode() == KeyEvent.VK_RIGHT && velocityX != -1){
            velocityX = 1;
            velocityY = 0 ;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
    }
        
}

