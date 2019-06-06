package com.nicksdesk.manager;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import javax.swing.JOptionPane;

import com.nicksdesk.tweaker.Main;
import com.nicksdesk.tweaker.TweakManagerFrame;
import com.nicksdesk.utilities.OS;

public class Worker {

	protected static JAPT apt;
	protected static TweakManagerFrame frame;
	
	public static void createDaemon(Runnable code) {
		Runnable background = new Runnable() {
			public void run() {
				code.run();
			}
		};
		ExecutorService runner = Executors.newCachedThreadPool();
		runner.submit(background);
	}
	
	public static void checkDeviceConnectionValidity(long repeatTimeMillis) {
		final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
		final ScheduledFuture<?> checkHandler = scheduler.scheduleAtFixedRate(new Runnable() {
			public void run() {
				if(!Main.didMakeConnection()) {
					Console.err("[CONNECTION] ERR!");
					JOptionPane.showMessageDialog(null, "The current managed device seems to have disconnected!", "Error!", JOptionPane.ERROR_MESSAGE);
					if(OS.isRuntimeJar()) {
						OS.restart();
					} else {
						Console.err("Cannot restart while in development!");
					}
				} else {
					Console.log("[CONNECTION] VALID!");
				}
			}
		}, 5, 10, TimeUnit.SECONDS);
		scheduler.schedule(new Runnable() {
			public void run() {
				checkHandler.cancel(true);
			}
		}, System.currentTimeMillis(), TimeUnit.MILLISECONDS);
	}
	
	
}