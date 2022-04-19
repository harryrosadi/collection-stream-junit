package org.example.kerjaSchedulerRedis.service.impl;
/**

import com.telkomsigma.kf.ma.rest.client.IPosNotificationClient;
import com.telkomsigma.kf.ma.trx.kftransactionhistory.service.INotificationSchedulerService;
import com.telkomsigma.kf.oa.shared.data.dto.request.trx.RequestSendNotificationSchedulerDTO;
import com.telkomsigma.kf.oa.shared.data.model.dwh.ItemOutlet;
import com.telkomsigma.kf.oa.util.exception.EndPointException;
import com.telkomsigma.kf.oa.util.exception.ServiceException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.quartz.JobExecutionContext;
import org.quartz.SchedulerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Slf4j
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@Service
public class SchedulerImpl implements INotificationSchedulerService {

		private final RedisTemplate<String, Integer> redisTemplate;
		private final IPosNotificationClient posNotificationClient;

		@Value("${pos-notification.title}")
		private String title;

		@Value("${pos-notification.content}")
		private String content;

		@Override
		public void processNotification(String triggerName, Map<Integer, List<ItemOutlet>> posOutletMap, JobExecutionContext jobExecutionContext) throws SchedulerException, ServiceException, EndPointException {

			if (jobExecutionContext.getJobDetail().getKey() != null) {
				log.info("service notification is running");

				if (!posOutletMap.isEmpty()) {
					RequestSendNotificationSchedulerDTO request = new RequestSendNotificationSchedulerDTO();

					for (Map.Entry<Integer, List<ItemOutlet>> entry : posOutletMap.entrySet()) {
						Integer key = entry.getKey();
						List<ItemOutlet> value = entry.getValue();
						request.setTitle(title);
						request.setOutletId(key);
						request.setContent(String.format(content, value.size()));

						redisTemplate.opsForValue().set("lastId", key);
						System.out.println("outlet id === " + request.getOutletId() + "  kontent  " + request.getContent());
						System.out.println("last id === " + key);
					}

					posNotificationClient.posNotification(request);
				} else {
					log.warn("Outlet Id is Empty");
				}
			} else {
				log.warn("No job for today");
			}

		}
	}

}
 */
