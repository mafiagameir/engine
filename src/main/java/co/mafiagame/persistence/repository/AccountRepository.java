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

package co.mafiagame.persistence.repository;

import co.mafiagame.common.domain.Account;
import co.mafiagame.common.domain.InterfaceType;
import co.mafiagame.common.utils.MessageHolder;
import co.mafiagame.persistence.rowmapper.AccountRowMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.Objects;

/**
 * @author nazila
 */
@Component
public class AccountRepository {

    @SuppressWarnings("SpringJavaAutowiringInspection")
    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Autowired
    private AccountRowMapper accountRowMapper;

    public void saveAccount(Account account) {
        String sql = "INSERT INTO TBL_ACCOUNT (ID,TYPE,USERNAME,FIRST_NAME,LAST_NAME,USER_INTERFACE_ID) VALUES(?,?,?,?,?,?)";
        jdbcTemplate.update(sql, account.getId(), account.getType().toString(),
                account.getUsername(), account.getFirstName(), account.getLastName(), account.getUserInterfaceId());
    }

    public void setLang(String userId, String lang) {
        jdbcTemplate.update("UPDATE TBL_ACCOUNT SET LANG=? WHERE USER_INTERFACE_ID=?", lang, userId);
    }

    public MessageHolder.Lang getLang(String userId) {
        try {
            String langStr = jdbcTemplate.queryForObject("SELECT LANG FROM TBL_ACCOUNT WHERE USER_INTERFACE_ID=?",
                    String.class, userId);
            if (Objects.isNull(langStr))
                return MessageHolder.Lang.EN;
            return MessageHolder.Lang.valueOf(langStr);
        } catch (IncorrectResultSizeDataAccessException e) {
            return MessageHolder.Lang.EN;
        }
    }

    public Account checkAccountExist(String username, InterfaceType type) {
        String sql = "SELECT * FROM TBL_ACCOUNT WHERE USERNAME=? AND \"type\"=?";
        try {
            return jdbcTemplate.queryForObject(sql, accountRowMapper,
                    username, type.name());
        } catch (Exception e) {
            return null;
        }
    }
}
