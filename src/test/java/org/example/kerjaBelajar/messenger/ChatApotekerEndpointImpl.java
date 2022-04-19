package org.example.kerjaBelajar.messenger;
/**
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.peentar.kf.oa.util.core.dto.APIResponseBuilder;
import com.peentar.kf.oa.util.core.dto.IResultDTO;
import com.peentar.kf.oa.util.core.dto.ResultDTO;
import com.telkomsigma.kf.ma.messenger.dto.response.ResponseChatParticipantDto;
import com.telkomsigma.kf.ma.messenger.dto.response.ResponseSessionChatApotekerDto;
import com.telkomsigma.kf.ma.messenger.endpoint.IChatApotekerEndpoint;
import com.telkomsigma.kf.ma.messenger.mapper.ChatApotkerParticipantMapper;
import com.telkomsigma.kf.ma.messenger.mapper.ChatUserParticipantMapper;
import com.telkomsigma.kf.ma.rest.client.IProfileAPIClient;
import com.telkomsigma.kf.ma.rest.client.chatapoteker.ITrxHomecareChatApotekerClient;
import com.telkomsigma.kf.oa.shared.data.dto.request.homecare.RequestAddChatSessionDTO;
import com.telkomsigma.kf.oa.shared.data.dto.response.ResponseFirebaseChatDto;
import com.telkomsigma.kf.oa.shared.data.dto.response.ResponseProfileDTO;
import com.telkomsigma.kf.oa.shared.data.dto.response.homecare.ResponseSessionChaApotekertDto;
import com.telkomsigma.kf.oa.shared.data.model.mobile.security.User;
import com.telkomsigma.kf.oa.shared.data.statval.enumeration.homecare.trx.chats.EMessageContentType;
import com.telkomsigma.kf.oa.shared.data.statval.enumeration.security.EUserRole;
import com.telkomsigma.kf.oa.util.exception.EndPointException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@RestController
public class ChatApotekerEndpointImpl implements IChatApotekerEndpoint {
	private final IProfileAPIClient profileAPIClient;
	private final ChatUserParticipantMapper userParticipantMapper;
	private final ChatApotkerParticipantMapper apotekerParticipantMapper;
	private final ITrxHomecareChatApotekerClient trxHomecareChatApotekerClient;

	@Value("${greeting.chat}")
	private String greeting;

	@Override
	public IResultDTO<ResponseFirebaseChatDto> startSession(OAuth2Authentication authentication, HttpServletRequest p_Request) throws EndPointException {
		List<ResponseChatParticipantDto> listParticipant = new ArrayList<>();
		ResponseSessionChatApotekerDto chatApotekerDto = new ResponseSessionChatApotekerDto();
		ResponseChatParticipantDto apotekerParticipant = new ResponseChatParticipantDto();
		ResponseFirebaseChatDto responseData = new ResponseFirebaseChatDto();

		User userId = (User) authentication.getPrincipal();
		ResultDTO<ResponseProfileDTO> profileByUserId = profileAPIClient.findProfileByUserId(userId.getId());
		ResponseProfileDTO user = profileByUserId.getResult();

		ResultDTO<List<ResponseProfileDTO>> profileByRole = profileAPIClient.findProfileByRole(EUserRole.ROLE_PHARMACIST);
		List<ResponseProfileDTO> profileRole = profileByRole.getResult();

		Collections.shuffle(profileRole, new Random());
		ResponseProfileDTO resultApoteker = profileRole.get(0);

		Map<String, Object> map = new HashMap<>();
		map.put("content", greeting);
		map.put("contentType", EMessageContentType.TEXT);

		apotekerParticipant = apotekerParticipantMapper.convert(resultApoteker);
		map.put("senderId", resultApoteker.getUserId());

		map.put("sentTimeFormat", ZonedDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSZ")));
		ResponseChatParticipantDto userParticipant = userParticipantMapper.convert(user);
		listParticipant.add(userParticipant);
		listParticipant.add(apotekerParticipant);

		List<RequestAddChatSessionDTO> requestAddChatSessionDTOS = CollectionUtils.emptyIfNull(listParticipant)
				.stream()
				.map(x -> RequestAddChatSessionDTO.builder()
						.userId(x.getUserId())
						.userName(x.getUserName())
						.fullName(x.getFullName())
						.userRole(x.getUserRole())
						.build())
				.collect(Collectors.toList());

		ResultDTO<ResponseSessionChaApotekertDto> responseSession = trxHomecareChatApotekerClient.setSessionId(requestAddChatSessionDTOS);
		ResponseSessionChaApotekertDto session = responseSession.getResult();

		DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("/chat/session").push();
		String key = databaseReference.getKey();
		DatabaseReference dataSessionId = FirebaseDatabase.getInstance().getReference("/chat/message").child(key);
		DatabaseReference dataGreetingMessage = FirebaseDatabase.getInstance().getReference("/chat/message").child(key).child("messageDTOS").push();

		Map<String, Object> mapSessionId = new HashMap<>();
		mapSessionId.put("sessionId", session.getSessionId());
		dataSessionId.setValueAsync(mapSessionId);

		responseData.setSessionId(session.getSessionId());
		responseData.setKey(key);

		chatApotekerDto.setParticipant(listParticipant);
		chatApotekerDto.setStartTime(LocalDateTime.now());
		chatApotekerDto.setSessionId(session.getSessionId());
		dataGreetingMessage.setValueAsync(map);
		databaseReference.setValueAsync(chatApotekerDto);
		return APIResponseBuilder.ok(responseData);
	}

}
*/