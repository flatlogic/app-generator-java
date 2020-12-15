
package com.flatlogic.app.generator.service.impl;

import com.flatlogic.app.generator.controller.request.UsersRequest;

import com.flatlogic.app.generator.entity.Products;

import com.flatlogic.app.generator.entity.Categories;

import com.flatlogic.app.generator.entity.Orders;

import com.flatlogic.app.generator.entity.Users;

import com.flatlogic.app.generator.entity.File;
import com.flatlogic.app.generator.exception.NoSuchEntityException;

import com.flatlogic.app.generator.repository.ProductsRepository;

import com.flatlogic.app.generator.repository.CategoriesRepository;

import com.flatlogic.app.generator.repository.OrdersRepository;

import com.flatlogic.app.generator.repository.UsersRepository;

import com.flatlogic.app.generator.service.UsersService;
import com.flatlogic.app.generator.util.Constants;
import com.flatlogic.app.generator.util.MessageCodeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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
 * UsersService service.
 */
@Service
@Transactional(readOnly = true)
public class UsersServiceImpl implements UsersService {


    /**
     * ProductsRepository instance.
     */
    @Autowired
    private ProductsRepository productsRepository;

    /**
     * CategoriesRepository instance.
     */
    @Autowired
    private CategoriesRepository categoriesRepository;

    /**
     * OrdersRepository instance.
     */
    @Autowired
    private OrdersRepository ordersRepository;

    /**
     * UsersRepository instance.
     */
    @Autowired
    private UsersRepository usersRepository;


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
    public List<Users> getUsers(final Integer offset, final Integer limit, final String orderBy) {
        String[] orderParam = !ObjectUtils.isEmpty(orderBy) ? orderBy.split("_") : null;
        if (limit != null && offset != null) {
            Sort sort = Sort.by(Constants.CREATED_AT).descending();
            if (orderParam != null) {
                if (Objects.equals("ASC", orderParam[1])) {
                    sort = Sort.by(orderParam[0]).ascending();
                } else {
                    sort = Sort.by(orderParam[0]).descending();
                }
            }
            Pageable sortedByCreatedAtDesc = PageRequest.of(offset, limit, sort);
            return usersRepository.findUsersByDeletedAtIsNull(sortedByCreatedAtDesc);
        } else {
            return usersRepository.findUsersByDeletedAtIsNull(Sort.by(Constants.CREATED_AT).descending());
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
    public List<Users> getUsers(final String query, final Integer limit) {
        if (!ObjectUtils.isEmpty(query)) {
            return usersRepository.findUsersByTitleLikeAndDeletedAtIsNullOrderByTitleAsc(query,
                    PageRequest.of(0, limit, Sort.by(Constants.TITLE).ascending()));
        } else {
            return usersRepository.findUsersByDeletedAtIsNull(PageRequest
                    .of(0, limit, Sort.by(Constants.TITLE).ascending()));
        }
    }

    /**
     * Get users by id.
     *
     * @param id Users Id
     * @return Users
     */
    @Override
    public Users getUsersById(final UUID id) {
        return usersRepository.findById(id).orElse(null);
    }

    /**
     * Save users.
     *
     * @param usersRequest UsersData
     * @param username       Users name
     * @return Users
     */
    @Override
    @Transactional
    public Users saveUsers(final UsersRequest usersRequest, final String username) {
        Users createdBy = usersRepository.findByEmail(username);
        Users users = new Users();
        setFields(usersRequest, users);
        users.setCreatedBy(createdBy);
        users = usersRepository.save(users);
        setEntries(usersRequest, users, createdBy);
        return users;
    }

    /**
     * Update users.
     *
     * @param id             Users Id
     * @param usersRequest UsersData
     * @param username       Users name
     * @return Users
     */
    @Override
    @Transactional
    public Users updateUsers(final UUID id, final UsersRequest usersRequest, final String username) {
        Users users = getUsersById(id);
        if (users == null) {
            throw new NoSuchEntityException(messageCodeUtil.getFullErrorMessageByBundleCode(
                    Constants.MSG_PRODUCT_BY_ID_NOT_FOUND, new Object[]{id}));
        }
        Users updatedBy = usersRepository.findByEmail(username);
        setFields(usersRequest, users);
        setEntries(usersRequest, users, updatedBy);
        users.setUpdatedBy(updatedBy);
        return users;
    }

    /**
     * Delete users.
     *
     * @param id Users Id
     */
    @Override
    @Transactional
    public void deleteUsers(final UUID id) {
        if (!usersRepository.existsById(id)) {
            throw new NoSuchEntityException(messageCodeUtil.getFullErrorMessageByBundleCode(
                    Constants.MSG_PRODUCT_BY_ID_NOT_FOUND, new Object[]{id}));
        }
        usersRepository.updateDeletedAt(id, new Date());
    }

    private void setFields(final UsersRequest usersRequest, final Users users) {

        users.setFirstName(usersRequest.getFirstName());
    
        users.setLastName(usersRequest.getLastName());
    
        users.setPhoneNumber(usersRequest.getPhoneNumber());
    
        users.setEmail(usersRequest.getEmail());
    
        users.setRole(usersRequest.getRole());
    
        users.setDisabled(usersRequest.isDisabled());
    
        users.setPassword(usersRequest.getPassword());
    
        users.setEmailVerified(usersRequest.isEmailVerified());
    
        users.setEmailVerificationToken(usersRequest.getEmailVerificationToken());
    
        users.setEmailVerificationTokenExpiresAt(usersRequest.getEmailVerificationTokenExpiresAt());
    
        users.setPasswordResetToken(usersRequest.getPasswordResetToken());
    
        users.setPasswordResetTokenExpiresAt(usersRequest.getPasswordResetTokenExpiresAt());
    
        users.setProvider(usersRequest.getProvider());
    

        users.setImportHash(usersRequest.getImportHash());
    }

    private void setEntries(final UsersRequest usersRequest, final Users users, final Users modifiedBy) {

        Optional.ofNullable(usersRequest.getWishlist()).ifPresent(wishlistIds -> {
            final List<Products> wishlist = users.getWishlist();
            wishlist.clear();
            wishlistIds.forEach(wishlistId -> wishlist.add(productsRepository.getOne(wishlistId)));
        });

        Optional.ofNullable(usersRequest.getAvatar()).ifPresent(fileRequests -> {
            final List<File> avatar = users.getAvatar();
            Map<UUID, File> mapFiles = avatar.stream().collect(Collectors.toMap(File::getId, file -> file));
            avatar.clear();
            fileRequests.forEach(fileRequest -> {
                File file = null;
                if (fileRequest.isNew()) {
                    file = new File();
                    file.setBelongsTo("users");
                    file.setBelongsToId(users.getId());
                    file.setBelongsToColumn("avatar");
                    file.setName(fileRequest.getName());
                    file.setPrivateUrl(fileRequest.getPrivateUrl());
                    file.setPublicUrl(fileRequest.getPublicUrl());
                    file.setSizeInBytes(fileRequest.getSizeInBytes());
                    file.setCreatedBy(modifiedBy);
                } else {
                    file = mapFiles.remove(fileRequest.getId());
                    file.setUpdatedBy(modifiedBy);
                }
                avatar.add(file);
            });
            mapFiles.forEach((key, value) -> {
                File file = value;
                file.setDeletedAt(new Date());
                avatar.add(file);
            });
        });

    }


    /**
     * Get user by email.
     *
     * @param email User email
     * @return User
     */
    @Override
    public User getUserByEmail(final String email) {
        return Optional.ofNullable(usersRepository.findByEmail(email)).orElseThrow(() ->
                new UsernameNotFoundException(messageCodeUtil.getFullErrorMessageByBundleCode(
                        Constants.ERROR_MSG_USER_BY_EMAIL_NOT_FOUND, new Object[]{email})));
    }

    /**
     * Update password reset token.
     *
     * @param email User email
     */
    @Override
    @Transactional
    public void updateUserPasswordResetTokenAndSendEmail(final String email) {
        UUID token = UUID.randomUUID();
        MimeMessage message = emailSender.createMimeMessage();
        try {
            MimeMessageHelper helper = new MimeMessageHelper(message, true, StandardCharsets.UTF_8.name());
            helper.setFrom(emailFrom);
            helper.setTo(email);
            helper.setSubject(messageCodeUtil.getFullErrorMessageByBundleCode(
                    Constants.MSG_AUTH_MAIL_RESET_PASSWORD_SUBJECT, new Object[]{APPLICATION}));
            String resetUrl = UriComponentsBuilder.fromUriString(frontendHost).pathSegment("#/password-reset")
                    .queryParam(Constants.TOKEN, token.toString()).build().toUriString();
            helper.setText(messageCodeUtil.getFullErrorMessageByBundleCode(Constants.MSG_AUTH_MAIL_RESET_PASSWORD_BODY,
                    new Object[]{APPLICATION, email, resetUrl, resetUrl, APPLICATION}), true);
            emailSender.send(message);
            usersRepository.updatePasswordResetToken(token.toString(), new Date(), email);
        } catch (MessagingException e) {
            throw new SendMailException(messageCodeUtil.getFullErrorMessageByBundleCode(
                    Constants.ERROR_MSG_USER_PASSWORD_RESET_SEND_MESSAGE));
        }
    }

    /**
     * Update password by passwordResetToken.
     *
     * @param token User token
     * @return User
     */
    @Override
    @Transactional
    public User updateUserPasswordByPasswordResetToken(final String token, final String password) {
        User user = Optional.ofNullable(usersRepository.findByPasswordResetToken(token)).orElseThrow(() ->
                new ValidationException(messageCodeUtil.getFullErrorMessageByBundleCode(
                        Constants.ERROR_MSG_USER_PASSWORD_RESET_OR_EXPIRED)));
        long different = (new Date().getTime() - user.getPasswordResetTokenExpiresAt().getTime()) / 1000 / 60;
        if (different > tokenExpiration) {
            throw new ValidationException(messageCodeUtil.getFullErrorMessageByBundleCode(
                    Constants.ERROR_MSG_USER_PASSWORD_RESET_OR_EXPIRED));
        }
        user.setPassword(passwordEncoder.encode(password));
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

}
