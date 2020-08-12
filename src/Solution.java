import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class Solution
{
		private static final List<Integer> randoms = new ArrayList<>();

		private Boolean transfer = true;

		private ReentrantLock reentrantLock = new ReentrantLock();

		private Condition condition = reentrantLock.newCondition();

		public static void main(String[] args) throws InterruptedException
		{
				while(true)
				{
						Solution solution = new Solution();
						solution.getConsumer().setDaemon(true);
						solution.getProducer().setDaemon(true);
						solution.getProducer().start();
						solution.getConsumer().start();
						Thread.sleep(1000);
				}
		}

		private final Thread producer = new Thread(() ->
		{
				reentrantLock.lock();
				try
				{
						while(!transfer)
						{
								condition.await();
						}
						transfer = false;
						for(int i = 0; i < new Random().nextInt(100); i++)
						{
								int number = new Random().nextInt(100);
								randoms.add(number);
								System.out.println(number);
						}
						System.out.println("wrote");
						condition.signalAll();
				}
				catch(InterruptedException e)
				{
						e.printStackTrace();
				}
				finally
				{
						reentrantLock.unlock();
				}
		});

		public Thread getProducer()
		{
				return producer;
		}

		private final Thread consumer = new Thread(() -> {
				reentrantLock.lock();
				try
				{
						while(transfer)
						{
								condition.await();
						}
						transfer = true;
						randoms.forEach(System.out::println);
						randoms.clear();
						System.out.println("read");
						condition.signalAll();
				}
				catch(InterruptedException e)
				{
						e.printStackTrace();
				}
				finally
				{
						reentrantLock.unlock();
				}
		});

		public Thread getConsumer()
		{
				return consumer;
		}
}
