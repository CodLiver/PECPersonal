import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

import org.json.JSONArray;
import org.json.JSONObject;

import javax.swing.JTable;
import javax.swing.JLabel;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.awt.event.ActionEvent;

public class EmployeePage extends JFrame {

	private JPanel contentPane;
	private JTable table;
	private JTable table_1;

	/**
	 * Create the frame.
	 */
	public EmployeePage(String name) {
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 687, 432);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		DefaultTableModel model = new DefaultTableModel();
        model.addColumn("Begin Date");
        model.addColumn("End Date");
        model.addColumn("Duration");
        model.addColumn("Holiday Remaining Time");
        model.addColumn("Peak Time");
        model.addColumn("Is Approved?");
        
		table_1 = new JTable(model);
		table_1.setBounds(108, 110, 478, 212);
		contentPane.add(table_1);
		
		try {
			StringBuilder result = new StringBuilder();
		      URL url = new URL("http://localhost:8180/HolidayBookingAppWebApplication/rest/con/user/"+name+"/requests");
		      HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		      conn.setRequestMethod("GET");
		      BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
		      String line;
		      while ((line = rd.readLine()) != null) {
		         result.append(line);
		      }
		      rd.close();
		      String res= result.toString();
		      System.out.println(result);
		      
		      
		      DefaultTableModel model1 = (DefaultTableModel) table_1.getModel();
		      model1.addRow(new Object[]{"Begin Date", "End Date", "Duration","Holiday Remaining Time","Peak Time","Is Approved?"});
		      
		      JSONObject obj = new JSONObject(res);
		      
		      //obj = (JSONObject) obj.get("data");
		      
		      //List<Object> lols = (List<Object>) obj.get("data");
		      
		      JSONArray data = obj.getJSONArray("data");
		      
		      //JSONArray firstPieceOfData = (JSONArray) data.g63t(0);
		      //Object firstRate = firstPieceOfData.get(1);
		      
 		      for (int x=0;x<data.length();x++) {
 		    	 //List<Object> l = (List<Object>) o;
 		    	 JSONArray inData = (JSONArray) data.get(x);
 		    	model1.addRow(new Object[]{inData.get(0), inData.get(1), inData.get(2),inData.get(3),inData.get(4),inData.get(5)});

		    	 // System.out.println(result);
		      }
		      
		      
			}catch(Exception e) {
				e.printStackTrace();
			}
		
		table_1.revalidate();
		
		JLabel lblSentReq = new JLabel("Sent Requests");
		lblSentReq.setBounds(105, 44, 133, 16);
		contentPane.add(lblSentReq);
		
		
		
		JButton btnNewReq = new JButton("Submit new Request");
		btnNewReq.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				SendRequest frame = new SendRequest(name,"");
				frame.setVisible(true);

				setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
				dispose();
			}
		});
		
		btnNewReq.setBounds(388, 40, 212, 25);
		contentPane.add(btnNewReq);
		
		JLabel lblNewLabel = new JLabel("Hello "+name);
		lblNewLabel.setBounds(102, 15, 56, 16);
		contentPane.add(lblNewLabel);
		
	}
}
