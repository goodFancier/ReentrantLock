
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

import java.util.LinkedHashMap;

public class App
{
		/**
		 * 1. 10 20
		 * 2. 20 10
		 * <p>
		 * <p>
		 * 1. 10 10
		 * 2. 20 20
		 * 3. 10 20
		 * 4. 20 10
		 */
		private void deadLockMethod()
		{
				ResourceClass1 resourceClass1 = new ResourceClass1();
				ResourceClass1 resourceClass2 = new ResourceClass1();
				Thread thread = new Thread(() -> {
						synchronized(resourceClass1)
						{
								resourceClass1.setA(10);
								try
								{
										Thread.sleep(1000);
								}
								catch(InterruptedException e)
								{
										e.printStackTrace();
								}
								synchronized(resourceClass2)
								{
										resourceClass2.setA(30);
								}
								System.out.println(resourceClass1.getA());
								System.out.println(resourceClass2.getA());
						}
				});
				thread.setDaemon(false);
				thread.start();
				Thread thread2 = new Thread(() -> {
						synchronized(resourceClass2)
						{
								resourceClass2.setA(40);
								try
								{
										Thread.sleep(1000);
								}
								catch(InterruptedException e)
								{
										e.printStackTrace();
								}
								synchronized(resourceClass1)
								{
										resourceClass1.setA(20);
								}
								System.out.println(resourceClass2.getA());
								System.out.println(resourceClass1.getA());
						}
				});
				thread2.setDaemon(false);
				thread2.start();
		}

		public static void main(String[] args)
		{
				/*ThreadSafeWithReentrantReadWriteLock threadSafeWithReentrantReadWriteLock = new ThreadSafeWithReentrantReadWriteLock();
				threadSafeWithReentrantReadWriteLock.startThreadProcess();*/
				/*ThreadSafeWithReentrantLock threadSafeWithReentrantLock = new ThreadSafeWithReentrantLock();
				threadSafeWithReentrantLock.startThreadProcess();*/
				/*ThreadSafeWithReentrantLock threadSafeWithSynchronization = new ThreadSafeWithReentrantLock();
				threadSafeWithSynchronization.startThreadProcess();*/
				new App().deadLockMethod();
		}
}
