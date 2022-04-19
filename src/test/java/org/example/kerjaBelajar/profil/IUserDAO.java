package org.example.kerjaBelajar.profil;

/**
import com.peentar.kf.oa.util.core.exception.DAOException;
import com.telkomsigma.kf.oa.shared.dao.base.IScaffoldingDAO;
import com.telkomsigma.kf.oa.shared.data.model.mobile.security.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Date;
import java.util.List;

public interface IUserDAO extends IScaffoldingDAO<User> {
    @EntityGraph(attributePaths = {"roles"})
    @Query("select u from User u where ((:username is null or u.userProfile.code = :username) " +
            "or (:email is null or upper(u.userProfile.email) = :email) " +
            "or (:mobilePhoneNumber is null or u.userProfile.mobilePhoneNumber = :mobilePhoneNumber)) and u.status = " +
            ":status ")
    User findUserByCodeOrUserProfile_EmailOrUserProfile_MobilePhoneNumberAndStatus(
            @Param("username") String username,
            @Param("email") String email,
            @Param("mobilePhoneNumber") String mobilePhoneNumber,
            @Param("status") boolean status
    )
            throws DAOException;

    @EntityGraph(attributePaths = {"roles", "userProfile", "userProfile.job", "userProfile.maritalStatus"})
    @Query("select u from User u where (u.userProfile.code = :username " +
            "or upper(u.userProfile.email) = :email " +
            "or u.userProfile.mobilePhoneNumber = :mobilePhoneNumber) ")
    User findUserByCodeOrUserProfile_EmailOrUserProfile_MobilePhoneNumber(
            String username,
            String email,
            String mobilePhoneNumber
    )
            throws DAOException;

    @EntityGraph(attributePaths = {"roles", "userProfile", "userProfile.job", "userProfile.maritalStatus"})
    @Query("select u from User u where (upper(u.userProfile.email) = :email " +
            "or u.userProfile.mobilePhoneNumber = :mobilePhoneNumber) ")
    User findUserByUserProfile_EmailOrUserProfile_MobilePhoneNumber(
            String email,
            String mobilePhoneNumber
    )
            throws DAOException;

    @EntityGraph(attributePaths = {"roles", "userProfile", "userProfile.job", "userProfile.maritalStatus"})
    @Query("select u from User u where u.code = :username")
    User findUserByCode(String username)
            throws DAOException;

    @EntityGraph(attributePaths = {"roles", "userProfile", "userProfile.job", "userProfile.maritalStatus"})
    @Query("select u from User u where upper(u.userProfile.email) = :email")
    User findUserByEmail(String email)
            throws DAOException;

    @EntityGraph(attributePaths = {"roles", "userProfile", "userProfile.job", "userProfile.maritalStatus"})
    @Query("select u from User u where u.userProfile.mobilePhoneNumber = :mobilePhoneNumber")
    User findUserByPhoneNumber(String mobilePhoneNumber)
            throws DAOException;

    User findByPassword(String password) throws DAOException;

    Page<User> findTop500ByPasswordNotLike(String password, Pageable pageable) throws DAOException;

    @EntityGraph(attributePaths = {"userProfile", "userProfile.job", "userProfile.maritalStatus"})
    List<User> findTop500ByPublicIdIsNullAndIdAfter(long id) throws DAOException;

    @Modifying(clearAutomatically = true)
    @Query("update User u set u.lastLogin=:lastLogin, u.loginStatus=:status where u.code=:code")
    void setLoginStatus(
            @Param("lastLogin") Date lastLogin, @Param("status") boolean loginStatus,
            @Param("code") String userCode
    );

    @Query(value = "SELECT max(u.id) FROM User u")
    Long findLastId() throws DAOException;

    @EntityGraph(attributePaths = {"roles", "userProfile", "userProfile.job", "userProfile.maritalStatus"})
    User findByExtendedId(Integer extendedId)
            throws DAOException;

    @EntityGraph(attributePaths = {"roles", "userProfile", "userProfile.job", "userProfile.maritalStatus"})
    User findByExtendedUsername(String extendedUsername)
            throws DAOException;

}
 */
