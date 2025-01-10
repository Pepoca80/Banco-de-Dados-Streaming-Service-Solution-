import javax.swing.*;
import java.awt.*;

public class SSS extends JFrame {
    private Timer blinkTimer;
    private boolean isTitleVisible = true;

    // Caminho para a imagem PNG (se não for usada, pode ser ignorada)
    private final Image backgroundImage;

    // Botão para ser posicionado dinamicamente
    private final JButton startButton;

    public SSS() {
        // Imagem de fundo (remover se desnecessário)
        backgroundImage = null;

        // Configurações da Janela
        setTitle("Streaming Service Solution");
        setSize(800, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        // Botão de início com estilo semelhante ao DatabaseGUI
        startButton = new JButton("Iniciar");
        startButton.setFont(new Font("Roboto", Font.PLAIN, 32));
        startButton.setForeground(Color.WHITE);
        startButton.setBackground(new Color(18, 18, 18));
        startButton.setFocusPainted(false);

        // Efeito de hover
        startButton.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                startButton.setForeground(new Color(229, 9, 20));
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent evt) {
                startButton.setForeground(Color.WHITE);
            }
        });

        // Ação do botão
        startButton.addActionListener(e -> openMainApplication());

        // Painel Customizado para o Título e Fundo Preto
        JPanel titlePanel = new JPanel(null) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;

                // Configura fundo preto
                g2d.setColor(Color.BLACK);
                g2d.fillRect(0, 0, getWidth(), getHeight());

                // Texto "Banco de Dados" (Topo, Maior)
                g2d.setFont(new Font("Courier New", Font.BOLD, 60));
                g2d.setColor(isTitleVisible ? Color.RED : getBackground()); // Piscar em vermelho
                String topText = "Streaming Service";
                FontMetrics topMetrics = g2d.getFontMetrics();
                int topX = (getWidth() - topMetrics.stringWidth(topText)) / 2;
                int topY = getHeight() / 2 - 40;
                g2d.drawString(topText, topX, topY);

                // Texto "Streaming" (Abaixo, Maior)
                g2d.setFont(new Font("Courier New", Font.BOLD, 60));
                String bottomText = "Solution";
                FontMetrics bottomMetrics = g2d.getFontMetrics();
                int bottomX = (getWidth() - bottomMetrics.stringWidth(bottomText)) / 2;
                int bottomY = topY + 50; // Posiciona logo abaixo de "Banco de Dados"
                g2d.drawString(bottomText, bottomX, bottomY);

                // Atualiza a posição do botão dinamicamente abaixo do texto "Streaming"
                int buttonWidth = 300;
                int buttonHeight = 80;
                int buttonX = (getWidth() - buttonWidth) / 2; // Centraliza o botão
                int buttonY = bottomY + 30; // Posiciona 30px abaixo do texto
                startButton.setBounds(buttonX, buttonY, buttonWidth, buttonHeight);
            }
        };

        // Configurações do painel principal
        titlePanel.add(startButton); // Adiciona o botão ao painel customizado

        // Timer para o efeito de piscar
        blinkTimer = new Timer(500, e -> {
            isTitleVisible = !isTitleVisible;
            titlePanel.repaint(); // Atualiza o painel para refletir a mudança
        });
        blinkTimer.start();

        // Adiciona o painel principal à Janela
        add(titlePanel);

        setVisible(true);
    }

    private void openMainApplication() {
        // Para o timer de piscagem
        blinkTimer.stop();
        // Capturar localização e tamanho da janela atual
        Point location = getLocation();
        Dimension size = getSize();
        // Fecha a janela atual
        dispose();

        // Abre a próxima janela na mesma posição e tamanho
        SwingUtilities.invokeLater(() -> new DatabaseGUI(location, size));
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(SSS::new);
    }
}



