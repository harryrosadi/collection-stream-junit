package org.example.kerjaBelajar.BaseResponse;

import java.util.Map;

public interface IResultDTO {

	T getResult();

	AResponseDTO getResponseData();

	Map<String, String> getMetaData();

}
