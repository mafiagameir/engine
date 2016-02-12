
/*
 * Copyright (C) 2015 mafiagame.ir
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 */

package co.mafiagame.persistence.api;

import co.mafiagame.common.domain.Account;
import co.mafiagame.common.domain.Audit;
import co.mafiagame.common.domain.InterfaceType;
import co.mafiagame.persistence.repository.AccountRepository;
import co.mafiagame.persistence.repository.AuditRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.UUID;

/**
 * @author nazila
 */
@Component
public class PersistenceApi {
    private static final Logger logger = LoggerFactory
            .getLogger(PersistenceApi.class);
    @Autowired
    AuditRepository auditRepository;

    @Autowired
    AccountRepository accountRepository;

    public boolean saveAudit(Audit audit) {
        try {
            auditRepository.saveAudit(audit);
            return true;
        } catch (Exception e) {
            logger.warn("can't save audit {}", audit, e);
            return false;
        }
    }

    public Account saveAccount(String username, String firstName, String lastName, InterfaceType type,
                               String userInterfaceId) {
        try {
            Account result = accountRepository
                    .checkAccountExist(username, type);
            if (result == null) {
                Account account = new Account();
                account.setId(UUID.randomUUID());
                account.setUsername(username);
                account.setFirstName(firstName);
                account.setLastName(lastName);
                account.setType(type);
                account.setUserInterfaceId(userInterfaceId);
                accountRepository.saveAccount(account);
                result = account;
            }
            return result;
        } catch (Exception e) {
            logger.error(
                    "can't find or save account with username {} and type {}",
                    username, type, e);
            return null;
        }
    }
}
