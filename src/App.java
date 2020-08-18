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

public class App
{
		private void deadLockMethod()
		{
				ResourceClass resourceClass = new ResourceClass();
				ResourceClass resourceClass2 = new ResourceClass();
				Thread thread = new Thread(() -> {
						synchronized(resourceClass)
						{
								resourceClass.setA(10);
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
								System.out.println(resourceClass.getA());
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
								synchronized(resourceClass)
								{
										resourceClass.setA(20);
								}
								System.out.println(resourceClass2.getA());
								System.out.println(resourceClass.getA());
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
