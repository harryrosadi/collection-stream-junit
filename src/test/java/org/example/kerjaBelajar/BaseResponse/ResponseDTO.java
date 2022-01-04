package org.example.kerjaBelajar.BaseResponse;

/**
 import lombok.Setter;

 import java.io.Serializable;
 import java.util.Map;

 public class ResponseDTO <T, U extends AResponseDTO> implements Serializable, IResultDTO {
	  		private static final long serialVersionUID = -2741720415995551498L;

	         @JsonProperty("result")
	         private T result;

	         @JsonProperty("responseData")
	         private U responseData;

	         @JsonProperty("metaData")
	         private Map<String, String> metaData;

	         @Override
	         public T getResult() {
	  			return result;
	         }

	         @Override
	         public U getResponseData() {
	  			return responseData;
	         }

	         @Override
	         public Map<String, String> getMetaData() {
	  			return metaData;
	         }
}
 */
