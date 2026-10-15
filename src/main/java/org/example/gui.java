package org.example;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class gui extends JFrame {
    private JPanel panel1;
    private JTextArea textArea1;
    private JButton generujRaportWTleButton;
    private JProgressBar progressBar1;

    public gui() {
        setTitle("INF.04 - Generator Raportów");
        setContentPane(panel1);
        setSize(500, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        initMenu();

        generujRaportWTleButton.addActionListener(e -> uruchomGenerowanieRaportu());
    }

    private void initMenu() {
        JMenuBar menuBar = new JMenuBar();
        JMenu menuPlik = new JMenu("Plik");

        JMenuItem itemZapisz = new JMenuItem("Zapisz Raport");
        JMenuItem itemWyjscie = new JMenuItem("Wyjście");

        itemZapisz.setAccelerator(KeyStroke.getKeyStroke('S', Toolkit.getDefaultToolkit().getMenuShortcutKeyMaskEx()));

        itemZapisz.addActionListener(e -> zapiszRaportDoPliku());
        itemWyjscie.addActionListener(e -> System.exit(0));

        menuPlik.add(itemZapisz);
        menuPlik.addSeparator();
        menuPlik.add(itemWyjscie);

        menuBar.add(menuPlik);
        setJMenuBar(menuBar);
    }

    private void uruchomGenerowanieRaportu() {
        generujRaportWTleButton.setEnabled(false);
        textArea1.setText("Rozpoczęto generowanie raportu w tle...\n");

        SwingWorker<Void, Integer> worker = new SwingWorker<>() {
            @Override
            protected Void doInBackground() throws Exception {
                for (int i = 1; i <= 10; i++) {
                    Thread.sleep(300); // 10 kroków co 300 ms
                    int postep = i * 10;
                    publish(postep);
                }
                return null;
            }

            @Override
            protected void process(List<Integer> chunks) {
                int latestProgress = chunks.get(chunks.size() - 1);
                progressBar1.setValue(latestProgress);
                textArea1.append("Wygenerowano wiersz raportu... (Postęp: " + latestProgress + "%)\n");
            }

            @Override
            protected void done() {
                generujRaportWTleButton.setEnabled(true);
                textArea1.append("Zakończono generowanie raportu pomyślnie!\n");

                JOptionPane.showMessageDialog(gui.this,
                        "Raport został pomyślnie wygenerowany.",
                        "Sukces",
                        JOptionPane.INFORMATION_MESSAGE);
            }
        };
        worker.execute();
    }





    private void zapiszRaportDoPliku() {
        if (textArea1.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Pole raportu jest puste! Nie ma czego zapisać.",
                    "Ostrzeżenie",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Zapisz raport jako");
        fileChooser.setFileFilter(new FileNameExtensionFilter("Pliki tekstowe (*.txt)", "txt"));

        int userSelection = fileChooser.showSaveDialog(this);

        if (userSelection == JFileChooser.APPROVE_OPTION) {
            File fileToSave = fileChooser.getSelectedFile();

            String filePath = fileToSave.getAbsolutePath();
            if (!filePath.toLowerCase().endsWith(".txt")) {
                fileToSave = new File(filePath + ".txt");
            }

            try (FileWriter writer = new FileWriter(fileToSave)) {
                writer.write(textArea1.getText());
                JOptionPane.showMessageDialog(this,
                        "Plik został pomyślnie zapisany:\n" + fileToSave.getAbsolutePath(),
                        "Zapisano",
                        JOptionPane.INFORMATION_MESSAGE);
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(this,
                        "Wystąpił błąd podczas zapisu pliku: " + ex.getMessage(),
                        "Błąd",
                        JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}