package org.example.kerjaSchedulerRedis.scheduler.job;
/**
import com.telkomsigma.kf.ma.trx.kftransactionhistory.scheduler.NotificationScheduler;
import com.telkomsigma.kf.ma.trx.kftransactionhistory.service.INotificationSchedulerService;
import com.telkomsigma.kf.oa.shared.dao.dwh.ItemOutletDAO;
import com.telkomsigma.kf.oa.shared.data.model.dwh.ItemOutlet;
import com.telkomsigma.kf.oa.util.exception.EndPointException;
import com.telkomsigma.kf.oa.util.exception.ServiceException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.logging.log4j.util.Strings;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@Slf4j
@Component
public class SchedulerJob implements Job {

		private final ItemOutletDAO itemOutletDAO;
		private final NotificationScheduler notificationScheduler;
		private final INotificationSchedulerService notificationSchedulerService;
		private final RedisTemplate<String, Integer> redisTemplate;
		private final SchedulerFactoryBean schedulerFactoryBean;

		@Override
		public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {

			Scheduler scheduler = schedulerFactoryBean.getScheduler();

			log.info("execute notification job");
			String triggerName = null;
			try {
				triggerName = (jobExecutionContext.getJobDetail().getKey().getName());
			} catch (Exception e) {
				log.warn("Failed to get scheduler job context", e);
			}
			if (Strings.isBlank(triggerName)) {
				log.error("We can't get triggerName for execute job");
				return;
			}
			try {
				Integer outletId = redisTemplate.opsForValue().get("lastId");
				if (outletId == null) {
					outletId = 0;
				}

				Pageable pageable = PageRequest.of(0, 50);

				List<ItemOutlet> itemOutlets = itemOutletDAO.findAllByPosOutletId(outletId, pageable);
				Map<Integer, List<ItemOutlet>> posOutletMap = itemOutlets.stream().collect(Collectors.groupingBy(ItemOutlet::getPosOutletId));

				if (CollectionUtils.sizeIsEmpty(itemOutlets)) {
					scheduler.deleteJob(jobExecutionContext.getJobDetail().getKey());
					log.info("All item has been send, lets start scheduler for tomorrow");
					redisTemplate.opsForValue().set("lastId", 0);
					log.info("reset Outlet Id Redis to 0");
					notificationScheduler.rescheduleJob();
					return;
				}
				notificationSchedulerService.processNotification(triggerName, posOutletMap, jobExecutionContext);

				if (jobExecutionContext.getTrigger().getNextFireTime() == null) {
					log.info("Run Scheduler Trigger has timeout start again tomorrow");
					notificationScheduler.rescheduleJob();
				}

			} catch (ServiceException | SchedulerException | EndPointException e) {
				log.warn("something wrong with execute job", e);
			}
		}
	}
	*/