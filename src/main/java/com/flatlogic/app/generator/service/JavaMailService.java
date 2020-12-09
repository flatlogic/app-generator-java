package com.flatlogic.app.generator.service;

import java.util.UUID;

public interface JavaMailService {

    void sendEmailForCreateUser(String email, UUID token);

    void sendEmailForUpdateUserPasswordResetTokenAnd(String email, UUID token);

}
