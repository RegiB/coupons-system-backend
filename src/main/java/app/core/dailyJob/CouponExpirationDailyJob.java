package app.core.dailyJob;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import app.core.exceptions.CouponSystemException;
import app.core.services.DailyJobService;

/**
 * thread that runs in parallel with the main coupon system needs service of its
 * own to do multi threading
 */
@Component
public class CouponExpirationDailyJob {

	@Autowired
	private DailyJobService dailyJobService;
	private Timer dailyJob = new Timer();

	@PostConstruct
	public void start() {

		TimerTask dailyTask = new TimerTask() {

			@Override
			public void run() {
				System.out.println("------> removal of expired coupons started ------>");
				try {
					dailyJobService.deleteExpiredCoupons();
				} catch (CouponSystemException e) {
					e.printStackTrace();
				}
			}
		};
		dailyJob.scheduleAtFixedRate(dailyTask, TimeUnit.SECONDS.toMillis(0), TimeUnit.HOURS.toMillis(24));
	}

	/**
	 * stops the daily job
	 */
	@PreDestroy
	public void stop() {
		dailyJob.cancel();
		System.out.println("------> removal of expired coupons stopped ------|");
	}

}
