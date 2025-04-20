import javax.swing.*;//Swing es una biblioteca para crear ventanas y botones en programas gráficos.

public class App {

    public static void main(String[]args) throws Exception{//throws Exception: Si hay un error, lo manda a otro lugar en vez de manejarlo aquí.

        int boardWidth=600;//ancho de 600px
        int boardHeight=boardWidth;//altura

        JFrame frame = new JFrame("Snake"); //Este comando crea una ventana en Java con el título "Snake"
        frame.setVisible(true);//Hace que la ventana sea visible en la pantalla y si fuera false la ocultaria 
        frame.setSize(boardWidth, boardHeight);//para darle la altura y el ancho a la ventana
        frame.setLocationRelativeTo(null);//centra la ventana en la pantalla del monitor
        frame.setResizable(false);//// Hace que la ventana no se pueda redimensionar, redimensionar sig: que el usuario no puede cambiar su ancho o altura
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);//hace que se cierre el programa cuando se le da clic en la x

        SnakeGame snakeGame = new SnakeGame(boardWidth, boardHeight);//constructor
        frame.add(snakeGame);//este comando agrega el objeto
        frame.pack();//este comando ajusta la venta con los pixel, para que no cuente los pixeles de la barra del title
        snakeGame.requestFocus(); // Le da el foco al panel del juego para que pueda recibir las teclas del usuario

    }

}