package com.frederickw.calculator;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.AbstractAction;
import javax.swing.ActionMap;
import javax.swing.InputMap;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;

import com.frederickw.calculator.token.Number;
import com.frederickw.calculator.token.Operation;
import com.frederickw.calculator.token.Parenthesis;


public class Calculator extends JFrame {

	private static final long serialVersionUID = 1473821778779370379L;

	private static final Pattern PATTERN = Pattern.compile(
			"([\\d.]+)|([-+*/%^])|([()])", Pattern.CASE_INSENSITIVE);

	private double memory = 0.0;
	private boolean clear = false;

	private final JLabel expressionLabel;
	private final JTextField numberField;

	public static void main(String[] args) {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) {
		}
		EventQueue.invokeLater(new Runnable() {
			@Override
			public void run() {
				try {
					Calculator calculator = new Calculator();
					calculator.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	private Calculator() {
		setTitle("Calculator");
		setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		JPanel contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(5, 5));
		setContentPane(contentPane);

		JPanel expressionPanel = new JPanel();
		expressionPanel.setLayout(new BorderLayout(5, 5));
		contentPane.add(expressionPanel, BorderLayout.NORTH);

		expressionLabel = new JLabel(" ");
		expressionLabel.setFont(new Font("Tahoma", Font.PLAIN, 15));
		JScrollPane expressionScrollPane = new JScrollPane(expressionLabel);
		expressionScrollPane
				.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
		expressionScrollPane
				.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);
		expressionPanel.add(expressionScrollPane, BorderLayout.NORTH);

		numberField = new JTextField("0");
		numberField.setBackground(Color.WHITE);
		numberField.setEditable(false);
		numberField.setHorizontalAlignment(SwingConstants.TRAILING);
		numberField.setFont(new Font("Tahoma", Font.PLAIN, 25));
		expressionPanel.add(numberField, BorderLayout.CENTER);

		JPanel buttonPanel = new JPanel();
		GridBagLayout gbl_buttonPanel = new GridBagLayout();
		gbl_buttonPanel.columnWidths = new int[] { 45, 45, 45, 0, 0, 0 };
		gbl_buttonPanel.rowHeights = new int[] { 45, 45, 45, 45, 45, 45, 0 };
		gbl_buttonPanel.columnWeights = new double[] { 1.0, 1.0, 1.0, 1.0, 1.0,
				Double.MIN_VALUE };
		gbl_buttonPanel.rowWeights = new double[] { 1.0, 1.0, 1.0, 1.0, 1.0,
				1.0, Double.MIN_VALUE };
		buttonPanel.setLayout(gbl_buttonPanel);
		contentPane.add(buttonPanel, BorderLayout.CENTER);

		JButton btnMemoryClear = new JButton("MC");
		btnMemoryClear.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				memory = 0.0;
			}
		});
		GridBagConstraints gbc_btnMemoryClear = new GridBagConstraints();
		gbc_btnMemoryClear.fill = GridBagConstraints.BOTH;
		gbc_btnMemoryClear.insets = new Insets(0, 0, 5, 5);
		gbc_btnMemoryClear.gridx = 0;
		gbc_btnMemoryClear.gridy = 0;
		buttonPanel.add(btnMemoryClear, gbc_btnMemoryClear);

		JButton btnMemoryRecall = new JButton("MR");
		btnMemoryRecall.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				appendString(Calculator.toString(memory));
			}
		});
		GridBagConstraints gbc_btnMemoryRecall = new GridBagConstraints();
		gbc_btnMemoryRecall.fill = GridBagConstraints.BOTH;
		gbc_btnMemoryRecall.insets = new Insets(0, 0, 5, 5);
		gbc_btnMemoryRecall.gridx = 1;
		gbc_btnMemoryRecall.gridy = 0;
		buttonPanel.add(btnMemoryRecall, gbc_btnMemoryRecall);

		JButton btnMemoryStore = new JButton("MS");
		btnMemoryStore.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (!clear) {
					memory = getNumber();
				}
			}
		});
		GridBagConstraints gbc_btnMemoryStore = new GridBagConstraints();
		gbc_btnMemoryStore.fill = GridBagConstraints.BOTH;
		gbc_btnMemoryStore.insets = new Insets(0, 0, 5, 5);
		gbc_btnMemoryStore.gridx = 2;
		gbc_btnMemoryStore.gridy = 0;
		buttonPanel.add(btnMemoryStore, gbc_btnMemoryStore);

		JButton btnMemoryAdd = new JButton("M+");
		btnMemoryAdd.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (!clear) {
					memory += getNumber();
				}
			}
		});
		GridBagConstraints gbc_btnMemoryAdd = new GridBagConstraints();
		gbc_btnMemoryAdd.fill = GridBagConstraints.BOTH;
		gbc_btnMemoryAdd.insets = new Insets(0, 0, 5, 5);
		gbc_btnMemoryAdd.gridx = 3;
		gbc_btnMemoryAdd.gridy = 0;
		buttonPanel.add(btnMemoryAdd, gbc_btnMemoryAdd);

		JButton btnMemorySubtract = new JButton("M-");
		btnMemorySubtract.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (!clear) {
					memory -= getNumber();
				}
			}
		});
		GridBagConstraints gbc_btnMemorySubtract = new GridBagConstraints();
		gbc_btnMemorySubtract.fill = GridBagConstraints.BOTH;
		gbc_btnMemorySubtract.insets = new Insets(0, 0, 5, 0);
		gbc_btnMemorySubtract.gridx = 4;
		gbc_btnMemorySubtract.gridy = 0;
		buttonPanel.add(btnMemorySubtract, gbc_btnMemorySubtract);

		AbstractAction delete = new AbstractAction() {
			private static final long serialVersionUID = -8183669168259162081L;

			@Override
			public void actionPerformed(ActionEvent e) {
				if (!clear) {
					String text = expressionLabel.getText();
					if (!text.equals(" ")) {
						if (text.length() == 1) {
							text = " ";
						} else {
							text = text.substring(0, text.length() - 1);
						}
						expressionLabel.setText(text);
					}
				}
			}
		};

		JButton btnDelete = new JButton("\u2190");
		btnDelete.addActionListener(delete);
		GridBagConstraints gbc_btnDelete = new GridBagConstraints();
		gbc_btnDelete.fill = GridBagConstraints.BOTH;
		gbc_btnDelete.insets = new Insets(0, 0, 5, 5);
		gbc_btnDelete.gridx = 0;
		gbc_btnDelete.gridy = 1;
		buttonPanel.add(btnDelete, gbc_btnDelete);

		AbstractAction clear = new AbstractAction() {
			private static final long serialVersionUID = 1L;

			@Override
			public void actionPerformed(ActionEvent e) {
				expressionLabel.setText(" ");
				numberField.setText("0");
			}
		};

		JButton btnClear = new JButton("C");
		btnClear.addActionListener(clear);
		GridBagConstraints gbc_btnClear = new GridBagConstraints();
		gbc_btnClear.gridwidth = 2;
		gbc_btnClear.fill = GridBagConstraints.BOTH;
		gbc_btnClear.insets = new Insets(0, 0, 5, 5);
		gbc_btnClear.gridx = 1;
		gbc_btnClear.gridy = 1;
		buttonPanel.add(btnClear, gbc_btnClear);

		AbstractAction input = new AbstractAction() {
			private static final long serialVersionUID = 1967747596854578047L;

			@Override
			public void actionPerformed(ActionEvent e) {
				appendString(e.getActionCommand());
			}
		};

		JButton btnSeven = new JButton("7");
		btnSeven.addActionListener(input);
		GridBagConstraints gbc_btnSeven = new GridBagConstraints();
		gbc_btnSeven.fill = GridBagConstraints.BOTH;
		gbc_btnSeven.insets = new Insets(0, 0, 5, 5);
		gbc_btnSeven.gridx = 0;
		gbc_btnSeven.gridy = 2;
		buttonPanel.add(btnSeven, gbc_btnSeven);

		JButton btnEight = new JButton("8");
		btnEight.addActionListener(input);
		GridBagConstraints gbc_btnEight = new GridBagConstraints();
		gbc_btnEight.fill = GridBagConstraints.BOTH;
		gbc_btnEight.insets = new Insets(0, 0, 5, 5);
		gbc_btnEight.gridx = 1;
		gbc_btnEight.gridy = 2;
		buttonPanel.add(btnEight, gbc_btnEight);

		JButton btnNine = new JButton("9");
		btnNine.addActionListener(input);
		GridBagConstraints gbc_btnNine = new GridBagConstraints();
		gbc_btnNine.fill = GridBagConstraints.BOTH;
		gbc_btnNine.insets = new Insets(0, 0, 5, 5);
		gbc_btnNine.gridx = 2;
		gbc_btnNine.gridy = 2;
		buttonPanel.add(btnNine, gbc_btnNine);

		JButton btnFour = new JButton("4");
		btnFour.addActionListener(input);
		GridBagConstraints gbc_btnFour = new GridBagConstraints();
		gbc_btnFour.fill = GridBagConstraints.BOTH;
		gbc_btnFour.insets = new Insets(0, 0, 5, 5);
		gbc_btnFour.gridx = 0;
		gbc_btnFour.gridy = 3;
		buttonPanel.add(btnFour, gbc_btnFour);

		JButton btnFive = new JButton("5");
		btnFive.addActionListener(input);
		GridBagConstraints gbc_btnFive = new GridBagConstraints();
		gbc_btnFive.fill = GridBagConstraints.BOTH;
		gbc_btnFive.insets = new Insets(0, 0, 5, 5);
		gbc_btnFive.gridx = 1;
		gbc_btnFive.gridy = 3;
		buttonPanel.add(btnFive, gbc_btnFive);

		JButton btnSix = new JButton("6");
		btnSix.addActionListener(input);
		GridBagConstraints gbc_btnSix = new GridBagConstraints();
		gbc_btnSix.fill = GridBagConstraints.BOTH;
		gbc_btnSix.insets = new Insets(0, 0, 5, 5);
		gbc_btnSix.gridx = 2;
		gbc_btnSix.gridy = 3;
		buttonPanel.add(btnSix, gbc_btnSix);

		JButton btnOne = new JButton("1");
		btnOne.addActionListener(input);
		GridBagConstraints gbc_btnOne = new GridBagConstraints();
		gbc_btnOne.fill = GridBagConstraints.BOTH;
		gbc_btnOne.insets = new Insets(0, 0, 5, 5);
		gbc_btnOne.gridx = 0;
		gbc_btnOne.gridy = 4;
		buttonPanel.add(btnOne, gbc_btnOne);

		JButton btnTwo = new JButton("2");
		btnTwo.addActionListener(input);
		GridBagConstraints gbc_btnTwo = new GridBagConstraints();
		gbc_btnTwo.fill = GridBagConstraints.BOTH;
		gbc_btnTwo.insets = new Insets(0, 0, 5, 5);
		gbc_btnTwo.gridx = 1;
		gbc_btnTwo.gridy = 4;
		buttonPanel.add(btnTwo, gbc_btnTwo);

		JButton btnThree = new JButton("3");
		btnThree.addActionListener(input);
		GridBagConstraints gbc_btnThree = new GridBagConstraints();
		gbc_btnThree.fill = GridBagConstraints.BOTH;
		gbc_btnThree.insets = new Insets(0, 0, 5, 5);
		gbc_btnThree.gridx = 2;
		gbc_btnThree.gridy = 4;
		buttonPanel.add(btnThree, gbc_btnThree);

		JButton btnZero = new JButton("0");
		btnZero.addActionListener(input);
		GridBagConstraints gbc_btnZero = new GridBagConstraints();
		gbc_btnZero.gridwidth = 2;
		gbc_btnZero.fill = GridBagConstraints.BOTH;
		gbc_btnZero.insets = new Insets(0, 0, 0, 5);
		gbc_btnZero.gridx = 0;
		gbc_btnZero.gridy = 5;
		buttonPanel.add(btnZero, gbc_btnZero);

		JButton btnDecimalPoint = new JButton(".");
		btnDecimalPoint.addActionListener(input);
		GridBagConstraints gbc_btnDecimalPoint = new GridBagConstraints();
		gbc_btnDecimalPoint.insets = new Insets(0, 0, 0, 5);
		gbc_btnDecimalPoint.fill = GridBagConstraints.BOTH;
		gbc_btnDecimalPoint.gridx = 2;
		gbc_btnDecimalPoint.gridy = 5;
		buttonPanel.add(btnDecimalPoint, gbc_btnDecimalPoint);

		JButton btnOpenParenthesis = new JButton("(");
		btnOpenParenthesis.addActionListener(input);
		GridBagConstraints gbc_btnOpenParenthesis = new GridBagConstraints();
		gbc_btnOpenParenthesis.fill = GridBagConstraints.BOTH;
		gbc_btnOpenParenthesis.insets = new Insets(0, 0, 5, 5);
		gbc_btnOpenParenthesis.gridx = 3;
		gbc_btnOpenParenthesis.gridy = 1;
		buttonPanel.add(btnOpenParenthesis, gbc_btnOpenParenthesis);

		JButton btnCloseParenthesis = new JButton(")");
		btnCloseParenthesis.addActionListener(input);
		GridBagConstraints gbc_btnCloseParenthesis = new GridBagConstraints();
		gbc_btnCloseParenthesis.fill = GridBagConstraints.BOTH;
		gbc_btnCloseParenthesis.insets = new Insets(0, 0, 5, 0);
		gbc_btnCloseParenthesis.gridx = 4;
		gbc_btnCloseParenthesis.gridy = 1;
		buttonPanel.add(btnCloseParenthesis, gbc_btnCloseParenthesis);

		JButton btnDivide = new JButton("/");
		btnDivide.addActionListener(input);
		GridBagConstraints gbc_btnDivide = new GridBagConstraints();
		gbc_btnDivide.fill = GridBagConstraints.BOTH;
		gbc_btnDivide.insets = new Insets(0, 0, 5, 5);
		gbc_btnDivide.gridx = 3;
		gbc_btnDivide.gridy = 2;
		buttonPanel.add(btnDivide, gbc_btnDivide);

		JButton btnMultiply = new JButton("*");
		btnMultiply.addActionListener(input);
		GridBagConstraints gbc_btnMultiply = new GridBagConstraints();
		gbc_btnMultiply.fill = GridBagConstraints.BOTH;
		gbc_btnMultiply.insets = new Insets(0, 0, 5, 5);
		gbc_btnMultiply.gridx = 3;
		gbc_btnMultiply.gridy = 3;
		buttonPanel.add(btnMultiply, gbc_btnMultiply);

		JButton btnMinus = new JButton("-");
		btnMinus.addActionListener(input);
		GridBagConstraints gbc_btnMinus = new GridBagConstraints();
		gbc_btnMinus.fill = GridBagConstraints.BOTH;
		gbc_btnMinus.insets = new Insets(0, 0, 5, 5);
		gbc_btnMinus.gridx = 3;
		gbc_btnMinus.gridy = 4;
		buttonPanel.add(btnMinus, gbc_btnMinus);

		JButton btnPlus = new JButton("+");
		btnPlus.addActionListener(input);
		GridBagConstraints gbc_btnPlus = new GridBagConstraints();
		gbc_btnPlus.fill = GridBagConstraints.BOTH;
		gbc_btnPlus.insets = new Insets(0, 0, 0, 5);
		gbc_btnPlus.gridx = 3;
		gbc_btnPlus.gridy = 5;
		buttonPanel.add(btnPlus, gbc_btnPlus);

		JButton btnModulus = new JButton("%");
		btnModulus.addActionListener(input);
		GridBagConstraints gbc_btnModulus = new GridBagConstraints();
		gbc_btnModulus.fill = GridBagConstraints.BOTH;
		gbc_btnModulus.insets = new Insets(0, 0, 5, 0);
		gbc_btnModulus.gridx = 4;
		gbc_btnModulus.gridy = 2;
		buttonPanel.add(btnModulus, gbc_btnModulus);

		JButton btnExponent = new JButton("^");
		btnExponent.addActionListener(input);
		GridBagConstraints gbc_btnExponent = new GridBagConstraints();
		gbc_btnExponent.fill = GridBagConstraints.BOTH;
		gbc_btnExponent.insets = new Insets(0, 0, 5, 0);
		gbc_btnExponent.gridx = 4;
		gbc_btnExponent.gridy = 3;
		buttonPanel.add(btnExponent, gbc_btnExponent);

		AbstractAction evaluate = new AbstractAction() {
			private static final long serialVersionUID = 5485548937923320066L;

			@Override
			public void actionPerformed(ActionEvent e) {
				String text = expressionLabel.getText();
				if (!text.equals(" ")) {
					Number evaluation = null;
					try {
						evaluation = evaluate(text);
					} catch (Exception ex) {
					}
					if (evaluation != null) {
						double value = evaluation.getValue();
						if (value == Double.POSITIVE_INFINITY
								|| value == Double.NEGATIVE_INFINITY
								|| value == Double.NaN) {
							text = "Math Error";
							Calculator.this.clear = true;
						} else {
							text = Calculator.toString(value);
						}
					} else {
						text = "Syntax Error";
						Calculator.this.clear = true;
					}
					expressionLabel.setText(" ");
					numberField.setText(text);
				}
			}
		};

		JButton btnEquals = new JButton("=");
		btnEquals.addActionListener(evaluate);
		GridBagConstraints gbc_btnEquals = new GridBagConstraints();
		gbc_btnEquals.fill = GridBagConstraints.BOTH;
		gbc_btnEquals.gridheight = 2;
		gbc_btnEquals.gridx = 4;
		gbc_btnEquals.gridy = 4;
		buttonPanel.add(btnEquals, gbc_btnEquals);

		pack();
		setLocationRelativeTo(null);

		InputMap inputMap = contentPane
				.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
		ActionMap actionMap = contentPane.getActionMap();

		inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_0, 0), "0");
		actionMap.put("0", input);

		inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_1, 0), "1");
		actionMap.put("1", input);

		inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_2, 0), "2");
		actionMap.put("2", input);

		inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_3, 0), "3");
		actionMap.put("3", input);

		inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_4, 0), "4");
		actionMap.put("4", input);

		inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_5, 0), "5");
		actionMap.put("5", input);

		inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_6, 0), "6");
		actionMap.put("6", input);

		inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_7, 0), "7");
		actionMap.put("7", input);

		inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_8, 0), "8");
		actionMap.put("8", input);

		inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_9, 0), "9");
		actionMap.put("9", input);

		inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_PERIOD, 0), ".");
		actionMap.put(".", input);

		inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_EQUALS, 1), "+");
		actionMap.put("+", input);

		inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_MINUS, 0), "-");
		actionMap.put("-", input);

		inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_8, 1), "*");
		actionMap.put("*", input);

		inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_SLASH, 0), "/");
		actionMap.put("/", input);

		inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_5, 1), "%");
		actionMap.put("%", input);

		inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_6, 1), "^");
		actionMap.put("^", input);

		inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_9, 1), "(");
		actionMap.put("(", input);

		inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_0, 1), ")");
		actionMap.put(")", input);

		inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_BACK_SPACE, 0),
				"delete");
		actionMap.put("delete", delete);

		inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_DELETE, 0), "clear");
		actionMap.put("clear", clear);

		inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0), "evaluate");
		inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_EQUALS, 0), "evaluate");
		actionMap.put("evaluate", evaluate);
	}

	private void appendString(String s) {
		String text;
		if (clear) {
			text = s;
			numberField.setText("0");
			clear = false;
		} else {
			text = expressionLabel.getText();
			if (text.equals(" ")) {
				text = s;
			} else {
				text = text + s;
			}
		}
		expressionLabel.setText(text);
	}

	private static String toString(double d) {
		String s = Double.toString(d);
		if (s.endsWith(".0")) {
			return s.substring(0, s.length() - 2);
		}
		return s;
	}

	private double getNumber() {
		return Double.parseDouble(numberField.getText());
	}

	private Number evaluate(String expression) {
		List<Token> tokens = new ArrayList<Token>();
		Matcher m = PATTERN.matcher(expression);
		int lastIndex = 0;
		while (m.find()) {
			if (m.start() != lastIndex) {
				throw new SyntaxException();
			}
			Token token;
			String match;
			if ((match = m.group(1)) != null) {
				token = Number.parseNumber(match);
			} else if ((match = m.group(2)) != null) {
				token = Operation.parseOperator(match);
			} else if ((match = m.group(3)) != null) {
				token = Parenthesis.parseParenthesis(match);
			} else {
				throw new IllegalStateException();
			}
			tokens.add(token);
			lastIndex = m.end();
		}
		return evaluate(tokens);
	}

	private Number evaluate(List<Token> tokens) {
		int start = -1;
		int end = -1;
		int count = 0;
		for (int i = 0; i < tokens.size(); i++) {
			Token s = tokens.get(i);
			if (s.getType() == Token.Type.PARENTHESIS) {
				Parenthesis p = (Parenthesis) s;
				if (p.isOpen()) {
					if (start == -1) {
						start = i;
					} else {
						count++;
					}
				} else {
					if (count > 0) {
						count--;
					} else {
						end = i;
					}
				}
				if (start == -1 && end != -1) {
					throw new SyntaxException();
				} else if (start != -1 && end != -1) {
					int size = end - start - 1;
					if (size == 0) {
						throw new SyntaxException();
					}
					List<Token> inner = new ArrayList<Token>(size);
					for (int n = start; n <= end; n++) {
						Token temp = tokens.remove(start);
						if (n != start && n != end) {
							inner.add(temp);
						}
					}
					tokens.add(start, evaluate(inner));
					start = -1;
					end = -1;
					i = start;
				}
			}
		}
		if (start != -1 && end == -1) {
			throw new SyntaxException();
		}

		for (int i = 0; i < tokens.size(); i++) {
			if (tokens.get(i).getType() == Token.Type.NUMBER) {
				tokens.set(i, checkNumber(i, tokens));
			}
		}

		for (int p = Operation.HIGHEST_PRECEDENCE; p <= Operation.LOWEST_PRECEDENCE; p++) {
			switch (p) {
			case 0:
				for (int i = tokens.size() - 1; i >= 0; i--) {
					i = checkOperator(i, tokens, p);
				}
				break;
			case 1:
			case 2:
				for (int i = 0; i < tokens.size(); i++) {
					i = checkOperator(i, tokens, p);
				}
				break;
			}
		}

		return (Number) tokens.remove(0);
	}

	private Number checkNumber(int index, List<Token> symbols) {
		Number n = (Number) symbols.get(index);
		if (index < symbols.size() - 1) {
			int i = index + 1;
			if (symbols.get(i).getType() == Token.Type.NUMBER) {
				Number value = new Number(n.getValue()
						* checkNumber(i, symbols).getValue());
				symbols.remove(i);
				return value;
			}
		}
		return n;
	}

	private int checkOperator(int index, List<Token> symbols, int precedence) {
		Token s = symbols.get(index);
		if (s.getType() == Token.Type.OPERATION) {
			Operation o = (Operation) s;
			if (o.getPrecedence() == precedence) {
				Number n = o.evaluate(index, symbols);
				symbols.remove(index + 1);
				if (index == 0) {
					symbols.set(index, n);
				} else {
					symbols.remove(index);
					symbols.set(--index, n);
				}
			}
		}
		return index;
	}

}
