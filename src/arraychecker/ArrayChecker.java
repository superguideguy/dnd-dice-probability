/*********************************************************************
* Copyright (c) 2019 superguideguy
*
* This program and the accompanying materials are made
* available under the terms of the Eclipse Public License 2.0
* which is available at https://www.eclipse.org/legal/epl-2.0/
*
* SPDX-License-Identifier: EPL-2.0
**********************************************************************/
package arraychecker;

import javax.swing.JFrame;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.Box;
import javax.swing.JRadioButton;
import javax.swing.ButtonGroup;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.JSeparator;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.SwingWorker;
import javax.swing.JButton;
import javax.swing.JProgressBar;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;

import javax.swing.JTextPane;

public class ArrayChecker implements ActionListener, Runnable{
	private static final ButtonGroup buttonGroup = new ButtonGroup();
	private static JTextField textFieldA;
	private static JTextField textFieldX;
	private static JTextField textFieldB;
	private static JTextField textFieldC;
	private static JTextField textFieldN;
	private static JTextField textFieldList;
	
	private ArrayCheckerState previousState = null;
	private Boolean b = null;
	JRadioButton rdbtnModeR;
	JFrame frame;
	JButton btnCalculate;
	static JProgressBar progressBarOuter;
	static JProgressBar progressBarInner;
	private static int linesTotal;
	static int linesProcessedOuter, linesProcessedInner;
	LinkedHashMap<Integer[],BigInteger> lhm;
	
	private static String alpha = "Percent of Arrays with Equal or Better Total: ",
			beta = "Percent of Arrays that are Strictly Better: ",
			delta = "5e Point Buy Cost: ",
			epsilon = "Pathfinder Point Buy Cost: ";
	private static JLabel lblBetterTotal,lblStrictlyBetter,lbl5ePointBuy,lblPathfinderPointBuy;

	/**
	 * @wbp.parser.entryPoint
	 */
	public void run() {
		
		frame = new JFrame("D&D Ability Score Array Probability Checker");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		Box horizontalBox = Box.createHorizontalBox();
		
		JSeparator separator = new JSeparator();
		
		Box horizontalBox_1 = Box.createHorizontalBox();
		
		btnCalculate = new JButton("Calculate");
		
		progressBarOuter = new JProgressBar();
		
		progressBarInner = new JProgressBar();
		
		JSeparator separator_1 = new JSeparator();
		
		Box verticalBox = Box.createVerticalBox();
		
		JTextPane progressMonitor = new JTextPane();
		GroupLayout groupLayout = new GroupLayout(frame.getContentPane());
		groupLayout.setHorizontalGroup(
			groupLayout.createParallelGroup(Alignment.TRAILING)
				.addGroup(groupLayout.createSequentialGroup()
					.addContainerGap()
					.addGroup(groupLayout.createParallelGroup(Alignment.TRAILING)
						.addComponent(progressBarInner, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 426, Short.MAX_VALUE)
						.addComponent(horizontalBox_1, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 426, Short.MAX_VALUE)
						.addComponent(separator, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 426, Short.MAX_VALUE)
						.addComponent(horizontalBox, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 426, Short.MAX_VALUE)
						.addComponent(btnCalculate, GroupLayout.DEFAULT_SIZE, 426, Short.MAX_VALUE)
						.addComponent(progressBarOuter, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 426, Short.MAX_VALUE)
						.addComponent(separator_1, GroupLayout.DEFAULT_SIZE, 426, Short.MAX_VALUE)
						.addComponent(progressMonitor, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 426, Short.MAX_VALUE)
						.addComponent(verticalBox, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 426, Short.MAX_VALUE))
					.addContainerGap())
		);
		groupLayout.setVerticalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addContainerGap()
					.addComponent(horizontalBox, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addComponent(separator, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(horizontalBox_1, GroupLayout.PREFERRED_SIZE, 21, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(btnCalculate)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(progressBarOuter, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(progressBarInner, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(progressMonitor, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addComponent(separator_1, GroupLayout.PREFERRED_SIZE, 9, GroupLayout.PREFERRED_SIZE)
					.addGap(1)
					.addComponent(verticalBox, GroupLayout.DEFAULT_SIZE, 86, Short.MAX_VALUE)
					.addContainerGap())
		);
		
		lblBetterTotal = new JLabel(alpha);
		verticalBox.add(lblBetterTotal);
		
		lblStrictlyBetter = new JLabel(beta);
		verticalBox.add(lblStrictlyBetter);
		
		Component verticalGlue = Box.createVerticalGlue();
		verticalBox.add(verticalGlue);
		
		lbl5ePointBuy = new JLabel(delta);
		verticalBox.add(lbl5ePointBuy);
		
		lblPathfinderPointBuy = new JLabel(epsilon);
		verticalBox.add(lblPathfinderPointBuy);
		
		JLabel lblListOfAbility = new JLabel("List of Ability Scores:");
		horizontalBox_1.add(lblListOfAbility);
		
		textFieldList = new JTextField();
		horizontalBox_1.add(textFieldList);
		textFieldList.setColumns(10);
		
		JRadioButton rdbtnModeD = new JRadioButton("Deterministic");
		buttonGroup.add(rdbtnModeD);
		horizontalBox.add(rdbtnModeD);
		
		rdbtnModeR = new JRadioButton("Random");
		buttonGroup.add(rdbtnModeR);
		horizontalBox.add(rdbtnModeR);
		
		textFieldA = new JTextField();
		textFieldA.setText("4");
		horizontalBox.add(textFieldA);
		textFieldA.setColumns(10);
		
		JLabel lblD = new JLabel("d");
		horizontalBox.add(lblD);
		
		textFieldX = new JTextField();
		textFieldX.setText("6");
		horizontalBox.add(textFieldX);
		textFieldX.setColumns(10);
		
		JLabel lblK = new JLabel("k");
		horizontalBox.add(lblK);
		
		textFieldB = new JTextField();
		textFieldB.setText("3");
		horizontalBox.add(textFieldB);
		textFieldB.setColumns(10);
		
		JLabel label = new JLabel("+");
		horizontalBox.add(label);
		
		textFieldC = new JTextField();
		textFieldC.setText("0");
		horizontalBox.add(textFieldC);
		textFieldC.setColumns(10);
		
		JLabel lblNumberOfAbilities = new JLabel("Number of Abilities:");
		horizontalBox.add(lblNumberOfAbilities);
		
		textFieldN = new JTextField();
		textFieldN.setText("6");
		horizontalBox.add(textFieldN);
		textFieldN.setColumns(10);
		frame.getContentPane().setLayout(groupLayout);
		
		frame.pack();
		rdbtnModeD.setSelected(true);
		rdbtnModeR.setEnabled(false);
		frame.getRootPane().setDefaultButton(btnCalculate);
		btnCalculate.setMnemonic(KeyEvent.VK_C);
		btnCalculate.setActionCommand("calculate");
		btnCalculate.addActionListener(this);
		frame.setVisible(true);
		
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if ("calculate".equalsIgnoreCase(e.getActionCommand())) {
			btnCalculate.setEnabled(false);
			RadioButtonMode r = rdbtnModeR.isSelected()
					? RadioButtonMode.RANDOM : RadioButtonMode.DETERMINISTIC;
			String[] db, list;
			String n;
			try {
				String[] dbX = {textFieldA.getText(),textFieldX.getText(),textFieldB.getText(),textFieldC.getText()};
				n = textFieldN.getText();
				list = textFieldList.getText().split(", *");
				db = dbX.clone();
			} catch (NullPointerException e1) {
				JOptionPane.showMessageDialog(null, "All text fields must be filled. "
						+ "Text fields may only contain digits, commas, and spaces; no letters,"
						+ " symbols, or whitespace other than spaces.\n"
						+ "In addition, commas are only allowed immmediately following a number.", 
						"Error: NullPointerException", JOptionPane.ERROR_MESSAGE);
				btnCalculate.setEnabled(true);return;
			}
			ArrayCheckerState currentState = new ArrayCheckerState(r,db,n,list);
			b = currentState.isValidAndEqual(previousState);
			if (b == null) {
				JOptionPane.showMessageDialog(null, "All text fields must be filled. "
						+ "Text fields may only contain digits, commas, and spaces; no letters,"
						+ " symbols, or whitespace other than spaces.\n"
						+ "In addition, commas are only allowed immmediately following a number. "
						+ "In \"AdXkB+C\", A cannot be less than B.", 
						"Error: NullPointerException", JOptionPane.WARNING_MESSAGE);
				btnCalculate.setEnabled(true);return;
			}
			previousState = currentState;
			TaskWorker task = new TaskWorker();
			task.execute();
		}
	}
	
	static void updateProgressBars() {
		progressBarOuter.setValue(linesProcessedOuter);
		progressBarInner.setValue(linesProcessedInner);
	}
	
	class TaskWorker extends SwingWorker<Void,Void> {

		protected Void doInBackground() throws Exception {
			int n = previousState.getN();
			int[] list = previousState.getList();
			int[] db = previousState.getDB();
			RadioButtonMode r = previousState.getR();
			if ((r.equals(RadioButtonMode.DETERMINISTIC)) && (b == false)) newDet(db,n,list);
			if ((r.equals(RadioButtonMode.DETERMINISTIC)) && (b == true)) oldDet(list);
			btnCalculate.setEnabled(true);
			return null;
		}
		
		protected void newDet(int[] db, int n, int[] list) {
			linesTotal = (int) MathFunctions.capacityCalc(n, db[2]+db[3], db[1]*db[2]+db[3]);
			progressBarOuter.setMaximum(linesTotal*3+4);
			progressBarInner.setMaximum(linesTotal);
			linesProcessedInner = 0;
			linesProcessedOuter = 0;
			progressBarOuter.setIndeterminate(true);
			progressBarOuter.setIndeterminate(true);
			try {
	            Thread.sleep(1);
	        } catch (InterruptedException ignore) {}
			
			ArrayDeterministicGenerator.pdfGenerator(db);
			progressBarOuter.setIndeterminate(false);
			progressBarInner.setIndeterminate(false);
			
			
			lhm = ArrayDeterministicGenerator.arrayProbability(n, db[2]+db[3], db[1]*db[2]+db[3]);
			linesProcessedInner = 0;
			linesProcessedOuter = linesTotal;
			arrayStatCalc(lhm,list);
		}
		
		protected void oldDet(int[] list) {
			progressBarOuter.setMaximum(linesTotal*3+3);
			progressBarInner.setMaximum(linesTotal);
			linesProcessedInner = 0;
			linesProcessedOuter = linesTotal;
			arrayStatCalc(lhm,list);
		}
		
		protected void arrayStatCalc(LinkedHashMap<Integer[],BigInteger> lhm, int[] list) {
			ArrayChecker.updateProgressBars(); 
			if (!previousState.isListValid()) {
				JOptionPane.showMessageDialog(null, "Either the number of elements in "
						+ "the list does not match the number of abilities, or at least "
						+ "one entry in the list is out of bounds.", 
						"Error: IllegalStateException",JOptionPane.WARNING_MESSAGE);
				btnCalculate.setEnabled(true);return;
			}
			
			Arrays.sort(list);
			Integer[] list2 = Arrays.stream(list).boxed().toArray(Integer[]::new);
			progressBarInner.setIndeterminate(true);
			updateProgressBars();
			ArrayList<Integer[]> keys = new ArrayList<Integer[]>(lhm.keySet());
			Integer[] list3 = null;
			int iX = 0;
			for (int i = 0; i < keys.size(); i++) if (Arrays.deepEquals(list2, keys.get(i))){
				list3 = keys.get(i);
				//System.out.println(list3.equals(keys.get(i)));
				iX = i;
				break;
			}
			BigInteger valueOfArray = lhm.get(list3); //Array stored as reference, not deep
			progressBarInner.setIndeterminate(false);
			updateProgressBars();
			//System.err.println(valueOfArray.toString());
			BigInteger totalValueOfMap = new BigInteger(Integer.toString(previousState.getDB()[1]));
			totalValueOfMap = totalValueOfMap.pow(previousState.getDB()[0]*previousState.getN());
			
			BigInteger betterThan = BigInteger.ZERO;
			int total = 0;
			for (int i = 0; i < list.length; i++) total += list[i];
			for (int i = 0; i < lhm.size(); i++, linesProcessedOuter++, linesProcessedInner++, updateProgressBars()) {
				int totalCompare = 0;
				Integer[] toCompare = keys.get(i);
				for (int j = 0; j < toCompare.length; j++) totalCompare += toCompare[j];
				if (totalCompare >= total) betterThan = betterThan.add(lhm.get(toCompare));
			}
			linesProcessedInner = 0;
			linesProcessedOuter = 2*linesTotal;
			betterThan = MathFunctions.subtract(betterThan, valueOfArray);
			
			BigInteger strictlyBetter = BigInteger.ZERO;
			outerfor: for (int i = 0; i < lhm.size(); i++, linesProcessedOuter++, linesProcessedInner++, updateProgressBars()) {
				if (i < iX) continue;
				Integer[] toCompare = keys.get(i);
				for (int j = 0; j < toCompare.length; j++)
					if (list3[j] > toCompare[j]) continue outerfor;
				strictlyBetter = strictlyBetter.add(lhm.get(toCompare));
			}
			strictlyBetter = MathFunctions.subtract(strictlyBetter, valueOfArray);
			linesProcessedInner = 0;
			linesProcessedOuter = progressBarOuter.getMaximum()-3;
			updateProgressBars();
			progressBarInner.setMaximum(3);
			updateProgressBars();

			String deltaX = "";
			int deltaY = 0;
			try {
				for (int i = 0; i < list.length; i++) 
					deltaY += MathFunctions.costTable5e[list[i]];
				deltaX = Integer.toString(deltaY);
			} catch (ArrayIndexOutOfBoundsException e) {
				deltaX = "N/A";
			}
			linesProcessedInner++;
			linesProcessedOuter++;
			updateProgressBars();
			
			String epsilonX = "";
			int epsilonY = 0;
			try {
				for (int i = 0; i < list.length; i++)
					epsilonY += MathFunctions.costTablePF[list[i]];
				epsilonX = Integer.toString(epsilonY);
			} catch (ArrayIndexOutOfBoundsException e) {
				epsilonX = "N/A";
			}
			linesProcessedInner++;
			linesProcessedOuter++;
			updateProgressBars();
			
			double alphaX = betterThan.multiply(BigInteger.TEN.pow(8)).divide(totalValueOfMap).doubleValue()/Math.pow(10, 6);
			double betaX = strictlyBetter.multiply(BigInteger.TEN.pow(8)).divide(totalValueOfMap).doubleValue()/Math.pow(10, 6);
			
			lblBetterTotal.setText("" + alpha + "" + alphaX);
			lblStrictlyBetter.setText("" + beta + "" + betaX);
			lbl5ePointBuy.setText("" + delta + "" + deltaX);
			lblPathfinderPointBuy.setText("" + epsilon + "" + epsilonX);
			linesProcessedInner++;
			linesProcessedOuter++;
			updateProgressBars();
		}
		
	}
}
