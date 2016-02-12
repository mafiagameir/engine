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

package co.mafiagame.common.domain;

import java.util.Date;
import java.util.UUID;

/**
 * @author hekmatof
 */
public class Audit {
    private UUID id;
    private Account actor;
    private Date date;
    private String roomId;
    private Action action;

    public Audit() {
    }

    public Audit(Account actor, Action action,String roomId) {
        this.id = UUID.randomUUID();
        this.actor = actor;
        this.date = new Date();
        this.action = action;
        this.roomId = roomId;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public Account getActor() {
        return actor;
    }

    public void setActor(Account actor) {
        this.actor = actor;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Action getAction() {
        return action;
    }

    public void setAction(Action action) {
        this.action = action;
    }

    public String getRoomId() {
        return roomId;
    }

    public void setRoomId(String roomId) {
        this.roomId = roomId;
    }

    @Override
    public String toString() {
        return "Audit{" +
                "id=" + id +
                ", actor=" + actor +
                ", date=" + date +
                ", roomId='" + roomId + '\'' +
                ", action=" + action +
                '}';
    }
}
