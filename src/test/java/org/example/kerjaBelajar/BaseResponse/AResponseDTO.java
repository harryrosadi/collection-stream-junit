package org.example.kerjaBelajar.BaseResponse;
/**
 *
 * import com.fasterxml.jackson.annotation.JsonIgnore;
 * import com.fasterxml.jackson.annotation.JsonProperty;
 * import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
 * import com.fasterxml.jackson.databind.util.StdConverter;
 * import lombok.AllArgsConstructor;
 * import lombok.Getter;
 * import lombok.NoArgsConstructor;
 * import lombok.Setter;
 * import org.springframework.http.HttpStatus;
 *
 * import javax.xml.bind.annotation.XmlRootElement;
 *
 *        @Getter
 *    @Setter
 *    @NoArgsConstructor
 *    @AllArgsConstructor
 *    @XmlRootElement
 *    public abstract class AResponseDTO {
 *        @JsonProperty("responseCode")
 *        @JsonDeserialize(converter = ResponseCodeConverter.class)
 * 		private Integer responseCode;
 *
 * 		private String responseMsg;
 *
 * 		public void setResponseCode(Integer value) {
 * 			this.responseCode = value;
 *        }
 *
 *        @JsonIgnore
 *        public void setResponseCode(HttpStatus value) {
 * 			this.responseCode = value.value();
 *        }
 *
 * 		public boolean equals(HttpStatus o) {
 * 			return responseCode.equals(o.value());
 *        }
 *
 * 		private static class ResponseCodeConverter extends StdConverter<String, Integer> {
 *            @Override
 *            public Integer convert(String value) {
 *
 * 				return Integer.valueOf(value.replaceAll("\\D+", ""));
 *            }
 *        }
 *    }
 *
 * }
 */