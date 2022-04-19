package org.example.kerjaSchedulerRedis.scheduler;
/**
import com.telkomsigma.kf.ma.trx.kftransactionhistory.scheduler.config.AutoWiringSpringBeanJobFactory;
import com.telkomsigma.kf.ma.trx.kftransactionhistory.scheduler.job.NotificationJob;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.quartz.DateBuilder;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SimpleScheduleBuilder;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.quartz.JobDetailFactoryBean;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.scheduling.quartz.SpringBeanJobFactory;
import org.springframework.stereotype.Component;

import java.util.Objects;
import java.util.UUID;

@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@Slf4j
@Component
@Configuration
public class Scheduler {

	private final SchedulerFactoryBean schedulerFactoryBean;
	private final ApplicationContext applicationContext;

	@Bean
	public Scheduler startScheduler() {

		Scheduler scheduler = schedulerFactoryBean.getScheduler();

		JobDetailFactoryBean rescheduleJobDetail = new JobDetailFactoryBean();
		rescheduleJobDetail.setJobClass(NotificationJob.class);
		rescheduleJobDetail.setName("Notification_to_pos_" + UUID.randomUUID());
		rescheduleJobDetail.setGroup("NOTIFICATION_JOB_" + UUID.randomUUID());
		rescheduleJobDetail.setDescription("Reminder to POS");
		rescheduleJobDetail.setDurability(false);
		rescheduleJobDetail.afterPropertiesSet();
		JobDetail job = rescheduleJobDetail.getObject();

		if (Objects.isNull(job)) {
			log.warn("Failed creating job");
		}

		Trigger trigger = TriggerBuilder.newTrigger()
				.withIdentity("trigger_" + UUID.randomUUID())
				.withSchedule(SimpleScheduleBuilder.simpleSchedule()
						.withIntervalInMinutes(1)
						.repeatForever())
				.startAt(DateBuilder.tomorrowAt(1, 0, 0))
				.endAt(DateBuilder.tomorrowAt(7, 0, 0))
				.build();

		log.info("SCHEDULER START JOB {} AND END JOB {} ", trigger.getStartTime(), trigger.getEndTime());
		try {
			scheduler.scheduleJob(job, trigger);
			if (!scheduler.isStarted()) {
				scheduler.start();
			}

		} catch (SchedulerException e) {
			log.warn("cannot start scheduler");
		}
		return scheduler;
	}

	public void rescheduleJob() {
		try {
			JobDetailFactoryBean rescheduleJobDetail = new JobDetailFactoryBean();
			rescheduleJobDetail.setJobClass(NotificationJob.class);
			rescheduleJobDetail.setName("Notification_to_pos_" + UUID.randomUUID());
			rescheduleJobDetail.setGroup("NOTIFICATION_JOB_" + UUID.randomUUID());
			rescheduleJobDetail.setDescription("Reminder to POS");
			rescheduleJobDetail.setDurability(false);
			rescheduleJobDetail.afterPropertiesSet();
			JobDetail job = rescheduleJobDetail.getObject();

			if (Objects.isNull(job)) {
				log.warn("Failed creating job");
			}

			Trigger trigger = TriggerBuilder.newTrigger()
					.withIdentity("trigger_" + UUID.randomUUID())
					.withSchedule(SimpleScheduleBuilder.simpleSchedule()
							.withIntervalInMinutes(1)
							.repeatForever())
					.startAt(DateBuilder.tomorrowAt(1, 0, 0))
					.endAt(DateBuilder.tomorrowAt(7, 0, 0))
					.build();
			schedulerFactoryBean.getScheduler()
					.scheduleJob(job, trigger);
			log.info("Tomorrow start scheduler at {} {}", trigger.getStartTime(), trigger.getEndTime());
		} catch (SchedulerException e) {
			log.warn("Failed to create a new schedule for current job. Rescheduling new job instead.");
		}
	}

	@Bean
	public SpringBeanJobFactory springBeanJobFactory() {
		AutoWiringSpringBeanJobFactory jobFactory = new AutoWiringSpringBeanJobFactory();

		jobFactory.setApplicationContext(applicationContext);
		return jobFactory;
	}
}
*/

