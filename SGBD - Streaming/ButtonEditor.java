import javax.swing.*;
import javax.swing.table.TableCellEditor;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.function.Consumer;

public class ButtonEditor extends DefaultCellEditor {
    private JButton button;
    private String label;
    private boolean clicked;
    private Consumer<String> callback; // Função a ser chamada ao clicar no botão
    private String cpf; // CPF associado ao botão

    public ButtonEditor(JCheckBox checkBox, Consumer<String> callback) {
        super(checkBox);
        this.callback = callback;

        button = new JButton();
        button.setOpaque(true);

        // Configura o listener para o botão
        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                clicked = true;
                fireEditingStopped(); // Para o modo de edição da célula
            }
        });
    }

    @Override
    public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
        label = value != null ? value.toString() : "";
        button.setText(label);

        // Recupera o CPF da linha correspondente
        cpf = table.getValueAt(row, 1).toString(); // Assume que a coluna CPF é a segunda (índice 1)

        if (isSelected) {
            button.setBackground(table.getSelectionBackground());
            button.setForeground(table.getSelectionForeground());
        } else {
            button.setBackground(table.getBackground());
            button.setForeground(table.getForeground());
        }

        clicked = true;
        return button;
    }

    @Override
    public Object getCellEditorValue() {
        if (clicked && callback != null) {
            callback.accept(cpf); // Executa a função com o CPF como argumento
        }
        clicked = false;
        return label;
    }

    @Override
    public boolean stopCellEditing() {
        clicked = false;
        return super.stopCellEditing();
    }

    @Override
    protected void fireEditingStopped() {
        super.fireEditingStopped();
    }
}
