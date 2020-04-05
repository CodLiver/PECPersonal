package ejbholidaybookingapp;

import javax.ejb.ActivationConfigProperty;
import javax.ejb.EJB;
import javax.ejb.MessageDriven;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.QueueConnection;
import javax.jms.QueueConnectionFactory;
import javax.jms.TextMessage;
import javax.jms.TopicSubscriber;
import javax.jms.*;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import ejbholidaybookingapp.JmsAlertDTO;
import ejbholidaybookingapp.HolidayBookingAppBeanRemote;

/**
 * Message-Driven Bean implementation class for: RequestAlert
 */
@MessageDriven(
		activationConfig = { 
		        @ActivationConfigProperty(
		        propertyName = "destinationLookup", propertyValue = "queue/RequestDeliveryPoint"),
		        
				@ActivationConfigProperty(
				propertyName = "destination", propertyValue = "RequestDeliveryPoint"), 
				
				@ActivationConfigProperty(
				propertyName = "destinationType", propertyValue = "javax.jms.Queue")
		}, 
		mappedName = "RequestDeliveryPoint")
public class RequestAlert implements MessageListener {

	@EJB
	private HolidayBookingAppBeanRemote holidayBookingAppBean;
	
    /**
     * Default constructor. 
     */
    public RequestAlert() {
        // TODO Auto-generated constructor stub
    }
	
	/**
     * @see MessageListener#onMessage(Message)
     */
    public void onMessage(Message message) {
        // TODO Auto-generated method stub
        TextMessage m=(TextMessage)message;  
        try{  
        	System.out.println("message received: "+m.getText());  
        	
        	String gotText = m.getText();
        	
        	JmsAlertDTO newJmsAlert = new JmsAlertDTO(0, gotText);
        	
        	
        	System.out.println("jmsalerdto: ");
        	System.out.println(newJmsAlert.getId()); 
        	System.out.println(newJmsAlert.getReqname()); 
        	
        	holidayBookingAppBean.addNewJmsAlert(newJmsAlert);
        	
        }catch(Exception e){
        	System.out.println(e);
        }  
    }
    
    

}
