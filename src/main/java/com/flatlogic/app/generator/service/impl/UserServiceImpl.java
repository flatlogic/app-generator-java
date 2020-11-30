package com.flatlogic.app.generator.service.impl;

import com.flatlogic.app.generator.controller.request.UserRequest;
import com.flatlogic.app.generator.entity.File;
import com.flatlogic.app.generator.entity.Product;
import com.flatlogic.app.generator.entity.User;
import com.flatlogic.app.generator.exception.NoSuchEntityException;
import com.flatlogic.app.generator.exception.UsernameNotFoundException;
import com.flatlogic.app.generator.exception.ValidationException;
import com.flatlogic.app.generator.repository.ProductRepository;
import com.flatlogic.app.generator.repository.UserRepository;
import com.flatlogic.app.generator.service.UserService;
import com.flatlogic.app.generator.type.BelongsToColumnType;
import com.flatlogic.app.generator.type.BelongsToType;
import com.flatlogic.app.generator.type.OrderType;
import com.flatlogic.app.generator.type.RoleType;
import com.flatlogic.app.generator.util.Constants;
import com.flatlogic.app.generator.util.MessageCodeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * UserService service.
 */
@Service
@Transactional(readOnly = true)
public class UserServiceImpl implements UserService {

    /**
     * UserRepository instance.
     */
    @Autowired
    private UserRepository userRepository;

    /**
     * ProductRepository instance.
     */
    @Autowired
    private ProductRepository productRepository;

    /**
     * PasswordEncoder instance.
     */
    @Autowired
    private PasswordEncoder passwordEncoder;

    /**
     * MessageCodeUtil instance.
     */
    @Autowired
    private MessageCodeUtil messageCodeUtil;

    /**
     * Get users.
     *
     * @param offset  Page offset
     * @param limit   Limit of Records
     * @param orderBy Order by default createdAt
     * @return List of Users
     */
    @Override
    public List<User> getUsers(final Integer offset, final Integer limit, final String orderBy) {
        String[] orderParam = !ObjectUtils.isEmpty(orderBy) ? orderBy.split("_") : null;
        if (limit != null && offset != null) {
            Sort sort = Sort.by(Constants.CREATED_AT).descending();
            if (orderParam != null) {
                if (Objects.equals(OrderType.ASC.name(), orderParam[1])) {
                    sort = Sort.by(orderParam[0]).ascending();
                } else {
                    sort = Sort.by(orderParam[0]).descending();
                }
            }
            Pageable sortedByCreatedAtDesc = PageRequest.of(offset, limit, sort);
            return userRepository.findUsersByDeletedAtIsNull(sortedByCreatedAtDesc);
        } else {
            return userRepository.findUsersByDeletedAtIsNull(Sort.by(Constants.CREATED_AT).descending());
        }
    }

    /**
     * Get users.
     *
     * @param query String for search
     * @param limit Limit of Records
     * @return List of Users
     */
    @Override
    public List<User> getUsers(final String query, final Integer limit) {
        if (!ObjectUtils.isEmpty(query)) {
            return userRepository.findUsersByEmailLikeAndDeletedAtIsNullOrderByEmailAsc(query,
                    PageRequest.of(0, limit, Sort.by(Constants.EMAIL).ascending()));
        } else {
            return userRepository.findUsersByDeletedAtIsNull(PageRequest
                    .of(0, limit, Sort.by(Constants.EMAIL).ascending()));
        }
    }

    /**
     * Get user by email.
     *
     * @param email User email
     * @return User
     */
    @Override
    public User getUserByEmail(final String email) {
        return Optional.ofNullable(userRepository.findByEmail(email)).orElseThrow(() ->
                new UsernameNotFoundException(messageCodeUtil.getFullErrorMessageByBundleCode(
                        Constants.ERROR_MSG_USER_BY_EMAIL_NOT_FOUND, new Object[]{email})));
    }

    /**
     * Get user by id.
     *
     * @param id User Id
     * @return User
     */
    @Override
    public User getUserById(final UUID id) {
        return userRepository.findById(id).orElse(null);
    }

    /**
     * Save user.
     *
     * @param userRequest User data
     * @param username    User name
     * @return User
     */
    @Override
    @Transactional
    public User saveUser(final UserRequest userRequest, final String username) {
        User createdBy = userRepository.findByEmail(username);
        User user = new User();
        setFields(userRequest, user);
        user.setCreatedBy(createdBy);
        userRepository.save(user);
        setEntries(userRequest, user, createdBy);
        return user;
    }

    /**
     * Update user.
     *
     * @param id          User Id
     * @param userRequest User data
     * @param username    User name
     * @return User
     */
    @Override
    @Transactional
    public User updateUser(final UUID id, final UserRequest userRequest, final String username) {
        User user = getUserById(id);
        if (user == null) {
            throw new NoSuchEntityException(messageCodeUtil.getFullErrorMessageByBundleCode(
                    Constants.ERROR_MSG_USER_BY_ID_NOT_FOUND, new Object[]{id}));
        }
        User updatedBy = userRepository.findByEmail(username);
        setFields(userRequest, user);
        setEntries(userRequest, user, updatedBy);
        user.setUpdatedBy(updatedBy);
        return user;
    }

    /**
     * Update user password.
     *
     * @param username        User name
     * @param currentPassword Current user password
     * @param newPassword     New user password
     */
    @Override
    @Transactional
    public void updateUserPassword(final String username, final String currentPassword, final String newPassword) {
        User user = getUserByEmail(username);
        if (Objects.equals(passwordEncoder.encode(currentPassword), user.getPassword())) {
            throw new ValidationException(messageCodeUtil.getFullErrorMessageByBundleCode(
                    Constants.ERROR_MSG_AUTH_WRONG_PASSWORD));
        }
        if (Objects.equals(passwordEncoder.encode(newPassword), user.getPassword())) {
            throw new ValidationException(messageCodeUtil.getFullErrorMessageByBundleCode(
                    Constants.ERROR_MSG_AUTH_PASSWORD_UPDATE_SAME_PASSWORD));
        }
        user.setPassword(passwordEncoder.encode(newPassword));
        user.setUpdatedBy(user);
    }

    /**
     * Delete user.
     *
     * @param id User Id
     */
    @Override
    @Transactional
    public void deleteUser(final UUID id) {
        if (!userRepository.existsById(id)) {
            throw new NoSuchEntityException(messageCodeUtil.getFullErrorMessageByBundleCode(
                    Constants.ERROR_MSG_USER_BY_ID_NOT_FOUND, new Object[]{id}));
        }
        userRepository.updateDeletedAt(id, new Date());
    }

    private void setFields(final UserRequest userRequest, final User user) {
        user.setFirstName(userRequest.getFirstName());
        user.setLastName(userRequest.getLastName());
        user.setPhoneNumber(userRequest.getPhoneNumber());
        user.setEmail(userRequest.getEmail());
        Optional.ofNullable(userRequest.getRole()).ifPresent(role -> user.setRole(RoleType.valueOfRole(role)));
        user.setDisabled(userRequest.isDisabled());
        user.setPassword(userRequest.getPassword());
        user.setEmailVerified(userRequest.isEmailVerified());
        user.setEmailVerificationToken(userRequest.getEmailVerificationToken());
        user.setEmailVerificationTokenExpiresAt(userRequest.getEmailVerificationTokenExpiresAt());
        user.setPasswordResetToken(userRequest.getPasswordResetToken());
        user.setPasswordResetTokenExpiresAt(userRequest.getPasswordResetTokenExpiresAt());
        user.setProvider(userRequest.getProvider());
        user.setImportHash(userRequest.getImportHash());
    }

    private void setEntries(final UserRequest userRequest, final User user, final User modifiedBy) {
        Optional.ofNullable(userRequest.getProductIds()).ifPresent(productIds -> {
            final List<Product> products = user.getProducts();
            products.clear();
            productIds.forEach(productId -> products.add(productRepository.getOne(productId)));
        });
        Optional.ofNullable(userRequest.getFileRequests()).ifPresent(fileRequests -> {
            final List<File> files = user.getFiles();
            Map<UUID, File> mapFiles = files.stream().collect(Collectors.toMap(File::getId, file -> file));
            files.clear();
            fileRequests.forEach(fileRequest -> {
                File file = null;
                if (fileRequest.isNew()) {
                    file = new File();
                    file.setBelongsTo(BelongsToType.USERS.getType());
                    file.setBelongsToId(user.getId());
                    file.setBelongsToColumn(BelongsToColumnType.AVATAR.getType());
                    file.setName(fileRequest.getName());
                    file.setPrivateUrl(fileRequest.getPrivateUrl());
                    file.setPublicUrl(fileRequest.getPublicUrl());
                    file.setSizeInBytes(fileRequest.getSizeInBytes());
                    file.setCreatedBy(modifiedBy);
                } else {
                    file = mapFiles.remove(fileRequest.getId());
                    file.setUpdatedBy(modifiedBy);
                }
                files.add(file);
            });
            mapFiles.forEach((key, value) -> {
                File file = value;
                file.setDeletedAt(new Date());
                files.add(file);
            });
        });
    }

}
