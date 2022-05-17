/**
//package org.example.kerjaBelajar.mapper;
//
//public class MapperSetter {
//private final ResponseClinicMapper responseClinicMapper;
//
//@Value("${data.treatment-type-code.clinic}")
//private List<String> clinicTreatmentCode;
//
//@Value("${data.treatment-type-code.lab}")
//private List<String> labTreatmentCode;
//
//@Override
//public IResultDTO<List<ResponseOutletClinicDTO>> getOutletNearby(
//        String coordinate, boolean availableOnMobile, Double distance,
//        HttpServletRequest request
//        ) throws
//        EndPointException {
//        List<Unit> units = getClinicsFromCoordinate(coordinate, distance, availableOnMobile);
//        if (units.isEmpty()) {
//        String message = String.format("No clinics found in the neighboring area (%s, %s) for %skm",
//        getCoordinates(coordinate)[0], getCoordinates(coordinate)[1], distance
//        );
//        return APIResponseBuilder.noContent(new ArrayList<>(), message);
//        }
//
//        return APIResponseBuilder.ok(responseOutletClinicMapper.sourceLocation(GeoLocation.fromDegrees(
//        getCoordinates(coordinate)[0],
//        getCoordinates(coordinate)[1]
//        )).convert(units));
//        }
//}
**/


/**
 @Service
 @RequiredArgsConstructor(onConstructor = @__(@Autowired))
 public class ResponseOutletClinicMapper extends ADATAMapper<List<Unit>, List<ResponseOutletClinicDTO>> {

 @Accessors(fluent = true)
 @Setter
 private GeoLocation sourceLocation;
 private final ResponseClinicMapper responseClinicMapper;

 @Override
 public List<ResponseOutletClinicDTO> convert(List<Unit> unit) {
 List<ResponseOutletClinicDTO> response = new ArrayList<>();
 unit.forEach(masterUnit1 -> response.add(responseClinicMapper.sourceLocation(sourceLocation).convert(masterUnit1)));

 if (Objects.nonNull(sourceLocation)) {
 response.sort(Comparator.comparing(responseOutletClinicDTO -> responseOutletClinicDTO.getAddress()
 .getDistance()));
 }
 return response;
 }
 */

/**
 package com.telkomsigma.kf.ma.master.clinic.mapper;

 import com.peentar.kf.oa.util.core.exception.DAOException;
 import com.peentar.kf.oa.util.core.external.util.CdnImage;
 import com.peentar.kf.oa.util.core.formula.GeoLocation;
 import com.peentar.kf.oa.util.core.mapper.ADATAMapper;
 import com.telkomsigma.kf.oa.shared.dao.clinic.ILayananPerklinikDAO;
 import com.telkomsigma.kf.oa.shared.data.dto.response.clinic.ResponseOutletClinicDTO;
 import com.telkomsigma.kf.oa.shared.data.dto.response.pos.ResponseOutletShiftDto;
 import com.telkomsigma.kf.oa.shared.data.model.clinic.LayananPerKlinik;
 import com.telkomsigma.kf.oa.shared.data.model.clinic.Unit;
 import lombok.RequiredArgsConstructor;
 import lombok.Setter;
 import lombok.experimental.Accessors;
 import lombok.extern.slf4j.Slf4j;
 import org.springframework.beans.factory.annotation.Autowired;
 import org.springframework.stereotype.Service;

 import java.time.LocalTime;
 import java.util.ArrayList;
 import java.util.List;
 import java.util.Objects;

 @Slf4j
 @Service
 @RequiredArgsConstructor(onConstructor = @__(@Autowired))
 public class ResponseClinicMapper extends ADATAMapper<Unit, ResponseOutletClinicDTO> {

 private final ILayananPerklinikDAO layananPerklinikDAO;
 @Accessors(fluent = true)
 @Setter
 private GeoLocation sourceLocation;
 private final ResponseAddressClinicMapper clinicMapper;
 private final CdnImage cdnImage;

 @Override
 public ResponseOutletClinicDTO convert(Unit unit) {
 List<LayananPerKlinik> homecareList = new ArrayList<>();
 try {
 homecareList = layananPerklinikDAO.findAllByHomecareFlag();
 } catch (DAOException e) {
 log.warn("something wrong with getting data flag ", e);
 }
 List<ResponseOutletShiftDto> shifts = new ArrayList<>();
 for(int shiftday=0;shiftday<=6;shiftday++){
 ResponseOutletShiftDto shift =new ResponseOutletShiftDto();
 shift.setDay(shiftday);
 shift.setOpenTime(LocalTime.parse("08:00"));
 shift.setCloseTime(LocalTime.parse("21:00"));
 shifts.add(shift);
 }
 ResponseOutletClinicDTO response = new ResponseOutletClinicDTO();
 response.setId(unit.getId() == null ? null : unit.getId().toString());
 response.setName(Objects.nonNull(unit.getDisplayName()) ? unit.getDisplayName() : unit.getNama());
 response.setPhone(Objects.isNull(unit.getTelepon()) ? null : unit.getTelepon());
 response.setEmail(Objects.isNull(unit.getEmail()) ? null : unit.getEmail());
 response.setImage(cdnImage.useFill(false)
 .addPadding(false)
 .getUrl("mobile/app_asset/20220401-outlet_default_image.jpeg"));
 response.setOutletId(unit.getOutletId());
 response.setOpenHours(shifts);
 response.setTimezone(Objects.isNull(unit.getZonaWaktu()) ? null : unit.getZonaWaktu());
 assert homecareList != null;
 homecareList.forEach(x -> {
 response.setServesHomeService(Objects.equals(unit.getId(), x.getUnit().getId()));
 });
 response.setAddress(clinicMapper.convert(ResponseAddressClinicMapper.Request.builder()
 .sourceLocation(sourceLocation)
 .unit(unit)
 .build()));
 response.setCode(unit.getKode());
 return response;
 }
 }

 */

/**
 package com.telkomsigma.kf.ma.master.clinic.mapper;

 import com.peentar.kf.oa.util.core.formula.GeoLocation;
 import com.peentar.kf.oa.util.core.mapper.ADATAMapper;
 import com.telkomsigma.kf.oa.shared.data.dto.response.clinic.ResponseAddressClinicDTO;
 import com.telkomsigma.kf.oa.shared.data.dto.response.lab.ResponseAddressLabDTO;
 import com.telkomsigma.kf.oa.shared.data.model.clinic.Unit;
 import lombok.Builder;
 import lombok.Value;
 import lombok.experimental.Accessors;
 import org.springframework.stereotype.Service;

 import java.util.Objects;

 @Service
 public class ResponseAddressClinicMapper extends ADATAMapper<ResponseAddressClinicMapper.Request, ResponseAddressClinicDTO> {
 @Builder
 @Value
 @Accessors(fluent = true)
 public static class Request {
 GeoLocation sourceLocation;
 Unit unit;
 }

 @Override
 public ResponseAddressClinicDTO convert(ResponseAddressClinicMapper.Request request) {
 String latitude = Objects.nonNull(request.unit.getLatlong()) && request.unit.getLatlong().contains(",") ?
 request.unit.getLatlong().split(",")[0] : "0.0";
 String longitude = Objects.nonNull(request.unit.getLatlong()) && request.unit.getLatlong().contains(",") ?
 request.unit.getLatlong().split(",")[1] : "0.0";
 ResponseAddressClinicDTO result = new ResponseAddressClinicDTO();
 result.setPostCode(request.unit.getKodepos());
 result.setProvince(request.unit.getProvinsi() == null ? null : request.unit.getProvinsi().getNama());
 result.setCity(request.unit.getKabupaten() == null ? null : request.unit.getKabupaten().getNama());
 result.setDistrict(request.unit.getKecamatan() == null ? null : request.unit.getKecamatan().getNama());
 result.setVillage(request.unit.getKelurahan());
 result.setStreet(request.unit.getAlamat());
 result.setLongitude(Double.valueOf(longitude));
 result.setLatitude(Double.valueOf(latitude));
 result.setPhoneNumber(request.unit.getTelepon());

 if (Objects.nonNull(request.sourceLocation)) {
 GeoLocation location = GeoLocation.fromDegrees(Double.parseDouble(latitude), Double.parseDouble(longitude));
 result.setDistance(location.distanceTo(request.sourceLocation));
 }
 return result;
 }

 public ResponseAddressClinicDTO convertLabTOClinic(ResponseAddressLabDTO responseAddressLabDTO) {
 return ResponseAddressClinicDTO.builder()
 .distance(responseAddressLabDTO.getDistance())
 .province(responseAddressLabDTO.getProvince())
 .city(responseAddressLabDTO.getCity())
 .village(responseAddressLabDTO.getVillage())
 .district(responseAddressLabDTO.getDistrict())
 .postCode(responseAddressLabDTO.getPostCode())
 .street(responseAddressLabDTO.getStreet())
 .longitude(responseAddressLabDTO.getLongitude())
 .latitude(responseAddressLabDTO.getLatitude())
 .phoneNumber(responseAddressLabDTO.getPhoneNumber())
 .build();
 }
 }

 */