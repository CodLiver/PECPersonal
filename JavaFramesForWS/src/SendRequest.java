import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import org.json.JSONObject;

import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.awt.event.ActionEvent;


import org.json.JSONArray;
import org.json.JSONObject;

public class SendRequest extends JFrame {

	private JPanel contentPane;
	private JTextField textField;
	private JTextField textField_1;
	private JLabel lblNewLabel_2;

	/**
	 * Create the frame.
	 */
	public SendRequest(String name,String err) {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 512, 357);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JLabel lblNewLabel = new JLabel("Start Date");
		lblNewLabel.setBounds(53, 44, 74, 16);
		contentPane.add(lblNewLabel);
		
		JLabel lblNewLabel_1 = new JLabel("End Date");
		lblNewLabel_1.setBounds(53, 87, 56, 16);
		contentPane.add(lblNewLabel_1);
		
		JTextField textField_s = new JTextField("yyyy-MM-dd");
		textField_s.setBounds(153, 41, 116, 22);
		contentPane.add(textField_s);
		textField_s.setColumns(10);
		
		JTextField textField_e = new JTextField("yyyy-MM-dd");
		textField_e.setBounds(153, 87, 116, 22);
		contentPane.add(textField_e);
		textField_e.setColumns(10);
		
		JLabel lbl_error = new JLabel(err);
		lbl_error.setBounds(53, 154, 171, 16);
		contentPane.add(lbl_error);
		
		JButton btnNewButton = new JButton("Send Request");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				
				Boolean isErr=false;
				Date startDate;
				Date finDate;
				try {
						startDate = new SimpleDateFormat("yyyy-MM-dd").parse(textField_s.getText());
						finDate = new SimpleDateFormat("yyyy-MM-dd").parse(textField_e.getText());
				}catch(Exception e) {
				isErr=true;
				SendRequest frame = new SendRequest(name,"incorrect date");
				frame.setVisible(true);

				setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
				dispose();
				}
				
				
				
				try {
					StringBuilder result = new StringBuilder();
				      URL url = new URL("http://localhost:8180/HolidayBookingAppWebApplication/rest/con/post/request");//   HolidayAppRest
				      HttpURLConnection conn = (HttpURLConnection) url.openConnection();
				      conn.setRequestMethod("POST");
				      conn.setRequestProperty("Content-Type", "application/json; utf-8");
				      conn.setRequestProperty("Accept", "application/json");
				      conn.setDoOutput(true);
				      String jsonInputString = "{beginDate: "+textField_s.getText()+", endDate: "+textField_e.getText()+", username: "+name+"}";
				      
				      try(OutputStream os = conn.getOutputStream()) {
				    	    byte[] input = jsonInputString.getBytes("utf-8");
				    	    os.write(input, 0, input.length);           
				    	}
				      
				      try(BufferedReader br = new BufferedReader(
				    		  new InputStreamReader(conn.getInputStream(), "utf-8"))) {
				    		    StringBuilder response = new StringBuilder();
				    		    String responseLine = null;
				    		    while ((responseLine = br.readLine()) != null) {
				    		        response.append(responseLine.trim());
				    		    }
				    		    
				    		    String res = response.toString();
				    		    
				    		    System.out.println(res);
				    		    
				    		    JSONObject obj = new JSONObject(res);
				    		    
				    		    String errCode = (String) obj.get("error");
				    		    
				    		    System.out.println("errCode"+errCode);
				    		    
				    		    if(!errCode.equals("OK")) {
				    		    	
				    		    	System.out.println("NotOK:"+errCode);
				    				isErr=true;
				    				SendRequest frame = new SendRequest(name,errCode);
				    				frame.setVisible(true);

				    				setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
				    				dispose();				    		    	
				    		    }else {
				    		    	System.out.println("OK:"+errCode);
				    				isErr=true;
				    				EmployeePage frame1 = new EmployeePage(name);
				    				frame1.setVisible(true);

				    				setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
				    				dispose();
				    		    	
				    		    }
				    		    
				    		    
				    		    //change here
				    		    //Integer resultRes = Integer.valueOf(response.toString());
				    		    //System.out.println("res: "+resultRes);		    
				    }
				   
				}catch(Exception e) {
						e.printStackTrace();
				}
				
				
				
				
				
				
				
				/*if(!isErr) {
				SendRequest frame = new SendRequest(name,"click");
				frame.setVisible(true);

				setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
				dispose();
				}*/
				
				/*try {
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
				      result.toString();
				      System.out.println(result);
					}catch(Exception e) {
						e.printStackTrace();
					}*/
				
				
			}
		});
		btnNewButton.setBounds(298, 150, 116, 25);
		contentPane.add(btnNewButton);
	}
}
