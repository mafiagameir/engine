/*
 *  Copyright (C) 2015 mafiagame.ir
 *
 *  This program is free software; you can redistribute it and/or
 *  modify it under the terms of the GNU General Public License
 *  as published by the Free Software Foundation; either version 2
 *  of the License, or (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program; if not, write to the Free Software
 *  Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 */

package co.mafiagame.test.persistence.api;

import co.mafiagame.common.domain.Account;
import co.mafiagame.common.domain.Audit;
import co.mafiagame.common.domain.InterfaceType;
import co.mafiagame.common.utils.MessageHolder;
import co.mafiagame.persistence.api.IPersistenceApi;
import org.springframework.stereotype.Component;

import java.util.UUID;

/**
 * @author Esa Hekmatizadeh
 */
@Component
public class TestPersistenceApi implements IPersistenceApi {
    @Override
    public void saveAudit(Audit audit) {

    }

    @Override
    public Account saveAccount(String userName, String firstName, String lastName, InterfaceType interfaceType, String userId) {
        Account account = new Account();
        account.setId(UUID.randomUUID());
        account.setUsername(userName);
        account.setFirstName(firstName);
        account.setLastName(lastName);
        account.setType(interfaceType);
        account.setUserInterfaceId(userId);
        return account;
    }

    @Override
    public MessageHolder.Lang getLang(String userId) {
        return MessageHolder.Lang.EN;
    }

    @Override
    public void setLang(String userId, MessageHolder.Lang lang) {
    }
}
