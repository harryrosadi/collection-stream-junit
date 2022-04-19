package org.example.kerjaBelajar.profil;

/**
 import com.peentar.kf.oa.util.core.exception.DAOException;
 import com.telkomsigma.kf.oa.shared.dao.base.IScaffoldingDAO;
 import com.telkomsigma.kf.oa.shared.data.model.mobile.security.UserProfile;
 import org.springframework.data.domain.Page;
 import org.springframework.data.domain.Pageable;
 import org.springframework.data.domain.Slice;
 import org.springframework.data.jpa.repository.EntityGraph;
 import org.springframework.data.jpa.repository.Modifying;
 import org.springframework.data.jpa.repository.Query;
 import org.springframework.data.repository.query.Param;

 import java.util.List;
 import java.util.Optional;

 * @author : <a href="mailto:fauzi.knightmaster.achmad@gmail.com">Achmad Fauzi</a>
 * @since : 2019-07-11

public interface IUserProfileDAO extends IScaffoldingDAO<UserProfile> {

    @EntityGraph(attributePaths = {"user"})
    UserProfile findByUser_Id(Long p_UserId) throws DAOException;

    List<UserProfile> findByUser_IdIn(Long[] userIds) throws DAOException;

    @EntityGraph(attributePaths = {"user"})
    UserProfile findByUser_Code(@Param("userCode") String p_UserCode) throws DAOException;

    @EntityGraph(attributePaths = {"user"})
    UserProfile findByUser_CodeOrUser_Id(String p_UserCode, Long p_UserId) throws DAOException;

    @EntityGraph(attributePaths = {"user"})
    @Query("from UserProfile up where not up.code = :userCode and (up.mobilePhoneNumber = :phone or up.email = " +
            ":email) ")
    UserProfile findByUserCodeNotAndMobilePhoneNumberOrEmail(String userCode, String phone, String email) throws
            DAOException;

    @EntityGraph(attributePaths = {"user"})
    @Query("from UserProfile up where up.code = :userCode or up.mobilePhoneNumber = :phone or up.email = :email ")
    List<UserProfile> findByUserCodeOrMobilePhoneNumberOrEmail(String userCode, String phone, String email) throws
            DAOException;

    Boolean existsByEmail(String email);

    @EntityGraph(attributePaths = {"user"})
    @Query("from UserProfile up where (up.mobilePhoneNumber = :phone or up.email = :email) ")
    List<UserProfile> findAllByMobilePhoneNumberOrEmail(String phone, String email) throws DAOException;

    @EntityGraph(attributePaths = {"user"})
    @Query("from UserProfile up where up.mobilePhoneNumber = :phone ")
    UserProfile findOneByMobilePhoneNumber(String phone) throws DAOException;

    @EntityGraph(attributePaths = {"user"})
    @Query("from UserProfile up where upper(up.user.name) like %:name% ")
    Page<UserProfile> findAllByName(@Param("name") String name, Pageable pageable) throws DAOException;

    @EntityGraph(attributePaths = {"user"})
    @Query("from UserProfile up where upper(up.code) like %:code% or up.mobilePhoneNumber like %:phone% or upper(up" +
            ".email) like %:email% ")
    List<UserProfile> findByUserCodeAndMobilePhoneNumber(
            @Param("code") String code, @Param("phone") String phone,
            @Param("email") String email
    ) throws DAOException;

    @EntityGraph(attributePaths = {"user"})
    @Query("from UserProfile up where up.code like %:code% or up.mobilePhoneNumber like %:phone% or upper(up.name) " +
            "like %:name% or upper(up.email) like %:email% ")
    Page<UserProfile> findByUserCodeAndMobilePhoneNumber(
            @Param("code") String code, @Param("phone") String phone,
            @Param("name") String name, @Param("email") String email,
            Pageable pageable
    ) throws DAOException;

    @EntityGraph(attributePaths = {"user"})
    @Query("from UserProfile up where up.code like %:code% " +
            "or up.mobilePhoneNumber like %:phone% or upper(up.name) like %:name% or upper(up.email) like %:email% ")
    Slice<UserProfile> findByUserCodeAndMobilePhoneNumberV2(
            @Param("code") String code,
            @Param("phone") String phone,
            @Param("name") String name,
            @Param("email") String email,
            Pageable pageable
    ) throws DAOException;

    @EntityGraph(attributePaths = {"user"})
    @Query("from UserProfile up where upper(up.user.publicId) = :publicId ")
    UserProfile findByCustomerId(@Param("publicId") String publicId) throws DAOException;

    @EntityGraph(attributePaths = {"user"})
    @Query("from UserProfile up where upper(up.user.publicId) = :publicId or upper(up.user.code) = " +
            ":userCode ")
    UserProfile findByCustomerIdOrUserCode(@Param("publicId") String publicId, @Param("userCode") String userCode)
            throws
            DAOException;

    @EntityGraph(attributePaths = {"user"})
    @Query("from UserProfile up where up.isVerifiedUser = :isVerificationUser ")
    Page<UserProfile> findAllByVerificationUser(
            @Param("isVerificationUser") boolean isVerificationUser,
            Pageable pageable
    ) throws DAOException;

    @EntityGraph(attributePaths = {"user"})
    UserProfile findByMobilePhoneNumber(@Param("mobilePhoneNumber") String mobilePhoneNumber) throws DAOException;

    @EntityGraph(attributePaths = {"user"})
    List<UserProfile> findByFirebaseToken(@Param("firebaseToken") String firebaseToken) throws DAOException;

    Optional<UserProfile> findByEmail(String email);

    @Modifying
    @Query("update UserProfile up set up.firebaseToken=:token where up.user.id=:userId")
    void setFirebaseToken(String token, long userId) throws DAOException;

    @EntityGraph(attributePaths = {"user"})
    List<UserProfile> findAllByUserIdIn(List<Long> userId) throws DAOException;

}
 **/