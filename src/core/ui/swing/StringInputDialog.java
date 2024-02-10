package core.ui.swing;

import java.awt.BorderLayout;
import java.awt.Dialog.ModalityType;
import java.awt.FlowLayout;
import java.awt.Window;
import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.BevelBorder;

import online.money_daisuki.api.base.Requires;
import online.money_daisuki.api.gui.swing.layouts.InputLayout;

public final class StringInputDialog {
	private final JDialog dialog;
	
	private final JPanel contentPanel;
	private final JPanel southPanel;
	
	private final JTextField[] fields;
	
	private String[] output;
	
	public StringInputDialog(final Window parent, final String title, final String[] messages) {
		this(parent, title, messages, 15);
	}
	public StringInputDialog(final Window parent, final String title, final String[] messages, final int fieldLen) {
		this(parent, title, messages, 15, null);
	}
	public StringInputDialog(final Window parent, final String title, final String[] messages, final int fieldLen,
			final String[] values) {
		Requires.notNull(messages, "messages == null");
		Requires.containsNotNull(messages, "messages contains null");
		Requires.greaterThanZero(fieldLen, "fieldLen <= 0");
		
		if(values != null) {
			Requires.equal(messages.length, values.length, "values must either be null or have the same len as messages");
		}
		
		
		dialog = new JDialog(parent, title, ModalityType.APPLICATION_MODAL);
		dialog.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		dialog.setResizable(false);
		
		
		contentPanel = new JPanel(new InputLayout(true, false));
		contentPanel.setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED));
		
		final int count = messages.length;
		fields = new JTextField[count];
		for(int i = 0; i < count; i++) {
			contentPanel.add(new JLabel(messages[i]), InputLayout.LEFT);
			
			final JTextField field = new JTextField(values != null ? values[i] : "", fieldLen);
			contentPanel.add(field, InputLayout.RIGHT);
			fields[i] = field;
		}
		dialog.add(contentPanel, BorderLayout.CENTER);
		
		
		southPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
		southPanel.add(new JButton(new AbstractAction("OK") {
			@Override
			public void actionPerformed(final ActionEvent e) {
				final int size = fields.length;
				output = new String[size];
				for(int i = 0; i < size; i++) {
					final JTextField field = fields[i];
					output[i] = field.getText();
					field.setText(values != null ? values[i] : "");
				}
				dialog.dispose();
			}
		}));
		dialog.add(southPanel, BorderLayout.SOUTH);
		
		dialog.pack();
	}
	public String[] show() {
		dialog.setLocationRelativeTo(dialog.getOwner());
		dialog.setVisible(true);
		
		final String[] out = output;
		output = null;
		return(out);
	}
}
