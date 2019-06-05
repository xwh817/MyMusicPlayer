package xwh.lib.music.util;

import android.os.Handler;
import android.os.Looper;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 缓存型线程池管理
 * 无界corePoolsize,0sizeBlockingQueue，适用于CPU密集型任务调度
 *
 * 线程池为无限大，当执行第二个任务时第一个任务已经完成，会复用执行第一个任务的线程，而不用每次新建线程。
 * 如果线程池长度超过处理需要，可灵活回收空闲线程，若无可回收，则新建线程。
 * 缺省timeout是60s,超过这个IDLE时长，线程实例将被终止及移出池。缓存型池子通常用于执行一些生存期很短的异步型任务 。
 */
public class ThreadPoolManager {
	private ExecutorService service;
	private static Handler handler = new Handler(Looper.getMainLooper());

	private ThreadPoolManager() {
		service = Executors.newCachedThreadPool();
	}

	private static volatile ThreadPoolManager manager;

	public static ThreadPoolManager getInstance() {
		if (null == manager) {
			synchronized (ThreadPoolManager.class) {
				if (null == manager) {
					manager = new ThreadPoolManager();
				}
			}
		}
		return manager;
	}

	/**
	 * 执行一个普通线程
	 * 在子线程运行
	 * @param runnable
	 */
	public void addTask(Runnable runnable) {
		service.submit(runnable);
	}


	/**
	 * 将一个线程丢到UI线程中执行。
	 * 注意：只是更新界面等操作，不要在UI线程中进行耗时操作
	 */
	public void postTaskOnUIThread(Runnable runnable) {
		handler.post(runnable);
	}
}
