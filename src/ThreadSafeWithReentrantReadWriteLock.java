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

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import static java.lang.Thread.sleep;

public class ThreadSafeWithReentrantReadWriteLock implements LockRealization
{
		private ExecutorService executorService = Executors.newFixedThreadPool(2);

		private Map<String, String> resourceMap = new HashMap<>();

		private ReadWriteLock readWriteLock = new ReentrantReadWriteLock();

		@Override
		public void startThreadProcess()
		{
				ThreadSafeWithReentrantReadWriteLock threadSafeWithReentrantReadWriteLock = new ThreadSafeWithReentrantReadWriteLock();
				while(true)
				{
						Runnable writeTask = () -> {
								readWriteLock.writeLock().lock();
								try
								{
										sleep(2000);
										resourceMap.put("foo", "bar");
										System.out.println("Wrote");
								}
								catch(InterruptedException e)
								{
										e.printStackTrace();
								}
								finally
								{
										readWriteLock.writeLock().unlock();
								}
						};
						Runnable readTask = () -> {
								readWriteLock.readLock().lock();
								try
								{
										System.out.println(resourceMap.get("foo"));
										sleep(2000);
								}
								catch(InterruptedException e)
								{
										e.printStackTrace();
								}
								finally
								{
										readWriteLock.readLock().unlock();
								}
						};
						executorService.submit(writeTask);
						executorService.submit(readTask);
						executorService.submit(readTask);
				}
		}
}
