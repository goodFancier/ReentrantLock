/*
 * Copyright 2020. Artem Nikitin (https://github.com/goodFancier)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.locks.Condition;

public class ThreadSafeWithReentrantLock implements LockRealization
{
		private static final List<Integer> randoms = new ArrayList<>();

		private Boolean transfer = true;

		private java.util.concurrent.locks.ReentrantLock reentrantLock = new java.util.concurrent.locks.ReentrantLock();

		private Condition condition = reentrantLock.newCondition();

		@Override
		public void startThreadProcess()
		{
				producer.start();
				consumer.start();
		}

		private final Thread producer = new Thread(() ->
		{
				while(true)
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
						try
						{
								Thread.sleep(1000);
						}
						catch(InterruptedException e)
						{
								e.printStackTrace();
						}
				}
		});

		private final Thread consumer = new Thread(() -> {
				while(true)
				{
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
						try
						{
								Thread.sleep(1000);
						}
						catch(InterruptedException e)
						{
								e.printStackTrace();
						}
				}
		});
}
