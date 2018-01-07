package es.ull.cc.main;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;
import org.graphstream.graph.implementations.SingleGraph;

import javax.swing.JTextArea;
import javax.swing.JButton;

/**
 * Graphic Interface for 3SAT to VC Transformation
 * @author Pedro Miguel Lagüera Cabrera
 * Dec 27, 2017
 * VC_GUI.java
 */
@SuppressWarnings("serial")
public class VC_GUI extends JFrame {

	private JPanel contentPane;
	private String clause = "[(]\\s*[!]?\\w\\d\\s*\\^\\s*[!]?\\w\\d\\s*\\^\\s*[!]?\\w\\d\\s*[)]";
	private String element = "[!]?\\w\\d";

	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					VC_GUI frame = new VC_GUI();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	public VC_GUI() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);

		JTextArea textArea = new JTextArea();
		textArea.setText("(!X1 ^ X2 ^ Y1) ^ (X2 ^!X1 ^ Y1) ^ (!X2 ^ X1 ^ Y2)"); 
		contentPane.add(textArea, BorderLayout.CENTER);

		JPanel panel = new JPanel();
		contentPane.add(panel, BorderLayout.SOUTH);

		JButton btnTransform = new JButton("Transform");
		panel.add(btnTransform);

		btnTransform.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Pattern pClause = Pattern.compile(clause);
				Matcher mClause = pClause.matcher(textArea.getText());
				List<Clause> clauses = new ArrayList<Clause>();
				while (mClause.find()) {
					String found = mClause.group();
					Pattern pElem = Pattern.compile(element);
					Matcher mElem = pElem.matcher(found);
					
					if(!mElem.find())
						continue;
					Component comp1 = new Component(mElem.group());
					if(!mElem.find())
						continue;
					Component comp2 = new Component(mElem.group());
					if(!mElem.find())
						continue;
					Component comp3 = new Component(mElem.group());
					clauses.add(new Clause(comp1, comp2, comp3));
				}
				
				if(clauses.size() != 3) {
					JOptionPane.showMessageDialog(panel, "Invalid Expression !!", "Error", JOptionPane.ERROR_MESSAGE);
					return;
				}
				
				List<String> TSC = getTruthSettingComp(clauses);
				Graph graph = new SingleGraph("Vertex Cover");
				int separation = 10;
				for(String i : TSC) {
					String node1 = i;
					String node2 = "!" + i;
					
					graph.addNode(node1);
					graph.addNode(node2);
					
					Node n1 = graph.getNode(node1);
					Node n2 = graph.getNode(node2);
					
					n1.addAttribute("layout.frozen");
					n1.addAttribute("xy", separation, 50);
					n1.addAttribute("ui.label", n1.getId());
					separation += 10;
					n2.addAttribute("layout.frozen");
					n2.addAttribute("xy", separation, 50);
					n2.addAttribute("ui.label", n2.getId());
					separation += 10;
					
					graph.addEdge(i + " TS" , i, "!" + i);
				}
				
				int spacing = ((TSC.size() * 2 - 1) * 10 - 10) / 2;
				int node1Sep = 10, node2Sep = 15, node3Sep = 20, count = 1;
				for(Clause clause : clauses) {
					String node1 = "a1[" + count + "]";
					String node2 = "a2[" + count + "]";
					String node3 = "a3[" + count + "]";
					graph.addNode(node1);
					graph.addNode(node2);
					graph.addNode(node3);
					Node n1 = graph.getNode(node1);
					Node n2 = graph.getNode(node2);
					Node n3 = graph.getNode(node3);
					n1.addAttribute("ui.label", clause.getComponents()[0].getValue());
					n2.addAttribute("ui.label", clause.getComponents()[1].getValue());
					n3.addAttribute("ui.label", clause.getComponents()[2].getValue());
					n1.addAttribute("layout.frozen");
					n2.addAttribute("layout.frozen");
					n3.addAttribute("layout.frozen");
					n1.addAttribute("xy", node1Sep, 5);
					n2.addAttribute("xy", node2Sep, 15);
					n3.addAttribute("xy", node3Sep, 5);
					
					node1Sep += spacing;
					node2Sep += spacing;
					node3Sep += spacing;
					
					graph.addEdge(clause.getComponents()[0].getValue() + " ST " + count, node1, clause.getComponents()[0].getValue());
					graph.addEdge(clause.getComponents()[1].getValue() + " ST " + count , node2, clause.getComponents()[1].getValue());
					graph.addEdge(clause.getComponents()[2].getValue() + " ST " + count , node3, clause.getComponents()[2].getValue());
					
					graph.addEdge(node1 + " ST" , node1, node2);
					graph.addEdge(node2 + " ST" , node2, node3);
					graph.addEdge(node3 + " ST" , node1, node3);
					
					count++;
				}
				graph.display();
			}
		});
		
	}
	
	private static List<String> getTruthSettingComp(List<Clause> clauses) {
		List<String> aux = new ArrayList<String>();
		for(Clause clause : clauses) {
			for(Component component : clause.getComponents()) {
				String value = component.isNegation() ? component.complementary() : component.getValue();
				if(!aux.contains(value))
					aux.add(value);
			}
		}
		return aux;
	}

}
