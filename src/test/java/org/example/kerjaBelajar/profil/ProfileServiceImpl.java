package org.example.kerjaBelajar.profil;
/**
import com.google.gson.Gson;
import com.peentar.kf.oa.util.core.exception.DAOException;
import com.peentar.kf.oa.util.core.exception.ServiceException;
import com.peentar.kf.oa.util.core.external.util.CdnImage;
import com.peentar.kf.oa.util.core.external.util.UploadImage;
import com.telkomsigma.kf.ma.rest.client.IForgotMailAPIClient;
import com.telkomsigma.kf.oa.security.mapper.OcrImageConverter;
import com.telkomsigma.kf.oa.security.service.IProfileService;
import com.telkomsigma.kf.oa.shared.dao.mobile.security.*;
import com.telkomsigma.kf.oa.shared.data.dto.request.RequestMemberVerificationDTO;
import com.telkomsigma.kf.oa.shared.data.dto.request.RequestUpdateProfileDTO;
import com.telkomsigma.kf.oa.shared.data.dto.request.messenger.RequestChangeProfileDTO;
import com.telkomsigma.kf.oa.shared.data.dto.response.user.ResponseOcrImageDTO;
import com.telkomsigma.kf.oa.shared.data.model.mobile.memberattribute.Job;
import com.telkomsigma.kf.oa.shared.data.model.mobile.memberattribute.MaritalStatus;
import com.telkomsigma.kf.oa.shared.data.model.mobile.security.OcrImage;
import com.telkomsigma.kf.oa.shared.data.model.mobile.security.User;
import com.telkomsigma.kf.oa.shared.data.model.mobile.security.UserProfile;
import com.telkomsigma.kf.oa.shared.data.model.mobile.security.UserRole;
import com.telkomsigma.kf.oa.shared.data.statval.IApplicationConstant;
import com.telkomsigma.kf.oa.shared.data.statval.enumeration.EActivitySource;
import com.telkomsigma.kf.oa.shared.data.statval.enumeration.security.EUserRole;
import com.telkomsigma.kf.oa.shared.service.scaffolding.impl.AScaffoldingService;
import com.telkomsigma.kf.oa.util.PreCondition;
import feign.RetryableException;
import id.co.bvk.buayaamazon.data.MinimunIndonesianEktp;
import id.co.bvk.buayaamazon.data.RequestExtractEktp;
import id.co.bvk.buayaamazon.data.RequestUploadImage;
import id.co.bvk.buayaamazon.data.ResponseUploadImage;
import id.co.bvk.buayaamazon.extractektp.ExtractEktpId;
import id.co.bvk.buayaamazon.s3.UploadImageAwsS3;
import io.micrometer.core.instrument.util.StringUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.ZoneId;
import java.util.*;

@Slf4j
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@Service
public class ProfileServiceImpl extends AScaffoldingService<UserProfile> implements IProfileService {

    private final IUserDAO userDAO;
    private final IUserProfileDAO userProfileDAO;
    private final IForgotMailAPIClient mailAPIClient;
    private final CdnImage cdnImage;
    private final IJobDAO iJobDAO;
    private final IMaritalStatusDAO iMaritalStatusDAO;
    private final IOcrImageDAO ocrImageDAO;
    private final UploadImage uploadImage;
    private final OcrImageConverter ocrImageConverter;
    private final IUserRoleDAO userRoleDAO;

    @Value("${config.profile.file-extension:jpg, jpeg, png, gif}")
    private List<String> allowedFileExtensions;

    @Value("${config.profile.picture-size:300000}")
    private long maxFileSize;

    @Value("vaccination-development")
    private String bucketName;

    @Transactional
    @Override
    public Boolean updateProfile(RequestUpdateProfileDTO p_RequestUpdateProfileDTO, String code) throws
            ServiceException {
        User user;
        try {
            user = userDAO.findByCodeAndStatus(code, IApplicationConstant.GeneralValue.STATUS_ACTIVE);
        } catch (DAOException e) {
            log.error("Failed to Find User {}", e.toString());
            throw new ServiceException("Failed to FInd User." + e.getMessage(), e);
        }

        if (user == null || user.getUserProfile() == null) {
            log.error("User Not Found");
            throw new ServiceException("Pengguna Tidak Ditemukan");
        }

        UserProfile userProfile = user.getUserProfile();
        boolean isDuplicateData;
        try {
            UserProfile existingProfile =
                    userProfileDAO.findByUserCodeNotAndMobilePhoneNumberOrEmail(
                            userProfile.getCode(),
                            p_RequestUpdateProfileDTO.getMobilePhoneNumber(),
                            p_RequestUpdateProfileDTO.getEmail()
                    );

            isDuplicateData = existingProfile != null;
        } catch (DAOException e) {
            throw new ServiceException("Error Execute DAO");
        }

        if (isDuplicateData) {
            throw new ServiceException("Nomor Telepon / Email Telah Terdaftar");
        }

        // TODO: Validate this using bean validation
        if (Strings.isBlank(p_RequestUpdateProfileDTO.getGender())) {
            throw new ServiceException("Jenis kelamin harus diisi");
        }

        String[] validGender = new String[]{"MALE", "FEMALE"};
        if (Arrays.stream(validGender).noneMatch(x -> Objects.equals(x, p_RequestUpdateProfileDTO.getGender()))) {
            throw new ServiceException("Gender Must Be Male or FEMALE");
        }


        boolean didEmailChanged = false;
        RequestChangeProfileDTO requestChangeEmailProfile = new RequestChangeProfileDTO();
        requestChangeEmailProfile.setTo(userProfile.getEmail());
        requestChangeEmailProfile.setName(userProfile.getName());
        if (Strings.isNotBlank(p_RequestUpdateProfileDTO.getEmail()) && !Objects.equals(
                p_RequestUpdateProfileDTO.getEmail(),
                userProfile.getEmail()
        )) {
            requestChangeEmailProfile.setSubject("Ubah Email berhasil");
            requestChangeEmailProfile.setOldChange(userProfile.getEmail());
            requestChangeEmailProfile.setNewChange(p_RequestUpdateProfileDTO.getEmail());

            userProfile.setEmail(p_RequestUpdateProfileDTO.getEmail());
            didEmailChanged = true;
        }
        boolean didMobileChanged = false;
        RequestChangeProfileDTO requestChangeMobileProfile = new RequestChangeProfileDTO();
        requestChangeMobileProfile.setTo(userProfile.getEmail());
        requestChangeMobileProfile.setName(userProfile.getName());
        if (Strings.isNotBlank(p_RequestUpdateProfileDTO.getMobilePhoneNumber()) && !Objects.equals(
                p_RequestUpdateProfileDTO.getMobilePhoneNumber(),
                userProfile.getMobilePhoneNumber()
        )) {
            requestChangeMobileProfile.setSubject("Ubah Handphone berhasil");
            requestChangeMobileProfile.setOldChange(userProfile.getMobilePhoneNumber());
            requestChangeMobileProfile.setNewChange(p_RequestUpdateProfileDTO.getMobilePhoneNumber());

            userProfile.setMobilePhoneNumber(p_RequestUpdateProfileDTO.getMobilePhoneNumber());
            didMobileChanged = true;
        }
        userProfile.setGender(p_RequestUpdateProfileDTO.getGender());
        userProfile.setName(StringUtils.isBlank(p_RequestUpdateProfileDTO.getName()) ? userProfile.getName() :
                                    p_RequestUpdateProfileDTO.getName());
        userProfile.setDateOfBirth(Objects.isNull(p_RequestUpdateProfileDTO.getDateOfBirth()) ?
                                           userProfile.getDateOfBirth() : p_RequestUpdateProfileDTO.getDateOfBirth());
        userProfileDAO.save(userProfile);
        log.info("Success Save Profile With Id {} and Code {}", userProfile.getId(), userProfile.getUser().getCode());

        user.setName(p_RequestUpdateProfileDTO.getName());
        userDAO.save(user);
        log.info("Success Save User With Id {}", user.getId());
        SecurityServiceImpl.refreshSecurityContext(userProfile);

        try {
            if (didEmailChanged) mailAPIClient.changeEmail(requestChangeEmailProfile);
            if (didMobileChanged) mailAPIClient.changePhone(requestChangeMobileProfile);
        } catch (RetryableException e) {
            log.warn("Failed sent email, maybe service messenger is down");
        }

        return true;
    }

    @Override
    public UserProfile findProfileByCode(String code) throws ServiceException {
        UserProfile userProfile;
        try {
            userProfile = userProfileDAO.findByUser_Code(code);
        } catch (DAOException e) {
            log.error("Error Find User Profile By user Code {}", e.toString());
            throw new ServiceException("Error Find User Profile By User Code." + e.getMessage());
        }

        return userProfile;
    }

    @Override
    public UserProfile findProfileByMobilePhoneNumber(String mobileNumber) throws ServiceException {
        UserProfile userProfile;
        try {
            userProfile = userProfileDAO.findOneByMobilePhoneNumber(mobileNumber);
        } catch (DAOException e) {
            log.error("Error Find User Profile By mobile number. Detail: {}", e.toString());
            throw new ServiceException("Error Find User Profile By mobile number." + e.getMessage());
        }

        return userProfile;
    }

    @Override
    public UserProfile findProfileByUserId(String userId) throws ServiceException {
        UserProfile userProfile;
        try {
            Long actualUserId = null;
            try {
                actualUserId = Long.valueOf(userId);
                return userProfileDAO.findByUser_Id(actualUserId);
            } catch (NumberFormatException ignored) {
                //ignore result
            }

            userProfile = userProfileDAO.findByUser_Code(userId);
        } catch (DAOException e) {
            log.error("Error Find User Profile By user id {}", e.toString());
            throw new ServiceException("Error Find User Profile By User Id." + e.getMessage());
        }

        return userProfile;
    }

    @Override
    public List<UserProfile> findProfileByUserIds(Long[] userIds) throws ServiceException {
        List<UserProfile> results;
        try {
            results = userProfileDAO.findByUser_IdIn(userIds);
        } catch (DAOException e) {
            log.error("Failed to get user profiles {}", e.toString());
            throw new ServiceException("Failed to get user profiles" + e.getMessage());
        }
        return results;
    }

    @Override
    public Page<UserProfile> getProfileByName(String name, Pageable pageable) throws ServiceException {
        Page<UserProfile> userProfile;
        try {
            userProfile = userProfileDAO.findAllByName(name.toUpperCase(), pageable);
        } catch (DAOException e) {
            log.warn("Error find user by name {}", name);
            throw new ServiceException("Error find user by name");
        }
        return userProfile;
    }

    @Override
    public Slice<UserProfile> findProfileUserName(String userName, Pageable pageable) throws ServiceException {
        Slice<UserProfile> userProfile;
        try {
            userProfile = userProfileDAO.findByUserCodeAndMobilePhoneNumberV2(
                    userName,
                    (userName.startsWith("0") ? userName.substring(1) : userName),
                    userName.toUpperCase(),
                    userName.toUpperCase(),
                    pageable
            );
        } catch (DAOException e) {
            log.error("Error Find User Profile By user id {}", e.toString());
            throw new ServiceException("Error Find User Profile By User Id." + e.getMessage());
        }

        return userProfile;
    }

    @Override
    public UserProfile getProfileByCustomerId(String customerId) throws ServiceException {
        UserProfile userProfile;
        try {
            userProfile = userProfileDAO.findByCustomerId(customerId.toUpperCase());
        } catch (DAOException e) {
            log.error("Error Find User Profile By user customer id {}", e.toString());
            throw new ServiceException("Error Find User Profile By customer Id." + e.getMessage());
        }
        return userProfile;
    }

    @Override
    public UserProfile getProfileByCustomerIdOrUserCode(String customerId) throws ServiceException {
        UserProfile userProfile;
        try {
            userProfile = userProfileDAO.findByCustomerIdOrUserCode(customerId.toUpperCase(), customerId.toUpperCase());
        } catch (DAOException e) {
            log.error("Error Find User Profile By user customer id {}", e.toString());
            throw new ServiceException("Error Find User Profile By customer Id." + e.getMessage());
        }
        return userProfile;
    }

    @Override
    public EActivitySource getRegistrationSourceByMobilePhone(String mobilePhoneNumber) throws ServiceException {
        UserProfile userProfile = null;
        List<UserProfile> userProfiles =
                userProfileDAO.findAll(Example.of(UserProfile.builder().mobilePhoneNumber(mobilePhoneNumber).build()));
        if (!CollectionUtils.sizeIsEmpty(userProfiles)) {
            userProfile = userProfiles.get(0);
        }

        if (userProfile == null) {
            throw new ServiceException("Nomor Telepon " + mobilePhoneNumber + " Tidak Ditemukan");
        }

        return userProfile.getUser().getSource();
    }

    @Override
    public EActivitySource getRegistrationSourceByEmail(String email) throws ServiceException {
        UserProfile userProfile = null;
        List<UserProfile> userProfiles = userProfileDAO.findAll(Example.of(UserProfile.builder().email(email).build()));
        if (!CollectionUtils.sizeIsEmpty(userProfiles)) {
            userProfile = userProfiles.get(0);
        }

        if (userProfile == null) {
            throw new ServiceException("Email " + email + " Tidak Ditemukan");
        }

        return userProfile.getUser().getSource();
    }

    @Override
    public Page<UserProfile> getAllMember(Pageable pageable) throws ServiceException {
        return userProfileDAO.findAll(pageable);
    }

    @Override
    public Page<UserProfile> getAllVerificationMember(boolean isVerificationUser, Pageable pageable) throws
            ServiceException {
        Page<UserProfile> userProfile;
        try {
            userProfile = userProfileDAO.findAllByVerificationUser(isVerificationUser, pageable);
        } catch (DAOException e) {
            log.error("Error Find User Profile By verification user{}", e.toString());
            throw new ServiceException("Error Find User Profile By verification user." + e.getMessage());
        }
        return userProfile;
    }

    @Override
    public boolean uploadMemberVerificationData(RequestMemberVerificationDTO requestMemberVerificationDTO, User user)
            throws
            ServiceException {
        UserProfile userProfile;
        try {
            userProfile = userProfileDAO.findByCustomerId(user.getPublicId());
        } catch (DAOException e) {
            log.warn("User not found with public id {}", user.getPublicId());
            throw new ServiceException("User Not Found" + e.getMessage());
        }

        PreCondition.checkError(!requestMemberVerificationDTO.getGender()
                                                             .equalsIgnoreCase("MALE") && !requestMemberVerificationDTO
                .getGender().equalsIgnoreCase(
                        "FEMALE"), new ServiceException("Gender must be MALE or FEMALE"));

        Job job = iJobDAO.findOne(Example.of(Job.createNew(requestMemberVerificationDTO.getJob()))).orElse(null);
        if (job == null) {
            log.warn("Job not available with name {}", requestMemberVerificationDTO.getJob());
            throw new ServiceException("Job " + requestMemberVerificationDTO.getJob() + " not available");
        }

        MaritalStatus maritalStatus =
                iMaritalStatusDAO
                        .findOne(Example.of(MaritalStatus.createNew(requestMemberVerificationDTO.getMaritalStatus())))
                        .orElse(
                                null);
        if (maritalStatus == null) {
            log.warn("Marital Status not available with name {}", requestMemberVerificationDTO.getMaritalStatus());
            throw new ServiceException("Marital Status " + requestMemberVerificationDTO.getMaritalStatus() + " not " +
                                               "available");
        }

        ZoneId defaultZoneId = ZoneId.systemDefault();

        // Ensure that user can't update it with empty values
        userProfile.setNik(Strings.isBlank(requestMemberVerificationDTO.getNik()) ? userProfile.getNik() :
                                   requestMemberVerificationDTO.getNik());
        userProfile.setNameAccordingToNik(Strings.isBlank(requestMemberVerificationDTO.getNameAccordingToNik()) ?
                                                  userProfile.getNameAccordingToNik() :
                                                  requestMemberVerificationDTO.getNameAccordingToNik());
        userProfile.setGender(Strings.isBlank(requestMemberVerificationDTO.getGender()) ? userProfile.getGender() :
                                      requestMemberVerificationDTO.getGender().toUpperCase());
        userProfile.setPlaceOfBirth(Strings.isBlank(requestMemberVerificationDTO.getPlaceOfBirth()) ?
                                            userProfile.getPlaceOfBirth() :
                                            requestMemberVerificationDTO.getPlaceOfBirth());
        userProfile.setDateOfBirth(Objects.isNull(requestMemberVerificationDTO.getDateOfBirth()) ?
                                           userProfile.getDateOfBirth() :
                                           Date.from(requestMemberVerificationDTO.getDateOfBirth().atStartOfDay(
                                                   defaultZoneId).toInstant()));
        userProfile.setAddress(Strings.isBlank(requestMemberVerificationDTO.getAddress()) ? userProfile.getAddress()
                                       : requestMemberVerificationDTO.getAddress());
        userProfile.setJob(job);
        userProfile.setMaritalStatus(maritalStatus);
        userProfile.setAllergy(Strings.isBlank(requestMemberVerificationDTO.getAllergy()) ? userProfile.getAllergy()
                                       : requestMemberVerificationDTO.getAllergy());
        userProfileDAO.save(userProfile);

        SecurityServiceImpl.refreshSecurityContext(userProfile);

        return true;
    }

    @Override
    public String uploadMemberVerificationPicture(MultipartFile picture, String userCode) throws ServiceException {
        if (picture == null) {
            log.warn("No image file uploaded");
            throw new ServiceException("No image file uploaded");
        }

        if (picture.getSize() > maxFileSize) {
            log.warn("File size exceed maximum file size. File size : " + maxFileSize);
            throw new ServiceException("File size can't exceed " + maxFileSize);
        }

        String extension = uploadImage.getFileExtension(picture.getOriginalFilename(), allowedFileExtensions);

        UserProfile userProfile;
        try {
            userProfile = userProfileDAO.findByUser_Code(userCode);
        } catch (DAOException e) {
            log.error("Error Find User Profile By user Code {}", e.toString());
            throw new ServiceException("Error Find User Profile By User Code." + e.getMessage());
        }

        if (userProfile == null) {
            log.info("User profile not found with code {}", userCode);
            throw new ServiceException("User profile not found with code " + userCode);
        }

        if (Strings.isNotBlank(userProfile.getLastPhotoUrl())) {
            cdnImage.purgeImage(userProfile.getLastPhotoUrl());
        }

        String path = "profile/" + userCode + "/verificationPicture-" + System.currentTimeMillis() + "." + extension;
        try {
            uploadImage.saveImage(picture.getBytes(), path);
        } catch (IOException e) {
            throw new ServiceException("Picture upload failed. " + e.getMessage());
        }

        userProfile.setLastPhotoUrl(path);
        userProfileDAO.save(userProfile);
        SecurityServiceImpl.refreshSecurityContext(userProfile);

        return cdnImage.getUrl(path);

    }

    @Override
    public boolean updateMemberStatus(String publicId) throws ServiceException {

        UserProfile userProfile = getProfileByCustomerId(publicId);

        if (userProfile == null) {
            log.warn("User Not Found with publicId {}", publicId);
            throw new ServiceException("User Not Found with publicId : " + publicId);
        }
        userProfile.setIsVerifiedUser(true);
        userProfileDAO.save(userProfile);
        SecurityServiceImpl.refreshSecurityContext(userProfile);

        return true;
    }

    @Override
    public ResponseOcrImageDTO uploadIdentificationCard(
            String fileName, byte[] pictureBinary,
            OAuth2Authentication authentication
    ) throws ServiceException {

        String extension = uploadImage.getFileExtension(fileName, allowedFileExtensions);

        String pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSSSS";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
        String date = simpleDateFormat.format(new Date());

        User user = new User();
        String fileNameRequest;
        if (authentication.getPrincipal() == null) {
            user.setId(null);
            fileNameRequest = date;
        } else {
            fileNameRequest = user.getCode() + "-" + date + "." + extension;
        }

        UploadImageAwsS3 uploadImageAwsS3 = new UploadImageAwsS3();
        RequestUploadImage requestUploadImage = new RequestUploadImage(
                pictureBinary,
                null,
                fileNameRequest,
                "AKIAU4RNGSWE4CMPPZ6M",
                "J2Mi2nsYBBYrQFmFUNWFP/eEyJslleT299cPYwgN",
                bucketName,
                "ap-southeast-1"
        );
        ResponseUploadImage responseUploadImage = uploadImageAwsS3.saveImage(requestUploadImage);
        if (responseUploadImage == null) throw new ServiceException("We failed to upload file");

        ExtractEktpId extractEktpId = new ExtractEktpId();
        RequestExtractEktp requestExtractEktp = new RequestExtractEktp("AKIAU4RNGSWE4CMPPZ6M", "J2Mi2nsYBBYrQFmFUNWFP" +
                "/eEyJslleT299cPYwgN", responseUploadImage.getFullPath(), bucketName);
        MinimunIndonesianEktp extractResult = extractEktpId.extractEktp(requestExtractEktp);

        OcrImage newOcrImage = OcrImage.builder()
                                       .publicUserId(user.getId())
                                       .bucketName(bucketName)
                                       .filePath(responseUploadImage.getFullPath())
                                       .result(new Gson().toJson(extractResult))
                                       .build();

        if (user.getId() == null) {
            newOcrImage.setCreatedBy(0L);
        } else {
            newOcrImage.setCreatedBy(user.getId());
        }
        ocrImageDAO.save(newOcrImage);

        return ocrImageConverter
                .eKtpUrl(responseUploadImage.getFullPath())
                .convert(extractResult);
    }

    @Override
    public String setProfilePicture(String fileName, byte[] pictureBinary, String userCode) throws ServiceException {
        String extension = uploadImage.getFileExtension(fileName, allowedFileExtensions);

        UserProfile userProfile;
        try {
            userProfile = userProfileDAO.findByUser_Code(userCode);

            if (Strings.isNotBlank(userProfile.getPhotoUrl())) {
                cdnImage.purgeImage(userProfile.getPhotoUrl());
            }
            String path = "profile/" + userCode + "/profilePicture-" + System.currentTimeMillis() + "." + extension;
            userProfile.setPhotoUrl(path);
            cdnImage.putImage(pictureBinary, path);

            userProfileDAO.save(userProfile);
            SecurityServiceImpl.refreshSecurityContext(userProfile);

            return cdnImage.getUrl(path);
        } catch (DAOException e) {
            log.error("Error Find User Profile By user Code {}", e.toString());
            throw new ServiceException("Error Find User Profile By User Code." + e.getMessage());
        }
    }

    @Override
    public boolean deleteProfilePicture(String userCode) throws ServiceException {
        UserProfile userProfile;
        try {
            userProfile = userProfileDAO.findByUser_Code(userCode);
            cdnImage.purgeImage(userProfile.getPhotoUrl());
            userProfile.setPhotoUrl(null);
            userProfileDAO.save(userProfile);
            SecurityServiceImpl.refreshSecurityContext(userProfile);

            return true;
        } catch (DAOException e) {
            log.error("Error Find User Profile By user Code {}", e.toString());
            throw new ServiceException("Error Find User Profile By User Code." + e.getMessage());
        }
    }

    @Override
    public String setPicture(String fileName, byte[] pictureBinary) throws ServiceException {
        String extension = null;
        if (fileName.indexOf(".") > 0) {
            extension = fileName.substring(fileName.lastIndexOf(".") + 1);
        }
        if (Strings.isBlank(extension)) {
            throw new ServiceException("Can't determine extension for file " + fileName);
        }

        String path = "banner/image-" + System.currentTimeMillis() + "." + extension;
        cdnImage.putImage(pictureBinary, path);

        return cdnImage.getUrl(path);
    }

    @PostConstruct
    @Override
    public void initService() {
        scaffoldingDAO = userProfileDAO;
    }

    @Override
    public UserProfile findProfileByPhoneNumber(String phoneNumber) throws ServiceException {
        UserProfile userProfile;
        try {
            userProfile = userProfileDAO.findByMobilePhoneNumber(phoneNumber);
        } catch (DAOException e) {
            log.error("Error Find User Profile By mobile phone number {}", e.toString());
            throw new ServiceException("Error Find User Profile By mobile phone number." + e.getMessage());
        }
        return userProfile;
    }

    @Override
    public List<UserProfile> findProfileByFirebaseToken(String firebaseToken) throws ServiceException {
        List<UserProfile> userProfile;
        try {
            userProfile = userProfileDAO.findByFirebaseToken(firebaseToken);
        } catch (DAOException e) {
            log.error("Error Find User Profile By Firebase Token {}", e.toString());
            throw new ServiceException("Error Find User Profile By Firebase Token." + e.getMessage());
        }
        return userProfile;
    }

    @Override
    public User findByExtendedId(Integer extendedId) throws ServiceException {
        User user;
        try {
            user = userDAO.findByExtendedId(extendedId);
        } catch (DAOException e) {
            throw new ServiceException("Error Find User By Extended Id." + e.getMessage());
        }
        return user;
    }

    @Override
    public User setExtendedData(UserProfile userProfile, Integer extendedId, String extendedUsername) throws ServiceException {
        User user = userProfile.getUser();
        user.setExtendedId(extendedId);
        user.setExtendedUsername(extendedUsername);
        userDAO.save(user);
        return user;
    }

    @Override
    public List<UserProfile> findProfileByRole(EUserRole roleIds) throws ServiceException {
        List<UserProfile> userProfile;
        List<UserRole> userRoles;
        List<Long> roleId = new ArrayList<>();
        try {
            userRoles = userRoleDAO.findAllByRoleId(roleIds);
            userRoles.forEach(x -> roleId.add(x.getUserId()));
            userProfile = userProfileDAO.findAllByUserIdIn(roleId);
        } catch (DAOException e) {
            log.error("Error Find User Profile By Role {}", e.toString());
            throw new ServiceException("Error Find User Profile By Role ." + e.getMessage());
        }
        return userProfile;
    }

}
*/