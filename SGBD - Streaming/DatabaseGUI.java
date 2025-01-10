import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.sql.*;
import java.sql.Date;
import java.util.*;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;

import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;

public class DatabaseGUI extends JFrame {
    private JFrame frame;
    private JTextArea resultArea;
    private JPanel inputPanel;
    private JComboBox<String> distribuidoraComboBox;
    private JPanel contentPanel;


    // Construtor padrão
    public DatabaseGUI() {
        this(null, null); // Chama o construtor parametrizado com valores padrão
    }

    public DatabaseGUI(Point location, Dimension size) {
        // Configuração da janela principal
        setTitle("Streaming Service Solution");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        // Configurar tamanho e posição
        if (size != null) {
            setSize(size); // Aplica o tamanho recebido
        } else {
            setSize(800, 600); // Tamanho padrão, se não for especificado
        }

        if (location != null) {
            setLocation(location); // Aplica a posição recebida
        } else {
            setLocationRelativeTo(null); // Centraliza se a localização não for especificada
        }
        setLayout(new BorderLayout());

        // Criar a barra lateral
        JPanel sidebar = createSidebar();

        // Criar o painel principal de conteúdo
        contentPanel = new JPanel(new CardLayout());
        contentPanel.setBackground(new Color(18, 18, 18));

        // Adicionar barra lateral e painel principal
        add(sidebar, BorderLayout.WEST);
        add(contentPanel, BorderLayout.CENTER);

        // Exibir a janela
        setVisible(true);
        // Configuração inicial...
        displayInitialData();
    }

    private JPanel createSidebar() {
        JPanel sidebar = new JPanel();
        sidebar.setBackground(new Color(0, 0, 0));
        sidebar.setPreferredSize(new Dimension(300, getHeight())); // Define a largura da barra lateral
        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS)); // Alinhamento vertical
        sidebar.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20)); // Margens internas

        // Título
        JPanel titlePanel = new JPanel(new BorderLayout());
        titlePanel.setBackground(new Color(0, 0, 0)); // Fundo igual ao sidebar
        titlePanel.setMaximumSize(new Dimension(260, 50)); // Tamanho consistente com os botões
        titlePanel.setPreferredSize(new Dimension(260, 50)); // Define a largura do título para alinhar com os botões

        JLabel title = new JLabel("Bem-Vindo ao SSS");
        title.setForeground(new Color(229, 9, 20));
        title.setFont(new Font("Roboto", Font.BOLD, 18));
        title.setHorizontalAlignment(SwingConstants.LEFT); // Alinha o texto à esquerda

        // Adicionar título ao painel
        titlePanel.add(title, BorderLayout.CENTER);
        sidebar.add(titlePanel);

        // Espaçamento
        sidebar.add(Box.createVerticalStrut(20)); // Espaço após o título

        // Botões
        addSidebarButton(sidebar, "Buscar Filmes", this::showSearchMovies);
        addSidebarButton(sidebar, "Buscar Clientes", this::showSearchClients);
        addSidebarButton(sidebar, "Verificar Catálogo", this::showCatalogUpdates);
        addSidebarButton(sidebar, "Previsão de Receita", this::showRevenueForecast);
        addSidebarButton(sidebar, "Fazer Pedido", this::handleRequestAction);
        addSidebarButton(sidebar, "Top Distribuidoras", this::showTopDistribuidoras);

        return sidebar;
    }

    private void addSidebarButton(JPanel sidebar, String label, Runnable action) {
        // Painel para encapsular e alinhar o botão
        JPanel buttonPanel = new JPanel(new BorderLayout());
        buttonPanel.setBackground(new Color(0, 0, 0)); // Fundo igual ao sidebar
        buttonPanel.setMaximumSize(new Dimension(260, 50)); // Define o tamanho máximo do painel do botão
        buttonPanel.setPreferredSize(new Dimension(260, 50)); // Define o tamanho preferido para alinhar os botões

        // Criar botão
        JButton button = new JButton(label);
        button.setForeground(Color.WHITE);
        button.setBackground(new Color(18, 18, 18));
        button.setFont(new Font("Roboto", Font.PLAIN, 16));
        button.setFocusPainted(false);
        button.setHorizontalAlignment(SwingConstants.LEFT); // Alinha o texto à esquerda

        // Efeito de hover
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setForeground(new Color(229, 9, 20));
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setForeground(Color.WHITE);
            }
        });

        // Ação do botão
        button.addActionListener(e -> action.run());

        // Adicionar o botão ao painel encapsulador
        buttonPanel.add(button, BorderLayout.CENTER);

        // Adicionar o painel do botão ao sidebar
        sidebar.add(buttonPanel);
        sidebar.add(Box.createVerticalStrut(10)); // Espaçamento entre os botões
    }


    private void showSearchMovies() {
        contentPanel.removeAll();
        JPanel panel = createSearchMoviesPanel();
        contentPanel.add(panel, "Busca por Filmes");
        contentPanel.revalidate();
        contentPanel.repaint();
    }

    private void showSearchClients() {
        contentPanel.removeAll();
        JPanel panel = createSearchClientsPanel();
        contentPanel.add(panel, "Busca por Clientes");
        contentPanel.revalidate();
        contentPanel.repaint();
    }

    private void showCatalogUpdates() {
        contentPanel.removeAll();
        JPanel panel = createCatalogUpdatesPanel();
        contentPanel.add(panel, "Atualizações no Catálogo");
        contentPanel.revalidate();
        contentPanel.repaint();
    }

    private void showRevenueForecast() {
        contentPanel.removeAll();
        JPanel panel = createRevenueForecastPanel();
        contentPanel.add(panel, "Previsão de Receita");
        contentPanel.revalidate();
        contentPanel.repaint();
    }

    private void handleRequestAction() {
        contentPanel.removeAll();
        JPanel panel = createRequestPanel(); // Painel para "Fazer Pedido"
        contentPanel.add(panel, "Fazer Pedido");
        contentPanel.revalidate();
        contentPanel.repaint();
    }

    private void showTopDistribuidoras() {
        contentPanel.removeAll();
        JPanel panel = createTopDistribuidorasPanel(); // Painel para "Top Distribuidoras"
        contentPanel.add(panel, "Top Distribuidoras");
        contentPanel.revalidate();
        contentPanel.repaint();
    }

    private void displayInitialData() {
        // Criar um painel para exibir os dados
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(new Color(18, 18, 18)); // Fundo escuro
    
        // Criar o título "Número de Entidades"
    JLabel titleLabel = new JLabel("Composição do Banco de Dados", SwingConstants.CENTER);
    titleLabel.setForeground(Color.WHITE); // Texto branco
    titleLabel.setFont(new Font("Roboto", Font.BOLD, 24)); // Fonte estilizada
    panel.add(titleLabel, BorderLayout.NORTH); // Adicionar título na parte superior
    
        // Criar o JTextArea para exibir os dados
        JTextArea textArea = new JTextArea();
        textArea.setEditable(false); // Não permite edição
        textArea.setFont(new Font("Roboto", Font.PLAIN, 16)); // Fonte estilizada
        textArea.setForeground(Color.WHITE); // Texto branco
        textArea.setBackground(new Color(18, 18, 18)); // Fundo do JTextArea escuro
        textArea.setLineWrap(true); // Quebra de linha automática
        textArea.setWrapStyleWord(true); // Quebra de palavras de forma elegante
    
        // Adicionar os dados no JTextArea

        textArea.append("\n\n");
        textArea.append(" Quantidade de filmes no banco: " + CountFunction.countRecords("Filmes") + "\n\n");
        textArea.append(" Quantidade de atores no banco: " + CountFunction.countRecords("Ator") + "\n\n");
        textArea.append(" Quantidade de diretores no banco: " + CountFunction.countRecords("Diretor") + "\n\n");
        textArea.append(" Quantidade de distribuidoras no banco: " + CountFunction.countRecords("Distribuidora") + "\n\n");
        textArea.append(" Quantidade de usuários no banco: " + CountFunction.countRecords("Usuario") + "\n\n");
    
        // Adicionar o JTextArea dentro de um JScrollPane
        JScrollPane scrollPane = new JScrollPane(textArea);
        panel.add(scrollPane, BorderLayout.CENTER);
    
        // Adicionar o painel ao contentPanel
        contentPanel.removeAll(); // Limpa o conteúdo existente
        contentPanel.add(panel, "Exibição Inicial");
        contentPanel.revalidate(); // Atualiza o layout
        contentPanel.repaint(); // Re-renderiza o conteúdo
    }

    private void populateDistributorComboBox(JComboBox<String> distributorComboBox) {
        try (Connection connection = DatabaseConnection.getConnection()) {
            PreparedStatement statement = connection.prepareStatement("SELECT DISTINCT NOME FROM DISTRIBUIDORA ORDER BY NOME");
            ResultSet resultSet = statement.executeQuery();

            distributorComboBox.addItem("Todos");
            while (resultSet.next()) {
                distributorComboBox.addItem(resultSet.getString("NOME"));
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Erro ao carregar distribuidoras: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Função para configurar a interface de busca de filmes
    public JPanel createSearchMoviesPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(new Color(18, 18, 18));

        JLabel label = new JLabel("Busca por Filmes", SwingConstants.CENTER);
        label.setForeground(Color.WHITE);
        label.setFont(new Font("Roboto", Font.BOLD, 24));
        panel.add(label, BorderLayout.NORTH);

        // Painel para filtros
        JPanel filterPanel = new JPanel(new GridLayout(0, 3, 2, 10));
        filterPanel.setBackground(new Color(18, 18, 18));

        JButton genreButton = new JButton("Selecionar Gêneros");
        JButton ageRatingButton = new JButton("Selecionar Classificação Etária");
        List<String> selectedGenres = new ArrayList<>();
        List<String> selectedAgeRatings = new ArrayList<>();

        JTextField titleField = new JTextField();
        JComboBox<String> directorComboBox = new JComboBox<>();
        JComboBox<String> actorComboBox = new JComboBox<>();
        JComboBox<String> distributorComboBox = new JComboBox<>();
        JComboBox<String> ratingComboBox = new JComboBox<>(new String[]{"Todos", "1", "2", "3", "4", "5", "6", "7", "8", "9", "10"});
        JButton executeSearchButton = new JButton("Buscar");
        // Configuração do botão "Buscar"
        executeSearchButton.setForeground(Color.WHITE);
        executeSearchButton.setBackground(Color.RED); // Botão com fundo vermelho
        executeSearchButton.setFont(new Font("Roboto", Font.BOLD, 16)); // Fonte em negrito
        executeSearchButton.setHorizontalAlignment(SwingConstants.CENTER); // Texto centralizado

        // Criar um painel para alinhar o botão
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.setBackground(new Color(18, 18, 18)); // Fundo igual ao restante do painel
        buttonPanel.add(executeSearchButton); // Adicionar o botão ao painel

         // Substituir a adição direta do botão no painel de filtros
        filterPanel.remove(executeSearchButton); // Remove o botão da posição antiga, se já estiver adicionado
        panel.add(buttonPanel, BorderLayout.SOUTH); // Adiciona o painel do botão na parte inferior do painel principal

        //executeSearchButton.setBounds(0,0,5,5);

        filterPanel.add(new JLabel("Título do Filme:"));
        filterPanel.add(new JLabel());
        filterPanel.add(titleField);
        filterPanel.add(new JLabel("Diretor:"));
        filterPanel.add(new JLabel());
        filterPanel.add(directorComboBox);
        filterPanel.add(new JLabel("Ator:"));
        filterPanel.add(new JLabel());
        filterPanel.add(actorComboBox);
        //filterPanel.add(new JLabel("Gênero:"));

        //filterPanel.add(new JLabel("Classificação etária:"));

        filterPanel.add(new JLabel("Distribuidora:"));
        filterPanel.add(new JLabel());
        filterPanel.add(distributorComboBox);
        filterPanel.add(genreButton);
        filterPanel.add(ageRatingButton);
        filterPanel.add(new JLabel("Nota Média Mínima:"));
        filterPanel.add(new JLabel());
        filterPanel.add(ratingComboBox);
        //filterPanel.add(new JLabel());
        filterPanel.add(new JLabel("Gênero"));
        filterPanel.add(new JLabel());
        filterPanel.add(genreButton);
        filterPanel.add(new JLabel("Classificação etária"));
        filterPanel.add(new JLabel());
        filterPanel.add(ageRatingButton);
        filterPanel.add(new JLabel());
        filterPanel.add(executeSearchButton);

        //JPanel searchPanel = new JPanel(new GridLayout(0, 2, 10, 10));
        //searchPanel.add(executeSearchButton);

        populateDirectorComboBox(directorComboBox);
        populateActorComboBox(actorComboBox);
        populateDistributorComboBox(distributorComboBox);

        JPanel resultPanel = new JPanel();
        resultPanel.setLayout(new BoxLayout(resultPanel, BoxLayout.Y_AXIS));
        resultPanel.setBackground(new Color(18, 18, 18));
        JScrollPane resultScrollPane = new JScrollPane(resultPanel);

        panel.add(filterPanel, BorderLayout.NORTH);
        panel.add(resultScrollPane, BorderLayout.CENTER);

        genreButton.addActionListener(e -> {
            List<String> genres = fetchDistinctValues("GENERO", "FILMES", null);
            JPanel genrePanel = new JPanel(new GridLayout(genres.size(), 1));
            List<JCheckBox> genreCheckBoxes = new ArrayList<>();

            for (String genre : genres) {
                JCheckBox checkBox = new JCheckBox(genre);
                genreCheckBoxes.add(checkBox);
                genrePanel.add(checkBox);
            }

            int selection = JOptionPane.showConfirmDialog(null, genrePanel, "Selecione Gêneros", JOptionPane.OK_CANCEL_OPTION);
            if (selection == JOptionPane.OK_OPTION) {
                selectedGenres.clear();
                for (JCheckBox checkBox : genreCheckBoxes) {
                    if (checkBox.isSelected()) {
                        selectedGenres.add(checkBox.getText());
                    }
                }
            }
        });

        ageRatingButton.addActionListener(e -> {
            List<String> ageRatings = fetchDistinctValues("CLASSIFICACAO", "FILMES", null);

            // Ordena a lista de classificações etárias. Usamos um comparador numérico, assumindo que as classificações são números.
            Collections.sort(ageRatings, new Comparator<String>() {
                @Override
                public int compare(String o1, String o2) {
                    // Tente converter para número (caso as classificações sejam numéricas)
                    try {
                        Integer num1 = Integer.parseInt(o1.replaceAll("[^0-9]", ""));  // Remove caracteres não numéricos (ex: "18 anos")
                        Integer num2 = Integer.parseInt(o2.replaceAll("[^0-9]", ""));
                        return num1.compareTo(num2);
                    } catch (NumberFormatException ex) {
                        // Caso não seja possível converter para inteiro, mantém a ordem alfabética
                        return o1.compareTo(o2);
                    }
                }
            });

            // Cria o painel de seleção de classificações etárias
            JPanel ageRatingPanel = new JPanel(new GridLayout(ageRatings.size(), 1));
            List<JCheckBox> ageRatingCheckBoxes = new ArrayList<>();

            for (String ageRating : ageRatings) {
                JCheckBox checkBox = new JCheckBox(ageRating);
                ageRatingCheckBoxes.add(checkBox);
                ageRatingPanel.add(checkBox);
            }

            int selection = JOptionPane.showConfirmDialog(null, ageRatingPanel, "Selecione Classificações Etárias", JOptionPane.OK_CANCEL_OPTION);
            if (selection == JOptionPane.OK_OPTION) {
                selectedAgeRatings.clear();
                for (JCheckBox checkBox : ageRatingCheckBoxes) {
                    if (checkBox.isSelected()) {
                        selectedAgeRatings.add(checkBox.getText());
                    }
                }
            }
        });

        executeSearchButton.addActionListener(e -> {
            resultPanel.removeAll();

            try (Connection connection = DatabaseConnection.getConnection()) {
                StringBuilder query = new StringBuilder(
                        "SELECT F.ID_FILME, F.NOME AS TITULO, F.GENERO, F.CLASSIFICACAO, " +
                                "STRING_AGG(DISTINCT D.NOME, ', ') AS DIRETORES, " +
                                "STRING_AGG(DISTINCT A.NOME, ', ') AS ATORES, " +
                                "COALESCE(TO_CHAR(ROUND(AVG(AV.NOTA), 1), 'FM999.0'), 'SEM AVALIAÇÕES') AS NOTA, " +
                                "DIST.NOME AS DISTRIBUIDORA, F.DATA_S " +
                                "FROM FILMES F " +
                                "LEFT JOIN DIRECAO DI ON F.ID_FILME = DI.ID_FILME " +
                                "LEFT JOIN DIRETOR D ON DI.ID_DIRETOR = D.ID_DIRETOR " +
                                "LEFT JOIN ELENCO E ON F.ID_FILME = E.ID_FILME " +
                                "LEFT JOIN ATOR A ON E.ID_ATOR = A.ID_ATOR " +
                                "LEFT JOIN AVALIACAO AV ON F.ID_FILME = AV.ID_FILME " +
                                "LEFT JOIN DISTRIBUIDORA DIST ON F.ID_DIST = DIST.ID_DIST " +
                                "WHERE 1=1"
                );

                List<Object> parameters = new ArrayList<>();

                // Filtro por título
                if (!titleField.getText().trim().isEmpty()) {
                    query.append(" AND UPPER(F.NOME) LIKE UPPER(?)");
                    parameters.add("%" + titleField.getText().trim() + "%");
                }

                // Filtro por diretor
                if (!"Todos".equals(directorComboBox.getSelectedItem())) {
                    query.append(" AND EXISTS (SELECT 1 FROM DIRECAO DI JOIN DIRETOR D ON DI.ID_DIRETOR = D.ID_DIRETOR WHERE DI.ID_FILME = F.ID_FILME AND D.NOME = ?)");
                    parameters.add(directorComboBox.getSelectedItem());
                }

                // Filtro por ator
                if (!"Todos".equals(actorComboBox.getSelectedItem())) {
                    query.append(" AND EXISTS (SELECT 1 FROM ELENCO E JOIN ATOR A ON E.ID_ATOR = A.ID_ATOR WHERE E.ID_FILME = F.ID_FILME AND A.NOME = ?)");
                    parameters.add(actorComboBox.getSelectedItem());
                }

                // Filtro por distribuidora
                if (!"Todos".equals(distributorComboBox.getSelectedItem())) {
                    query.append(" AND DIST.NOME = ?");
                    parameters.add(distributorComboBox.getSelectedItem());
                }

                // Filtro por gênero
                if (!selectedGenres.isEmpty()) {
                    query.append(" AND (");
                    for (int i = 0; i < selectedGenres.size(); i++) {
                        query.append("UPPER(F.GENERO) LIKE UPPER(?)");
                        if (i < selectedGenres.size() - 1) query.append(" OR ");
                        parameters.add("%" + selectedGenres.get(i) + "%");
                    }
                    query.append(")");
                }

                // Filtro por classificação etária
                if (!selectedAgeRatings.isEmpty()) {
                    query.append(" AND (");
                    for (int i = 0; i < selectedAgeRatings.size(); i++) {
                        query.append("UPPER(F.CLASSIFICACAO) LIKE UPPER(?)");
                        if (i < selectedAgeRatings.size() - 1) query.append(" OR ");
                        parameters.add("%" + selectedAgeRatings.get(i) + "%");
                    }
                    query.append(")");
                }

                // Filtro por nota mínima
                if (!"Todos".equals(ratingComboBox.getSelectedItem())) {
                    query.append(" GROUP BY F.ID_FILME, F.NOME, F.GENERO, F.CLASSIFICACAO, F.ID_DIST, F.DATA_S, DIST.NOME");
                    query.append(" HAVING ROUND(AVG(AV.NOTA), 1) >= ?");
                    parameters.add(Integer.parseInt((String) ratingComboBox.getSelectedItem()));
                } else {
                    query.append(" GROUP BY F.ID_FILME, F.NOME, F.GENERO, F.CLASSIFICACAO, F.ID_DIST, F.DATA_S, DIST.NOME");
                }

                PreparedStatement statement = connection.prepareStatement(query.toString());
                for (int i = 0; i < parameters.size(); i++) {
                    statement.setObject(i + 1, parameters.get(i));
                }

                ResultSet resultSet = statement.executeQuery();

                while (resultSet.next()) {
                    // Extraindo os dados do filme
                    String titulo = resultSet.getString("TITULO");
                    String genero = resultSet.getString("GENERO");
                    String classificacao = resultSet.getString("CLASSIFICACAO");
                    String diretores = resultSet.getString("DIRETORES");
                    String atores = resultSet.getString("ATORES");
                    String nota = resultSet.getString("NOTA");
                    String distribuidora = resultSet.getString("DISTRIBUIDORA");
                    String dataLancamento = resultSet.getString("DATA_S");

                    // Criando o painel para o filme
                    JPanel moviePanel = new JPanel(new BorderLayout());
                    moviePanel.setBackground(new Color(28, 28, 28));
                    moviePanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

                    JLabel movieTitle = new JLabel(titulo);
                    movieTitle.setForeground(Color.WHITE);
                    movieTitle.setFont(new Font("Roboto", Font.BOLD, 16));

                    JButton detailsButton = new JButton("Ver mais");

                    // Passando os dados para o botão
                    detailsButton.addActionListener(ev -> {
                        showMovieDetails(titulo, genero, classificacao, diretores, atores, nota, distribuidora, dataLancamento);
                    });

                    moviePanel.add(movieTitle, BorderLayout.CENTER);
                    moviePanel.add(detailsButton, BorderLayout.EAST);

                    resultPanel.add(moviePanel);
                }


                resultPanel.revalidate();
                resultPanel.repaint();

            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(null, "Erro ao buscar filmes: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
            }
        });

        return panel;
    }




    private void showMovieDetails(String titulo, String genero, String classificacao, String diretores,
                                  String atores, String nota, String distribuidora, String dataLancamento) {
        JDialog detailsDialog = new JDialog();
        detailsDialog.setTitle("Detalhes do Filme");
        detailsDialog.setSize(400, 300);
        detailsDialog.setLocationRelativeTo(null);

        // Painel principal
        JPanel detailsPanel = new JPanel();
        detailsPanel.setLayout(new BoxLayout(detailsPanel, BoxLayout.Y_AXIS));
        detailsPanel.setBackground(new Color(18, 18, 18)); // Fundo preto

        // Configuração padrão para os textos
        Font textFont = new Font("Roboto", Font.PLAIN, 14);
        Color textColor = Color.WHITE;

        // Adicionar informações do filme
        detailsPanel.add(createStyledLabel("Título: " + titulo, textFont, textColor));
        detailsPanel.add(createStyledLabel("Gênero: " + genero, textFont, textColor));
        detailsPanel.add(createStyledLabel("Classificação: " + classificacao, textFont, textColor));
        detailsPanel.add(createStyledLabel("Diretores: " + diretores, textFont, textColor));
        detailsPanel.add(createStyledLabel("Atores: " + atores, textFont, textColor));
        detailsPanel.add(createStyledLabel("Nota: " + nota, textFont, textColor));
        detailsPanel.add(createStyledLabel("Distribuidora: " + distribuidora, textFont, textColor));
        detailsPanel.add(createStyledLabel("Disponível até: " + dataLancamento, textFont, textColor));

        // Obter total de visualizações do filme
        try (Connection connection = DatabaseConnection.getConnection()) {
            String query = """
            SELECT COUNT(*) AS total_visualizacoes
            FROM ASSISTIDO A
            JOIN FILMES F ON A.ID_FILME = F.ID_FILME
            WHERE F.NOME = ?
        """;

            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, titulo);

            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                int totalVisualizacoes = resultSet.getInt("total_visualizacoes");
                detailsPanel.add(createStyledLabel("Total de Visualizações: " + totalVisualizacoes, textFont, textColor));
            } else {
                detailsPanel.add(createStyledLabel("Total de Visualizações: 0", textFont, textColor));
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Erro ao buscar total de visualizações: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }

        detailsDialog.add(detailsPanel);
        detailsDialog.setVisible(true);
    }

    // Método auxiliar para criar JLabel com estilo
    private JLabel createStyledLabel(String text, Font font, Color color) {
        JLabel label = new JLabel(text);
        label.setFont(font);
        label.setForeground(color);
        return label;
    }



    // Funções para preencher JComboBox
    private void populateDirectorComboBox(JComboBox<String> directorComboBox) {
        try (Connection connection = DatabaseConnection.getConnection()) {
            PreparedStatement statement = connection.prepareStatement("SELECT DISTINCT NOME FROM DIRETOR ORDER BY NOME");
            ResultSet resultSet = statement.executeQuery();

            directorComboBox.addItem("Todos");
            while (resultSet.next()) {
                directorComboBox.addItem(resultSet.getString("NOME"));
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Erro ao carregar diretores: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void populateActorComboBox(JComboBox<String> actorComboBox) {
        try (Connection connection = DatabaseConnection.getConnection()) {
            PreparedStatement statement = connection.prepareStatement("SELECT DISTINCT NOME FROM ATOR ORDER BY NOME");
            ResultSet resultSet = statement.executeQuery();

            actorComboBox.addItem("Todos");
            while (resultSet.next()) {
                actorComboBox.addItem(resultSet.getString("NOME"));
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Erro ao carregar atores: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }




    private List<String> fetchDistinctValues(String columnName, String tableName, String condition) {
        List<String> values = new ArrayList<>();
        String query = "SELECT DISTINCT " + columnName + " FROM " + tableName;
        if (condition != null) {
            query += " WHERE " + condition;
        }

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                values.add(resultSet.getString(columnName));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return values;
    }



    public JPanel createCatalogUpdatesPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(new Color(18, 18, 18));

        JLabel label = new JLabel("Atualizações no Catálogo", SwingConstants.CENTER);
        label.setForeground(Color.WHITE);
        label.setFont(new Font("Roboto", Font.BOLD, 24));
        panel.add(label, BorderLayout.NORTH);

        // Painel para exibição de resultados
        JPanel resultPanel = new JPanel();
        resultPanel.setLayout(new BoxLayout(resultPanel, BoxLayout.Y_AXIS));
        resultPanel.setBackground(new Color(18, 18, 18));
        JScrollPane resultScrollPane = new JScrollPane(resultPanel);

        panel.add(resultScrollPane, BorderLayout.CENTER);

        try (Connection connection = DatabaseConnection.getConnection()) {
            // Adicionar seções ao painel
            addCatalogSection(
                    resultPanel,
                    connection,
                    "Filmes que já saíram do catálogo",
                    """
                    SELECT F.NOME, D.NOME AS DISTRIBUIDORA, F.DATA_S AS DATA, 'SAIU' AS STATUS
                    FROM FILMES F
                    JOIN DISTRIBUIDORA D ON F.ID_DIST = D.ID_DIST
                    WHERE F.DATA_S < CURRENT_DATE
                    ORDER BY F.DATA_S DESC
                    """
            );

            addCatalogSection(
                    resultPanel,
                    connection,
                    "Filmes que vão sair nos próximos 6 meses",
                    """
                    SELECT F.NOME, D.NOME AS DISTRIBUIDORA, F.DATA_S AS DATA, 'SAINDO' AS STATUS
                    FROM FILMES F
                    JOIN DISTRIBUIDORA D ON F.ID_DIST = D.ID_DIST
                    WHERE F.DATA_S BETWEEN CURRENT_DATE AND CURRENT_DATE + INTERVAL '6 months'
                    ORDER BY F.DATA_S
                    """
            );

            addCatalogSection(
                    resultPanel,
                    connection,
                    "Filmes que estão para entrar no catálogo",
                    """
                    SELECT F.NOME, D.NOME AS DISTRIBUIDORA, F.DATA_E AS DATA, 'ENTRANDO' AS STATUS
                    FROM FILMES F
                    JOIN DISTRIBUIDORA D ON F.ID_DIST = D.ID_DIST
                    WHERE F.DATA_E > CURRENT_DATE
                    ORDER BY F.DATA_E
                    """
            );

            resultPanel.revalidate();
            resultPanel.repaint();

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Erro ao verificar o catálogo: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }

        return panel;
    }

    private void addCatalogSection(JPanel resultPanel, Connection connection, String sectionTitle, String query) throws SQLException {
        // Adicionar título da seção
        JLabel sectionLabel = new JLabel(sectionTitle);
        sectionLabel.setForeground(new Color(229, 9, 20));
        sectionLabel.setFont(new Font("Roboto", Font.BOLD, 18));
        sectionLabel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        resultPanel.add(sectionLabel);

        try (PreparedStatement statement = connection.prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()) {

            boolean hasResults = false;
            while (resultSet.next()) {
                hasResults = true;

                // Dados do filme
                String nomeFilme = resultSet.getString("NOME");
                String distribuidora = resultSet.getString("DISTRIBUIDORA");
                Date data = resultSet.getDate("DATA");
                String status = resultSet.getString("STATUS");

                // Criar painel para cada filme
                JPanel moviePanel = new JPanel(new BorderLayout());
                moviePanel.setBackground(new Color(28, 28, 28));
                moviePanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

                JLabel movieNameLabel = new JLabel(nomeFilme);
                movieNameLabel.setForeground(Color.WHITE);
                movieNameLabel.setFont(new Font("Roboto", Font.PLAIN, 16));

                // Botão "Ver Detalhes"
                JButton detailsButton = new JButton("Ver Detalhes");
                detailsButton.addActionListener(ev -> showMovieDetails(nomeFilme, distribuidora, data, status));

                moviePanel.add(movieNameLabel, BorderLayout.CENTER);
                moviePanel.add(detailsButton, BorderLayout.EAST);

                resultPanel.add(moviePanel);
            }

            if (!hasResults) {
                JLabel noResultsLabel = new JLabel("Nenhum filme encontrado.");
                noResultsLabel.setForeground(Color.LIGHT_GRAY);
                noResultsLabel.setFont(new Font("Roboto", Font.ITALIC, 14));
                noResultsLabel.setBorder(BorderFactory.createEmptyBorder(5, 0, 5, 0));
                resultPanel.add(noResultsLabel);
            }
        }
    }

    private void showMovieDetails(String nomeFilme, String distribuidora, Date data, String status) {
        JDialog detailsDialog = new JDialog();
        detailsDialog.setTitle("Detalhes do Filme");
        detailsDialog.setSize(300, 200);
        detailsDialog.setLocationRelativeTo(null);

        // Painel principal com fundo escuro
        JPanel detailsPanel = new JPanel();
        detailsPanel.setLayout(new BoxLayout(detailsPanel, BoxLayout.Y_AXIS));
        detailsPanel.setBackground(new Color(18, 18, 18)); // Fundo preto
        detailsPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Fonte e cor padrão para os rótulos
        Font textFont = new Font("Roboto", Font.PLAIN, 14);
        Color textColor = Color.WHITE;


        JLabel nameLabel = new JLabel("Nome: " + nomeFilme);
        nameLabel.setForeground(Color.WHITE);
        JLabel distributorLabel = new JLabel("Distribuidora: " + distribuidora);
        distributorLabel.setForeground(Color.WHITE);
        JLabel dateLabel = new JLabel("Data: " + data);
        dateLabel.setForeground(Color.WHITE);
        JLabel statusLabel = new JLabel("Status: " + status);
        statusLabel.setForeground(Color.WHITE);

        detailsPanel.add(nameLabel);
        detailsPanel.add(distributorLabel);
        detailsPanel.add(dateLabel);
        detailsPanel.add(statusLabel);

        detailsDialog.add(detailsPanel);
        detailsDialog.setVisible(true);
    }

    public JPanel createRevenueForecastPanel() {
        // Criar o painel principal
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(new Color(18, 18, 18));

        // Criar o título "Previsão de Receita"
        JLabel label = new JLabel("Previsão de Receita", SwingConstants.CENTER);
        label.setForeground(Color.WHITE);
        label.setFont(new Font("Roboto", Font.BOLD, 24));
        panel.add(label, BorderLayout.NORTH);

        // Criar o JTextArea para exibir os resultados
        JTextArea resultTextArea = new JTextArea();
        resultTextArea.setEditable(false); // Apenas leitura
        resultTextArea.setFont(new Font("Roboto", Font.PLAIN, 16));
        resultTextArea.setForeground(Color.WHITE); // Texto branco
        resultTextArea.setBackground(new Color(18, 18, 18)); // Fundo escuro
        resultTextArea.setLineWrap(true); // Quebra de linha automática
        resultTextArea.setWrapStyleWord(true);

        // Adicionar o JTextArea em um JScrollPane para suporte a rolagem
        JScrollPane scrollPane = new JScrollPane(resultTextArea);
        panel.add(scrollPane, BorderLayout.CENTER);

        try (Connection connection = DatabaseConnection.getConnection()) {
            // Criar campos de entrada para datas
            JTextField startDateField = new JTextField(10);
            JTextField endDateField = new JTextField(10);

            JPanel dateInputPanel = new JPanel();
            dateInputPanel.add(new JLabel("Data Início (AAAA-MM-DD):"));
            dateInputPanel.add(startDateField);
            dateInputPanel.add(Box.createHorizontalStrut(10));
            dateInputPanel.add(new JLabel("Data Fim (AAAA-MM-DD):"));
            dateInputPanel.add(endDateField);

            // Mostrar diálogo para entrada das datas
            int option = JOptionPane.showConfirmDialog(
                    null,
                    dateInputPanel,
                    "Intervalo de Datas",
                    JOptionPane.OK_CANCEL_OPTION
            );

            if (option != JOptionPane.OK_OPTION) {
                resultTextArea.setText("Operação cancelada pelo usuário.");
                return panel;
            }

            // Validar e processar as datas de entrada
            LocalDate startDate;
            LocalDate endDate;
            try {
                String startDateText = startDateField.getText().trim();
                String endDateText = endDateField.getText().trim();

                if (startDateText.isEmpty() || endDateText.isEmpty()) {
                    throw new IllegalArgumentException("Ambas as datas devem ser preenchidas.");
                }

                startDate = LocalDate.parse(startDateText);
                endDate = LocalDate.parse(endDateText);
            } catch (Exception e) {
                resultTextArea.setText("Formato de data inválido. Por favor, insira no formato AAAA-MM-DD.");
                return panel;
            }

            // Consulta SQL para receita por tipo de plano
            String queryPlano = """
            SELECT
                TP.TIPO_P AS TIPO_PLANO,
                SUM(
                    CASE
                        WHEN P.TIPO_D = 'MENSAL' THEN (TP.VALOR - TD.DESCONTO)
                        WHEN P.TIPO_D = 'SEMESTRAL' THEN (TP.VALOR - TD.DESCONTO)
                        WHEN P.TIPO_D = 'ANUAL' THEN (TP.VALOR - TD.DESCONTO)
                        ELSE 0
                    END
                ) AS RECEITA_PREVISTA
            FROM
                MENSALIDADE M
            JOIN PLANOS P ON M.ID_PLANO = P.ID_PLANO
            JOIN TIPO_P TP ON P.TIPO_P = TP.TIPO_P
            JOIN TIPO_D TD ON P.TIPO_D = TD.TIPO_D
            WHERE
                M.ANO_MES BETWEEN ? AND ?
            GROUP BY
                TP.TIPO_P
        """;

            // Consulta SQL para o total geral
            String queryTotal = """
            SELECT
                SUM(
                    CASE
                        WHEN P.TIPO_D = 'MENSAL' THEN (TP.VALOR - TD.DESCONTO)
                        WHEN P.TIPO_D = 'SEMESTRAL' THEN (TP.VALOR - TD.DESCONTO)
                        WHEN P.TIPO_D = 'ANUAL' THEN (TP.VALOR - TD.DESCONTO)
                        ELSE 0
                    END
                ) AS TOTAL_RECEITA
            FROM
                MENSALIDADE M
            JOIN PLANOS P ON M.ID_PLANO = P.ID_PLANO
            JOIN TIPO_P TP ON P.TIPO_P = TP.TIPO_P
            JOIN TIPO_D TD ON P.TIPO_D = TD.TIPO_D
            WHERE
                M.ANO_MES BETWEEN ? AND ?
        """;

            // Preparar a instrução para receita por plano
            PreparedStatement statementPlano = connection.prepareStatement(queryPlano);
            statementPlano.setDate(1, java.sql.Date.valueOf(startDate));
            statementPlano.setDate(2, java.sql.Date.valueOf(endDate));

            // Preparar a instrução para o total geral
            PreparedStatement statementTotal = connection.prepareStatement(queryTotal);
            statementTotal.setDate(1, java.sql.Date.valueOf(startDate));
            statementTotal.setDate(2, java.sql.Date.valueOf(endDate));

            // Executar consulta por tipo de plano
            ResultSet resultSetPlano = statementPlano.executeQuery();
            StringBuilder result = new StringBuilder(" Previsão de Receita por Plano:\n\n");
            while (resultSetPlano.next()) {
                String tipoPlano = resultSetPlano.getString("TIPO_PLANO");
                double receitaPrevista = resultSetPlano.getDouble("RECEITA_PREVISTA");
                result.append(" Tipo de Plano: ").append(tipoPlano)
                        .append(", Receita Prevista: R$ ").append(String.format("%.2f", receitaPrevista))
                        .append("\n");
            }

            // Executar consulta para o total geral
            ResultSet resultSetTotal = statementTotal.executeQuery();
            if (resultSetTotal.next()) {
                double totalReceita = resultSetTotal.getDouble("TOTAL_RECEITA");
                result.append("\nTOTAL: R$ ").append(String.format("%.2f", totalReceita));
            }

            // Exibir os resultados no JTextArea
            resultTextArea.setText(result.toString());

        } catch (SQLException e) {
            resultTextArea.setText("Erro ao prever receita: " + e.getMessage());
        }

        return panel;
    }


    public JPanel createSearchClientsPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(new Color(18, 18, 18));

        // Título
        JLabel label = new JLabel("Busca por Clientes", SwingConstants.CENTER);
        label.setForeground(Color.WHITE);
        label.setFont(new Font("Roboto", Font.BOLD, 24));
        panel.add(label, BorderLayout.NORTH);

        // Painel de filtros
        JPanel filterPanel = new JPanel(new GridLayout(0, 2, 10, 10));
        filterPanel.setBackground(new Color(18, 18, 18));

        JTextField nameField = new JTextField(); // Campo para nome
        JTextField cpfField = new JTextField(); // Campo para CPF
        JComboBox<String> planTypeComboBox = new JComboBox<>(new String[]{"Todos", "BASICO", "ESTUDANTE"}); // Filtro de tipo de plano
        JComboBox<String> paymentPlanComboBox = new JComboBox<>(new String[]{"Todos", "ANUAL", "SEMESTRAL", "MENSAL"}); // Filtro de tipo de pagamento
        JComboBox<String> genderComboBox = new JComboBox<>(new String[]{"Todos", "M", "F"}); // Filtro de gênero

        filterPanel.add(new JLabel("Nome do Cliente:"));
        filterPanel.add(nameField);
        filterPanel.add(new JLabel("CPF:"));
        filterPanel.add(cpfField);
        filterPanel.add(new JLabel("Tipo de Plano (Plano):"));
        filterPanel.add(planTypeComboBox);
        filterPanel.add(new JLabel("Tipo de Pagamento (Plano):"));
        filterPanel.add(paymentPlanComboBox);
        filterPanel.add(new JLabel("Gênero:"));
        filterPanel.add(genderComboBox);

        panel.add(filterPanel, BorderLayout.NORTH);

        // Painel de resultados
        JPanel resultPanel = new JPanel();
        resultPanel.setLayout(new BoxLayout(resultPanel, BoxLayout.Y_AXIS));
        resultPanel.setBackground(new Color(18, 18, 18));
        JScrollPane resultScrollPane = new JScrollPane(resultPanel);

        panel.add(resultScrollPane, BorderLayout.CENTER);

        // Configuração do botão de busca
        JButton executeSearchButton = new JButton("Buscar");
        executeSearchButton.setForeground(Color.WHITE);
        executeSearchButton.setBackground(Color.RED);
        executeSearchButton.setFont(new Font("Roboto", Font.BOLD, 16));

        // Painel para alinhar o botão
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.setBackground(new Color(18, 18, 18));
        buttonPanel.add(executeSearchButton);

        panel.add(buttonPanel, BorderLayout.SOUTH);

        // Ação do botão de busca
        executeSearchButton.addActionListener(e -> {
            resultPanel.removeAll();
            try (Connection connection = DatabaseConnection.getConnection()) {
                StringBuilder query = new StringBuilder(
                        "SELECT U.NOME AS NOME_CLIENTE, U.CPF, U.SEXO AS GENERO, TP.TIPO_P AS TIPO_PLANO, TD.TIPO_D AS TIPO_PAGAMENTO " +
                                "FROM USUARIO U " +
                                "LEFT JOIN PLANOS P ON U.CPF = P.CPF_CLIENTE " +
                                "LEFT JOIN TIPO_P TP ON P.TIPO_P = TP.TIPO_P " +
                                "LEFT JOIN TIPO_D TD ON P.TIPO_D = TD.TIPO_D " +
                                "WHERE 1=1"
                );

                List<Object> parameters = new ArrayList<>();

                // Filtro de Nome
                if (!nameField.getText().trim().isEmpty()) {
                    query.append(" AND UPPER(U.NOME) LIKE UPPER(?)");
                    parameters.add("%" + nameField.getText().trim() + "%");
                }

                // Filtro de CPF
                if (!cpfField.getText().trim().isEmpty()) {
                    query.append(" AND U.CPF = ?");
                    parameters.add(cpfField.getText().trim());
                }

                // Filtro de Tipo de Plano
                if (!"Todos".equals(planTypeComboBox.getSelectedItem())) {
                    query.append(" AND TP.TIPO_P = ?");
                    parameters.add(planTypeComboBox.getSelectedItem());
                }

                // Filtro de Tipo de Pagamento
                if (!"Todos".equals(paymentPlanComboBox.getSelectedItem())) {
                    query.append(" AND TD.TIPO_D = ?");
                    parameters.add(paymentPlanComboBox.getSelectedItem());
                }

                // Filtro de Gênero
                if (!"Todos".equals(genderComboBox.getSelectedItem())) {
                    query.append(" AND U.SEXO = ?");
                    parameters.add(genderComboBox.getSelectedItem());
                }

                PreparedStatement statement = connection.prepareStatement(query.toString());
                for (int i = 0; i < parameters.size(); i++) {
                    statement.setObject(i + 1, parameters.get(i));
                }

                ResultSet resultSet = statement.executeQuery();

                while (resultSet.next()) {
                    String nome = resultSet.getString("NOME_CLIENTE");
                    String cpf = resultSet.getString("CPF");
                    String genero = resultSet.getString("GENERO");
                    String plano = resultSet.getString("TIPO_PLANO");
                    String tipoPagamento = resultSet.getString("TIPO_PAGAMENTO");

                    // Criando painel para cada cliente
                    JPanel clientPanel = new JPanel(new BorderLayout());
                    clientPanel.setBackground(new Color(28, 28, 28));
                    clientPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

                    JLabel clientNameLabel = new JLabel(nome);
                    clientNameLabel.setForeground(Color.WHITE);
                    clientNameLabel.setFont(new Font("Roboto", Font.PLAIN, 16));

                    // Botão para ver detalhes
                    JButton detailsButton = new JButton("Ver Detalhes");
                    detailsButton.addActionListener(ev -> showClientDetails(nome, cpf, genero, plano, tipoPagamento));

                    // Botão para ver histórico
                    JButton historyButton = new JButton("Ver Histórico");
                    historyButton.addActionListener(ev -> showClientHistory(cpf));

                    JPanel buttonPanelClient = new JPanel(new FlowLayout(FlowLayout.RIGHT));
                    buttonPanelClient.setBackground(new Color(28, 28, 28));
                    buttonPanelClient.add(detailsButton);
                    buttonPanelClient.add(historyButton);

                    clientPanel.add(clientNameLabel, BorderLayout.CENTER);
                    clientPanel.add(buttonPanelClient, BorderLayout.EAST);

                    resultPanel.add(clientPanel);
                }

                resultPanel.revalidate();
                resultPanel.repaint();

            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(null, "Erro ao buscar clientes: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
            }
        });

        return panel;
    }








    private void showClientDetails(String nome, String cpf, String genero, String plano, String tipoPagamento) {
        JDialog detailsDialog = new JDialog();
        detailsDialog.setTitle("Detalhes do Cliente");
        detailsDialog.setSize(300, 250);
        detailsDialog.setLocationRelativeTo(null);

        JPanel detailsPanel = new JPanel();
        detailsPanel.setLayout(new BoxLayout(detailsPanel, BoxLayout.Y_AXIS));
        detailsPanel.setBackground(new Color(18, 18, 18));
        detailsPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JLabel nameLabel = new JLabel("Nome: " + nome);
        nameLabel.setForeground(Color.WHITE);
        JLabel cpfLabel = new JLabel("CPF: " + cpf);
        cpfLabel.setForeground(Color.WHITE);
        JLabel genderLabel = new JLabel("Gênero: " + genero);
        genderLabel.setForeground(Color.WHITE);
        JLabel planLabel = new JLabel("Tipo de Plano: " + plano);
        planLabel.setForeground(Color.WHITE);
        JLabel paymentLabel = new JLabel("Tipo de Pagamento: " + tipoPagamento);
        paymentLabel.setForeground(Color.WHITE);

        detailsPanel.add(nameLabel);
        detailsPanel.add(cpfLabel);
        detailsPanel.add(genderLabel);
        detailsPanel.add(planLabel);
        detailsPanel.add(paymentLabel);

        detailsDialog.add(detailsPanel);
        detailsDialog.setVisible(true);
    }


    private void showClientHistory(String cpf) {
        JDialog historyDialog = new JDialog();
        historyDialog.setTitle("Histórico do Cliente");
        historyDialog.setSize(500, 500);
        historyDialog.setLocationRelativeTo(null);

        // Painel principal
        JPanel historyPanel = new JPanel(new BorderLayout());
        historyPanel.setBackground(new Color(18, 18, 18));

        // Área de título
        JLabel titleLabel = new JLabel("Histórico do Cliente (CPF: " + cpf + ")", SwingConstants.CENTER);
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setFont(new Font("Roboto", Font.BOLD, 16));
        historyPanel.add(titleLabel, BorderLayout.NORTH);

        // Área de exibição dos resultados
        JPanel resultPanel = new JPanel();
        resultPanel.setLayout(new BoxLayout(resultPanel, BoxLayout.Y_AXIS));
        resultPanel.setBackground(new Color(28, 28, 28));
        JScrollPane scrollPane = new JScrollPane(resultPanel);
        historyPanel.add(scrollPane, BorderLayout.CENTER);

        // Botão de busca por período
        JButton searchByPeriodButton = new JButton("Buscar por Período");
        searchByPeriodButton.setBackground(Color.RED);
        searchByPeriodButton.setForeground(Color.WHITE);

        // Rodapé com o botão
        JPanel footerPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        footerPanel.setBackground(new Color(18, 18, 18));
        footerPanel.add(searchByPeriodButton);
        historyPanel.add(footerPanel, BorderLayout.SOUTH);

        // Ação para carregar histórico completo
        loadClientHistory(cpf, null, null, resultPanel);

        // Ação do botão para buscar por período
        searchByPeriodButton.addActionListener(ev -> {
            JTextField startDateField = new JTextField(10);
            JTextField endDateField = new JTextField(10);

            JPanel dateInputPanel = new JPanel();
            dateInputPanel.add(new JLabel("Data Início (YYYY-MM-DD):"));
            dateInputPanel.add(startDateField);
            dateInputPanel.add(Box.createHorizontalStrut(10));
            dateInputPanel.add(new JLabel("Data Fim (YYYY-MM-DD):"));
            dateInputPanel.add(endDateField);

            int option = JOptionPane.showConfirmDialog(null, dateInputPanel, "Período para Histórico", JOptionPane.OK_CANCEL_OPTION);

            if (option == JOptionPane.OK_OPTION) {
                String startDate = startDateField.getText().trim();
                String endDate = endDateField.getText().trim();

                if (startDate.isEmpty() || endDate.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Por favor, insira as duas datas.", "Erro", JOptionPane.ERROR_MESSAGE);
                } else {
                    loadClientHistory(cpf, startDate, endDate, resultPanel);
                }
            }
        });

        historyDialog.add(historyPanel);
        historyDialog.setVisible(true);
    }

    private void loadClientHistory(String cpf, String startDate, String endDate, JPanel resultPanel) {
        resultPanel.removeAll();

        try (Connection connection = DatabaseConnection.getConnection()) {
            StringBuilder query = new StringBuilder(
                    "SELECT F.NOME AS FILME, F.GENERO, F.CLASSIFICACAO, A.DATA_ASSISTIDO " +
                            "FROM ASSISTIDO A " +
                            "JOIN FILMES F ON A.ID_FILME = F.ID_FILME " +
                            "WHERE A.CPF = ?"
            );

            List<Object> parameters = new ArrayList<>();
            parameters.add(cpf);

            // Adiciona filtros de período se as datas forem fornecidas
            if (startDate != null && endDate != null) {
                query.append(" AND A.DATA_ASSISTIDO BETWEEN ? AND ?");
                parameters.add(java.sql.Date.valueOf(startDate));
                parameters.add(java.sql.Date.valueOf(endDate));
            }

            query.append(" ORDER BY A.DATA_ASSISTIDO DESC");

            PreparedStatement statement = connection.prepareStatement(query.toString());
            for (int i = 0; i < parameters.size(); i++) {
                statement.setObject(i + 1, parameters.get(i));
            }

            ResultSet resultSet = statement.executeQuery();

            boolean hasResults = false;
            while (resultSet.next()) {
                hasResults = true;

                String filme = resultSet.getString("FILME");
                String genero = resultSet.getString("GENERO");
                String classificacao = resultSet.getString("CLASSIFICACAO");
                Date dataAssistido = resultSet.getDate("DATA_ASSISTIDO");

                // Painel de exibição do nome do filme e botão
                JPanel moviePanel = new JPanel(new BorderLayout());
                moviePanel.setBackground(new Color(28, 28, 28));
                moviePanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

                JLabel movieTitle = new JLabel(filme);
                movieTitle.setForeground(Color.WHITE);

                JButton detailsButton = new JButton("Exibir Detalhes");
                detailsButton.setBackground(Color.RED);
                detailsButton.setForeground(Color.WHITE);

                // Ação do botão para exibir detalhes
                // Ação do botão para exibir detalhes
                detailsButton.addActionListener(ev -> {
                    // Personalizar o JOptionPane para fundo preto e texto branco
                    UIManager.put("OptionPane.background", Color.BLACK);
                    UIManager.put("Panel.background", Color.BLACK);
                    UIManager.put("OptionPane.messageForeground", Color.WHITE);

                    // Mensagem a ser exibida
                    String message = "Filme: " + filme + "\n" +
                            "Gênero: " + genero + "\n" +
                            "Classificação: " + classificacao + "\n" +
                            "Data Assistida: " + dataAssistido;

                    // Exibe o JOptionPane estilizado
                    JOptionPane.showMessageDialog(null, message, "Detalhes do Filme", JOptionPane.INFORMATION_MESSAGE);

                    // Restaurar as configurações padrão para evitar impacto global
                    UIManager.put("OptionPane.background", null);
                    UIManager.put("Panel.background", null);
                    UIManager.put("OptionPane.messageForeground", null);
                });

// Adiciona componentes ao painel do filme
                moviePanel.add(movieTitle, BorderLayout.CENTER);
                moviePanel.add(detailsButton, BorderLayout.EAST);

// Adiciona o painel do filme ao painel de resultados
                resultPanel.add(moviePanel);

            }

            if (!hasResults) {
                JLabel noResultsLabel = new JLabel("Nenhum filme encontrado.");
                noResultsLabel.setForeground(Color.WHITE);
                noResultsLabel.setHorizontalAlignment(SwingConstants.CENTER);
                resultPanel.add(noResultsLabel);
            }

            resultPanel.revalidate();
            resultPanel.repaint();

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Erro ao buscar histórico: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }



    private JPanel createTopDistribuidorasPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(new Color(18, 18, 18));
    
        // Título
        JLabel titleLabel = new JLabel("Top Distribuidoras", SwingConstants.CENTER);
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setFont(new Font("Roboto", Font.BOLD, 24)); // Mesma fonte do título de Previsão de Receita
        panel.add(titleLabel, BorderLayout.NORTH);
    
        // JTextArea para exibir os resultados
        JTextArea textArea = new JTextArea();
        textArea.setEditable(false);
        textArea.setFont(new Font("Roboto", Font.PLAIN, 16));
        textArea.setForeground(Color.WHITE);
        textArea.setBackground(new Color(18, 18, 18));
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);
    
        // JScrollPane para rolagem
        JScrollPane scrollPane = new JScrollPane(textArea);
        panel.add(scrollPane, BorderLayout.CENTER);
    
        // Lógica para buscar as distribuidoras mais assistidas
        try (Connection connection = DatabaseConnection.getConnection()) {
            String query = """
                SELECT 
                    D.NOME AS DISTRIBUIDORA,
                    COUNT(A.CPF) AS TOTAL_VISUALIZACOES
                FROM 
                    DISTRIBUIDORA D
                JOIN 
                    FILMES F ON D.ID_DIST = F.ID_DIST
                LEFT JOIN 
                    ASSISTIDO A ON F.ID_FILME = A.ID_FILME
                GROUP BY 
                    D.ID_DIST, D.NOME
                HAVING COUNT(A.CPF) > 0
                ORDER BY 
                    TOTAL_VISUALIZACOES DESC;
            """;
    
            try (PreparedStatement statement = connection.prepareStatement(query);
                 ResultSet resultSet = statement.executeQuery()) {
    
                StringBuilder result = new StringBuilder(" Distribuidoras Mais Assistidas:\n\n");
                while (resultSet.next()) {
                    String distribuidora = resultSet.getString("DISTRIBUIDORA");
                    int totalVisualizacoes = resultSet.getInt("TOTAL_VISUALIZACOES");
                    result.append(" Distribuidora: ").append(distribuidora)
                          .append("\n Visualizações: ").append(totalVisualizacoes)
                          .append(" \n\n");
                }
    
                textArea.setText(result.toString());
            }
        } catch (SQLException e) {
            textArea.setText(" Erro ao listar distribuidoras mais assistidas: " + e.getMessage());
        }
    
        return panel;
    }



    private JPanel createRequestPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(new Color(18, 18, 18));

        // Título
        JLabel titleLabel = new JLabel("Renovação de Contratos", SwingConstants.CENTER);
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setFont(new Font("Roboto", Font.BOLD, 24));
        panel.add(titleLabel, BorderLayout.NORTH);

        // Painel de resultados
        JPanel resultPanel = new JPanel();
        resultPanel.setLayout(new BoxLayout(resultPanel, BoxLayout.Y_AXIS));
        resultPanel.setBackground(new Color(18, 18, 18));
        JScrollPane resultScrollPane = new JScrollPane(resultPanel);

        // Painel de botão para renovação automática
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.setBackground(new Color(18, 18, 18));
        JButton autoRenewButton = new JButton("Renovar Todos");
        autoRenewButton.setBackground(new Color(229, 9, 20));
        autoRenewButton.setForeground(Color.WHITE);
        autoRenewButton.setFont(new Font("Roboto", Font.BOLD, 16));
        buttonPanel.add(autoRenewButton);

        panel.add(buttonPanel, BorderLayout.SOUTH);
        panel.add(resultScrollPane, BorderLayout.CENTER);

        // Preenchendo os dados do painel
        try (Connection connection = DatabaseConnection.getConnection()) {
            String query = """
            SELECT D.NOME AS DISTRIBUIDORA, F.NOME AS FILME, F.DATA_S
            FROM FILMES F
            JOIN DISTRIBUIDORA D ON F.ID_DIST = D.ID_DIST
            WHERE F.DATA_S BETWEEN CURRENT_DATE AND CURRENT_DATE + INTERVAL '6 months'
            ORDER BY D.NOME, F.NOME
        """;

            PreparedStatement statement = connection.prepareStatement(query);
            ResultSet resultSet = statement.executeQuery();

            Map<String, List<Map<String, Object>>> distribuidoraFilmesMap = new LinkedHashMap<>();
            while (resultSet.next()) {
                String distribuidora = resultSet.getString("DISTRIBUIDORA");
                String filme = resultSet.getString("FILME");
                Date dataSaida = resultSet.getDate("DATA_S");

                // Armazenar cada filme junto com a data
                Map<String, Object> filmeData = new HashMap<>();
                filmeData.put("filme", filme);
                filmeData.put("dataSaida", dataSaida);

                distribuidoraFilmesMap.computeIfAbsent(distribuidora, k -> new ArrayList<>()).add(filmeData);
            }

            // Preencher a interface com os resultados
            if (!distribuidoraFilmesMap.isEmpty()) {
                for (Map.Entry<String, List<Map<String, Object>>> entry : distribuidoraFilmesMap.entrySet()) {
                    String distribuidora = entry.getKey();
                    List<Map<String, Object>> filmes = entry.getValue();

                    JLabel distribuidoraLabel = new JLabel(distribuidora);
                    distribuidoraLabel.setForeground(new Color(229, 9, 20));
                    distribuidoraLabel.setFont(new Font("Roboto", Font.BOLD, 18));
                    distribuidoraLabel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
                    resultPanel.add(distribuidoraLabel);

                    for (Map<String, Object> filmeData : filmes) {
                        String filme = (String) filmeData.get("filme");
                        Date dataSaida = (Date) filmeData.get("dataSaida");

                        // Painel para cada filme
                        JPanel moviePanel = new JPanel(new BorderLayout());
                        moviePanel.setBackground(new Color(28, 28, 28));
                        moviePanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

                        JLabel movieLabel = new JLabel(String.format("%s - Saída: %s", filme, dataSaida));
                        movieLabel.setForeground(Color.WHITE);
                        movieLabel.setFont(new Font("Roboto", Font.PLAIN, 16));

                        // Botão de renovação individual
                        JButton renewButton = new JButton("Renovar");
                        renewButton.addActionListener(ev -> renewContractWithFile(distribuidora, List.of(filme)));

                        moviePanel.add(movieLabel, BorderLayout.CENTER);
                        moviePanel.add(renewButton, BorderLayout.EAST);

                        resultPanel.add(moviePanel);
                    }
                }
            } else {
                JLabel noResultsLabel = new JLabel("Nenhuma distribuidora com filmes saindo do catálogo nos próximos 6 meses.");
                noResultsLabel.setForeground(Color.LIGHT_GRAY);
                noResultsLabel.setFont(new Font("Roboto", Font.ITALIC, 14));
                noResultsLabel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
                resultPanel.add(noResultsLabel);
            }

            resultPanel.revalidate();
            resultPanel.repaint();

        } catch (SQLException e) {
            JLabel errorLabel = new JLabel("Erro ao carregar dados: " + e.getMessage());
            errorLabel.setForeground(Color.RED);
            resultPanel.add(errorLabel);
        }

        // Ação para renovar todos os contratos
        autoRenewButton.addActionListener(e -> renewAllContractsWithFile(resultPanel));

        return panel;
    }


    private void renewContractWithFile(String distribuidora, List<String> filmes) {
        try {
            String filePath = gerarPDFContrato(distribuidora, filmes, 3); // Renovar contrato por 3 anos e retornar o caminho do arquivo
            JOptionPane.showMessageDialog(null, "Contrato renovado para os filmes:\n" + String.join(", ", filmes), "Sucesso", JOptionPane.INFORMATION_MESSAGE);

            // Botão para abrir o PDF
            int escolha = JOptionPane.showConfirmDialog(null, "Deseja abrir o PDF do contrato?", "Abrir Contrato", JOptionPane.YES_NO_OPTION);
            if (escolha == JOptionPane.YES_OPTION) {
                Desktop.getDesktop().open(new File(filePath));
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Erro ao renovar contrato: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }


    private void renewAllContractsWithFile(JPanel resultPanel) {
        try {
            String query = """
            SELECT D.NOME AS DISTRIBUIDORA, F.NOME AS FILME
            FROM FILMES F
            JOIN DISTRIBUIDORA D ON F.ID_DIST = D.ID_DIST
            WHERE F.DATA_S BETWEEN CURRENT_DATE AND CURRENT_DATE + INTERVAL '6 months'
        """;

            Map<String, List<String>> distribuidoraFilmesMap = new LinkedHashMap<>();

            try (Connection connection = DatabaseConnection.getConnection();
                 PreparedStatement statement = connection.prepareStatement(query);
                 ResultSet resultSet = statement.executeQuery()) {

                while (resultSet.next()) {
                    String distribuidora = resultSet.getString("DISTRIBUIDORA");
                    String filme = resultSet.getString("FILME");
                    distribuidoraFilmesMap.computeIfAbsent(distribuidora, k -> new ArrayList<>()).add(filme);
                }
            }

            for (Map.Entry<String, List<String>> entry : distribuidoraFilmesMap.entrySet()) {
                String distribuidora = entry.getKey();
                List<String> filmes = entry.getValue();
                String filePath = gerarPDFContrato(distribuidora, filmes, 3);

                JOptionPane.showMessageDialog(null, "Contratos renovados para " + distribuidora, "Sucesso", JOptionPane.INFORMATION_MESSAGE);

                // Botão para abrir o PDF
                int escolha = JOptionPane.showConfirmDialog(null, "Deseja abrir o PDF do contrato de " + distribuidora + "?", "Abrir Contrato", JOptionPane.YES_NO_OPTION);
                if (escolha == JOptionPane.YES_OPTION) {
                    Desktop.getDesktop().open(new File(filePath));
                }
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Erro ao renovar todos os contratos: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }


    // Função para obter as distribuidoras do banco
    private List<String> getDistribuidoras(Connection connection) throws SQLException {
        List<String> distribuidoras = new ArrayList<>();
        String query = "SELECT nome FROM distribuidora";
        try (PreparedStatement statement = connection.prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                distribuidoras.add(resultSet.getString("nome"));
            }
        }
        return distribuidoras;
    }

    private String gerarPDFContrato(String distribuidora, List<String> filmes, int anos) throws DocumentException {
        Document document = new Document();
        String fileName = distribuidora.replaceAll("\\s+", "_") + "_Contrato.pdf";
        String filePath = System.getProperty("user.home") + File.separator + fileName; // Salvar na pasta do usuário

        try {
            PdfWriter.getInstance(document, new FileOutputStream(filePath));
            document.open();

            // Cabeçalho
            document.add(new Paragraph("Contrato de Licenciamento", FontFactory.getFont(FontFactory.HELVETICA_BOLD, 18)));
            document.add(new Paragraph("Distribuidora: " + distribuidora, FontFactory.getFont(FontFactory.HELVETICA, 14)));
            document.add(new Paragraph("Duração: " + anos + " anos", FontFactory.getFont(FontFactory.HELVETICA, 14)));
            document.add(new Paragraph(" "));

            // Listar filmes no contrato
            document.add(new Paragraph("Filmes Renovados:", FontFactory.getFont(FontFactory.HELVETICA_BOLD, 14)));
            for (String filme : filmes) {
                document.add(new Paragraph("- " + filme, FontFactory.getFont(FontFactory.HELVETICA, 12)));
            }

            // Rodapé
            document.add(new Paragraph(" "));
            document.add(new Paragraph("Contrato gerado automaticamente pelo sistema.", FontFactory.getFont(FontFactory.HELVETICA, 10)));

        } catch (FileNotFoundException e) {
            throw new DocumentException("Erro ao salvar o arquivo: " + e.getMessage());
        } finally {
            document.close();
        }

        return filePath; // Retorna o caminho completo do arquivo gerado
    }

    public static void main(String[] args) {
        new DatabaseGUI();
    }
}



