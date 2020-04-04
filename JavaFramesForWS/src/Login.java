import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.xml.ws.Response;

public class Login extends JFrame {

	private JPanel contentPane;
	private JTextField textField;
	private JTextField textField_1;
	private JLabel lblNewLabel;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Login frame = new Login();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public Login() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		
		JTextField tf_username = new JTextField();
		tf_username.setBounds(153, 73, 116, 22);
		contentPane.add(tf_username);
		tf_username.setColumns(10);
		
		JTextField tf_password = new JTextField();
		tf_password.setBounds(153, 128, 116, 22);
		contentPane.add(tf_password);
		tf_password.setColumns(10);
		
		JLabel label_error = new JLabel("");
		label_error.setBounds(69, 185, 200, 16);
		contentPane.add(label_error);		
		
		JButton button_login = new JButton("Login");
		button_login.setBounds(292, 181, 95, 25);
		button_login.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				
			try {
				StringBuilder result = new StringBuilder();
			      URL url = new URL("http://localhost:8180/HolidayBookingAppWebApplication/rest/con/login");//   HolidayAppRest
			      HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			      conn.setRequestMethod("POST");
			      conn.setRequestProperty("Content-Type", "application/json; utf-8");
			      conn.setRequestProperty("Accept", "application/json");
			      conn.setDoOutput(true);
			      String jsonInputString = "{email: "+tf_username.getText()+", password: "+tf_password.getText()+"}";
			      
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
			    		    
			    		    Integer resultRes = Integer.valueOf(response.toString());
			    		    System.out.println("res: "+resultRes);
			    		    
			    		    if (resultRes==2) {
			    		    	
			    				EmployeePage frame = new EmployeePage(tf_username.getText());
			    				frame.setVisible(true);

			    				setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
			    				dispose();
			    				
			    				
			    		    }else {
			    		    	
			    		    	label_error.setText("Invalid password or not employee login info.");
			    		    }
			    		    
			    		    
			    }
			   
				}catch(Exception e) {
					e.printStackTrace();
				}
				
				
				
				/*working get request
				 * 
				 * try {
				StringBuilder result = new StringBuilder();
			      URL url = new URL("http://localhost:8180/HolidayAppRest/rest/con/hi");
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
		contentPane.setLayout(null);
		contentPane.add(button_login);
		
		JLabel label_username = new JLabel("Username:");
		label_username.setBounds(69, 76, 72, 16);
		contentPane.add(label_username);
		
		JLabel label_password = new JLabel("Password:");
		label_password.setBounds(69, 131, 72, 16);
		contentPane.add(label_password);
	
	}
}
