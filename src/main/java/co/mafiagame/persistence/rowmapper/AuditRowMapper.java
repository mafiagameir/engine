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

package co.mafiagame.persistence.rowmapper;

import co.mafiagame.common.domain.Account;
import co.mafiagame.common.domain.Action;
import co.mafiagame.common.domain.Audit;
import co.mafiagame.common.domain.InterfaceType;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

/**
 * @author nazila
 */
@Component
public class AuditRowMapper implements RowMapper<Audit> {

    public Audit mapRow(ResultSet rs, int rowNum) throws SQLException {
        Audit audit = new Audit();
        Account account = new Account();
        account.setId(rs.getObject("ACTOR_ID", UUID.class));
        account.setType(InterfaceType.valueOf(rs.getString("account_type")));
        account.setUsername(rs.getString("account_username"));
        audit.setId(rs.getObject("ID", UUID.class));
        audit.setActor(account);
        audit.setRoomId(rs.getString("ROOM_ID"));
        audit.setDate(rs.getDate("DATE"));
        audit.setAction(Action.valueOf(rs.getString("ACTION")));
        return audit;
    }

}
