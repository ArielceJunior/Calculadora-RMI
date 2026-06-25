package br.edu.ifsuldeminas.mch.sd.rmi.client;

import java.awt.*;
import java.awt.event.*;
import java.net.MalformedURLException;
import java.rmi.ConnectException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.List;
import javax.swing.*;
import javax.swing.border.*;
import br.edu.ifsuldeminas.mch.sd.rmi.remote.Operations;

public class CalculatorGUI extends JFrame {

	private static final long serialVersionUID = 1L;

	private Operations calc;

	// Conexão
	private JTextField txtHost;
	private JLabel lblStatus;

	// Resultado e histórico
	private JTextField txtResult;
	private DefaultListModel<String> historyModel;
	private JList<String> lstHistory;

	// Aba Básico
	private JTextField txtBasicA, txtBasicB;

	// Aba Avançado
	private JTextField txtAdvA, txtAdvB;

	// Aba Conversão
	private JTextField txtConvInput;
	private JComboBox<String> cmbConvFrom, cmbConvTo;

	public CalculatorGUI() {
		super("Calculadora RMI Distribuída");
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setSize(600, 560);
		setLocationRelativeTo(null);
		setLayout(new BorderLayout(8, 8));

		add(buildConnectionPanel(), BorderLayout.NORTH);
		add(buildTabbedPane(), BorderLayout.CENTER);
		add(buildResultPanel(), BorderLayout.SOUTH);

		setVisible(true);
	}

	// -------------------------------------------------------------------------
	// Painel de conexão
	// -------------------------------------------------------------------------
	private JPanel buildConnectionPanel() {
		JPanel p = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 6));
		p.setBorder(new TitledBorder("Conexão"));

		p.add(new JLabel("Host:"));
		txtHost = new JTextField("localhost", 14);
		p.add(txtHost);

		JButton btnConnect = new JButton("Conectar");
		btnConnect.addActionListener(e -> connect());
		p.add(btnConnect);

		lblStatus = new JLabel("Desconectado");
		lblStatus.setForeground(Color.RED);
		p.add(lblStatus);

		return p;
	}

	// -------------------------------------------------------------------------
	// Abas
	// -------------------------------------------------------------------------
	private JTabbedPane buildTabbedPane() {
		JTabbedPane tabs = new JTabbedPane();
		tabs.addTab("Básico", buildBasicTab());
		tabs.addTab("Avançado", buildAdvancedTab());
		tabs.addTab("Conversão", buildConversionTab());
		tabs.addTab("Histórico", buildHistoryTab());
		return tabs;
	}

	// ---- Aba Básico ---------------------------------------------------------
	private JPanel buildBasicTab() {
		JPanel p = new JPanel(new GridBagLayout());
		p.setBorder(new EmptyBorder(12, 16, 12, 16));
		GridBagConstraints g = defaultGbc();

		g.gridx = 0; g.gridy = 0;
		p.add(new JLabel("Operando A:"), g);
		g.gridx = 1;
		txtBasicA = new JTextField(10); p.add(txtBasicA, g);

		g.gridx = 0; g.gridy = 1;
		p.add(new JLabel("Operando B:"), g);
		g.gridx = 1;
		txtBasicB = new JTextField(10); p.add(txtBasicB, g);

		g.gridy = 2; g.gridx = 0; g.gridwidth = 2;
		JPanel btns = new JPanel(new FlowLayout(FlowLayout.CENTER, 8, 4));
		btns.add(makeBtn("Somar (+)",      () -> show(calc.sum(numA(), numB()))));
		btns.add(makeBtn("Subtrair (−)",   () -> show(calc.sub(numA(), numB()))));
		btns.add(makeBtn("Multiplicar (×)",() -> show(calc.mul(numA(), numB()))));
		btns.add(makeBtn("Dividir (÷)",    () -> show(calc.div(numA(), numB()))));
		p.add(btns, g);

		return p;
	}

	// ---- Aba Avançado -------------------------------------------------------
	private JPanel buildAdvancedTab() {
		JPanel p = new JPanel(new GridBagLayout());
		p.setBorder(new EmptyBorder(12, 16, 12, 16));
		GridBagConstraints g = defaultGbc();

		g.gridx = 0; g.gridy = 0;
		p.add(new JLabel("Valor A:"), g);
		g.gridx = 1;
		txtAdvA = new JTextField(10); p.add(txtAdvA, g);

		g.gridx = 0; g.gridy = 1;
		p.add(new JLabel("Valor B (índice/expoente/base/%):"), g);
		g.gridx = 1;
		txtAdvB = new JTextField(10); p.add(txtAdvB, g);

		// Linha 1 de botões
		g.gridy = 2; g.gridx = 0; g.gridwidth = 2;
		JPanel row1 = new JPanel(new FlowLayout(FlowLayout.CENTER, 8, 4));
		row1.add(makeBtn("√ Raiz Quadrada",   () -> show(calc.sqrt(advA()))));
		row1.add(makeBtn("ⁿ√ Raiz N (B=índice)", () -> show(calc.nthRoot(advA(), advB()))));
		row1.add(makeBtn("Aᴮ Potência",       () -> show(calc.pow(advA(), advB()))));
		p.add(row1, g);

		// Linha 2 de botões
		g.gridy = 3;
		JPanel row2 = new JPanel(new FlowLayout(FlowLayout.CENTER, 8, 4));
		row2.add(makeBtn("B% de A  (Porcentagem)", () -> show(calc.percentage(advA(), advB()))));
		row2.add(makeBtn("A mod B  (Módulo)",      () -> show(calc.mod(advA(), advB()))));
		row2.add(makeBtn("A!  (Fatorial)",         () -> showFactorial(advA().longValue())));
		p.add(row2, g);

		return p;
	}

	// ---- Aba Conversão ------------------------------------------------------
	private JPanel buildConversionTab() {
		JPanel p = new JPanel(new GridBagLayout());
		p.setBorder(new EmptyBorder(12, 16, 12, 16));
		GridBagConstraints g = defaultGbc();

		g.gridx = 0; g.gridy = 0;
		p.add(new JLabel("Valor:"), g);
		g.gridx = 1;
		txtConvInput = new JTextField(16); p.add(txtConvInput, g);

		g.gridx = 0; g.gridy = 1;
		p.add(new JLabel("De:"), g);
		g.gridx = 1;
		String[] bases = {"Decimal", "Binário", "Hexadecimal"};
		cmbConvFrom = new JComboBox<>(bases); p.add(cmbConvFrom, g);

		g.gridx = 0; g.gridy = 2;
		p.add(new JLabel("Para:"), g);
		g.gridx = 1;
		cmbConvTo = new JComboBox<>(bases); cmbConvTo.setSelectedIndex(1);
		p.add(cmbConvTo, g);

		g.gridx = 0; g.gridy = 3; g.gridwidth = 2;
		JButton btnConvert = new JButton("Converter");
		btnConvert.addActionListener(e -> doConvert());
		p.add(btnConvert, g);

		return p;
	}

	// ---- Aba Histórico -------------------------------------------------------
	private JPanel buildHistoryTab() {
		JPanel p = new JPanel(new BorderLayout(4, 4));
		p.setBorder(new EmptyBorder(8, 8, 8, 8));

		historyModel = new DefaultListModel<>();
		lstHistory = new JList<>(historyModel);
		lstHistory.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 12));
		p.add(new JScrollPane(lstHistory), BorderLayout.CENTER);

		JButton btnRefresh = new JButton("Atualizar Histórico");
		btnRefresh.addActionListener(e -> refreshHistory());
		p.add(btnRefresh, BorderLayout.SOUTH);

		return p;
	}

	// -------------------------------------------------------------------------
	// Painel de resultado
	// -------------------------------------------------------------------------
	private JPanel buildResultPanel() {
		JPanel p = new JPanel(new BorderLayout(6, 4));
		p.setBorder(new TitledBorder("Resultado"));
		txtResult = new JTextField();
		txtResult.setEditable(false);
		txtResult.setFont(new Font(Font.MONOSPACED, Font.BOLD, 16));
		txtResult.setHorizontalAlignment(JTextField.RIGHT);
		p.add(txtResult, BorderLayout.CENTER);
		return p;
	}

	// -------------------------------------------------------------------------
	// Lógica de conexão
	// -------------------------------------------------------------------------
	private void connect() {
		String host = txtHost.getText().trim();
		if (host.isEmpty()) host = "localhost";
		try {
			calc = (Operations) Naming.lookup("rmi://" + host + "/CalculatorService");
			lblStatus.setText("Conectado a " + host);
			lblStatus.setForeground(new Color(0, 140, 0));
		} catch (ConnectException e) {
			error("Servidor não iniciado em " + host + ".");
		} catch (NotBoundException e) {
			error("Serviço não encontrado.");
		} catch (MalformedURLException e) {
			error("URL inválida.");
		} catch (RemoteException e) {
			error("Erro remoto: " + e.getMessage());
		}
	}

	// -------------------------------------------------------------------------
	// Conversão
	// -------------------------------------------------------------------------
	private void doConvert() {
		if (!checkConnected()) return;
		String input = txtConvInput.getText().trim().toUpperCase();
		String from = (String) cmbConvFrom.getSelectedItem();
		String to   = (String) cmbConvTo.getSelectedItem();
		if (from.equals(to)) { txtResult.setText(input); return; }
		try {
			String result = convert(from, to, input);
			txtResult.setText(result);
			historyModel.addElement(from + "(" + input + ") -> " + to + " = " + result);
		} catch (NumberFormatException ex) {
			error("Entrada inválida para base " + from + ": " + input);
		} catch (RemoteException ex) {
			error("Erro remoto: " + ex.getMessage());
		}
	}

	private String convert(String from, String to, String input) throws RemoteException {
		switch (from + "->" + to) {
			case "Decimal->Binário":      return calc.decimalToBinary(Long.parseLong(input));
			case "Decimal->Hexadecimal":  return calc.decimalToHex(Long.parseLong(input));
			case "Binário->Decimal":      return String.valueOf(calc.binaryToDecimal(input));
			case "Binário->Hexadecimal":  return calc.binaryToHex(input);
			case "Hexadecimal->Decimal":  return String.valueOf(calc.hexToDecimal(input));
			case "Hexadecimal->Binário":  return calc.hexToBinary(input);
			default: return input;
		}
	}

	// -------------------------------------------------------------------------
	// Auxiliares
	// -------------------------------------------------------------------------
	private void show(Number result) {
		txtResult.setText(format(result));
	}

	private void showFactorial(long n) {
		if (!checkConnected()) return;
		try {
			long r = calc.factorial(n);
			txtResult.setText(String.valueOf(r));
			historyModel.addElement(n + "! = " + r);
		} catch (ArithmeticException ex) {
			error(ex.getMessage());
		} catch (RemoteException ex) {
			error("Erro remoto: " + ex.getMessage());
		}
	}

	private String format(Number n) {
		if (n instanceof Double) {
			double d = n.doubleValue();
			if (d == Math.floor(d) && !Double.isInfinite(d))
				return String.valueOf((long) d);
		}
		return n.toString();
	}

	private void refreshHistory() {
		if (!checkConnected()) return;
		try {
			List<String> ops = calc.lastOperations();
			historyModel.clear();
			ops.forEach(historyModel::addElement);
		} catch (RemoteException ex) {
			error("Erro ao buscar histórico.");
		}
	}

	private boolean checkConnected() {
		if (calc == null) {
			error("Conecte-se ao servidor primeiro.");
			return false;
		}
		return true;
	}

	private Number advA() { return parseField(txtAdvA, "Valor A"); }
	private Number advB() { return parseField(txtAdvB, "Valor B"); }
	private Number numA() { return parseField(txtBasicA, "Operando A"); }
	private Number numB() { return parseField(txtBasicB, "Operando B"); }

	private double parseField(JTextField f, String name) {
		try { return Double.parseDouble(f.getText().trim()); }
		catch (NumberFormatException e) { throw new RuntimeException(name + " inválido."); }
	}

	private JButton makeBtn(String label, RemoteAction action) {
		JButton b = new JButton(label);
		b.addActionListener(e -> {
			if (!checkConnected()) return;
			try { action.run(); }
			catch (RuntimeException ex) { error(ex.getMessage()); }
			catch (RemoteException ex)  { error("Erro remoto: " + ex.getMessage()); }
		});
		return b;
	}

	private void error(String msg) {
		JOptionPane.showMessageDialog(this, msg, "Erro", JOptionPane.ERROR_MESSAGE);
	}

	private GridBagConstraints defaultGbc() {
		GridBagConstraints g = new GridBagConstraints();
		g.insets = new Insets(5, 5, 5, 5);
		g.fill = GridBagConstraints.HORIZONTAL;
		g.anchor = GridBagConstraints.WEST;
		g.gridwidth = 1;
		return g;
	}

	@FunctionalInterface
	interface RemoteAction { void run() throws RemoteException; }

	// -------------------------------------------------------------------------
	public static void main(String[] args) {
		SwingUtilities.invokeLater(CalculatorGUI::new);
	}
}
