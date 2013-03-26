import junit.framework.Assert;
import org.junit.Test;


public class MessageQueueTest {

	@Test
	public void testRemove() {

		MessageQueue newMessages = new MessageQueue();
		
		Message msg0 = new Message("MSG0");
		Message msg1 = new Message("MSG1");
		Message msg2 = new Message("MSG2");
		
		System.out.println("--Adding 3 messages to message queue--");
		
		newMessages.add(msg0);
		newMessages.add(msg1);
		newMessages.add(msg2);
				
		System.out.println("--Remove 2 head elements--");
		newMessages.remove();
		newMessages.remove();
		
		Assert.assertEquals(newMessages.getStr(0), "MSG" + 2);
		
		System.out.println("--testRemove passed--\n");
		
	}

	@Test
	public void testAdd() {

		MessageQueue newMessages = new MessageQueue();
		
		Message msg0 = new Message("MSG0");
		Message msg1 = new Message("MSG1");
		Message msg2 = new Message("MSG2");
		
		System.out.println("--Adding 3 messages to message queue--");
		
		newMessages.add(msg0);
		newMessages.add(msg1);
		newMessages.add(msg2);
		
		for (Integer c = 0; c < 3; c++)
		{
			Assert.assertEquals(newMessages.getStr(c),"MSG" + c);
		}
		
		System.out.println("--testAdd passed--\n");
	}

	@Test
	public void testSize() {
		
		MessageQueue newMessages = new MessageQueue();
		
		Message msg0 = new Message("MSG0");
		System.out.println("--Adding 1 messages to message queue--");
		newMessages.add(msg0);
				
		System.out.println("--Remove head element--");
		newMessages.remove();
		
		Assert.assertEquals(newMessages.size(), 0);
		System.out.println("--testSize passed--\n");
		
	}

	@Test
	public void testPeek() {

		MessageQueue newMessages = new MessageQueue();
		
		Message msg0 = new Message("MSG0");
		Message msg1 = new Message("MSG1");
		Message msg2 = new Message("MSG2");
		
		System.out.println("--Adding 3 messages to message queue--");
		
		newMessages.add(msg0);
		newMessages.add(msg1);
		newMessages.add(msg2);
		
		newMessages.remove();
		newMessages.remove();

		Assert.assertEquals(newMessages.peek().getText(),"MSG" + 2);
		
		System.out.println("--testPeek passed--\n");

	}
	
	@Test
	public void testDelete() {

		MessageQueue newMessages = new MessageQueue();
		
		Message msg0 = new Message("MSG0");
		Message msg1 = new Message("MSG1");
		Message msg2 = new Message("MSG2");
		
		System.out.println("--Adding 3 messages to message queue--");
		
		newMessages.add(msg0);
		newMessages.add(msg1);
		newMessages.add(msg2);
				
		System.out.println("--Remove 2 head elements--");
		newMessages.delete(msg0);
		newMessages.delete(msg1);
		
		Assert.assertEquals(newMessages.peek().getText(), "MSG" + 2);
		
		System.out.println("--testDelete passed--\n");

	}


}
