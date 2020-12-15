package com.flatlogic.app.generator.controller;

import com.flatlogic.app.generator.controller.data.AutocompleteData;
import com.flatlogic.app.generator.controller.request.RequestData;
import com.flatlogic.app.generator.controller.request.GetModelAttribute;
import com.flatlogic.app.generator.controller.request.UsersRequest;
import com.flatlogic.app.generator.controller.data.RowsData;
import com.flatlogic.app.generator.dto.UsersDto;
import com.flatlogic.app.generator.entity.Users;
import com.flatlogic.app.generator.service.UsersService;
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
 * UsersController REST controller.
 */
@RestController
@RequestMapping("users")
public class UsersController {

    /**
     * Logger constant.
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(UsersController.class);

    /**
     * UsersService instance.
     */
    @Autowired
    private UsersService usersService;

    /**
     * DefaultConversionService instance.
     */
    @Autowired
    private DefaultConversionService defaultConversionService;

    /**
     * Get users.
     *
     * @param modelAttribute GetModelAttribute
     * @return Users RowsWrapper
     */
    @GetMapping
    public ResponseEntity<RowsData<UsersDto>> getUsers(@ModelAttribute GetModelAttribute modelAttribute) {
        LOGGER.info("Get users.");
        RowsData<UsersDto> rowsData = new RowsData<>();
        List<Users> users = usersService.getUsers(modelAttribute.getOffset(),
                modelAttribute.getLimit(), modelAttribute.getOrderBy());
        List<UsersDto> usersDtos = users.stream().map(__users -> defaultConversionService.
                convert(__users, UsersDto.class)).collect(Collectors.toList());
        rowsData.setRows(usersDtos);
        rowsData.setCount(usersDtos.size());
        return new ResponseEntity<>(rowsData, HttpStatus.OK);
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
        List<Users> users = usersService.getUsers(query, limit);
        List<AutocompleteData> wrappers = users.stream().map(__users ->
                new AutocompleteData(__users.getId(), __users.getImportHash())).collect(Collectors.toList());
        return new ResponseEntity<>(wrappers, HttpStatus.OK);
    }

    /**
     * Get users by id.
     *
     * @param id Users Id
     * @return Users
     */
    @GetMapping("{id}")
    public ResponseEntity<UsersDto> getUsersById(@PathVariable UUID id) {
        LOGGER.info("Get users by id.");
        return Optional.ofNullable(usersService.getUsersById(id))
                .map(users -> new ResponseEntity<>(defaultConversionService
                        .convert(users, UsersDto.class), HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.BAD_REQUEST));
    }

    /**
     * Save users.
     *
     * @param requestData RequestData
     * @param userDetails UserDetails
     * @return Users
     */
    @PostMapping
    public ResponseEntity<UsersDto> saveUsers(@RequestBody RequestData<UsersRequest> requestData,
                                                  @AuthenticationPrincipal UserDetails userDetails) {
        LOGGER.info("Save users.");
        Users users = usersService.saveUsers(requestData.getData(), userDetails.getUsername());
        return new ResponseEntity<>(defaultConversionService.convert(users, UsersDto.class), HttpStatus.OK);
    }

    /**
     * Update users.
     *
     * @param id          Users id
     * @param requestData RequestData
     * @param userDetails UserDetails
     * @return Users
     */
    @PutMapping("{id}")
    public ResponseEntity<UsersDto> updateUsers(@PathVariable UUID id,
                                                    @RequestBody RequestData<UsersRequest> requestData,
                                                    @AuthenticationPrincipal UserDetails userDetails) {
        LOGGER.info("Update users.");
        Users users = usersService.updateUsers(id, requestData.getData(), userDetails.getUsername());
        return new ResponseEntity<>(defaultConversionService.convert(users, UsersDto.class), HttpStatus.OK);
    }

    /**
     * Delete users.
     *
     * @param id Users id
     * @return Void
     */
    @DeleteMapping("{id}")
    public ResponseEntity<Void> deleteUsers(@PathVariable UUID id) {
        LOGGER.info("Delete users.");
        usersService.deleteUsers(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

}
