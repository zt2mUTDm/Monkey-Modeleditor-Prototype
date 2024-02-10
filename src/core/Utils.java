package core;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import javax.swing.JOptionPane;

import com.jme3.app.Application;

import online.money_daisuki.api.base.models.SetableMutableSingleValueModel;
import online.money_daisuki.api.base.models.SetableMutableSingleValueModelImpl;

public final class Utils {
	
	private static boolean excepted;
	
	public static void enqueueAndWait(final Application app, final Runnable run) {
		final Lock lock = new ReentrantLock();
		final Condition con = lock.newCondition();
		
		final SetableMutableSingleValueModel<Throwable> t = new SetableMutableSingleValueModelImpl<>();
		
		try {
			lock.lock();
			
			app.enqueue(new Runnable() {
				@Override
				public void run() {
					try {
						lock.lock();
						run.run();
					} catch(final Throwable e) {
						t.sink(e);
					} finally {
						try {
							con.signal();
						} finally {
							lock.unlock();
						}
					}
				}
			});
			con.await();
			
			if(t.isSet()) {
				JOptionPane.showMessageDialog(null, "Uncaught exception: " + t.source().getMessage());
			}
		} catch (final InterruptedException e) {
			throw new RuntimeException(e);
		} finally {
			lock.unlock();
		}
	}
}
