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

import co.mafiagame.common.domain.Audit;
import co.mafiagame.persistence.rowmapper.AuditRowMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author nazila
 */
@Component
public class AuditRepository {

    @SuppressWarnings("SpringJavaAutowiringInspection")
    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Autowired
    private AuditRowMapper auditRowMapper;

    public void saveAudit(Audit audit) {
        String sql = "INSERT INTO TBL_AUDIT "
                + "(ID,ACTOR_ID,ROOM_ID,DATE,ACTION) VALUES(?,?,?,?,?)";
        jdbcTemplate.update(sql, audit.getId(), audit.getActor().getId(),
                audit.getRoomId(), audit.getDate(), audit.getAction().name());
    }

    public List<Audit> getAllAudit() {
        String sql = "SELECT u.*,c.TYPE AS account_type,c.USERNAME AS account_username" +
                " FROM TBL_AUDIT AS u JOIN TBL_ACCOUNT AS c ON ACTOR_ID=c.ID";
        return jdbcTemplate.query(sql, auditRowMapper);
    }
}
