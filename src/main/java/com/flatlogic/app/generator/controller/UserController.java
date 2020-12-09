package com.flatlogic.app.generator.controller;

import com.flatlogic.app.generator.controller.data.AutocompleteData;
import com.flatlogic.app.generator.controller.request.RequestData;
import com.flatlogic.app.generator.controller.request.GetModelAttribute;
import com.flatlogic.app.generator.controller.data.RowsData;
import com.flatlogic.app.generator.controller.request.UserRequest;
import com.flatlogic.app.generator.dto.UserDto;
import com.flatlogic.app.generator.entity.User;
import com.flatlogic.app.generator.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.support.DefaultConversionService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * UserController REST controller.
 */
@RestController
@RequestMapping("users")
public class UserController {

    /**
     * Logger constant.
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(UserController.class);

    /**
     * UserService instance.
     */
    @Autowired
    private UserService userService;

    /**
     * DefaultConversionService instance.
     */
    @Autowired
    private DefaultConversionService defaultConversionService;

    /**
     * Get users.
     *
     * @param modelAttribute GetModelAttribute
     * @return User RowsWrapper
     */
    @GetMapping
    public ResponseEntity<RowsData<UserDto>> getUsers(@ModelAttribute GetModelAttribute modelAttribute) {
        LOGGER.info("Get users.");
        RowsData<UserDto> rowsData = new RowsData<>();
        List<User> users = userService.getUsers(modelAttribute.getOffset(),
                modelAttribute.getLimit(), modelAttribute.getOrderBy());
        List<UserDto> userDtos = users.stream().map(user -> defaultConversionService.convert(user,
                UserDto.class)).collect(Collectors.toList());
        rowsData.setRows(userDtos);
        rowsData.setCount(userDtos.size());
        return ResponseEntity.ok(rowsData);
    }

    /**
     * Get users.
     *
     * @param query String for search
     * @param limit Limit of Records
     * @return List of Users
     */
    @GetMapping("autocomplete")
    public ResponseEntity<List<AutocompleteData>> getUsersAutocomplete(@RequestParam String query,
                                                                       @RequestParam Integer limit) {
        LOGGER.info("Get users (autocomplete).");
        List<User> users = userService.getUsers(query, limit);
        List<AutocompleteData> wrappers = users.stream().map(user ->
                new AutocompleteData(user.getId(), user.getEmail())).collect(Collectors.toList());
        return ResponseEntity.ok(wrappers);
    }

    /**
     * Get user by id.
     *
     * @param id User Id
     * @return User
     */
    @GetMapping("{id}")
    public ResponseEntity<UserDto> getUserById(@PathVariable UUID id) {
        LOGGER.info("Get user by id.");
        return Optional.ofNullable(userService.getUserById(id))
                .map(user -> new ResponseEntity<>(defaultConversionService.convert(user,
                        UserDto.class), HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.BAD_REQUEST));
    }

    /**
     * Save user.
     *
     * @param requestData RequestData
     * @param userDetails UserDetails
     * @return User
     */
    @PostMapping
    public ResponseEntity<UserDto> saveUser(@RequestBody RequestData<UserRequest> requestData,
                                            @AuthenticationPrincipal UserDetails userDetails) {
        LOGGER.info("Save user.");
        User user = userService.saveUser(requestData.getData(), userDetails.getUsername());
        return new ResponseEntity<>(defaultConversionService.convert(user, UserDto.class), HttpStatus.OK);
    }

    /**
     * Update user.
     *
     * @param id          User id
     * @param requestData RequestData
     * @param userDetails UserDetails
     * @return User
     */
    @PutMapping("{id}")
    public ResponseEntity<UserDto> updateUser(@PathVariable UUID id, @RequestBody RequestData<UserRequest> requestData,
                                              @AuthenticationPrincipal UserDetails userDetails) {
        LOGGER.info("Update user.");
        User user = userService.updateUser(id, requestData.getData(), userDetails.getUsername());
        return new ResponseEntity<>(defaultConversionService.convert(user, UserDto.class), HttpStatus.OK);
    }

    /**
     * Delete user.
     *
     * @param id User id
     * @return Void
     */
    @DeleteMapping("{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable UUID id) {
        LOGGER.info("Delete user.");
        userService.deleteUser(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

}
